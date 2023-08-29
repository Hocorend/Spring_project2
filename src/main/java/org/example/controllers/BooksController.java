package org.example.controllers;

import org.example.dao.BookDAO;
import org.example.dao.PersonDAO;
import org.example.models.Book;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "books/new";

        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookDAO.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@ModelAttribute("person")Person person, @PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.show(id));
        model.addAttribute("people", personDAO.index());

        if (bookDAO.isAvailable(id)) {
            return "books/showAvailable";
        }

        model.addAttribute("personActive", personDAO.show(bookDAO.show(id).getPersonId()));
        return "books/showNotAvailable";
    }

    @PatchMapping("/take/{idBook}")
    public String takeBook(@ModelAttribute("person") Person person, @PathVariable("idBook") int id){
        bookDAO.takeBook(id,person.getId());

        return "redirect:/books";
    }

    @PatchMapping("/give/{idBook}")
    public String giveBook(@PathVariable("idBook") int id){
        bookDAO.giveBook(id);

        return "redirect:/books";
    }

}
