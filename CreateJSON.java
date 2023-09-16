import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class CreateJSON {

    public static boolean createJsonFile(String jsonString, String filepath, String fileName) {
        boolean flag = true;
        String fullPath = filepath + File.separator + fileName + ".json";

        try {
            File file = new File(fullPath);
//            if (!file.getParentFile().exists()) {
//                file.getParentFile().mkdirs();
//            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            jsonString = JsonFormatTool.formatJson(jsonString);

            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        return flag;
    }
}
