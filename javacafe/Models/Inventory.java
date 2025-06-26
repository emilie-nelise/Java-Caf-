package javacafe.Models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Gerencia o inventário de produtos, lendo e escrevendo em um arquivo.
 * A classe agora é dinâmica e não depende da quantidade ou ordem dos itens.
 * Ela utiliza a classe Product para encapsular os dados de cada item.
 */
public class Inventory {

    // Usamos um Map para armazenar os produtos. A chave é o nome do produto (String).
    // LinkedHashMap mantém a ordem de inserção, garantindo que o arquivo seja escrito
    // e lido sempre na mesma ordem, o que é bom para consistência.
    private final Map<String, Product> products = new LinkedHashMap<>();
    private final String inventoryFilePath = "files/inventory.txt";

    /**
     * Construtor que carrega o inventário do arquivo ao ser instanciado.
     * @throws IOException Se houver um erro de leitura do arquivo.
     */
    public Inventory() throws IOException {
        loadInventoryFromFile();
    }

    /**
     * Retorna a quantidade em estoque de um produto específico.
     * @param productName O nome do produto (ex: "capuccino").
     * @return A quantidade em estoque, ou -1 se o produto não for encontrado.
     */
    public int getStock(String productName) {
        Product product = products.get(productName.toLowerCase());
        return (product != null) ? product.getStock() : -1;
    }

    /**
     * Retorna o objeto Product completo, que inclui nome, estoque e preço.
     * Útil para quando você precisa de mais do que apenas o estoque.
     * @param productName O nome do produto.
     * @return O objeto Product correspondente, ou null se não for encontrado.
     */
    public Product getProduct(String productName) {
        return products.get(productName.toLowerCase());
    }

    /**
     * Atualiza o estoque de um produto.
     * @param productName O nome do produto a ser atualizado.
     * @param amount A quantidade para adicionar (ex: 5) ou subtrair (ex: -1).
     * @throws IOException Se houver um erro ao escrever no arquivo.
     */
    public void updateStock(String productName, int amount) throws IOException {
        Product product = products.get(productName.toLowerCase());

        if (product == null) {
            System.err.println("ERRO: Tentativa de atualizar um produto inexistente: " + productName);
            return;
        }

        // Verifica se há estoque suficiente antes de subtrair
        if (amount < 0 && product.getStock() < Math.abs(amount)) {
            System.err.println("ESTOQUE INSUFICIENTE para o item: " + productName);
            return; // Interrompe a operação para não deixar o estoque negativo
        }
        
        product.updateStock(amount);
        writeInventoryToFile();
    }

    /**
     * Carrega os dados do inventário do arquivo para o Map.
     * Este método é dinâmico e lê quantos produtos houver no arquivo,
     * no formato "nome:estoque:preco".
     */
    private void loadInventoryFromFile() throws IOException {
        File file = new File(inventoryFilePath);
        if (!file.exists()) {
            System.err.println("Arquivo de inventário não encontrado. O inventário estará vazio.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Ignora linhas em branco

                String[] parts = line.split(":");
                if (parts.length == 3) { // Espera 3 partes: nome, estoque, preço
                    String name = parts[0].trim().toLowerCase();
                    int stock = Integer.parseInt(parts[1].trim());
                    double price = Double.parseDouble(parts[2].trim());
                    products.put(name, new Product(name, stock, price));
                } else {
                    System.err.println("AVISO: Linha mal formatada no inventário e será ignorada: " + line);
                }
            }
        }
    }

    /**
     * Escreve o estado atual do Map de produtos de volta para o arquivo,
     * sobrescrevendo o conteúdo anterior no formato "nome:estoque:preco".
     */
    private void writeInventoryToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(inventoryFilePath, false))) {
            for (Product product : products.values()) {
                bw.write(product.getName() + ":" + product.getStock() + ":" + product.getPrice());
                bw.newLine();
            }
        }
    }
}
