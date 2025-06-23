package com.formation.library.dto;

import com.formation.library.entity.Book.BookStatus;
import java.time.LocalDate;

public record BookDTO(
    Long id,
    String title,
    String isbn,
    LocalDate publicationDate,
    Integer pages,
    String genre,
    BookStatus status,
    Long authorId,
    String authorName
) {
}