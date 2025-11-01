package org.example.Console;

import org.example.Enum.UserIDEnum;
import org.example.Enum.UserIDsEnum;
import org.example.General;
import org.example.IO;

import java.io.*;
import java.util.ArrayList;

public class DataBase {
    public static void run() throws IOException {
        switch(General.strings.get(1)) {
            case "load" -> {
                ArrayList<DataInputStream> dataInputStreams = new ArrayList<>();
                for (int index = 2; index < General.strings.size(); ++index) {
                    try {
                        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(General.strings.get(index)));
                        dataInputStreams.add(dataInputStream);
                    } catch (FileNotFoundException e) {
                        System.out.println("Error not file: " + General.strings.get(index));
                        for (DataInputStream element : dataInputStreams)
                            element.close();
                        return;
                    }

                    if (General.strings.size() - index > 1 && !General.strings.get(index + 1).equals(",")) {
                        System.out.println("Error syntax in ,");
                        return;
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
                System.out.println(
                        "Users count: " + Integer.toString(General.users.size())
                        + "\n\tFirstName count: " + Integer.toString(General.userStrings[UserIDsEnum.FRIENDS.ordinal()].strings.size())
                        + "\n\tLastName count: " + Integer.toString(General.userStrings[UserIDEnum.LAST_NAME.ordinal()].strings.size())
                        + "\n\tNickName count: " + Integer.toString(General.userStrings[UserIDEnum.NICK_NAME.ordinal()].strings.size())
                                + "\n\tStatus count: " + Integer.toString(General.userStrings[UserIDEnum.STATUS.ordinal()].strings.size())
                                + "\n\tDOMAIN count: " + Integer.toString(General.userStrings[UserIDEnum.DOMAIN.ordinal()].strings.size())
                                + "\nGroups count: " + Integer.toString(General.groups.size())
                );
            }
        }
    }
}
