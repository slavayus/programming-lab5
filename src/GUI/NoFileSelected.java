package GUI;

/**
 * Created by slavik on 08.04.17.
 */
public class NoFileSelected extends Throwable{
    private String msg;

    public NoFileSelected(String msg){
        this.msg = msg;
    }

    @Override
    public String getMessage(){
        return msg;
    }
}
