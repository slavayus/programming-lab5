package dataBase;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import commands.ShowAlert;
import javafx.scene.control.Alert;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by slavik on 13.04.17.
 */
public class ConnectDB implements Runnable {

    @Override
    public void run() {

        Properties runProperties = getRunProperties();

        try (PrintWriter logError = new PrintWriter(runProperties.getProperty("file.err"))) {
            try (FileInputStream connectDBProperties = new FileInputStream(runProperties.getProperty("file.connectDB"))) {
                Properties settingsDB = new Properties();
                settingsDB.load(connectDBProperties);
                JSch jSch = new JSch();
                Session session = jSch.getSession(settingsDB.getProperty("user"),settingsDB.getProperty("host"), Integer.parseInt(settingsDB.getProperty("port")));
                session.setPassword(settingsDB.getProperty("password.ssh"));
                session.setConfig("StrictHostKeyChecking", "no");
//                session.setPortForwardingL(5432,settingsDB.getProperty("host"),2222);
                session.connect();

                Connection connection = DriverManager.getConnection(settingsDB.getProperty("url"),settingsDB.getProperty("user"),settingsDB.getProperty("password.DB"));

            } catch (IOException e) {
                logError.println(e.getMessage());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (JSchException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            //do nothing
        }
    }

    private Properties getRunProperties() {
        Properties runProperties = new Properties();
        try (FileInputStream runFileProperties = new FileInputStream("properties/run.properties")) {
            runProperties.load(runFileProperties);
        } catch (IOException e) {
            new ShowAlert(Alert.AlertType.ERROR, "Error", "Программа не установлена корректно");
        }
        return runProperties;
    }
}
