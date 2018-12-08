import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day04 {

    public static void main(String[] args) {
        List<String> rawRecords = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            rawRecords.add(scanner.nextLine());
        }

        System.out.println(day4Part1(rawRecords));
        System.out.println(day4Part2(rawRecords));
    }

    private static Integer day4Part1(List<String> rawRecords) {
        return new RecordList(rawRecords).getStrategy1();
    }

    private static Integer day4Part2(List<String> rawRecords) {
        return new RecordList(rawRecords).getStrategy2();
    }
}

class RecordList {
    private static final Pattern BEGINS_SHIFT_PATTERN = Pattern.compile("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}\\] Guard #(\\d+) begins shift");
    private static final Pattern FALLS_ASLEEP_PATTERN = Pattern.compile("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:(\\d{2})\\] falls asleep");
    private static final Pattern WAKES_UP_PATTERN = Pattern.compile("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:(\\d{2})\\] wakes up");
    private static final int MAX_MINUTES = 60;

    private final List<AbstractMap.SimpleEntry<Integer, boolean[]>> guardRecords = new ArrayList<>();
    private final Map<Integer, Integer> asleepMinutes = new LinkedHashMap<>();

    RecordList(List<String> rawRecords) {
        Matcher beginsShiftMatcher;
        Matcher fallsAsleepMatcher;
        Matcher wakesUpMatcher;

        Integer currentMinute = 0;
        Integer nextMinute = 0;
        Integer currentGuard = 0;
        boolean[] currentHour = new boolean[MAX_MINUTES];
        Collections.sort(rawRecords);

        for (String rawRecord : rawRecords) {
            beginsShiftMatcher = BEGINS_SHIFT_PATTERN.matcher(rawRecord);
            if (beginsShiftMatcher.matches()) {
                for (int i = currentMinute; i < MAX_MINUTES; i++) {
                    currentHour[i] = true;
                }
                currentGuard = Integer.parseInt(beginsShiftMatcher.group(1));
                guardRecords.add(new AbstractMap.SimpleEntry<>(currentGuard, new boolean[MAX_MINUTES]));
                currentMinute = 0;
                currentHour = guardRecords.get(guardRecords.size() - 1).getValue();
                continue;
            }

            fallsAsleepMatcher = FALLS_ASLEEP_PATTERN.matcher(rawRecord);
            if (fallsAsleepMatcher.matches()) {
                nextMinute = Integer.parseInt(fallsAsleepMatcher.group(1));
                for (int i = currentMinute; i < nextMinute; i++) {
                    currentHour[i] = true;
                }
                currentMinute = nextMinute;
                continue;
            }

            wakesUpMatcher = WAKES_UP_PATTERN.matcher(rawRecord);
            if (wakesUpMatcher.matches()) {
                nextMinute = Integer.parseInt(wakesUpMatcher.group(1));
                for (int i = currentMinute; i < nextMinute; i++) {
                    currentHour[i] = false;
                }
                if (asleepMinutes.containsKey(currentGuard)) {
                    asleepMinutes.put(currentGuard, asleepMinutes.get(currentGuard) + nextMinute - currentMinute);
                } else {
                    asleepMinutes.put(currentGuard, nextMinute - currentMinute);
                }
                currentMinute = nextMinute;
            }
        }
    }

    Integer getStrategy1() {
        Integer guardId = asleepMinutes
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();

        List<AbstractMap.SimpleEntry<Integer, boolean[]>> guardRecordsForId = guardRecords.
                stream()
                .filter(x -> guardId.equals(x.getKey()))
                .collect(Collectors.toList());

        Integer maxCount = 0;
        Integer maxMinute = 0;
        for (int i = 0; i < MAX_MINUTES; i++) {
            Integer currentCount = 0;
            for (AbstractMap.SimpleEntry<Integer, boolean[]> guardRecordForId : guardRecordsForId) {
                if (!guardRecordForId.getValue()[i]) {
                    currentCount++;
                }
            }
            if (currentCount > maxCount) {
                maxCount = currentCount;
                maxMinute = i;
            }
        }

        return guardId * maxMinute;
    }

    Integer getStrategy2() {
        Set<Integer> guardIds = asleepMinutes.keySet();

        Integer maxFrequency = 0;
        Integer maxMinute = 0;
        Integer maxGuardId = 0;
        for (Integer guardId : guardIds) {
            List<AbstractMap.SimpleEntry<Integer, boolean[]>> guardRecordsForId = guardRecords
                    .stream()
                    .filter(x -> guardId.equals(x.getKey()))
                    .collect(Collectors.toList());

            for (int i = 0; i < MAX_MINUTES; i++) {
                Integer currentFrequency = 0;
                for (AbstractMap.SimpleEntry<Integer, boolean[]> guardRecordForId : guardRecordsForId) {
                    if (!guardRecordForId.getValue()[i]) {
                        currentFrequency++;
                    }
                }
                if (currentFrequency > maxFrequency) {
                    maxFrequency = currentFrequency;
                    maxMinute = i;
                    maxGuardId = guardId;
                }
            }
        }

        return maxGuardId * maxMinute;
    }
}


