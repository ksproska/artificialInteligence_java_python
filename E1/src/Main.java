import zad1.Factory;
import zad1.InstanceEnum;

public class Main {
    public static void main(String[] args) {
        String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
        Factory factory = new Factory(InstanceEnum.EASY, folderPath);
        System.out.println(factory);

        var initPopulation = factory.createInitMachinesPositions();
        factory.displayPopulation(initPopulation);
        var eval = factory.evaluatePopulation(initPopulation);
        System.out.printf("\nEvaluated: %s", eval);
    }
}
