package com.formation.library.mapper;

import com.formation.library.dto.AuthorDTO;
import com.formation.library.dto.AuthorWithBooksDTO;
import com.formation.library.dto.BookDTO;
import com.formation.library.dto.BookWithAuthorDTO;
import com.formation.library.entity.Author;
import com.formation.library.entity.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DTOMapper {

    public AuthorDTO toAuthorDTO(Author author) {
        if (author == null) {
            return null;
        }
        return new AuthorDTO(
            author.getId(),
            author.getName(),
            author.getEmail(),
            author.getBirthDate(),
            author.getBiography()
        );
    }

    public BookDTO toBookDTO(Book book) {
        if (book == null) {
            return null;
        }
        return new BookDTO(
            book.getId(),
            book.getTitle(),
            book.getIsbn(),
            book.getPublicationDate(),
            book.getPages(),
            book.getGenre(),
            book.getStatus(),
            book.getAuthor() != null ? book.getAuthor().getId() : null,
            book.getAuthor() != null ? book.getAuthor().getName() : null
        );
    }

    public AuthorWithBooksDTO toAuthorWithBooksDTO(Author author) {
        if (author == null) {
            return null;
        }
        List<BookDTO> bookDTOs = author.getBooks().stream()
            .map(this::toBookDTO)
            .collect(Collectors.toList());
        
        return new AuthorWithBooksDTO(
            author.getId(),
            author.getName(),
            author.getEmail(),
            author.getBirthDate(),
            author.getBiography(),
            bookDTOs
        );
    }

    public BookWithAuthorDTO toBookWithAuthorDTO(Book book) {
        if (book == null) {
            return null;
        }
        return new BookWithAuthorDTO(
            book.getId(),
            book.getTitle(),
            book.getIsbn(),
            book.getPublicationDate(),
            book.getPages(),
            book.getGenre(),
            book.getStatus(),
            toAuthorDTO(book.getAuthor())
        );
    }

    public List<AuthorDTO> toAuthorDTOList(List<Author> authors) {
        return authors.stream()
            .map(this::toAuthorDTO)
            .collect(Collectors.toList());
    }

    public List<BookDTO> toBookDTOList(List<Book> books) {
        return books.stream()
            .map(this::toBookDTO)
            .collect(Collectors.toList());
    }

    public List<AuthorWithBooksDTO> toAuthorWithBooksDTOList(List<Author> authors) {
        return authors.stream()
            .map(this::toAuthorWithBooksDTO)
            .collect(Collectors.toList());
    }

    public List<BookWithAuthorDTO> toBookWithAuthorDTOList(List<Book> books) {
        return books.stream()
            .map(this::toBookWithAuthorDTO)
            .collect(Collectors.toList());
    }

    public Author toAuthorEntity(AuthorDTO authorDTO) {
        if (authorDTO == null) {
            return null;
        }
        Author author = new Author();
        author.setId(authorDTO.id());
        author.setName(authorDTO.name());
        author.setEmail(authorDTO.email());
        author.setBirthDate(authorDTO.birthDate());
        author.setBiography(authorDTO.biography());
        return author;
    }

    public Book toBookEntity(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }
        Book book = new Book();
        book.setId(bookDTO.id());
        book.setTitle(bookDTO.title());
        book.setIsbn(bookDTO.isbn());
        book.setPublicationDate(bookDTO.publicationDate());
        book.setPages(bookDTO.pages());
        book.setGenre(bookDTO.genre());
        book.setStatus(bookDTO.status());
        return book;
    }
}