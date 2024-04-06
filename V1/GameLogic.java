import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.Optional;

public class GameLogic {
    // this needs to be worked on

    private Piece[][] board;
    private Player p1, p2;
    private ArrayList<Piece> capturedPieces;

    public GameLogic() {
        p1 = new Player("placeholder1", true);
        p2 = new Player("placeholder2", false);
        capturedPieces = new ArrayList<>();
        board = new Piece[8][8];
    }

    public void gameFlow() {

    }

    public boolean isLegalMove() {
        return true; //placeholder
    }
}
