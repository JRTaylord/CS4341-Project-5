import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	public static void parseItem(HashMap<String, Integer> map, String rawLine ) {
		String [] words = rawLine.split("\\s+");
		map.put(words[0], Integer.parseInt(words[1]));
	}

    public static void parseBag(HashMap<String, Bag> map, String rawLine ) {
        String [] words = rawLine.split("\\s+");
        map.put(words[0], new Bag(Integer.parseInt(words[1])));
    }

    public static void main(String [] args){
        if(args.length < 2){
            System.out.println("Not enough inputs");
            return;
        }
        File file = new File(args[1]);
        BufferedReader reader = null;
        ArrayList<String> list = new ArrayList<>();
        try{
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            while ((text = reader.readLine()) != null){
                list.add(text);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String, Integer> items = new HashMap<>();
        HashMap<String, Bag> bags = new HashMap<>();
        int minItems;
        int maxItems;
        ArrayList<Unary> unaries = new ArrayList<>();
        ArrayList<Binary> binaries = new ArrayList<>();
        ArrayList<Mutual> mutuals = new ArrayList<>();

        int i = 0;
        for (String line : list) {
            if (line.charAt(0)=='#'){
                i++;
            } else{
                switch (i){
                    case 1:
                        Main.parseItem(items, line);
                        break;
                    case 2:
                        Main.parseBag(bags, line);
                        break;
                    case 3:
                        String [] words = line.split("\\s+");
                        minItems = Integer.parseInt(words[0]);
                        maxItems = Integer.parseInt(words[1]);
                        break;
                    case 4:
                        unaries.add(new Unary(line, true));
                        break;
                    case 5:
                        unaries.add(new Unary(line, false));
                        break;
                    case 6:
                        binaries.add(new Binary(line, true));
                        break;
                    case 7:
                        binaries.add(new Binary(line, false));
                        break;
                    case 8:
                        mutuals.add(new Mutual(line));
                        break;
                    default:
                        System.out.println("Error index out of bounds: "+i);
                }
            }
        }
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
