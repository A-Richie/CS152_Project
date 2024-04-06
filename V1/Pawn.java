public class Pawn extends Piece {
    public Pawn(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
    }

    @Override
    public boolean isValidMove(int newX, int newY) {
        if(!isInBounds(newX, newY)) return false;
        if(hasMoved == false){
            if(isWhite){
                if(newX == x && newY <= y-2) return true;
            } else {
                if(newX == x && newY <= y+2) return true;
            }
        } else {
            if(isWhite){
                if(newX == x && newY <= y-1) return true;
            } else {
                if(newX == x && newY <= y+1) return true;
            }
        }
        if(isOverlap() == true) {
            if(isWhite){
                if((newX == x+1 && newY <= y-1) || (newX == x-1 && newY <= y-1)) return true;
            } else {
                if((newX == x+1 && newY <= y+1) || (newX == x-1 && newY <= y+1)) return true;
            }
        }

        return false;
    }

    //need to add this to all pieces so this is a placeholder for now
    public boolean isOverlap() {
        return true;
    }
}