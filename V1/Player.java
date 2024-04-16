import java.util.ArrayList;

public class Player {
    private String name;
    private boolean isWhite;
    private ArrayList<Piece> takenPieces;
    private int countChecks;
    private boolean isCheckmate; //may not need this

    public Player(String name, boolean isWhite){
        this.name = name;
        this.isWhite = isWhite;
        takenPieces = new ArrayList<>();
        countChecks = 0;
        isCheckmate = false;
    }

    public String getName() {
        return name;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public ArrayList<Piece> getTakenPieces() {
        return takenPieces;
    }

    public int getCountChecks() {
        return countChecks;
    }

    public boolean isCheckmate() {
        return isCheckmate;
    }

    public void addTakenPiece(Piece piece){
        takenPieces.add(piece);
    }
}
