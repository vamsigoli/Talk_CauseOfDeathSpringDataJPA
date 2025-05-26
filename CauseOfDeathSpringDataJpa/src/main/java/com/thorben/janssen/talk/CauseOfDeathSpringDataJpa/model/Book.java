package com.thorben.janssen.talk.CauseOfDeathSpringDataJpa.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Cacheable
public class Book {

	@Id
	@GeneratedValue
	private Long id;

	@Version
	private int version;

	private String title;

	@ManyToMany(cascade = CascadeType.ALL)
//    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
//	private List<Author> authors = new ArrayList<>();
	 private Set<Author> authors = new HashSet<Author>();

	@OneToMany(mappedBy = "book"
		// , fetch = FetchType.EAGER
	)
//	 @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
//	private List<Review> reviews = new ArrayList<>();
	 private Set<Review> reviews = new HashSet<>();

	public Long getId() {
		return this.id;
	}

	public int getVersion() {
		return this.version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

//	public List<Author> getAuthors() {
//		return authors;
//	}
//
//	public void setAuthors(List<Author> authors) {
//		this.authors = authors;
//	}

	 public Set<Author> getAuthors() {
	 	return authors;
	 }

	 public void setAuthors(Set<Author> authors) {
	 	this.authors = authors;
	 }

	public void addAuthor(Author a) {
		this.authors.add(a);
		a.getBooks().add(this);
	}

	public void removeAuthor(Author a) {
		this.authors.remove(a);
		a.getBooks().remove(this);
	}

//	public List<Review> getReviews() {
//		return reviews;
//	}
//
//	public void setReviews(List<Review> reviews) {
//		this.reviews = reviews;
//	}

	 public Set<Review> getReviews() {
	 	return reviews;
	 }

	 public void setReviews(Set<Review> reviews) {
	 	this.reviews = reviews;
	 }

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Book)) {
			return false;
		}
		Book other = (Book) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (title != null && !title.trim().isEmpty())
			result += "title: " + title;
		return result;
	}

}