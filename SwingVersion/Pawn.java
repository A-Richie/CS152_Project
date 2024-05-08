public class Pawn extends Piece {
    public Pawn(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
    }

    @Override
    public boolean isValidMove(int pX, int pY, int newX, int newY) {
        if(!isInBounds(newX, newY)) return false;
        if (isWhite) {
            if (!hasMoved && newX == pX && (newY == pY - 2 || newY == pY - 1)) {
                hasMoved = true;
                return true;
            }
            return newX == pX && newY == pY - 1;
        }
        else {
            if (!hasMoved && newX == pX && (newY == pY + 2 || newY == pY + 1)) {
                hasMoved = true;
                return true;
            }
            return newX == pX && newY == pY + 1;
        }
    }


    //need to add this to all pieces so this is a placeholder for now
    @Override
    public boolean isOverlapStep() {
        return false;
    }
}