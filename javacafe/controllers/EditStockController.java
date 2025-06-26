package javacafe.controllers;

import javacafe.Models.Inventory;
// A classe User não estava sendo usada, então o import pode ser removido se não for necessário.
// import javacafe.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditStockController extends PageNavigationController {
    // Componentes da Interface Gráfica (FXML)
    @FXML private Text numbercookie;
    @FXML private Text numbercapuccino;
    @FXML private Text numberlatte;
    @FXML private Text numbermate;
    @FXML private Text numberespressof;
    @FXML private Text numberespresso;
    @FXML private Text numberbrownie;

    // --- MELHORIA 1: Instância Única de Inventory ---
    // Criamos uma única instância de Inventory para a tela toda.
    // Isso evita ler o arquivo repetidamente a cada clique de botão.
    private Inventory inventory;

    /**
     * O método initialize é chamado pelo JavaFX quando a tela é carregada.
     * É o lugar perfeito para configurar o estado inicial.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Edit Stock Page Initialized");
        try {
            // Instanciamos o inventário UMA VEZ.
            this.inventory = new Inventory();
            // Atualizamos todos os textos da tela com os valores do inventário.
            updateAllStockLabels();
        } catch (IOException e) {
            System.out.println("ERRO CRÍTICO AO CARREGAR O INVENTÁRIO INICIAL");
            // Em uma aplicação real, você mostraria um pop-up de erro para o usuário aqui.
            throw new RuntimeException(e);
        }
    }

    // --- MELHORIA 2: Método único para atualizar a UI ---
    /**
     * Atualiza todos os campos de texto da interface gráfica com os valores
     * atuais do nosso objeto 'inventory'. Isso evita código repetido.
     */
    private void updateAllStockLabels() {
        // Usamos os novos métodos com nomes de produtos. Muito mais legível!
        // Usamos "espresso f" conforme seu código antigo. Se o nome no arquivo for diferente, ajuste aqui.
        numbercapuccino.setText(String.valueOf(inventory.getStock("capuccino")));
        numberlatte.setText(String.valueOf(inventory.getStock("latte")));
        numbermate.setText(String.valueOf(inventory.getStock("mate")));
        numberespressof.setText(String.valueOf(inventory.getStock("espresso f")));
        numberespresso.setText(String.valueOf(inventory.getStock("espresso")));
        numbercookie.setText(String.valueOf(inventory.getStock("cookie")));
        numberbrownie.setText(String.valueOf(inventory.getStock("brownie")));
    }
    
    // --- MELHORIA 3: Generalizar a lógica de atualização ---
    /**
     * Um método privado e genérico para lidar com a atualização de qualquer produto.
     * @param productName O nome do produto (ex: "capuccino").
     * @param amount A quantidade a ser alterada (ex: 1 para adicionar, -1 para remover).
     */
    private void handleStockChange(String productName, int amount) {
        try {
            // Usa o método refatorado de Inventory.
            inventory.updateStock(productName, amount);
            // Após a atualização, sincroniza a tela inteira.
            updateAllStockLabels();
        } catch (IOException e) {
            System.err.println("Erro ao salvar o inventário para o produto: " + productName);
            // Aqui também seria bom mostrar um alerta para o usuário.
        }
    }

    // --- Funções de evento (Handlers dos botões) ---
    // Agora os métodos dos botões são extremamente simples. Eles apenas
    // delegam a lógica para o nosso método genérico 'handleStockChange'.

    public void takeCapuccino(ActionEvent event) { handleStockChange("capuccino", -1); }
    public void addCapuccino(ActionEvent event) { handleStockChange("capuccino", 1); }

    public void takeLatte(ActionEvent event) { handleStockChange("latte", -1); }
    public void addLatte(ActionEvent event) { handleStockChange("latte", 1); }

    public void takeMate(ActionEvent event) { handleStockChange("mate", -1); }
    public void addMate(ActionEvent event) { handleStockChange("mate", 1); }
    
    // ATENÇÃO: Verifique se o nome "espresso f" corresponde exatamente ao que está no seu arquivo inventory.txt
    public void takeEspressoF(ActionEvent event) { handleStockChange("espresso f", -1); }
    public void addEspressoF(ActionEvent event) { handleStockChange("espresso f", 1); }

    public void takeEspresso(ActionEvent event) { handleStockChange("espresso", -1); }
    public void addEspresso(ActionEvent event) { handleStockChange("espresso", 1); }

    public void takeCookie(ActionEvent event) { handleStockChange("cookie", -1); }
    public void addCookie(ActionEvent event) { handleStockChange("cookie", 1); }

    public void takeBrownie(ActionEvent event) { handleStockChange("brownie", -1); }
    public void addBrownie(ActionEvent event) { handleStockChange("brownie", 1); }
}
