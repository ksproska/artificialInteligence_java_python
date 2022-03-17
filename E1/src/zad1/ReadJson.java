package zad1;

import java.util.Arrays;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReadJson {
    private static final InstanceEnum[] INSTANCE_ENUMS = new InstanceEnum[]{InstanceEnum.EASY, InstanceEnum.FLAT, InstanceEnum.HARD, InstanceEnum.TEST};
    private static final DataEnum[] DATA_ENUMS = new DataEnum[]{DataEnum.COST, DataEnum.FLOW};
    private static final HashMap<DataEnum, String[]> dataTypeElementSubames = new HashMap<>() {
        {
            put(DataEnum.COST, new String[]{"source", "dest", "cost"});
            put(DataEnum.FLOW, new String[]{"source", "dest", "amount"});
        }
    };
    private static final String FILE_TYPE = "json";

    public static int[][] getData(String folderPath, InstanceEnum instanceEnum, DataEnum dataEnum) {
        // checking if selection parameters are correct
        if(Arrays.stream(INSTANCE_ENUMS).noneMatch(instanceEnum::equals)) {
            String errorMessage = String.format("Instance type: %s; allowed: %s", instanceEnum, Arrays.toString(INSTANCE_ENUMS));
            throw new IllegalArgumentException(errorMessage);
        }
        if(Arrays.stream(DATA_ENUMS).noneMatch(dataEnum::equals)) {
            String errorMessage = String.format("Data type: %s; allowed: %s", dataEnum, Arrays.toString(DATA_ENUMS));
            throw new IllegalArgumentException(errorMessage);
        }
        // reading file
        String filename = String.format("%s\\%s_%s.%s",
                folderPath,
                FactorySetupVals.getInstanceEnumString(instanceEnum),
                FactorySetupVals.getDataEnumString(dataEnum),
                FILE_TYPE);

        try (FileReader reader = new FileReader(filename)) {
            return getValsFromJson(reader, dataEnum);
        }
        catch (IOException | ParseException e) {
            String errorMessage = String.format("Problem reading file \"%s\"", filename);
            System.out.println(errorMessage);
        }
        return null;
    }

    private static int[][] getValsFromJson(FileReader reader, DataEnum dataEnum) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Object parsedReader = jsonParser.parse(reader);
        JSONArray elementsList = (JSONArray) parsedReader;

        String[] elementSubnamesList = dataTypeElementSubames.get(dataEnum);
        int[][] readTables = new int[elementsList.size()][elementSubnamesList.length];

        for (int iElem = 0; iElem < elementsList.size(); iElem++) {
            JSONObject jsonElement = ((JSONObject) elementsList.get(iElem));
            for (int iName = 0; iName < elementSubnamesList.length; iName++) {
                String subname = elementSubnamesList[iName];
                var readValue = jsonElement.get(subname);
                readTables[iElem][iName] = ((Long) (readValue)).intValue();
            }
        }
        return readTables;
    }
}

class ReadJsonTest {
    static String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";

    @Test
    void easyTest() {
        int[][] flow = ReadJson.getData(folderPath, InstanceEnum.EASY, DataEnum.FLOW);
        int[][] cost = ReadJson.getData(folderPath, InstanceEnum.EASY, DataEnum.COST);

        Assertions.assertArrayEquals(flow[0], new int[]{0, 1, 100});
        Assertions.assertArrayEquals(flow[5], new int[]{2, 3, 0});
        Assertions.assertArrayEquals(flow[flow.length - 1], new int[]{7, 8, 12});

        Assertions.assertArrayEquals(cost[0], new int[]{0, 1, 1});
        Assertions.assertArrayEquals(cost[5], new int[]{2, 3, 5});
        Assertions.assertArrayEquals(cost[cost.length - 1], new int[]{7, 8, 1});
    }

    @Test
    void flatTest() {
        int[][] flow = ReadJson.getData(folderPath, InstanceEnum.FLAT, DataEnum.FLOW);
        int[][] cost = ReadJson.getData(folderPath, InstanceEnum.FLAT, DataEnum.COST);

        Assertions.assertArrayEquals(flow[0], new int[]{0, 3, 310});
        Assertions.assertArrayEquals(flow[5], new int[]{11, 9, 120});
        Assertions.assertArrayEquals(flow[flow.length - 1], new int[]{1, 7, 90});

        Assertions.assertArrayEquals(cost[0], new int[]{0, 3, 1});
        Assertions.assertArrayEquals(cost[5], new int[]{11, 9, 1});
        Assertions.assertArrayEquals(cost[cost.length - 1], new int[]{1, 7, 1});
    }

    @Test
    void hardTest() {
        int[][] flow = ReadJson.getData(folderPath, InstanceEnum.HARD, DataEnum.FLOW);
        int[][] cost = ReadJson.getData(folderPath, InstanceEnum.HARD, DataEnum.COST);

        Assertions.assertArrayEquals(flow[0], new int[]{21, 0, 440});
        Assertions.assertArrayEquals(flow[5], new int[]{13, 6, 230});
        Assertions.assertArrayEquals(flow[flow.length - 1], new int[]{12, 18, 160});

        Assertions.assertArrayEquals(cost[0], new int[]{21, 0, 1});
        Assertions.assertArrayEquals(cost[5], new int[]{13, 6, 1});
        Assertions.assertArrayEquals(cost[cost.length - 1], new int[]{12, 18, 1});
    }
}
