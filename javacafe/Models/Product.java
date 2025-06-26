package javacafe.Models;

public class Product {
    private final String name;
    private int stock;
    private final double price; 

    public Product(String name, int initialStock, double price) { 
        this.name = name;
        this.stock = initialStock;
        this.price = price; 
    }

    public String getName() { return name; }
    public int getStock() { return stock; }
    public double getPrice() { return price; } 

    public void updateStock(int amount) {
        this.stock += amount;
    }
}
