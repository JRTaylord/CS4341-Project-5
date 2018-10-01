import java.util.ArrayList;
import java.util.Set;

public class ForwardCheckList {
    ArrayList<ArrayList<String>> checkList;

    public ForwardCheckList(ArrayList<String> items, Set<String> bags, ArrayList<Constraint> constraints){
        for (int i = 0; i < items.size(); i++) {
            ArrayList<String> temp = new ArrayList<String>();
            temp.add(items.get(i));
            temp.addAll(bags);
            this.checkList.add(temp);
        }
        processInitialConstraints(constraints);
    }

    public ForwardCheckList(ForwardCheckList forwardCheckList){
        this.checkList = forwardCheckList.checkList;
    }

    public boolean checkConstraints(ArrayList<Bag> bags){

    }

    private void processInitialConstraints(ArrayList<Constraint> constraints){
        for (Constraint constraint :
                constraints) {
            switch (constraint.getType().charAt(0)){
                case 'b':
                case 'm':
                default:
                    break;
                case 'u':
                    ArrayList<String> unary = constraint.getContraints();
                    for (int i = 0; i < checkList.size(); i++) {
                        if (unary.get(0).equals(checkList.get(i).get(0))){
                            if(constraint.getType().charAt(1)=='i') {
                                for (int j = 1; j < checkList.get(i).size(); j++) {
                                    // a count of number of times the item appears in the constraint
                                    int count = 0;
                                    for (int k = 1; k < unary.size(); k++) {
                                        if (unary.get(k) == checkList.get(i).get(j)) count++;
                                    }
                                    if (count == 0){
                                        checkList.get(i).remove(j);
                                        j--;
                                    }
                                }
                            } else {
                                for (int j = 1; j < checkList.get(i).size(); j++) {
                                    // a count of number of times the item appears in the constraint
                                    int count = 0;
                                    for (int k = 1; k < unary.size(); k++) {
                                        if (unary.get(k) == checkList.get(i).get(j)) count++;
                                    }
                                    if (count != 0){
                                        checkList.get(i).remove(j);
                                        j--;
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }
}
