import java.util.HashMap;

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
    
}
