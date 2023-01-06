package fr.univ_amu.iut;

import fr.univ_amu.iut.client.ServerCommunication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;

/**
 * Controller of the login's page
 */
public class LoginController {
    @FXML
    private TextField mailTextField;
    @FXML
    private PasswordField passwordTextField;
    private final ServerCommunication serverCommunication;
    private String message;
    private static String mail;

    private final SceneController sceneController;
    public LoginController() {
        serverCommunication = Main.getClient();  // Get the connection with the server
        sceneController = new SceneController();
    }


    /**
     * Supports the login service
     * @return if the username and the password are corrects
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public boolean verifyLogin(String mail, String password) throws IOException {
        serverCommunication.sendMessageToServer("LOGIN_FLAG");
        serverCommunication.sendMessageToServer(mail);
        serverCommunication.sendMessageToServer(password);
        message = serverCommunication.receiveMessageFromServer();
        return message.equals("[+] LOGIN !");
    }

    /**
     * Supports the login of the user
     * @param event of the button actioned
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void serviceLogin(ActionEvent event) throws IOException {
        if(verifyLogin(mailTextField.getText(),passwordTextField.getText())) {
            mail = mailTextField.getText();  // Store the mail into a static variable for the multiplayer (send the mail to the host when the user join a multiplayer session)
            //Get the name of the file
            Node node = (Node) event.getSource() ;
            String nameNextPage = (String) node.getUserData();
            sceneController.switchTo(nameNextPage);
        }  else{
            Alert connexionError = new Alert(Alert.AlertType.ERROR, "The username and/or password are incorrect");
            connexionError.show();
        }
    }

    public static String getMail() { return mail;}
}
