package org.example.Console.DB;

import org.example.Algorithm.Generate;
import org.example.Console.Base;
import org.example.Data.IDHistory;
import org.example.Data.IDsHistory;
import org.example.Enum.UserIDEnum;
import org.example.Enum.UserIDsEnum;
import org.example.General;
import org.example.Utils;
import org.example.VKData.UserDB;

import java.util.ArrayList;
import java.util.TreeSet;

public class Users extends IDsBase {
    public Users(ArrayList<Integer> data, long date) {
        super(data, date);
    }

    public record NamesIndices (int first, int last) {}

    public static class NamesIndicesSet {
        public TreeSet<Integer> first = new TreeSet<>();
        public TreeSet<Integer> last = new TreeSet<>();
    }

    protected ArrayList<Integer> filterIdBuffer(int index, int id) {
        ArrayList<Integer> buffer = new ArrayList<>();
        if (date == 0) {
            for (int element : this.data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;

                if (userDB.idHistories[index].data.getLast().id == id)
                    buffer.add(element);
            }
        } else {
            for (int element : this.data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;

                IDHistory.Node node = userDB.idHistories[index].get(date);
                if (node == null) continue;
                if (node.id == id) buffer.add(element);
            }
        } return buffer;
    }

    public void filterId(int index, int id) {
        this.data = filterIdBuffer(index, id);
        if (this.data.isEmpty()) this.data = null;
    }

    public void filterIdRemove(int index, int id) {
        this.data.removeAll(filterIdBuffer(index, id));
        if (this.data.isEmpty()) this.data = null;
    }

    protected ArrayList<Integer> filterNameFull(int index, ArrayList<String> strings) {
        TreeSet<Integer> search = new TreeSet<>();

        for (String element : strings) {
            int result = General.userStrings[index].search(element);
            if (result > -1) search.add(result);
        }

        if (search.isEmpty()) {
            data = null;
            return null;
        }

        TreeSet<Integer> scan = new TreeSet<>();
        ArrayList<Integer> buffer = new ArrayList<>();

        for (int element : search)
            scan.addAll(General.userStrings[index].nodes.get(element).indices);

        if (date == 0) {
            for (int element : data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;
                if (scan.contains(userDB.idHistories[index].data.getLast().id)) buffer.add(element);
            }
        } else {
            for (int element : data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;

                IDHistory.Node node = userDB.idHistories[index].get(date);
                if (scan.contains(node.id)) buffer.add(element);
            }
        } return buffer;
    }

    protected ArrayList<Integer> filterNamePercent(int index, ArrayList<String> strings, int percent) throws InterruptedException {
        TreeSet<Integer> temp = new TreeSet<>();
        if (date == 0) {
            for (int element : data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;
                temp.add(userDB.idHistories[index].data.getLast().id);
            }
        } else {
            for (int element : data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;

                IDHistory.Node node = userDB.idHistories[index].get(date);
                if (node == null) continue;
                temp.add(node.id);
            }
        }

        ArrayList<Integer> searchIndices = General.userStrings[index].getIndices(temp);
        ArrayList<Integer> buffer = new ArrayList<>();
        TreeSet<Integer> search = new TreeSet<>();
        temp.clear();

        for (String element : strings)
            temp.addAll(General.userStrings[index].search(element, percent, General.threadCount, searchIndices));

        for (int element : temp)
            search.addAll(General.userStrings[index].nodes.get(element).indices);

        if (date == 0) {
            for (int element : data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;
                if (search.contains(userDB.idHistories[index].data.getLast().id))
                    buffer.add(element);
            }
        } else {
            for (int element : data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                if (userDB.idHistories == null) continue;
                if (userDB.idHistories[index] == null) continue;

                IDHistory.Node node = userDB.idHistories[index].get(date);
                if (node == null) continue;
                if (search.contains(node.id)) buffer.add(element);
            }
        } return buffer;
    }

    public void filterName(int index, ArrayList<String> strings, int percent) throws InterruptedException {
        data = ((percent == 100) ? filterNameFull(index, strings) : filterNamePercent(index, strings, percent));
        if (data == null || data.isEmpty()) data = null;
    }

    public void removeName(int index, ArrayList<String> strings, int percent) throws InterruptedException {
        ArrayList<Integer> temp = ((percent == 100) ? filterNameFull(index, strings) : filterNamePercent(index, strings, percent));
        data.removeAll(temp);
        if (data.isEmpty()) data = null;
    }

    protected ArrayList<Integer> searchNames(ArrayList<Base.Names> names, int percent) {
        ArrayList<Integer> buffer = new ArrayList<>();
        if (percent == 100) {
            ArrayList<NamesIndices> namesIndices = new ArrayList<>();
            for (Base.Names element : names) {
                NamesIndices temp = new NamesIndices(
                        General.userStrings[UserIDEnum.FIRST_NAME.ordinal()].search(element.first()),
                        General.userStrings[UserIDEnum.LAST_NAME.ordinal()].search(element.last())
                ); if (temp.first > -1 && temp.last > -1) namesIndices.add(temp);
            }

            if (date == 0) {
                for (int element : data) {
                    UserDB userDB = General.users.get(element);
                    if (userDB == null || userDB.idHistories == null || userDB.idHistories[UserIDEnum.FIRST_NAME.ordinal()] == null || userDB.idHistories[UserIDEnum.LAST_NAME.ordinal()] == null) continue;

                    for (NamesIndices name : namesIndices) {
                        if (
                                General.userStrings[UserIDEnum.FIRST_NAME.ordinal()].nodes.get(name.first).indices.contains(userDB.idHistories[UserIDEnum.FIRST_NAME.ordinal()].data.getLast().id) &&
                                        General.userStrings[UserIDEnum.LAST_NAME.ordinal()].nodes.get(name.last).indices.contains(userDB.idHistories[UserIDEnum.LAST_NAME.ordinal()].data.getLast().id)
                        ) {
                            buffer.add(element);
                            break;
                        }
                    }
                }
            } else {
                for (int element : data) {
                    UserDB userDB = General.users.get(element);
                    if (userDB == null || userDB.idHistories == null || userDB.idHistories[UserIDEnum.FIRST_NAME.ordinal()] == null || userDB.idHistories[UserIDEnum.LAST_NAME.ordinal()] == null) continue;

                    IDHistory.Node first = userDB.idHistories[UserIDEnum.FIRST_NAME.ordinal()].get(date);
                    if (first == null) continue;
                    IDHistory.Node last = userDB.idHistories[UserIDEnum.LAST_NAME.ordinal()].get(date);
                    if (last == null) continue;

                    for (NamesIndices name : namesIndices) {
                        if (General.userStrings[UserIDEnum.FIRST_NAME.ordinal()].nodes.get(name.first).indices.contains(first.id) &&
                                General.userStrings[UserIDEnum.LAST_NAME.ordinal()].nodes.get(name.last).indices.contains(last.id)
                        ) {
                            buffer.add(element);
                            break;
                        }
                    }
                }
            }
        } else {
            if (date == 0) {

            } else {

            }
        } return buffer;
    }

    public void filterNames(ArrayList<Base.Names> names, int percent) {
        this.data = searchNames(names, percent);
        if (this.data.isEmpty()) data = null;
    }

    public void generalIds(int indexGenerate, int index, boolean generate) {
        TreeSet<Integer> buffer;
        TreeSet<Integer> temp;

        if (generate) {
            if (date == 0) {
                temp = General.generateIds[indexGenerate].get(data.getFirst());
                if (temp == null) {
                    data = null;
                    return;
                } buffer = new TreeSet<>(temp);

                for (int a = 1; a < data.size(); ++a) {
                    temp = General.generateIds[indexGenerate].get(data.get(a));
                    if (temp == null) {
                        data = null;
                        return;
                    } buffer.retainAll(temp);
                }
            } else {
                temp = Generate.getGenerateUserIds(data.getFirst(), indexGenerate, index, date);
                if (temp == null) {
                    data = null;
                    return;
                } buffer = new TreeSet<>(temp);

                for (int a = 1; a < data.size(); ++a) {
                    temp = Generate.getGenerateUserIds(data.get(a), indexGenerate, index, date);
                    if (temp == null) {
                        data = null;
                        return;
                    } buffer.retainAll(temp);
                }
            }
        } else {
            buffer = new TreeSet<>();

            if (date == 0) {
                UserDB userDB = General.users.get(data.getFirst());
                if (userDB == null || userDB.iDsHistories == null || userDB.iDsHistories[index] == null || userDB.iDsHistories[index].last.data == null) {
                    data = null;
                    return;
                }

                for (int element : userDB.iDsHistories[index].last.data)
                    buffer.add(element);

                for (int a = 1; a < data.size(); ++a) {
                    UserDB user = General.users.get(data.get(a));
                    if (user == null || user.iDsHistories == null || user.iDsHistories[index] == null || user.iDsHistories[index].last.data == null) {
                        data = null;
                        return;
                    }

                    temp = new TreeSet<>();
                    for (int element : user.iDsHistories[index].last.data)
                        temp.add(element);
                    buffer.retainAll(temp);
                }
            } else {
                UserDB userDB = General.users.get(data.getFirst());
                if (userDB == null || userDB.iDsHistories == null || userDB.iDsHistories[index] == null) {
                    data = null;
                    return;
                }

                temp = userDB.iDsHistories[index].get(date);
                if (temp == null || temp.isEmpty()) {
                    data = null;
                    return;
                }

                for (int element : userDB.iDsHistories[index].last.data)
                    buffer.add(element);

                for (int a = 1; a < data.size(); ++a) {
                    UserDB user = General.users.get(data.get(a));
                    if (user == null || user.iDsHistories == null || user.iDsHistories[index] == null) {
                        data = null;
                        return;
                    }

                    temp = user.iDsHistories[index].get(date);
                    if (temp == null || temp.isEmpty()) {
                        data = null;
                        return;
                    } buffer.retainAll(temp);
                }
            }
        }

        if (buffer.isEmpty()) {
            data = null;
            return;
        } data = new ArrayList<>(buffer);
    }

    public void out() {
        System.out.println("Users count: " + data.size());
        if (date == 0) {
            for (int element : data) {
                UserDB userDB = General.users.get(element);
                if (userDB == null) continue;
                System.out.println(Integer.toString(element) + ':' + userDB);
            }
        } else {

        }
    }
}
