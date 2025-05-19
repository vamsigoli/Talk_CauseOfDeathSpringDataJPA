package com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.controller;

import com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.repository.BookRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class BookController {

    private BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

}
