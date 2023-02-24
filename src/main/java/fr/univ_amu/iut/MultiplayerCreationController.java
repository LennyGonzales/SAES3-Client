package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller of the multiplayer session's creation page
 * @author LennyGonzales
 */
public class MultiplayerCreationController {
    @FXML
    private TextField codeSession;
    @FXML
    private ListView<String> usersPresentListView;
    private final Communication communication;
    private final SceneController sceneController;
    private Timeline timeline;


    public MultiplayerCreationController() {
        communication = Main.getCommunication();
        sceneController = new SceneController();
    }

    /**
     * Start the session
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull Throw if the url of the next page is null
     * @throws NotTheExpectedFlagException Throw when the flag received isn't the expected flag | Print the expected flag
     * @throws ClassNotFoundException Throw if the object class not found when we receive an object from the server
     * @throws NotAStringException Throw when the message received from the server isn't a string
     */
    public void sessionBegin() throws IOException, UrlOfTheNextPageIsNull, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException, InterruptedException {
        timeline.stop();    // Stop getting email of the users who joined

        communication.sendMessageToServer("BEGIN");    // Send to the server that we want to start the game by clicking on the 'Start' button
        if(!(communication.receiveMessageFromServer().equals("BEGIN_FLAG"))) { // The game begins
            throw new NotTheExpectedFlagException("BEGIN_FLAG");
        }
        communication.sendMessageToServer("BEGIN_FLAG");  // Send to the server that the game begin
        sceneController.switchTo("fxml/question.fxml");
    }

    /**
     * Get users who joined the multiplayer session before that the user start the session
     */
    public void getEmailOfTheUsersWhoJoined() throws NotAStringException, IOException, ClassNotFoundException, InterruptedException {
        usersPresentListView.getItems().add(communication.receiveMessageFromServer());
        /*
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.05), e -> {
                    try {
                        if(serverCommunication.isReceiveMessageFromServer()) {   // Verify if the server sent a message
                            usersPresentListView.getItems().add(serverCommunication.receiveMessageFromServer());
                        }
                    } catch (IOException | ClassNotFoundException | NotAStringException ex) {
                        throw new RuntimeException(ex);
                    }

                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

         */
    }

    /**
     * Get the session code from the server
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws NotTheExpectedFlagException Throw when the flag received isn't the expected flag | Print the expected flag
     * @throws ClassNotFoundException Throw if the object class not found when we receive an object from the server
     * @throws NotAStringException Throw when the message received from the server isn't a string
     */
    public void getSessionCode() throws IOException, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException, InterruptedException {
        if(!(communication.receiveMessageFromServer().equals("CODE_FLAG"))) {
            throw new NotTheExpectedFlagException("CODE_FLAG");
        }
        codeSession.setText(communication.receiveMessageFromServer());
    }

    /**
     * Initialize the page
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws NotTheExpectedFlagException Throw when the flag received isn't the expected flag | Print the expected flag
     * @throws ClassNotFoundException Throw if the object class not found when we receive an object from the server
     * @throws NotAStringException Throw when the message received from the server isn't a string
     */
    @FXML
    public void initialize() throws IOException, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException, InterruptedException {
        getSessionCode();
        getEmailOfTheUsersWhoJoined();
    }
}
