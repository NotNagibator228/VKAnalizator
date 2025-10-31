package org.example.Clients;

import org.example.DataAdd;
import org.example.General;
import org.example.VKRequest.RequestsGetID;

import java.util.ArrayList;
import java.util.TreeSet;

public class ScanThreads {
    public static class ScanThread <T> extends Thread {
        public final VKToken vkToken;
        public ArrayList<Integer> ids;
        public boolean error = false;

        public void errorBlyad(int index) {
            ids = new ArrayList<>(ids.subList(index, ids.size()));
            error = true;
        }

        public ScanThread(VKToken vkToken, ArrayList<Integer> ids) {
            this.vkToken = vkToken;
            this.ids = ids;
        }
    }

    public static class ScanReturn <T> extends ScanThread <T> {
        public final TreeSet<Integer> newScan = new TreeSet<>();

        public ScanReturn(VKToken vkToken, ArrayList<Integer> ids) {
            super(vkToken, ids);
        }
    }

    public static class FriendsGetThread extends ScanReturn <RequestsGetID> {
        public FriendsGetThread(VKToken vkToken, ArrayList<Integer> ids) {
            super(vkToken, ids);
        }

        @Override
        public void run() {
            for (int index = 0; index < ids.size(); ++index) {
                if (index % 20 == 0) System.out.println("Scan friends: " + Integer.toString(index) + " in " + Integer.toString(ids.size()));
                try {
                    RequestsGetID buffer = vkToken.friendsGet(ids.get(index));
                    synchronized (General.lock) {
                        int[] array = DataAdd.addFriends(buffer);
                        if (array != null)
                            for (int id : array)
                                newScan.add(id);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (VKToken.VkError e) {
                    if (e.getErrorCode() == 9 || e.getErrorCode() == 6) {
                        errorBlyad(index);
                        return;
                    }
                }
            }
        }
    }
}
