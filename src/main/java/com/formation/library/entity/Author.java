package com.formation.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Le nom doit être renseigné")
  @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
  @Column(nullable = false)
  private String name;

  @Email(message = "Format d'email invalide")
  @Column(unique = true)
  private String email;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Size(max = 500, message = "La biographie ne peut pas dépasser 500 caractères")
  private String biography;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Book> books = new ArrayList<>();

  public Author() {
  }

  public Author(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public Author(String name, String email, LocalDate birthDate, String biography) {
    this.name = name;
    this.email = email;
    this.birthDate = birthDate;
    this.biography = biography;
  }

  // Getters et Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getBiography() {
    return biography;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public List<Book> getBooks() {
    return books;
  }

  public void setBooks(List<Book> books) {
    this.books = books;
  }

  // Méthodes utilitaires
  public void addBook(Book book) {
    books.add(book);
    book.setAuthor(this);
  }

  public void removeBook(Book book) {
    books.remove(book);
    book.setAuthor(null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Author))
      return false;
    Author author = (Author) o;
    return id != null &&
        id.equals(author.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
