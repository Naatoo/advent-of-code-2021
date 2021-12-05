package day5;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

public class Solution {

    public static void main(String[] args) {
        HydrothermalVenturePart1 hV = new HydrothermalVenturePart1();
        System.out.println("Part 1: " + hV.processPart(1));
        System.out.println("Part 2: " + hV.processPart(2));
    }
}

class HydrothermalVenturePart1 {
    private List<Vent> data = new ArrayList<>();
    private final static Integer minOverlap = 2;

    public HydrothermalVenturePart1() {
        try {
            this.setData();
        } catch (IOException e) {
            System.out.println("Wrong input");
        }
    }

    public int processPart(int part) {
        Map<List<Integer>, Integer> mapOccur = this.markCovered(part);
        return countMoreThanTwo(mapOccur);
    }


    private void setData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day5/input.txt"));
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" -> ");
            this.data.add(new Vent(line));
        }
    }


    private Map<List<Integer>, Integer> markCovered(int part) {
        Map<List<Integer>, Integer> mapOccur = new HashMap<>();
        for (Vent vent : this.data) {
            List<Integer[]> coveredPoints = vent.findCoveredPoints(part);
            for ( Integer[] point : coveredPoints) {
                List<Integer> pointList = Arrays.asList(point);
                if (mapOccur.containsKey(pointList)) {
                    mapOccur.put(pointList, mapOccur.get(pointList) + 1);
                }
                else {
                    mapOccur.put(pointList, 1);
                }
            }
        }
        return mapOccur;
    }

    private static int countMoreThanTwo(Map<List<Integer>, Integer> mapOccur) {
        List<Integer> occurs = new ArrayList<>(mapOccur.values());
        var found = 0;
        for ( Integer num : occurs ) {
            if (num >= minOverlap) {
                found ++;
            }
        }
        return found;
    }
}

class Vent {

    private int[] xDelta = new int[2];
    private int[] yDelta = new int[2];

    public Vent(String[] line) {
        for (int i = 0; i < 2; i++) {
            String[] point = line[i].split(",");
            this.xDelta[i] = Integer.parseInt(point[0]);
            this.yDelta[i] = Integer.parseInt(point[1]);
        }
    }

    public List<Integer[]> findCoveredPoints(int part) {
        List<Integer[]> coveredPoints = new ArrayList<>();
        boolean isNotVerticalHorizontal = this.xDelta[0] != this.xDelta[1] && this.yDelta[0] != this.yDelta[1];
        switch (part) {
            case (1):
                if (isNotVerticalHorizontal) {
                    coveredPoints = new ArrayList<>();
                } else {
                    coveredPoints = this.getPointsRange("horizontal_vertical");
                }
                break;
            case (2):
                if (isNotVerticalHorizontal) {
                    coveredPoints = this.getPointsRange("diagonal");
                } else {
                    coveredPoints = this.getPointsRange("horizontal_vertical");
                }
                break;
        }
        return coveredPoints;
    }

    private List<Integer[]> getPointsRange(String rangeType) {
        List<Integer[]> coveredPoints;
        int[] xPoints = getRangeForDelta(this.xDelta);
        int[] yPoints = getRangeForDelta(this.yDelta);
        if (rangeType.equals("diagonal")) {
            coveredPoints = getPointsRangeDiagonal(xPoints, yPoints);
        }
        else {
            coveredPoints = getPointsHorizontalVertical(xPoints, yPoints);
        }
        return coveredPoints;
    }



    private static List<Integer[]> getPointsHorizontalVertical(int[] xPoints, int[] yPoints) {
        List<Integer[]> coveredPoints = new ArrayList<>();
        for ( int x : xPoints ) {
            for ( int y : yPoints) {
                Integer[] xy = {x, y};
                coveredPoints.add(xy);
            }
        }
        return coveredPoints;
    }

    private static List<Integer[]> getPointsRangeDiagonal(int[] xPoints, int[] yPoints) {
        List<Integer[]> coveredPoints = new ArrayList<>();
        for (int index = 0; index < xPoints.length; index ++) {
            Integer[] xy = {xPoints[index], yPoints[index]};
            coveredPoints.add(xy);
    }
    return coveredPoints;
    }

    private static int[] getRangeForDelta(int[] delta) {
        boolean reverse = delta[0] > delta[1];
        Arrays.sort(delta);
        int[] range = IntStream.rangeClosed(delta[0], delta[1]).toArray();
        if (reverse) {
            range = reverseRange(range);
        }
        return range;
    }

    private static int[] reverseRange(int[] array) {
        int rangeLength = array.length;
        int[] reversedRange = new int[rangeLength];
        for (int index = rangeLength - 1; index >= 0; index--) {
            reversedRange[rangeLength - index - 1] = array[index];
        }
        return reversedRange;
    }

}