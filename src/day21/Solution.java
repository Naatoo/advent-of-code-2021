package day21;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


public class Solution {

    public static void main(String[] args) throws IOException {
        System.out.println("Part 1: " + new DiracDice(readData()).part1());
        System.out.println("Part 2: " + new DiracDice(readData()).part2());
    }

    private static int[] readData() throws IOException {
        Scanner scanner = new Scanner(Path.of("src/day21/input.txt"));
        int[] startingPos = new int[2];
        for (int index = 0; index < 2; index++) {
            startingPos[index] = Integer.parseInt(scanner.nextLine().split(" ")[4]);
        }
        return startingPos;
    }

}

class DiracDice {
    private static final Map<String, long[]> cachedUniverses = new HashMap<>();
    private final int[] playersPos;
    private final int[] playersScore = new int[]{0, 0};
    private final List<Integer> possibilities = new ArrayList<>();
    private int diceValue = 0;
    private int roundIndex = 0;

    public DiracDice(int[] pos) {
        this.playersPos = pos;
    }

    private int processRoundPart1() {
        int playerWon = -1;
        for (int playerIndex = 0; playerIndex < 2; playerIndex++) {
            int pos = this.playersPos[playerIndex];
            int forwardMoves = this.countForwardMoves();
            int newPos = this.countPlayerNewPos(pos, forwardMoves);
            this.roundIndex += 3;
            this.playersPos[playerIndex] = newPos;
            this.playersScore[playerIndex] += newPos;

            int pointsToWin = 1000;
            if (this.playersScore[playerIndex] >= pointsToWin) {
                playerWon = playerIndex;
                break;
            }
        }
        return playerWon;
    }

    private int countForwardMoves() {
        int sum = 0;
        for (var index = 0; index < 3; index++) {
            if (this.diceValue + 1 == 101) {
                this.diceValue = 1;
            } else {
                this.diceValue++;
            }
            sum += this.diceValue;
        }
        return sum;
    }

    private int countPlayerNewPos(int pos, int forwardMoves) {
        int maxSpace = 10;
        int parsedMoves = forwardMoves % maxSpace;
        int newPos;
        if (pos + parsedMoves > maxSpace) {
            newPos = parsedMoves - (maxSpace - pos);
        } else {
            newPos = pos + parsedMoves;
        }
        return newPos;
    }

    public int part1() {
        int playerWon = -1;
        while (playerWon == -1) {
            playerWon = this.processRoundPart1();
        }
        return this.roundIndex * this.playersScore[playerWon == 0 ? 1 : 0];
    }

    private long[] processPart2(int pos, int score, int opponentPos, int opponentScore) {

        int part2Limit = 21;
        if (score >= part2Limit) {
            return new long[]{1, 0};
        } else if (opponentScore >= part2Limit) {
            return new long[]{0, 1};
        }

        String cacheKey = String.format("%d-%d-%d-%d", pos, score, opponentPos, opponentScore);

        if (cachedUniverses.containsKey(cacheKey)) {
            return cachedUniverses.get(cacheKey);
        }
        long wins = 0;
        long opponentWins = 0;

        for (int possibleValue : this.possibilities) {
            int newPos = this.countPlayerNewPos(pos, possibleValue);
            int newScore = score + newPos;
            long[] winsValues = this.processPart2(opponentPos, opponentScore, newPos, newScore);
            wins += winsValues[1];
            opponentWins += winsValues[0];
        }
        long[] result = new long[]{wins, opponentWins};
        cachedUniverses.put(cacheKey, result);
        return result;
    }

    public long part2() {
        this.setPossibilities();
        long[] res = this.processPart2(this.playersPos[0], 0, this.playersPos[1], 0);
        return Math.max(res[0], res[1]);
    }


    private void setPossibilities() {
        for (int a = 1; a <= 3; a++) {
            for (int b = 1; b <= 3; b++) {
                for (int c = 1; c <= 3; c++) {
                    this.possibilities.add(a + b + c);
                }
            }
        }
    }
}
