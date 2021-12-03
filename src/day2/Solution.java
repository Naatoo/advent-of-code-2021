package day2;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {

    public static void main (String[] args)  {
        try {
            List<String[]> data = getInput();
            System.out.println("Part1: " + part1(data));
            System.out.println("Part2: " + part2(data));
        }
        catch (IOException e) {
            System.out.println("Wrong input");
        }
    }

    private static List<String[]> getInput() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day2/input.txt"));
        List<String[]> data = new ArrayList<>();
        while (scanner.hasNext()) {
            data.add(scanner.nextLine().split(" "));
        }
        return data;
    }

    private static int part1(List<String[]> data) {
        int x = 0;
        int y = 0;
        for (String[] instruction : data) {
            int val = Integer.parseInt(instruction[1]);
            switch (instruction[0]) {
                case "forward":
                    x += val;
                    break;
                case "up":
                    y -= val;
                    break;
                case "down":
                    y += val;
                    break;
            }
        }
        return x * y;

    }

    private static int part2(List<String[]> data) {
        int x = 0;
        int y = 0;
        int aim = 0;
        for (String[] instruction : data) {
            int val = Integer.parseInt(instruction[1]);
            switch (instruction[0]) {
                case "forward":
                    y += aim * val;
                    x += val;
                    break;
                case "up":
                    aim -= val;
                    break;
                case "down":
                    aim += val;
                    break;
            }
        }
        return x * y;
    }
}
