package com.formation.library.repository;

import com.formation.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

  Optional<Author> findByEmail(String email);

  List<Author> findByNameContainingIgnoreCase(String name);

  @Query("SELECT a FROM Author a WHERE a.birthDate BETWEEN :startDate AND :endDate")
  List<Author> findByBirthDateBetween(@Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  @Query("SELECT a FROM Author a JOIN a.books b WHERE b.genre = :genre")
  List<Author> findByBooksGenre(@Param("genre") String genre);

  boolean existsByEmail(String email);
}
