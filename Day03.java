import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {
    private static SquareStatus[][] FABRIC = SquareStatus.matrixOfDefault(1000, 1000);

    public static void main(String[] args) {
        List<Claim> claims = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            claims.add(new Claim(scanner.nextLine()));
        }

        System.out.println(day3Part1(claims));
        System.out.println(day3Part2(claims));
    }

    private static int day3Part1(List<Claim> claims) {
        int overlappingSquaresTotal = 0;

        for (Claim claim : claims) {
            overlappingSquaresTotal += cutAndCount(claim);
        }

        return overlappingSquaresTotal;
    }

    private static String day3Part2(List<Claim> claims) {
        for (Claim claim : claims) {
            if (!overlaps(claim)) {
                return claim.getId();
            }
        }

        return "";
    }

    private static int cutAndCount(Claim claim) {
        int overlappingSquaresSubtotal = 0;

        int lastColumn = claim.getInchesFromTop() + claim.getHeight();
        for (int i = claim.getInchesFromTop(); i < lastColumn; i++) {
            int lastRow = claim.getInchesFromLeft() + claim.getWidth();
            for (int j = claim.getInchesFromLeft(); j < lastRow; j++) {
                if (FABRIC[i][j].equals(SquareStatus.FREE)) {
                    FABRIC[i][j] = SquareStatus.CLAIMED;
                } else if (FABRIC[i][j].equals(SquareStatus.CLAIMED)) {
                    FABRIC[i][j] = SquareStatus.OVERLAPPED;
                    overlappingSquaresSubtotal++;
                }
            }
        }
        return overlappingSquaresSubtotal;
    }

    private static boolean overlaps(Claim claim) {
        boolean overlaps = false;

        int lastColumn = claim.getInchesFromTop() + claim.getHeight();
        for (int i = claim.getInchesFromTop(); i < lastColumn && !overlaps; i++) {
            int lastRow = claim.getInchesFromLeft() + claim.getWidth();
            for (int j = claim.getInchesFromLeft(); j < lastRow && !overlaps; j++) {
                if (!FABRIC[i][j].equals(SquareStatus.CLAIMED)) {
                    overlaps = true;
                }
            }
        }

        return overlaps;
    }
}

class Claim {
    private String id;
    private int inchesFromLeft;
    private int inchesFromTop;
    private int width;
    private int height;

    Claim(String claimString) {
        Pattern pattern = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");
        Matcher matcher = pattern.matcher(claimString);

        if (matcher.matches()) {
            this.id = matcher.group(1);
            this.inchesFromLeft = Integer.parseInt(matcher.group(2));
            this.inchesFromTop = Integer.parseInt(matcher.group(3));
            this.width = Integer.parseInt(matcher.group(4));
            this.height = Integer.parseInt(matcher.group(5));
        }
    }

    String getId() {
        return id;
    }

    int getInchesFromLeft() {
        return inchesFromLeft;
    }

    int getInchesFromTop() {
        return inchesFromTop;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }
}

enum SquareStatus {
    FREE, CLAIMED, OVERLAPPED;

    public static SquareStatus[][] matrixOfDefault(int width, int height) {
        SquareStatus[][] matrix = new SquareStatus[height][width];
        for (int i = 0; i < height; i++) {
            Arrays.fill(matrix[i], SquareStatus.FREE);
        }
        return matrix;
    }
}