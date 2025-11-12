package org.example.Console;

import org.example.Algorithm.Chains;
import org.example.Algorithm.Generate;
import org.example.Clients.ScanClasses;
import org.example.Colors;
import org.example.DB.Groups;
import org.example.DB.IDsBase;
import org.example.DB.Users;
import org.example.Enum.GenerateIDsEnum;
import org.example.Enum.UserIDEnum;
import org.example.Enum.UserIDsEnum;
import org.example.General;
import org.example.Utils;
import org.example.VKData.UserDB;

import java.util.*;

public class Other {
    public static HashMap<String, org.example.DB.Base> map = new HashMap<>();

    public static class Node {
        public boolean copy;
        public org.example.DB.Base data;

        public Node(boolean copy, org.example.DB.Base data) {
            this.copy = copy;
            this.data = data;
        }
    }

    public static Node getBase() throws InterruptedException {
        org.example.DB.Base data = null;
        long date = 0;
        String name = General.strings.get(General.indexString);
        ++General.indexString;

        int type = switch (name) {
            case "users" -> 0;
            case "friends" -> 1;
            case "friendsGenerate" -> 2;
            case "groups" -> 3;
            case "groupsIn" -> 4;
            case "groupsInGenerate" -> 5;
            case "subscribers" -> 6;
            case "subscribersIn" -> 7;
            case "usersAll" -> 8;
            default -> -1;
        };

        if (type == -1) {
            if (Utils.isInteger(name)) return null;
            return new Node(true, map.get(name));
        }

        Long dateTemp = Base.getDate();
        if (dateTemp == null) return null;
        date = dateTemp;

        General.lock.lock1();
        switch (type) {
            case 8 -> { data = new Users(new ArrayList<>(General.users.keySet()), date); }
            default -> {
                ArrayList<Integer> ids = Base.getIntegers();
                if (ids == null) return null;

                switch (type) {
                    case 0 -> { data = new Users(ids, date); }
                    case 1 -> {
                        TreeSet<Integer> set = new TreeSet<>();
                        if (date == 0) {
                            for (int element : ids) {
                                UserDB userDB = General.users.get(element);
                                if (userDB == null) continue;
                                if (userDB.iDsHistories == null) continue;
                                if (userDB.iDsHistories[UserIDsEnum.FRIENDS.ordinal()] == null) continue;
                                if (userDB.iDsHistories[UserIDsEnum.FRIENDS.ordinal()].last.data == null) continue;
                                for (int id : userDB.iDsHistories[UserIDsEnum.FRIENDS.ordinal()].last.data) set.add(id);
                            }
                        } else {
                            for (int element : ids) {
                                UserDB userDB = General.users.get(element);
                                if (userDB == null) continue;
                                if (userDB.iDsHistories == null) continue;
                                if (userDB.iDsHistories[UserIDsEnum.FRIENDS.ordinal()] == null) continue;
                                if (userDB.iDsHistories[UserIDsEnum.FRIENDS.ordinal()].last.data == null) continue;

                                TreeSet<Integer> temp = userDB.iDsHistories[UserIDsEnum.FRIENDS.ordinal()].get(date);
                                if (temp == null) continue;
                                set.addAll(temp);
                            }
                        } data = new Users(new ArrayList<>(set), date);
                    }
                    case 2 -> {
                        TreeSet<Integer> set = new TreeSet<>();
                        if (date == 0) {
                            for (int element : ids) {
                                TreeSet<Integer> temp = General.generateIds[GenerateIDsEnum.FRIENDS.ordinal()].get(element);
                                if (temp == null) continue;
                                set.addAll(temp);
                            }
                        } else {
                            for (int element : ids) {
                                TreeSet<Integer> temp = Generate.getGenerateUserIds(element, GenerateIDsEnum.FRIENDS.ordinal(), UserIDsEnum.FRIENDS.ordinal(), date);
                                if (temp == null) continue;
                                set.addAll(temp);
                            }
                        } data = new IDsBase(new ArrayList<>(set), date);
                    }
                    case 3 -> {  }
                    case 4 -> {  }
                    case 5 -> {  }
                }
            }
        }

        General.lock.unlock1();
        return new Node(false, data);
    }

    public static boolean isValue(String string) {
        return switch (string) {
            case "to", "scan", "token", "dataBase" -> false;
            default -> true;
        };
    }

    //0 новая переменна
    //1 копированная переменная
    //2 созданная переменная

    public static boolean run() throws InterruptedException {
        String name = General.strings.get(General.indexString);
        org.example.DB.Base data = null;
        int valueType;

        switch (name) {
            case "remove" -> {
                ArrayList<String> strings = Base.getStrings();
                if (strings == null) return false;

                for (String element : strings)
                    map.remove(element);
                return true;
            }
            case "clear" -> {
                map.clear();
                ++General.indexString;
                return true;
            }
            case "list" -> {
                System.out.println(Colors.ANSI_GREEN + "value count: " + Integer.toString(map.size()) + Colors.ANSI_RESET);
                for (Map.Entry<String, org.example.DB.Base> entry : map.entrySet())
                    System.out.println(Colors.ANSI_GREEN + ":" + entry.getKey() + ":\t" + entry.getValue().getClass() + Colors.ANSI_RESET + "\t" + entry.getValue().data.size());
                ++General.indexString;
                return true;
            }
            default -> {
                Node node = getBase();
                if (node == null) return false;
                if (node.data == null) {
                    valueType = 0;
                }
                else {
                    valueType = ((node.copy) ? 1 : 2);
                    data = node.data;
                }
            }
        }

        if (General.strings.size() - General.indexString == 0) {
            System.out.println("Error not command: " + name);
            return false;
        }

    q:  switch (General.strings.get(General.indexString)) {
            case "=" -> {
                if (valueType == 2) {
                    System.out.println("Error not value: " + name);
                    return false;
                }

                ++General.indexString;
                switch (General.strings.get(General.indexString)) {
                    case "filter", "remove", "chain", "scan", "out" -> { --General.indexString; }
                    case "dataBase", "token", "about", "help", "exit" -> {
                        System.out.println("Error align command: " + General.strings.get(General.indexString));
                        return false;
                    }
                    default -> {
                        String name2 = General.strings.get(General.indexString);
                        Node node = getBase();

                        if (node == null) return false;
                        if (node.data == null) {
                            System.out.println("Error not value: " + name2);
                            return false;
                        }

                        valueType = 2;
                        data = ((valueType == 0) ? node.data : node.data.copy() );
                        if (valueType == 0) map.put(name, data);
                        else if (!map.containsKey(name)) map.put(name, data);
                    }
                }
            }
            case "<" -> {
                if (valueType == 2) {
                    System.out.println("Error not value: " + name);
                    return false;
                }

                ++General.indexString;
                if (data == null) {
                    Node node = getBase();
                    if (node == null) return false;
                    if (node.data == null) {
                        System.out.println("Error 228");
                        return false;
                    }

                    valueType = 2;
                    data = node.data;
                    map.put(name, data);
                    if (!(General.strings.size() - General.indexString > 1 && General.strings.get(General.indexString).equals(","))) break;
                    else ++General.indexString;
                }

                TreeSet<Object> set = new TreeSet<>();
                while (General.strings.size() - General.indexString > 0) {
                    Node node = getBase();
                    if (node == null || node.data == null) {
                        System.out.println("Error 1488");
                        return false;
                    }

                    if (data.getClass() == node.data.getClass()) {
                        set.addAll(node.data.data);
                    } else {
                        System.out.println("Error 1337");
                        return false;
                    }

                    if (General.strings.size() - General.indexString > 1 && General.strings.get(General.indexString).equals(",")) ++General.indexString;
                    else break;
                } data.append(set);
            }
            default -> {
                if (valueType == 0) {
                    System.out.println("Error not command: " + name);
                    return false;
                }
            }
        }

        while (General.strings.size() - General.indexString > 1) {
            boolean equals = false;
            if (General.strings.get(General.indexString).equals("=")){
                if (valueType != 2) {
                    System.out.println("Error not value align: " + name);
                    return false;
                } equals = true;
            } else if (!General.strings.get(General.indexString).equals(">")) break;

            if (data == null || data.data == null || data.data.isEmpty()) {
                System.out.println("error data is empty");
                return false;
            }

            if (General.strings.size() - General.indexString < 2) {
                System.out.println("Error not io command");
                return false;
            }

            switch (General.strings.get(++General.indexString)) {
                case "out" -> { data.out(); }
                case "count" -> { System.out.println(Colors.ANSI_GREEN + Integer.toString(data.data.size()) + Colors.ANSI_RESET); }
                case "scan" -> {
                    if (General.vkTokens.data.isEmpty()) {
                        System.out.println("Error not accessTokens");
                        return false;
                    }

                    ++General.indexString;
                    Scan.Node node = Scan.getScan();
                    if (node == null) return false;

                    ScanClasses.ScanBase scan = null;
                    switch (node.type) {
                        case 0 -> {
                            if (data instanceof Users) {
                                scan = new ScanClasses.ScanFriends(data.data, node.tokens, node.rerty, node.rertyTime, node.level);
                            } else {
                                System.out.println("Error scan not users");
                                return false;
                            }
                        }
                    }

                    scan.start();
                    scan.join();
                }
                case "history" -> {
                    if (General.strings.size() - General.indexString < 2) {
                        System.out.println("Error not command of history");
                        return false;
                    }

                    if (data instanceof Users users) {
                        switch (General.strings.get(++General.indexString)) {
                            case "friends" -> { users.outHistoryIds(UserIDsEnum.FRIENDS.ordinal()); }
                            default -> {
                                System.out.println("Error not command history: " + General.strings.get(General.indexString));
                                return false;
                            }
                        }
                    }
                }
                case "filter" -> {
                    if (equals) {
                        if (valueType != 2) map.put(name, data);
                    } else if (valueType != 2) data = data.copy();

                    if (General.strings.size() - General.indexString < 3) {
                        System.out.println("Error not command of filter");
                        return false;
                    }

                    switch (General.strings.get(++General.indexString)) {
                        case "bdate" -> {
                            if (data instanceof Users users) {
                                if (!Utils.isBDate(General.strings.get(++General.indexString))) return false;
                                int bdate = Utils.addBDate(General.strings.get(General.indexString));
                                users.filterId(UserIDEnum.BDATE.ordinal(), bdate);
                            } else {
                                System.out.println("Error syntax bdate");
                                return false;
                            }
                        }
                        case "name" -> {
                            if (data instanceof Users users) {
                                ++General.indexString;
                                int percent = Base.getPercent();
                                if (percent < 0) return false;

                                int type = switch (General.strings.get(General.indexString)) {
                                    case "first" -> UserIDEnum.FIRST_NAME.ordinal();
                                    case "last" -> UserIDEnum.LAST_NAME.ordinal();
                                    case "nick" -> UserIDEnum.NICK_NAME.ordinal();
                                    case "status" -> UserIDEnum.STATUS.ordinal();
                                    case "domain" -> UserIDEnum.DOMAIN.ordinal();
                                    default -> -1;
                                };

                                if (type == -1) {
                                    System.out.println("Error not name: " + General.strings.get(General.indexString));
                                    return false;
                                } ++General.indexString;

                                ArrayList<String> strings = Base.getStrings();
                                if (strings == null) return false;
                                users.filterName(type, strings, percent);
                                --General.indexString;
                            }
                        }
                        case "names" -> {
                            if (data instanceof Users users) {
                                ++General.indexString;
                                int percent = Base.getPercent();

                                ArrayList<Base.Names> buffer = Base.getNames();
                                if (buffer == null) return false;
                                users.filterNames(buffer, percent);
                                --General.indexString;
                            }
                        }
                        case "id" -> {
                            if (data instanceof Users users) {
                                int id = Integer.parseInt(General.strings.get(General.indexString + 2));
                                switch (General.strings.get(General.indexString + 1)) {
                                    case "city" -> { users.filterId(UserIDEnum.CITY.ordinal(), id); }
                                    case "sex" -> { users.filterId(UserIDEnum.SEX.ordinal(), id); }
                                    case "type" -> { users.filterId(UserIDEnum.TYPE.ordinal(), id); }
                                    default -> {
                                        System.out.println("Error not filter in: " + General.strings.get(General.indexString + 1));
                                        return false;
                                    }
                                } General.indexString += 2;
                            }

                        }
                        case "idGenerate" -> {
                            if (data instanceof Users users) {
                                ++General.indexString;
                                Base.GenerateAndLevel args = new Base.GenerateAndLevel();
                                if (args.error) return false;

                                int id;
                                try {
                                    id = Integer.parseInt(General.strings.get(General.indexString + 1));
                                } catch (NumberFormatException e) {
                                    System.out.println("Error unconvert id: " + General.strings.get(General.indexString + 1));
                                    return false;
                                }

                                switch (General.strings.get(General.indexString)) {
                                    case "city" -> { users.filterIdGenerate(UserIDEnum.CITY.ordinal(), id, args.generate, args.level); }
                                    case "type" -> { users.filterIdGenerate(UserIDEnum.TYPE.ordinal(), id, args.generate, args.level); }
                                    case "sex" -> { { users.filterIdGenerate(UserIDEnum.SEX.ordinal(), id, args.generate, args.level); } }
                                    default -> {
                                        System.out.println("Error is generate command: " + General.strings.get(General.indexString));
                                        return false;
                                    }
                                } ++General.indexString;
                            } else {
                                System.out.println("Error io element is not users");
                                return false;
                            }
                        }
                    }
                }
                case "remove" -> {
                    if (equals) {
                        if (valueType != 2) map.put(name, data);
                    } else if (valueType != 2) data = data.copy();

                    if (General.strings.size() - General.indexString < 3) {
                        System.out.println("Error not command of filter");
                        return false;
                    }

                    switch (General.strings.get(++General.indexString)) {
                        case "bdate" -> {
                            if (data instanceof Users users) {
                                if (!Utils.isBDate(General.strings.get(++General.indexString))) return false;
                                int bdate = Utils.addBDate(General.strings.get(General.indexString));
                                users.filterIdRemove(UserIDEnum.BDATE.ordinal(), bdate);
                            } else {
                                System.out.println("Error syntax bdate");
                                return false;
                            }
                        }
                        case "name" -> {
                            if (data instanceof Users users) {
                                ++General.indexString;
                                int percent = Base.getPercent();
                                if (percent < 0) return false;

                                int type = switch (General.strings.get(General.indexString)) {
                                    case "first" -> UserIDEnum.FIRST_NAME.ordinal();
                                    case "last" -> UserIDEnum.LAST_NAME.ordinal();
                                    case "nick" -> UserIDEnum.NICK_NAME.ordinal();
                                    case "status" -> UserIDEnum.STATUS.ordinal();
                                    case "domain" -> UserIDEnum.DOMAIN.ordinal();
                                    default -> -1;
                                };

                                if (type == -1) {
                                    System.out.println("Error not name: " + General.strings.get(General.indexString));
                                    return false;
                                } ++General.indexString;

                                ArrayList<String> strings = Base.getStrings();
                                if (strings == null) return false;
                                users.filterName(type, strings, percent);
                                --General.indexString;
                            }
                        }
                        case "names" -> {
                            if (data instanceof Users users) {
                                ++General.indexString;
                                int percent = Base.getPercent();

                                ArrayList<Base.Names> buffer = Base.getNames();
                                if (buffer == null) return false;
                                users.removeNames(buffer, percent);
                                --General.indexString;
                            }
                        }
                        case "id" -> {
                            if (data instanceof Users users) {
                                int id = Integer.parseInt(General.strings.get(General.indexString + 2));
                                switch (General.strings.get(General.indexString + 1)) {
                                    case "city" -> { users.filterIdRemove(UserIDEnum.CITY.ordinal(), id); }
                                    case "sex" -> { users.filterIdRemove(UserIDEnum.SEX.ordinal(), id); }
                                    case "type" -> { users.filterIdRemove(UserIDEnum.TYPE.ordinal(), id); }
                                    default -> {
                                        System.out.println("Error not filter in: " + General.strings.get(General.indexString + 1));
                                        return false;
                                    }
                                } General.indexString += 2;
                            }

                        }
                        case "idGenerate" -> {
                            if (data instanceof Users users) {
                                ++General.indexString;
                                Base.GenerateAndLevel args = new Base.GenerateAndLevel();
                                if (args.error) return false;

                                int id;
                                try {
                                    id = Integer.parseInt(General.strings.get(General.indexString + 1));
                                } catch (NumberFormatException e) {
                                    System.out.println("Error unconvert id: " + General.strings.get(General.indexString + 1));
                                    return false;
                                }

                                switch (General.strings.get(General.indexString)) {
                                    case "city" -> { users.removeIdGenerate(UserIDEnum.CITY.ordinal(), id, args.generate, args.level); }
                                    case "type" -> { users.removeIdGenerate(UserIDEnum.TYPE.ordinal(), id, args.generate, args.level); }
                                    case "sex" -> { { users.removeIdGenerate(UserIDEnum.SEX.ordinal(), id, args.generate, args.level); } }
                                    default -> {
                                        System.out.println("Error is generate command: " + General.strings.get(General.indexString));
                                        return false;
                                    }
                                } ++General.indexString;
                            } else {
                                System.out.println("Error io element is not users");
                                return false;
                            }
                        }
                    }
                }
                case "chain" -> {
                    if (data instanceof Users users) {
                        ++General.indexString;
                        ArrayList<Chains.Base> chains = new ArrayList<>();

                        while (General.strings.size() - General.indexString > 2) {
                            Boolean generate = Base.getGenerate();
                            if (generate == null) return false;
                            ArrayList<Integer> in;
                            ArrayList<Integer> to;

                            if (Utils.isInteger(General.strings.get(General.indexString))) {
                                in = Base.getIntegers();
                                if (in == null) return false;
                            } else {
                                String nameIn = General.strings.get(General.indexString);
                                Node node = getBase();
                                if (node == null || !(node.data instanceof Users)) {
                                    System.out.println("Error not value or value is not users in chain in: " + nameIn);
                                    return false;
                                } in = new ArrayList<>(node.data.data);
                            }

                            if (!General.strings.get(General.indexString).equals("to")) {
                                System.out.println("Error not to in chain");
                                return false;
                            } ++General.indexString;

                            if (Utils.isInteger(General.strings.get(General.indexString))) {
                                to = Base.getIntegers();
                                if (to == null) return false;
                            } else {
                                String nameTo = General.strings.get(General.indexString);
                                Node node = getBase();
                                if (node == null || !(node.data instanceof Users)) {
                                    System.out.println("Error not value or value is not users in chain to: " + nameTo);
                                    return false;
                                } to = new ArrayList<>(node.data.data);
                            }

                            TreeSet<Integer> set = new TreeSet<>(in);
                            set.addAll(to);

                            if (set.size() < in.size() + to.size()) {
                                System.out.println("Error in and to equals elements");
                                return false;
                            }

                            Chains.Base chain = ((generate) ? new Chains.ChainGenerateUsers(in, to, data.date, data.data) : new Chains.ChainUsers(in, to, data.date, data.data));
                            chains.add(chain);
                            chain.start();

                            if (General.strings.size() - General.indexString > 0 && General.strings.get(General.indexString).equals(",")) ++General.indexString;
                            else break;
                        }

                        for (Chains.Base element : chains)
                            element.join();

                        chains.removeIf(s -> s.data == null);
                        data = new org.example.DB.Chains(data.date, chains);
                        if (equals) map.put(name, data);
                        --General.indexString;
                    } else {
                        System.out.println("Error data not users");
                        return false;
                    }
                }
                case "general" -> {
                    if (equals) {
                        if (valueType != 2) map.put(name, data);
                    } else if (valueType != 2) data = data.copy();

                    ++General.indexString;
                    Boolean generate = Base.getGenerate();
                    if (generate == null) return false;

                    if (data instanceof Users users) {
                        switch (General.strings.get(General.indexString)) {
                            case "friends" -> { users.generalIds(GenerateIDsEnum.FRIENDS.ordinal(), UserIDsEnum.FRIENDS.ordinal(), generate); }
                            case "subscribers" -> { users.generalIds(GenerateIDsEnum.SUBSCRIBERS.ordinal(), UserIDsEnum.SUBSCRIBERS.ordinal(), generate); }
                            case "groups" -> {
                                users.generalIds(GenerateIDsEnum.GROUPS.ordinal(), UserIDsEnum.GROUPS.ordinal(), generate);
                                data = new Groups(data.data, data.date);
                            }
                            default -> {
                                System.out.println("Error not general command: " + General.strings.get(General.indexString));
                                return false;
                            }
                        }
                    }
                }
                default -> {
                    System.out.println("Error not command io: " + General.strings.get(General.indexString));
                    return false;
                }
            } ++General.indexString;
        } return true;
    }
}
