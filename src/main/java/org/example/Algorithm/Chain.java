package org.example.Algorithm;

import org.example.Colors;
import org.example.Enum.GenerateIDsEnum;
import org.example.Enum.UserIDsEnum;
import org.example.General;
import org.example.Utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Chain {
    public static class Base {
        public record Node(int id, int[] array) { }

        public class DB {
            public TreeSet<Integer> done = new TreeSet<>();
            public ArrayList<TreeMap<Integer, TreeSet<Integer>>> data = new ArrayList<>();
            public TreeSet<Integer> scanned = new TreeSet<>();

            public DB() {
                data.add(new TreeMap<>());
            }
        }

        public ArrayList<Integer> in;
        public ArrayList<Integer> to;
        public Node[][] data;
        public long date;

        public DB genNoDate() { return null; }
        public DB genDate() { return null; }

        public Base(ArrayList<Integer> in, ArrayList<Integer> to, long date) {
            this.in = in;
            this.to = to;
            this.date = date;
            DB data = ((date == 0) ? genNoDate() : genDate());

            if (!data.done.isEmpty()) {
                in = new ArrayList<>(data.done);
                generate(data.data);
            }
        }

        public void generate(ArrayList<TreeMap<Integer, TreeSet<Integer>>> data) {
            data.getLast().entrySet().removeIf(s -> !Utils.binSearch(to, s.getKey()));
            if (data.getLast().isEmpty()) return;

            for (int index = data.size() - 2; index > -1; --index) {
                TreeMap<Integer, TreeSet<Integer>> back = data.get(index + 1);
                TreeMap<Integer, TreeSet<Integer>> db = data.get(index);
                TreeSet<Integer> set = new TreeSet<>();

                for (Map.Entry<Integer, TreeSet<Integer>> entry : back.entrySet())
                    set.addAll(entry.getValue());
                db.entrySet().removeIf(s -> !set.contains(s.getKey()));
            }

            for (int index = data.size() - 2; index > -1; --index) {
                TreeMap<Integer, TreeSet<Integer>> back = data.get(index + 1);
                TreeMap<Integer, TreeSet<Integer>> db = data.get(index);

                for (Map.Entry<Integer, TreeSet<Integer>> entry : back.entrySet())
                    entry.getValue().removeIf(s -> !db.containsKey(s) && !Utils.binSearch(in, entry.getKey()));
            } this.data = new Node[data.size()][];

            for (int index = data.size() - 2; index > -1; --index) {
                TreeMap<Integer, TreeSet<Integer>> back = data.get(index + 1);
                TreeMap<Integer, TreeSet<Integer>> db = data.get(index);
                ArrayList<Integer> temp = new ArrayList<>(db.keySet());
                this.data[index + 1] = new Node[back.size()];
                int ind = 0;

                for (Map.Entry<Integer, TreeSet<Integer>> entry : back.entrySet()) {
                    ArrayList<Integer> temp2 = new ArrayList<>();
                    for (int element : entry.getValue()) {
                        int result = Utils.binSearchIndexOf(temp, element);
                        if (result != -1) temp2.add(result);
                    } this.data[index + 1][ind++] = new Node(entry.getKey(), temp2.stream().mapToInt(Integer::intValue).toArray());
                }
            }

            TreeSet<Integer> temp = new TreeSet<>();
            for (Map.Entry<Integer, TreeSet<Integer>> entry : data.getFirst().entrySet())
                temp.addAll(entry.getValue());

            this.data[0] = new Node[data.getFirst().size()];
            in = new ArrayList<>(temp);
            int index = 0;

            for (Map.Entry<Integer, TreeSet<Integer>> entry : data.getFirst().entrySet()) {
                ArrayList<Integer> temp2 = new ArrayList<>();
                for (int element : entry.getValue()) {
                    int result = Utils.binSearchIndexOf(in, element);
                    if (result != -1) temp2.add(result);
                } this.data[0][index++] = new Node(entry.getKey(), temp2.stream().mapToInt(Integer::intValue).toArray());
            }
        }

        public void out() {
            System.out.println("Chain in: ");
            if (date == 0) {
                for (int index = 0; index < in.size(); ++index)
                    System.out.println("\t" + Integer.toString(index) + "\t:" + Integer.toString(in.get(index)) + ':' + General.users.get(in.get(index)));

                for (int a = 0; a < data.length; ++a) {
                    System.out.println(Colors.ANSI_BLUE + "Level: " + Integer.toString(a) + Colors.ANSI_RESET);
                    for (int b = 0; b < data[a].length; ++b) {
                        System.out.println("\t" + Integer.toString(b) + "\t:" + Integer.toString(data[a][b].id) + ':' + General.users.get(data[a][b].id));
                        System.out.println("\t" + Integer.toString(b) + "\t:" + Colors.ANSI_RED + Utils.arrayToString(data[a][b].array) + Colors.ANSI_RESET);
                    }
                }
            } else {
                for (int index = 0; index < in.size(); ++index)
                    System.out.println("\t" + Integer.toString(index) + "\t:" + Integer.toString(in.get(index)) + ':' + ((General.users.get(in.get(index)) != null) ? General.users.get(in.get(index)).toString(date) : General.users.get(in.get(index))));

                for (int a = 0; a < data.length; ++a) {
                    System.out.println(Colors.ANSI_BLUE + "Level: " + Integer.toString(a) + Colors.ANSI_RESET);
                    for (int b = 0; b < data[a].length; ++b) {
                        System.out.println("\t" + Integer.toString(b) + "\t:" + Integer.toString(data[a][b].id) + ':' + ((General.users.get(in.get(data[a][b].id)) != null) ? General.users.get(in.get(data[a][b].id)).toString(date) : General.users.get(in.get(data[a][b].id))));
                        System.out.println("\t" + Integer.toString(b) + "\t:" + Colors.ANSI_RED + Utils.arrayToString(data[a][b].array) + Colors.ANSI_RESET);
                    }
                }
            }
        }
    }

    public static class ChainGenerate extends Base {
        @Override
        public DB genNoDate() {
            DB buffer = new DB();
            for (int element : in) {
                TreeSet<Integer> temp = General.generateIds[GenerateIDsEnum.FRIENDS.ordinal()].get(element);
                if (temp == null) continue;

                for (int id : temp) {
                    buffer.data.getFirst().computeIfAbsent(id, s -> new TreeSet<>()).add(element);
                    if (Utils.binSearch(to, id)) buffer.done.add(id);
                    buffer.scanned.add(id);
                }
            }

            while (true) {
                TreeMap<Integer, TreeSet<Integer>> map = new TreeMap<>();
                for (Map.Entry<Integer, TreeSet<Integer>> entry : buffer.data.getLast().entrySet()) {
                    TreeSet<Integer> temp = General.generateIds[GenerateIDsEnum.FRIENDS.ordinal()].get(entry.getKey());
                    if (temp == null) continue;

                    for (int element : temp) {
                        if (!buffer.scanned.add(element)) continue;
                        map.computeIfAbsent(element, s -> new TreeSet<>()).add(entry.getKey());
                        if (Utils.binSearch(to, element)) buffer.done.add(element);
                    }
                }

                if (!map.isEmpty()) buffer.data.add(map);
                else break;
                if (buffer.done.size() == to.size()) break;
            } return buffer;
        }

        @Override
        public DB genDate() {
            DB buffer = new DB();
            for (int element : in) {
                TreeSet<Integer> temp = Generate.getGenerateUserIds(element, GenerateIDsEnum.FRIENDS.ordinal(), UserIDsEnum.FRIENDS.ordinal(), date);
                if (temp == null) continue;

                for (int id : temp) {
                    buffer.data.getFirst().computeIfAbsent(id, s -> new TreeSet<>()).add(element);
                    if (Utils.binSearch(to, id)) buffer.done.add(id);
                    buffer.scanned.add(id);
                }
            }

            while (true) {
                TreeMap<Integer, TreeSet<Integer>> map = new TreeMap<>();
                for (Map.Entry<Integer, TreeSet<Integer>> entry : buffer.data.getLast().entrySet()) {
                    TreeSet<Integer> temp = Generate.getGenerateUserIds(entry.getKey(), GenerateIDsEnum.FRIENDS.ordinal(), UserIDsEnum.FRIENDS.ordinal(), date);
                    if (temp == null) continue;

                    for (int element : temp) {
                        if (!buffer.scanned.add(element)) continue;
                        map.computeIfAbsent(element, s -> new TreeSet<>()).add(entry.getKey());
                        if (Utils.binSearch(to, element)) buffer.done.add(element);
                    }
                }

                if (!map.isEmpty()) buffer.data.add(map);
                else break;
                if (buffer.done.size() == to.size()) break;
            } return buffer;
        }

        public ChainGenerate(ArrayList<Integer> in, ArrayList<Integer> to, long date) {
            super(in, to, date);
        }
    }
}
