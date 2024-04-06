public class Bishop extends Piece {
    public Bishop(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        if(!isInBounds(newX, newY)) return false;
        int dx = newX - x;
        int dy = newY - y;
        if (Math.abs(dx) == Math.abs(dy)) {
            return true;
        }
        return false;
    }
}
