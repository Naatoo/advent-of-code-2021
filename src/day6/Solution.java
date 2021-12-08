package day6;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Solution {

    public static void main(String[] args) {
        LanternfishProblem lP = new LanternfishProblem();
        System.out.println("Part 1: " + lP.countFishPart1(80));
        System.out.println("Part 2: " + lP.countFishPart2(256));

    }
}

class LanternfishProblem {
    private final List<Integer> data = new ArrayList<>();

    public LanternfishProblem() {
        try {
            this.setData();
        } catch (IOException e) {
            System.out.println("Wrong input");
        }
    }

    public int countFishPart1(int totalDays) {
        List<Integer> part1 = new ArrayList<>(this.data);
        for (int day = 1; day <= totalDays; day++) {
            List<Integer> newDayFish = new ArrayList<>();
            for (int fishIndex = 0; fishIndex < part1.size(); fishIndex ++) {
                int currentValue = part1.get(fishIndex);
                int newValue;
                if (currentValue == 0) {
                    newValue = 6;
                    newDayFish.add(8);
                } else {
                    newValue = currentValue - 1;
                }
                part1.set(fishIndex, newValue);
            }
            part1 = Stream.concat(part1.stream(), newDayFish.stream()).collect(Collectors.toList());
        }
        return part1.size();
    }

    public BigInteger countFishPart2(int totalDays) {
        BigInteger[] fishInStates = new BigInteger[9];
        for (int fish = 0; fish < 9; fish ++) {
            fishInStates[fish] = new BigInteger("0");
        }
        for (int fish = 0; fish < this.data.size(); fish ++) {
            fishInStates[this.data.get(fish)] = fishInStates[this.data.get(fish)].add(new BigInteger("1"));
        }

        for (int day = 1; day <= totalDays; day++) {
            var newFish = fishInStates[0];
            fishInStates[0] = new BigInteger("0");
            for (int stateIndex = 1; stateIndex < 9; stateIndex++) {
                fishInStates[stateIndex - 1] = fishInStates[stateIndex - 1].
                        add(new BigInteger(fishInStates[stateIndex].toString()));
                fishInStates[stateIndex] = fishInStates[stateIndex].
                        subtract(new BigInteger(fishInStates[stateIndex].toString()));
            }
            fishInStates[8] = new BigInteger(newFish.toString());
            fishInStates[6] = fishInStates[6].add(new BigInteger(newFish.toString()));
        }
        BigInteger sum = new BigInteger("0");
        for (int stateIndex = 0; stateIndex < 9; stateIndex ++) {
            sum = sum.add(new BigInteger(fishInStates[stateIndex].toString()));
        }
        return sum;
    }

    private void setData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day6/input.txt"));
        String[] line = scanner.nextLine().split(",");
        for (String num : line) {
            this.data.add(Integer.parseInt(num));
        }
    }
}
