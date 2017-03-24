package deprecated;

import GUI.Storage;

/**
 * Created by slavik on 30.10.16.
 */
public class Food {
    private String typeFood;
    private String state;
    private Table table = new Table();

    public Food(String x) {
        typeFood = x;
    }

    public void setState() {

        Table table = new Table();

        if (table.getNumberOfEmptyPlaces() == 0) {
            state = "топчик";
        } else if (table.getNumberOfEmptyPlaces() <= Storage.getInstanceOf().getАllPlaces() / 2)

        {
            state = "нормалек";
        } else

        {
            state = "невеселый";
        }
    }


    public String getStateTable() {
        if (table.getNumberOfEmptyPlaces() == 0) {
            return ("стол полон");
        } else if (table.getNumberOfEmptyPlaces() <= Storage.getInstanceOf().getАllPlaces() / 2) {
            return ("стол наполовину пустой");
        } else {
            return "стол почти пуст";
        }
    }

    public String getState() {
        return state;
    }

    public String getTypeFood() {

        return typeFood;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj))
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Food other = (Food) obj;
        if (typeFood != other.typeFood)
            return false;
        if (state != other.state)
            return false;
        if (table.getNumberOfEmptyPlaces() != other.table.getNumberOfEmptyPlaces()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 23489;
        for (char x : state.toCharArray()) {
            result += (int) x;
        }
        for (char x : typeFood.toCharArray()) {
            result += (int) x;
        }
        return result + table.getNumberOfEmptyPlaces();
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append(getClass().getName()).append(typeFood).append(table.getNumberOfEmptyPlaces());
        return output.toString();
    }
}