package models;

import database.Database;

import javax.xml.crypto.Data;

public class BookOrder extends Entity {

    private int bookId;
    private Book book;

    private int userId;
    private User user;

    public BookOrder(int id) {
        super(id);
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
    public String toString() {
        return "BookOrder{" +
                "bookId=" + bookId +
                ", userId=" + userId +
                '}';
    }
}
