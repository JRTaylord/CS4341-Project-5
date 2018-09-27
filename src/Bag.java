import java.util.ArrayList;

public class Bag {
    int size;
    ArrayList<Integer> items;
    String name;

    public Bag(int size, String name){
        this.size = size;
        this.name = name;
        items = new ArrayList<>();
    }
}
