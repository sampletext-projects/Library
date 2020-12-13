package database;

import models.BookStorage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class BookStorages extends Database<BookStorage> {

    @Override
    public BookStorage deserialize(Node node) {
        BookStorage shopStorage = new BookStorage();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            shopStorage.setId(Integer.parseInt(Utils.getTag(element, "id")));
            Node entitiesNode = Utils.getTagNode(element, "entities");
            NodeList shop_entity_idNodes = ((Element)entitiesNode).getElementsByTagName("id");
            List<Integer> entities = new ArrayList<>();
            for (int i = 0; i < shop_entity_idNodes.getLength(); i++) {
                if(shop_entity_idNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    entities.add(Integer.parseInt(shop_entity_idNodes.item(i).getTextContent()));
                }
            }
            shopStorage.setBooksIds(entities);
        }
        return shopStorage;
    }

    @Override
    public Node serialize(BookStorage storage, Document document) {
        Element storageElement = document.createElement(getNodeName());

        Element idElement = document.createElement("id");
        idElement.setTextContent(Integer.toString(storage.getId()));

        Element entities_ids_element = document.createElement("entities");
        for (int entity_id : storage.getBooksIds()) {
            Element entity_id_element = document.createElement("id");
            entity_id_element.setTextContent(Integer.toString(entity_id));
            entities_ids_element.appendChild(entity_id_element);
        }

        storageElement.appendChild(idElement);
        storageElement.appendChild(entities_ids_element);
        return storageElement;
    }

    public BookStorage getMainStorage() {
        return select(t -> t.getId() == 0).get(0);
    }

    public BookStorage getByUserId(int userId) {
        return select(t -> t.get).get(0);
    }

    @Override
    public String getFileName() {
        return "book_storages";
    }

    @Override
    public String getNodeName() {
        return "storage";
    }
}