package day12;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

    public static void main(String[] args) {
        System.out.println("Part 1: " + new PassagePathing().part1());
        System.out.println("Part 2: " + new PassagePathing().part2());
    }
}

class PassagePathing {
    private final Map<String, List<String>> data = new HashMap<>();
    private final List<List<String>> foundPaths = new ArrayList<>();

    public PassagePathing() {
        try {
            this.setData();
        } catch (IOException e) {
            System.out.println("Wrong input");
        }
    }


    public int part1() {
        return processPart1(List.of("start"), this.data.get("start"));
    }

    private int processPart1(List<String> currentPath, List<String> possibleCaves) {
        for (String cave : possibleCaves) {
            List<String> tempPath = new ArrayList<>(currentPath);
            tempPath.add(cave);
            if (this.isForbiddenPathPart1(tempPath)) {
                continue;
            }
            if (!this.foundPaths.contains(tempPath)) {
                if (cave.equals("end")) {
                    this.foundPaths.add(tempPath);
                } else {
                    List<String> possibilities = this.getCavesBackwards(cave);
                    if (this.data.containsKey(cave)) {
                        possibilities = Stream.concat(possibilities.stream(),
                                this.data.get(cave).stream()).collect(Collectors.toList());
                    }
                    possibilities.remove("start");
                    processPart1(tempPath, possibilities);
                }
            }
        }
        return this.foundPaths.size();
    }

    public int part2() {
        return processPart2(List.of("start"), this.data.get("start"));
    }

    private int processPart2(List<String> currentPath, List<String> possibleCaves) {
        for (String cave : possibleCaves) {
            List<String> tempPath = new ArrayList<>(currentPath);
            if (this.isForbiddenPathPart2(tempPath, cave)) {
                continue;
            }
            tempPath.add(cave);
            if (cave.equals("end")) {
                if (!this.foundPaths.contains(tempPath)) {
                    this.foundPaths.add(tempPath);
                }
            } else {
                List<String> possibilities = this.getCavesBackwards(cave);
                if (this.data.containsKey(cave)) {
                    possibilities = Stream.concat(possibilities.stream(),
                            this.data.get(cave).stream()).collect(Collectors.toList());
                }
                possibilities.remove("start");
                processPart2(tempPath, possibilities);
            }
        }
        return this.foundPaths.size();
    }

    private boolean isForbiddenPathPart1(List<String> path) {
        List<String> usedCaves = new ArrayList<>();
        for (String cave : path) {
            if (cave.equals(cave.toLowerCase(Locale.ROOT))) {
                if (usedCaves.contains(cave)) {
                    return true;
                } else {
                    usedCaves.add(cave);
                }
            }
        }
        return false;
    }

    private boolean isForbiddenPathPart2(List<String> path, String cave) {
        Map<String, Long> counts = path.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        for (Map.Entry<String, Long> countMapping : counts.entrySet()) {
            if (countMapping.getKey().equals(countMapping.getKey().toLowerCase(Locale.ROOT))
                    && countMapping.getValue() == 2) {
                if (counts.containsKey(cave) && counts.get(cave) > 0 && cave.equals(cave.toLowerCase(Locale.ROOT))) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> getCavesBackwards(String cave) {
        List<String> backwardPossibilities = new ArrayList<>();
        for (Map.Entry<String, List<String>> mapping : this.data.entrySet()) {
            if (mapping.getValue().contains(cave)) {
                backwardPossibilities.add(mapping.getKey());
            }
        }
        return backwardPossibilities;
    }

    private void setData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day12/input.txt"));
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("-");
            if (line[1].equals("start") || line[0].equals("end")) {
                String temp = line[0];
                line[0] = line[1];
                line[1] = temp;
            }
            if (!this.data.containsKey(line[0])) {
                this.data.put(line[0], new ArrayList<>());
            }
            this.data.get(line[0]).add(line[1]);
        }
    }
}
