package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import fr.univ_amu.iut.server.ServerCommunication;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Create a configured Button to switch to the menu page when the user click on it
 * @author LennyGonzales
 */
public class ButtonSwitchToMenu extends Button {

    public ButtonSwitchToMenu() {
        initializeButton();
    }

    /**
     * Initialize the button
     */
    public void initializeButton() {
        getStyleClass().add("Btn"); // Add a style class to get css style

        // When the user click on the button, we notify the server that we go back to the menu page
        setOnAction(event ->
                {
                    try {
                        Main.getServerCommunication().sendMessageToServer("BACK_TO_MENU_FLAG");

                        SceneController sceneController = new SceneController();
                        sceneController.switchTo("fxml/menu.fxml");
                    } catch (UrlOfTheNextPageIsNull | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
