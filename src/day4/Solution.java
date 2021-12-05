package day4;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        GiantSquidPart1 bD = new GiantSquidPart1();
        System.out.println("Part 1: " + bD.processPart1());
        System.out.println("Part 2: " + bD.processPart2());
    }
}

class GiantSquidPart1 {
    private static final int rows = 5;
    private static final int columns = 5;
    private String[] order;
    private List<String[][]> data;
    private List<String[][]> transpositedData;

    public GiantSquidPart1() {
        try {
            this.setData();
        } catch (IOException e) {
            System.out.println("Wrong input");
        }
        this.setTranspositedData();
    }

    public int processPart1() {
        return this.countFinalScore();
    }

    public int processPart2() {
        return this.findMatrixWithMatchingRow();
    }

    private void setData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day4/input.txt"));
        this.order = scanner.nextLine().split(",");
        this.data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            scanner.nextLine();
            this.data.add(new String[rows][columns]);
            for (int row = 0; row < rows; row ++) {
                String rawLine = scanner.nextLine();
                if (rawLine.charAt(0) == ' ') {
                    rawLine = rawLine.substring(1);
                }
                String[] rawLineArray = rawLine.split("\\s+");
                this.data.get(this.data.size() - 1)[row] = rawLineArray;
            }
        }
    }

    private void setTranspositedData() {
        List<String[][]> transData = new ArrayList<>();
        for (String[][] matrix : this.data) {
            String[][] newMatrix = new String[rows][columns];
            for (int i = 0; i < rows; i ++) {
                newMatrix[i] = matrix[i].clone();
            }
            transData.add(newMatrix);
        }
        for (String[][] matrix : transData) {
            for (int r = 0; r < rows; r ++) {
                for (int c = r + 1; c < columns; c ++) {
                    String temp = matrix[r][c];
                    matrix[r][c] = matrix[c][r];
                    matrix[c][r] = temp;
                }
            }
        }
        this.transpositedData = new ArrayList<>(transData);
    }

    private int countFinalScore() {
        List<String> currentOrder = new ArrayList<>();
        for (String num : this.order) {
            currentOrder.add(num);
            int res = this.calculateSumRestWinner(this.data, currentOrder);
            if (res == -1) {
                res = this.calculateSumRestWinner(this.transpositedData, currentOrder);
            }
            if (res != -1) {
                return res * Integer.parseInt(num);
            }
        }
        return 0;
    }

    private int calculateSumRestWinner(List<String[][]> currentData, List<String> currentOrder) {
        for (String[][] matrix : currentData) {
            for (String[] dataset : matrix) {
                if (currentOrder.containsAll(Arrays.asList(dataset))) {
                    return this.calculateSumUnmarked(matrix, currentOrder);
                }
            }
        }
        return -1;
    }

    private int calculateSumUnmarked(String[][] matrix, List<String> currentOrder) {
        var sum = 0;
        for (String[] datasetInWinner : matrix) {
            for (String num : datasetInWinner) {
                if (!currentOrder.contains(num)) {
                    sum += Integer.parseInt(num);
                }
            }
        }
        return sum;
    }

    private int findMatrixWithMatchingRow() {
        List<String> currentOrder = new ArrayList<>();
        String[][] lastManStanding = new String[rows][columns];
        for (String num : this.order) {
            currentOrder.add(num);
            List<String[][]> dataMatrix = this.getMatrix(this.data, currentOrder);
            List<String[][]> transpositedDataMatrix = this.getMatrix(this.transpositedData, currentOrder);
            this.deleteMatrix(dataMatrix, false);
            this.deleteMatrix(transpositedDataMatrix, true);
            if (this.data.size() == 1 && this.transpositedData.size() == 1){
                lastManStanding = this.data.get(0);
            }
            if (this.data.isEmpty() && this.transpositedData.isEmpty()) {
                return this.calculateSumUnmarked(lastManStanding, currentOrder) * Integer.parseInt(num);
            }
        }
        return 0;
    }

    private List<String[][]> getMatrix(List<String[][]> currentData, List<String> currentOrder) {
        List<String[][]> matchedMatrix = new ArrayList<>();
        for (String[][] matrix : currentData) {
            for (String[] dataset : matrix) {
                if (currentOrder.containsAll(Arrays.asList(dataset))) {
                    matchedMatrix.add(matrix);
                }
            }
        }
        return matchedMatrix;
    }

    private void deleteMatrix(List<String[][]> foundMatrix, boolean isTransposited) {
        List<String[][]> sourceData;
        List<String[][]> additionalData;
        if (isTransposited) {
            sourceData = this.transpositedData;
            additionalData = this.data;
        }
        else {
            sourceData = this.data;
            additionalData = this.transpositedData;
        }
        if (foundMatrix.size() != 0) {
        for (String[][] matrix : foundMatrix) {
            if (sourceData.contains(matrix)) {
                additionalData.remove(sourceData.indexOf(matrix));
                sourceData.remove(matrix);
            }
        }
        }
    }
}

