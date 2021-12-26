package day19;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Solution {

    public static void main(String[] args) throws IOException {
        BeaconScanner bS = new BeaconScanner(readData());
        System.out.println("Part 1: " + bS.part1());
        System.out.println("Part 2: " + bS.part2());
    }

    private static List<List<int[]>> readData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day19/input.txt"));
        List<List<int[]>> allScanners = new ArrayList<>();
        List<int[]> scannerPoints = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(",")) {
                scannerPoints.add(Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray());
            } else if (line.contains("---")) {
                scannerPoints = new ArrayList<>();
            } else {
                allScanners.add(scannerPoints);
            }
        }
        return allScanners;
    }

}

class BeaconScanner {

    private final List<List<int[]>> beacons;
    private final List<List<List<int[]>>> rotatedBeacons;
    private final List<Integer> used = new ArrayList<>();
    private final List<int[]> scannerCoords = new ArrayList<>();
    private List<int[]> foundPoints = new ArrayList<>();

    public BeaconScanner(List<List<int[]>> beacons) {
        this.beacons = beacons;
        this.rotatedBeacons = this.getRotatedBeacons();
    }

    private static int[] getNewPoint(int[] first, int[] second) {
        return new int[]{second[0] - first[0], second[1] - first[1], second[2] - first[2]};
    }

    private List<List<List<int[]>>> getRotatedBeacons() {
        List<List<List<int[]>>> rotatedBeacons = new ArrayList<>();
        this.used.add(0);
        for (List<int[]> beacon : this.beacons) {
            List<List<int[]>> singleBeacon = new ArrayList<>();
            for (int index = 0; index < 24; index++) {
                List<int[]> asd = new ArrayList<>();
                singleBeacon.add(asd);
            }
            for (int[] rawPoints : beacon) {
                singleBeacon.get(0).add(new int[]{+rawPoints[0], +rawPoints[1], +rawPoints[2]});
                singleBeacon.get(1).add(new int[]{+rawPoints[0], -rawPoints[2], +rawPoints[1]});
                singleBeacon.get(2).add(new int[]{+rawPoints[0], -rawPoints[1], -rawPoints[2]});
                singleBeacon.get(3).add(new int[]{+rawPoints[0], +rawPoints[2], -rawPoints[1]});
                singleBeacon.get(4).add(new int[]{-rawPoints[0], -rawPoints[1], +rawPoints[2]});
                singleBeacon.get(5).add(new int[]{-rawPoints[0], +rawPoints[2], +rawPoints[1]});
                singleBeacon.get(6).add(new int[]{-rawPoints[0], +rawPoints[1], -rawPoints[2]});
                singleBeacon.get(7).add(new int[]{-rawPoints[0], -rawPoints[2], -rawPoints[1]});
                singleBeacon.get(8).add(new int[]{+rawPoints[1], +rawPoints[2], +rawPoints[0]});
                singleBeacon.get(9).add(new int[]{+rawPoints[1], -rawPoints[0], +rawPoints[2]});
                singleBeacon.get(10).add(new int[]{+rawPoints[1], -rawPoints[2], -rawPoints[0]});
                singleBeacon.get(11).add(new int[]{+rawPoints[1], +rawPoints[0], -rawPoints[2]});
                singleBeacon.get(12).add(new int[]{-rawPoints[1], -rawPoints[2], +rawPoints[0]});
                singleBeacon.get(13).add(new int[]{-rawPoints[1], +rawPoints[0], +rawPoints[2]});
                singleBeacon.get(14).add(new int[]{-rawPoints[1], +rawPoints[2], -rawPoints[0]});
                singleBeacon.get(15).add(new int[]{-rawPoints[1], -rawPoints[0], -rawPoints[2]});
                singleBeacon.get(16).add(new int[]{+rawPoints[2], +rawPoints[0], +rawPoints[1]});
                singleBeacon.get(17).add(new int[]{+rawPoints[2], -rawPoints[1], +rawPoints[0]});
                singleBeacon.get(18).add(new int[]{+rawPoints[2], -rawPoints[0], -rawPoints[1]});
                singleBeacon.get(19).add(new int[]{+rawPoints[2], +rawPoints[1], -rawPoints[0]});
                singleBeacon.get(20).add(new int[]{-rawPoints[2], -rawPoints[0], +rawPoints[1]});
                singleBeacon.get(21).add(new int[]{-rawPoints[2], +rawPoints[1], +rawPoints[0]});
                singleBeacon.get(22).add(new int[]{-rawPoints[2], +rawPoints[0], -rawPoints[1]});
                singleBeacon.get(23).add(new int[]{-rawPoints[2], -rawPoints[1], -rawPoints[0]});
            }
            rotatedBeacons.add(singleBeacon);

        }
        return rotatedBeacons;
    }

    public int part1() {
        this.foundPoints = this.beacons.get(0);
        while (this.used.size() < this.beacons.size()) {
            search:
            {
                for (int index = 0; index < this.beacons.size(); index++) {
                    if (this.used.contains(index)) {
                        continue;
                    }
                    List<List<int[]>> rotatedBeaconsForScanner = this.rotatedBeacons.get(index);
                    for (List<int[]> orientationPoints : rotatedBeaconsForScanner) {
                        for (int[] originalPoint : this.foundPoints) {
                            for (int[] rotatedPoint : orientationPoints) {
                                int[] diff = getNewPoint(originalPoint, rotatedPoint);
                                List<int[]> samePoints = new ArrayList<>();
                                for (int[] rotatedPointExtra : orientationPoints) {
                                    int[] possiblePoint = getNewPoint(diff, rotatedPointExtra);
                                    for (int[] originalPointToCheck : this.foundPoints) {
                                        if (Arrays.equals(originalPointToCheck, possiblePoint)) {
                                            samePoints.add(possiblePoint);
                                            break;
                                        }
                                    }
                                    if (samePoints.size() == 12) {
                                        addFoundPoints(orientationPoints, diff);
                                        this.used.add(index);
                                        this.scannerCoords.add(Arrays.stream(diff).map(coord -> -coord).toArray());
                                        break search;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return this.foundPoints.size();
    }

    private void addFoundPoints(List<int[]> scannerPointsOrientation, int[] diff) {
        for (int[] point : scannerPointsOrientation) {
            int[] finalPointToAdd = getNewPoint(diff, point);
            if (!this.listContainsArray(this.foundPoints, finalPointToAdd)) {
                this.foundPoints.add(finalPointToAdd);
            }
        }
    }


    private boolean listContainsArray(List<int[]> list, int[] points) {
        for (int[] currentPoint : list) {
            if (Arrays.equals(currentPoint, points)) {
                return true;
            }
        }
        return false;
    }


    public int part2() {
        return countManhattanDistance();
    }

    private int countManhattanDistance() {
        int max = 0;
        this.scannerCoords.forEach(array -> System.out.println(Arrays.toString(array)));
        for (int[] coords : this.scannerCoords) {
            List<int[]> copiedCoords = new ArrayList<>(this.scannerCoords);
            copiedCoords.remove(coords);
            for (int[] secondsCoords : copiedCoords) {
                int manhattanDistance =
                        Math.abs(secondsCoords[0] - coords[0]) +
                                Math.abs(secondsCoords[1] - coords[1]) +
                                Math.abs(secondsCoords[2] - coords[2]);
                if (manhattanDistance > max) {
                    max = manhattanDistance;
                }
            }

        }
        return max;
    }
}

