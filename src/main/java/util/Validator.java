package util;

public class Validator {

    public boolean isNumeric(String s){
        if (s == null) {
            return false;
        }
        else return s.matches("-?\\d+(\\.\\d+)?");
    }
}
