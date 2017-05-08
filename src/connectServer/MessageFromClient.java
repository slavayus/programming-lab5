package connectServer;

import old.school.Man;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Map;

/**
 * Created by slavik on 03.05.17.
 */
public class MessageFromClient {
    private boolean clientCollectionState;
    private int modifiedRow;
    private Map<String, Man> dataFromClient;
    private String msg;

    public boolean getClientCollectionState() {
        return clientCollectionState;
    }

    public int getModifiedRow() {
        return modifiedRow;
    }

    public Map<String, Man> getDataFromClient() {
        return dataFromClient;
    }

    private static final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

    public void setClientCollectionState(boolean clientCollectionState) {
        this.clientCollectionState = clientCollectionState;
    }

    public void setModifiedRow(int modifiedRow) {
        this.modifiedRow = modifiedRow;
    }

    public void setDataFromClient(Map<String, Man> dataFromClient) {
        this.dataFromClient = dataFromClient;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = (String) msg;
    }
}
