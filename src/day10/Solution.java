package day10;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        SyntaxScoring sS = new SyntaxScoring();
        System.out.println("Part 1: " + sS.part1());
        System.out.println("Part 2: " + sS.part2());
    }
}

class SyntaxScoring {
    private final List<String[]> linesData = new ArrayList<>();
    private final List<String> openingSigns = List.of("[", "(", "{", "<");
    private final List<String> closingSigns = List.of("]", ")", "}", ">");
    private final Map<String, Integer> signValuesPart1 = new HashMap<>(Map.of(
            ")", 3,
            "]", 57,
            "}", 1197,
            ">", 25137
    ));
    private final Map<String, Integer> signValuesPart2 = new HashMap<>(Map.of(
            ")", 1,
            "]", 2,
            "}", 3,
            ">", 4
    ));
    private int sumCorrupted = 0;
    private final List<List<String>> incompleteLineClosingSigns = new ArrayList<>();


    public SyntaxScoring() {
        try {
            this.setData();
        } catch (IOException e) {
            System.out.println("Wrong input");
        }
        this.countSumCorruptedAndIncompleteLineClosingSigns();
    }


    public int part1() {
        return this.sumCorrupted;
    }

    public BigInteger part2() {
        List<BigInteger> incompleteLinesScore = new ArrayList<>();
        for (List<String> lineSigns : this.incompleteLineClosingSigns) {
            BigInteger lineSum = BigInteger.ZERO;
            for (String sign : lineSigns) {
                lineSum = lineSum.multiply(new BigInteger(String.valueOf(5)));
                lineSum = lineSum.add(new BigInteger(String.valueOf(this.signValuesPart2.get(sign))));
            }
            incompleteLinesScore.add(lineSum);
        }
        Collections.sort(incompleteLinesScore);
        return incompleteLinesScore.get((incompleteLinesScore.size() - 1) / 2);
    }

    private void countSumCorruptedAndIncompleteLineClosingSigns() {
        for (String[] line : this.linesData) {
            List<String> expectedSigns = new ArrayList<>();
            corrupted:
            {
                for (String sign : line) {
                    if (this.openingSigns.contains(sign)) {
                        String correspondingClosingSign = closingSigns.get(openingSigns.indexOf(sign));
                        expectedSigns.add(0, correspondingClosingSign);
                    } else {
                        if (expectedSigns.get(0).equals(sign)) {
                            expectedSigns.remove(0);
                        } else {
                            this.sumCorrupted += this.signValuesPart1.get(sign);
                            break corrupted;
                        }
                    }
                }
                this.incompleteLineClosingSigns.add(expectedSigns);
            }
        }
    }

    private void setData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day10/input.txt"));
        while (scanner.hasNextLine()) {
            this.linesData.add(scanner.nextLine().split(""));
        }
    }
}
