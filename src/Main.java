import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	public static void parseItem(HashMap<String, Integer> map, String rawLine) {
		String[] words = rawLine.split("\\s+");
		map.put(words[0], Integer.parseInt(words[1]));
	}

	public static void parseBag(HashMap<String, Bag> map, String rawLine) {
		String[] words = rawLine.split("\\s+");
		map.put(words[0], new Bag(Integer.parseInt(words[1]), words[0]));
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Not enough inputs");
			return;
		}
		File file = new File(args[1]);
		BufferedReader reader = null;
		ArrayList<String> list = new ArrayList<>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;

			while ((text = reader.readLine()) != null) {
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
		ArrayList<Constraint> constraints = new ArrayList<>();

		int i = 0;
		for (String line : list) {
			if (line.charAt(0) == '#') {
				i++;
			} else {
				switch (i) {
				case 1:
					Main.parseItem(items, line);
					break;
				case 2:
					Main.parseBag(bags, line);
					break;
				case 3:
					String[] words = line.split("\\s+");
					minItems = Integer.parseInt(words[0]);
					maxItems = Integer.parseInt(words[1]);
					break;
				case 4:
					constraints.add(new Unary(line, true, bags));
					break;
				case 5:
					constraints.add(new Unary(line, false, bags));
					break;
				case 6:
					constraints.add(new Binary(line, true));
					break;
				case 7:
					constraints.add(new Binary(line, false));
					break;
				case 8:
					constraints.add(new Mutual(line, bags));
					break;
				default:
					System.out.println("Error index out of bounds: " + i);
				}
			}
		}
		bags = Main.backTrack(items, bags, constraints, maxItems, minItems);
		for (Bag b : bags.values()) {
			b.print();
		}

	}

	public static HashMap<String, Bag> backTrack(HashMap<String, Integer> items, HashMap<String, Bag> bags,
			ArrayList<Constraint> constraints, int maxItems, int minItems) {
		if (items.isEmpty()) {
			if (checkBagMinSize(bags, minItems)) {
				return bags;			
				}else {
					return null;
				}
		}
		String item = items.keySet().iterator().next();
		Integer weight = items.remove(item);
		for (String bag : bags.keySet()) {
			if (meetsConstraints(bag, item, weight, constraints, items.keySet())) {
				HashMap<String, Bag> result = backTrack(new HashMap<String, Integer>(items), bags, constraints,
						maxItems, minItems);
				if (result == null) {
					bags.get(bag).items.remove(item);
				} else {
					return result;
				}
			}
		}
		return null;
	}

	private static boolean checkBagMinSize(HashMap<String, Bag> bags, int minItems) {
		for (Bag b : bags.values()) {
			if (b.items.size() < minItems) {
				return false;
			}
		}
		return true;
	}
}
