package day20;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Solution {

    public static void main(String[] args) throws IOException {
        TrenchMap tM = new TrenchMap(readData());
        System.out.println("Part 1: " + tM.part1());
        System.out.println("Part 2: " + tM.part2());
    }

    private static Object[] readData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day20/input.txt"));
        String enhancementAlgorithm = scanner.nextLine();
        scanner.nextLine();
        List<List<String>> inputImage = new ArrayList<>();
        while (scanner.hasNextLine()) {
            inputImage.add(Arrays.asList(scanner.nextLine().split("")));

        }
        return new Object[]{enhancementAlgorithm, inputImage};
    }

}

class TrenchMap {

    private final String enhancementAlgorithm;
    private final List<List<String>> initialInputImage;
    private int sumWhitePixels = 0;


    public TrenchMap(Object[] data) {
        this.enhancementAlgorithm = (String) data[0];
        this.initialInputImage = (List<List<String>>) data[1];
    }

    private static String getRawOutput(List<List<String>> inputImage, int rowIndex, int columnIndex, int stepIndex) {
        StringBuilder builder = new StringBuilder();
        for (int xIndex = rowIndex - 1; xIndex <= rowIndex + 1; xIndex++) {
            for (int yIndex = columnIndex - 1; yIndex <= columnIndex + 1; yIndex++) {
                String newSign;
                if (xIndex < 0 || xIndex >= inputImage.size() || yIndex < 0 || yIndex >= inputImage.size()) {
                    newSign = stepIndex % 2 == 0 ? "1" : "0";
                } else {
                    newSign = inputImage.get(xIndex).get(yIndex).equals(".") ? "0" : "1";
                }
                builder.append(newSign);
            }
        }
        return builder.toString();
    }

    private List<List<String>> getOutput(List<List<String>> inputImage, int stepIndex) {
        int size = 3;
        int minPixelIndex = -size;
        int maxPixelIndex = inputImage.get(0).size() + size;
        List<List<String>> output = new ArrayList<>();
        this.sumWhitePixels = 0;
        for (int rowIndex = minPixelIndex; rowIndex < maxPixelIndex; rowIndex++) {
            List<String> outputRow = new ArrayList<>();
            for (int columnIndex = minPixelIndex; columnIndex < maxPixelIndex; columnIndex++) {
                String rawOutput = getRawOutput(inputImage, rowIndex, columnIndex, stepIndex);
                String outputSign = this.getOutputString(rawOutput);
                outputRow.add(outputSign);
                if (outputSign.equals("#")) {
                    this.sumWhitePixels++;
                }
            }
            output.add(outputRow);
        }
        return output;
    }

    private String getOutputString(String rawOutput) {
        int val = Integer.parseInt(rawOutput, 2);
        return this.enhancementAlgorithm.charAt(val) == '.' ? "." : "#";
    }

    private void printImage(List<List<String>> image) {
        for (int xIndex = 0; xIndex < image.size(); xIndex++) {
            for (int yIndex = 0; yIndex < image.size(); yIndex++) {
                System.out.print(image.get(xIndex).get(yIndex));
                if (yIndex == image.size() - 1) {
                    System.out.println("\n");
                }
            }
        }
    }


    private void process(List<List<String>> output, int steps) {
        for (int step = 1; step <= steps; step++) {
            output = this.getOutput(output, step);
        }
    }


    public int part1() {
        this.process(this.initialInputImage, 2);
        return this.sumWhitePixels;
    }

    public int part2() {
        this.process(this.initialInputImage, 50);
        return this.sumWhitePixels;
    }
}
