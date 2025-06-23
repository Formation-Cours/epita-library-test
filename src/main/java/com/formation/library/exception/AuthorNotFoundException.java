package com.formation.library.exception;

public class AuthorNotFoundException extends RuntimeException {
  public AuthorNotFoundException(Long id) {
    super("Auteur non trouvé avec l'ID: " + id);
  }

  public AuthorNotFoundException(String email) {
    super("Auteur non trouvé avec l'email: " + email);
  }
}
