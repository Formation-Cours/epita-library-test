package com.formation.library.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formation.library.entity.Author;
import com.formation.library.exception.AuthorNotFoundException;
import com.formation.library.repository.AuthorRepository;

@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl implements IAuthorService {

  private final AuthorRepository authorRepository;

  public AuthorServiceImpl(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  @Override
  @Transactional(readOnly = false)
  public Author save(Author author) {
    validateAuthor(author);
    return authorRepository.save(author);
  }

  @Override
  public Author findById(Long id) {
    return authorRepository.findById(id)
        .orElseThrow(() -> new AuthorNotFoundException(id));
  }

  @Override
  public Author findByEmail(String email) {
    return authorRepository.findByEmail(email)
        .orElseThrow(() -> new AuthorNotFoundException(email));
  }

  @Override
  public List<Author> findAll() {
    return authorRepository.findAll();
  }

  @Override
  public List<Author> findByName(String name) {
    return authorRepository.findByNameContainingIgnoreCase(name);
  }

  @Override
  public List<Author> findByBirthDateBetween(LocalDate startDate, LocalDate endDate) {
    return authorRepository.findByBirthDateBetween(startDate, endDate);
  }

  @Override
  public List<Author> findByGenre(@NonNull String genre) {
    return authorRepository.findByBooksGenre(genre);
  }

  @Override
  @Transactional(readOnly = false)
  public Author update(Long id, Author author) {
    Author existingAuthor = findById(id);

    existingAuthor.setName(author.getName());
    existingAuthor.setEmail(author.getEmail());
    existingAuthor.setBirthDate(author.getBirthDate());
    existingAuthor.setBiography(author.getBiography());

    return authorRepository.save(existingAuthor);
  }

  @Override
  @Transactional(readOnly = false)
  public void deleteById(Long id) {
    Author author = findById(id);
    authorRepository.delete(author);
  }

  @Override
  public boolean existsByEmail(String email) {
    return authorRepository.existsByEmail(email);
  }

  private void validateAuthor(Author author) {
    if (author.getName() == null ||
        author.getName().isBlank()) {
      throw new IllegalArgumentException("Le nom de l'auteur ne peut pas être vide");
    }

    if (author.getEmail() == null ||
        author.getEmail().isBlank()) {
      throw new IllegalArgumentException("L'email doit être renseigné");
    }

    if (existsByEmail(author.getEmail())) {
      throw new IllegalArgumentException("Un auteur avec cet email existe déjà");
    }
  }
}
