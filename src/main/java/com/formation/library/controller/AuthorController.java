package com.formation.library.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formation.library.dto.AuthorDTO;
import com.formation.library.dto.AuthorWithBooksDTO;
import com.formation.library.entity.Author;
import com.formation.library.mapper.DTOMapper;
import com.formation.library.service.IAuthorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/authors")
@CrossOrigin(origins = "*")
public class AuthorController {

  private final IAuthorService authorService;
  private final DTOMapper dtoMapper;

  public AuthorController(IAuthorService authorService, DTOMapper dtoMapper) {
    this.authorService = authorService;
    this.dtoMapper = dtoMapper;
  }

  @PostMapping
  public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody Author author) {
    Author savedAuthor = authorService.save(author);
    AuthorDTO authorDTO = dtoMapper.toAuthorDTO(savedAuthor);
    return new ResponseEntity<>(authorDTO, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AuthorWithBooksDTO> getAuthor(@PathVariable Long id) {
    Author author = authorService.findById(id);
    AuthorWithBooksDTO authorDTO = dtoMapper.toAuthorWithBooksDTO(author);
    return ResponseEntity.ok(authorDTO);
  }

  @GetMapping
  public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
    List<Author> authors = authorService.findAll();
    List<AuthorDTO> authorDTOs = dtoMapper.toAuthorDTOList(authors);
    return ResponseEntity.ok(authorDTOs);
  }

  @GetMapping("/search")
  public ResponseEntity<List<AuthorDTO>> searchAuthors(@RequestParam String name) {
    List<Author> authors = authorService.findByName(name);
    List<AuthorDTO> authorDTOs = dtoMapper.toAuthorDTOList(authors);
    return ResponseEntity.ok(authorDTOs);
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<AuthorDTO> getAuthorByEmail(@PathVariable String email) {
    Author author = authorService.findByEmail(email);
    AuthorDTO authorDTO = dtoMapper.toAuthorDTO(author);
    return ResponseEntity.ok(authorDTO);
  }

  @GetMapping("/birthdate")
  public ResponseEntity<List<AuthorDTO>> getAuthorsByBirthDate(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    List<Author> authors = authorService.findByBirthDateBetween(startDate, endDate);
    List<AuthorDTO> authorDTOs = dtoMapper.toAuthorDTOList(authors);
    return ResponseEntity.ok(authorDTOs);
  }

  @GetMapping("/genre/{genre}")
  public ResponseEntity<List<AuthorDTO>> getAuthorsByGenre(@PathVariable String genre) {
    List<Author> authors = authorService.findByGenre(genre);
    List<AuthorDTO> authorDTOs = dtoMapper.toAuthorDTOList(authors);
    return ResponseEntity.ok(authorDTOs);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @Valid @RequestBody Author author) {
    Author updatedAuthor = authorService.update(id, author);
    AuthorDTO authorDTO = dtoMapper.toAuthorDTO(updatedAuthor);
    return ResponseEntity.ok(authorDTO);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
    authorService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
