package org.example.Console;

import java.util.ArrayList;

public class StringUtils {
    public static ArrayList<String> getString(String string) {
        ArrayList<String> buffer = new ArrayList<>();
        for (int a = 0; a < string.length(); ++a) {
            if (string.charAt(a) != ' ') {
                boolean done = false;

            q:  for (int b = a; b < string.length(); ++b) {
                    switch (string.charAt(b)) {
                        case ' ' -> {
                            buffer.add(string.substring(a, b));
                            a = b; done = true; break q;
                        }
                        case ',', '>', '&' -> {
                            buffer.add(string.substring(b, b + 1));
                            if (a != b) buffer.add(string.substring(a, b));
                            a = b; done = true; break q;
                        }
                        case '"' -> {
                            StringBuffer stringBuffer = new StringBuffer();
                        q2:  for (int c = b + 1; c < string.length(); ++c) {
                                switch (string.charAt(c)) {
                                    case '\\' -> {
                                        if (string.length() - c < 3) {
                                            System.out.println("Error");
                                            return null;
                                        } ++c;

                                        if (string.charAt(c) == '\\' || string.charAt(c) == '"') stringBuffer.append(string.charAt(c));
                                        else if (string.charAt(c) == 'n') stringBuffer.append('\n');
                                        else {
                                            System.out.println("Error");
                                            return null;
                                        }
                                    }
                                    case '"' -> { a = c; done = true; break q2; }
                                    default -> { stringBuffer.append(string.charAt(c)); }
                                }
                            }

                            if (!done) {
                                System.out.println("Error");
                                return null;
                            }

                            buffer.add(stringBuffer.toString());
                            break q;
                        }
                    }
                } if (!done){
                    buffer.add(string.substring(a));
                    break;
                }
            }
        }

        if (buffer.isEmpty()) {
            System.out.println("Error");
            return null;
        } return buffer;
    }
}
