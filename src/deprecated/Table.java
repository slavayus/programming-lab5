package deprecated;

import GUI.Storage;

/**
 * Created by slavik on 20.02.17.
 */
public class Table {
    private int numberOfEmptyPlaces;

    public int getNumberOfEmptyPlaces() {
        numberOfEmptyPlaces = Storage.getInstanceOf().getАllPlaces();
        for (Place i : Storage.getInstanceOf().getPlaces()) {
            if (i.getEmpty()) {
                numberOfEmptyPlaces--;
            }
        }
        return numberOfEmptyPlaces;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj))
            return true;
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass())
            return false;
        Table other = (Table) obj;
        if (numberOfEmptyPlaces != other.numberOfEmptyPlaces) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 98754 + numberOfEmptyPlaces;
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append(getClass().getName()).append(numberOfEmptyPlaces);
        return output.toString();
    }
}
