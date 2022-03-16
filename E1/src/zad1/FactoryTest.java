package zad1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class FactoryTest {
    static String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";

    @Test
    void FactoryGetXYTest() {
        var hardF = new Factory(InstanceEnum.HARD, folderPath); // 5x6

        Assertions.assertEquals(hardF.getX(13), 3);
        Assertions.assertEquals(hardF.getY(13), 2);

        Assertions.assertEquals(hardF.getX(7), 2);
        Assertions.assertEquals(hardF.getY(7), 1);

        Assertions.assertEquals(hardF.getX(9), 4);
        Assertions.assertEquals(hardF.getY(9), 1);

        Assertions.assertEquals(hardF.getX(4), 4);
        Assertions.assertEquals(hardF.getY(4), 0);

        Assertions.assertEquals(hardF.getX(15), 0);
        Assertions.assertEquals(hardF.getY(15), 3);
    }

    @Test
    void FactoryEasyTest() {
        var factory = new Factory(InstanceEnum.EASY, folderPath); // 3x3
        Assertions.assertEquals(7664, factory.evaluateGrid(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}));
    }

    @Test
    void FactoryTestTest() {
        var factory = new Factory(InstanceEnum.TEST, folderPath); // 3x2
        Assertions.assertEquals(4 + 3, factory.evaluateGrid(new int[]{0, 1, 2, 3, 4, 5}));
        Assertions.assertEquals(5, factory.evaluateGrid(new int[]{0, 1, 2, 5, 4, 3}));
        Assertions.assertEquals(5, factory.evaluateGrid(new int[]{0, 3, 4, 1, 2, 5}));
        Assertions.assertEquals(2 + 2 + 1 + 2 + 2, factory.evaluateGrid(new int[]{0, 4, 2, 5, 1, 3}));
        Assertions.assertEquals(3 + 2 + 3 + 2 + 1, factory.evaluateGrid(new int[]{0, 5, 3, 2, 4, 1}));
    }
}
