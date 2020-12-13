package database;

import models.Book;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Books extends Database<Book> {

    @Override
    public Book deserialize(Node node) {
        Book item = new Book();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            item.setId(Integer.parseInt(Utils.getTag(element, "id")));
            item.setTitle(Utils.getTag(element, "title"));
            item.setAuthor(Utils.getTag(element, "author"));
        }
        return item;
    }

    @Override
    public Node serialize(Book book, Document document) {
        Element itemElement = document.createElement(getNodeName());

        Element idElement = document.createElement("id");
        idElement.setTextContent(Integer.toString(book.getId()));

        Element titleElement = document.createElement("title");
        titleElement.setTextContent(book.getTitle());

        Element authorElement = document.createElement("author");
        authorElement.setTextContent(book.getAuthor());

        itemElement.appendChild(idElement);
        itemElement.appendChild(titleElement);
        itemElement.appendChild(authorElement);
        return itemElement;
    }

    @Override
    public String getFileName() {
        return "books";
    }

    @Override
    public String getNodeName() {
        return "book";
    }
}
