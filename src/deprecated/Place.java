package deprecated;

/**
 * Created by slavik on 30.10.16.
 */
public class Place {
    private boolean empty = false;
    private People full;

    public void setEmpty() {
        empty = false;
        full = null;
    }

    public void setFull(People x) {
        empty = true;
        full = x;
    }

    public boolean getEmpty() {
        return empty;
    }

    public People getFull() {
        return full;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj))
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Place other = (Place) obj;
//        if(empty==other.empty)
//            return true;
        return full.equals(other.full);
    }

    @Override
    public int hashCode() {
        int result = 48043;
        result += full.hashCode();
        if (empty) {
            ++result;
        } else {
            result += 2;
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append(getClass().getName()).append(full.toString()).append(empty);
        return output.toString();
    }
}