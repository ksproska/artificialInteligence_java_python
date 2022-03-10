package zad1;

import java.util.Arrays;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadJson {
    private static final InstanceEnum[] INSTANCE_ENUMS = new InstanceEnum[]{InstanceEnum.EASY, InstanceEnum.FLAT, InstanceEnum.HARD};
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
        String filename = String.format("%s\\%s_%s.%s", folderPath, FactorySetupVals.instanceTypeStringHashMap.get(instanceEnum), FactorySetupVals.dataTypeStringHashMap.get(dataEnum), FILE_TYPE);
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
