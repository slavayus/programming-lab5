package GUI;

/**
 * Created by slavik on 17.04.17.
 */
public class Container {
    private String key;
    private String value;
    private ContainerType type;

    public Container(String key, String value, ContainerType type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ContainerType getType() {
        return type;
    }

    public void setType(ContainerType type) {
        this.type = type;
    }
}
