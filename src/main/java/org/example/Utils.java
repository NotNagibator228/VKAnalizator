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
}
