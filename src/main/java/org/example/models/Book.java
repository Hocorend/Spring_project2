package org.example.models;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@Data
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Title of book should not be empty")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Author should not be empty")
    @Column(name = "author")
    private String author;

    @Column(name = "year_of_release")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy")
    private Date yearOfRelease;

    @Column(name = "day_of_issue")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dayOfIssue;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;
    public Book(){}

    @Transient
    public boolean isOverdue;

    public Book(int id, String title, String author, Date yearOfRelease, Date dayOfIssue, Person owner) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.yearOfRelease = yearOfRelease;
        this.dayOfIssue = dayOfIssue;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", yearOfRelease=" + yearOfRelease +
                ", dayOfIssue=" + dayOfIssue +
                '}';
    }

    public boolean isOverdue(){
        return isOverdue;
    }

    public void setOverdue(boolean overdue){
        this.isOverdue = overdue;
    }
}
