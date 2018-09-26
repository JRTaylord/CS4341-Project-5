import java.util.ArrayList;

public class Mutual {
    ArrayList<String> items;
    ArrayList<String> bags;
    
    public Mutual(String rawLine) {
    	String[] words = rawLine.split("\\s+");
    	this.items.add(words[0]);
    	this.items.add(words[1]);
    	this.bags.add(words[2]);
    	this.bags.add(words[3]);
    }
}
