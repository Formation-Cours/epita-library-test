package com.formation.library.service;

import java.time.LocalDate;
import java.util.List;

import com.formation.library.entity.Author;

public interface IAuthorService {

  Author save(Author author);

  Author findById(Long id);

  Author findByEmail(String email);

  List<Author> findAll();

  List<Author> findByName(String name);

  List<Author> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);

  List<Author> findByGenre(String genre);

  Author update(Long id, Author author);

  void deleteById(Long id);

  boolean existsByEmail(String email);
}
