package com.library.controller;

import com.library.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("books")
public class LibraryController {

    private List<Map<String, Object>> library = new ArrayList<Map<String, Object>>(){{
        add(new HashMap<String,Object>(){{ put("id", 1); put("name", "Война и мир"); }});
        add(new HashMap<String,Object>(){{ put("id", 2); put("name", "Преступление и наказание"); }});
        add(new HashMap<String,Object>(){{ put("id", 3); put("name", "Идиот"); }});
        add(new HashMap<String,Object>(){{ put("id", 4); put("name", "Мастер и Маргарита"); }});
        add(new HashMap<String,Object>(){{ put("id", 5); put("name", "Отцы и дети"); }});
    }};

    @GetMapping
    public List<Map<String, Object>> library(){
        return library;
    }

    @GetMapping("{id}")
    public Map<String, Object> getBook(@PathVariable Integer id){
        return getBookById(id);
    }

    private Map<String, Object> getBookById(Integer id){
        return library.stream().filter(library -> library.get("id").equals(id)).findFirst()
            .orElseThrow(NotFoundException :: new);
    }

    @PostMapping
    public Map<String, Object> addBook(@RequestBody Map<String, Object> book){
        int curId = 1;
        if(library != null && !library.isEmpty()){
            int maxId = (Integer) library.get(0).get("id");
            for(Map<String, Object> bk : library){
                int id = (Integer) bk.get("id");
                if(maxId <= id){
                    maxId = id;
                }
            }
            curId = maxId+1;
        }

        book.put("id", curId);
        library.add(book);
        return book;
    }

    @PutMapping("{id}")
    public Map<String, Object> updateBook(@RequestBody Map<String, Object> book, @PathVariable Integer id){
        Map<String, Object> bookFromDb = getBookById(id);
        bookFromDb.putAll(book);
        bookFromDb.put("id", id);
        return bookFromDb;
    }

    @DeleteMapping("{id}")
    public void deleteBook(@PathVariable Integer id){
        Map<String, Object> book = getBookById(id);
        library.remove(book);
    }
}
