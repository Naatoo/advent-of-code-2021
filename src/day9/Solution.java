package day9;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        SmokeBasin sB = new SmokeBasin();
        System.out.println("Part 1: " + sB.part1());
        System.out.println("Part 2: " + sB.part2());
    }
}

class SmokeBasin {
    private final List<List<Integer>> locations = new ArrayList<>();
    List<List<Integer>> lowPointsCoords = new ArrayList<>();
    private Integer rowSize = null;
    private Integer columnSize = null;
    private final List<Integer[]> currentBasinCoords = new ArrayList<>();


    public SmokeBasin() {
        try {
            this.setData();
        } catch (IOException e) {
            System.out.println("Wrong input");
        }
    }

    public int part1() {
        for (int rowIndex = 0; rowIndex < this.rowSize; rowIndex ++) {
            for (int columnIndex = 0; columnIndex < this.columnSize; columnIndex++) {
                int currentValue = this.locations.get(rowIndex).get(columnIndex);
                Integer[][] surroundingIndexes = getSurroundingIndexes(rowIndex, columnIndex);
                int exp = 4;
                int act = 0;
                    for (Integer[] coords : surroundingIndexes) {
                        if (!coordsInBounds(coords)) {
                            exp --;
                            continue;
                        }
                        if (this.locations.get(coords[0]).get(coords[1]) > currentValue) {
                            act ++;
                        }
                    }
                if (exp == act) {
                    this.lowPointsCoords.add(List.of(rowIndex, columnIndex));
                }
            }
        }
        var sum = 0;
        for (List<Integer> coords : this.lowPointsCoords) {
            sum += this.locations.get(coords.get(0)).get(coords.get(1)) + 1;
        }
        return sum;
    }

    public int part2() {
        List<Integer> basinSizes = new ArrayList<>();
        for (List<Integer> currentCoords : this.lowPointsCoords) {
            this.currentBasinCoords.add(new Integer[]{currentCoords.get(0), currentCoords.get(1)});
            this.process(currentCoords.get(0), currentCoords.get(1));
            basinSizes.add(this.currentBasinCoords.size());
            this.currentBasinCoords.clear();
        }
        basinSizes.sort(Collections.reverseOrder());
        return basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2);
    }


    private void process (Integer x, Integer y) {
        for (Integer[] possibleCoord : getSurroundingIndexes(x, y)) {
            alreadyInBasin: {
            if (coordsInBounds(possibleCoord) && this.locations.get(possibleCoord[0]).get(possibleCoord[1]) != 9) {
                for (Integer[] basinCoords : this.currentBasinCoords) {
                    if (Arrays.equals(basinCoords, possibleCoord)) {
                        break alreadyInBasin;
                    }
                }
                this.currentBasinCoords.add(possibleCoord);
                this.process(possibleCoord[0], possibleCoord[1]);
                }
            }
        }
    }


    private static Integer[][] getSurroundingIndexes(Integer x, Integer y) {
        return new Integer[][]{{x - 1, y}, {x + 1, y}, {x, y - 1}, {x, y + 1}};
    }

    private boolean coordsInBounds(Integer[] coords) {
        return (coords[0] >= 0 && coords[0] <= this.rowSize - 1 && coords[1] >= 0 && coords[1] <= this.columnSize - 1);
    }


    private void setData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day9/input.txt"));

        var lineIndex = 0;
        while (scanner.hasNextLine()) {
            this.locations.add(new ArrayList<>());
            for (String num : scanner.nextLine().split("")) {
                this.locations.get(lineIndex).add(Integer.parseInt(num));
            }
            lineIndex++;
        }
        this.rowSize = this.locations.size();
        this.columnSize = this.locations.get(0).size();
    }
}
