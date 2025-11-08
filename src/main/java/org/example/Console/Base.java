package org.example.Console;

import org.example.General;

import java.io.DataInputStream;
import java.text.ParseException;
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

    public static int getPercent() {
        int buffer = 100;
        if (General.strings.get(General.indexString).charAt(0) == '-') {
            if (General.strings.get(General.indexString).length() == 1){
                System.out.println("Error length argument -");
                return -1;
            }

            if (General.strings.get(General.indexString).charAt(1) == '-') {
                if (General.strings.get(General.indexString).equals("percent")) {
                    try {
                        buffer = Integer.parseInt(General.strings.get(General.indexString + 1));
                    } catch (NumberFormatException e) {
                        System.out.println("Error percent");
                        return -3;
                    }
                } else {
                    System.out.println("Error not argument: " + General.strings.get(General.indexString));
                    return -2;
                }
            } else {
                if (General.strings.get(General.indexString).charAt(1) == 'p') {
                    General.indexString += 2;
                    try {
                        buffer = Integer.parseInt(General.strings.get(General.indexString + 1));
                    } catch (NumberFormatException e) {
                        System.out.println("Error percent");
                        return -4;
                    }
                } else {
                    System.out.println("Error not key: " + General.strings.get(General.indexString).charAt(1));
                    return -5;
                }
            } ++General.indexString;
        }

        if (buffer < 20 || buffer > 100) {
            System.out.println("Error percent < 20 or percent > 100");
            return -6;
        } return buffer;
    }

    public static Boolean getGenerate() {
        boolean buffer = false;
        if (General.strings.get(General.indexString).charAt(0) == '-') {
            if (General.strings.get(General.indexString).length() == 1) {
                System.out.println("Error length general argument -");
                return false;
            }

            if (General.strings.get(General.indexString).charAt(1) == '-') {
                if (General.strings.get(General.indexString).substring(2).equals("generate"))
                    buffer = true;
                else {
                    System.out.println("Error not argument of general:" + General.strings.get(General.indexString).substring(2));
                    return null;
                }
            } else {
                if (General.strings.get(General.indexString).charAt(1) == 'g') buffer = true;
                else {
                    System.out.println("Error not general key: " + General.strings.get(General.indexString).charAt(1));
                    return null;
                }
            } ++General.indexString;
        } return buffer;
    }

    public static Long getDate() {
        long buffer = 0;
        if (General.strings.get(General.indexString).charAt(0) == '-') {
            if (General.strings.get(General.indexString).length() == 1) {
                System.out.println("Error length argument -");
                return null;
            }

            if (General.strings.get(General.indexString).charAt(1) == '-') {
                if (General.strings.get(General.indexString).substring(2).equals("date")) {
                    try {
                        buffer = General.simpleDateFormat.parse(General.strings.get(General.indexString + 1)).getTime();
                    } catch (ParseException e) {
                        System.out.println("Error not date: " + General.strings.get(General.indexString + 1));
                        return null;
                    }
                } else {
                    System.out.println("Error not argument: " + General.strings.get(General.indexString).substring(2));
                    return null;
                }
            } else {
                if (General.strings.get(General.indexString).charAt(1) == 'd') {
                    try {
                        buffer = General.simpleDateFormat.parse(General.strings.get(General.indexString + 1)).getTime();
                    } catch (ParseException e) {
                        System.out.println("Error not date: " + General.strings.get(General.indexString + 1));
                        return null;
                    }
                } else {
                    System.out.println("Error not key: " + General.strings.get(General.indexString).charAt(1));
                    return null;
                }
            }
            General.indexString += 2;
        } return buffer;
    }
}
