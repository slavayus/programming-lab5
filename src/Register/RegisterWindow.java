package Register;


import commands.ShowAlert;
import connectServer.ClientLoad;
import connectServer.MessageFromClient;
import javafx.scene.control.Alert;
import old.school.Man;
import old.school.User;

import java.io.*;
import java.util.*;

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
        if (data.size() < 2) {
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


    public boolean hasAccount(String fileName) {

        return false;
    }
}
