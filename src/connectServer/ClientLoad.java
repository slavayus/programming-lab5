package connectServer;

import GUI.Storage;
import old.school.Man;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by slavik on 03.05.17.
 */

public class ClientLoad {

    private byte[] dataFromServer = new byte[8 * 1024];
    private static InetAddress inetAddress;
    private static int port = 7007;
    private boolean connection = true;
    private ServiceInformation serviceInformation = ServiceInformation.OLD;
    private static DatagramSocket clientSocket;

    static {
        try {
            clientSocket = new DatagramSocket();
            inetAddress = InetAddress.getByName("10.0.0.8");
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }


    public ClientLoad() throws IOException {
        if (clientSocket == null || inetAddress == null) {
            throw new IOException();
        }
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
                dataFromServer = new byte[8 * 1024];
                DatagramPacket datagramPacketFromServer = new DatagramPacket(dataFromServer, dataFromServer.length, inetAddress, port);
                clientSocket.receive(datagramPacketFromServer);
                String command = new String(datagramPacketFromServer.getData(), 0, datagramPacketFromServer.getLength());

                dataFromServer = Arrays.copyOf(datagramPacketFromServer.getData(), datagramPacketFromServer.getLength());

                analysisMsgFromServer(command, byteArrayOutputStream, messageFromClient);
            }

            serviceInformation = ServiceInformation.OLD;

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            messageFromClient.setDataFromClient((Map<String, Man>) objectInputStream.readObject());


            System.out.println("getData" + messageFromClient.getDataFromClient());

        } catch (IOException e) {
            connection = false;
            messageFromClient.setMsg("Connection lost");
        } catch (ClassNotFoundException e) {
            messageFromClient.setMsg("Class not found");
        }
        return messageFromClient;
    }


    private void analysisMsgFromServer(String command, ByteArrayOutputStream byteArrayOutputStream, MessageFromClient messageFromClient) throws IOException, ClassNotFoundException {
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

    public boolean createNewConnection() {

        return true;
    }


    public static DatagramSocket getClientSocket() {
        return clientSocket;
    }

    public static InetAddress getInetAddress() {
        return inetAddress;
    }

    public static int getPort() {
        return port;
    }
}