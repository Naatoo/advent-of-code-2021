package day3;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

public class Solution {

    public static void main(String[] args) {
        BinaryDiagnosticsPart1 bD = new BinaryDiagnosticsPart1();
        System.out.println("Part1: " + bD.process());
        BinaryDiagnosticsPart2 bD2 = new BinaryDiagnosticsPart2();
        System.out.println("Part2: " + bD2.process());
    }
}

class BinaryDiagnosticsPart1 {
    private String data;
    private final int bitsQuantity;


    public BinaryDiagnosticsPart1() {
        try {
            this.setData();
        } catch (IOException e) {
            System.out.println("Wrong input");
        }
        this.bitsQuantity = this.data.indexOf("\n");

    }

    private void setData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day3/input.txt"));
        scanner.useDelimiter("\\Z");
        this.data = scanner.next();
    }


    public int process() {
        int[] bitSum = this.parseReport();
        List<String[]> binaryNumbers = this.countBinaryNumbers(bitSum);
        return BinaryOperations.executeCalculations(binaryNumbers.get(0), binaryNumbers.get(1));
    }

    private int[] parseReport() {
        int[] bitSum = new int[this.bitsQuantity];
        var bitIndex = 0;
        for (String num : this.data.split("")) {
            if (num.equals("\n")) {
                bitIndex = 0;
            }
            else {
                bitSum[bitIndex] += Integer.parseInt(num);
                bitIndex++;
            }
        }
        return bitSum;
    }

    private List<String[]> countBinaryNumbers(int[] bitSum) {
        String[] firstNumber = new String[this.bitsQuantity];
        String[] secondNumber = new String[this.bitsQuantity];
        for (int i = 0; i < this.bitsQuantity; i++) {
            int bit = bitSum[i] / 500;
            firstNumber[i] = String.valueOf(bit);
            secondNumber[i] = bit == 0 ? "1" : "0";
        }
        List<String[]> numbers = new ArrayList<>();
        numbers.add(firstNumber);
        numbers.add(secondNumber);
        return numbers;
    }
}


class BinaryDiagnosticsPart2 {
    private final List<String> data = new ArrayList<>();
    private final int linesQuantity;
    private final int bitsQuantity;

    public BinaryDiagnosticsPart2() {
        try {
            this.setData();
        } catch (IOException e) {
            System.out.println("Wrong input");
        }
        this.linesQuantity = data.size();
        this.bitsQuantity = this.data.get(1).length();
    }

    private void setData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day3/input.txt"));
        while (scanner.hasNextLine()) {
            this.data.add(scanner.nextLine());
        }
    }

    public int process() {
        return BinaryOperations.executeCalculations(
                this.findBinaryNumber("1", "0").split(""),
                this.findBinaryNumber("0", "1").split(""));
    }

    public String findBinaryNumber(String strongerSign, String weakerSign) {
        List<String> currentData = new ArrayList<>(this.data);
        int dataSize = this.linesQuantity;
        for (int bitIndex = 0; bitIndex < this.bitsQuantity; bitIndex++) {
            int[] bitValues = new int[linesQuantity];
            for (int lineIndex = 0; lineIndex < dataSize; lineIndex++) {
                bitValues[lineIndex] = Integer.parseInt(String.valueOf(currentData.get(lineIndex).charAt(bitIndex)));
            }
            String bitChosenSign = IntStream.of(bitValues).sum() >= (double) currentData.size() / 2 ? strongerSign : weakerSign;
            Iterator<String> binaryToDelete = currentData.iterator();
            while (binaryToDelete.hasNext()) {
                String binaryNumber = binaryToDelete.next();
                if (!bitChosenSign.equals(String.valueOf(binaryNumber.charAt(bitIndex)))) {
                    binaryToDelete.remove();
                }
            }
            dataSize = currentData.size();
            if (dataSize == 1) {
                break;
            }
        }
        return currentData.get(0);
    }
}

class BinaryOperations {

    public static int executeCalculations(String[] first, String[] second) {
        int firstNumber = castToDecimal(first);
        int secondNumber = castToDecimal(second);
        return firstNumber * secondNumber;
    }

    private static int castToDecimal(String[] binaryNumber) {
        return Integer.parseInt(String.join("", binaryNumber), 2);
    }
}
