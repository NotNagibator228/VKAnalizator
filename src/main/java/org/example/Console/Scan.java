package org.example.Console;

import org.example.Clients.ScanClasses;
import org.example.Clients.VKToken;
import org.example.General;

import java.util.ArrayList;

public class Scan {
    public static class Node {
        public int type = 0;
        public ArrayList<VKToken> tokens;
        public int rerty = 1;
        public long rertyTime = 60000;
        int level;

        public Node(int type, ArrayList<VKToken> tokens, int rerty, long rertyTime, int level) {
            this.type = type;
            this.tokens = tokens;
            this.rerty = rerty;
            this.rertyTime = rertyTime;
            this.level = level;
        }
    }

    public static Node getScan() {
        int rerty = 1;
        long rertyTime = 60000;
        ArrayList<VKToken> tokens = null;
        int level = 1;

        if (General.strings.size() - General.indexString > 2) {
            if (General.strings.get(General.indexString).charAt(0) == '-') {
                if (General.strings.get(General.indexString).length() == 1) {
                    System.out.println("Error not length argument -");
                    return null;
                }

                if (General.strings.size() - General.indexString < 2) {
                    System.out.println("Error not arguments element");
                    return null;
                }

                if (General.strings.get(General.indexString).charAt(1) == '-') {
                    ++General.indexString;
                    switch (General.strings.get(General.indexString - 1).substring(2)) {
                        case "rerty" -> {
                            try {
                                rerty = Integer.parseInt(General.strings.get(General.indexString));
                            } catch (NumberFormatException e) {
                                System.out.println("Error unconvert rertyCount: " + General.strings.get(General.indexString));
                                return null;
                            }
                        }
                        case "level" -> {
                            try {
                                level = Integer.parseInt(General.strings.get(General.indexString));
                            } catch (NumberFormatException e) {
                                System.out.println("Error unconvert level: " + General.strings.get(General.indexString));
                                return null;
                            }
                        }
                        case "rertyTime" -> {
                            try {
                                rertyTime = Long.parseLong(General.strings.get(General.indexString));
                            } catch (NumberFormatException e) {
                                System.out.println("Error unconvert rertyTime: " + General.strings.get(General.indexString));
                                return null;
                            }
                        }
                        case "tokens" -> {
                            ArrayList<Integer> ids = Base.getIntegers();
                            if (ids == null) {
                                System.out.println("Error not tokens ids");
                                return null;
                            } if (!Base.isAcceessTokenIndices(ids)) return null;

                            tokens = new ArrayList<>();
                            --General.indexString;

                            for (int element : ids)
                                tokens.add(General.vkTokens.data.get(element));
                        }
                        default -> {
                            System.out.println("Error not argument: " + General.strings.get(General.indexString - 1).substring(2));
                            return null;
                        }
                    }
                } else {
                    for (char element : General.strings.get(General.indexString).substring(1).toCharArray()) {
                        if (General.strings.size() - General.indexString < 3) {
                            System.out.println("Error not arguments element");
                            return null;
                        } ++General.indexString;
                        switch (element) {
                            case 'r' -> {
                                try {
                                    rerty = Integer.parseInt(General.strings.get(General.indexString));
                                } catch (NumberFormatException e) {
                                    System.out.println("Error unconvert rertyCount: " + General.strings.get(General.indexString));
                                    return null;
                                }
                            }
                            case 't' -> {
                                try {
                                    rertyTime = Long.parseLong(General.strings.get(General.indexString));
                                } catch (NumberFormatException e) {
                                    System.out.println("Error unconvert rertyTime: " + General.strings.get(General.indexString));
                                    return null;
                                }
                            }
                            case 'T' -> {
                                ArrayList<Integer> ids = Base.getIntegers();
                                if (ids == null) {
                                    System.out.println("Error not tokens ids");
                                    return null;
                                } if (!Base.isAcceessTokenIndices(ids)) return null;

                                tokens = new ArrayList<>();
                                --General.indexString;

                                for (int index : ids)
                                    tokens.add(General.vkTokens.data.get(index));
                            }
                            case 'l' -> {
                                try {
                                    level = Integer.parseInt(General.strings.get(General.indexString));
                                } catch (NumberFormatException e) {
                                    System.out.println("Error uncovert level: " + General.strings.get(General.indexString));
                                    return null;
                                }
                            }
                        }
                    }
                } ++General.indexString;
            }
        }

        int type = switch (General.strings.get(General.indexString)) {
            case "friends" -> 0;
            default -> -1;
        };

        if (type == -1) {
            System.out.println("Error not scan type: " + General.strings.get(General.indexString));
            return null;
        } ++General.indexString;

        if (rerty < 1) {
            System.out.println("Error rerty < 1");
            return null;
        }

        if (rertyTime < 200) {
            System.out.println("Error rertyTime < 200 ms");
            return null;
        }

        if (level < 1) {
            System.out.println("Error level < 1");
            return null;
        }

        if (tokens == null) tokens = new ArrayList<>(General.vkTokens.data);
        return new Node(type, tokens, rerty, rertyTime, level);
    }

    public static boolean run() throws InterruptedException {
        if (General.vkTokens.data.isEmpty()) {
            System.out.println("Error vkTokens is empty");
            return false;
        }

        ArrayList<Node> buffer = new ArrayList<>();
        ArrayList<ScanClasses.ScanBase> scan = new ArrayList<>();
        ++General.indexString;

        while (General.strings.size() - General.indexString > 3) {
            if (General.strings.get(General.indexString).equals("{")) {
                ++General.indexString;

                System.out.println("yes0");

                Node node = getScan();
                if (node == null) return false;

                ArrayList<Integer> temp = Base.getIntegers();
                if (temp == null) return false;

                if (General.strings.get(General.indexString).equals("}")) {
                    ++General.indexString;
                    scan.add(
                            switch (node.type) {
                                case 0 -> new ScanClasses.ScanFriends(temp, node.tokens, node.rerty, node.rertyTime, node.level);
                                default -> throw new IllegalStateException("Unexpected value: " + node.type);
                            }
                    );
                } else return false;
            } else break;
        }

        if (scan.isEmpty()) {
            System.out.println("Error scan is Empty");
            return false;
        }

        for (ScanClasses.ScanBase element : scan)
            element.start();

        for (ScanClasses.ScanBase element : scan)
            element.join();
        return true;
    }
}
