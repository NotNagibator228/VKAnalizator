package org.example.Console;

import org.example.General;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class Base {
    public static ArrayList<Integer> getIntegers() {
        TreeSet<Integer> set = new TreeSet<>();
        while (General.indexString < General.strings.size()) {
            try {
                set.add(Integer.parseInt(General.strings.get(General.indexString)));
            } catch (NumberFormatException e) { break; }

            ++General.indexString;
            if (General.strings.size() - General.indexString > 1 && General.strings.get(General.indexString).equals(",")) ++General.indexString;
        }

        if (set.isEmpty()) {
            System.out.println("Error not integers");
            return null;
        } return new ArrayList<>(set);
    }

    public static ArrayList<String> getStrings() {
        HashSet<String> buffer = new HashSet<>();
        while (General.indexString < General.strings.size()) {
            buffer.add(General.strings.get(General.indexString));
            ++General.indexString;
            if (General.strings.size() - General.indexString > 1) {
                if (General.strings.get(General.indexString).equals(",")) {
                    ++General.indexString;
                    continue;
                } else break;
            }
        }

        if (buffer.isEmpty()) {
            System.out.println("Error not integers");
            return null;
        } return new ArrayList<>(buffer);
    }

    public static boolean isAcceessTokenIndices(ArrayList<Integer> data) {
        return data.getFirst() > -1 || data.getLast() < General.vkTokens.data.size();
    }

    public record Names(String first, String last) { }

    public static ArrayList<Names> getNames() {
        ArrayList<Names> buffer = new ArrayList<>();
        while (General.strings.size() - General.indexString > 2) {
            if (General.strings.get(General.indexString).equals(">") || General.strings.get(General.indexString).equals("&")) break;
            buffer.add(new Names(General.strings.get(General.indexString), General.strings.get(General.indexString + 1)));
            General.indexString += 2;

            if (General.strings.size() - General.indexString > 2 && General.strings.get(General.indexString).equals(",")) ++General.indexString;
            else break;
        }

        if (buffer.isEmpty()) {
            System.out.println("Error not names");
            return null;
        } return buffer;
    }
}
