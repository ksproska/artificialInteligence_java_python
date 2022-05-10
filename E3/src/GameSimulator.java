import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class GameSimulator {

    public static PlayerColor runGame(CheckersGridHandler grid, Player white, Player black) {
        ArrayList<Move> allMoves;
        while (!grid.isGameFinished()) {
            allMoves = grid.getAllCurrentPossibleMoves();
            System.out.println("-------------------------------\nAvailable moves count: " + allMoves.size());
            System.out.println(grid);

//            System.out.print("Press ENTER to continue...");
//            scanner.nextLine();
            System.out.println("Counting...");
            if (PlayerColor.WHITE == grid.getCurrentPlayer()) {
                var selectedMove = white.getChosenMove(grid);
                System.out.println("White: " + selectedMove);
                grid.executeMove(selectedMove);
                if (white instanceof GuiHuman) {
                    ((GuiHuman) white).getCheckersGuiGame().updateGrid();
                }
                if (black instanceof GuiHuman) {
                    ((GuiHuman) black).getCheckersGuiGame().updateGrid();
                }
            }
            else {
                var selectedMove = black.getChosenMove(grid);
                System.out.println("Black: " + selectedMove);
                grid.executeMove(selectedMove);
                if (white instanceof GuiHuman) {
                    ((GuiHuman) white).getCheckersGuiGame().updateGrid();
                }
                if (black instanceof GuiHuman) {
                    ((GuiHuman) black).getCheckersGuiGame().updateGrid();
                }
            }
        }
        System.out.println(grid);
        System.out.println("WINNER: " + grid.getWinner());
        if (white instanceof Bot) {
            System.out.println("White bot:");
            ((Bot) white).printAverages();
        }
        if (black instanceof Bot) {
            System.out.println("Black bot:");
            ((Bot) black).printAverages();
        }
        return grid.getWinner();
    }

    public static void main(String[] args) {
        var grid = new CheckersGridHandler();
        grid.basicSetup();

        final CheckersGuiGame[] gui = new CheckersGuiGame[1];
//        Thread thread = new Thread(){
//            public void run(){
//                gui[0] = new CheckersGuiGame(grid);
//                gui[0].updateGrid();
//            }
//        };
//        thread.start();

        var botWhite = new MinMaxBot(new SimpleAccessor(), PlayerColor.WHITE, 7);
        var botBlack = new MinMaxBot(new ComplexGridAccessor(), PlayerColor.BLACK, 7);
        var botBlack2 = new AlphaBetaBot(new SimpleAccessor(), PlayerColor.BLACK, 7);
        var human = new Human();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Press ENTER to continue...");
        scanner.nextLine();
        var humanGui = new GuiHuman(gui[0]);

//        runGame(botWhite, botBlack2);
//        runGame(botWhite, human);
        runGame(grid, botWhite, humanGui);
    }
}
