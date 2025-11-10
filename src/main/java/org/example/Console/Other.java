package org.example.Console;

import org.example.Algorithm.Chains;
import org.example.Algorithm.Generate;
import org.example.Clients.ScanClasses;
import org.example.Colors;
import org.example.Console.DB.Groups;
import org.example.Console.DB.IDsBase;
import org.example.Console.DB.Users;
import org.example.Enum.GenerateIDsEnum;
import org.example.Enum.UserIDEnum;
import org.example.Enum.UserIDsEnum;
import org.example.General;
import org.example.Utils;
import org.example.VKData.UserDB;

import java.security.CodeSigner;
import java.util.ArrayList;
import java.util.TreeSet;

public class Other {


    public static boolean run() throws InterruptedException {
        IDsBase data = null;
        long date = 0;
        int type;

        type = switch (General.strings.get(General.indexString)) {
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
            System.out.println("Error not command: " + General.strings.get(General.indexString));
            System.out.println(General.help);
            return false;
        }

        ++General.indexString;
        Long dateTemp = Base.getDate();
        if (dateTemp == null) return false;
        date = dateTemp;

        switch (type) {
            case 8 -> { data = new Users(new ArrayList<>(General.users.keySet()), date); }
            case 9 -> {

            }
            default -> {
                ArrayList<Integer> ids = Base.getIntegers();
                if (ids == null) return false;

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

        while (General.strings.size() - General.indexString > 1) {
            if (!General.strings.get(General.indexString).equals(">")) break;
            if (data.data == null || data.data.isEmpty()) {
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

                                type = switch (General.strings.get(General.indexString)) {
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

                }
                case "chain" -> {
                    if (data instanceof Users users) {
                        ++General.indexString;
                        Boolean generate = Base.getGenerate();
                        if (generate == null) return false;

                        ArrayList<Integer> in = Base.getIntegers();
                        if (in == null) return false;

                        if (!General.strings.get(General.indexString).equals("to")) {
                            System.out.println("Error not chain to");
                            return false;
                        }

                        ++General.indexString;
                        ArrayList<Integer> to = Base.getIntegers();
                        --General.indexString;

                        TreeSet<Integer> set = new TreeSet<>();
                        set.addAll(in);
                        set.addAll(to);

                        if (set.size() < (in.size() + to.size())) {
                            System.out.println("Error in array or to array identical elements");
                            return false;
                        }

                        Chains.Base chain = ((generate) ? new Chains.ChainGenerateUsers(in, to, date, users.data) : new Chains.ChainUsers(in, to, date, users.data));
                        if (chain.data == null) return false;
                        chain.out();
                        data.data = chain.getIds();
                    }
                }
                case "general" -> {
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
