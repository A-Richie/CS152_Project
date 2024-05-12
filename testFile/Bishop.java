public class Bishop extends Piece {
    public Bishop(int x, int y, boolean isWhite)
    {
        super(x, y, isWhite);
    }

    @Override
    public boolean isValidMove(int pX, int pY, int newX, int newY) {
        if(!isInBounds(newX, newY)) return false;
        int dx = newX - pX;
        int dy = newY - pY;
        if (Math.abs(dx) == Math.abs(dy)) {
            return true;
        }
        return false;
    }
}
