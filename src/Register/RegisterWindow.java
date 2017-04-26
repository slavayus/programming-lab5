package Register;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by slavik on 13.04.17.
 */
public class RegisterWindow {
    public List<String> data = new ArrayList<>();
    String fileNameLimitedEdition;
    String fileNameFullVersion;

    {
        Properties runProperties = new Properties();
        try (InputStream runFileProperties = RegisterWindow.class.getResourceAsStream("/properties/data.properties")) {
            runProperties.load(runFileProperties);
            fileNameLimitedEdition = runProperties.getProperty("limitedEdition.file");
            fileNameFullVersion = runProperties.getProperty("fullEdition.file");
        } catch (IOException e) {
            //do nothing
        }
    }

    public boolean setData(List<String> data) {
        if(data.size()<2){
            return false;
        }
        this.data = data;
        return true;
    }

    public List<String> getData() {
        return data;
    }

    public String getFileNameLimitedEdition() {
        return fileNameLimitedEdition;
    }

    public String getFileNameFullVersion() {
        return fileNameFullVersion;
    }


    protected void saveToFile(String fileName) {
        Properties runProperties = getProperties(RegisterWindow.class.getResourceAsStream("/properties/run.properties"));
        try (PrintWriter errorWriter = new PrintWriter(runProperties.getProperty("file.err"))) {
            try (BufferedWriter dataWriter = new BufferedWriter(new FileWriter(fileName, true))) {

                for (String string : data) {
                    dataWriter.write(string + " ");
                }
                dataWriter.write("\n");
                dataWriter.flush();

            } catch (IOException e) {
                errorWriter.println(e.getMessage());
                System.out.println(e.getMessage());
            }
        } catch (FileNotFoundException e) {
            //do nothing
        }
    }

    public boolean hasAccount(String fileName)  {
        Properties runProperties = getProperties(RegisterWindow.class.getResourceAsStream("/properties/run.properties"));
        try (PrintWriter errorWriter = new PrintWriter(runProperties.getProperty("file.err"))) {
            try (BufferedReader dataFile = new BufferedReader(new FileReader(fileName))) {
                String dataFromFile;
                while ((dataFromFile = dataFile.readLine()) != null) {
                    String[] list = dataFromFile.split(" ");
                    int i = 0;
                    int numberTrueData=0;
                    for (String string : data) {
                        if (list[i++].equals(string)) {
                            numberTrueData++;
                        }
                    }
                    if(numberTrueData==data.size()){
                        return true;
                    }
                }
            } catch (IOException e) {
                errorWriter.println(e.getMessage());
                return true;
            }
        } catch (FileNotFoundException e) {
            //do nothing
            return true;
        }
        return false;
    }

    private Properties getProperties(InputStream file) {
        Properties properties = new Properties();

        try {
            properties.load(file);
        } catch (IOException e) {
            //do nothing
        }
        return properties;
    }


}
