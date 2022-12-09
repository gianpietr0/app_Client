import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Classe controller per il bottone di login della loginPage.
 */
public class loginController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Variabile rappresentante la stringa indicante l'esito della connessione al server.
     */
    @FXML
    Label lblClient;


    public void loginAction(ActionEvent actionEvent) throws IOException, InterruptedException {
        try {
            if (Client.getIstance() == null) {
                lblClient.setTextFill(Color.RED);
                lblClient.setText("Connection failed!");
            } else {
                lblClient.setTextFill(Color.GREEN);
                lblClient.setText("Client connected!");
                root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (RuntimeException err) {
            lblClient.setTextFill(Color.RED);
            lblClient.setText("Client not connected!");
        } catch (Exception exc) {
            lblClient.setTextFill(Color.RED);
            lblClient.setText("Connection failed!");
        }
    }
}
