package models;

import database.BookStorages;
import database.Database;

import java.util.List;

public class BookStorage extends Entity {

    private int ownerId;
    private User owner;

    private List<Book> books;
    private List<Integer> booksIds;


    public BookStorage(int id, List<Integer> entities_ids) {
        super(id);
        setBooksIds(entities_ids);
    }

    public BookStorage() {
        super(-1);
    }

    public List<Integer> getBooksIds() {
        return booksIds;
    }

    public void setBooksIds(List<Integer> booksIds) {
        this.booksIds = booksIds;
        this.books = Database.getBooksDb().select(t -> booksIds.contains(t.getId()));
    }

    public List<Book> getBooks() {
        return books;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        owner = Database.getUsersDb().getById(ownerId);
    }

    public User getOwner() {
        return owner;
    }

    @Override
    public BookStorage pull() {
        if (getId() == -1) {
            Database.getBookStoragesDb().insert(this);
        } else {
            BookStorage storage = Database.getBookStoragesDb().getById(getId());
            if (storage == null) {
                throw new UnsupportedOperationException("This object doesn't exist");
            }
            this.setBooksIds(storage.booksIds);
        }
        return this;
    }

    @Override
    public BookStorage push() {
        super.push();
        return this;
    }

    @Override
    public BookStorage erase() {
        super.erase();
        return this;
    }

    @Override
    protected BookStorages getDatabase() {
        return Database.getBookStoragesDb();
    }

    @Override
    public String toString() {
        return "ShopStorage{" +
                "bookIds=" + booksIds +
                "} " + super.toString();
    }
}
