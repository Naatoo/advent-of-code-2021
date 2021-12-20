package day17;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


public class Solution {

    public static void main(String[] args) throws IOException {
        TrickShot tS = new TrickShot(readData());
        tS.run();
        System.out.println("Part 1: " + tS.part1());
        System.out.println("Part 2: " + tS.part2());
    }

    private static int[][] readData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day17/input.txt"));
        String[] elems = scanner.nextLine().split(" ");
        int[][] target = new int[2][2];
        for (int coordIndex = 0; coordIndex < 2; coordIndex ++) {
            String[] values = elems[coordIndex + 2].split(",")[0].split("=")[1].split("\\.\\.");
            for (int boundIndex = 0; boundIndex < 2; boundIndex ++) {
                target[coordIndex][boundIndex] = Integer.parseInt(values[boundIndex]);
            }
        }
        return target;
    }

}

class TrickShot {

    private final int[][] targetBounds;
    private int sumApproved = 0;
    private int totalMaxHeight = 0;

    public TrickShot(int[][] target) {
        this.targetBounds = target;
    }

    public void run() {
        for (int x = 1; x < 1000; x ++) {
            for (int y = -1000; y < 1000; y++ ) {
                int maxY = 0;
                int[] currentVelocity = new int[]{x, y};
                int[] totalVelocity = new int[]{x, y};
                while(currentVelocity[0] <= this.targetBounds[0][1] && currentVelocity[1] >= this.targetBounds[1][0]) {
                    if (totalVelocity[1] <= this.targetBounds[1][1] && totalVelocity[1] >= this.targetBounds[1][0]
                            && totalVelocity[0] >= this.targetBounds[0][0] && totalVelocity[0] <= this.targetBounds[0][1]) {
                        this.sumApproved ++;
                        if (maxY > this.totalMaxHeight) {
                            this.totalMaxHeight = maxY;
                        }
                        break;
                    }
                    currentVelocity = getNextVelocity(currentVelocity);
                    totalVelocity[0] += currentVelocity[0];
                    totalVelocity[1] += currentVelocity[1];
                    if (totalVelocity[1] > maxY) {
                        maxY = totalVelocity[1];
                    }
                }
            }
        }
    }

    public int part1() {
        return this.totalMaxHeight;
    }

    public int part2() {
        return this.sumApproved;
    }

    private static int[] getNextVelocity (int[] velocity) {
        int[] nextVelocity = velocity.clone();
        if (nextVelocity[0] > 0) {
            nextVelocity[0] --;
        }
        nextVelocity[1]--;
        return nextVelocity;
    }
}

