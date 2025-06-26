package javacafe.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class salesController extends PageNavigationController {

    // --- MELHORIA 1: Centralizar nome do arquivo como constante ---
    private static final String SALES_HISTORY_FILE = "files/sales.txt";

    // --- MELHORIA 2: Nomenclatura mais clara ---
    // Lembre-se de atualizar o fx:id no seu arquivo .fxml de "ordertxt" para "salesHistoryText"
    @FXML
    private Text salesHistoryText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Sales Page Initialized");
        try {
            // Usa a constante e exibe o conteúdo no componente com nome mais claro
            String content = readFile(SALES_HISTORY_FILE);
            salesHistoryText.setText(content);
        } catch (IOException e) {
            // --- MELHORIA 3: Tratamento de erro mais amigável para o usuário ---
            String errorMessage = "Não foi possível carregar o histórico de vendas.\nO arquivo pode não existir ou estar corrompido.";
            salesHistoryText.setText(errorMessage);
            System.err.println("Erro ao ler o arquivo de vendas: " + e.getMessage());
        }
    }

    /**
     * Lê o conteúdo de um arquivo e o retorna como uma String.
     * @param filePath O caminho do arquivo a ser lido.
     * @return O conteúdo do arquivo.
     * @throws IOException Se ocorrer um erro de leitura.
     */
    private String readFile(String filePath) throws IOException {
        // Esta implementação é mais moderna e eficiente que o loop com StringBuilder
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    // --- MELHORIA 4: Implementação da funcionalidade 'generateReport' ---
    /**
     * Gera um relatório de vendas criando uma cópia do histórico atual
     * com um nome de arquivo único baseado na data e hora.
     */
    public void generateReport(ActionEvent event) {
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String reportFileName = "sales_report_" + timestamp + ".txt";

            String reportContent = "Sales Report generated on: " + new Date() + "\n\n" + salesHistoryText.getText();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFileName))) {
                writer.write(reportContent);
            }

            showAlert(Alert.AlertType.INFORMATION, "Relatório Gerado", "O relatório de vendas foi salvo com sucesso como:\n" + reportFileName);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível gerar o relatório de vendas.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // --- Métodos de navegação (sem alterações necessárias) ---
    public void goToOrder(ActionEvent event) throws IOException { super.goToOrder(event); }
    public void goToItems(ActionEvent event) throws IOException { super.goToItems(event); }
    public void goToInventory(ActionEvent event) throws IOException { super.goToInventory(event); }
    public void exitScreen(ActionEvent event) throws IOException { super.exitScreen(event); }

    @Override
    public void goToSales(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Atenção", "Você já está na tela de histórico de vendas!");
    }
}
