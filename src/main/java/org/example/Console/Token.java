package org.example.Console;

import org.example.Clients.VKToken;
import org.example.General;

import java.util.ArrayList;

public class Token {
    public static boolean run() {
        switch (General.strings.get(++General.indexString)) {
            case "add" -> {
                for (int index = General.indexString + 1; index < General.strings.size(); index += 3) {
                    try {
                        int id = Integer.parseInt(General.strings.get(index));
                        General.vkTokens.add(id, General.strings.get(index + 1));
                    } catch (NumberFormatException e) {
                        System.out.println("Error not number in accessToken: " + General.strings.get(index));
                        return false;
                    }

                    if (General.strings.size() - index > 3 && !General.strings.get(index + 2).equals(",")) {
                        System.out.println("Error syntax of ,");
                        return false;
                    }
                }
            }
            case "info" -> {
                System.out.println("Token's count: " + General.vkTokens.data.size());
                for (int index = 0; index < General.vkTokens.data.size(); ++index)
                    System.out.println(Integer.toString(index) + "\t:" + General.vkTokens.data.get(index));
            }
            case "remove" -> {
                ArrayList<Integer> indices = Base.getIntegers();
                if (indices == null) return false;
                if (Base.isAcceessTokenIndices(indices)) return false;

                ArrayList<VKToken> vkTokens = new ArrayList<>();
                for (int element : indices)
                    vkTokens.add(General.vkTokens.data.get(element));
                General.vkTokens.remove(vkTokens);
            }
        }

        ++General.indexString;
        return true;
    }
}
