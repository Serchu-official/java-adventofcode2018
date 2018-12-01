import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day01 {

    public static void main(String []args) {
        List<Integer> numbers = new ArrayList();

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            numbers.add(scanner.nextInt());
        }

        System.out.println(day1Part1(numbers));
        System.out.println(day1Part2(numbers));
    }

    private static Integer day1Part1(List<Integer> numbers) {
        return numbers.stream().mapToInt(Integer::intValue).sum();
    }

    private static Integer day1Part2(List<Integer> numbers) {
        Set<Integer> frecuencies = new HashSet();

        Integer currentFrecuency = 0;
        for(int i = 0; !frecuencies.contains(currentFrecuency); i = (i + 1) % numbers.size()) {
            frecuencies.add(currentFrecuency);
            currentFrecuency += numbers.get(i);
        }

        return currentFrecuency;
    }

}