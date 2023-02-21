package FlowerShop.domain;

import FlowerShop.repository.ReadWriteTxt;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlowerShop implements Serializable {
    private String name;
    private List<Ticket> invoices;
    private List<Product> inventory;
    private static FlowerShop instance;

    private FlowerShop(String name) {
        this.name = name;
        this.invoices = new ArrayList<>();
        this.inventory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Ticket> getInvoices() {
        invoices = ReadWriteTxt.readTicketFile();
        return invoices;
    }

    public List<Product> getInventory() {
        inventory = ReadWriteTxt.readProductFile();
        return inventory;
    }

    public void removeProductFromInventory(String product) throws IOException {
        ReadWriteTxt.removeProductFromFile(product);
    }

    public void addProductToInventory(Product product, boolean sumQuantity) throws IOException {
        ReadWriteTxt.addProduct(product, sumQuantity);
    }

    public void addTicketToInvoices(Ticket ticket) throws IOException {
        ReadWriteTxt.addTicket(ticket);
    }

    public static FlowerShop getInstance(String name) {
        if (instance == null) {
            instance = new FlowerShop(name);
        }
        return instance;
    }
}
