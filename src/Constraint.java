import java.util.ArrayList;
import java.util.Set;

public abstract class Constraint {

	public Constraint() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract boolean isValid(String item, Bag bag, Set<String> items);
	public abstract boolean hasItem(String item);

}
