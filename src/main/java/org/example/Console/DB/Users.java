package org.example.Console.DB;

import org.example.Algorithm.Generate;
import org.example.Algorithm.IDUsersGenerate;
import org.example.Colors;
import org.example.Console.Base;
import org.example.Data.IDHistory;
import org.example.Data.IDsHistory;
import org.example.Enum.GenerateIDsEnum;
import org.example.Enum.UserIDEnum;
import org.example.Enum.UserIDsEnum;
import org.example.General;
import org.example.Utils;
import org.example.VKData.UserDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

    protected ArrayList<Integer> searchNames(ArrayList<Base.Names> names, int percent) throws InterruptedException {
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
            TreeSet<Integer> firstNodes = new TreeSet<>();
            TreeSet<Integer> lastNodes = new TreeSet<>();

            if (date == 0) {
                for (int userId : data) {
                    UserDB userDB = General.users.get(userId);
                    if (userDB.idHistories == null || userDB.idHistories[UserIDEnum.FIRST_NAME.ordinal()] == null || userDB.idHistories[UserIDEnum.LAST_NAME.ordinal()] == null) continue;

                    firstNodes.add(userDB.idHistories[UserIDEnum.FIRST_NAME.ordinal()].data.getLast().id);
                    lastNodes.add(userDB.idHistories[UserIDEnum.LAST_NAME.ordinal()].data.getLast().id);
                }
            } else {
                for (int userId : data) {
                    UserDB userDB = General.users.get(userId);
                    if (userDB.idHistories == null || userDB.idHistories[UserIDEnum.FIRST_NAME.ordinal()] == null || userDB.idHistories[UserIDEnum.LAST_NAME.ordinal()] == null) continue;

                    IDHistory.Node firstNode = userDB.idHistories[UserIDEnum.FIRST_NAME.ordinal()].get(date);
                    if (firstNode == null) continue;
                    IDHistory.Node lastNode = userDB.idHistories[UserIDEnum.LAST_NAME.ordinal()].get(date);
                    if (lastNode == null) continue;

                    firstNodes.add(firstNode.id);
                    lastNodes.add(lastNode.id);
                }
            }

            ArrayList<Integer> firstIndices = General.userStrings[UserIDEnum.FIRST_NAME.ordinal()].getIndices(firstNodes);
            ArrayList<Integer> lastIndices = General.userStrings[UserIDEnum.LAST_NAME.ordinal()].getIndices(lastNodes);
            ArrayList<NamesIndicesSet> namesIndicesSets = new ArrayList<>();

            for (Base.Names element : names) {
                ArrayList<Integer> first = General.userStrings[UserIDEnum.FIRST_NAME.ordinal()].search(element.first(), percent, General.threadCount, firstIndices);
                ArrayList<Integer> last = General.userStrings[UserIDEnum.LAST_NAME.ordinal()].search(element.last(), percent, General.threadCount, lastIndices);
                if (first.isEmpty() || last.isEmpty()) continue;

                NamesIndicesSet temp = new NamesIndicesSet();
                for (int index : first)
                    temp.first.addAll(General.userStrings[UserIDEnum.FIRST_NAME.ordinal()].nodes.get(index).indices);
                for (int index : last)
                    temp.last.addAll(General.userStrings[UserIDEnum.LAST_NAME.ordinal()].nodes.get(index).indices);
                namesIndicesSets.add(temp);
            } if (namesIndicesSets.isEmpty()) return buffer;

            if (date == 0) {
                for (int userId : data) {
                    UserDB userDB = General.users.get(userId);
                    if (userDB == null || userDB.idHistories == null || userDB.idHistories[UserIDEnum.FIRST_NAME.ordinal()] == null || userDB.idHistories[UserIDEnum.LAST_NAME.ordinal()] == null) continue;

                    for (NamesIndicesSet element : namesIndicesSets) {
                        if (element.first.contains(userDB.idHistories[UserIDEnum.FIRST_NAME.ordinal()].data.getLast().id) && element.last.contains(userDB.idHistories[UserIDEnum.LAST_NAME.ordinal()].data.getLast().id)) {
                            buffer.add(userId);
                            break;
                        }
                    }
                }
            } else {
                for (int userId : data) {
                    UserDB userDB = General.users.get(userId);
                    if (userDB == null || userDB.idHistories == null || userDB.idHistories[UserIDEnum.FIRST_NAME.ordinal()] == null || userDB.idHistories[UserIDEnum.LAST_NAME.ordinal()] == null) continue;

                    IDHistory.Node first = userDB.idHistories[UserIDEnum.FIRST_NAME.ordinal()].get(date);
                    if (first == null) continue;
                    IDHistory.Node last = userDB.idHistories[UserIDEnum.LAST_NAME.ordinal()].get(date);
                    if (last == null) continue;

                    for (NamesIndicesSet element : namesIndicesSets) {
                        if (element.first.contains(first.id) && element.last.contains(last.id)) {
                            buffer.add(userId);
                            break;
                        }
                    }
                }
            }
        } return buffer;
    }

    public void filterNames(ArrayList<Base.Names> names, int percent) throws InterruptedException {
        this.data = searchNames(names, percent);
        if (this.data.isEmpty()) data = null;
    }

    public void removeNames(ArrayList<Base.Names> names, int percent) throws InterruptedException {
        data.removeAll(searchNames(names, percent));
        if (data.isEmpty()) data = null;
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

    public void outHistoryIds(int index) {
        switch (index) {
            case 0 -> {
                if (date == 0) {
                    for (int element : data) {
                        UserDB userDB = General.users.get(element);
                        if (userDB == null || userDB.iDsHistories == null || userDB.iDsHistories[0] == null) continue;
                        System.out.println("History fiends of: " + userDB);

                        if (userDB.iDsHistories[0].deleted == null) {
                            for (IDsHistory.Node node : userDB.iDsHistories[0].added) {
                                System.out.println(Colors.ANSI_BLUE + "added :" + ((node.data == null) ? "0" : Integer.toString(node.data.length)) + " in " + new Date(node.date).toString() + Colors.ANSI_RESET);

                                if (node.data != null)
                                    for (int id : node.data)
                                        System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));
                            }
                        } else {
                            Iterator<IDsHistory.Node> addedIterator = userDB.iDsHistories[0].added.iterator();
                            Iterator<IDsHistory.Node> deletedIterator = userDB.iDsHistories[0].deleted.iterator();
                            IDsHistory.Node addedNode = addedIterator.next();
                            IDsHistory.Node deletedNode = deletedIterator.next();

                            while (addedNode != null && deletedNode != null) {
                                if (addedNode.date < deletedNode.date) {
                                    System.out.println(Colors.ANSI_BLUE + "added :" + ((addedNode.data == null) ? "0" : Integer.toString(addedNode.data.length)) + " in " + new Date(addedNode.date).toString() + Colors.ANSI_RESET);
                                    if (addedNode.data != null)
                                        for (int id : addedNode.data)
                                            System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));
                                    addedNode = ((addedIterator.hasNext()) ? addedIterator.next() : null);
                                } else if (addedNode.date > deletedNode.date) {
                                    System.out.println(Colors.ANSI_BLUE + "deleted: " + Integer.toString(deletedNode.data.length) + " in " + new Date(deletedNode.date).toString() + Colors.ANSI_RESET);
                                    for (int id : deletedNode.data)
                                        System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));
                                    deletedNode = ((deletedIterator.hasNext()) ? deletedIterator.next() : null);
                                } else {
                                    System.out.println(Colors.ANSI_BLUE + "added :" + ((addedNode.data == null) ? "0" : Integer.toString(addedNode.data.length)) + " in " + new Date(addedNode.date).toString() + Colors.ANSI_RESET);
                                    if (addedNode.data != null)
                                        for (int id : addedNode.data)
                                            System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));

                                    System.out.println(Colors.ANSI_BLUE + "deleted: " + Integer.toString(deletedNode.data.length) + " in " + new Date(deletedNode.date).toString() + Colors.ANSI_RESET);
                                    for (int id : deletedNode.data)
                                        System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));

                                    addedNode = ((addedIterator.hasNext()) ? addedIterator.next() : null);
                                    deletedNode = ((deletedIterator.hasNext()) ? deletedIterator.next() : null);
                                }
                            }

                            while (addedNode != null) {
                                System.out.println(Colors.ANSI_BLUE + "added :" + ((addedNode.data == null) ? "0" : Integer.toString(addedNode.data.length)) + " in " + new Date(addedNode.date).toString() + Colors.ANSI_RESET);
                                if (addedNode.data != null)
                                    for (int id : addedNode.data)
                                        System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));
                                addedNode = ((addedIterator.hasNext()) ? addedIterator.next() : null);
                            }

                            while (deletedNode != null) {
                                System.out.println(Colors.ANSI_BLUE + "deleted: " + Integer.toString(deletedNode.data.length) + " in " + new Date(deletedNode.date).toString() + Colors.ANSI_RESET);
                                for (int id : deletedNode.data)
                                    System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));
                                deletedNode = ((deletedIterator.hasNext()) ? deletedIterator.next() : null);
                            }
                        }
                    }
                } else {
                    for (int element : data) {
                        UserDB userDB = General.users.get(element);
                        if (userDB == null || userDB.iDsHistories == null || userDB.iDsHistories[0] == null) continue;
                        System.out.println("History fiends of: " + userDB.toString(date));

                        if (userDB.iDsHistories[0].deleted == null) {
                            for (IDsHistory.Node node : userDB.iDsHistories[0].added) {
                                System.out.println(Colors.ANSI_BLUE + "added :" + ((node.data == null) ? "0" : Integer.toString(node.data.length)) + " in " + new Date(node.date).toString() + Colors.ANSI_RESET);

                                if (node.data != null)
                                    for (int id : node.data) {
                                        userDB = General.users.get(id);
                                        if (userDB == null) System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));
                                        else System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id).toString(date));
                                    }

                            }
                        } else {
                            Iterator<IDsHistory.Node> addedIterator = userDB.iDsHistories[0].added.iterator();
                            Iterator<IDsHistory.Node> deletedIterator = userDB.iDsHistories[0].deleted.iterator();
                            IDsHistory.Node addedNode = addedIterator.next();
                            IDsHistory.Node deletedNode = deletedIterator.next();

                            while (addedNode != null && deletedNode != null) {
                                if (addedNode.date < deletedNode.date) {
                                    System.out.println(Colors.ANSI_BLUE + "added :" + ((addedNode.data == null) ? "0" : Integer.toString(addedNode.data.length)) + " in " + new Date(addedNode.date).toString() + Colors.ANSI_RESET);
                                    if (addedNode.data != null)
                                        for (int id : addedNode.data) {
                                            userDB = General.users.get(id);
                                            if (userDB == null) System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));
                                            else System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id).toString(date));
                                        }
                                    addedNode = ((addedIterator.hasNext()) ? addedIterator.next() : null);
                                } else if (addedNode.date > deletedNode.date) {
                                    System.out.println(Colors.ANSI_BLUE + "deleted: " + Integer.toString(deletedNode.data.length) + " in " + new Date(deletedNode.date).toString() + Colors.ANSI_RESET);
                                    for (int id : deletedNode.data) {
                                        userDB = General.users.get(id);
                                        if (userDB == null) System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));
                                        else System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id).toString(date));
                                    } deletedNode = ((deletedIterator.hasNext()) ? deletedIterator.next() : null);
                                } else {
                                    System.out.println(Colors.ANSI_BLUE + "added :" + ((addedNode.data == null) ? "0" : Integer.toString(addedNode.data.length)) + " in " + new Date(addedNode.date).toString() + Colors.ANSI_RESET);
                                    if (addedNode.data != null)
                                        for (int id : addedNode.data) {
                                            userDB = General.users.get(id);
                                            if (userDB == null) System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));
                                            else System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id).toString(date));
                                        }

                                    System.out.println(Colors.ANSI_BLUE + "deleted: " + Integer.toString(deletedNode.data.length) + " in " + new Date(deletedNode.date).toString() + Colors.ANSI_RESET);
                                    for (int id : deletedNode.data) {
                                        userDB = General.users.get(id);
                                        if (userDB == null) System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));
                                        else System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id).toString(date));
                                    }

                                    addedNode = ((addedIterator.hasNext()) ? addedIterator.next() : null);
                                    deletedNode = ((deletedIterator.hasNext()) ? deletedIterator.next() : null);
                                }
                            }

                            while (addedNode != null) {
                                System.out.println(Colors.ANSI_BLUE + "added :" + ((addedNode.data == null) ? "0" : Integer.toString(addedNode.data.length)) + " in " + new Date(addedNode.date).toString() + Colors.ANSI_RESET);
                                if (addedNode.data != null)
                                    for (int id : addedNode.data) {
                                        userDB = General.users.get(id);
                                        if (userDB == null) System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));
                                        else System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id).toString(date));
                                    }
                                addedNode = ((addedIterator.hasNext()) ? addedIterator.next() : null);
                            }

                            while (deletedNode != null) {
                                System.out.println(Colors.ANSI_BLUE + "deleted: " + Integer.toString(deletedNode.data.length) + " in " + new Date(deletedNode.date).toString() + Colors.ANSI_RESET);
                                for (int id : deletedNode.data) {
                                    userDB = General.users.get(id);
                                    if (userDB == null) System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id));
                                    else System.out.println("\t" + Integer.toString(id) + ":" + General.users.get(id).toString(date));
                                } deletedNode = ((deletedIterator.hasNext()) ? deletedIterator.next() : null);
                            }
                        }
                    }
                }
            }
            case 1 -> {

            }
            case 2 -> {

            }
        }
    }

    private static class IdGenerateGenerate extends Thread {
        public final int begin, end;
        public final int id, index;

        public final ArrayList<Integer> data;
        public final ArrayList<Integer> buffer = new ArrayList<>();

        public IdGenerateGenerate(int begin, int end, int id, int index, ArrayList<Integer> data) {
            this.begin = begin;
            this.end = end;
            this.id = id;
            this.index = index;
            this.data = data;
        }

        @Override
        public void run() {
            for (int index = begin; index < end; ++index) {
                int result = IDUsersGenerate.getGenerateUserIdGenerate(data.get(index), GenerateIDsEnum.FRIENDS.ordinal(), this.index);
                if (result == id) buffer.add(data.get(index));
            }
        }
    }

    private static class IdGenerateGenerateDate extends IdGenerateGenerate {
        public final long date;

        public IdGenerateGenerateDate(int begin, int end, int id, int index, ArrayList<Integer> data, long date) {
            super(begin, end, id, index, data);
            this.date = date;
        }

        @Override
        public void run() {
            for (int index = begin; index < end; ++index) {
                int result = IDUsersGenerate.getGenerateUserIdGenerate(data.get(index), GenerateIDsEnum.FRIENDS.ordinal(), this.index, date);
                if (result == id) buffer.add(data.get(index));
            }
        }
    }

    private static class IdGenerate extends IdGenerateGenerate {
        public IdGenerate(int begin, int end, int id, int index, ArrayList<Integer> data) {
            super(begin, end, id, index, data);
        }

        @Override
        public void run() {
            for (int index = begin; index < end; ++index) {
                int result = IDUsersGenerate.getGenerateUserId(data.get(index), UserIDsEnum.FRIENDS.ordinal(), this.index);
                if (result == id) buffer.add(data.get(index));
            }
        }
    }

    private static class IdGenerateDate extends IdGenerateGenerateDate {
        public IdGenerateDate(int begin, int end, int id, int index, ArrayList<Integer> data, long date) {
            super(begin, end, id, index, data, date);
        }

        @Override
        public void run() {
            for (int index = begin; index < end; ++index) {
                int result = IDUsersGenerate.getGenerateUserId(data.get(index), UserIDsEnum.FRIENDS.ordinal(), this.index, date);
                if (result == id) buffer.add(data.get(index));
            }
        }
    }

    public static class DBThread {
        public final int count, fix, thread;
        public int begin = 0, end = 0;

        public DBThread(int size, int thread) {
            this.thread = Math.min(size, thread);
            this.count = size / this.thread;
            this.fix = size % thread;
        }
    }

    public ArrayList<Integer> idGenerate(int index, int id, boolean generate, int level) throws InterruptedException {
        ArrayList<Integer> buffer = new ArrayList<>();
        if (date == 0) {
            if (generate) {
                if (level == 2) {
                    DBThread db = new DBThread(data.size(), General.threadCount);
                    IdGenerateGenerate[] idGenerateGenerates = new IdGenerateGenerate[db.thread];

                    for (int a = 0; a < db.thread; ++a) {
                        db.end += db.count;
                        if (a <  db.fix) ++db.end;
                        idGenerateGenerates[a] = new IdGenerateGenerate(db.begin, db.end, id, index, data);
                        db.begin = db.end;
                    }

                    for (IdGenerateGenerate element : idGenerateGenerates)
                        element.start();

                    for (IdGenerateGenerate element : idGenerateGenerates) {
                        element.join();
                        buffer.addAll(element.buffer);
                    }
                } else {

                }
            } else {
                if (level == 2) {
                    DBThread db = new DBThread(data.size(), General.threadCount);
                    IdGenerate[] idGenerateGenerates = new IdGenerate[db.thread];

                    for (int a = 0; a < db.thread; ++a) {
                        db.end += db.count;
                        if (a <  db.fix) ++db.end;
                        idGenerateGenerates[a] = new IdGenerate(db.begin, db.end, id, index, data);
                        db.begin = db.end;
                    }

                    for (IdGenerateGenerate element : idGenerateGenerates)
                        element.start();

                    for (IdGenerateGenerate element : idGenerateGenerates) {
                        element.join();
                        buffer.addAll(element.buffer);
                    }
                } else {

                }
            }
        } else {
            if (generate) {
                if (level == 2) {
                    DBThread db = new DBThread(data.size(), General.threadCount);
                    IdGenerateGenerateDate[] idGenerateGenerateDate = new IdGenerateGenerateDate[db.thread];

                    for (int a = 0; a < idGenerateGenerateDate.length; ++a) {
                        db.end += db.count;
                        if (a < db.fix) ++db.end;
                        idGenerateGenerateDate[a] = new IdGenerateGenerateDate(db.begin, db.end, id, index, data, date);
                        db.begin = db.end;
                    }

                    for (IdGenerateGenerateDate element : idGenerateGenerateDate)
                        element.start();

                    for (IdGenerateGenerateDate element : idGenerateGenerateDate) {
                        element.join();
                        buffer.addAll(element.buffer);
                    }
                } else {

                }
            } else {
                if (level == 2) {
                    DBThread db = new DBThread(data.size(), General.threadCount);
                    IdGenerateDate[] idGenerateDates = new IdGenerateDate[db.thread];

                    for (int a = 0; a < idGenerateDates.length; ++a) {
                        db.end = db.count;
                        if (a < db.fix) ++db.end;
                        idGenerateDates[a] = new IdGenerateDate(db.begin, db.end, id, index, data, date);
                        db.begin = db.end;
                    }

                    for (IdGenerateDate element : idGenerateDates)
                        element.start();

                    for (IdGenerateDate element : idGenerateDates) {
                        element.join();
                        buffer.addAll(element.buffer);
                    }
                } else {

                }
            }
        } return buffer;
    }

    public void filterIdGenerate(int index, int id, boolean generate, int level) throws InterruptedException {
        this.data = idGenerate(index, id, generate, level);
        if (this.data.isEmpty()) this.data = null;
    }

    public void removeIdGenerate(int index, int id, boolean generate, int level) throws InterruptedException {
        this.data.removeAll(idGenerate(index, id, generate, level));
        if (this.data.isEmpty()) this.data = null;
    }

    @Override
    public void outNoDate() {
        for (int element : data) {
            UserDB userDB = General.users.get(element);
            System.out.println(Integer.toString(element) + ':' + userDB);
        }
    }

    @Override
    public void outDate() {
        for (int element : data) {
            UserDB userDB = General.users.get(element);
            System.out.println(Integer.toString(element) + ':' + ((userDB == null) ? "None" : userDB.toString(date)));
        }
    }
}
