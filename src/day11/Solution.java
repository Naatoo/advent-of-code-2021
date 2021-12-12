package day11;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        System.out.println("Part 1: " + new DumboOctopus().part1(100));
        System.out.println("Part 2: " + new DumboOctopus().part2());
    }
}

class DumboOctopus {
    private final static int rowsQuantity = 10;
    private final static int columnsQuantity = 10;
    private final Integer[][] data = new Integer[rowsQuantity][columnsQuantity];
    private final List<List<Integer>> flashedInStep = new ArrayList<>();

    public DumboOctopus() {
        try {
            this.setData();
        } catch (IOException e) {
            System.out.println("Wrong input");
        }
    }


    public int part1(int steps) {
        var flashedSum = 0;
        for (int stepIndex = 0; stepIndex < steps; stepIndex++) {
            this.processInitial();
            flashedSum += this.flashedInStep.size();
            this.setFlashedToZero();
            this.flashedInStep.clear();
        }
        return flashedSum;
    }

    public int part2() {
        Integer stepIndexFound = null;
        int stepIndex = 1;
        while (stepIndexFound == null) {
            this.processInitial();
            this.setFlashedToZero();
            if (doAllflash()) {
                stepIndexFound = stepIndex;
            }
            this.flashedInStep.clear();
            stepIndex++;
        }
        return stepIndexFound;
    }

    private void processInitial() {
        this.increaseAllByOne();
        for (int rowIndex = 0; rowIndex < rowsQuantity; rowIndex ++) {
            for (int columnIndex = 0; columnIndex < columnsQuantity; columnIndex ++) {
                if (this.data[rowIndex][columnIndex] > 9 && !this.hasAlreadyFlashedInStep(new Integer[]{rowIndex, columnIndex})) {
                    this.flashedInStep.add(List.of(rowIndex, columnIndex));
                    this.processNeighbours(new Integer[]{rowIndex, columnIndex});
                }
            }
        }
    }


    private void processNeighbours (Integer[] currentCoord) {
        for (Integer[] possibleCoord : getAllSurroundingCoords(currentCoord[0], currentCoord[1])) {
            if (this.hasAlreadyFlashedInStep(possibleCoord)) {
                continue;
            }
            this.data[possibleCoord[0]][possibleCoord[1]] ++;
            if (this.data[possibleCoord[0]][possibleCoord[1]] > 9) {
                this.flashedInStep.add(List.of(possibleCoord[0], possibleCoord[1]));
                this.processNeighbours(possibleCoord);
            }
        }
    }

    private boolean hasAlreadyFlashedInStep(Integer[] coords) {
        return this.flashedInStep.contains(Arrays.asList(coords));
    }


    private boolean doAllflash() {
        return this.flashedInStep.size() == 100;
    }

    private void setFlashedToZero() {
        for (List<Integer> flashedCoord : this.flashedInStep) {
                this.data[flashedCoord.get(0)][flashedCoord.get(1)] = 0;
        }
    }

    private void increaseAllByOne() {
        for (int rowIndex = 0; rowIndex < rowsQuantity; rowIndex ++) {
            for (int columnIndex = 0; columnIndex < columnsQuantity; columnIndex ++) {
                this.data[rowIndex][columnIndex] ++;
            }
    }}

    private static List<Integer[]> getAllSurroundingCoords(Integer x, Integer y) {
        List<Integer[]> surroundingCoords = new ArrayList<>();
        for (int row = x - 1; row <= x + 1; row ++) {
            for (int column = y - 1; column <= y + 1; column++) {
                if ((row == x && column == y) || !coordsInBounds(row, column)) {
                    continue;
                }
                surroundingCoords.add(new Integer[]{row, column});
            }
        }
        return surroundingCoords;
    }

    private static boolean coordsInBounds(Integer x, Integer y) {
        return (x >= 0 && x <= rowsQuantity - 1 && y >= 0 && y <= columnsQuantity - 1);
    }

    private void setData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day11/input.txt"));
        var rowIndex = 0;
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("");
            for (int columnIndex = 0; columnIndex < columnsQuantity; columnIndex ++) {
                this.data[rowIndex][columnIndex] = Integer.parseInt(line[columnIndex]);
            }
            rowIndex++;
        }
    }
}
