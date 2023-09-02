package org.example.controllers;

import org.example.dao.BookDAO;
import org.example.dao.PersonDAO;
import org.example.models.Book;
import org.example.models.Person;
import org.example.services.BooksService;
import org.example.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    private final PeopleService peopleService;

    private final BooksService booksService;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO, PeopleService peopleService, BooksService booksService) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "sort_by_year",required = false) boolean sort,
                        @RequestParam(value = "page",required = false) Integer page,
                        @RequestParam(value = "books_per_page",required = false) Integer booksPerPage) {

        if (page == null || booksPerPage == null) {
            model.addAttribute("books", booksService.findAll(sort));
        }
        else model.addAttribute("books", booksService.findAll(page,booksPerPage,sort));

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

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id,book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@ModelAttribute("person")Person person, @PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));

        Optional<Book>bookOwner = Optional.ofNullable(booksService.getBookOwner(person));

        if (bookOwner.isPresent()){
            model.addAttribute("owner", bookOwner.get());
        }
        else model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @GetMapping("/search")
    public String search(){
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("title") String title) {
        model.addAttribute("books", booksService.findByTitle(title));
        return "books/search";
    }

    @PatchMapping("/take/{idBook}")
    public String takeBook(@ModelAttribute("person") Person person, @PathVariable("idBook") int id){
        booksService.takeBook(id,person);
        return "redirect:/books";
    }

    @PatchMapping("/give/{idBook}")
    public String giveBook(@PathVariable("idBook") int id){
        booksService.giveBook(id);

        return "redirect:/books";
    }

}
