package GUI;

import deprecated.People;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by slavik on 30.03.17.
 */
public class Stor<T extends People> implements Iterable<T>{
    private List<T> list = new ArrayList<>();
    private int index=0;

    public void  add (T o){
       list.add(o);
    }

    public int get(T o){
        int index = -1;
        for(T t : list){
             index = t.equals(o)?list.indexOf(o):-1;
        }
        return index;
    }

    public boolean remove (T o){
        return list.remove(o);
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<T> it = new Iterator<T>() {
            private int currentIndex =0;

            @Override
            public boolean hasNext() {
                return list.size()-currentIndex>=0;
            }

            @Override
            public T next() {
                return list.get(currentIndex++);
            }


        };
        return it;
    }
}
