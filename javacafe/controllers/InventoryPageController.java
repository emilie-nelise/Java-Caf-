package javacafe.controllers;

import javacafe.Models.Inventory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InventoryPageController extends PageNavigationController {
    // Campos da interface (FXML)
    @FXML private Text capuccinoStock;
    @FXML private Text latteStock;
    @FXML private Text mateStock;
    @FXML private Text espressoFStock;
    @FXML private Text espressoStock;
    @FXML private Text cookieStock;
    @FXML private Text brownieStock;

    // --- MELHORIA 1: Instância Única de Inventory ---
    // Assim como no outro controller, usamos uma única instância para a tela toda.
    private Inventory inventory;

    /**
     * Método chamado pelo JavaFX quando a tela é carregada.
     * Ideal para configurar o estado inicial.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Inventory Page Initialized");
        try {
            // Instanciamos o inventário apenas uma vez.
            this.inventory = new Inventory();
            // Chamamos nosso método auxiliar para popular os textos da tela.
            displayAllStockLevels();
        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO AO CARREGAR O INVENTÁRIO.");
            // Considerar mostrar um alerta visual para o usuário sobre a falha.
            throw new RuntimeException(e);
        }
    }
    
    // --- MELHORIA 2: Método único para atualizar a UI ---
    /**
     * Atualiza todos os campos de texto da interface gráfica com os valores
     * atuais do nosso objeto 'inventory'.
     */
    private void displayAllStockLevels() {
        // Usando a nova API baseada em Strings. O código se torna auto-documentado.
        capuccinoStock.setText(String.valueOf(inventory.getStock("capuccino")));
        latteStock.setText(String.valueOf(inventory.getStock("latte")));
        mateStock.setText(String.valueOf(inventory.getStock("mate")));
        espressoFStock.setText(String.valueOf(inventory.getStock("espresso f")));
        espressoStock.setText(String.valueOf(inventory.getStock("espresso")));
        cookieStock.setText(String.valueOf(inventory.getStock("cookie")));
        brownieStock.setText(String.valueOf(inventory.getStock("brownie")));
    }

    @Override
    public void goToInventory(ActionEvent event) throws IOException {
        // O comportamento aqui está correto, não precisa de alteração.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("You already are on the inventory screen!");
        alert.showAndWait();
    }

    /**
     * Direciona para a página de editar estoque.
     * Nenhuma alteração necessária aqui, a navegação está correta.
     * @param event O evento do clique.
     * @throws IOException Se o arquivo FXML não for encontrado.
     */
    public void editStock(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../pages/editstockpage.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
