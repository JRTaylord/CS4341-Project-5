import java.util.ArrayList;

public class Binary {
    boolean isEqual;
    ArrayList<String> items;
    
    public Binary(String rawLine, boolean equals) {
    	this.isEqual = equals;
    	String[] words = rawLine.split("\\s+");
    	for (int i =0; i < words.length; i++) {
    		this.items.add(words[i]);
    	}
    }
}
