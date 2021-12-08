package day7;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        TreacheryOfWhales tOW = new TreacheryOfWhales();
        System.out.println("Part 1: " + tOW.process(1));
        System.out.println("Part 1: " + tOW.process(2));
    }
}

class TreacheryOfWhales {
    private final Map<Integer, Integer> data = new HashMap<>();
    private static int min;
    private static int max;

    public TreacheryOfWhales() {
        try {
            this.setData();
        } catch (IOException e) {
            System.out.println("Wrong input");
        }
    }

    public int process(int part) {
        List<Integer> fuelUsed = new ArrayList<>();
        for (int pos = min; pos <= max; pos ++) {
            int lowerSum = 0;
            for (int lowerIndex = pos - 1; lowerIndex >= min; lowerIndex --){
                lowerSum += (part == 1 ? this.countPart1LowerData(pos, lowerIndex) : this.countPart2LowerData(pos, lowerIndex));
            }
            int higherSum = 0;
            for (int higherIndex = pos + 1; higherIndex <= max; higherIndex ++){
                higherSum += (part == 1 ? this.countPart1HigherData(pos, higherIndex) : this.countPart2HigherData(pos, higherIndex));
            }
            fuelUsed.add(lowerSum + higherSum);
        }
        return fuelUsed.stream().mapToInt(num -> num).min().orElseThrow(NoSuchElementException::new);
    }

    private int countPart1LowerData(int pos, int index) {
        return (pos - index) * this.data.get(index);
    }

    private int countPart1HigherData(int pos, int index) {
        return (index - pos) * this.data.get(index);
    }

    private int countPart2LowerData(int pos, int index) {
        int tempSum = 0;
        for (int i = pos - 1; i >= index; i --) {
            tempSum += (pos - i);
        }
        return tempSum * this.data.get(index);
    }

    private int countPart2HigherData(int pos, int index) {
        int tempSum = 0;
        for (int i = pos + 1 ; i <= index; i ++) {
            tempSum += (i - pos);
        }
        return tempSum * this.data.get(index);
    }

    private void setData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day7/input.txt"));

        List<Integer> rawData = new ArrayList<>();
        String[] line = scanner.nextLine().split(",");
        for (String num : line) {
            rawData.add(Integer.parseInt(num));
        }
        Collections.sort(rawData);

        max = rawData.stream().mapToInt(num -> num).max().orElseThrow(NoSuchElementException::new);
        min = rawData.stream().mapToInt(num -> num).min().orElseThrow(NoSuchElementException::new);

        for (int pos = min; pos <= max; pos ++) {
            this.data.put(pos, 0);
        }
        for ( int crab : rawData ) {
            this.data.put(crab, this.data.get(crab) + 1);
        }
    }
}
