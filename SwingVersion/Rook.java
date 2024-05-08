public class Rook extends Piece{
    public Rook(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
    }

    @Override
    public boolean isValidMove(int pX, int pY, int newX, int newY) {
        if(!isInBounds(newX, newY)) return false;
        if(newX == pX || newY == pY) {
            return true;
        }
        return false;
    }

    //need to add this to all pieces so this is a placeholder for now
    @Override
    public boolean isOverlapStep() {
        return false;
    }

}
