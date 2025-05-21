package com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.repository;

import com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.model.Author;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a  FROM Author a LEFT JOIN FETCH a.books WHERE a.id = :authorId")
    Author findAuthorWithBooks(Long authorId);

    @Query("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.books b WHERE b.id = :bookId")
    // @Query("SELECT DISTINCT a FROM Author a LEFT JOIN a.books b LEFT JOIN FETCH a.books WHERE b.id = :bookId")
    List<Author> findAuthorOfBooks(Long bookId);

    @Query("UPDATE Author a SET a.firstName = upper(a.firstName), a.lastName = upper(a.lastName)")
    @Modifying
    void updateAuthorNames();
}
