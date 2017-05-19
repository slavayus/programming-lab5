package commands;

import GUI.Container;
import GUI.ContainerType;
import GUI.MainWindow;
import GUI.Storage;
import com.google.gson.JsonSyntaxException;
import connectServer.ClientLoad;
import connectServer.MessageFromClient;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import old.school.Man;
import old.school.People;

import java.io.IOException;
import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.Map;

public class TreeTextFieldEditor extends TreeCell<Container> {
    private TextField textField;
    private TreeView<Container> tree;

    public TreeTextFieldEditor(TreeView<Container> tree) {
        this.tree = tree;
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getString());
        setGraphic(getTreeItem().getGraphic());
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        }
        setText(null);
        textField.setText(getString());
        setGraphic(textField);
        textField.selectAll();
    }

    public void createTextField() {
        textField = new TextField(getString());
        textField.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (getItem().getType() == ContainerType.ELEMENT) {
                    commitEdit(getItem());
                    getItem().setValue(textField.getText());

                    try {
                        Map<String, Man> newData = new LinkedHashMap<>();
                        People people = new People(Storage.getInstanceOf().getFamily().get(getItem().getKey()).getName());
                        people.setAge(Storage.getInstanceOf().getFamily().get(getItem().getKey()).getAge());
                        newData.put(getItem().getKey(), people);
                        if (!newData.values().iterator().next().setName(getItem().getValue())) {
                            new ShowAlert(Alert.AlertType.ERROR, "Error", "Name isn't correct");
                        }

                        ClientLoad clientLoad = new ClientLoad();
                        clientLoad.send(newData, "UPDATE");
                        MessageFromClient messageFromClient = clientLoad.readData();

                        if (!clientLoad.getConnection()) {
                            new ShowAlert(Alert.AlertType.ERROR, "Error", messageFromClient.getMsg());
                            return;
                        }

                        Storage.getInstanceOf().setFamily(messageFromClient.getDataFromClient());

                        if (!messageFromClient.getClientCollectionState()) {
                            new ShowAlert(Alert.AlertType.INFORMATION, "Done", "You had an old version of the collection. \nThe collection was updated.");
                        }


                        tree.setRoot(MainWindow.getTreeForPeople());

                    } catch (NullPointerException ex) {
                        new ShowAlert(Alert.AlertType.ERROR, "Error", "Не верно введены данные об объекте");
                    } catch (IOException ex) {
                        new ShowAlert(Alert.AlertType.ERROR, "Error", "\nCould not connect to server");
                    }
                } else {
                    new ShowAlert(Alert.AlertType.ERROR, "Error", "У тебя тут нет прав");
                    cancelEdit();
                }
            } else if (e.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().getValue();
    }

    @Override
    protected void updateItem(Container string, boolean empty) {
        super.updateItem(string, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(getTreeItem().getGraphic());
            }
        }
    }
}