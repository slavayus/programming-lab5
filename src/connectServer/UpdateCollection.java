package connectServer;

import GUI.MainWindow;
import GUI.Storage;
import javafx.application.Platform;
import old.school.Man;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by slavik on 12.05.17.
 */
public class UpdateCollection implements Runnable {

    private DatagramSocket clientSocket;
    private InetAddress inetAddress;
    private int port;


    public UpdateCollection() {
        this.clientSocket = ClientLoad.getClientSocket();
        this.inetAddress = ClientLoad.getInetAddress();
        this.port = ClientLoad.getPort();
    }

    public void run() {
        byte[] dataFromServer;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectInputStream objectInputStream;
        ByteArrayInputStream byteArrayInputStream;

        try {
            String command = null;
            while (true) {
                dataFromServer = new byte[8 * 1024];
                DatagramPacket datagramPacketFromServer = new DatagramPacket(dataFromServer, dataFromServer.length, InetAddress.getLocalHost(), port);
                clientSocket.receive(datagramPacketFromServer);

                command = new String(datagramPacketFromServer.getData(), 0, datagramPacketFromServer.getLength());
                if ("END".equals(command)) {

                    byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    if (byteArrayInputStream.available() != 0) {
                        objectInputStream = new ObjectInputStream(byteArrayInputStream);
                        Storage.getInstanceOf().setFamily((Map<String, Man>) objectInputStream.readObject());
                    }

                    Platform.runLater(() -> MainWindow.getPeopleTree().setRoot(MainWindow.getTreeForPeople()));
                    byteArrayOutputStream.reset();
                    byteArrayInputStream.reset();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
//                        e.printStackTrace();
                    }
                } else {
                    byteArrayOutputStream.write(Arrays.copyOf(datagramPacketFromServer.getData(), datagramPacketFromServer.getLength()));
                    byteArrayOutputStream.flush();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
}