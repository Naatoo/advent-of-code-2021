package day13;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


public class Solution {

    public static void main(String[] args) {
        TransparentOrigami tP = new TransparentOrigami();
        System.out.println("Part 1: " + tP.part1());
        tP.part2();
    }
}

class TransparentOrigami {
    private final Set<List<Integer>> data = new HashSet<>();
    private final LinkedHashMap<String, Integer> foldInstructions = new LinkedHashMap<>();

    public TransparentOrigami() {
        try {
            this.setData();
        } catch (IOException e) {
            System.out.println("Wrong input");
        }
    }

    public int part1() {
        int xFoldLine;
        int yFoldLine;
        for (Map.Entry<String, Integer> folding : foldInstructions.entrySet()) {
            if (folding.getKey().contains("x")) {
                xFoldLine = folding.getValue();
                yFoldLine = 0;
            } else {
                yFoldLine = folding.getValue();
                xFoldLine = 0;
            }
            int maxX = this.getMaxIndex("row");
            int maxY = this.getMaxIndex("column");
            for (int indexX = xFoldLine; indexX < maxX; indexX++) {
                for (int indexY = yFoldLine; indexY < maxY; indexY++) {
                    if (this.data.contains(List.of(indexX, indexY))) {
                        if (folding.getKey().contains("x")) {
                            this.data.add(List.of(indexX - (indexX - xFoldLine) * 2, indexY));
                        } else {
                            this.data.add(List.of(indexX, indexY - (indexY - yFoldLine) * 2));
                        }
                        this.data.remove(List.of(indexX, indexY));
                    }
                }
            }
        }
        return this.data.size();
    }

    private int getMaxIndex(String lineType) {
        var index = lineType.equals("row") ? 0 : 1;
        var max = 0;
        for (List<Integer> coord : this.data) {
            max = coord.get(index) > max ? coord.get(index) : max;
        }
        return max;
    }

    public void part2() {
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 40; x++) {
                if (this.data.contains(List.of(x, y))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
                if (x == 39) {
                    System.out.println();
                }
            }
        }
    }

    private void setData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day13/input.txt"));
        int counter = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(",")) {
                String[] parsedLine = line.split(",");
                this.data.add(List.of(Integer.parseInt(parsedLine[0]), Integer.parseInt(parsedLine[1])));
            } else if (line.contains("=")) {
                String[] parsedLine = line.split(" ")[2].split("=");
                this.foldInstructions.put(parsedLine[0] + counter, Integer.parseInt(parsedLine[1]));
                counter++;
            }
        }
    }
}
