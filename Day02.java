import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day02 {
    
    public static void main(String []args) {
        List<String> ids = new ArrayList();
        
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            ids.add(scanner.nextLine());
        }
        
        System.out.println(day2Part1(ids));
        System.out.println(day2Part2(ids));
    }

    private static Integer day2Part1(List<String> ids) {
        int secondRepetitions = 0;
        int thirdRepetitions = 0;
        Map<Integer, Boolean> secondAndThirdRepetitionsExists;
        
        for(String id : ids) {
            secondAndThirdRepetitionsExists = getSecondAndThirdRepetitions(id);
            
            if(secondAndThirdRepetitionsExists.get(2)) {
                secondRepetitions++;
            }
            if(secondAndThirdRepetitionsExists.get(3)) {
                thirdRepetitions++;
            }
        }
        
        return secondRepetitions * thirdRepetitions;
    }
    
    private static String day2Part2(List<String> ids) {
        boolean found = false;
        
        for (int i = 0; i < ids.size() && !found; i++) {
            for (int j = i+1; j < ids.size() && !found; j++) {
                if(differByOneCharacter(ids.get(i), ids.get(j))) {
                    return getMatchingCharacters(ids.get(i), ids.get(j));
                }
            }
        }
        
        return "";
    }
    
    private static Map<Integer, Boolean> getSecondAndThirdRepetitions(String id) {
        Map<Character, Integer> repetitions = new HashMap();
        for(char character : id.toCharArray()) {
            if (repetitions.containsKey(character)) {
                repetitions.put(character, repetitions.get(character) + 1);
            } else {
                repetitions.put(character, 1);
            }
        }
        
        Map<Integer, Boolean> isThereRepetition = new HashMap();
        isThereRepetition.put(2, repetitions.containsValue(2));
        isThereRepetition.put(3, repetitions.containsValue(3));
        return isThereRepetition;
    }
    
    private static Boolean differByOneCharacter(String id1, String id2) {
        int differenceCount = 0;
        
        if (id1.length() != id2.length()) {
            return false;
        } else {
            for(int i = 0; i < id1.length() && differenceCount <= 1; i++) {
                if(id1.charAt(i) != id2.charAt(i)) {
                    differenceCount++;
                }
            }
        }

        return differenceCount == 1;
    }
    
    private static String getMatchingCharacters(String id1, String id2) {
        StringBuilder stringBuilder = new StringBuilder();
        
        for(int i = 0; i < id1.length() && i < id2.length(); i++) {
            if(id1.charAt(i) == id2.charAt(i)) {
                stringBuilder.append(id1.charAt(i));
            }
        }
        
        return stringBuilder.toString();
    }
        
}