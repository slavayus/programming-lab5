package connectServer;

import GUI.Storage;
import old.school.Man;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Map;

import connectDB.MessageToClient;

/**
 * Created by slavik on 03.05.17.
 */

public class ClientLoad {

    private static InetAddress serverInetAddress;
    private static int port = 7007;
    private boolean connection = true;
    private static DatagramSocket clientSocket;

    {
        try {
            clientSocket = new DatagramSocket();
            serverInetAddress = InetAddress.getByName("10.0.0.8");
        } catch (Exception e) {
        }
    }


    public ClientLoad() throws IOException {
        if (clientSocket == null || serverInetAddress == null) {
            throw new IOException();
        }
    }

    public void send(Map<String, Man> newData, String command) {
        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(Storage.getInstanceOf().getFamily());
            clientSocket.send(new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size(), serverInetAddress, port));


            clientSocket.send(new DatagramPacket("NEW".getBytes(), "NEW".getBytes().length, serverInetAddress, port));
            byteArrayOutputStream.reset();
            ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
            oos.writeObject(newData);
            clientSocket.send(new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size(), serverInetAddress, port));


            clientSocket.send(new DatagramPacket("BUTTON".getBytes(), "BUTTON".getBytes().length, serverInetAddress, port));
            clientSocket.send(new DatagramPacket(command.getBytes(), command.getBytes().length, serverInetAddress, port));
            clientSocket.send(new DatagramPacket("END".getBytes(), "END".getBytes().length, serverInetAddress, port));

        } catch (IOException e) {
            e.printStackTrace();
            connection = false;
        }

    }


    public MessageFromClient readData() {

        MessageFromClient messageFromClient = new MessageFromClient();

        try {
            byte[] dataFromServer = new byte[8 * 1024 * 1024];
            DatagramPacket datagramPacketFromServer = new DatagramPacket(dataFromServer, dataFromServer.length);
            clientSocket.receive(datagramPacketFromServer);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataFromServer);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            MessageToClient msg = (MessageToClient) objectInputStream.readObject();

            messageFromClient.setDataFromClient(msg.getDataToClient());
            messageFromClient.setClientCollectionState(msg.isClientCollectionState());
            messageFromClient.setModifiedRow(msg.getModifiedRow());
            messageFromClient.setMsg(msg.getMsgToClient());

        } catch (IOException e) {
            connection = false;
            messageFromClient.setMsg("Connection lost");
        } catch (ClassNotFoundException e) {
            connection = false;
            messageFromClient.setMsg("Class not found");
        }
        return messageFromClient;
    }


    public boolean getConnection() {
        return connection;
    }

    public boolean createNewConnection() {

        return true;
    }


    public static DatagramSocket getClientSocket() {
        return clientSocket;
    }

    public static InetAddress getInetAddress() {
        return serverInetAddress;
    }

    public static int getPort() {
        return port;
    }
}