package day25;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Solution {

    public static void main(String[] args) throws IOException {
        SeaCucumber sC = new SeaCucumber(readData());
        System.out.println("Part 1: " + sC.part1());
        System.out.println("Part 2: " + "END :)");
    }

    private static List<List<String>> readData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day25/input.txt"));
        List<List<String>> data = new ArrayList<>();
        var row = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            var column = 0;
            List<String> rowData = new ArrayList<>();
            for (String sign : line.split("")) {
                rowData.add(sign);
                column++;
            }
            row++;
            data.add(rowData);
        }
        System.out.println(data);
        return data;
    }
}

class SeaCucumber {

    private final String[][] arrayData;
    private final int rowSize;
    private final int colSize;
    private boolean hasChanged = true;


    public SeaCucumber(List<List<String>> data) {
        this.rowSize = data.size();
        this.colSize = data.get(0).size();
        String[][] array = new String[data.size()][data.get(0).size()];

        for (int x = 0; x < this.rowSize; x++) {
            for (int y = 0; y < this.colSize; y++) {
                array[x][y] = data.get(x).get(y);
            }
        }
        this.arrayData = array;
    }

    public int part1() {
        var step = 0;
        this.hasChanged = true;
        while (hasChanged) {
            this.hasChanged = false;
            this.processStepEast();
            this.processStepSouth();
            step++;
        }
        return step;
    }


    private void processStepEast() {
        for (int x = 0; x < this.rowSize; x++) {
            boolean hasFirstColMoved = false;
            for (int y = 0; y < this.colSize; y++) {
                if (this.arrayData[x][y].equals(">")) {
                    if (y == this.colSize - 1 && hasFirstColMoved) {
                        continue;
                    }
                    int newY = this.getCoordToMove(y, this.colSize);
                    if (this.arrayData[x][newY].equals(".")) {
                        this.arrayData[x][y] = ".";
                        this.arrayData[x][newY] = ">";
                        if (y == 0) {
                            hasFirstColMoved = true;
                        }
                        this.hasChanged = true;
                        y++;
                    }
                }
            }
        }
    }

    private void processStepSouth() {
        for (int y = 0; y < this.colSize; y++) {
            boolean hasFirstRowMoved = false;
            for (int x = 0; x < this.rowSize; x++) {
                if (this.arrayData[x][y].equals("v")) {
                    if (x == this.rowSize - 1 && hasFirstRowMoved) {
                        continue;
                    }
                    int newX = this.getCoordToMove(x, this.rowSize);
                    if (this.arrayData[newX][y].equals(".")) {
                        this.arrayData[x][y] = ".";
                        this.arrayData[newX][y] = "v";
                        if (x == 0) {
                            hasFirstRowMoved = true;
                        }
                        this.hasChanged = true;
                        x++;
                    }
                }
            }
        }
    }

    private int getCoordToMove(int coord, int max) {
        return coord == max - 1 ? 0 : coord + 1;
    }

}