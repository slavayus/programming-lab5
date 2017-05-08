package GUI;

/**
 * Created by slavik on 08.04.17.
 */
public class NoFileSelectedException extends Throwable{
    private String msg;

    public NoFileSelectedException(String msg){
        this.msg = msg;
    }

    @Override
    public String getMessage(){
        return msg;
    }
}
