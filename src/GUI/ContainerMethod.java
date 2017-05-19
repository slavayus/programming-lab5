package GUI;

/**
 * Created by slavik on 20.05.17.
 */
public class ContainerMethod {
    private String methodName;
    private String inShow;

    public ContainerMethod(String methodName, String inShow) {
        this.methodName = methodName;
        this.inShow = inShow;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getInShow() {
        return inShow;
    }
}
