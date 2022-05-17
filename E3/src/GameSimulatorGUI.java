import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameSimulatorGUI {
    private final ImageIcon whiteIcon, blackIcon, whiteCrownedIcon, blackCrownedIcon;
    private final CheckersGridHandler grid;
    private final ArrayList<ArrayList<JButton>> buttons;
    private GridItem fromItem, toItem;
    private final Bot black;
    private final JFrame frame;

    public void resetInput() {
        fromItem = null;
        toItem = null;
    }

    public GameSimulatorGUI(CheckersGridHandler grid, Bot black) {
        this.black = black;
        this.grid = grid;
        frame = new JFrame("Checkers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        var whiteIconTemp = new ImageIcon("D:\\sztuczna_inteligencja\\E3\\src\\images\\white.png");
        whiteIcon = new ImageIcon(whiteIconTemp.getImage().getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH));
        var whiteCrownedIconTemp = new ImageIcon("D:\\sztuczna_inteligencja\\E3\\src\\images\\white_crowned.png");
        whiteCrownedIcon = new ImageIcon(whiteCrownedIconTemp.getImage().getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH));
        var blackIconTemp = new ImageIcon("D:\\sztuczna_inteligencja\\E3\\src\\images\\black.png");
        blackIcon = new ImageIcon(blackIconTemp.getImage().getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH));
        var blackIconCrownedTemp = new ImageIcon("D:\\sztuczna_inteligencja\\E3\\src\\images\\black_crowned.png");
        blackCrownedIcon = new ImageIcon(blackIconCrownedTemp.getImage().getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH));

        buttons = new ArrayList<ArrayList<JButton>>();
        for (var row : CheckersGrid.board) {
            var temp = new ArrayList<JButton>();
            for (var item : row) {
                var button = new JButton();
                switch (item.gridItemColor) {
                    case BLACK -> button.setBackground(Color.GRAY);
                    case WHITE -> {
                        button.setBackground(Color.WHITE);
                        button.setEnabled(false);
                    }
                }
                temp.add(button);
                frame.add(button);
            }
            buttons.add(temp);
        }
        for (int i = 0; i < buttons.size(); i++) {
            for (int j = 0; j < buttons.get(i).size(); j++) {
                var button = buttons.get(i).get(j);
                int finalI = i;
                int finalJ = j;
                button.addActionListener(e -> {
                    if (fromItem == null) {
                        fromItem = CheckersGrid.board.get(finalI).get(finalJ);
                        button.setBackground(Color.RED);
                    } else {
                        toItem = CheckersGrid.board.get(finalI).get(finalJ);
                        executeMove();
                    }
                });
            }
        }
        frame.setLayout(new GridLayout(8, 8));
        frame.setVisible(true);
        frame.setResizable(false);
        updateGrid();
    }

    public void executeMove() {
        ArrayList<Move> allMoves = grid.checkersGrid.getAllCurrentPossibleMoves();
        for (var move : allMoves) {
            if (move.startingPoint == fromItem) {
                if (move.toJumpItems.get(move.toJumpItems.size() - 1) == toItem) {
                    grid.executeMove(move);
                    System.out.println("\n----------------------------------------");
                    System.out.println("White: " + move);
                    System.out.println(grid);
                    resetInput();
//                    System.out.println(black.getAccessor().accessCheckersGrid(grid.getCheckersGrid(), PlayerColor.BLACK, null));
                    updateGrid();
                    if (grid.isGameFinished()) {
                        System.out.println("WINNER: " + grid.getWinner());
                        black.printStats();
//                        for (var row : buttons) {
//                            for (var button : row) {
//                                button.setEnabled(false);
//                            }
//                        }
                        return;
                    }
                    var selectedMove = black.getChosenMove(grid);
                    System.out.println("\n----------------------------------------");
                    System.out.println("Black: " + selectedMove);
                    grid.executeMove(selectedMove);
                    System.out.println(grid);
                    updateGrid();
                    if (grid.isGameFinished()) {
                        System.out.println("WINNER: " + grid.getWinner());
                        black.printStats();
//                        for (var row : buttons) {
//                            for (var button : row) {
//                                button.setEnabled(false);
//                            }
//                        }
                    }
                    return;
                }
            }
        }
        updateGrid();
        System.out.println("\n" + "\u001B[43;30m" + "INCORRECT INPUT!" + "\u001B[0m" + "\nPossible moves:");
        for (var move : allMoves) {
            System.out.println(move);
        }
        resetInput();
    }

    public void updateGrid() {
        var allJumpedTo = new ArrayList<GridItem>();
        if (grid.getLastMove() != null) {
            allJumpedTo = grid.getLastMove().getAllJumpedTo();
        }
        for (int i = 0; i < CheckersGrid.board.size(); i++) {
            for (int j = 0; j < CheckersGrid.board.get(i).size(); j++) {
                var gridItem = CheckersGrid.board.get(i).get(j);
                var figure = grid.checkersGrid.getFigures().get(i).get(j);
                var button = buttons.get(i).get(j);
                if (allJumpedTo.contains(gridItem)) {
                    button.setBackground(Color.CYAN);
                }
                else if (gridItem.gridItemColor == GridItemColor.BLACK) {
                    button.setBackground(Color.GRAY);
                }
                if (figure != null) {
                    switch (figure.figureType) {
                        case NORMAL -> {
                            switch (figure.playerColor) {
                                case WHITE -> button.setIcon(whiteIcon);
                                case BLACK -> button.setIcon(blackIcon);
                            }
                        }
                        case CROWNED -> {
                            switch (figure.playerColor) {
                                case WHITE -> button.setIcon(whiteCrownedIcon);
                                case BLACK -> button.setIcon(blackCrownedIcon);
                            }
                        }
                    }
                }
                else {
                    button.setIcon(null);
                }
            }
        }
    }

    public static void main(String[] args) {
        var botBlack = new BotAlphaBeta(new SimpleAccessor(), PlayerColor.BLACK, 7);
        var checkersGridHandler = new CheckersGridHandler();
        checkersGridHandler.basicSetup();
//        checkersGridHandler.exampleSetup2();
        new GameSimulatorGUI(checkersGridHandler, botBlack);
    }
}
