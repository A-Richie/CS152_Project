
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main {

    public static void main(String[] args) {
        Board newBoard = new Board();
        GameLogic game = new GameLogic("Player 1", "Player 2");
        game.displayBoard();
        game.gameFlow();


    }

    /*@Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Visuals.fxml"));
        primaryStage.setTitle("Chess Game");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    } */
}