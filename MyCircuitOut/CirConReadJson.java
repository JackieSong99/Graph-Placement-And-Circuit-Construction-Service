import java.io.*;

public class CirConReadJson {
    /*
    * ReadJson.read_json(filename) will read the content of the original json file and return it in string format
    */
    public static String CirConread_json(String src) throws Exception{
        String jsonStr = "";
        try {
            File jsonFile = new File(src);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
