
//Zuha Ahmad 17777

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Question2{
    public static void main(String[] args) {
        //create digraph
        Digraph digraph = new Digraph("sgb-words.txt");

        // print out graph
        //System.out.println(digraph);
        int length = 5;
        boolean exit = false;
        while (!exit) {
            String start, end;
            System.out.print("Enter two 5-letter words (length != 5 to end): ");
            Scanner sc = new Scanner(System.in);
            start = sc.next();
            end = sc.next();
            if (start.length() != length || end.length() != length) {
                exit = true;
            } else {
                digraph.BFS(start, end);
            }
        }
    }
}
class Digraph {

    // symbol table of linked lists
    private TreeMap<String, TreeSet<String>> st;

    // create an empty digraph
    public Digraph() {
        st = new TreeMap<String, TreeSet<String>>();
    }

    public Digraph(String filename) {
        st = new TreeMap<String, TreeSet<String>>();
        try {
            Scanner in = new Scanner(new File(filename));
            while (in.hasNextLine()) {
                String line = in.nextLine();
                addVertex(line);
            }
            for (String s1 : st.keySet()) {
                for (String s2 : st.keySet()) {
                    if (!s1.equals(s2)) {
                        makeEdge(s1, s2);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println(filename + " not found! Returning empty graph");
        }
    }

    //make edge if word's final four letters appear in the other words
    public void makeEdge(String v, String w) {
        int c = 0;
        String temp = w;
        for (int i = 1; i < v.length(); i++) {
            for (int j = 0; j < temp.length(); j++) {
                if ((v.charAt(i) == temp.charAt(j))) {
                    c++;
                    char[] x = temp.toCharArray();
                    x[j] = '0';
                    temp = String.valueOf(x);
                    break;
                }
            }
        }
        if (c == 4) {
            addEdge(v, w);
        }
    }

    // add v to w's list of neighbors; self-loops allowed
    public void addEdge(String v, String w) {
        if (!st.containsKey(v)) {
            addVertex(v);
        }
        if (!st.containsKey(w)) {
            addVertex(w);
        }
        st.get(v).add(w);
    }

    // add a new vertex v with no neighbors if vertex does not yet exist
    public void addVertex(String v) {
        if (!st.containsKey(v)) {
            st.put(v, new TreeSet<String>());
        }
    }

    // return the degree of vertex v
    public int degree(String v) {
        if (!st.containsKey(v)) {
            return 0;
        } else {
            return st.get(v).size();
        }
    }

    // return the array of vertices incident to v
    public Iterable<String> adjacentTo(String v) {
        if (!st.containsKey(v)) {
            return new TreeSet<String>();
        } else {
            return st.get(v);
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String v : st.keySet()) {
            s.append(v + ": ");
            for (String w : st.get(v)) {
                s.append(w + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    //prints shortest path using BFS
    public void BFS(String start, String end) {
        String[] prev = new String[st.size()];
        LinkedList<String> visited = new LinkedList<String>();
        LinkedList<String> queue = new LinkedList<String>();

        visited.add(start);
        queue.add(start);
        boolean isEnd = false;
        while (!queue.isEmpty() && !isEnd) {
            String temp = queue.remove();
            for (String s : adjacentTo(temp)) {
                if (!visited.contains(s)) {
                    visited.add(s);
                    queue.add(s);
                    prev[visited.indexOf(s)] = temp;
                }
                if (s.equals(end)) {
                    prev[visited.indexOf(s)] = temp;
                    isEnd = true;
                }
            }

        }
        if (!isEnd) {
            System.out.println("There is no path from " + start + " to " + end);
        } else {
            String path = end;
            int i = visited.indexOf(end);
            while (!prev[i].equals(start)) {
                if (prev[i] != null) {
                    path = prev[i] + " -> " + path;
                    i = visited.indexOf(prev[i]);
                } else {
                    path = null;
                    break;
                }
            }
            if (prev[i].equals(start)) {
                path = start + " -> " + path;
                System.out.println(path);
            }
        }
    }
}
