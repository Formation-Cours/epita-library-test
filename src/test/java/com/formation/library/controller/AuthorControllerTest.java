package com.formation.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.library.dto.AuthorDTO;
import com.formation.library.entity.Author;
import com.formation.library.mapper.DTOMapper;
import com.formation.library.service.IAuthorService;

@WebMvcTest(AuthorController.class)
@DisplayName("Tests du controlleur Author")
public class AuthorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private IAuthorService authorService;

  @MockitoBean
  private DTOMapper dtoMapper;

  @Autowired
  private ObjectMapper objectMapper;

  private Author author1;
  private Author author2;

  @BeforeEach
  void setUp() {
    author1 = new Author("Victor Hugo", "victor.hugo@example.com");
    author1.setId(1L);
    author1.setBirthDate(LocalDate.of(1802, 2, 26));
    author1.setBiography("Écrivain français du XIXe siècle");

    author2 = new Author("Émile Zola", "emile.zola@example.com");
    author2.setId(2L);
    author2.setBirthDate(LocalDate.of(1840, 4, 2));
    author2.setBiography("Écrivain français, chef de file du naturalisme");
  }

  @Test
  void shouldCreateValidAuthor() throws Exception {

    AuthorDTO authorDTO = new AuthorDTO(author1.getId(), author1.getName(), author1.getEmail(), author1.getBirthDate(),
        author1.getBiography());

    when(authorService.save(any(Author.class))).thenReturn(author1);
    when(dtoMapper.toAuthorDTO(author1)).thenReturn(authorDTO);

    mockMvc.perform(
        post("/api/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(author1)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));

  }

}
