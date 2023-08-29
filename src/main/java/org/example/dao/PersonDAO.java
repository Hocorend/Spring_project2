package org.example.dao;

import org.example.models.Book;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index(){
        return jdbcTemplate.query("Select * from Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate.query("select * from person where id=?",new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }
    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(fullname,year) VALUES (?,?)", person.getFullname(), person.getYear());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE PERSON SET fullname=?, year=? where id=?", updatedPerson.getFullname(),updatedPerson.getYear(),updatedPerson.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE from PERSON where id=?", id);
    }

    public boolean hasBooks(int id){
        return jdbcTemplate.query("select * from book where person_id=?",new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().isPresent();
    }

}
