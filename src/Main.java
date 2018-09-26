import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	public void parseItemOrBag(HashMap<String, Integer> map, String rawLine ) {
		String [] words = rawLine.split("\\s+");
		map.put(words[0], Integer.parseInt(words[1]));
	}
    public static void main(String [] args){
        HashMap<String, Integer> items;
        HashMap<String, Integer> bags;
        int minItems;
        int maxItems;
        ArrayList<Unary> unaries;
        ArrayList<Binary> binaries;
        ArrayList<Mutual> mutuals;
    }
    
    public static HashMap<String,Bag> backTrack(HashMap<String, Integer>items, HashMap<String, Bag> bags, ArrayList<Constraint> constraints) {
    if (items.isEmpty()) {
    	return bags;
    }
    String item = items.next();
    for (Bag bag: bags.keys()) {
        if (meetsConstraints(bag, item, constraints)) {
        	HashMap<String,Bag> result = backTrack(deepCopy(items), bags);
        	    if (result == null) {
        	    	removeItem(item, bag);
        	    }else {
        	    	return result;
        	    }
        }
    
    }
    return null;
    }
}
