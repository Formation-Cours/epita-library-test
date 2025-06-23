package com.formation.library.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.formation.library.entity.Book;
import com.formation.library.entity.Book.BookStatus;

public interface BookRepository extends JpaRepository<Book, Long> {

  Optional<Book> findByIsbn(String isbn);

  List<Book> findByTitleContainingIgnoreCase(String title);

  List<Book> findByStatus(BookStatus status);

  List<Book> findByGenreIgnoreCase(String genre);

  List<Book> findByAuthorId(Long authorId);

  @Query("SELECT b FROM Book b WHERE b.publicationDate BETWEEN :startDate AND :endDate")
  List<Book> findByPublicationDateBetween(@Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  @Query("SELECT b FROM Book b WHERE b.author.name LIKE %:authorName%")
  List<Book> findByAuthorNameContaining(@Param("authorName") String authorName);

  @Query("SELECT COUNT(b) FROM Book b WHERE b.author.id = :authorId AND b.status = :status")
  long countByAuthorIdAndStatus(@Param("authorId") Long authorId, @Param("status") BookStatus status);

  boolean existsByIsbn(String isbn);
}
