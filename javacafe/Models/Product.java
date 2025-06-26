package javacafe.Models;

public class Product {
    private final String name; // O nome do produto (ex: "capuccino")
    private int stock;      // A quantidade em estoque

    public Product(String name, int initialStock) {
        this.name = name;
        this.stock = initialStock;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public void updateStock(int amount) {
        this.stock += amount;
    }
}
