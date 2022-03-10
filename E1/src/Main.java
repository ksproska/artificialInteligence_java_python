public class Main {
    public static void main(String[] args) {
        String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
        Factory factory = new Factory(FactoryValues.HARD, folderPath);
        System.out.println(factory);

        var initPopulation = factory.createInitMachinesPositions();
        factory.displayPopulation(initPopulation);
    }
}
