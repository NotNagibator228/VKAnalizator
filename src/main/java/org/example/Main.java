package org.example;

import org.example.Algorithm.Chain;
import org.example.Algorithm.IDUsersGenerate;
import org.example.Clients.ScanClasses;
import org.example.Clients.VKToken;
import org.example.Console.DataBase;
import org.example.Console.Other;
import org.example.Console.Scan;
import org.example.Console.Token;
import org.example.Enum.UserIDEnum;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
                    case "scan" -> { Scan.run(); }
                    case "test" -> {
                        Chain.ChainGenerate test = new Chain.ChainGenerate(new ArrayList<>(List.of(182011717)), new ArrayList<>(List.of(517988146)), 0);
                        test.out();
                        return;
                    }
                    case "help" -> {
                        System.out.println(
                            "Пока читайте исходники я это не сделал"
                        );
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