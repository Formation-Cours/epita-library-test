package com.formation.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "books")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Le titre ne peut pas être vide")
  @Column(nullable = false)
  private String title;

  @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:\\d+[- ]){3})[- 0-9X]{13}$|97[89]\\d{10}$|(?=(?:\\d+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?\\d{1,5}[- ]?\\d+[- ]?\\d+[- ]?[0-9X]$", message = "Format ISBN invalide")
  @Column(unique = true, nullable = false)
  private String isbn;

  @Column(name = "publication_date")
  private LocalDate publicationDate;

  @Positive(message = "Le nombre de pages doit être positif")
  private Integer pages;

  private String genre;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Le statut ne peut pas être null")
  private BookStatus status = BookStatus.AVAILABLE;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  @NotNull(message = "L'auteur ne peut pas être null")
  private Author author;

  public enum BookStatus {
    AVAILABLE, BORROWED, RESERVED, MAINTENANCE
  }

  // Constructeurs
  public Book() {
  }

  public Book(String title, String isbn, Author author) {
    this.title = title;
    this.isbn = isbn;
    this.author = author;
  }

  public Book(String title, String isbn, LocalDate publicationDate,
      Integer pages, String genre, Author author) {
    this.title = title;
    this.isbn = isbn;
    this.publicationDate = publicationDate;
    this.pages = pages;
    this.genre = genre;
    this.author = author;
  }

  // Getters et Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public LocalDate getPublicationDate() {
    return publicationDate;
  }

  public void setPublicationDate(LocalDate publicationDate) {
    this.publicationDate = publicationDate;
  }

  public Integer getPages() {
    return pages;
  }

  public void setPages(Integer pages) {
    this.pages = pages;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public BookStatus getStatus() {
    return status;
  }

  public void setStatus(BookStatus status) {
    this.status = status;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  // Méthodes métier
  public boolean isAvailable() {
    return status == BookStatus.AVAILABLE;
  }

  public void borrow() {
    if (!isAvailable()) {
      throw new IllegalStateException("Le livre n'est pas disponible pour l'emprunt");
    }
    this.status = BookStatus.BORROWED;
  }

  public void returnBook() {
    if (status != BookStatus.BORROWED) {
      throw new IllegalStateException("Le livre n'est pas emprunté");
    }
    this.status = BookStatus.AVAILABLE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Book))
      return false;
    Book book = (Book) o;
    return id != null && id.equals(book.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
