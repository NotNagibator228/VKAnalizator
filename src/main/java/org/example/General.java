package org.example;

import org.example.Clients.VKTokens;
import org.example.Data.StringHistory;
import org.example.Enum.GenerateIDs;
import org.example.Enum.GroupIDEnum;
import org.example.Enum.UserIDEnum;
import org.example.VKData.GroupDB;
import org.example.VKData.UserDB;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;

public class General {
    public final static int userStringCount = 5;
    public final static int groupStringCount = 2;

    public final static int[] monthCode = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    public final static RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    public static ArrayList<String> strings;
    public static int indexString;
    public final static VKTokens vkTokens = new VKTokens();
    public final static Object lock = new Object();

    public static final StringHistory[] userStrings = new StringHistory[userStringCount];
    public static final StringHistory[] groupStrings = new StringHistory[groupStringCount];

    public static final TreeMap<Integer, UserDB> users = new TreeMap<>();
    public static final TreeMap<Integer, GroupDB> groups = new TreeMap<>();

    public static final TreeMap<Integer, TreeSet<Integer>>[] idGenerateUsers = new TreeMap[UserIDEnum.values().length];
    public static final TreeMap<Integer, TreeSet<Integer>>[] idGenerateGroups = new TreeMap[GroupIDEnum.values().length];
    public static final TreeMap<Integer, TreeSet<Integer>>[] generateIds = new TreeMap[GenerateIDs.values().length];

    public static void init(){
        for (int index = 0; index < userStringCount; ++index)
            userStrings[index] = new StringHistory();
        for (int index = 0; index < groupStringCount; ++index)
            groupStrings[index] = new StringHistory();

        for (int index = 0; index < UserIDEnum.values().length; ++index)
            idGenerateUsers[index] = new TreeMap<>();
        for (int index = 0; index < GroupIDEnum.values().length; ++index)
            idGenerateGroups[index] = new TreeMap<>();
        for (int index = 0; index < GenerateIDs.values().length; ++index)
            generateIds[index] = new TreeMap<>();
    }

    void clear() {
        users.clear();
        groups.clear();

        for (StringHistory element : userStrings)
            element.clear();
        for (StringHistory element : groupStrings)
            element.clear();

        for (TreeMap<Integer, TreeSet<Integer>> element : idGenerateUsers)
            element.clear();
        for (TreeMap<Integer, TreeSet<Integer>> element : idGenerateGroups)
            element.clear();
        for (TreeMap<Integer, TreeSet<Integer>> element : generateIds)
            element.clear();
    }
}
