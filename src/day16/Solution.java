package day16;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


public class Solution {

    public static void main(String[] args) throws IOException {
        PacketDecoder pD = new PacketDecoder(readData());
        pD.run();
        System.out.println("Part 1: " + pD.part1());
        System.out.println("Part 2: " + pD.part2());
    }

    private static String readData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day16/input.txt"));
        return scanner.nextLine();
    }

}

class PacketDecoder {

    private final List<String> binaryMessage;
    private int versionSum = 0;
    private long packetSum;

    public PacketDecoder(String rawData) {
        String hex = rawData;
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        this.binaryMessage = Arrays.asList(hex.split(""));
    }

    private static long binListToDec(List<String> data) {
        return Long.parseLong(String.join("", data), 2);
    }

    public void run() {
        this.packetSum = this.processMessage(0)[1];
    }

    public int part1() {
        return this.versionSum;
    }

    public long part2() {
        return this.packetSum;
    }

    private long[] processMessage(int index) {
        long[] answer;
        long subpacketValue;
        long packetVersion = binListToDec(this.binaryMessage.subList(index, index + 3));
        this.versionSum += packetVersion;
        long packetType = binListToDec(this.binaryMessage.subList(index + 3, index + 6));
        if (packetType == 4) {
            answer = this.processLiteralType(index + 6);
        } else {
            answer = this.processOperatorType(index + 6, packetType);
        }
        index = (int) answer[0];
        subpacketValue = answer[1];
        return new long[]{(long) index, subpacketValue};
    }

    private long[] processLiteralType(int startIndex) {
        var prefix = -1;
        var index = startIndex;
        List<String> number = new ArrayList<>();
        while (prefix != 0) {
            prefix = Integer.parseInt(this.binaryMessage.get(index));
            number.add(String.join("", this.binaryMessage.subList(index + 1, index + 5)));
            index += 5;
        }
        long packetValue = binListToDec(number);
        return new long[]{index, packetValue};
    }

    private long[] processOperatorType(int startIndex, long packetType) {
        long[] answer;
        if (Integer.parseInt(this.binaryMessage.get(startIndex)) == 0) {
            answer = this.processOperatorTotalLength(startIndex + 1, packetType);
        } else {
            answer = this.processOperatorNumberSubPackets(startIndex + 1, packetType);
        }
        return answer;
    }

    private long[] processOperatorTotalLength(int startIndex, long packetType) {
        long[] answer;
        long subValue;
        var startSubPacketsIndex = startIndex + 15;
        List<String> totalLengthBinary = this.binaryMessage.subList(startIndex, startSubPacketsIndex);
        var totalLengthDecimal = binListToDec(totalLengthBinary);
        var index = startSubPacketsIndex;
        List<Long> subpacketValues = new ArrayList<>();
        while (index != startSubPacketsIndex + totalLengthDecimal) {
            answer = this.processMessage(index);
            index = (int) answer[0];
            subValue = answer[1];
            subpacketValues.add(subValue);
        }
        long finalVal = getPacketValue(subpacketValues, (int) packetType);
        return new long[]{index, finalVal};
    }

    private long[] processOperatorNumberSubPackets(int startIndex, long packetType) {
        long[] answer;
        long subValue;
        var startSubPacketsIndex = startIndex + 11;
        List<String> subpacketsNumberBinary = this.binaryMessage.subList(startIndex, startSubPacketsIndex);
        var subpacketsNumberDecimal = binListToDec(subpacketsNumberBinary);
        var index = startSubPacketsIndex;
        List<Long> subpacketValues = new ArrayList<>();
        for (int subpacketIndex = 0; subpacketIndex < subpacketsNumberDecimal; subpacketIndex++) {
            answer = this.processMessage(index);
            index = (int) answer[0];
            subValue = answer[1];
            subpacketValues.add(subValue);
        }
        long finalVal = getPacketValue(subpacketValues, (int) packetType);
        return new long[]{index, finalVal};
    }

    private static long getPacketValue(List<Long> subpacketValues, int packetType) {
        long packetValue = -1;
        switch (packetType) {
            case 0:
                packetValue = subpacketValues.stream().mapToLong(Long::longValue).sum();
                break;
            case 1:
                packetValue = multiply(subpacketValues);
                break;
            case 2:
                packetValue = Collections.min(subpacketValues);
                break;
            case 3:
                packetValue = Collections.max(subpacketValues);
                break;
            case 5:
                packetValue = subpacketValues.get(0) > subpacketValues.get(1) ? 1 : 0;
                break;
            case 6:
                packetValue = subpacketValues.get(0) < subpacketValues.get(1) ? 1 : 0;
                break;
            case 7:
                packetValue = subpacketValues.get(0).equals(subpacketValues.get(1)) ? 1 : 0;
                break;
        }
        return packetValue;
    }



    private static long multiply (List<Long> data) {
        long result;
        if (data.size() == 1) {
            result = data.get(0);
        } else {
            result = 1;
            for (long val : data) {
                result *= val;
            }
        }
        return result;
    }

}

