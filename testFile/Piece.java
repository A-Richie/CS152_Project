public abstract class Piece {
    protected final boolean isWhite;
    private int x, y; // (0,0) is set to top left as default by JavaFX (more consistent this way)
    //private final String name;
    protected boolean hasMoved;

    public Piece(int x, int y, boolean isWhite) {
        this.x = x;
        this.y = y;
        //this.name = name;
        this.isWhite = isWhite;
        this.hasMoved = false;
    }

    public abstract boolean isValidMove(int pX, int pY, int newX, int newY);


    public boolean isInBounds(int newX, int newY){
        if(newX >= 0 && newX <= 7 && newY >= 0 && newY <= 7) return true;
        return false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        hasMoved = true;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        hasMoved = true;
    }
    //public String returnName() {
        //return this.name;
    //}
}

