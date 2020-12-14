package models;

import database.Database;

import javax.xml.crypto.Data;

public class BookOrder extends Entity {

    private int bookId;
    private Book book;

    private int userId;
    private User user;

    public BookOrder(int id, int bookId, int userId) {
        super(id);
        setUserId(userId);
        setBookId(bookId);
    }

    public BookOrder() {
        super(-1);
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
        this.book = Database.getBooksDb().getById(bookId);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        this.user = Database.getUsersDb().getById(userId);
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    @Override
    public BookOrder push() {
        super.push();
        return this;
    }

    @Override
    public BookOrder pull() {
        super.pull();
        return this;
    }

    @Override
    public BookOrder erase() {
        super.erase();
        return this;
    }

    @Override
    public String toString() {
        return "BookOrder{" +
                "bookId=" + bookId +
                ", userId=" + userId +
                '}';
    }
}
