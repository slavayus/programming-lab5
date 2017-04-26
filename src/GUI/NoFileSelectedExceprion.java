package GUI;

/**
 * Created by slavik on 08.04.17.
 */
public class NoFileSelectedExceprion extends Throwable{
    private String msg;

    public NoFileSelectedExceprion(String msg){
        this.msg = msg;
    }

    @Override
    public String getMessage(){
        return msg;
    }
}
