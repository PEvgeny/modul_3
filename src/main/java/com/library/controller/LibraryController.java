package com.library.controller;

import com.library.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("books")
public class LibraryController {

    private List<Map<String, String>> library = new ArrayList<Map<String, String>>(){{
        add(new HashMap<String,String>(){{ put("id", "1"); put("name", "Война и мир"); }});
        add(new HashMap<String,String>(){{ put("id", "2"); put("name", "Преступление и наказание"); }});
        add(new HashMap<String,String>(){{ put("id", "3"); put("name", "Идиот"); }});
        add(new HashMap<String,String>(){{ put("id", "4"); put("name", "Мастер и Маргарита"); }});
        add(new HashMap<String,String>(){{ put("id", "5"); put("name", "Отцы и дети"); }});
    }};

    @GetMapping
    public List<Map<String, String>> library(){
        return library;
    }

    @GetMapping("{id}")
    public Map<String, String> getBook(@PathVariable String id){
        return getBookById(id);
    }

    private Map<String, String> getBookById(String id){
        return library.stream().filter(library -> library.get("id").equals(id)).findFirst()
            .orElseThrow(NotFoundException :: new);
    }

    @PostMapping
    public Map<String, String> addBook(@RequestBody Map<String, String> book){
        String curId = "1";
        if(library != null && !library.isEmpty()){
            String maxId = library.get(0).get("id");
            for(Map<String, String> bk : library){
                String id = bk.get("id");
                if(maxId.compareTo(id) <= 0){
                    maxId = id;
                }
            }
            curId = String.valueOf(Integer.parseInt(maxId)+1);
        }

        book.put("id", curId);
        library.add(book);
        return book;
    }

    @PutMapping("{id}")
    public Map<String, String> updateBook(@RequestBody Map<String, String> book, @PathVariable String id){
        Map<String, String> bookFromDb = getBookById(id);
        bookFromDb.putAll(book);
        bookFromDb.put("id", id);
        return bookFromDb;
    }

    @DeleteMapping("{id}")
    public void deleteBook(@PathVariable String id){
        Map<String, String> book = getBookById(id);
        library.remove(book);
    }
}
