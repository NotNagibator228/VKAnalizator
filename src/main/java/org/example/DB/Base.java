package org.example.DB;

import org.example.Colors;

import java.util.ArrayList;
import java.util.TreeSet;

public class Base <T> {
    public final long date;
    public ArrayList<T> data;

    public Base(long date, ArrayList<T> data) {
        this.date = date;
        this.data = data;
    }

    public void outNoDate() throws InterruptedException { }
    public void outDate() throws InterruptedException { }

    public void out() throws InterruptedException {
        System.out.println(Colors.ANSI_BLUE + "Items count: " + data.size() + Colors.ANSI_RESET);
        if (date == 0) outNoDate();
        else outDate();
    }

    public Base<T> copy() {
        return new Base<T>(date, new ArrayList<T>(data));
    }

    public void append(TreeSet<T> elements) {
        elements.addAll(this.data);
        this.data = new ArrayList<>(elements);
    }
}
