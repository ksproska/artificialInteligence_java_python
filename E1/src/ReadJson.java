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
    private static final HashMap<String, String[]> dataTypeElements = new HashMap<>() {
        {
            put(FactoryValues.cost, new String[]{"source", "dest", "cost"});
            put(FactoryValues.flow, new String[]{"source", "dest", "amount"});
        }
    };
    private static final String FILE_TYPE = "json";

    public static int[][] getData(String folderPath, String instanceType, String dataType) {
        if(Arrays.stream(instanceTypes).noneMatch(instanceType::equals)) {
            String errorMessage = String.format("Instance type: %s; allowed: %s", instanceType, Arrays.toString(instanceTypes));
            throw new IllegalArgumentException(errorMessage);
        }
        if(Arrays.stream(dataTypes).noneMatch(dataType::equals)) {
            String errorMessage = String.format("Data type: %s; allowed: %s", dataType, Arrays.toString(dataTypes));
            throw new IllegalArgumentException(errorMessage);
        }
        String filename = String.format("%s\\%s_%s.%s", folderPath, instanceType, dataType, FILE_TYPE);

        JSONParser jsonParser = new JSONParser();
        int[][] readTables = new int[0][];
        try (FileReader reader = new FileReader(filename))
        {
            Object obj = jsonParser.parse(reader);
            JSONArray elementList = (JSONArray) obj;
            String[] dataNames = dataTypeElements.get(dataType);
            readTables = new int[elementList.size()][dataNames.length];

            for (int ei = 0; ei < elementList.size(); ei++) {
                JSONObject jsObj = ((JSONObject) elementList.get(ei));
                for (int ni = 0; ni < dataNames.length; ni++) {
                    readTables[ei][ni] = ((Long) (jsObj.get(dataNames[ni]))).intValue();
                }
            }
        } catch (IOException | ParseException e) {
            String errorMessage = String.format("Problem reading file \"%s\"", filename);
            System.out.println(errorMessage);
        }
        return readTables;
    }
}
