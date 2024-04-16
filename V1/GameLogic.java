import java.util.ArrayList;
import java.util.Scanner;

public class GameLogic {
    // this needs to be worked on

    private Piece[][] board;
    private Player p1, p2;
    private ArrayList<Piece> capturedPieces;
    private boolean isGameOver;

    public GameLogic(String p1N, String p2N) {
        p1 = new Player(p1N, true);
        p2 = new Player(p2N, false);

        capturedPieces = new ArrayList<>();
        board = new Piece[8][8];

        isGameOver = false;

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

    public void gameFlow() {
        Scanner scan = new Scanner(System.in);
        boolean whiteTurn = true;
        String pieceToPlay = "";
        String putPiece = "";
        while(isGameOver == false){
            if(whiteTurn) System.out.println(p1.getName() + "'s Turn");
            else System.out.println(p2.getName() + "'s Turn");
            System.out.println("Enter piece to move in format 'xy' (ex. (5,3) is 53):");
            pieceToPlay = scan.nextLine();
            int x = Integer.parseInt(pieceToPlay.substring(0,1));
            int y = Integer.parseInt(pieceToPlay.substring(1));
            System.out.println("Enter location of piece in the same format:");
            putPiece = scan.nextLine();
            int newX = Integer.parseInt(putPiece.substring(0,1));
            int newY = Integer.parseInt(putPiece.substring(1));
            movePiece(x,y,newX,newY, whiteTurn);
            displayBoard();
            whiteTurn = !whiteTurn;
        }
    }

    private boolean movePiece(int x, int y, int newX, int newY, boolean whiteTurn) {
        if(board[y][x].isWhite != whiteTurn) return false;
        boolean isPawn = board[y][x].getClass().getSimpleName().equals("Pawn");
        boolean isBishop = board[y][x].getClass().getSimpleName().equals("Bishop");
        boolean isRook = board[y][x].getClass().getSimpleName().equals("Rook");
        boolean isQueen = board[y][x].getClass().getSimpleName().equals("Queen");
        if(isBishop && hasObstructionsDiagonal(x, y, newX, newY)) return false;
        else if(isRook && hasObstructionsStraight(x, y, newX, newY)) return false;
        else if(isQueen && (hasObstructionsStraight(x, y, newX, newY) || hasObstructionsStraight(x, y, newX, newY))) return false;
        if(isPawn && isDiagonalPawn(x,y,newX,newY, whiteTurn)){
            if(board[newY][newX] == null) return false;
        } else {
            if (!(board[y][x].isValidMove(x, y, newX, newY))) return false;
        }
        if (board[newY][newX] != null) {
            if (board[newY][newX].isWhite == board[y][x].isWhite) return false;
            else {
                capturedPieces.add(board[newY][newX]);
                if (whiteTurn) p1.addTakenPiece(board[newY][newX]);
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

    public void displayBoard(){
        System.out.println("x 0 1 2 3 4 5 6 7");
        for (int row = 0; row < 8; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece == null) {
                    System.out.print("- "); // Empty square
                } else {
                    switch (piece.getClass().getSimpleName()) {
                        case "Pawn":
                            System.out.print(piece.isWhite ? "P " : "p ");
                            break;
                        case "Rook":
                            System.out.print(piece.isWhite ? "R " : "r ");
                            break;
                        case "Knight":
                            System.out.print(piece.isWhite ? "N " : "n ");
                            break;
                        case "Bishop":
                            System.out.print(piece.isWhite ? "B " : "b ");
                            break;
                        case "Queen":
                            System.out.print(piece.isWhite ? "Q " : "q ");
                            break;
                        case "King":
                            System.out.print(piece.isWhite ? "K " : "k ");
                            break;
                        default:
                            System.out.print("? ");
                            break;
                    }
                }
            }
            System.out.println(); // Move to the next row
        }
        System.out.println("Captured Pieces:");
        System.out.println("White: " + p1.getTakenPieces());
        System.out.println("Black: " + p2.getTakenPieces());
    }
}
