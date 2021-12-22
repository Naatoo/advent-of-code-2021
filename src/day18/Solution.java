package day18;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Solution {

    public static void main(String[] args) throws IOException {
        snailFish sF = new snailFish(readData());
        System.out.println("Part 1: " + sF.part1());
        System.out.println("Part 2: " + sF.part2());
    }

    private static List<String> readData() throws IOException {
        List<String> rawSnailNumbers = new ArrayList<>();
        Scanner scanner = new Scanner(Path.of("src/day18/input.txt"));
        while (scanner.hasNextLine()) {
            rawSnailNumbers.add(scanner.nextLine());
        }
        return rawSnailNumbers;
    }

}

class snailFish {

    private final List<String> rawSnailNumbers;

    public snailFish(List<String> rawSnailNumbers) {
        this.rawSnailNumbers = rawSnailNumbers;
    }

    private static int countMagnitude(List<Object> number) {
        Object leftVal = number.get(0);
        if (!(leftVal instanceof Integer)) {
            leftVal = countMagnitude((ArrayList) leftVal);
        }
        int leftResult = (int) leftVal * 3;

        Object rightVal = number.get(1);
        if (!(rightVal instanceof Integer)) {
            rightVal = countMagnitude((ArrayList) rightVal);
        }
        int rightResult = (int) rightVal * 2;

        return leftResult + rightResult;
    }

    private static String reduceSnailFishNumber(String number) {
        int maxDepth = 0;
        boolean isSplittable = true;
        checkMaxDepth:
        {
            for (int index = 0; index < number.length(); index++) {
                switch (number.charAt(index)) {
                    case '[':
                        maxDepth++;
                        if (maxDepth == 5) {
                            isSplittable = false;
                            break checkMaxDepth;
                        }
                        break;
                    case ']':
                        maxDepth--;
                        break;
                    default:
                        break;
                }
            }
        }

        int depth = 0;
        boolean exploded = false;
        boolean splitted = false;
        addition:
        {
            for (int index = 0; index < number.length(); index++) {
                switch (number.charAt(index)) {
                    case '[':
                        depth++;
                        break;
                    case ']':
                        depth--;
                        break;
                    case ',':
                        break;
                    default:
                        if (depth == 5) {
                            number = explodeNumber(number, index);

                            exploded = true;
                            break addition;
                        } else {
                            if (isSplittable && Character.isDigit(number.charAt(index + 1))) {
                                int value = Integer.parseInt(number.substring(index, index + 2));
                                if (value >= 10) {
                                    number = splitNumber(number, index, value);
                                    splitted = true;
                                    break addition;
                                }
                            }
                        }
                }

            }
        }
        if (exploded || splitted) {
            number = reduceSnailFishNumber(number);
        }

        return number;
    }

    private static String explodeNumber(String number, int index) {
        int endIndex = -1;
        for (int numberIndex = index; numberIndex < number.length(); numberIndex++) {
            if (number.charAt(numberIndex) == ']') {
                endIndex = numberIndex;
                break;
            }
        }
        String[] vals = number.substring(index, endIndex).split(",");
        int leftVal = Integer.parseInt(vals[0]);
        int rightVal = Integer.parseInt(vals[1]);
        String leftSide = explodeLeftSide(number.substring(0, index - 1), leftVal);
        String rightSide = explodeRightSide(number.substring(index + vals[0].length() + vals[1].length() + 2), rightVal);
        number = leftSide + "0" + rightSide;
        return number;
    }

    private static String splitNumber(String number, int index, int value) {
        int leftVal = value / 2;
        int rightVal = (int) Math.ceil(value / 2.0);
        String leftSide = number.substring(0, index);
        String rightSide = number.substring(index + 2);
        number = leftSide + "[" + leftVal + "," + rightVal + "]" + rightSide;
        return number;
    }

    private static String explodeLeftSide(String number, int value) {
        int signIndex = -1;
        int foundVal = -1;
        int indexStart = -1;
        String newSnailNumber;
        for (int index = number.length() - 1; index >= 0; index--) {
            if (Character.isDigit(number.charAt(index))) {
                StringBuilder foundStringVal = new StringBuilder(String.valueOf(number.charAt(index)));
                indexStart = index;
                for (int numIndex = index - 1; numIndex > 0; numIndex--) {
                    if (Character.isDigit(number.charAt(numIndex))) {
                        foundStringVal.insert(0, number.charAt(numIndex));
                        indexStart--;
                    } else {
                        break;
                    }
                }
                foundVal = Integer.parseInt(foundStringVal.toString());

                foundVal += value;
                signIndex = index;
                break;
            }
        }
        if (signIndex != -1) {
            newSnailNumber = number.substring(0, indexStart) + foundVal + number.substring(signIndex + 1);
        } else {
            newSnailNumber = number;
        }
        return newSnailNumber;

    }

    private static String explodeRightSide(String number, int value) {
        int signIndex = -1;
        int foundVal = -1;
        int indexEnd = -1;
        String newSnailNumber;
        for (int index = 0; index < number.length(); index++) {
            if (Character.isDigit(number.charAt(index))) {
                StringBuilder foundStringVal = new StringBuilder(String.valueOf(number.charAt(index)));
                indexEnd = index;
                for (int numIndex = index + 1; numIndex < number.length(); numIndex++) {
                    if (Character.isDigit(number.charAt(numIndex))) {
                        foundStringVal.append(number.charAt(numIndex));
                        indexEnd++;
                    } else {
                        break;
                    }
                }
                foundVal = Integer.parseInt(foundStringVal.toString());

                foundVal += value;
                signIndex = index;
                break;
            }
        }
        if (signIndex != -1) {
            newSnailNumber = number.substring(0, signIndex) + foundVal + number.substring(indexEnd + 1);
        } else {
            newSnailNumber = number;
        }
        return newSnailNumber;

    }

    private static List<Object> rawNumberToSnailfish(String numberString) {
        String[] splitted = splitNumber(numberString);
        String leftNumber = splitted[0];
        String rightNumber = splitted[1];
//        }
        List<Object> parsedNumber = new ArrayList<>();
        if (leftNumber.contains(",")) {
            List<Object> leftNumberNotSingle = rawNumberToSnailfish(leftNumber);
            parsedNumber.add(0, leftNumberNotSingle);
        } else {
            parsedNumber.add(0, Integer.parseInt(leftNumber));
        }

        if (rightNumber.contains(",")) {
            List<Object> leftNumberNotSingle = rawNumberToSnailfish(rightNumber);
            parsedNumber.add(parsedNumber.size(), leftNumberNotSingle);
        } else {
            parsedNumber.add(parsedNumber.size(), Integer.parseInt(rightNumber));
        }
        return parsedNumber;
    }

    private static String[] splitNumber(String rawNumber) {
        rawNumber = rawNumber.substring(0, rawNumber.length() - 1);
        rawNumber = rawNumber.substring(1);
        int splitindex = 123456;
        var opening = 0;
        var closing = 0;
        for (var index = 0; index < rawNumber.length(); index++) {
            char sign = rawNumber.charAt(index);
            if (sign == '[') {
                opening++;
            } else if (sign == ']') {
                closing++;
            } else if (sign == ',') {
                if (opening == closing) {
                    splitindex = index;
                    break;
                }
            }
        }
        return new String[]{rawNumber.substring(0, splitindex), rawNumber.substring(splitindex + 1)};
    }

    public int part1() {
        String number = this.rawSnailNumbers.get(0);
        for (String nextNumber : this.rawSnailNumbers.subList(1, this.rawSnailNumbers.size())) {
            String mergedNumber = String.join(",", number, nextNumber);
            mergedNumber = "[" + mergedNumber + "]";

            number = reduceSnailFishNumber(mergedNumber);
        }
        List<Object> snailFishNumber = rawNumberToSnailfish(number);
        return countMagnitude(snailFishNumber);
    }

    public int part2() {
        int maxMagnitude = 0;
        for (String number : this.rawSnailNumbers) {
            List<String> numbersCopy = new ArrayList<>(this.rawSnailNumbers);
            numbersCopy.remove(number);
            for (String secondNumber : numbersCopy) {
                List<String> pair = List.of(number, secondNumber);
                List<String> secondPair = new ArrayList<>(pair);
                Collections.reverse(secondPair);
                List<List<String>> possibilities = List.of(pair, secondPair);
                for (List<String> pos : possibilities) {
                    String mergedNumber = String.join(",", pos.get(0), pos.get(1));
                    mergedNumber = "[" + mergedNumber + "]";
                    String finalNumber = reduceSnailFishNumber(mergedNumber);
                    List<Object> snailFishNumber = rawNumberToSnailfish(finalNumber);
                    int sumMagnitude = countMagnitude(snailFishNumber);
                    if (sumMagnitude > maxMagnitude) {
                        maxMagnitude = sumMagnitude;
                    }
                }
            }
        }
        return maxMagnitude;
    }

}
