package com.formation.library.dto;

import java.time.LocalDate;

public record AuthorDTO(
    Long id,
    String name,
    String email,
    LocalDate birthDate,
    String biography
) {
}