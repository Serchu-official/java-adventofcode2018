import java.util.Scanner;

public class Day05 {
    private static char[] UNITS = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String polymer = scanner.nextLine();

        System.out.println(day5Part1(polymer));
        System.out.println(day5Part2(polymer));
    }

    private static Integer day5Part1(String polymer) {
        return react(polymer).length();
    }

    private static Integer day5Part2(String polymer) {
        String candidatePolymer;
        Integer minLength = Integer.MAX_VALUE;

        for (char unit : UNITS) {
            candidatePolymer = polymer.replaceAll(String.valueOf(unit), "");
            candidatePolymer = candidatePolymer.replaceAll(String.valueOf((char) (unit + 32)), "");
            Integer currentLenght = react(candidatePolymer).length();
            if (currentLenght < minLength) {
                minLength = currentLenght;
            }
        }

        return minLength;
    }

    private static String react(String polymer){
        StringBuilder polymerBuilder = new StringBuilder(polymer);

        for (int i = 0; i < polymerBuilder.length() - 1; i++) {
            if (Math.abs(polymerBuilder.charAt(i) - polymerBuilder.charAt(i + 1)) == 32) {
                polymerBuilder.deleteCharAt(i);
                polymerBuilder.deleteCharAt(i);
                i = Math.max(i - 2, -1);
            }
        }

        return polymerBuilder.toString();
    }
}