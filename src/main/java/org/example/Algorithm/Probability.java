package org.example.Algorithm;

public class Probability {
    public static class Node implements Comparable<Node> {
        public final int percent;
        public final int id;

        public Node(int percent, int id) {
            this.percent = percent;
            this.id = id;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.percent, o.percent);
        }
    }
}
