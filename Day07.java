import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day07 {
    public static void main(String[] args) {
        List<String> rawInstructions = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            rawInstructions.add(scanner.nextLine());
        }

        System.out.println(day7Part1(rawInstructions));
        System.out.println(day7Part2(rawInstructions));
    }

    private static String day7Part1(List<String> rawInstructions) {
        return new InstructionTree(rawInstructions).getSteps();
    }

    private static Integer day7Part2(List<String> rawInstructions) {
        return new InstructionTree(rawInstructions).getStepsTime();
    }
}

class InstructionTree {
    private static final Pattern STEP_PATTERN = Pattern.compile("Step (.) must be finished before step (.) can begin.");

    private Map<Character, Set<Character>> parents = new HashMap<>();
    private Map<Character, Set<Character>> children = new HashMap<>();
    private Set<Character> roots;
    private Set<Character> instructions = new HashSet<>();

    InstructionTree(List<String> rawInstructions) {
        for (String rawInstruction : rawInstructions) {
            Matcher stepMatcher = STEP_PATTERN.matcher(rawInstruction);
            if (stepMatcher.matches()) {
                Character parent = stepMatcher.group(1).charAt(0);
                Character child = stepMatcher.group(2).charAt(0);

                if (!parents.containsKey(child)) {
                    parents.put(child, new HashSet<>());
                }
                parents.get(child).add(parent);

                if (!children.containsKey(parent)) {
                    children.put(parent, new HashSet<>());
                }
                children.get(parent).add(child);

                instructions.add(parent);
                instructions.add(child);
            }
        }

        if (!parents.isEmpty()) {
            roots = new HashSet<>(children.keySet());
            roots.removeAll(parents.keySet());
        }
    }

    String getSteps() {
        StringBuilder steps = new StringBuilder();
        Set<Character> completedSteps = new HashSet<>();
        Set<Character> candidateSteps = new HashSet<>(roots);

        while (completedSteps.size() != instructions.size()) {
            List<Character> sortedCandidateSteps = new ArrayList<>(candidateSteps);
            Collections.sort(sortedCandidateSteps);

            Character nextStep = null;
            for (Character sortedCandidateStep : sortedCandidateSteps) {
                if (roots.contains(sortedCandidateStep) || completedSteps.containsAll(parents.get(sortedCandidateStep))) {
                    nextStep = sortedCandidateStep;
                    break;
                }
            }

            candidateSteps.remove(nextStep);
            completedSteps.add(nextStep);
            if (children.containsKey(nextStep)) {
                candidateSteps.addAll(children.get(nextStep));
            }
            steps.append(nextStep);
        }

        return steps.toString();
    }

    Integer getStepsTime() {
        final Integer numWorkers = 5;
        final Integer extraStepTime = 60;
        List<ElfWorker> elfWorkers = IntStream.range(0, numWorkers).mapToObj(i -> new ElfWorker(extraStepTime)).collect(Collectors.toList());

        Set<Character> completedSteps = new HashSet<>();
        Set<Character> candidateSteps = new HashSet<>(roots);

        int timeCounter;
        for (timeCounter = 0; completedSteps.size() != instructions.size(); timeCounter++) {
            for (ElfWorker elfWorker : elfWorkers) {
                List<Character> sortedCandidateSteps = new ArrayList<>(candidateSteps);
                Collections.sort(sortedCandidateSteps);

                if (elfWorker.isIdle()) {
                    Character nextStep = null;
                    for (Character sortedCandidateStep : sortedCandidateSteps) {
                        if (roots.contains(sortedCandidateStep) || completedSteps.containsAll(parents.get(sortedCandidateStep))) {
                            nextStep = sortedCandidateStep;
                            break;
                        }
                    }

                    if (nextStep != null) {
                        candidateSteps.remove(nextStep);
                        elfWorker.assignTask(nextStep);
                    }
                }

                elfWorker.tic();
            }

            for (ElfWorker elfWorker : elfWorkers) {
                if (elfWorker.hasFinished()) {
                    Character completedStep = elfWorker.getTask();
                    elfWorker.setIdle();

                    completedSteps.add(completedStep);
                    if (children.containsKey(completedStep)) {
                        candidateSteps.addAll(children.get(completedStep));
                    }
                }
            }
        }

        return timeCounter;
    }
}

class ElfWorker {
    private Character task;
    private Integer workLeft;
    private Integer extraStepTime;

    ElfWorker(Integer extraStepTime) {
        this.task = null;
        this.workLeft = 0;
        this.extraStepTime = extraStepTime;
    }

    void assignTask(Character newTask) {
        this.task = newTask;
        this.workLeft = extraStepTime + newTask - 64;
    }

    void tic() {
        if (workLeft != 0) {
            this.workLeft--;
        }
    }

    boolean isIdle() {
        return this.workLeft == 0;
    }

    void setIdle() {
        this.task = null;
    }

    boolean hasFinished() {
        return this.workLeft == 0 && task != null;
    }

    Character getTask() {
        return task;
    }
}