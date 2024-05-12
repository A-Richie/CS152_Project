import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Board {

    private final JFrame gameGUI;
    private final BoardPanel boardPanel;
    private int startTileRow;
    private int startTileCol;
    private int destTileRow;
    private int destTileCol;
    private static final Dimension OUTTER_FRAME_SIZE = new Dimension(600, 600);
    private static final Dimension BOARD_DIMENSION = new Dimension(400, 350);
    private static final Dimension TILE_DIMENSION = new Dimension(400, 350);
    GameLogic game;


    //Default board constructor which sets up all the graphical interfaces and the selection of pieces.
    public Board() {
        //Creates the board for the GUI
        String player1 = javax.swing.JOptionPane.showInputDialog("What is the name of Player 1 (White)?");
        String player2 = javax.swing.JOptionPane.showInputDialog("What is the name of Player 2 (Black)?");
        this.game = new GameLogic(player1, player2);
        this.gameGUI = new JFrame("Chess Game");
        this.gameGUI.setLayout(new BorderLayout());
        startTileRow = -1;
        startTileCol = -1;
        destTileRow = -1;
        destTileCol = -1;

        this.boardPanel = new BoardPanel();
        this.boardPanel.setAllPieces(game.board);

        this.gameGUI.add(this.boardPanel, BorderLayout.CENTER);
        this.gameGUI.setSize(OUTTER_FRAME_SIZE);
        this.gameGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Displays the board
        this.gameGUI.setVisible(true);
    }

    //Gets run when the second piece is selected. This does the basic game flow where character switches, moves the piece, checks or check or checkmate, etc.
    public void gameFlow(){
        if (destTileRow != -1 && destTileCol != -1) {
            if (game.isSafeMove(startTileCol, startTileRow, destTileCol, destTileRow) && game.movePiece(startTileCol, startTileRow, destTileCol, destTileRow)) {
                if(game.isCheck()){
                    System.out.println("check!");
                    if(game.isCheckmate()){
                        game.isGameOver = true;
                        System.exit(0);
                    } else {
                        //Add code to display that it is check and that the king should be the only piece moving.
                    }
                }
                updateBoard();
                game.isWhiteTurn = !game.isWhiteTurn;
            }
            startTileRow = -1;
            startTileCol = -1;
            destTileRow = -1;
            destTileCol = -1;
            removeBorder();
        }
    }

    //Removes border around selected piece when done with turn.
    private void removeBorder() {
        for (int i = 0; i < boardPanel.boardTiles.size(); i++) {
            boardPanel.boardTiles.get(i).setBorder(null);
        }
    }

    //Updates board to change positions of pieces when turn is complete.
    public void updateBoard() {
        boardPanel.removePieces();
        boardPanel.setAllPieces(game.board);
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;
        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for(int row = 0; row < 8; row++)
            {
                for(int column = 0; column < 8; column++)
                {
                    final TilePanel tilePanel = new TilePanel(this, row, column);
                    this.boardTiles.add(tilePanel);
                    add(tilePanel);
                }

            }
        }

        //Sets all the pieces up on the board
        void setAllPieces(Piece[][] board){
            for (int i = 0; i < boardTiles.size(); i++) {
                if(board[boardTiles.get(i).getRow()][boardTiles.get(i).getCol()] != null) {
                    boardTiles.get(i).setPiece(board[boardTiles.get(i).getRow()][boardTiles.get(i).getCol()]);
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return OUTTER_FRAME_SIZE;
        }

        //Clears all pieces for preparation for updating the board.
        public void removePieces() {
            for (int i = 0; i < boardTiles.size(); i++) {
                if(boardTiles.get(i).pieceLabel != null) boardTiles.get(i).remove(boardTiles.get(i).pieceLabel);
            }
        }
    }

    private class TilePanel extends JPanel {
        private final int row;
        private final int column;
        private JLabel pieceLabel;


        TilePanel(final BoardPanel boardPanel, final int row, final int column)
        {
            super(new GridBagLayout());
            this.row = row;
            this.column = column;
            setPreferredSize(TILE_DIMENSION);
            assignTileColor(row, column);

            setFocusable(true);

            //Main listener for the selection of pieces
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (game.isGameOver) {
                        return;
                    }
                    if(startTileRow == -1 && startTileCol == -1) {
                        //first click
                        if(game.checkValidity(row,column)){
                            setBorder(BorderFactory.createLineBorder(Color.CYAN,3));
                            startTileRow =  row;
                            startTileCol = column;
                        }
                    } else if(destTileRow == -1 || destTileCol == -1 && (startTileCol != destTileCol && startTileRow != destTileRow)){ // verify this
                        //second click
                        destTileRow = row;
                        destTileCol = column;
                        gameFlow();
                    } else if(isRightMouseButton(e)) {
                        //cancel
                        startTileRow = -1;
                        startTileCol = -1;
                        destTileRow = -1;
                        destTileCol = -1;
                        setBorder(null);
                    }
                    //System.out.println("Clicked on row: " + row + ", column: " + column);
                }
            });

        }

        public int getRow() {
            return row;
        }
        public int getCol(){
            return column;
        }

        private void assignTileColor(int row, int column) {
            if(row % 2 == 0)
            {
                if(column % 2 == 0)
                {
                    setBackground(Color.white);
                }
                else{
                    setBackground(Color.lightGray);
                }

            }
            else {
                if(column % 2 == 0)
                {
                    setBackground(Color.lightGray);
                }
                else{
                    setBackground(Color.white);
                }
            }
        }

        //Algorithm for setting individual pieces up.
        public void setPiece(Piece piece) {
            String defaultStartPath = "SwingVersion/Chess_Pieces/";
            String color;
            if(piece.isWhite) color = "W_";
            else color = "B_";
            try{
                BufferedImage img = ImageIO.read(new File(defaultStartPath + color + piece.getClass().getSimpleName() + ".png"));
                Image scaledImage = img.getScaledInstance(TILE_DIMENSION.width/8, TILE_DIMENSION.height/7, Image.SCALE_SMOOTH);
                pieceLabel = new JLabel(new ImageIcon (scaledImage));
                add(pieceLabel);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}