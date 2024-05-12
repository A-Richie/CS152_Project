import java.util.ArrayList;

public class Player {
    private String name;
    private boolean isWhite;
    private ArrayList<Piece> takenPieces;
    private int countChecks;
    private boolean isCheck; // may not need this

    public static int kingX;
    public static int kingY;

    public Player(String name, boolean isWhite){
        this.name = name;
        this.isWhite = isWhite;
        takenPieces = new ArrayList<>();
        countChecks = 0;
        isCheck = false;
        kingX = -1; // set to -1 for simplicity - updated later
        kingY = -1;
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

    public boolean isCheck() {
        return isCheck;
    }

    public void addTakenPiece(Piece piece){
        takenPieces.add(piece);
    }

    public boolean containsKing() {
        for (Piece piece : takenPieces) {
            if (piece instanceof King) {
                return true;
            }
        }
        return false;
    }

    public static void updateKingPositions(int y, int x) {
        kingX = x;
        kingY = y;
    }


}
