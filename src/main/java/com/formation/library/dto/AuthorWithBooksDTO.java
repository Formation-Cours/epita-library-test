package com.formation.library.dto;

import java.time.LocalDate;
import java.util.List;

public record AuthorWithBooksDTO(
    Long id,
    String name,
    String email,
    LocalDate birthDate,
    String biography,
    List<BookDTO> books
) {
}