package com.formation.library.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public record AuthorDTO(
                Long id,
                String name,
                String email,
                LocalDate birthDate,
                String biography) {
}
