public class King extends Piece {
    public King(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        if(!isInBounds(newX, newY)) return false;
        if(newX <= x+1 && newX >= x-1 && newY <= y+1 && newY >= y-1) {
            return true;
        }
        return false;
    }

    //need to add this to all pieces so this is a placeholder for now
    public boolean isOverlap() {
        return true;
    }
}
