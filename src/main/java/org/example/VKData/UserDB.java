package org.example.VKData;

import org.example.Colors;
import org.example.Data.IDHistory;
import org.example.Data.IDsHistory;
import org.example.Data.OnlineHistory;
import org.example.Enum.UserIDEnum;
import org.example.Enum.UserIDsEnum;
import org.example.General;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeSet;

public class UserDB {
    public long phoneNumberLong = -1;
    public String phoneNumberString = null;

    public IDHistory[] idHistories = null;
    public IDsHistory[] iDsHistories = null;
    public OnlineHistory onlineHistory = null;

    public static final char[] nameChar = { 'F', 'L', 'N', 'S', 'D' };

    public void addPhoneNumber(String str) {
        if (phoneNumberLong > 0) return;
        phoneNumberLong = 0;

        for (char element : str.toCharArray()) {
            if (element == '*') {
                phoneNumberString = str;
                phoneNumberLong = 0;
                return;
            } else if (Character.isDigit(element)) phoneNumberLong = (phoneNumberLong * 10) + (long)(element - '0');
        } phoneNumberString = null;
    }

    public void load(DataInputStream dataInputStream, int[][] arrays, int id) throws IOException {
        long tempPhoneNumberLong = dataInputStream.readLong();
        String tempPhoneNumberString = (tempPhoneNumberLong == 0) ? dataInputStream.readUTF() : null;

        if (phoneNumberLong < 1) {
            phoneNumberLong = tempPhoneNumberLong;
            phoneNumberString = tempPhoneNumberString;
        }

        int count = dataInputStream.readInt();
        if (count != -1) {
            if (idHistories == null) idHistories = new IDHistory[UserIDEnum.values().length];
            for (int index = 0; index < General.userStringCount; ++index) {
                if (count != 0) {
                    if (idHistories[index] == null) idHistories[index] = new IDHistory();
                    TreeSet<IDHistory.Node> nodes = IDHistory.loadNodes(dataInputStream, count, arrays[index]);

                    for (IDHistory.Node element : nodes)
                        General.idGenerateUsers[index].computeIfAbsent(element.id, s -> new TreeSet<>()).add(id);
                    idHistories[index].load(nodes);
                } count = dataInputStream.readInt();
            }

            for (int index = General.userStringCount; index < idHistories.length; ++index) {
                if (count != 0) {
                    if (idHistories[index] == null) idHistories[index] = new IDHistory();
                    TreeSet<IDHistory.Node> nodes = IDHistory.loadNodes(dataInputStream, count);

                    for (IDHistory.Node element : nodes)
                        General.idGenerateUsers[index].computeIfAbsent(element.id, s -> new TreeSet<>()).add(id);
                    idHistories[index].load(nodes);
                } count = dataInputStream.readInt();
            }
        } else count = dataInputStream.readInt();

        if (count != -1) {
            if (iDsHistories == null) iDsHistories = new IDsHistory[UserIDsEnum.values().length];
            if (count != 0) {
                if (iDsHistories[0] == null) iDsHistories[0] = new IDsHistory();
                IDsHistory.LoadNodes loadNodes = new IDsHistory.LoadNodes(dataInputStream, count);
                TreeSet<Integer> set = General.generateIds[0].computeIfAbsent(id, s -> new TreeSet<>());

                for (Map.Entry<Long, TreeSet<Integer>> element : loadNodes.added.entrySet()) {
                    for (int element_id : element.getValue())
                        General.generateIds[0].computeIfAbsent(element_id, s -> new TreeSet<>()).add(id);
                    set.addAll(element.getValue());
                } iDsHistories[0].load(loadNodes);
                count = dataInputStream.readInt();
            } else count = dataInputStream.readInt();

            if (count != 0) {
                if (iDsHistories[1] == null) iDsHistories[1] = new IDsHistory();
                IDsHistory.LoadNodes loadNodes = new IDsHistory.LoadNodes(dataInputStream, count);
                TreeSet<Integer> set = General.generateIds[1].computeIfAbsent(id, s -> new TreeSet<>());

                for (Map.Entry<Long, TreeSet<Integer>> element : loadNodes.added.entrySet()) {
                    for (int element_id : element.getValue())
                        General.generateIds[3].computeIfAbsent(element_id, s -> new TreeSet<>()).add(id);
                    set.addAll(element.getValue());
                } iDsHistories[1].load(loadNodes);
                count = dataInputStream.readInt();
            } else count = dataInputStream.readInt();

            if (count != 0) {
                if (iDsHistories[2] == null) iDsHistories[2] = new IDsHistory();
                IDsHistory.LoadNodes loadNodes = new IDsHistory.LoadNodes(dataInputStream, count);
                TreeSet<Integer> set = General.generateIds[2].computeIfAbsent(id, s -> new TreeSet<>());

                for (Map.Entry<Long, TreeSet<Integer>> element : loadNodes.added.entrySet()) {
                    for (int element_id : element.getValue())
                        General.generateIds[4].computeIfAbsent(element_id, s -> new TreeSet<>()).add(id);
                    set.addAll(element.getValue());
                } iDsHistories[2].load(loadNodes);
                count = dataInputStream.readInt();
            } else count = dataInputStream.readInt();
        } else count = dataInputStream.readInt();

        if (count != -1) {
            if (onlineHistory == null) onlineHistory = new OnlineHistory();
            onlineHistory.load(dataInputStream, count);
        }
    }

    public void save(DataOutputStream dataOutputStream) throws IOException {
        System.out.println(phoneNumberLong);
        System.out.println(phoneNumberString);

        dataOutputStream.writeLong(phoneNumberLong);
        if (phoneNumberLong == 0) dataOutputStream.writeUTF(phoneNumberString);

        if (idHistories != null) {
            for (IDHistory element : idHistories)
                if (element != null)
                    element.save(dataOutputStream);
                else dataOutputStream.writeInt(0);
        } else dataOutputStream.writeInt(-1);

        if (iDsHistories != null) {
            for (IDsHistory element : iDsHistories)
                if (element != null)
                    element.save(dataOutputStream);
                else dataOutputStream.writeInt(0);
        } else dataOutputStream.writeInt(-1);

        if (onlineHistory != null) {
            onlineHistory.save(dataOutputStream);
        } else dataOutputStream.writeInt(-1);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        if (idHistories != null) {
            for (int index = 0; index < nameChar.length; ++index) {
                if (idHistories[index] != null) {
                    buffer.append(nameChar[index]);
                    buffer.append(Colors.ANSI_GREEN).append(General.userStrings[index].strings.get(idHistories[index].data.getLast().id)).append(Colors.ANSI_RESET);
                }
            }
        } else buffer.append("NoneName");

        if (phoneNumberLong != -1) buffer.append(Colors.ANSI_GREEN).append((phoneNumberLong > 0) ? Long.toString(phoneNumberLong) : phoneNumberString).append(Colors.ANSI_RESET);

        return buffer.toString();
    }
}
