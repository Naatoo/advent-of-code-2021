package day15;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


public class Solution {

    public static void main(String[] args) throws IOException {
        System.out.println("Part 1: " + new Chiton(readData(), 1).run());
        System.out.println("Part 2: " + new Chiton(readData(), 5).run());
    }

    private static List<String> readData() throws IOException {
        List<String> stringData = new ArrayList<>();
        Scanner scanner = new Scanner(Path.of("src/day15/input.txt"));
        while (scanner.hasNextLine()) {
            stringData.add(scanner.nextLine());
        }
        return stringData;
    }

}

class Chiton {
    private final int length;
    private final int[][] data;
    private final int[][] minRiskLevel;

    private final PriorityQueue<Integer[]> queue = new PriorityQueue<>(100, Comparator.comparingInt(a -> a[2]));

    public Chiton(List<String> rawData, int repeat) {
        int rawlength = rawData.get(0).length();
        this.length = rawData.get(0).length() * repeat;
        this.data = new int[length][length];
        this.minRiskLevel = new int[length][length];
        for (int row = 0; row < length; row++) {
            String[] line = rawData.get(row % rawlength).split("");
            for (int column = 0; column < length; column++) {
                int newVal = Integer.parseInt(line[column % rawlength]) + column / rawlength + row / rawlength;
                if (newVal <= 9) {
                    this.data[row][column] = newVal;
                } else {
                    this.data[row][column] = (newVal + 1) % 10;
                }
                this.minRiskLevel[row][column] = Integer.MAX_VALUE;
            }
        }
        this.minRiskLevel[0][0] = this.data[0][0];
    }

    private static Integer[][] getNeigh(Integer[] field) {
        return new Integer[][]{
                {field[0] - 1, field[1]}, {field[0] + 1, field[1]},
                {field[0], field[1] - 1}, {field[0], field[1] + 1}};
    }

    public int run() {
        Integer[] nearest = new Integer[]{0, 0};
        while (!Arrays.equals(nearest, new Integer[]{length - 1, length - 1})) {
            this.process(nearest);
            Integer[] temp = this.queue.remove();
            nearest = new Integer[]{temp[0], temp[1]};
        }
        return this.minRiskLevel[length - 1][length - 1] - this.minRiskLevel[0][0];
    }

    private void process(Integer[] field) {
        Integer[][] neighbours = getNeigh(field);
        int riskLevelBasic = this.minRiskLevel[field[0]][field[1]];
        for (Integer[] neigh : neighbours) {
            if (neigh[0] < 0 || neigh[0] >= this.length || neigh[1] < 0 || neigh[1] >= this.length) {
                continue;
            }
            var newRisk = riskLevelBasic + this.data[neigh[0]][neigh[1]];
            if (newRisk < this.minRiskLevel[neigh[0]][neigh[1]]) {
                this.minRiskLevel[neigh[0]][neigh[1]] = newRisk;
                this.queue.add(new Integer[]{neigh[0], neigh[1], newRisk});

            }
        }
    }

}
