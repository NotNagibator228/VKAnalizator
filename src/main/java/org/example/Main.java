package org.example;

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
import java.util.Scanner;

import static org.example.Console.StringUtils.getString;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);
        General.init();

        while (true) {
            System.out.print("> ");
            String str = scanner.nextLine();
            General.strings = getString(str);
            if (General.strings == null) continue;

            switch (General.strings.getFirst()) {
                case "dataBase" -> { DataBase.run(); }
                case "token" -> { Token.run(); }
                case "exit" -> { System.exit(0); }
                case "scan" -> { Scan.run(); }
                default -> { Other.run(); }
            }
        }
    }
}