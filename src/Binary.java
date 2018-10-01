import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Binary extends Constraint {
    boolean isEqual;
    ArrayList<String> items;
    
    public Binary(String rawLine, boolean equals) {
    	this.isEqual = equals;
    	items = new ArrayList<String>();
    	String[] words = rawLine.split("\\s+");
    	for (int i =0; i < words.length; i++) {
    		this.items.add(words[i]);
    	}
    }

	@Override
	public boolean isValid(String item, Bag bag, Set <String> items) {
		if (!this.items.contains(item)) { //if the item is not relevant return true
			return true;
		}
		HashSet<String> s = new HashSet<>(bag.items.keySet());
		if (isEqual) {
			s.addAll(items);
			return s.containsAll(this.items); // false if one is in another bag, that is if all items are not in this bag or another bag
		}else {
			return !s.containsAll(this.items); //false if the bag contains all of a type
		}
	}

	@Override
	public boolean hasItem(String item) {
		// TODO Auto-generated method stub
		return items.contains(item);
	}

	@Override
	public String getType() {
    	String type = "n";
    	if(isEqual) type = "e";
		return "b" + type;
	}

	@Override
	public ArrayList<String> getContraints() {
    	return items;
	}
}
