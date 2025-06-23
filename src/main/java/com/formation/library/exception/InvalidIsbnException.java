package com.formation.library.exception;

public class InvalidIsbnException extends RuntimeException {
  public InvalidIsbnException(String isbn) {
    super("ISBN invalide: " + isbn);
  }
}
