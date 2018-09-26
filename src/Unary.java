import java.util.ArrayList;

public class Unary {
    boolean inclusive;
    String item;
    ArrayList<String> bags;
    
    public Unary(String rawLine, boolean inclusive) {
    	this.inclusive = inclusive;
    	String[] words = rawLine.split("\\s+");
    	this.item = words[0];
    	for (int i =1; i < words.length; i++) {
    		this.bags.add(words[i]);
    	}
    }
}
