public class Pawn extends Piece {
    public Pawn(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
    }

    @Override
    public boolean isValidMove(int pX, int pY, int newX, int newY) {
        if(!isInBounds(newX, newY)) return false;
        if(hasMoved == false){
            if(isWhite){
                if(newX == pX && newY <= pY-2) return true;
            } else {
                if(newX == pX && newY <= pY+2) return true;
            }
        } else {
            if(isWhite){
                if(newX == pX && newY <= pY-1) return true;
            } else {
                if(newX == pX && newY <= pY+1) return true;
            }
        }
        if(isOverlapStep() == true) {
            if(isWhite){
                if((newX == pX+1 && newY <= pY-1) || (newX == pX-1 && newY <= pY-1)) return true;
            } else {
                if((newX == pX+1 && newY <= pY+1) || (newX == pX-1 && newY <= pY+1)) return true;
            }
        }

        return false;
    }

    //need to add this to all pieces so this is a placeholder for now
    @Override
    public boolean isOverlapStep() {
        return false;
    }
}