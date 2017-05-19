package connectDB;

import old.school.Man;

import java.io.Serializable;
import java.util.Map;

/**
 * Created byteArrayOutputStream slavik on 02.05.17.
 */
public class MessageToClient implements Serializable {
    private boolean clientCollectionState;
    private int modifiedRow;
    private Map<String, Man> dataToClient;
    private String msgToClient;
    private static final long serialVersionUID =2;

    public MessageToClient() {
    }

    MessageToClient(boolean clientCollectionState, int modifiedRow, Map<String, Man> dataToClient, String msgToClient) {
        this.clientCollectionState = clientCollectionState;
        this.modifiedRow = modifiedRow;
        this.dataToClient = dataToClient;
        this.msgToClient = msgToClient;
    }

    public boolean isClientCollectionState() {
        return clientCollectionState;
    }

    public int getModifiedRow() {
        return modifiedRow;
    }

    public Map<String, Man> getDataToClient() {
        return dataToClient;
    }

    public String getMsgToClient() {
        return msgToClient;
    }
}
