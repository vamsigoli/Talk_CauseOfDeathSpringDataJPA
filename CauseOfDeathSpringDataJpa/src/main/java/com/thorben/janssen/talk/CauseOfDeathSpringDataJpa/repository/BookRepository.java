package com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.repository;

import com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.dto.BookAuthorName;
import com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.dto.BookAuthorReview;
import com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.model.Book;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
            SELECT b 
            FROM Book b 
                LEFT JOIN FETCH b.authors a 
                LEFT JOIN FETCH b.reviews r 
            WHERE b.id = :bookId""")
    Book findBookWithAuthorsAndReviews(Long bookId);

    @Query("""
            SELECT b.title as title, a.firstName as firstName, a.lastName as lastName, count(r.id) as reviewCount
            FROM Book b
                LEFT JOIN b.authors a
                LEFT JOIN b.reviews r
            WHERE b.id = :bookId
            GROUP BY b.title, a.firstName, a.lastName""")
    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    List<BookAuthorReview> findBookWithAuthorsAndReviews_DTO(Long bookId);

    List<BookAuthorName> findBookAuthorNameById(Long id);

    Slice<Book> findAllBooksBy(Pageable page);

    @Query("""
        SELECT b 
        FROM Book b 
            LEFT JOIN FETCH b.authors
            LEFT JOIN b.authors a
        WHERE a.id = :authorId""")
    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    List<Book> findBookWithAuthorsByAuthorId(Long authorId);
}
