package org.example.dao;


import org.example.models.Book;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index(){
        return jdbcTemplate.query("Select * from Book", new BeanPropertyRowMapper<>(Book.class));
    }

    public List<Book> index(int personId){
        return jdbcTemplate.query("Select * from Book where person_id=?",new Object[]{personId}, new BeanPropertyRowMapper<>(Book.class));
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book(name,author,year) VALUES (?,?,?)",book.getName(), book.getAuthor(), book.getYear());
    }

    public Book show(int id) {
        return jdbcTemplate.query("select * from Book where id=?",new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public boolean isAvailable(int id){
        Book book = jdbcTemplate.query("select * from book where id=?",new Object[]{id}, new BeanPropertyRowMapper<>(Book.class)).stream().collect(Collectors.toList()).get(0);

        if (book.getPersonId()==0)return true;
        else return false;
    }

    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE Book SET name=?,author=?,year =? where id=?" , updatedBook.getName(),updatedBook.getAuthor(),updatedBook.getYear(),updatedBook.getId());
    }

    public void takeBook(int idBook,int idPerson) {
        jdbcTemplate.update("UPDATE Book SET person_id=? where id=?",idPerson,idBook);
    }
    public void giveBook(int idBook) {
        jdbcTemplate.update("UPDATE Book SET person_id=0 where id=?",idBook);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE from BOOK where id=?", id);
    }
}
