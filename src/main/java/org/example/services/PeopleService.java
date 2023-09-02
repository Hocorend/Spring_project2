package org.example.services;

import org.example.models.Book;
import org.example.models.Person;
import org.example.repositories.BooksRepository;
import org.example.repositories.PeopleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    private final BooksRepository booksRepository;

    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findOne(int id){
        return peopleRepository.findById(id).orElse(null);
    }

    public List<Book> getBooksByPerson(Person person){
        List<Book>books = booksRepository.findByOwner(person);
        if (!books.isEmpty()){
            books.forEach(book -> {
                Calendar today = GregorianCalendar.getInstance();
                today.setTime(new Date());
                today.add(Calendar.DATE,-10);
                Calendar dayOfIssue = GregorianCalendar.getInstance();
                dayOfIssue.setTime(book.getDayOfIssue());
                if (dayOfIssue.compareTo(today) < 0){
                    book.setOverdue(true);
                }
                System.out.println("Result: "+ dayOfIssue.compareTo(today));
            });
        }
        return books;
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id,Person updatePerson){
        updatePerson.setId(id);
        peopleRepository.save(updatePerson);
    }

    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }
}
