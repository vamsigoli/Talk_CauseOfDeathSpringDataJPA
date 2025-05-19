package com.thorben.janssen.talk.CauseOfDeathSpringDataJpa;

import com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.dto.BookAuthorName;
import com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.dto.BookAuthorReview;
import com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.model.Author;
import com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.model.Book;
import com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.repository.AuthorRepository;
import com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.repository.BookRepository;

import jakarta.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class TestExamples {

	Logger log = LogManager.getLogger(this.getClass().getName());

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private EntityManager em;

	@Test
	public void readOneAuthorWithBooks() {
		log.info("... readOneAuthorWithBooks ...");

//		Author a = this.authorRepository.findById(1L).orElseThrow();
		Author a = this.authorRepository.findAuthorWithBooks(1L);

		log.info("Author " + a.getFirstName() + " " + a.getLastName() + " wrote "
				+ a.getBooks().stream().map(b -> b.getTitle()).collect(Collectors.joining(", ")));
	}

	@Test
	public void readAllAuthors() {
		log.info("... readAllAuthors ...");

		List<Author> authors = authorRepository.findAll();

		for (Author a : authors) {
			log.info("Author " + a.getFirstName() + " " + a.getLastName());
		}
	}

	@Test
	@Transactional
	@Commit
	public void removeAuthorFromBook() {
		log.info("... removeAuthorFromBook ...");

		Book b = bookRepository.findById(6L).orElseThrow();
		b.getAuthors().remove(b.getAuthors().toArray()[0]);
	}

	@Test
	public void readBookWithAuthorsAndReviews() {
		log.info("... readBookWithAuthorsAndReviews ...");

		Book b = bookRepository.findBookWithAuthorsAndReviews(2L);

		log.info("Book "
				+ b.getTitle() + " was written by " + b.getAuthors().stream()
						.map(a -> a.getFirstName() + " " + a.getLastName()).collect(Collectors.joining(", "))
				+ " and got " + b.getReviews().size() + " reviews");

	}

	@Test
	@Transactional
	public void readBookAuthorName() {
		log.info("... readBookAuthorName ...");

		List<BookAuthorName> books = bookRepository.findBookAuthorNameById(2L);

		books.forEach(b -> log.info("Book " + b.getTitle() + " was written by " + b.getAuthors()));

	}

	@Test
	public void readBookWithAuthorsAndReviewsDtos() {
		log.info("... readBookWithAuthorsAndReviewsDtos ...");

		List<BookAuthorReview> books = bookRepository.findBookWithAuthorsAndReviews_DTO(2L);

		books.forEach(b ->
			log.info("Book " + b.getTitle()
					+ " was written by " + b.getFirstName() + " " + b.getLastName()
					+ " and got " + b.getReviewCount() + " reviews"));

	}

//	@Test
//	public void cacheBookWithAuthorsAndReviews() {
//		log.info("... cacheBookWithAuthorsAndReviews ...");
//
//		// 1st iteration - Execute query and store result in cache
//		EntityManager em = emf.createEntityManager();
//		em.getTransaction().begin();
//
//		TypedQuery<Book> q = em.createQuery(
//				"SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors a LEFT JOIN FETCH b.reviews r",
//				Book.class);
//		q.setHint(AvailableHints.HINT_CACHEABLE, true);
//		List<Book> books = q.getResultList();
//
//		em.getTransaction().commit();
//		em.close();
//
//		log.info("2nd iteration");
//
//		// 2nd iteration - Query result should be cached
//		em = emf.createEntityManager();
//		em.getTransaction().begin();
//
//		q = em.createQuery(
//				"SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors a LEFT JOIN FETCH b.reviews r",
//				Book.class);
//		q.setHint(AvailableHints.HINT_CACHEABLE, true);
//		books = q.getResultList();
//		log.info("Initialize associations");
//		for (Book b : books) {
//			Hibernate.initialize(b.getAuthors());
//			Hibernate.initialize(b.getReviews());
//		}
//
//		em.getTransaction().commit();
//		em.close();
//
//		for (Book b : books) {
//			log.info("Book " + b.getTitle()
//				+ " was written by "
//				+ b.getAuthors().stream().map(a -> a.getFirstName()+" "+a.getLastName()).collect(Collectors.joining(", "))
//				+ " and got " + b.getReviews().size()+" reviews");
//		}
//
//	}
//
//	@Test
//	public void cacheBookWithAuthorsAndReviewsDtos() {
//		log.info("... cacheBookWithAuthorsAndReviewsDtos ...");
//
//		// 1st iteration - Execute query and store result in cache
//		EntityManager em = emf.createEntityManager();
//		em.getTransaction().begin();
//
//		TypedQuery<BookAuthorReview> q = em.createQuery(
//				"SELECT new com.thorben.janssen.causeOfDeathSpringDataJpa.dto.BookAuthorReview(b.title, CAST(STRING_AGG(a.firstName || ' ' || a.lastName, ', ') as text), count(r.id)) FROM Book b LEFT JOIN b.authors a LEFT JOIN b.reviews r GROUP BY b.title",
//				BookAuthorReview.class);
//		q.setHint(AvailableHints.HINT_CACHEABLE, true);
//		List<BookAuthorReview> books = q.getResultList();
//
//		em.getTransaction().commit();
//		em.close();
//
//		// 2nd iteration - Query result should be cached
//        em = emf.createEntityManager();
//		em.getTransaction().begin();
//
//		q = em.createQuery(
//				"SELECT new com.thorben.janssen.causeOfDeathSpringDataJpa.dto.BookAuthorReview(b.title, CAST(STRING_AGG(a.firstName || ' ' || a.lastName, ', ') as text), count(r.id)) FROM Book b LEFT JOIN b.authors a LEFT JOIN b.reviews r GROUP BY b.title",
//				BookAuthorReview.class);
//		q.setHint(AvailableHints.HINT_CACHEABLE, true);
//		books = q.getResultList();
//
//		em.getTransaction().commit();
//		em.close();
//
//		for (BookAuthorReview b : books) {
//			log.info("Book " + b.getTitle() + " was written by " + b.getAuthorNames() + " and got " + b.getReviewCount()
//					+ " reviews");
//		}
//	}
//
	private void logBooks(EntityManager em) {
		log.info("##########  Books  ################");
		List<Book> books = bookRepository.findAll();
		for (Book b : books) {
			log.info(b);
		}
		em.clear();
	}

	private void logBooksOfAuthor2(EntityManager em) {
		log.info("##########  Books  ################");
		List<Book> books = bookRepository.findBookWithAuthors(2L);
		for (Book b : books) {
			log.info(b + " was written by " + b.getAuthors().stream().map(a -> a.getId().toString()).collect(Collectors.joining(", ")));
		}
		em.clear();
	}

	//
	// DATA INCONSISTENCIES
	//

	@Test
	public void readAuthorsOfBook2() {
		log.info("... readAuthorsOfBook2 ...");
		
		List<Author> authors = authorRepository.findAuthorOfBooks(2L);

		for (Author a : authors) {
			log.info("Author " + a.getFirstName() + " " + a.getLastName() + " wrote "
					+ a.getBooks().stream().map(b -> b.getTitle()).collect(Collectors.joining(", ")));
		}
	}

	@Test
	@Transactional
	@Commit
	public void jpqlUpdate() {
		log.info("... jpqlUpdate ...");
		
		List<Author> authors = authorRepository.findAll();

		authorRepository.updateAuthorNames();

		authors = authorRepository.findAll();	

		for (Author a : authors) {
			log.info("Author " + a.getFirstName() + " " + a.getLastName());
		}
	}

	@Test
	@Transactional
	@Commit
	public void manageBidirectionalAssociations() {
		log.info("... manageBidirectionalAssociations ...");
		
		Author a = authorRepository.findById(1L).orElseThrow();
		Book b = bookRepository.findById(1L).orElseThrow();

        a.getBooks().remove(b);
		// b.getAuthors().remove(a);
		// b.removeAuthor(a);

		log.info("Author "+a.getFirstName()+" "+a.getLastName()+" wrote "+
			a.getBooks().stream().map(book -> book.getTitle()).collect(Collectors.joining(", ")));

		log.info("The book "+b.getTitle()+" was written by "+
			b.getAuthors().stream().map(author -> author.getFirstName()+" "+a.getLastName()).collect(Collectors.joining(", ")));
	}

	// @Test
	// public void openSessionInView() {
	// 	log.info("... openSessionInView ...");
	// 	EntityManager em1 = emf.createEntityManager();
	// 	em1.getTransaction().begin();

	// 	Object[] r = (Object[]) em1.createQuery("SELECT a, count(b.id) FROM Author a JOIN a.books b WHERE a.id=1 GROUP BY a.id").getSingleResult();
	// 	Author a = (Author) r[0];
	// 	long bookCount = (long) r[1];

	// 	parallelTransaction();

	// 	em1.getTransaction().commit();
	// 	log.info("Transaction committed");

	// 	log.info("Author "+a.getFirstName()+" "+a.getLastName()+" wrote "+bookCount+" books.");
	// 	log.info("These are: "+a.getBooks().stream().map(book -> book.getTitle()).collect(Collectors.joining(", ")));

	// 	em1.close();
	// }
//
//	private void parallelTransaction() {
//		EntityManager em = emf.createEntityManager();
//		em.getTransaction().begin();
//
//		Author a = em.find(Author.class, 1L);
//		Book b = em.find(Book.class, 1L);
//
//		a.getBooks().remove(b);
//		b.getAuthors().remove(a);
//
//		em.getTransaction().commit();
//		em.close();
//	}
//
	//
	// LOST DATA
	//

	@Test
	@Transactional
	public void clearWithoutFlush() {
		log.info("... clearWithoutFlush ...");
		
		Slice<Book> books = bookRepository.findAllBooksBy(PageRequest.of(0, 5, Sort.by("id")));
		boolean loop = true;
		while(loop) {
			int i = 0;
			for (Book b : books) {
				b.setTitle(b.getTitle().toUpperCase());

				i++;
				if (i%5==0) {
					// em.flush();
					em.clear();
				}
			}
			if (books.hasNext()) {
				books = bookRepository.findAllBooksBy(books.nextPageable());
			} else {
				loop = false;
			}
		}

		List<Book> bs = em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
		for (Book b : bs) {
			log.info(b.getTitle());
		}
	}

	@Test
	@Transactional
	@Commit
	public void removeAuthorsAndBooks() {
		log.info("... removeAuthorsAndBooks ...");
		
		log.info("Before removing Author 1");
		logBooks(em);
		// logBooksOfAuthor2(em);

		Author a = authorRepository.findById(1L).orElseThrow();
		em.remove(a);

		log.info("After removing Author 1");
		logBooks(em);
		// logBooksOfAuthor2(em);
	}
}
