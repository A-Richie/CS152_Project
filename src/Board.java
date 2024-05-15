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
import static java.awt.Component.BOTTOM_ALIGNMENT;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Board {

    private final JFrame gameGUI;
    private final BoardPanel boardPanel;

    private static JPanel capturePanel;
    private final JPanel playerPanel;
    private static JPanel player1Panel;
    private static JPanel player2Panel;
    private int startTileRow;
    private int startTileCol;
    private int destTileRow;
    private int destTileCol;
    private static final Dimension OUTTER_FRAME_SIZE = new Dimension(800, 600);
    private static final Dimension BOARD_DIMENSION = new Dimension(500, 500);
    private static final Dimension TILE_DIMENSION = new Dimension(400, 350);

    private static final Font pieceFont = new Font("Arial", Font.PLAIN, 14);
    static Font captureFont = new Font("Arial", Font.PLAIN, 20); // font for major events
    private static String player1;
    private String player2;

    private static JLabel checkLabel;

    private static boolean checkDisplayFlag;

    private static int checkDisplayNumber;
    GameLogic game;


    //Default board constructor which sets up all the graphical interfaces and the selection of pieces.
    public Board() {
        //Set up Check Label


        checkDisplayFlag = false;
        checkDisplayNumber = 0;
        //Creates the board for the GUI
        player1 = javax.swing.JOptionPane.showInputDialog("What is the name of Player 1 (White)?");
        player2 = javax.swing.JOptionPane.showInputDialog("What is the name of Player 2 (Black)?");
        Font captureFont = new Font("Arial", Font.PLAIN, 20); // Change the font size as needed
        Font nameFont = new Font("Arial", Font.PLAIN, 16); // Font for the Player Names

        this.game = new GameLogic(player1, player2);
        this.gameGUI = new JFrame("Chess Game");
        this.gameGUI.setLayout(new BorderLayout());
        startTileRow = -1;
        startTileCol = -1;
        destTileRow = -1;
        destTileCol = -1;

        this.boardPanel = new BoardPanel();
        this.boardPanel.setAllPieces(game.board);

        /* Adds stuff for the GUI - add player names: */
        this.capturePanel = new JPanel();
        this.playerPanel = new JPanel();
        this.player1Panel = new JPanel();
        this.player2Panel = new JPanel();
        this.capturePanel.setLayout(new BoxLayout(capturePanel, BoxLayout.Y_AXIS));
        this.playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS)); // Vertical layout
        this.player1Panel.setLayout(new BoxLayout(player1Panel, BoxLayout.Y_AXIS));
        this.player2Panel.setLayout(new BoxLayout(player2Panel, BoxLayout.Y_AXIS));
        this.player1Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel player1Label = new JLabel("Player 1: " + player1);
        JLabel player2Label = new JLabel("Player 2: " + player2);
        JLabel capturedLabel = new JLabel("Captured Pieces");
        // player1Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to player1Panel
        // playerPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN)); // Add border to player1Panel
        // capturePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY)); // Add border to player1Panel

        //Set fonts:
        capturedLabel.setFont(captureFont);
        player1Label.setFont(nameFont);
        player2Label.setFont(nameFont);
        player1Label.setAlignmentY(Component.TOP_ALIGNMENT); // Align text center vertically
        player2Label.setAlignmentY(Component.TOP_ALIGNMENT); // Align text center vertically
        player1Panel.setAlignmentY(Component.TOP_ALIGNMENT); // Align the entire panel to the top
        player2Panel.setAlignmentY(Component.TOP_ALIGNMENT); // Align at the top
        capturedLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        capturedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        checkLabel = new JLabel("In Check: " );
        checkLabel.setFont(captureFont);
        checkLabel.setAlignmentY(BOTTOM_ALIGNMENT);
        checkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.capturePanel.add(checkLabel);

        this.capturePanel.add(capturedLabel);

        this.player1Panel.add(player1Label);
        this.player2Panel.add(player2Label);
        // Add space between player 1 and player 2 panels
        this.player1Panel.add(Box.createRigidArea(new Dimension(10, 0))); //Creates border that has empty space
        this.player2Panel.add(Box.createRigidArea(new Dimension(10, 0)));
        this.playerPanel.add(player1Panel);
        this.playerPanel.add(player2Panel);
        this.capturePanel.add(playerPanel);

        this.gameGUI.add(this.boardPanel, BorderLayout.CENTER);
        this.gameGUI.add(this.capturePanel, BorderLayout.EAST);
        this.gameGUI.setSize(OUTTER_FRAME_SIZE);
        this.gameGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Displays the board
        this.gameGUI.setVisible(true);
    }

    public static void updateCapture(String name, String piece) {
        JLabel pieceLabel = new JLabel(piece.toString());
        pieceLabel.setFont(pieceFont);

        //pieceLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        if(name.equals(player1)){
            player1Panel.add(pieceLabel);
        }
        //if the player that captured a piece is player 2 add the name to the corresponding name
        else {
            player2Panel.add(pieceLabel);
        }
    }
    public static void showCheck(String name) {

        if(!checkDisplayFlag) {
            // Show a simple message dialog
            checkLabel.setText("In Check: " + name);
            //capturePanel.remove(checkLabel);
            checkDisplayFlag = true;
        }


    }

    //Removes the CheckLabel if it's displayed
    public static void removeCheck() {

        if(checkDisplayFlag) {
            checkLabel.setText("In Check: ");
            checkDisplayFlag = false;
        }
    }

    public static void showCheckMate()
    {
        JOptionPane.showMessageDialog(null, "Checkmate");
    }

    //Gets run when the second piece is selected. This does the basic game flow where character switches, moves the piece, checks or check or checkmate, etc.
    public void gameFlow(){
        if (destTileRow != -1 && destTileCol != -1) {
            if (game.isSafeMove(startTileCol, startTileRow, destTileCol, destTileRow) && game.movePiece(startTileCol, startTileRow, destTileCol, destTileRow)) {
                updateBoard();
                game.isWhiteTurn = !game.isWhiteTurn;
            }
            startTileRow = -1;
            startTileCol = -1;
            destTileRow = -1;
            destTileCol = -1;
            removeBorder();

            if(game.isCheck()){
                if(game.isWhiteTurn)
                {
                    showCheck(game.p1.getName());
                }
                else
                    showCheck(game.p2.getName());

                if(game.isCheckmate()) {
                    showCheckMate();
                    System.exit(0);
                }
            }
            else {
                removeCheck();
            }
            updateBoard();
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
            // String defaultStartPath = "Src/Chess_Pieces/";  // For Alyssa's IDE
            // String defaultStartPath = "SwingVersion/Chess_Pieces/";
            String defaultStartPath = "src/Chess_Pieces/";
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