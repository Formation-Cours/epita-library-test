package com.formation.library.exception;

public class BookNotFoundException extends RuntimeException {
  public BookNotFoundException(Long id) {
    super("Livre non trouvé avec l'ID: " + id);
  }

  public BookNotFoundException(String isbn) {
    super("Livre non trouvé avec l'ISBN: " + isbn);
  }
}
