import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

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
		if (args.length < 1) {
			System.out.println("Not enough inputs");
			return;
		}
		File file = new File(args[0]);
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
		int minItems = 0;
		int maxItems = Integer.MAX_VALUE;
		ArrayList<Constraint> constraints = new ArrayList<>();

		int i = 0;
		for (String line : list) {
			if (line.length() < 1) {
				continue;
			}
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
		long start =  System.nanoTime();
		HashMap<String, Bag> bags2 = Main.backTrack(new HashMap <String, Integer> (items), bags, constraints, maxItems, minItems);
		long end =  System.nanoTime();
		System.out.println("Time: "+ (end-start));
		if (bags2  == null) {
			System.out.println("No solution");
		} else {
			System.out.println("BackTrack");
			for (Bag b : bags2.values()) {
				b.print();
			}
		}

		start = System.nanoTime();
		bags2 = Main.backTrackHeuristics(new HashMap <String, Integer> (items), bags, constraints, maxItems, minItems);
		end =  System.nanoTime();
		System.out.println("Time: "+ (end-start));
		if (bags2 == null) {
			System.out.println("No solution");
		} else {
			System.out.println("Heuristic");
			for (Bag b : bags2.values()) {
				b.print();
			}
		}
		
		start = System.nanoTime();
		bags2 = Main.backTrackFoward(new HashMap <String, Integer> (items), bags, constraints, maxItems, minItems);
		end =  System.nanoTime();
		System.out.println("Time: "+ (end-start));
		if (bags2 == null) {
			System.out.println("No solution");
		} else {
			System.out.println("Foward Checking");
			for (Bag b : bags2.values()) {
				b.print();
			}
		}

	}

	public static HashMap<String, Bag> backTrack(HashMap<String, Integer> items, HashMap<String, Bag> bags,
			ArrayList<Constraint> constraints, int maxItems, int minItems) {
		if (items.isEmpty()) {
			if (checkBagMinSize(bags, minItems)) {
				return bags;
			} else {
				return null;
			}
		}
		String item = items.keySet().iterator().next();
		Integer weight = items.remove(item);
		for (String bag : bags.keySet()) {
			System.out.println("CheckDone");

			if (meetsConstraints(bags.get(bag), item, weight, constraints, items.keySet(), maxItems)) {
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

	public static HashMap<String, Bag> backTrackHeuristics(HashMap<String, Integer> items, HashMap<String, Bag> bags,
			ArrayList<Constraint> constraints, int maxItems, int minItems) {
		if (items.isEmpty()) {
			if (checkBagMinSize(bags, minItems)) {
				return bags;
			} else {
				return null;
			}
		}

		String item = getMRV(items, constraints, bags, maxItems);
		Integer weight = items.remove(item);
		for (String bag : orderLCV(bags, item, items, maxItems, constraints, weight)) {
			System.out.println("CheckDone");

			if (meetsConstraints(bags.get(bag), item, weight, constraints, items.keySet(), maxItems)) {
				HashMap<String, Bag> result = backTrackHeuristics(new HashMap<String, Integer>(items), bags, constraints,
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
	
	public static HashMap<String, Bag> backTrackFoward(HashMap<String, Integer> items, HashMap<String, Bag> bags,
			ArrayList<Constraint> constraints, int maxItems, int minItems) {
		if (items.isEmpty()) {
			if (checkBagMinSize(bags, minItems)) {
				return bags;
			} else {
				return null;
			}
		}

		String item = getMRV(items, constraints, bags, maxItems);
		Integer weight = items.remove(item);
		for (String bag : orderForwardCheckLCV(bags, item, items, maxItems, constraints, weight)) {
			System.out.println("CheckDone");
			if (meetsConstraints(bags.get(bag), item, weight, constraints, items.keySet(), maxItems)) {
				HashMap<String, Bag> result = backTrackFoward(new HashMap<String, Integer>(items), bags, constraints,
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

	private static ArrayList<String> orderLCV(HashMap<String, Bag> bags, String item, HashMap<String, Integer> items, 
			int maxItems, ArrayList<Constraint> constraints, int itemWeight) {
		HashMap<String, Integer> bagList = new HashMap<String, Integer>();
		for (Bag bOuter : bags.values()) {
			int bagCount = 0;
			bOuter.addItem(item, itemWeight);
			for (Bag b : bags.values()) {
				for (String it: items.keySet()) {
					if (meetsConstraints(b, it, items.get(it), constraints, items.keySet(), maxItems)) {
						bagCount++;
						b.items.remove(it);
					}
				}
			}
			bOuter.items.remove(item);
			bagList.put(bOuter.name, bagCount);
			bagCount = 0;
		}
		TreeMap<String, Integer> sortedList = new TreeMap<String, Integer>(new ValueCompare(bagList));
		sortedList.putAll(bagList);
		return new ArrayList<String>(sortedList.keySet());
	}
	
	private static ArrayList<String> orderForwardCheckLCV(HashMap<String, Bag> bags, String item, HashMap<String, Integer> items, 
			int maxItems, ArrayList<Constraint> constraints, int itemWeight) {
		HashMap<String, Integer> bagList = new HashMap<String, Integer>();
		for (Bag bOuter : bags.values()) {
			int bagCount = 0;
			bOuter.addItem(item, itemWeight);
			for (Bag b : bags.values()) {
				for (String it: items.keySet()) {
					if (meetsConstraints(b, it, items.get(it), constraints, items.keySet(), maxItems)) {
						bagCount++;
						b.items.remove(it);
					}
				}
			}
			bOuter.items.remove(item);
			if (bagCount >= 0) {
				bagList.put(bOuter.name, bagCount);
			}else {
				System.out.println("prunned" + bOuter.name);
			}
			bagCount = 0;
		}
		TreeMap<String, Integer> sortedList = new TreeMap<String, Integer>(new ValueCompare(bagList));
		sortedList.putAll(bagList);
		return new ArrayList<String>(sortedList.keySet());
	}

	private static String getMRV(HashMap<String, Integer> items, ArrayList<Constraint> constraints,
			HashMap<String, Bag> bags, int maxItems) {
		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		HashMap<String, Integer> constraintCounts = new HashMap<String, Integer>();
		for (String item : items.keySet()) {
			int bagCount = 0;
			constraintCounts.put(item, getConstraintCount(constraints, item));
			for (Bag b : bags.values()) {
				if (meetsConstraints(b, item, items.get(item), constraints, items.keySet(), maxItems)) {
					bagCount++;
					b.items.remove(item);
				}

			}
			counts.put(item, bagCount);
		}
		int minCount = Integer.MAX_VALUE;
		int maxConstraints = -1;
		boolean sameValue = false;
		String minCountItem = null;
		String maxConstraintItem = null;
		for (String item : counts.keySet()) {
			if (counts.get(item) < minCount) {
				minCount = counts.get(item);
				minCountItem = item;
				sameValue = false;
			}
			if (counts.get(item) == minCount) {
				sameValue = true;
			}
		}
		if (!sameValue) {// return MRV
			return minCountItem;
		}
		// If no MRV get degree
		for (String item : counts.keySet()) {
			if (constraintCounts.get(item) > maxConstraints) {
				maxConstraints = counts.get(item);
				maxConstraintItem = item;
			}

		}
		return maxConstraintItem;

	}

	private static int getConstraintCount(ArrayList<Constraint> constraints, String item) {
		int count = 0;
		for (Constraint c : constraints) {
			if (c.hasItem(item)) {
				count++;
			}
		}
		return count;

	}

	private static boolean meetsConstraints(Bag bag, String item, Integer weight, ArrayList<Constraint> constraints,
			Set<String> Items, int maxItems) {
		// Checks the validity of adding an item to the list
		boolean valid = bag.fits(weight);
		valid = valid && (bag.items.size() <= maxItems);
		if (valid) {
			bag.addItem(item, weight);
			for (Constraint constraint : constraints) {
				boolean constValid = constraint.isValid(item, bag, Items);
				if (!constValid) {
					bag.items.remove(item);
					return false;
				}
			}
		}
		return valid;
	}
}
