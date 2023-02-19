package FlowerShop.domain;


import FlowerShop.repository.ReadWriteTxt;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Tree extends Product {
    private double height;
    private static long idCounter;
    private long treeId;

    public Tree(String name, double height, double price, int quantity) {
        super(name, price, quantity);
        this.height = height;
        idCounter = updateID() + 1;
        super.productId = idCounter;
    }

    public static long updateID(){
        List<Product> productList;
        AtomicLong newID = new AtomicLong();

        productList = ReadWriteTxt.readProductFile();

        if (productList != null){
            productList.stream()
                    .filter(product -> product.getClass().equals(Tree.class))
                    .reduce((first, second) -> second)
                    .ifPresent(product -> {
                        newID.set(product.getProductId());
                    });
            return newID.longValue();
        } else {
            return 0;
        }

    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public void changeSUMquantity(int quantity) {
        this.quantity = this.quantity + quantity;
    }

    @Override
    public void changeRESTquantity(int quantity) {
        this.quantity = this.quantity - quantity;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String toString() {

        return "- Tree. Type: " + super.getName()
                + "\nID: " + super.productId
                + ". Height: " + this.height
                + ". Total amount " + super.getQuantity()
                + ". Price per unit: " + super.getPrice();

    }

}





