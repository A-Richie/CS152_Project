public class Knight extends Piece {
    public Knight(int x, int y, boolean isWhite) {
        super(x, y, isWhite);
    }

    @Override
    public boolean isValidMove(int pX, int pY, int newX, int newY) {
        if (!isInBounds(newX, newY)) {
            System.out.println("Move out of bounds");
            return false;
        }

        boolean up1left2 = (newX == pX - 2 && newY == pY - 1);
        boolean up2left1 = (newX == pX - 1 && newY == pY - 2);
        boolean up2right1 = (newX == pX + 1 && newY == pY - 2);
        boolean up1right2 = (newX == pX + 2 && newY == pY - 1);
        boolean down1right2 = (newX == pX + 2 && newY == pY + 1);
        boolean down2right1 = (newX == pX + 1 && newY == pY + 2);
        boolean down2left1 = (newX == pX - 1 && newY == pY + 2);
        boolean down1left2 = (newX == pX - 2 && newY == pY + 1);

        boolean isValid = up1left2 || up2left1 || up2right1 || up1right2 || down1right2 || down2right1 || down2left1 || down1left2;
        return isValid;
    }
}