import java.util.*;

public class Day06 {
    private static final Integer MAX_DISTANCE = 10000;

    private static Map<Integer, Map.Entry<Integer, Integer>> normalizedCoordinates = new HashMap<>();
    private static Integer gridWidth = 0;
    private static Integer gridHeight = 0;

    public static void main(String[] args) {
        List<String> rawCoordinates = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            rawCoordinates.add(scanner.nextLine());
        }

        System.out.println(day6Part1(rawCoordinates));
        System.out.println(day6Part2(rawCoordinates));
    }

    private static Integer day6Part1(List<String> rawCoordinates) {
        normalizeCoordinates(rawCoordinates);

        Map<Integer, Integer> areas = new HashMap<>();
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                boolean equallyFar = false;
                int minDistance = Integer.MAX_VALUE;
                Map.Entry<Integer, Map.Entry<Integer, Integer>> currentCoordinate = new AbstractMap.SimpleEntry<>(0, new AbstractMap.SimpleEntry<>(0, 0));
                for (Map.Entry<Integer, Map.Entry<Integer, Integer>> normalizedCoordinate : normalizedCoordinates.entrySet()) {
                    int currentDistance = manhattanDistance(j, i, normalizedCoordinate.getValue().getKey(), normalizedCoordinate.getValue().getValue());
                    if (currentDistance < minDistance) {
                        minDistance = currentDistance;
                        equallyFar = false;
                        currentCoordinate = normalizedCoordinate;
                    } else if (currentDistance == minDistance) {
                        equallyFar = true;
                    }
                }
                if (!equallyFar && !isInEdge(currentCoordinate)) {
                    if (areas.containsKey(currentCoordinate.getKey())) {
                        areas.put(currentCoordinate.getKey(), areas.get(currentCoordinate.getKey()) + 1);
                    } else {
                        areas.put(currentCoordinate.getKey(), 1);
                    }
                }
            }
        }

        return areas.values().stream().mapToInt(x -> x).max().orElse(Integer.MIN_VALUE);
    }

    private static Integer day6Part2(List<String> rawCoordinates) {
        normalizeCoordinates(rawCoordinates);

        int totalArea = 0;
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                int sumDistances = 0;
                for (Map.Entry<Integer, Map.Entry<Integer, Integer>> coordinate : normalizedCoordinates.entrySet()) {
                    sumDistances += manhattanDistance(j, i, coordinate.getValue().getKey(), coordinate.getValue().getValue());
                }
                if (sumDistances < MAX_DISTANCE) {
                    totalArea++;
                }
            }
        }

        return totalArea;
    }

    private static void normalizeCoordinates(List<String> rawCoordinates) {
        List<Map.Entry<Integer, Integer>> coordinates = new ArrayList<>();

        for (String rawCoordinate : rawCoordinates) {
            String[] splitRawCoordinate = rawCoordinate.split(", ");
            coordinates.add(new AbstractMap.SimpleEntry<>(Integer.valueOf(splitRawCoordinate[0]), Integer.valueOf(splitRawCoordinate[1])));
        }

        Integer minColumn = coordinates.stream().mapToInt(Map.Entry::getKey).min().orElse(Integer.MIN_VALUE);
        Integer maxColumn = coordinates.stream().mapToInt(Map.Entry::getKey).max().orElse(Integer.MAX_VALUE);
        Integer minRow = coordinates.stream().mapToInt(Map.Entry::getValue).min().orElse(Integer.MIN_VALUE);
        Integer maxRow = coordinates.stream().mapToInt(Map.Entry::getValue).max().orElse(Integer.MAX_VALUE);

        for (int i = 0; i < coordinates.size(); i++) {
            normalizedCoordinates.put(i + 1, new AbstractMap.SimpleEntry<>(coordinates.get(i).getKey() - minColumn, coordinates.get(i).getValue() - minRow));
        }

        gridWidth = maxColumn - minColumn + 1;
        gridHeight = maxRow - minRow + 1;
    }

    private static int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private static boolean isInEdge(Map.Entry<Integer, Map.Entry<Integer, Integer>> currentCoordinate) {
        return currentCoordinate.getValue().getKey().equals(0) ||
                currentCoordinate.getValue().getKey().equals(gridWidth - 1) ||
                currentCoordinate.getValue().getValue().equals(0) ||
                currentCoordinate.getValue().getValue().equals(gridHeight - 1);
    }
}
