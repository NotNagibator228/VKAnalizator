package org.example.Algorithm;

import org.example.General;
import org.example.Utils;

import java.util.*;

public class Probability {
    public record Node (int percent, int index) implements Comparable<Node> {
        @Override
        public int compareTo(Node o) {
            return Integer.compare(percent, o.percent);
        }
    }

    public static ArrayList<Node> getProbabilityBase(TreeMap<Integer, TreeSet<Integer>> data, int count, int percent) {
        ArrayList<Node> buffer = new ArrayList<>();
        for (Map.Entry<Integer, TreeSet<Integer>> entry : data.entrySet()) {
            int result = 100 * entry.getValue().size() / count;
            if (result >= percent) buffer.add(new Node(result, entry.getKey()));
        }

        buffer.sort(Collections.reverseOrder());
        return buffer;
    }

    public static void probability(TreeMap<Integer, TreeSet<Integer>> buffer, TreeSet<Integer> set, int indexGenerateIn) {
        for (int element : set) {
            TreeSet<Integer> temp = General.generateIds[indexGenerateIn].get(element);
            if (temp == null) continue;
            for (int elem : temp) buffer.computeIfAbsent(elem, s -> new TreeSet<>()).add(element);
        }
    }

    public static ArrayList<Node> getProbabilityGenerate(int indexGenerate, int indexGenerateIn, int id, int percent) {
        TreeMap<Integer, TreeSet<Integer>> buffer = new TreeMap<>();
        TreeSet<Integer> set = General.generateIds[indexGenerate].get(id);
        if (set == null) return null;

        probability(buffer, set, indexGenerateIn);
        buffer.remove(id);
        return getProbabilityBase(buffer, set.size(), percent);
    }

    public static ArrayList<Node> getProbabilityGenerate(int indexGenerate, int indexGenerateIn, ArrayList<Integer> ids, int percent) {
        TreeMap<Integer, TreeSet<Integer>> buffer = new TreeMap<>();
        TreeSet<Integer> set = new TreeSet<>();

        for (int element : ids) {
            TreeSet<Integer> temp = General.generateIds[indexGenerate].get(element);
            if (temp != null) set.addAll(temp);
        } if (set.isEmpty()) return null;

        probability(buffer, set, indexGenerateIn);
        buffer.entrySet().removeIf(s -> Utils.binSearch(ids, s.getKey()));
        return getProbabilityBase(buffer, set.size(), percent);
    }
}
