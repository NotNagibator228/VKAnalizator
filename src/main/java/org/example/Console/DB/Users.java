package org.example.Console.DB;

import org.example.Data.IDHistory;
import org.example.General;
import org.example.Utils;
import org.example.VKData.UserDB;

import java.util.ArrayList;
import java.util.TreeSet;

public class Users extends IDsBase {
    public Users(ArrayList<Integer> data, long date) {
        super(data, date);
    }

    protected ArrayList<Integer> filterIdBuffer(int index, int id) {
        ArrayList<Integer> buffer = new ArrayList<>();
        if (date == 0) {
            for (int element : this.data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;

                if (userDB.idHistories[index].data.getLast().id == id)
                    buffer.add(element);
            }
        } else {
            for (int element : this.data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;

                IDHistory.Node node = userDB.idHistories[index].get(date);
                if (node == null) continue;
                if (node.id == id) buffer.add(element);
            }
        } return buffer;
    }

    public void filterId(int index, int id) {
        this.data = filterIdBuffer(index, id);
        if (this.data.isEmpty()) this.data = null;
    }

    public void filterIdRemove(int index, int id) {
        this.data.removeAll(filterIdBuffer(index, id));
        if (this.data.isEmpty()) this.data = null;
    }

    protected ArrayList<Integer> filterNameFull(int index, String string) {
        int ind = General.userStrings[index].search(string);
        if (ind < 0) {
            data = null;
            return null;
        }

        ArrayList<Integer> scan = General.userStrings[index].nodes.get(index).indices;
        ArrayList<Integer> buffer = new ArrayList<>();

        if (date == 0) {
            for (int element : data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;
                if (Utils.binSearch(scan, element)) buffer.add(element);
            }
        } else {
            for (int element : data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;

                IDHistory.Node node = userDB.idHistories[index].get(date);
                if (Utils.binSearch(scan, node.id)) buffer.add(element);
            }
        } return buffer;
    }

    protected ArrayList<Integer> filterNamePercent(int index, String string, int percent) throws InterruptedException {
        TreeSet<Integer> temp = new TreeSet<>();
        if (date == 0) {
            for (int element : data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;
                temp.add(userDB.idHistories[index].data.getLast().id);
            }
        } else {
            for (int element : data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;

                IDHistory.Node node = userDB.idHistories[index].get(date);
                if (node == null) continue;
                temp.add(node.id);
            }
        }

        ArrayList<Integer> scanned = General.userStrings[index].search(string, percent, 4, new ArrayList<>(temp));
        ArrayList<Integer> buffer = new ArrayList<>();
        temp.clear();

        for (int element : scanned)
            temp.addAll(General.userStrings[index].nodes.get(element).indices);

        if (date == 0) {
            for (int element : data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;
                if (temp.contains(userDB.idHistories[index].data.getLast().id))
                    buffer.add(element);
            }
        } else {
            for (int element : data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;

                IDHistory.Node node = userDB.idHistories[index].get(date);
                if (node == null) continue;
                if (temp.contains(node.id)) buffer.add(element);
            }
        } return buffer;
    }

    public void filterName(int index, String string, int percent) throws InterruptedException {
        data = ((percent == 10) ? filterNameFull(index, string) : filterNamePercent(index, string, percent));
        if (data == null || data.isEmpty()) data = null;
    }
}
