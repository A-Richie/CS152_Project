public class Knight extends Piece {
    public Knight(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        if(!isInBounds(newX, newY)) return false;
        int dx = Math.abs(newX - x);
        int dy = Math.abs(newY - y);

        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }
}