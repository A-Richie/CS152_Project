public class Rook extends Piece{
    public Rook(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        if(!isInBounds(newX, newY)) return false;
        if(newX == x || newY == y) {
            return true;
        }
        return false;
    }
}
