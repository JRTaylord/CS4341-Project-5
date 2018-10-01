CS4341-Project-5
To Run the Code EITHER:
	1. Set up an IDE to take your .txt file as an input or replace the provided TestData.txt with your input
	2. Run java from the command line with the path to your file as the second input
Output is to SDOUT

Approach (backtracking) psuedocode:
public Map backTrack(items, bags, constraints, maxItems, minItems) {
	if (items.isEmpty()) {
		if (checkBagMinSize(bags, minItems)) {
			return bags;
		} else {
			return null;
		}
	}
	item = items.next();
	for (bag in bags) {
		if (meetsConstraints(bag, item, weight, constraints, items, maxItems)) {
			 result = backTrack(new HashMap<String, Integer>(items), bags, constraints,
					maxItems, minItems);
			if (result == null) {
				bag.remove(item);
			} else {
				return result;
			}
		}
	}
	return null;
}
	
This approach involves simply taking the next variable and trying to assign it each value until a solution is found. 

Approach (MRV, LCV, deggree) psuedocode:
public Map backTrack(items, bags, constraints, maxItems, minItems) {
	if (items.isEmpty()) {
		if (checkBagMinSize(bags, minItems)) {
			return bags;
		} else {
			return null;
		}
	}
	item = items.getMRV();
	for (bag in orderByLCV(bags)) {
		if (meetsConstraints(bag, item, weight, constraints, items, maxItems)) {
			 result = backTrack(new HashMap<String, Integer>(items), bags, constraints,
					maxItems, minItems);
			if (result == null) {
				bag.remove(item);
			} else {
				return result;
			}
		}
	}
	return null;
}
This approach uses the MRV, degree and LCV as heuristics to increase the efficiency of the algorithm. The MRV works by finding the variable 
that can have the smallest number of remaining values by testing each variable and seeing how many bags it fits into using the same meets constraint method
as the inner algorithm. It breaks ties by using degree, that is the selecting the variable with the most constraints. The values are then sorted by LCV that 
is the value which provides the most valid options for the future related variables. Our code does this by simulating adding the one item then checking the 
possible bags for the item afterwards.


Tests:
We ran test 19-24 from the input file and took the average of each time and check calls in the algorithm.


 
