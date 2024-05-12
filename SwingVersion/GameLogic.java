import java.util.ArrayList;

public class GameLogic {
    protected Piece[][] board;

    protected Player p1, p2;
    protected ArrayList<Piece> capturedPieces;
    protected boolean isWhiteTurn;
    protected boolean isGameOver;

    public GameLogic(String p1N, String p2N) {
        p1 = new Player(p1N, true);
        p2 = new Player(p2N, false);

        capturedPieces = new ArrayList<>();
        board = new Piece[8][8];

        isGameOver = false;
        isWhiteTurn = true;

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
                Piece piece = board[row][col];
                if (piece instanceof King && piece.isWhite == isWhiteTurn) {
                    kingX = col;
                    kingY = row;
                    break;
                }
            }
        }

        // Check if any opponent's piece threatens the king
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.isWhite != isWhiteTurn) {
                    if (piece instanceof Pawn && isPawnThreat(col, row, kingX, kingY)) {
                        return true;
                    } else if (piece instanceof Knight && isKnightThreat(col, row, kingX, kingY)) {
                        return true;
                    } else if (piece.isValidMove(col, row, kingX, kingY)) {
                        if ((piece instanceof Rook || piece instanceof Queen) && hasObstructionsStraight(col, row, kingX, kingY)) {
                            System.out.println(col);
                            System.out.println(row);
                            return false;
                        }
                        if ((piece instanceof Bishop || piece instanceof Queen) && hasObstructionsDiagonal(col, row, kingX, kingY)) {
                            return false;
                        }
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isKnightThreat(int knightX, int knightY, int kingX, int kingY) {
        int[][] knightMoves = { { -2, -1 }, { -1, -2 }, { 1, -2 }, { 2, -1 }, { 2, 1 }, { 1, 2 }, { -1, 2 }, { -2, 1 } };

        for (int[] move : knightMoves) {
            int newX = knightX + move[0];
            int newY = knightY + move[1];
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 && newX == kingX && newY == kingY) {
                return true;
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
        //need to implement this
        return false;
    }

    public boolean movePiece(int x, int y, int newX, int newY) {
        boolean isPawn = board[y][x].getClass().getSimpleName().equals("Pawn");
        boolean isBishop = board[y][x].getClass().getSimpleName().equals("Bishop");
        boolean isRook = board[y][x].getClass().getSimpleName().equals("Rook");
        boolean isQueen = board[y][x].getClass().getSimpleName().equals("Queen");

        if(x == newX && y == newY) return false;
        //Castling
        if(isRook && board[newY][newX] != null && board[newY][newX].getClass().getSimpleName().equals("King") && board[newY][newX].isWhite == isWhiteTurn){
            if(!board[y][x].hasMoved && !board[newY][newX].hasMoved){
                if(x == 7){
                    for(int i = x-1; i > newX; i--){
                        if(board[y][i] != null) return false;
                    }
                    board[y][5] = board[y][x];
                    board[y][x] = null;
                    board[y][6] = board[newY][newX];
                    board[newY][newX] = null;
                    return true;
                } else if (x == 0) {
                    for(int i = x+1; i < newX; i++){
                        if(board[y][i] != null) return false;
                    }
                    board[y][3] = board[y][x];
                    board[y][x] = null;
                    board[y][2] = board[newY][newX];
                    board[newY][newX] = null;
                    return true;
                }
            }
        }

        if(board[y][x].isWhite != isWhiteTurn) return false;
//        if (board[y][x] instanceof King && !isSafeMove(x, y, newX, newY)) {
//            return false;
//        }

//        //broken en passant
//        if (isPawn && x != newX && board[newY][newX] == null) {
//            int direction = (board[y][x].isWhite) ? -1 : 1;
//            Piece adjacentPawn = board[y][newX];
//            if (adjacentPawn instanceof Pawn && adjacentPawn.isWhite != isWhiteTurn && ((Pawn) adjacentPawn).justDoubleMoved) {
//                if ((y == 3 && isWhiteTurn) || (y == 4 && !isWhiteTurn)) {
//                    capturedPieces.add(adjacentPawn);
//                    if (isWhiteTurn) p1.addTakenPiece(adjacentPawn);
//                    else p2.addTakenPiece(adjacentPawn);
//                    board[y][newX] = null;
//                }
//            }
//        }

        if(isPawn) {
            if(isWhiteTurn) {
                if(x == newX && y-1 == newY && board[newY][newX] != null) return false;
            } else if (x == newX && y+1 == newY && board[newY][newX] != null) return false;
        }
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
        return true;
    }

    public boolean isSafeMove(int x, int y, int newX, int newY) {
        Piece originalPiece = board[newY][newX];
        // Execute the move temporarily
        board[newY][newX] = board[y][x];
        board[y][x] = null;

        // Check if the piece is still in check after the move
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
        int xInc = (newX > x) ? 1 : -1;
        int yInc = (newY > y) ? 1 : -1;

        int currX = x + xInc;
        int currY = y + yInc;

        while (currX != newX && currY != newY && currX >= 0 && currX < 8 && currY >= 0 && currY < 8) {
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
