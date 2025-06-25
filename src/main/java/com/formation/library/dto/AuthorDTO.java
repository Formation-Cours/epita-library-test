package com.formation.library.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public record AuthorDTO(
                                Long id,
                                String name,
                                String email,
                                @JsonFormat(pattern = "dd/MM/yyyy") LocalDate birthDate,
                                String biography) {
}
