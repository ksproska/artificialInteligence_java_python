import zad1.Factory;
import zad1.InstanceEnum;

public class Main {
    public static void main(String[] args) {
        String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
        Factory factory = new Factory(InstanceEnum.EASY, folderPath);
        System.out.println(factory);

        int[] initMachinesPositions = factory.createInitMachinesPositions();
        factory.displayMachinesPositions(initMachinesPositions);

        int eval = factory.evaluateMachinesPositions(initMachinesPositions);
        System.out.printf("\nEvaluated: %s", eval);
    }
}
