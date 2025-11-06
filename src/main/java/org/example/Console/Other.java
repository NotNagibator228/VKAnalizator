package org.example.Console;

import org.example.Algorithm.Generate;
import org.example.Console.DB.Groups;
import org.example.Console.DB.IDsBase;
import org.example.Console.DB.Users;
import org.example.Enum.GenerateIDsEnum;
import org.example.Enum.UserIDEnum;
import org.example.Enum.UserIDsEnum;
import org.example.General;
import org.example.Utils;
import org.example.VKData.UserDB;

import java.util.ArrayList;
import java.util.TreeSet;

public class Other {
    public static void getTime() {
        long buffer = Long.parseLong(General.strings.get(General.indexString + 1));
        ++General.indexString;
    }

    public static int getPercent() {
        int buffer = 100;
        if (General.strings.get(General.indexString).charAt(0) == '-') {
            if (General.strings.get(General.indexString).length() == 1){
                System.out.println("Error length argument -");
                return -1;
            }

            if (General.strings.get(General.indexString).charAt(1) == '-') {
                if (General.strings.get(General.indexString).equals("percent")) {
                    try {
                        buffer = Integer.parseInt(General.strings.get(General.indexString + 1));
                    } catch (NumberFormatException e) {
                        System.out.println("Error percent");
                        return -3;
                    }
                } else {
                    System.out.println("Error not argument: " + General.strings.get(General.indexString));
                    return -2;
                }
            } else {
                if (General.strings.get(General.indexString).charAt(1) == 'p') {
                    General.indexString += 2;
                    try {
                        buffer = Integer.parseInt(General.strings.get(General.indexString + 1));
                    } catch (NumberFormatException e) {
                        System.out.println("Error percent");
                        return -4;
                    }
                } else {
                    System.out.println("Error not key: " + General.strings.get(General.indexString).charAt(1));
                    return -5;
                }
            }
        }

        if (buffer < 20 || buffer > 100) {
            System.out.println("Error percent < 20 or percent > 100");
            return -6;
        } return buffer;
    }

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
            return false;
        }

        ++General.indexString;
        if (General.strings.get(General.indexString).charAt(0) == '-') {
            if (General.strings.get(General.indexString).length() == 1) {
                System.out.println("Error parameter -");
                return false;
            }

            if (General.strings.get(General.indexString).charAt(1) == '1') {
                if (General.strings.get(General.indexString).substring(2).equals("date")) {
                    getTime();
                } else {
                    System.out.println("Error not argument: " + General.strings.get(General.indexString));
                    return false;
                }
            } else {
                if (General.strings.get(General.indexString).charAt(1) == 'd') getTime();
                else {
                    System.out.println("Error not key: " + General.strings.get(General.indexString).charAt(1));
                    return false;
                }
            }
        }

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
            //ОткладОчка
            //System.out.println("Debug: " + General.strings.get(General.indexString));

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
                case "out" -> {
                    if (data instanceof Users users) { users.out(); }
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
                                General.indexString += 2;
                            } else {
                                System.out.println("Error syntax bdate");
                                return false;
                            }
                        }
                        case "name" -> {
                            if (data instanceof Users users) {
                                int percent = getPercent();
                                if (percent < 0) return false;

                                type = switch (General.strings.get(++General.indexString)) {
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
                                }

                                ArrayList<String> strings = Base.getStrings();
                                if (strings == null) return false;
                                users.filterName(type, strings, percent);
                                General.indexString += 2;
                            }
                        }
                        case "names" -> {
                            if (data instanceof Users users) {
                                ++General.indexString;
                                int percent = getPercent();

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

                        }
                    }
                }
                case "remove" -> {

                }
                case "general" -> {
                    boolean generate = false;
                    ++General.indexString;

                    if (General.strings.get(General.indexString).charAt(0) == '-') {
                        if (General.strings.get(General.indexString).length() == 1) {
                            System.out.println("Error length general argument -");
                            return false;
                        }

                        if (General.strings.get(General.indexString).charAt(1) == '-') {
                            if (General.strings.get(General.indexString).substring(2).equals("generate"))
                                generate = true;
                            else {
                                System.out.println("Error not argument of general:" + General.strings.get(General.indexString).substring(2));
                                return false;
                            }
                        } else {
                            if (General.strings.get(General.indexString).charAt(1) == 'g') generate = true;
                            else {
                                System.out.println("Error not general key: " + General.strings.get(General.indexString).charAt(1));
                                return false;
                            }
                        } ++General.indexString;
                    }

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
