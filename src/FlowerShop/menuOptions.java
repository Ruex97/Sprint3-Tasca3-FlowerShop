package FlowerShop;

import FlowerShop.domain.*;
import FlowerShop.repository.ReadWriteTxt;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


public class menuOptions {

    static boolean flowershopCreated = false;

    static FlowerShop flowerShop;
    static Ticket ticket;

    public static void createFlowerShop() {

        flowerShop = FlowerShop.getInstance(Keyboard.getString("Type the flower's shop name."));
        System.out.println(flowerShop.getName() + " created!");
        flowershopCreated = true;
    }

    public static void addTree() {

        Product tree = new Tree(Keyboard.getString("What kind of tree is?"),
                Keyboard.getDouble("Type the tree's height."),
                Keyboard.getDouble("Enter the tree's retail price."),
                Keyboard.getInt("How many trees are you adding?"));
        try {
            ReadWriteTxt.addProduct(tree);
            System.out.println("Products successfully added!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addFlower() {

        Product flower = new Flower(Keyboard.getString("What kind of flower is?"),
                Keyboard.getString("Type the flower's color."),
                Keyboard.getDouble("Enter the flower's retail price."),
                Keyboard.getInt("How many flowers are you adding?"));
        try {
            ReadWriteTxt.addProduct(flower);
            System.out.println("Products successfully added!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addDecoration() {

        Product decoration = new Decoration(Keyboard.getString("What kind of decoration is?"),
                Keyboard.getString("It is plastic or wood made?"),
                Keyboard.getDouble("Enter the decoration's retail price."),
                Keyboard.getInt("How many decorations are you adding?"));
        try {
            ReadWriteTxt.addProduct(decoration);
            System.out.println("Products successfully added!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void printAllStock() {

        if (ReadWriteTxt.readProductFile() != null){
            ReadWriteTxt.readProductFile().forEach(System.out::println);
        } else {
            System.out.println("You need to add some products first!");
        }

    }

    public static void removeProduct(String product) throws IOException {

        if (ReadWriteTxt.readProductFile() != null){
            ReadWriteTxt.removeProductFromFile(Keyboard.getString("Type the name of the " + product + " you want to remove"));
            System.out.println("Products successfully removed!");
        } else {
            System.out.println("You need to add some products first!");
        }

    }

    /* METODES QUE NO FEM SERVIR. ELIMINEM?
    public static void removeTree() throws IOException {

        ReadWriteTxt.removeProductFromFile(Keyboard.getString("Type the name of the tree you want to remove"),
                                    Keyboard.getInt("Type the amount of trees you want to remove"));
        System.out.println("Tree has been removed");
    }

    public static void removeFlower() throws IOException {

        ReadWriteTxt.removeProductFromFile(Keyboard.getString("Type the name of the flower you want to remove"),
                Keyboard.getInt("Type the amount of flowers you want to remove"));
        System.out.println("Flower has been removed");
    }

    public static void removeDecoration() throws IOException {

        ReadWriteTxt.removeProductFromFile(Keyboard.getString("Type the name of the decoration you want to remove"),
                Keyboard.getInt("Type the amount of decorations you want to remove"));
        System.out.println("Decoration has been removed");
    }*/

    public static void printStockAndQuantities() {

        if (ReadWriteTxt.readProductFile() != null){

        } else {
            System.out.println("You need to add some products first!");
        }

    }

    public static void printFullValue() {



    }

    public static void createPurchaseTicket() throws IOException {

        Product purchaseProduct;
        int option;
        ticket = new Ticket();

        do {
            System.out.println("""
                    1- Add Product.
                    2. Stop adding product to ticket.""");
            option = Keyboard.getInt("Choose an option.");

            switch (option){
                case 1 -> {
                    purchaseProduct = new Product(Keyboard.getString("Type the name of the product you want to buy"),
                            Keyboard.getInt("Type the amount you want to buy"));
                    createPurchaseTicketCalcul(purchaseProduct);
                }
                case 2 -> ReadWriteTxt.addTicket(ticket);

            }
        } while (option != 2);

    }

    public static void createPurchaseTicketCalcul(Product purchaseProduct) throws IOException {

        int amount = ReadWriteTxt.readProductFile().stream()
                .filter(databaseProduct -> databaseProduct.getName().equalsIgnoreCase(purchaseProduct.getName()))
                .mapToInt(Product::getQuantity)
                .sum();

        if (amount >= purchaseProduct.getQuantity()) {
            ticket.addProduct(purchaseProduct);
            //aqui no s'hauria de fer remove product sinó canviar la quantitat d'aquell product que ens queda!
            //ReadWriteTxt.removeProductFromFile(purchaseProduct.getName());
            //seria una cosa així:
            ReadWriteTxt.readProductFile().stream()
                    .filter(databaseProduct -> databaseProduct.getName().equalsIgnoreCase(purchaseProduct.getName()))
                    .findAny()
                    .ifPresent(databaseProduct -> {
                        databaseProduct.changeRESTquantity(purchaseProduct.getQuantity());
                        try {
                            ReadWriteTxt.addProduct(databaseProduct);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        //
                        if (databaseProduct.getQuantity() == 0){ //si la quantity es queda a 0 dp d'afegir a ticket, eliminar producte
                            try {
                                ReadWriteTxt.removeProductFromFile(purchaseProduct.getName());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
            System.out.println("Product added to ticket");
        } else {
            System.out.println("The desired quantity exceeds the stock.");
        }

        //s'ha canviat el mètode add i ara no cal recorrer tota la llista,
        // ja que tots els productes que siguin iguals s'ajunataran en un i es sumarà la quantitat
        //un cop trobem el producte parar de buclejar
    }

    public static void printOldPurchases() {

        if (ReadWriteTxt.readTicketFile() != null){
            ReadWriteTxt.readTicketFile().forEach(System.out::println);
        } else {
            System.out.println("Create a ticket first!");
        }

    }

    public static void printTotalSumPurchases() {

        AtomicInteger totalSumPurchases = new AtomicInteger();

        if (ReadWriteTxt.readTicketFile() != null){
            ReadWriteTxt.readTicketFile()
                    .forEach(ticket -> ticket.getProducts()
                            .forEach(product -> totalSumPurchases.addAndGet(product.getQuantity())));

            System.out.println("The flower shop " + flowerShop.getName() + " has sold a total value of " + totalSumPurchases.get() + "€.");

        } else {
            System.out.println("Create a ticket first!");
        }
    }
}
