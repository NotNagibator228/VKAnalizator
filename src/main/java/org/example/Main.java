package org.example;

import org.example.Console.DataBase;
import org.example.Console.Other;
import org.example.Console.Scan;
import org.example.Console.Token;

import java.io.*;
import java.util.Scanner;

import static org.example.Console.StringUtils.getString;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);
        General.init();
    q:  while (true) {
            System.out.print(Colors.ANSI_GREEN + "> " + Colors.ANSI_RESET);
            String str = scanner.nextLine();
            General.strings = getString(str);
            if (General.strings == null) continue;
            General.indexString = 0;

            while (General.strings.size() - General.indexString > 0) {
                switch (General.strings.get(General.indexString)) {
                    case "dataBase" -> { if (!DataBase.run()) continue q; }
                    case "token" -> { if (!Token.run()) continue q; }
                    case "scan" -> { if (!Scan.run()) continue q; }
                    case "help" -> {
                        System.out.println(General.help);
                        ++General.indexString;
                    }
                    case "about" -> {
                        System.out.println("VKAnalizator v0.2\nby A.S.Zaykov\nbc1qxs7vzarpr2p3k50fczem3u0wuqwl094m7zd0jx поддержите проэкт биткоином\nhttps://github.com/NotNagibator228/VKAnalizator гитхаб проэкта");
                        ++General.indexString;
                    }
                    case "exit" -> {
                        scanner.close();
                        System.exit(0);
                    }
                    default -> { if (!Other.run()) continue q; }
                }

                if (General.strings.size() - General.indexString > 0) {
                    if (General.strings.get(General.indexString).equals("&")) {
                        if (General.strings.size() - General.indexString > 1) ++General.indexString;
                        else {
                            System.out.println("Error not command");
                            continue q;
                        }
                    } else {
                        System.out.println("Error expected &");
                        continue q;
                    }
                } else break;
            }
        }
    }
}