import java.util.ArrayList;
import java.util.Scanner;

public class GameLogic {
    protected Piece[][] board;

    protected Player p1, p2;
    protected ArrayList<Piece> capturedPieces;
    protected boolean isWhiteTurn;
    protected boolean isGameOver;
    protected boolean isInCheck;

    public GameLogic(String p1N, String p2N) {
        p1 = new Player(p1N, true);
        p2 = new Player(p2N, false);

        capturedPieces = new ArrayList<>();
        board = new Piece[8][8];

        isGameOver = false;
        isWhiteTurn = true;
        isInCheck = false;

        //Black Pieces
        board[0][0] = new Rook(0, 0, false);
        board[0][1] = new Knight(0, 1, false);
        board[0][2] = new Bishop(0, 2, false);
        board[0][3] = new Queen(0, 3, false);
        board[0][4] = new King(0, 4, false);
        board[0][5] = new Bishop(0, 5, false);
        board[0][6] = new Knight(0, 6, false);
        board[0][7] = new Rook(0, 7, false);

        //White Pieces
        board[7][0] = new Rook(7, 0, true);
        board[7][1] = new Knight(7, 1, true);
        board[7][2] = new Bishop(7, 2, true);
        board[7][3] = new Queen(7, 3, true);
        board[7][4] = new King(7, 4, true);
        board[7][5] = new Bishop(7, 5, true);
        board[7][6] = new Knight(7, 6, true);
        board[7][7] = new Rook(7, 7, true);

        //Pawns
        for (int i = 0; i < board.length; i++) {
            board[1][i] = new Pawn(1, i, false);
            board[6][i] = new Pawn(6, i, true);
        }
    }

    public boolean isCheck() {
        int kingX = -1;
        int kingY = -1;

        // Find the current player's king
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] instanceof King && board[row][col].isWhite == isWhiteTurn) {
                    kingX = col;
                    kingY = row;
                    break;
                }
            }
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] != null && board[row][col].isWhite != isWhiteTurn) {
                    if(board[row][col] instanceof Pawn){
                        if (isPawnThreat(col, row, kingX, kingY)) return true;
                    } else if (board[row][col].isValidMove(col, row, kingX, kingY)) {
                        if (board[row][col] instanceof Rook || board[row][col] instanceof Queen) {
                            if (hasObstructionsStraight(col, row, kingX, kingY)) continue;
                        }
                        if (board[row][col] instanceof Bishop || board[row][col] instanceof Queen) {
                            if (hasObstructionsDiagonal(col, row, kingX, kingY)) continue;
                        }
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isPawnThreat(int col, int row, int kingX, int kingY) {
        int direction;
        if (board[row][col].isWhite) {
            direction = -1;
        } else {
            direction = 1;
        }

        return (kingX == col + 1 || kingX == col - 1) && kingY == row + direction;
    }

    public boolean isCheckmate() {
        if (!isCheck()) {
            return false;
        }

//        for (int row = 0; row < 8; row++) {
//            for (int col = 0; col < 8; col++) {
//                if (board[row][col] != null && board[row][col].isWhite == isWhiteTurn) {
//                    // Try moving each piece to check if it gets out of check
//                    for (int newRow = 0; newRow < 8; newRow++) {
//                        for (int newCol = 0; newCol < 8; newCol++) {
//                            if (board[newRow][newCol] != null && movePiece(col, row, newCol, newRow)) {
//                                // If moving the piece gets out of check, return false
//                                if (!isCheck()) {
//                                    // Undo the move
//                                    movePiece(newCol, newRow, col, row);
//                                    return false;
//                                }
//                                // Undo the move
//                                movePiece(newCol, newRow, col, row);
//                            }
//                        }
//                    }
//                }
//            }
//        }

        return false;
    }


    public boolean movePiece(int x, int y, int newX, int newY) {
        if(board[y][x].isWhite != isWhiteTurn) return false;
        if (board[y][x] instanceof King && !isSafeMoveForKing(x, y, newX, newY)) {
            return false;
        }
        boolean isPawn = board[y][x].getClass().getSimpleName().equals("Pawn");
        boolean isBishop = board[y][x].getClass().getSimpleName().equals("Bishop");
        boolean isRook = board[y][x].getClass().getSimpleName().equals("Rook");
        boolean isQueen = board[y][x].getClass().getSimpleName().equals("Queen");
        if(isBishop && hasObstructionsDiagonal(x, y, newX, newY)) return false;
        else if(isRook && hasObstructionsStraight(x, y, newX, newY)) return false;
        else if(isQueen && (hasObstructionsStraight(x, y, newX, newY) && hasObstructionsDiagonal(x, y, newX, newY))) return false;
        if(isPawn && isDiagonalPawn(x,y,newX,newY, isWhiteTurn)){
            if(board[newY][newX] == null) return false;
        } else {
            if (!(board[y][x].isValidMove(x, y, newX, newY))) return false;
        }
        if (board[newY][newX] != null) {
            if (board[newY][newX].isWhite == board[y][x].isWhite) return false;
            else {
                capturedPieces.add(board[newY][newX]);
                if (isWhiteTurn) p1.addTakenPiece(board[newY][newX]);
                else p2.addTakenPiece(board[newY][newX]);
            }
        }
        board[newY][newX] = board[y][x];
        if(board[newY][newX].hasMoved == false) board[newY][newX].hasMoved = true;
        board[y][x].setX(newX);
        board[y][x].setY(newY);
        board[y][x] = null;
        if(isInCheck) isInCheck = false;
        return true;
    }

    private boolean isSafeMoveForKing(int x, int y, int newX, int newY) {
        Piece originalPiece = board[newY][newX];
        // Execute the move temporarily
        board[newY][newX] = board[y][x];
        board[y][x] = null;

        // Check if the king is still in check after the move
        boolean isSafe = !isCheck();

        // Undo the move
        board[y][x] = board[newY][newX];
        board[newY][newX] = originalPiece;

        return isSafe;
    }

    private boolean hasObstructionsStraight(int x, int y, int newX, int newY) {
        int xInc, yInc;
        if (newX > x) xInc = 1;
        else if (newX < x) xInc = -1;
        else xInc = 0;
        if (newY > y) yInc = 1;
        else if (newY < y) yInc = -1;
        else yInc = 0;

        if (xInc != 0) {
            int currentX = x + xInc;
            while (currentX != newX) {
                if (board[y][currentX] != null) return true;
                currentX += xInc;
            }
        }

        if (yInc != 0) {
            int currentY = y + yInc;
            while (currentY != newY) {
                if (board[currentY][x] != null) return true;
                currentY += yInc;
            }
        }

        return false;
    }

    private boolean hasObstructionsDiagonal(int x, int y, int newX, int newY) {
        int xInc, yInc;
        if(newX > x) xInc = 1;
        else xInc = -1;
        if(newY > y) yInc = 1;
        else yInc = -1;

        int currX = x + xInc;
        int currY = y + yInc;

        while (currX != newX && currY != newY) {
            if (board[currY][currX] != null) return true;
            currX += xInc;
            currY += yInc;
        }
        return false;
    }

    private boolean isDiagonalPawn(int x, int y, int newX, int newY, boolean whiteTurn) {
        if (whiteTurn) return (newX == x + 1 || newX == x - 1) && newY == y - 1;
        else return (newX == x + 1 || newX == x - 1) && newY == y + 1;
    }

    public boolean checkValidity(int row, int col) {
        if(isWhiteTurn && board[row][col] != null) {
            if(board[row][col].isWhite) return true;
        } else if(!isWhiteTurn && board[row][col] != null){
            if(!board[row][col].isWhite) return true;
        }
        return false;
    }
}
