package org.example.services;

import org.example.models.Book;
import org.example.models.Person;
import org.example.repositories.BooksRepository;
import org.example.repositories.PeopleRepository;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findByTitle(String title) {
        return booksRepository.findByTitleIgnoreCaseStartingWith(title);
    }

    public List<Book> findAll(boolean sorted) {
        if (sorted) {
            return booksRepository.findAll(Sort.by("yearOfRelease"));
        }
        return booksRepository.findAll();
    }

    public List<Book> findAll(Integer page, Integer booksPerPage, boolean sorted) {
        if (sorted) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("yearOfRelease"))).getContent();
        }
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Book findOne(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    public Book getBookOwner(Person person) {
        return booksRepository.getBookByOwner(person);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updateBook) {
        updateBook.setId(id);
        booksRepository.save(updateBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void takeBook(int bookId, Person person) {
        booksRepository.findById(bookId).ifPresent(
                book -> {
                    book.setOwner(person);
                    book.setDayOfIssue(new Date()); // текущее время
                }
        );
    }

    @Transactional
    public void giveBook(int id) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setDayOfIssue(null);
                }
        );
    }


}
