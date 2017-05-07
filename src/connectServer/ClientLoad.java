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


    public ClientLoad() throws SocketException {
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            connection = false;
        }

    }


    public static void main(String[] args) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        String msg;

        while (true) {
            Scanner scanner = new Scanner(System.in);


            Map<String, Man> manMap = new LinkedHashMap<>();
            People people = new People("s");
            people.setAge(32358);
            manMap.put("32358", people);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(manMap);

            msg = scanner.nextLine();
            DatagramPacket datagramPacketToServer = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size(), InetAddress.getLocalHost(), 7007);
            clientSocket.send(datagramPacketToServer);
            clientSocket.send(new DatagramPacket("NEW".getBytes(), "NEW".getBytes().length, InetAddress.getLocalHost(), 7007));


            //send new Data
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(manMap);
            DatagramPacket datToClient = new DatagramPacket(baos.toByteArray(), baos.size(), InetAddress.getLocalHost(), 7007);
            clientSocket.send(datToClient);


            clientSocket.send(new DatagramPacket("BUTTON".getBytes(), "BUTTON".getBytes().length, InetAddress.getLocalHost(), 7007));
            clientSocket.send(new DatagramPacket("REMOVE_ALL".getBytes(), "REMOVE_ALL".getBytes().length, InetAddress.getLocalHost(), 7007));
            clientSocket.send(new DatagramPacket("END".getBytes(), "END".getBytes().length, InetAddress.getLocalHost(), 7007));

            if (msg.equals("end")) {
                break;
            }

//            System.out.println(readData().getMsg());


            DatagramPacket datagramPacketFromServer = new DatagramPacket(dataFromServer, dataFromServer.length, InetAddress.getLocalHost(), 7007);
            clientSocket.receive(datagramPacketFromServer);

            System.out.println(new String(datagramPacketFromServer.getData(), 0, datagramPacketFromServer.getLength()));

        }
        clientSocket.close();
    }

    private DatagramSocket clientSocket = new DatagramSocket();

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
                DatagramPacket datagramPacketFromServer = new DatagramPacket(dataFromServer, dataFromServer.length, InetAddress.getLocalHost(), 7007);
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

}