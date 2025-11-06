package org.example;

import java.util.ArrayList;

public class Utils {
    public static boolean binSearch(ArrayList<Integer> data, int element) {
        int begin = 0, end = data.size();
        while (begin < end) {
            int index = begin + ((end - begin) >> 1);
            int mid = data.get(index);

            if (element < mid) end = index;
            else if (element > mid) begin = index + 1;
            else return true;
        } return false;
    }

    public static int binSearchIndexOf(ArrayList<Integer> data, int element) {
        int begin = 0, end = data.size();
        while (begin < end) {
            int index = begin + ((end - begin) >> 1);
            int mid = data.get(index);

            if (element < mid) end = index;
            else if (element > mid) begin = index + 1;
            else return index;
        } return -1;
    }

    public static boolean isInteger(String string) {
        for (char element : string.toCharArray())
            if (!Character.isDigit(element)) return false;
        return true;
    }

    public static boolean isBDate(String str) {
        int num = 0, count = 0;
        for (char element : str.toCharArray()) {
            if (!Character.isDigit(element)) {
                if (num == 0) return false;
                num = 0; ++count;
            } else num = (num * 10) + (int)(element - '0');
        } return num != 0 && count >= 1 && count <= 2;
    }

    public static int addBDate(String str) {
        int[] dates = new int[3];
        int index = 0, result = 0;

        for (char element : str.toCharArray()) {
            if (Character.isDigit(element)) dates[index] = (dates[index] * 10) + (int)(element - '0');
            else ++index;
        } dates[1] -= 1;

        if (str.length() > 6) {
            if (dates[2] > 1999) {
                result += 36524;
                if (dates[2] > 2000) ++result;
            } result += dates[0];

            dates[2] %= 100;
            result += dates[2];
            result += dates[2] % 4;

            if (dates[2] % 4 == 0) {
                if (dates[1] > 1) ++result;
                if (dates[2] == 0) --result;
            } result += General.monthCode[dates[1]];
        } else result = -(dates[1] * 100) - dates[0];
        return result;
    }

    public static String arrayToString(int[] data) {
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < data.length - 1; ++index)
            buffer.append(data[index]).append(',');
        buffer.append(data[data.length - 1]);
        return buffer.toString();
    }
}
