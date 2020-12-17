package database;

import models.BookOrder;
import models.BookStorage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BookOrders extends Database<BookOrder> {
    @Override
    protected BookOrder deserialize(Node node) {
        BookOrder bookOrder = new BookOrder();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            bookOrder.setId(Integer.parseInt(Utils.getTag(element, "id")));
            bookOrder.setBookId(Integer.parseInt(Utils.getTag(element, "bookId")));
            bookOrder.setUserId(Integer.parseInt(Utils.getTag(element, "userId")));
        }
        return bookOrder;
    }

    @Override
    protected Node serialize(BookOrder bookOrder, Document document) {
        Element bookOrderElement = document.createElement(getNodeName());

        Element idElement = document.createElement("id");
        idElement.setTextContent(Integer.toString(bookOrder.getId()));

        Element bookIdElement = document.createElement("bookId");
        bookIdElement.setTextContent(Integer.toString(bookOrder.getBookId()));

        Element userIdElement = document.createElement("userId");
        userIdElement.setTextContent(Integer.toString(bookOrder.getUserId()));

        bookOrderElement.appendChild(idElement);
        bookOrderElement.appendChild(bookIdElement);
        bookOrderElement.appendChild(userIdElement);
        return bookOrderElement;
    }

    public List<BookOrder> getByUserId(int userId) {
        return this.select(t->t.getUserId() == userId);
    }

    public List<BookOrder> getByBookId(int bookId) {
        return this.select(t->t.getBookId() == bookId);
    }

    @Override
    public String getFileName() {
        return "book_orders";
    }

    @Override
    public String getNodeName() {
        return "book_order";
    }
}
