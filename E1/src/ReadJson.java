import java.util.Arrays;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadJson {
    private static final String[] instanceTypes = new String[]{FactoryValues.EASY, FactoryValues.FLAT, FactoryValues.HARD};
    private static final String[] dataTypes = new String[]{FactoryValues.cost, FactoryValues.flow};
    private static final HashMap<String, String[]> dataTypeElementSubames = new HashMap<>() {
        {
            put(FactoryValues.cost, new String[]{"source", "dest", "cost"});
            put(FactoryValues.flow, new String[]{"source", "dest", "amount"});
        }
    };
    private static final String FILE_TYPE = "json";

    public static int[][] getData(String folderPath, String instanceType, String dataType) {
        // checking if selection parameters are correct
        if(Arrays.stream(instanceTypes).noneMatch(instanceType::equals)) {
            String errorMessage = String.format("Instance type: %s; allowed: %s", instanceType, Arrays.toString(instanceTypes));
            throw new IllegalArgumentException(errorMessage);
        }
        if(Arrays.stream(dataTypes).noneMatch(dataType::equals)) {
            String errorMessage = String.format("Data type: %s; allowed: %s", dataType, Arrays.toString(dataTypes));
            throw new IllegalArgumentException(errorMessage);
        }
        // reading file
        String filename = String.format("%s\\%s_%s.%s", folderPath, instanceType, dataType, FILE_TYPE);
        try (FileReader reader = new FileReader(filename)) {
            return getTableOfIntTables(reader, dataType);
        }
        catch (IOException | ParseException e) {
            String errorMessage = String.format("Problem reading file \"%s\"", filename);
            System.out.println(errorMessage);
        }
        return null;
    }

    private static int[][] getTableOfIntTables(FileReader reader, String dataType) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Object parsedReader = jsonParser.parse(reader);
        JSONArray elementsList = (JSONArray) parsedReader;

        String[] elementSubnamesList = dataTypeElementSubames.get(dataType);
        int[][] readTables = new int[elementsList.size()][elementSubnamesList.length];

        for (int iElem = 0; iElem < elementsList.size(); iElem++) {
            JSONObject jsonElement = ((JSONObject) elementsList.get(iElem));
            for (int iName = 0; iName < elementSubnamesList.length; iName++) {
                String subname = elementSubnamesList[iName];
                readTables[iElem][iName] = ((Long) (jsonElement.get(subname))).intValue();
            }
        }
        return readTables;
    }
}
