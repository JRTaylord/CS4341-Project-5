import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Mutual extends Constraint {
    ArrayList<String> items;
    ArrayList<Bag> bags;
    
    public Mutual(String rawLine, HashMap<String, Bag> map) {
    	bags = new ArrayList<Bag>();
    	items = new ArrayList<String>();

    	String[] words = rawLine.split("\\s+");
    	this.items.add(words[0]);
    	this.items.add(words[1]);
    	this.bags.add(map.get(words[2]));
    	this.bags.add(map.get(words[3]));
    }

	@Override
	public boolean isValid(String item, Bag bag, Set<String> items) {
		if (!this.items.contains(item)) {
			return true; //not a relevant item
		}
		if (!(bags.get(0).items.containsKey(item) || bags.get(1).items.containsKey(item))) {
			return false; //if it was not placed in either bag return false;
		}
		Bag b = bags.get(0).items.containsKey(item) ? bags.get(0): bags.get(1) ; //has to be one or the other
		
		if (b.items.keySet().containsAll(this.items)) {
			return false; //if the other item is in my bag return false
		}
		
		return true;
	}

	@Override
	public boolean hasItem(String item) {
		this.items.contains(item);
		return false;
	}
}
