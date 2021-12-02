package day1;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {

    public static List<Integer> getInput() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day1/input.txt"));
        List<Integer> data = new ArrayList<>();
        while (scanner.hasNext()) {
            data.add(Integer.parseInt(scanner.nextLine()));
        }
        return data;
    }

    public static int part1(List<Integer> data) {
        Integer prev = data.get(0);
        int increased = 0;
        for (Integer val : data) {
            if (val > prev) {
                increased ++;
            }
            prev = val;
        }
        return increased;
    }

    public static int part2(List<Integer> data) {
        int sumSize = 3;
        int prevSubListSum = 0;
        int increasedSum = 0;
        for (int index = 0; index < data.size() - sumSize + 1; index++) {
            List<Integer> subList = data.subList(index, index + sumSize);
            int subListSum = subList.stream().mapToInt(Integer::intValue).sum();
            if (subListSum > prevSubListSum && prevSubListSum != 0) {
                increasedSum++;
            }
            prevSubListSum = subListSum;
        }
        return increasedSum;
    }

    public static void main(String[] args)  {
        try {
            List<Integer> data = getInput();
            System.out.println("Part1: " + part1(data));
            System.out.println("Part2: " + part2(data));
        }
        catch (IOException e) {
            System.out.println("Wrong input");
        }
    }
}
