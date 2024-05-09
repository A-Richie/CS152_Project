public class Pawn extends Piece {
    protected boolean justDoubleMoved;
    public Pawn(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
        justDoubleMoved = false;
    }

    public void setJustDoubleMoved(boolean value) {
        justDoubleMoved = value;
    }

    @Override
    public boolean isValidMove(int pX, int pY, int newX, int newY) {
        if(!isInBounds(newX, newY)) return false;
        if (isWhite) {
            if (!hasMoved && newX == pX && (newY == pY - 2 || newY == pY - 1)) {
                hasMoved = true;
                justDoubleMoved = true;
                return true;
            }
            justDoubleMoved = false;
            return newX == pX && newY == pY - 1;
        }
        else {
            if (!hasMoved && newX == pX && (newY == pY + 2 || newY == pY + 1)) {
                hasMoved = true;
                justDoubleMoved = true;
                return true;
            }
            justDoubleMoved = false;
            return newX == pX && newY == pY + 1;
        }
    }
}