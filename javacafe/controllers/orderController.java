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

public class orderController extends PageNavigationController {

    // --- MELHORIA 1: Centralizar nomes de arquivos como constantes ---
    private static final String ORDER_DETAILS_FILE = "counts.txt";
    private static final String ORDER_SUMMARY_FILE = "files/resumo.txt";
    private static final String SALES_HISTORY_FILE = "files/sales.txt";

    @FXML
    private Text ordertxt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Order Page Initialized");
        try {
            // Usa a constante para ler o arquivo do pedido atual
            String content = readFile(ORDER_DETAILS_FILE);
            ordertxt.setText(content);
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo do pedido: " + e.getMessage());
            ordertxt.setText("Não foi possível carregar os detalhes do pedido.");
        }
    }

    /**
     * Finaliza o pedido, gera o recibo e limpa o estado do pedido atual.
     */
    public void finishOrder(ActionEvent event) {
        try {
            // --- MELHORIA 2: Lógica unificada em um único bloco try-catch ---
            
            // 1. Gera um recibo único com timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String receiptFileName = "receipt_" + timestamp + ".txt";
            String receiptContent = "Receipt\n\n" + ordertxt.getText();
            
            writeFile(receiptFileName, receiptContent, false); // false para não adicionar, mas sobrescrever
            System.out.println("Recibo gerado com sucesso: " + receiptFileName);

            // 2. Adiciona o resumo do pedido ao histórico de vendas
            String historyContent = readFile(ORDER_SUMMARY_FILE);
            writeFile(SALES_HISTORY_FILE, historyContent, true); // true para adicionar ao final do arquivo

            // 3. --- MELHORIA 3: Limpa o pedido atual para evitar "pedidos fantasma" ---
            clearCurrentOrderFiles();
            ordertxt.setText("Pedido finalizado com sucesso!\nUm novo pedido pode ser iniciado.");
            
            // 4. Mostra alerta de sucesso
            showAlert(Alert.AlertType.INFORMATION, "Pedido Concluído", "O recibo foi gerado e o pedido finalizado.");

        } catch (IOException e) {
            System.err.println("Falha ao finalizar o pedido: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível finalizar o pedido e gerar os arquivos.");
            e.printStackTrace();
        }
    }
    
    /**
     * Limpa os arquivos que representam o estado do pedido atual,
     * para que o próximo pedido comece do zero.
     * @throws IOException
     */
    private void clearCurrentOrderFiles() throws IOException {
        writeFile(ORDER_DETAILS_FILE, "", false);
        writeFile(ORDER_SUMMARY_FILE, "", false);
    }

    // Métodos auxiliares para leitura e escrita de arquivos
    
    private String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    private void writeFile(String path, String content, boolean append) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, append))) {
            writer.write(content);
        }
    }
    
    // Método auxiliar para simplificar a criação de Alertas
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void goToOrder(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Atenção", "Você já está na tela de pedidos!");
    }
}
