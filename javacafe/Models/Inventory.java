package javacafe.Models;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Gerencia o inventário de produtos, lendo e escrevendo em um arquivo.
 * A classe agora é dinâmica e não depende da quantidade ou ordem dos itens.
 */
public class Inventory {

    // Usamos um Map para armazenar os produtos. A chave é o nome do produto (String).
    // LinkedHashMap mantém a ordem de inserção, garantindo que o arquivo seja escrito sempre na mesma ordem.
    private final Map<String, Product> products = new LinkedHashMap<>();
    private final String inventoryFilePath = "files/inventory.txt";

    /**
     * Construtor que carrega o inventário do arquivo ao ser instanciado.
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
     * Atualiza o estoque de um produto.
     * @param productName O nome do produto a ser atualizado.
     * @param amount A quantidade para adicionar (ex: 5) ou subtrair (ex: -1).
     * @throws IOException Se houver um erro ao escrever no arquivo.
     */
    public void updateStock(String productName, int amount) throws IOException {
        Product product = products.get(productName.toLowerCase());

        if (product == null) {
            System.out.println("ERRO: Tentativa de atualizar um produto inexistente: " + productName);
            return;
        }

        // Lógica de verificação de estoque antes de subtrair
        if (amount < 0 && product.getStock() < Math.abs(amount)) {
            System.out.println("ESTOQUE INSUFICIENTE para o item: " + productName);
            return; // Interrompe a operação
        }
        
        product.updateStock(amount);
        writeInventoryToFile();
    }

    /**
     * Carrega os dados do inventário do arquivo para o Map.
     * Este método agora é dinâmico e lê quantos produtos houver no arquivo.
     */
    private void loadInventoryFromFile() throws IOException {
        File file = new File(inventoryFilePath);
        if (!file.exists()) {
            System.out.println("Arquivo de inventário não encontrado. O inventário estará vazio.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Ignora linhas em branco

                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String name = parts[0].trim().toLowerCase();
                    int stock = Integer.parseInt(parts[1].trim());
                    products.put(name, new Product(name, stock));
                }
            }
        }
    }

    /**
     * Escreve o estado atual do Map de produtos de volta para o arquivo,
     * sobrescrevendo o conteúdo anterior.
     */
    private void writeInventoryToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(inventoryFilePath, false))) {
            for (Product product : products.values()) {
                bw.write(product.getName() + ":" + product.getStock());
                bw.newLine();
            }
        }
    }
