package day8;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class Solution {

    public static void main(String[] args) {
        SevenSegmentSearch sSS = new SevenSegmentSearch();
        System.out.println("Part 1: " + sSS.part1());
        System.out.println("Part 2: " + sSS.part2());
    }
}

class SevenSegmentSearch {
    private final List<String[]> inputData = new ArrayList<>();
    private final List<String[]> outputData = new ArrayList<>();
    private final Map<Integer, Set<String>> segments = new HashMap<>();
    private final Map<List<Integer>, Integer> segmentsToNumbers = new HashMap<>(Map.of(
            List.of(1, 2, 3, 4, 5, 6), 0,
            List.of(2, 3), 1,
            List.of(1, 2, 4, 5, 7), 2,
            List.of(1, 2, 3, 4, 7), 3,
            List.of(2, 3, 6, 7), 4,
            List.of(1, 3, 4, 6, 7), 5,
            List.of(1, 3, 4, 5, 6, 7), 6,
            List.of(1, 2, 3), 7,
            List.of(1, 2, 3, 4, 5, 6, 7), 8,
            List.of(1, 2, 3, 4, 6, 7), 9
    ));

    public SevenSegmentSearch() {
        try {
            this.setData();
        } catch (IOException e) {
            System.out.println("Wrong input");
        }
        for (int index = 1; index <= 7; index ++) {
            this.segments.put(index, new TreeSet<>());
        }
    }

    public int part1() {
        int occurrences = 0;
        for ( String[] outputDigits : this.outputData ) {
            for ( String digit : outputDigits) {
                if ((digit.length() <= 4) || (digit.length() == 7)) {
                    System.out.println(digit);
                    occurrences ++;
                }
            }
        }
        return occurrences;
    }

    public int part2() {

        Iterator<String[]> input = this.inputData.iterator();
        Iterator<String[]> output = this.outputData.iterator();
        var outputSum = 0;
        while (output.hasNext() && input.hasNext()) {
            String[] outputEntry = output.next();
            String[] entry = Stream.
                    concat(Arrays.stream(outputEntry), Arrays.stream(input.next())).
                    toArray(String[]::new);
            Map<Integer, String> indexToCodeDigit = getDecodedEntry(entry);
            List<Integer> outputNumbers = this.getOutputNumbers(outputEntry, indexToCodeDigit);
            outputSum += getFinalOutputValue(outputNumbers);
            }

        return outputSum;
    }

    private static Integer getFinalOutputValue(List<Integer> outputNumbers) {
        StringBuilder numString = new StringBuilder();
        for ( Integer num : outputNumbers ) {
            numString.append(num.toString());
        }
        return Integer.parseInt(numString.toString());
    }

    private List<Integer> getOutputNumbers(String[] output, Map<Integer, String> indexToCodeDigit) {
        List<Integer> outputNumbers = new ArrayList<>();
        for ( String digitCode : output ) {
            List<Integer> outputDecoded = new ArrayList<>();
            for ( String letter : digitCode.split("")) {
                indexToCodeDigit.forEach((key, value) -> {
                    if (value.equals(letter)) {
                        outputDecoded.add(key);
                    }
                });
            }
            Collections.sort(outputDecoded);
            for ( Map.Entry<List<Integer>, Integer> segmentMapping : this.segmentsToNumbers.entrySet()) {
                if (outputDecoded.equals(segmentMapping.getKey())) {
                    outputNumbers.add(segmentMapping.getValue());
                    break;
                }
            }
        }
        return outputNumbers;
    }

    private static Map<Integer, String> getDecodedEntry(String[] entry) {
        Map<Integer, String> indexToCodeDigit = new HashMap<>();

        String digitOne = getDigitCodeOfLength(entry, 2, 1).get(0);
        String digitSeven = getDigitCodeOfLength(entry, 3, 1).get(0);
        indexToCodeDigit.put(1, getUniqueLetters(digitSeven, digitOne, 1).get(0));
        String digitFour = getDigitCodeOfLength(entry, 4, 1).get(0);
        List<String> digitFourUnique = getUniqueLetters(digitFour, digitOne, 2);
        List<String> digitZeroSixNine = getDigitCodeOfLength(entry, 6, 3);
        indexToCodeDigit.put(7, getUniqueLetterFromMany(digitZeroSixNine,
                digitFourUnique));
        indexToCodeDigit.put(6, getRemainingString(digitFourUnique,
                List.of(indexToCodeDigit.get(7)), 1).get(0));
        indexToCodeDigit.put(2, getUniqueLetterFromMany(digitZeroSixNine,
                new ArrayList<>(Arrays.asList(digitOne.split("")))));
        indexToCodeDigit.put(3, getRemainingString(Arrays.asList(digitOne.split("")),
                List.of(indexToCodeDigit.get(2)), 1).get(0));
        List<String> remainingLetters = getRemainingString(Arrays.asList("a", "b", "c", "d", "e", "f", "g"),
                new ArrayList<>(indexToCodeDigit.values()), 2);
        List<String> digitTwoThreeFive = getDigitCodeOfLength(entry, 6, 3);
        indexToCodeDigit.put(5, getUniqueLetterFromMany(digitTwoThreeFive,
                remainingLetters));
        indexToCodeDigit.put(4, getRemainingString(remainingLetters,
                List.of(indexToCodeDigit.get(5)), 1).get(0));
        return indexToCodeDigit;
    }

    private static List<String> getDigitCodeOfLength(String[] entry, int length, int expectedQuantity) {
        List<String> sameLengthDigitCodes = new ArrayList<>();
        for ( String digitCode : entry) {
            if (digitCode.length() == length) {
                sameLengthDigitCodes.add(digitCode);
            }
        }
        if (sameLengthDigitCodes.size() != expectedQuantity) {
            System.out.println("Quantity of digit codes with length=" + length + " are not equal to expeceted=" +
                    expectedQuantity + " entry=" + Arrays.toString(entry) + " found=" + sameLengthDigitCodes);
        }
        return sameLengthDigitCodes;
    }

    private static List<String> getUniqueLetters(String longer, String shorter, int expectedLength) {
        List<String> uniqueLetters = new ArrayList<>();
        for ( String letterFromLonger : longer.split("")) {
            if (!shorter.contains(letterFromLonger)) {
                uniqueLetters.add(letterFromLonger);
            }
        }
        if (uniqueLetters.size() != expectedLength) {
            System.out.println("Unique letter not found: longer=" + longer + " shorter=" + shorter);
        }
        return uniqueLetters;
    }

    private static String getUniqueLetterFromMany(List<String> examinatedDigitCodes, List<String> uniqueLetters) {
        String uniqueLetter = null;
        for ( String digitCode : examinatedDigitCodes ) {
            for ( String letter : uniqueLetters) {
                if (!digitCode.contains(letter)) {
                    uniqueLetter = letter;
                }
            }
        }
        if (uniqueLetter == null) {
            System.out.println("Unique letter from " + uniqueLetters.toString() + " in " + examinatedDigitCodes);
            uniqueLetter = "ZX";
        }
        return uniqueLetter;
    }

    private static List<String> getRemainingString(List<String> letterList, List<String> commonLettersList, int expectedQuantity) {
        ArrayList<String> foundLetters = new ArrayList<>();
        for ( String letter : letterList) {
            if (!commonLettersList.contains(letter)) {
                foundLetters.add(letter);
            }

        }
        if (foundLetters.size() != expectedQuantity) {
            System.out.println("Remaining letter quantity=" + expectedQuantity + " from " + letterList +
                    " not match, commonLetters=" + commonLettersList);
        }
        return foundLetters;
    }


    private void setData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day8/input.txt"));

        while (scanner.hasNextLine()) {
            String asd = scanner.nextLine();
            String[] dataline = asd.split(" \\| ");
            this.inputData.add(dataline[0].split(" "));
            this.outputData.add(dataline[1].split(" "));
        }
    }
}
