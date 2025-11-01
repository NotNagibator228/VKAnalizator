package org.example.Console;

import org.example.Clients.VKToken;
import org.example.General;

public class Token {
    public static void run() {
        switch (General.strings.get(1)) {
            case "add" -> {
                for (int index = 2; index < General.strings.size(); index += 3) {
                    try {
                        int id = Integer.parseInt(General.strings.get(index));
                        General.vkTokens.add(id, General.strings.get(index + 1));
                    } catch (NumberFormatException e) {
                        System.out.println("Error not number in accessToken: " + General.strings.get(index));
                        return;
                    }

                    if (General.strings.size() - index > 3 && !General.strings.get(index + 2).equals(",")) {
                        System.out.println("Error syntax of ,");
                        return;
                    }
                }
            }
            case "info" -> {
                System.out.println("Token's count: " + General.vkTokens.data.size());
                for (int index = 0; index < General.vkTokens.data.size(); ++index)
                    System.out.println(Integer.toString(index) + "\t:" + General.vkTokens.data.get(index));
            }
            case "remove" -> {

            }
        }
    }
}
