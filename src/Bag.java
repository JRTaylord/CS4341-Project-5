import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Bag {
    int size;
    HashMap<String, Integer> items;
    String name;

    public Bag(int size, String name){
        this.size = size;
        this.name = name;
        items = new HashMap<>();
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof Bag) {
			return name.equals(((Bag)arg0).name);
		}
		return false;
	}
	
	public void print() {
		int sum = 0;
		for (Integer val: items.values()) {
			sum+= val;
		}
		System.out.println(name + ' '+ items.keySet().toString());
		System.out.println("number of items: "+items.size());
		System.out.println("total weight: " + sum + "/"+size);
		System.out.println("wasted capacity: "+ (size-sum));
		System.out.println("");
	}

	public boolean canContain(String item, Integer itemVal, ArrayList<Constraint> constraints, Set<String> items, int maxItems) {
		int sum = 0;
		for (Integer val: this.items.values()) {
			sum+= val;
		}
		if (itemVal+ sum > size) {
			return false;
		}
		if (this.items.size() +1 > maxItems) {
			return false;
		}
		for (Constraint c: constraints) {
			if (!c.isValid(item, this, items)) {
				return false;
			}
		}
		return true;
	}


    
}
