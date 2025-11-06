package org.example.Console;

import org.example.Enum.UserIDEnum;
import org.example.Enum.UserIDsEnum;
import org.example.General;
import org.example.IO;
import org.example.VKData.UserDB;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class DataBase {
    public static boolean run() throws IOException {
        switch(General.strings.get(++General.indexString)) {
            case "load" -> {
                ArrayList<DataInputStream> dataInputStreams = new ArrayList<>();
                for (int index = 2; index < General.strings.size(); ++index) {
                    General.indexString = index;
                    try {
                        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(General.strings.get(index)));
                        dataInputStreams.add(dataInputStream);
                    } catch (FileNotFoundException e) {
                        System.out.println("Error not file: " + General.strings.get(index));
                        for (DataInputStream element : dataInputStreams) element.close();
                        return false;
                    }

                    if (General.strings.size() - index > 1 && !General.strings.get(index + 1).equals(",")) {
                        System.out.println("Error syntax in ,");
                        return false;
                    }
                }

                for (DataInputStream element : dataInputStreams) {
                    IO.load(element);
                    element.close();
                }
            }
            case "save" -> {
                try {
                    DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(General.strings.get(2)));
                    IO.save(dataOutputStream);
                    dataOutputStream.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Error not save file: " + General.strings.get(2));
                } catch (IOException e) {
                    System.out.println("Error save file: " + General.strings.get(2));
                }
            }
            case "info" -> {
                int[] userId = new int[UserIDEnum.values().length - General.userStringCount];
                int[] userIds = new int[UserIDsEnum.values().length];

                for (Map.Entry<Integer, UserDB> entry : General.users.entrySet()) {
                    if (entry.getValue().idHistories != null)
                        for (int index = General.userStringCount; index < UserIDEnum.values().length; ++index)
                            if (entry.getValue().idHistories[index] != null) ++userId[index - General.userStringCount];

                    if (entry.getValue().iDsHistories != null)
                        for (int index = 0; index < UserIDsEnum.values().length; ++index)
                            if (entry.getValue().iDsHistories[index] != null) ++userIds[index];
                }

                System.out.println(
                        "Users count: " + Integer.toString(General.users.size())
                        + "\n\tFirstName: " + Integer.toString(General.userStrings[UserIDsEnum.FRIENDS.ordinal()].strings.size())
                        + "\n\tLastName: " + Integer.toString(General.userStrings[UserIDEnum.LAST_NAME.ordinal()].strings.size())
                        + "\n\tNickName: " + Integer.toString(General.userStrings[UserIDEnum.NICK_NAME.ordinal()].strings.size())
                                + "\n\tStatus: " + Integer.toString(General.userStrings[UserIDEnum.STATUS.ordinal()].strings.size())
                                + "\n\tDomain: " + Integer.toString(General.userStrings[UserIDEnum.DOMAIN.ordinal()].strings.size())
                                + "\n\tType: " + Integer.toString(userId[UserIDEnum.TYPE.ordinal() - General.userStringCount])
                                + "\n\tSex: " + Integer.toString(userId[UserIDEnum.SEX.ordinal() - General.userStringCount])
                                + "\n\tBdate: " + Integer.toString(userId[UserIDEnum.BDATE.ordinal() - General.userStringCount])
                                + "\n\tCity: " + Integer.toString(userId[UserIDEnum.CITY.ordinal() - General.userStringCount])
                                + "\n\tFriends: " + Integer.toString(userIds[UserIDsEnum.FRIENDS.ordinal()])
                                + "\n\tSubscribers: " + Integer.toString(userIds[UserIDsEnum.SUBSCRIBERS.ordinal()])
                                + "\n\tGroups: " + Integer.toString(userIds[UserIDsEnum.GROUPS.ordinal()])

                                + "\nGroups count: " + Integer.toString(General.groups.size())
                );
            }
        }

        ++General.indexString;
        return true;
    }
}
