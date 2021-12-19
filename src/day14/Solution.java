package day14;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


public class Solution {

    public static void main(String[] args) {
        ExtendedPolymerization eP = new ExtendedPolymerization();
        System.out.println("Part 1: " + eP.part1());
        System.out.println("Part 2: " + eP.part2());
    }
}

class ExtendedPolymerization {
    private final List<String> template = new ArrayList<>();
    private final Map<List<String>, String> rules = new LinkedHashMap<>();

    public ExtendedPolymerization() {
        try {
            this.setData();
        } catch (IOException e) {
            System.out.println("Wrong input");
        }
    }

    public int part1() {
        List<String> oldTemplate = new ArrayList<>(this.template);
        List<String> newTemplate = new ArrayList<>(oldTemplate);

        for (int step = 0; step < 10; step++) {
            var added = 0;
            for (int index = 0; index < oldTemplate.size() - 1; index++) {
                if (rules.containsKey(oldTemplate.subList(index, index + 2))) {
                    newTemplate.add(index + 1 + added, this.rules.get(oldTemplate.subList(index, index + 2)));
                    added++;
                }
            }
            oldTemplate = new ArrayList<>(newTemplate);
        }
        int max = 0;
        int min = newTemplate.size();
        for (String sign : new HashSet<>(this.template)) {
            int occurrences = Collections.frequency(oldTemplate, sign);
            if (occurrences > max) {
                max = occurrences;
            } else if (occurrences < min) {
                min = occurrences;
            }
        }
        return max - min;
    }

    public Long part2() {
        Map<String, String> rulesMap = new HashMap<>();
        for (Map.Entry<List<String>, String> rule : this.rules.entrySet()) {
            rulesMap.put(String.join("", rule.getKey()), rule.getValue());
        }

        Map<String, Long> occurrences = new HashMap<>();
        for (int index = 0; index < this.template.size() - 1; index++) {
            String pair = String.join("", this.template.subList(index, index + 2));
            if (!occurrences.containsKey(pair)) {
                occurrences.put(pair, 1L);
            } else {
                occurrences.put(pair, occurrences.get(pair) + 1);
            }
        }

        for (int step = 0; step < 40; step++) {
            Map<String, Long> newOccurrences = new HashMap<>();
            for (Map.Entry<String, Long> pairMapping : occurrences.entrySet()) {
                if (rulesMap.containsKey(pairMapping.getKey())) {
                    Map<String, Long> data = new HashMap<>();
                    data.put(pairMapping.getKey().charAt(0) + rulesMap.get(pairMapping.getKey()),
                            pairMapping.getValue());
                    data.put(rulesMap.get(pairMapping.getKey()) + pairMapping.getKey().charAt(1),
                            pairMapping.getValue());
                    for (Map.Entry<String, Long> dataRow : data.entrySet()) {
                        newOccurrences.put(dataRow.getKey(),
                                newOccurrences.getOrDefault(dataRow.getKey(), 0L) + dataRow.getValue());
                    }
                } else {
                    newOccurrences.put(pairMapping.getKey(),
                            newOccurrences.getOrDefault(pairMapping.getKey(), 0L) + pairMapping.getValue());
                }
            }
            occurrences = new HashMap<>(newOccurrences);
        }

        Map<String, Long> count = new HashMap<>();
        for (Map.Entry<String, Long> pairOcc : occurrences.entrySet()) {
            for (String letter : pairOcc.getKey().split("")) {
                if (!count.containsKey(letter)) {
                    count.put(letter, pairOcc.getValue());
                } else {
                    count.put(letter, count.get(letter) + pairOcc.getValue());
                }
            }
        }
        count.put(this.template.get(0), count.get(this.template.get(0)) + 1);
        count.put(this.template.get(this.template.size() - 1), count.get(this.template.get(this.template.size() - 1)) + 1);
        count.replaceAll((k, v) -> v / 2);

        List<Long> values = new ArrayList<>(count.values());
        Collections.sort(values);
        return values.get(values.size() - 1) - values.get(0);
    }

    private void setData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day14/input.txt"));
        this.template.addAll(Arrays.asList(scanner.nextLine().split("")));
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String[] elems = scanner.nextLine().split(" -> ");
            this.rules.put(Arrays.asList(elems[0].split("")), elems[1]);
        }
    }
}
