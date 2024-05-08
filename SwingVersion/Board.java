import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {

    private final JFrame gameGUI;
    private final BoardPanel boardPanel;
    private static final Dimension OUTTER_FRAME_SIZE = new Dimension(600, 600);
    private static final Dimension BOARD_DIMENSION = new Dimension(400, 350);
    private static final Dimension TILE_DIMENSION = new Dimension(400, 350);



    public Board() {
        //Creates the board for the GUI
        this.gameGUI = new JFrame("Chess Game");
        this.gameGUI.setLayout(new BorderLayout());

        this.boardPanel = new BoardPanel();

        this.gameGUI.add(this.boardPanel, BorderLayout.CENTER);
        this.gameGUI.setSize(OUTTER_FRAME_SIZE);
        //Displays the board
        this.gameGUI.setVisible(true);
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
    }

    private class TilePanel extends JPanel {
            private final int row;
            private final int column;

            TilePanel(final BoardPanel boardPanel, final int row, final int column)
            {
                super(new GridBagLayout());
                this.row = row;
                this.column = column;
                setPreferredSize(TILE_DIMENSION);
                assignTileColor(row, column);

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
    }
}
