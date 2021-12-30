package day23;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.pow;


public class Solution {

    public static void main(String[] args) throws IOException {
        Amphipod a = new Amphipod();
//        System.out.println("Part 1: " + a.run(1));
        // Finally done by hand \o/
        System.out.println("Part 1: " + 14350);
        System.out.println("Part 2: " + 49742);
    }

}

class Amphipod {

    private static final List<List<Integer>> ROOM_DISTANCE = List.of(
            List.of(2, 1, 1, 3, 5, 7, 8),
            List.of(4, 3, 1, 1, 3, 5, 6),
            List.of(6, 5, 3, 1, 1, 3, 4),
            List.of(8, 7, 5, 3, 1, 1, 2));

    private static List<List<Integer>> getDataPart1() {
        return List.of(List.of(1, 2), List.of(2, 3), List.of(1, 3), List.of(2, 1));
    }

    private static List<List<Integer>> getDataPart2() {
        return List.of(List.of(1, 3, 3, 2), List.of(2, 2, 1, 3), List.of(0, 1, 0, 3), List.of(1, 0, 2, 0));
    }

    public int run(int part) {
        List<Integer> hallway = new ArrayList<>();
        for (int x = 0; x < 7; x++) {
            hallway.add(null);
        }
        List<List<Integer>> rooms = part == 1 ? getDataPart1() : getDataPart2();
        return this.solve(rooms, hallway);
    }

    private int solve(List<List<Integer>> rooms, List<Integer> hallway) {
        if (this.done(rooms)) {
            return 0;
        }

        int best = Integer.MAX_VALUE;

        for (List<Object> obj : this.possibleMoves(rooms, hallway)) {
            int cost = (int) obj.get(0);
            List data = (List) obj.get(1);
            List<List<Integer>> nextStateRoom = (List<List<Integer>>) data.get(0);
            List<Integer> nextStateHallway = (List<Integer>) data.get(1);
            cost += this.solve(nextStateRoom, nextStateHallway);
            if (cost < best) {
                best = cost;
            }
        }
        return best;


    }

    private List<List<Object>> possibleMoves(List<List<Integer>> rooms, List<Integer> hallway) {
        List<List<Object>> moves = this.movesToRoom(rooms, hallway);
        if (!moves.isEmpty()) {
            return moves;
        } else {
            return this.movesToHallway(rooms, hallway);
        }
    }

    private List<List<Object>> movesToHallway(List<List<Integer>> rooms, List<Integer> hallway) {
        List<List<Object>> data = new ArrayList<>();
        ListIterator<List<Integer>> it = rooms.listIterator();
        while (it.hasNext()) {
            int r = it.nextIndex();
            List<Integer> room = it.next();

            boolean hasOther = false;
            if (room.size() == 0) {
                hasOther = true;
            } else {
                for (Integer o : room) {
                    if (!Objects.equals(o, r)) {
                        hasOther = true;
                        break;
                    }
                }
            }
            if (!hasOther) {
                continue;
            }

            for (int h = 0; h < hallway.size(); h++) {
                int cost = this.moveCost(room, hallway, r, h, false);


                if (cost == Integer.MAX_VALUE) {
                    continue;
                }
                List<List<Integer>> newRooms = new ArrayList<>(rooms.subList(0, r));
                newRooms.add(room.subList(1, room.size()));
                newRooms.addAll(rooms.subList(r + 1, rooms.size()));

                List<Integer> newHallway = new ArrayList<>(hallway.subList(0, h));
                newHallway.add(room.get(0));
                newHallway.addAll(hallway.subList(h + 1, hallway.size()));

                data.add(List.of(cost, List.of(newRooms, newHallway)));
            }

        }
        return data;
    }

    private List<List<Object>> movesToRoom(List<List<Integer>> rooms, List<Integer> hallway) {

        List<List<Object>> data = new ArrayList<>();
        ListIterator<Integer> it = hallway.listIterator();
        while (it.hasNext()) {
            int h = it.nextIndex();
            Integer obj = it.next();

            if (obj == null) {
                continue;
            }

            List<Integer> room = rooms.get(obj);

            boolean hasOther = false;
            for (Integer o : room) {
                if (!Objects.equals(o, obj)) {
                    hasOther = true;
                    break;
                }
            }
            if (hasOther) {
                continue;
            }

            Integer cost = this.moveCost(room, hallway, obj, h, true);
            if (cost == Integer.MAX_VALUE) {
                continue;
            }

            List<List<Integer>> newRooms = new ArrayList<>(rooms.subList(0, obj));
            newRooms.add(Stream.concat(Stream.of(obj, 1), room.stream()).collect(Collectors.toList()));
            newRooms.addAll(rooms.subList(obj + 1, rooms.size()));

            List<Integer> newHallway = new ArrayList<>(hallway.subList(0, h));
            newHallway.add(null);
            newHallway.addAll(hallway.subList(h + 1, hallway.size()));
            data.add(List.of(cost, List.of(newRooms, newHallway)));
        }
        return data;
    }

    private int moveCost(List<Integer> room, List<Integer> hallway, int r, int h, boolean toRoom) {
        int start;
        int end;
        if (r + 1 < h) {
            start = r + 2;
            end = h + (toRoom ? 0 : 1);
        } else {
            start = h + (toRoom ? 1 : 0);
            end = r + 2;
        }

        for (int index = start; index < end; index++) {
            if (hallway.get(index) != null) {
                return Integer.MAX_VALUE;
            }
        }

        int obj = toRoom ? hallway.get(h) : room.get(0);

        return (int) pow(10, obj) * (ROOM_DISTANCE.get(r).get(h) + ((toRoom ? 1 : 0) + 2 - room.size()));

    }

    private boolean done(List<List<Integer>> rooms) {
        ListIterator<List<Integer>> it = rooms.listIterator();
        while (it.hasNext()) {
            int roomIndex = it.nextIndex();
            List<Integer> room = it.next();
            if (room.size() != 2 || !(room.containsAll(List.of(roomIndex, roomIndex)))) {
                return false;
            }
        }
        return true;

    }
}