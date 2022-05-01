import org.junit.Assert;
import org.junit.jupiter.api.Test;

class CheckersGridTest {
    @Test
    void multipleCrownedJumps() {
        var grid = new CheckersGrid();
        grid.exampleSetup4();
        System.out.println(grid);
        Assert.assertEquals(1, grid.getAllCurrentPossibleMoves().size());
        grid.executeMove(grid.getAllCurrentPossibleMoves().get(0));
        System.out.println(grid);
        Assert.assertEquals("""
                NEXT: BLACK
                B8 W C
                H8 W C
                E3 B N
                """, grid.currentState());
    }

    @Test
    void multipleNormalJumps() {
        var grid = new CheckersGrid();
        grid.exampleSetup5();
        System.out.println(grid);
        Assert.assertEquals(2, grid.getAllCurrentPossibleMoves().size());
        grid.executeMove(grid.getAllCurrentPossibleMoves().get(0));
        System.out.println(grid);
        Assert.assertEquals("""
                NEXT: BLACK
                B6 B N
                D6 B N
                F4 B N
                E1 W N
                """, grid.currentState());
    }

    @Test
    void maxPossibleJump() {
        var grid = new CheckersGrid();
        grid.exampleSetup6();
        System.out.println(grid);
        System.out.println(grid.getAllCurrentPossibleMoves());
        Assert.assertEquals(12, grid.getAllCurrentPossibleMoves().size());
        grid.executeMove(grid.getAllCurrentPossibleMoves().get(0));
        System.out.println(grid);
        Assert.assertEquals("""
                NEXT: BLACK
                B2 W C
                """, grid.currentState());
    }

    @Test
    void maxNormalPossibleJump() {
        var grid = new CheckersGrid();
        grid.exampleSetup7();
        System.out.println(grid);
        System.out.println(grid.getAllCurrentPossibleMoves());
//        Assert.assertEquals(4, grid.getAllMoves().size());
        grid.executeMove(grid.getAllCurrentPossibleMoves().get(0));
        System.out.println(grid);
//        Assert.assertEquals("""
//                NEXT: BLACK
//                B4 W C
//                """, grid.currentState());
    }

}
