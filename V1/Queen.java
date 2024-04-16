public class Queen extends Piece {
    public Queen(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
    }

    @Override
    public boolean isValidMove(int pX, int pY, int newX, int newY) {
        if(!isInBounds(newX, newY)) return false;
        int dx = Math.abs(newX - pX);
        int dy = Math.abs(newY - pY);

        return (dx == 0 || dy == 0 || dx == dy);
    }

    //need to add this to all pieces so this is a placeholder for now
    @Override
    public boolean isOverlapStep() {
        return false;
    }

}
