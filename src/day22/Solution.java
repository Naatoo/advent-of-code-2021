package day22;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.Math.*;


public class Solution {

    public static void main(String[] args) throws IOException {
        ReactorReboot rR = new ReactorReboot();
        rR.run();
        System.out.println("Part 1: " + rR.getPart1());
        System.out.println("Part 2: " + rR.getPart2());
    }

}

class ReactorReboot {
    private final Set<List<String>> allCubes = new HashSet<>();
    private long part1 = 0;
    private long part2 = 0;


    public ReactorReboot() throws IOException {
        this.readData();
    }

    public void run() {
        for (List<String> cu : this.allCubes) {
            String state = cu.get(0);
            int x1 = Integer.parseInt(cu.get(1));
            int x2 = Integer.parseInt(cu.get(2));
            int y1 = Integer.parseInt(cu.get(3));
            int y2 = Integer.parseInt(cu.get(4));
            int z1 = Integer.parseInt(cu.get(5));
            int z2 = Integer.parseInt(cu.get(6));
            if (state.equals("on")) {
                for (int xDim = max(-50, x1); xDim < min(50, x2) + 1; xDim++) {
                    for (int yDim = max(-50, y1); yDim < min(50, y2) + 1; yDim++) {
                        for (int zDim = max(-50, z1); zDim < min(50, z2) + 1; zDim++) {
                            this.part1 += 1;
                        }
                    }
                }
                this.part2 += (long) abs(x2 - x1 + 1) * abs(y2 - y1 + 1) * abs(z2 - z1 + 1);
            }

        }
    }

    public long getPart1() {
        return part1;
    }

    public long getPart2() {
        return part2;
    }

    private void process(String state, Integer[][] rowData) {
        Integer x1 = rowData[0][0];
        Integer x2 = rowData[0][1];
        Integer y1 = rowData[1][0];
        Integer y2 = rowData[1][1];
        Integer z1 = rowData[2][0];
        Integer z2 = rowData[2][1];

        Set<List<String>> toadd = new HashSet<>();
        Set<List<String>> todel = new HashSet<>();

        var selfadded = false;

        for (List<String> cu : this.allCubes) {
            String s1 = cu.get(0);
            Integer xx1 = Integer.valueOf(cu.get(1));
            Integer xx2 = Integer.valueOf(cu.get(2));
            Integer yy1 = Integer.valueOf(cu.get(3));
            Integer yy2 = Integer.valueOf(cu.get(4));
            Integer zz1 = Integer.valueOf(cu.get(5));
            Integer zz2 = Integer.valueOf(cu.get(6));
            if (!(xx1 <= x2 && x1 <= xx2 && yy1 <= y2 && y1 <= yy2 && zz1 <= z2 && z1 <= zz2)) {
                continue;
            }
            var nx1 = Integer.valueOf(max(xx1, x1));
            var nx2 = Integer.valueOf(min(xx2, x2));

            var ny1 = Integer.valueOf(max(yy1, y1));
            var ny2 = Integer.valueOf(min(yy2, y2));

            var nz1 = Integer.valueOf(max(zz1, z1));
            var nz2 = Integer.valueOf(min(zz2, z2));

            todel.add(List.of(s1, xx1.toString(), xx2.toString(), yy1.toString(), yy2.toString(), zz1.toString(), zz2.toString()));
            selfadded = true;


            if (xx1 < nx1) {
                toadd.add(List.of(s1, xx1.toString(), String.valueOf(nx1 - 1), yy1.toString(), yy2.toString(), zz1.toString(), zz2.toString()));
            }
            if (nx2 < xx2) {
                toadd.add(List.of(s1, String.valueOf(nx2 + 1), xx2.toString(), yy1.toString(), yy2.toString(), zz1.toString(), zz2.toString()));
            }
            if (yy1 < ny1) {
                toadd.add(List.of(s1, nx1.toString(), nx2.toString(), yy1.toString(), String.valueOf(ny1 - 1), zz1.toString(), zz2.toString()));
            }
            if (ny2 < yy2) {
                toadd.add(List.of(s1, nx1.toString(), nx2.toString(), String.valueOf(ny2 + 1), yy2.toString(), zz1.toString(), zz2.toString()));
            }
            if (zz1 < nz1) {
                toadd.add(List.of(s1, nx1.toString(), nx2.toString(), ny1.toString(), ny2.toString(), zz1.toString(), String.valueOf(nz1 - 1)));
            }
            if (nz2 < zz2) {
                toadd.add(List.of(s1, nx1.toString(), nx2.toString(), ny1.toString(), ny2.toString(), String.valueOf(nz2 + 1), zz2.toString()));
            }
            toadd.add(List.of(state, String.valueOf(min(nx1, x1)), String.valueOf(max(nx2, x2)), String.valueOf(min(ny1, y1)),
                    String.valueOf(max(ny2, y2)), String.valueOf(min(nz1, z1)), String.valueOf(max(nz2, z2))));
        }

        this.allCubes.removeAll(todel);
        this.allCubes.addAll(toadd);

        if (!(selfadded)) {
            this.allCubes.add(List.of(state, x1.toString(), x2.toString(), y1.toString(), y2.toString(), z1.toString(), z2.toString()));
        }
    }


    private void readData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day22/input.txt"));
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" ");
            String state = line[0];
            Integer[][] rowData = new Integer[3][2];
            var index = 0;
            for (String coords : line[1].split(",")) {
                String[] vals = coords.split("=")[1].split("\\.\\.");
                Integer[] coordsVals = Stream.of(vals).map(Integer::valueOf).toArray(Integer[]::new);
                rowData[index] = coordsVals;
                index++;
            }
            this.process(state, rowData);
        }
    }
}