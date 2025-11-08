package org.example.Console.DB;

import org.example.Colors;

import java.util.ArrayList;

public class Base <T> {
    public final long date;
    public ArrayList<T> data;

    public Base(long date, ArrayList<T> data) {
        this.date = date;
        this.data = data;
    }

    public void outNoDate() { }
    public void outDate() { }

    public void out() {
        System.out.println(Colors.ANSI_BLUE + "Items count: " + data.size() + Colors.ANSI_RESET);
        if (date == 0) outNoDate();
        else outDate();
    }
}
