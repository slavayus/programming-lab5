package connectServer;

import GUI.Storage;
import old.school.People;
import old.school.Man;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by slavik on 03.05.17.
 */

public class ClientLoad {

    private static byte[] dataFromServer = new byte[8 * 1024];
    private InetAddress inetAddress;
    private int port = 7007;
    private static boolean connection = true;
    private static ServiceInformation serviceInformation = ServiceInformation.OLD;
    private DatagramSocket clientSocket;


    public ClientLoad() throws IOException {
            clientSocket = new DatagramSocket();
            inetAddress = InetAddress.getLocalHost();
            new Socket(inetAddress, port);
    }


    public void send(Map<String, Man> newData, String command) {
        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(Storage.getInstanceOf().getFamily());
            clientSocket.send(new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size(), inetAddress, port));


            clientSocket.send(new DatagramPacket("NEW".getBytes(), "NEW".getBytes().length, inetAddress, port));
            byteArrayOutputStream.reset();
            ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
            oos.writeObject(newData);
            clientSocket.send(new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size(), inetAddress, port));


            clientSocket.send(new DatagramPacket("BUTTON".getBytes(), "BUTTON".getBytes().length, inetAddress, port));
            clientSocket.send(new DatagramPacket(command.getBytes(), command.getBytes().length, inetAddress, port));
            clientSocket.send(new DatagramPacket("END".getBytes(), "END".getBytes().length, inetAddress, port));

        } catch (IOException e) {
            connection = false;
        }

    }


    public MessageFromClient readData() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MessageFromClient messageFromClient = new MessageFromClient();

        try {
            while (serviceInformation != ServiceInformation.END) {
                dataFromServer = new byte[8 * 1024 * 1024];
                DatagramPacket datagramPacketFromServer = new DatagramPacket(dataFromServer, dataFromServer.length, InetAddress.getLocalHost(), port);
                clientSocket.receive(datagramPacketFromServer);

                String command = new String(datagramPacketFromServer.getData(), 0, datagramPacketFromServer.getLength());

                dataFromServer = Arrays.copyOf(datagramPacketFromServer.getData(), datagramPacketFromServer.getLength());

                analysisMsgFromClient(command, byteArrayOutputStream, messageFromClient);
            }

            serviceInformation = ServiceInformation.OLD;

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            messageFromClient.setDataFromClient((Map<String, Man>) objectInputStream.readObject());

        } catch (IOException e) {
            connection = false;
            messageFromClient.setMsg("Connection lost");
        } catch (ClassNotFoundException e) {
            messageFromClient.setMsg("Class not found");
        }
        return messageFromClient;
    }


    private static void analysisMsgFromClient(String command, ByteArrayOutputStream byteArrayOutputStream, MessageFromClient messageFromClient) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream;
        ObjectInputStream objectInputStream;

        try {
            serviceInformation = ServiceInformation.valueOf(command);
            return;
        } catch (IllegalArgumentException e) {
//            System.out.println(e.getMessage());
        }

        switch (serviceInformation) {
            case OLD:
                byteArrayOutputStream.write(dataFromServer);
                break;
            case NEW:
                byteArrayInputStream = new ByteArrayInputStream(dataFromServer);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                messageFromClient.setModifiedRow(objectInputStream.readInt());
                objectInputStream.close();
                byteArrayInputStream.close();
                break;
            case STATE:
                byteArrayInputStream = new ByteArrayInputStream(dataFromServer);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                messageFromClient.setClientCollectionState(objectInputStream.readBoolean());
                objectInputStream.close();
                byteArrayInputStream.close();
                break;
            case MSG:
                byteArrayInputStream = new ByteArrayInputStream(dataFromServer);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                messageFromClient.setMsg(objectInputStream.readObject());
                objectInputStream.close();
                byteArrayInputStream.close();
                break;
        }

    }

    public boolean getConnection() {
        return connection;
    }
}