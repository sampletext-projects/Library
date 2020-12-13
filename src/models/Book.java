package models;

import database.Database;

public class Book extends Entity {

    private String title;
    private String author;

    public Book(int id, String title, String author) {
        super(id);
        setTitle(title);
        setAuthor(author);
    }

    public Book() {
        super(-1);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public Book erase() {
        super.erase();
        return this;
    }

    @Override
    public Book pull() {
        if (getId() == -1) {
            Database.getBooksDb().insert(this);
        } else {
            Book book = Database.getBooksDb().getById(getId());
            if (book == null) {
                throw new UnsupportedOperationException("This object doesn't exist");
            }
            this.setAuthor(book.getAuthor());
            this.setTitle(book.getTitle());
        }
        return this;
    }

    @Override
    public Book push() {
        super.push();
        return this;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
