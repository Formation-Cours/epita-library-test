package com.formation.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.formation.library.dto.AuthorWithBooksDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.library.dto.AuthorDTO;
import com.formation.library.entity.Author;
import com.formation.library.exception.AuthorNotFoundException;
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

    private AuthorDTO author1DTO;
    private AuthorDTO author2DTO;

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

        author1DTO = new AuthorDTO(author1.getId(), author1.getName(), author1.getEmail(), author1.getBirthDate(),
                author1.getBiography());

        author2DTO = new AuthorDTO(author2.getId(), author2.getName(), author2.getEmail(), author2.getBirthDate(),
                author2.getBiography());

    }

    @Test
    void shouldCreateValidAuthor() throws Exception {

        when(authorService.save(any(Author.class))).thenReturn(author1);
        when(dtoMapper.toAuthorDTO(author1)).thenReturn(author1DTO);

        mockMvc.perform(
                post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author1)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Victor Hugo"))
                .andExpect(jsonPath("$.email").value("victor.hugo@example.com"))
                .andExpect(jsonPath("$.birthDate").value("26/02/1802"));

        verify(authorService).save(any(Author.class));
    }

    @Test
    void shouldRejectAuthorWithNullName() throws Exception {
        Author invalidAuthor = new Author(null, "victor.hugo@example.com");

        mockMvc.perform(
                post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAuthor)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errors.name").value("Le nom doit être renseigné"));

        verifyNoInteractions(authorService);
    }

    @Test
    void shouldGetAuthorID() throws Exception {

        AuthorWithBooksDTO dto = new AuthorWithBooksDTO(
                author1.getId(),
                author1.getName(),
                author1.getEmail(),
                author1.getBirthDate(),
                author1.getBiography(),
                List.of());

        when(authorService.findById(1L)).thenReturn(author1);
        when(dtoMapper.toAuthorWithBooksDTO(author1)).thenReturn(dto);

        mockMvc.perform(
                get("/api/authors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Victor Hugo"))
                .andExpect(jsonPath("$.email").value("victor.hugo@example.com"))
                .andExpect(jsonPath("$.birthDate").value("1802-02-26"));

        verify(authorService).findById(1L);
    }

    @Test
    void shouldReturn404ForNonExistingAuthor() throws Exception {

        when(authorService.findById(999L)).thenThrow(new AuthorNotFoundException(999L));

        mockMvc.perform(
                get("/api/authors/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.message").value("Auteur non trouvé avec l'ID: 999"));

        verify(authorService).findById(999L);
    }

    @Test
    void shouldGetAllAuthors() throws Exception {
        List<Author> authors = List.of(author1, author2);
        List<AuthorDTO> authorsDTO = List.of(author1DTO, author2DTO);

        when(authorService.findAll()).thenReturn(authors);
        when(dtoMapper.toAuthorDTOList(authors)).thenReturn(authorsDTO);

        mockMvc.perform(
                get("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Victor Hugo"))
                .andExpect(jsonPath("$[1].name").value("Émile Zola"));

        verify(authorService).findAll();
        verifyNoMoreInteractions(authorService);
    }

    @Test
    void shouldSearchAuthors() throws Exception {
        List<Author> authors = List.of(author1);
        List<AuthorDTO> authorsDTO = List.of(author1DTO);

        when(authorService.findByName("Victor")).thenReturn(authors);
        when(dtoMapper.toAuthorDTOList(authors)).thenReturn(authorsDTO);

        mockMvc.perform(
                get("/api/authors/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "Victor"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Victor Hugo"));

        verify(authorService).findByName("Victor");
    }
}
