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
}
