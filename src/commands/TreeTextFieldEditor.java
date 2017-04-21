package commands;

import GUI.Container;
import GUI.ContainerType;
import GUI.Storage;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TreeTextFieldEditor extends TreeCell<Container> {
    private TextField textField;
    private TreeView<Container> tree;

    public TreeTextFieldEditor(TreeView<Container> tree) {
        this.tree = tree;
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (textField == null)
            createTextField();
        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (getItem().getType() == ContainerType.ELEMENT) {
                    commitEdit(getItem());
                    getItem().setValue(textField.getText());
                    Storage.getInstanceOf().getFamily().get(getItem().getKey()).setName(getItem().getValue());
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