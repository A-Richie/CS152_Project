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
        this.gameGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Properly close the application
        //Displays the board
        this.gameGUI.setVisible(true);
    }

    public void gameFlow(){
        if (startTileRow != -1 && startTileCol != -1 && destTileRow != -1 && destTileCol != -1) {
            if (game.movePiece(startTileCol, startTileRow, destTileCol, destTileRow)) {
                //boardPanel.setAllPieces(game.board);
            }
            // Reset start and destination tile coordinates
            startTileRow = -1;
            startTileCol = -1;
            destTileRow = -1;
            destTileCol = -1;
        }
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

            setFocusable(true); // Enable mouse events for this panel
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if(startTileRow == -1 || startTileCol == -1) {
                        //first click
                        if(game.checkValidity(row,column)){
                            setBorder(BorderFactory.createLineBorder(Color.CYAN,3));
                            startTileRow =  row;
                            startTileCol = column;
                        }
                    } else if(destTileRow == -1 || destTileCol == -1){ // verify this
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
                    System.out.println("Clicked on row: " + row + ", column: " + column);
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