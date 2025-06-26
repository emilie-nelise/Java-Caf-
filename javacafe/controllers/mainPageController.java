// Em: javacafe/controllers/mainPageController.java
package javacafe.controllers;

import javacafe.Models.Inventory;
import javacafe.Models.Product; // Import necessário
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap; // Import necessário
import java.util.Map;     // Import necessário
import java.util.ResourceBundle;

public class mainPageController extends PageNavigationController {
    // --- Campos FXML (sem alterações) ---
    @FXML private Text numbercookie;
    @FXML private Text numbercapuccino;
    @FXML private Text numberlatte;
    @FXML private Text numbermate;
    @FXML private Text numberespressof;
    @FXML private Text numberespresso;
    @FXML private Text numberbrownie;
    
    // --- MELHORIA 1: Gerenciamento de Estado ---
    private Inventory inventory; // Acesso ao estoque geral
    // O "carrinho de compras" atual. Mapeia o nome do produto à quantidade pedida.
    private Map<String, Integer> currentOrder; 
    // Mapeia o nome do produto ao seu componente de Texto na UI para fácil acesso.
    private Map<String, Text> uiTextMap; 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Main Page Initialized");
        try {
            this.inventory = new Inventory();
            this.currentOrder = new HashMap<>();
            
            // Mapeia os nomes dos produtos aos seus respectivos TextFields da UI
            initializeUiMap();
            
            // Reseta a tela para um novo pedido
            resetOrder();
        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO AO INICIALIZAR A PÁGINA PRINCIPAL");
            throw new RuntimeException(e);
        }
    }

    private void initializeUiMap() {
        this.uiTextMap = new HashMap<>();
        uiTextMap.put("capuccino", numbercapuccino);
        uiTextMap.put("latte", numberlatte);
        uiTextMap.put("mate", numbermate);
        uiTextMap.put("espresso f", numberespressof);
        uiTextMap.put("espresso", numberespresso);
        uiTextMap.put("cookie", numbercookie);
        uiTextMap.put("brownie", numberbrownie);
    }
    
    /**
     * Zera o pedido atual, limpando o carrinho e a UI.
     */
    private void resetOrder() {
        currentOrder.clear();
        for (Text uiText : uiTextMap.values()) {
            uiText.setText("0");
        }
        updateAndSaveReceipts(); // Gera os recibos vazios iniciais
    }

    // --- MELHORIA 2: Lógica de Manipulação do Pedido Generalizada ---
    
    private void handleAddItem(String productName) {
        Product product = inventory.getProduct(productName);
        if (product != null && product.getStock() > 0) {
            try {
                // Adiciona ao carrinho local
                int currentCount = currentOrder.getOrDefault(productName, 0);
                currentOrder.put(productName, currentCount + 1);

                // Remove do estoque geral
                inventory.updateStock(productName, -1);
                
                // Atualiza a tela e salva os recibos
                updateAndSaveReceipts();
            } catch (IOException e) {
                showAlert("ERRO", "Não foi possível atualizar o estoque.");
            }
        } else {
            showAlert("Estoque Esgotado", "Não há mais " + productName + " em estoque!");
        }
    }

    private void handleRemoveItem(String productName) {
        int currentCount = currentOrder.getOrDefault(productName, 0);
        if (currentCount > 0) {
            try {
                // Remove do carrinho local
                currentOrder.put(productName, currentCount - 1);
                
                // Devolve ao estoque geral
                inventory.updateStock(productName, 1);
                
                // Atualiza a tela e salva os recibos
                updateAndSaveReceipts();
            } catch (IOException e) {
                showAlert("ERRO", "Não foi possível atualizar o estoque.");
            }
        }
    }
    
    /**
     * Método central que atualiza a UI e salva os arquivos de recibo.
     */
    private void updateAndSaveReceipts() {
        // Atualiza a contagem na tela
        for (Map.Entry<String, Text> entry : uiTextMap.entrySet()) {
            entry.getValue().setText(String.valueOf(currentOrder.getOrDefault(entry.getKey(), 0)));
        }
        
        // Gera e salva os recibos
        saveReceiptToFile("files/counts.txt", false);
        saveReceiptToFile("files/resumo.txt", true);
    }

    // --- MELHORIA 3: Lógica de Geração de Recibo Unificada ---
    
    private void saveReceiptToFile(String filePath, boolean isSummary) {
        StringBuilder content = new StringBuilder();
        double totalSum = 0;

        if (isSummary) {
            content.append("----------------------------------\n");
        }

        for (Map.Entry<String, Integer> orderEntry : currentOrder.entrySet()) {
            String productName = orderEntry.getKey();
            int quantity = orderEntry.getValue();
            
            if (quantity > 0) {
                Product product = inventory.getProduct(productName);
                if (product != null) {
                    double subtotal = quantity * product.getPrice();
                    totalSum += subtotal;

                    if (isSummary) {
                        content.append(product.getName().substring(0, 3)).append(": ").append(quantity).append(" | ");
                    } else {
                        String line = String.format("%-20s %d \t R$ %.2f\n", product.getName() + ":", quantity, subtotal);
                        content.append(line);
                    }
                }
            }
        }

        if (!isSummary) {
            content.append("------------------------------------------------------------------\n");
        }

        if (totalSum > 0) {
            if (isSummary) {
                content.append(String.format("\nR$ %.2f", totalSum));
            } else {
                content.append(String.format("Valor: \t\t\t R$ %.2f", totalSum));
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content.toString());
        } catch (IOException e) {
            System.err.println("Error saving file " + filePath + ": " + e.getMessage());
        }
    }

    // --- Handlers dos Botões (agora muito mais simples) ---
    public void takeCapuccino(ActionEvent event) { handleRemoveItem("capuccino"); }
    public void addCapuccino(ActionEvent event) { handleAddItem("capuccino"); }

    public void takeLatte(ActionEvent event) { handleRemoveItem("latte"); }
    public void addLatte(ActionEvent event) { handleAddItem("latte"); }

    public void takeMate(ActionEvent event) { handleRemoveItem("mate"); }
    public void addMate(ActionEvent event) { handleAddItem("mate"); }

    public void takeEspressoF(ActionEvent event) { handleRemoveItem("espresso f"); }
    public void addEspressoF(ActionEvent event) { handleAddItem("espresso f"); }

    public void takeEspresso(ActionEvent event) { handleRemoveItem("espresso"); }
    public void addEspresso(ActionEvent event) { handleAddItem("espresso"); }

    public void takeCookie(ActionEvent event) { handleRemoveItem("cookie"); }
    public void addCookie(ActionEvent event) { handleAddItem("cookie"); }

    public void takeBrownie(ActionEvent event) { handleRemoveItem("brownie"); }
    public void addBrownie(ActionEvent event) { handleAddItem("brownie"); }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
