package com.formation.library.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.BDDMockito.Then;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import com.formation.library.entity.Author;
import com.formation.library.exception.AuthorNotFoundException;
import com.formation.library.repository.AuthorRepository;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {

  @Mock
  private AuthorRepository authorRepository;

  @InjectMocks
  private AuthorServiceImpl authorService;

  private Author author;

  @BeforeEach
  void setUp() {
    author = new Author("John Doe", "john@example.com");
    author.setId(1L);
  }

  @Test
  void shouldSaveValidAuthor() {
    when(authorRepository.existsByEmail(author.getEmail())).thenReturn(false);
    when(authorRepository.save(author)).thenReturn(author);

    Author savedAuthor = authorService.save(author);

    assertThat(savedAuthor).isEqualTo(author);

    verify(authorRepository).existsByEmail(author.getEmail());
    verify(authorRepository).save(author);
  }

  @Test
  void shouldThrowExceptionWhenSavingAuthorWithNullName() {
    author.setName(null);

    assertThatThrownBy(() -> authorService.save(author))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Le nom de l'auteur ne peut pas être vide");

    // verify(authorRepository, never()).save(author);
    verifyNoInteractions(authorRepository);
  }

  @Test
  void shouldThrowExceptionWhenSavingAuthorWithEmptyName() {
    author.setName("    ");

    assertThatThrownBy(() -> authorService.save(author))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Le nom de l'auteur ne peut pas être vide");

    verifyNoInteractions(authorRepository);
  }

  @Test
  void shouldThrowExceptionWhenSavingAuthorWithNullEmail() {
    author.setEmail(null);

    assertThatThrownBy(() -> authorService.save(author))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("L'email doit être renseigné");

    verifyNoInteractions(authorRepository);
  }

  @Test
  void shouldThrowExceptionWhenSavingAuthorWithEmptyEmail() {
    author.setEmail("     ");

    assertThatThrownBy(() -> authorService.save(author))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("L'email doit être renseigné");

    verifyNoInteractions(authorRepository);
  }

  @Test
  void shouldThrowExceptionWhenSavingAuthorWithExistingEmail() {
    when(authorRepository.existsByEmail(author.getEmail())).thenReturn(true);

    assertThatThrownBy(() -> authorService.save(author))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Un auteur avec cet email existe déjà");

    verify(authorRepository).existsByEmail(author.getEmail());
    verifyNoMoreInteractions(authorRepository);
  }

  @Test
  void shouldFindAuthorById() {
    when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

    Author foundAuthor = authorService.findById(1L);

    assertThat(foundAuthor).isEqualTo(author);

    verify(authorRepository).findById(1L);
    verifyNoMoreInteractions(authorRepository);
  }

  @Test
  void shouldThrowExceptionWhenAuthorNotFoundById() {
    when(authorRepository.findById(author.getId())).thenReturn(Optional.empty());
    
    assertThatThrownBy(() -> authorService.findById(1L))
        .isInstanceOf(AuthorNotFoundException.class)
        .hasMessage("Auteur non trouvé avec l'ID: 1");

    verify(authorRepository).findById(1L);
    verifyNoMoreInteractions(authorRepository);
  }

  @Test
  void shouldFindAuthorByEmail() {
    when(authorRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));

    Author foundAuthor = authorService.findByEmail("john@example.com");

    assertThat(foundAuthor).isEqualTo(author);

    verify(authorRepository).findByEmail("john@example.com");
    verifyNoMoreInteractions(authorRepository);
  }

  @Test
  void shouldThrowExceptionWhenAuthorNotFoundByEmail() {
    when(authorRepository.findByEmail(author.getEmail())).thenReturn(Optional.empty());
    
    assertThatThrownBy(() -> authorService.findByEmail("john@example.com"))
        .isInstanceOf(AuthorNotFoundException.class)
        .hasMessage("Auteur non trouvé avec l'email: john@example.com");

    verify(authorRepository).findByEmail("john@example.com");
    verifyNoMoreInteractions(authorRepository);
  }

  @Test
  void shouldFindAllAuthors() {
    List<Author> authors = List.of(author, new Author("Jane Doe", "jane@example.com"));
    when(authorRepository.findAll()).thenReturn(authors);

    List<Author> foundAuthors = authorService.findAll();

    assertThat(foundAuthors)
      .hasSize(2)
      .contains(author);

    verify(authorRepository).findAll();
    verifyNoMoreInteractions(authorRepository);
  }

  @Test
  void shouldFindAuthorsByName() {
    List<Author> authors = List.of(author);
    when(authorRepository.findByNameContainingIgnoreCase("john")).thenReturn(authors);

    List<Author> foundAuthors = authorService.findByName("john");

    assertThat(foundAuthors)
      .hasSize(1)
      .contains(author);

    verify(authorRepository).findByNameContainingIgnoreCase("john");
    verifyNoMoreInteractions(authorRepository);
  }


}
