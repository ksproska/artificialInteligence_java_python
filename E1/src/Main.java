public class Main {
    public static void main(String[] args) {
        String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
        Factory factory = new Factory(FactorySetupVals.EASY, folderPath);
        System.out.println(factory);

        var initPopulation = factory.createInitMachinesPositions();
        factory.displayPopulation(initPopulation);
        var eval = factory.evaluatePopulation(initPopulation);
        System.out.printf("\nEvaluated: %s", eval);
    }
}
