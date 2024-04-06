public class Queen extends Piece {
    public Queen(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        if(!isInBounds(newX, newY)) return false;
        int dx = Math.abs(newX - x);
        int dy = Math.abs(newY - y);

        return (dx == 0 || dy == 0 || dx == dy);
    }
}
