package com.formation.library.dto;

import com.formation.library.entity.Book.BookStatus;
import java.time.LocalDate;

public record BookWithAuthorDTO(
    Long id,
    String title,
    String isbn,
    LocalDate publicationDate,
    Integer pages,
    String genre,
    BookStatus status,
    AuthorDTO author
) {
}