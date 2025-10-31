package org.example.Data;

import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class StringHistory {
    public static class Node {
        public final String lower;
        public final String upper;
        public final ArrayList<Integer> indices = new ArrayList<>();

        public Node(String lower) {
            this.lower = lower;
            this.upper = lower.toUpperCase();
        }
    }

    public ArrayList<Node> nodes = new ArrayList<>();
    public TreeMap<Integer, ArrayList<Integer>> map = new TreeMap<>();
    public ArrayList<String> strings = new ArrayList<>();
    public ArrayList<Integer> backIndices = new ArrayList<>();

    public void clear() {
        nodes.clear();
        map.clear();
        strings.clear();
        backIndices.clear();
    }

    public int add(String string) {
        String lower = string.toLowerCase();
        ArrayList<Integer> indices = map.computeIfAbsent(lower.hashCode(), s -> new ArrayList<>());

        for (int index : indices) {
            for (int element : nodes.get(index).indices)
                if (strings.get(element).equals(string))
                    return element;

            int buffer = strings.size();
            strings.add(string);
            backIndices.add(index);
            nodes.get(index).indices.add(buffer);
            return buffer;
        }

        indices.add(nodes.size());
        backIndices.add(nodes.size());
        nodes.add(new Node(lower));
        nodes.getLast().indices.add(strings.size());
        int buffer = strings.size();
        strings.add(string);
        return buffer;
    }

    public int[] load(DataInputStream dataInputStream, int count) throws IOException {
        int[] buffer = new int[count];
        for (int index = 0; index < count; ++index)
            buffer[index] = add(dataInputStream.readUTF());
        return buffer;
    }

    public void save(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(strings.size());
        for (String element : strings)
            dataOutputStream.writeUTF(element);
    }

    public int search(String string) {
        ArrayList<Integer> indices = map.get(string.toLowerCase().hashCode());
        if (indices == null) return -1;

        for (int index : indices)
            for (int element : nodes.get(index).indices)
                if (strings.get(element).equals(string))
                    return element;
        return -2;
    }

    public ArrayList<Integer> searchFuzzy(String string, int percent) {
        ArrayList<Integer> buffer = new ArrayList<>();
        String upper = string.toUpperCase();

        for (int index = 0; index < nodes.size(); ++index) {
            int result = FuzzySearch.partialRatio(upper, nodes.get(index).upper);
            if (result >= percent) buffer.add(index);
        } return buffer;
    }

    private class SearchThread extends Thread {
        public String upper;
        public int percent;
        public ArrayList<Integer> buffer = new ArrayList<>();
        int begin, end;

        public SearchThread(String upper, int percent, int begin, int end) {
            this.upper = upper;
            this.percent = percent;
            this.begin = begin;
            this.end = end;
        }

        @Override
        public void run() {
            for (int index = begin; index < end; ++index) {
                int result = FuzzySearch.partialRatio(upper, nodes.get(index).upper);
                if (result >= percent) buffer.add(index);
            }
        }
    }

    public ArrayList<Integer> search(String string, int percent, int thread) throws InterruptedException {
        ArrayList<Integer> buffer = new ArrayList<>();
        thread = Math.min(nodes.size(), thread);
        int length = nodes.size() / thread;
        int fix = nodes.size() % thread;
        String upper = string.toUpperCase();
        int begin = 0;
        int end = 0;

        SearchThread[] searchThreads = new SearchThread[thread];
        for (int index = 0; index < thread; ++index) {
            end += length;
            if (index < fix) end++;
            searchThreads[index] = new SearchThread(upper, percent, begin, end);
            searchThreads[index].start();
            begin = end;
        }

        for (SearchThread element : searchThreads) {
            element.join();
            buffer.addAll(element.buffer);
        } return buffer;
    }
}
