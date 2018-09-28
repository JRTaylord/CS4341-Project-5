import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Unary extends Constraint{
    boolean inclusive;
    String item;
    ArrayList<Bag> bags;
    
    public Unary(String rawLine, boolean inclusive, HashMap <String, Bag> map) {
    	this.inclusive = inclusive;
    	String[] words = rawLine.split("\\s+");
    	this.item = words[0];
    	for (int i =1; i < words.length; i++) {
    		this.bags.add(map.get(words[i]));
    	}
    }

	@Override
	public boolean isValid(String item, Bag bag, Set<String> items) {
		if (!item.equals(this.item)) { //it only matter is the the item is the one in question
			return true;
		}
		return bags.contains(bag);
	}
}
