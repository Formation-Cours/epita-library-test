package com.formation.library.exception;

public class BookAlreadyBorrowedException extends RuntimeException {
  public BookAlreadyBorrowedException(String isbn) {
    super("Le livre avec l'ISBN " + isbn + " est déjà emprunté");
  }
}
