package org.example.Algorithm;

import org.example.Data.IDHistory;
import org.example.General;
import org.example.VKData.UserDB;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class IDUsersGenerate {
    public record Pair(int count, int index) implements Comparable<Pair> {
        @Override
        public int compareTo(Pair o) {
            return Integer.compare(count, o.count);
        }
    }

    public static int getMax(TreeMap<Integer, Integer> data) {
        Pair[] buffer = new Pair[data.size()];
        int index = 0;

        for (Map.Entry<Integer, Integer> entry : data.entrySet())
            buffer[index++] = new Pair(entry.getValue(), entry.getKey());

        Arrays.sort(buffer);
        return buffer[buffer.length - 1].index;
    }

    public static int getGenerateIdGenerate(int id, int indexGenerate, int index) {
        TreeSet<Integer> generate = General.generateIds[indexGenerate].get(id);
        if (generate == null) return -1;

        TreeMap<Integer, Integer> buffer = new TreeMap<>();
        for (int element : generate) {
            UserDB userDB = General.users.get(element);
            if (userDB == null) continue;
            if (userDB.idHistories == null) continue;
            if (userDB.idHistories[index] == null) continue;
            buffer.put(userDB.idHistories[index].data.getLast().id, buffer.getOrDefault(userDB.idHistories[index].data.getLast().id, 0) + 1);
        }

        if (buffer.isEmpty()) return -2;
        return getMax(buffer);
    }

    public static int getGenerateIdGenerate(int id, int indexGenerate, int index, long date) {
        TreeSet<Integer> generate = General.generateIds[indexGenerate].get(id);
        if (generate == null) return -1;

        TreeMap<Integer, Integer> buffer = new TreeMap<>();
        for (int element : generate) {
            UserDB userDB = General.users.get(element);
            if (userDB == null) continue;
            if (userDB.idHistories == null) continue;
            if (userDB.idHistories[index] == null) continue;

            IDHistory.Node node = userDB.idHistories[index].get(date);
            if (node == null) continue;
            buffer.put(node.id, buffer.getOrDefault(node.id, 0) + 1);
        }

        if (buffer.isEmpty()) return -2;
        return getMax(buffer);
    }

    public static int getGenerateId(int userId, int indexIds, int indexId) {
        UserDB userDB = General.users.get(userId);
        if (userDB == null) return -1;
        if (userDB.iDsHistories == null) return -2;
        if (userDB.iDsHistories[indexIds] == null) return -3;
        if (userDB.iDsHistories[indexIds].last.data == null) return -4;

        TreeMap<Integer, Integer> buffer = new TreeMap<>();
        for (int element : userDB.iDsHistories[indexIds].last.data) {
            UserDB user = General.users.get(element);
            if (user == null) continue;
            if (user.idHistories == null) continue;
            if (user.idHistories[indexId] == null) continue;
            buffer.put(user.idHistories[indexId].data.getLast().id, buffer.getOrDefault(user.idHistories[indexId].data.getLast().id, 0) + 1);
        }

        if (buffer.isEmpty()) return -5;
        return getMax(buffer);
    }

    public static int getGenerateId(int userId, int indexIds, int indexId, long date) {
        UserDB userDB = General.users.get(userId);
        if (userDB == null) return -1;
        if (userDB.iDsHistories == null) return -2;
        if (userDB.iDsHistories[indexIds] == null) return -3;
        if (userDB.iDsHistories[indexIds].last.data == null) return -4;

        TreeMap<Integer, Integer> buffer = new TreeMap<>();
        for (int element : userDB.iDsHistories[indexIds].last.data) {
            UserDB user = General.users.get(element);
            if (user == null) continue;
            if (user.idHistories == null) continue;
            if (user.idHistories[indexId] == null) continue;

            IDHistory.Node node = user.idHistories[indexId].get(date);
            buffer.put(node.id, buffer.getOrDefault(node.id, 0) + 1);
        }

        if (buffer.isEmpty()) return -5;
        return getMax(buffer);
    }

    /*
    public static int getGenerateIdGenerateLevel(int userId, int indexGenerate, int indexGenerateIn, int index, int level) {
        TreeSet<Integer> scanned = new TreeSet<>();
        TreeSet<Integer> newScan = new TreeSet<>();
        TreeMap<Integer, Integer> buffer = new TreeMap<>();

        newScan.add(userId);
        for (int a = 0; a < level; ++a) {
            for (int element : newScan) {
                int result = getGenerateIdGenerate(element, indexGenerate, index);
                if (result < 0) continue;

                buffer.put();
            }

            if (level - a > 1) {

            }
        }
    }

     */
}
