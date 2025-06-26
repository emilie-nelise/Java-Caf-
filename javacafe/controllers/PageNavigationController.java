package javacafe.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

abstract public class PageNavigationController implements Initializable {
    private Stage stage;
    private Scene scene;

    public void goToOrder(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../pages/orderpage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToItems(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../pages/mainpage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToInventory(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../pages/inventorypage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToSales(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../pages/salespage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void exitScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../pages/firstpage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
