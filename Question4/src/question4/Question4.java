/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package question4;

/**
 *
 * @author win
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Question4 {

    static String uCycle;
    static LinkedList<String> uVisited;

    //searches for cycle in digraph with minimum 3 distinct vertices
    public static void DFS(String start, String end, String cycle, Digraph4 graph) {
        if (uCycle != null) {
            return;
        }
        uVisited.add(start);
        cycle = new String(cycle + " " + start);
        for (String s : graph.adjacentTo(start)) {
            if (!uVisited.contains(s)) {
                DFS(s, end, cycle, graph);
            } else if (s.equals(end)) {
                String[] cycleWords = cycle.split(" ");
                if (cycleWords.length > 3) {
                    uCycle = cycle;
                    return;
                }
            }
        }
    }

    public static void findCyclesDirected(String start, Digraph4 graph) {
        uVisited = new LinkedList<String>();
        uCycle = null;
        DFS(start, start, "", graph);
        if (uCycle != null) {
            System.out.println(uCycle.trim() + " " + start + "\n");
        } else {
            System.out.println("There is no cycle containing " + start + ".\n");
        }
    }

    //searches for cycle in graph with minimum 3 distinct vertices
    public static void DFS(String start, String end, String cycle, Graph4 graph) {
        if (uCycle != null) {
            return;
        }
        uVisited.add(start);
        cycle = new String(cycle + " " + start);
        for (String s : graph.adjacentTo(start)) {
            if (!uVisited.contains(s)) {
                DFS(s, end, cycle, graph);
            } else if (s.equals(end)) {
                String[] cycleWords = cycle.split(" ");
                if (cycleWords.length > 3) {
                    uCycle = cycle;
                    return;
                }
            }
        }
    }

    public static void findCyclesUndirected(String start, Graph4 graph) {
        uVisited = new LinkedList<String>();
        uCycle = null;
        DFS(start, start, "", graph);
        if (uCycle != null) {
            System.out.println(uCycle.trim() + " " + start + "\n");
        } else {
            System.out.println("There is no cycle containing " + start + ".\n");
        }
    }

    public static void main(String[] args) {
        try {
            Graph4 graph = new Graph4("sgb-words.txt");
            Digraph4 digraph = new Digraph4("sgb-words.txt");
            Scanner sc = new Scanner(System.in);
            
            while (true) {
                System.out.print("Enter a word (length != 5 to end): ");
                String word1 = sc.nextLine();
                word1 = word1.trim();

                if (word1.length() != 5) {
                    System.out.println("Not a 5-letter word!");
                    break;
                } else {
                    System.out.print("Do you want do use graph(1) or digraph(2)?\nEnter 1 or 2: ");
                    String word2 = sc.nextLine();
                    word2 = word2.trim();
                    if (word2.equals("1")) {
                        Question4.findCyclesUndirected(word1, graph);
                    } else if (word2.equals("2")) {
                        Question4.findCyclesDirected(word1, digraph);
                    } else {
                        System.out.println("Incorrect graph type, enter 1 or 2!");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

class Digraph4 {

    // symbol table of linked lists
    private TreeMap<String, TreeSet<String>> st;
    int count = 0;

    // create an empty digraph
    public Digraph4() {
        st = new TreeMap<String, TreeSet<String>>();
    }

    //reads from file and makes graph. Adds directed edge between vertices if 
    //last 4 letters of first word are present in second word. repitition of
    //letters is also countered by replacing the letter in second string by '.'
    public Digraph4(String filename) {
        st = new TreeMap<String, TreeSet<String>>();
        try {
            Scanner in = new Scanner(new File(filename));
            while (in.hasNextLine()) {
                String line = in.nextLine();
                addVertex(line);
                for (String str : st.keySet()) {
                    if (str != line) {
                        int n1 = 0;
                        String str1 = new String(str);
                        StringBuilder newStr1 = new StringBuilder(str);
                        int n2 = 0;
                        String str2 = new String(line);
                        StringBuilder newStr2 = new StringBuilder(line);
                        for (int k = 1; k < 5; k++) {
                            str1 = newStr1.toString();
                            int index = str1.indexOf(line.charAt(k));
                            if (index != -1) {
                                newStr1.setCharAt(index, '.');
                                n1++;
                            }
                            str2 = newStr2.toString();
                            index = str2.indexOf(str.charAt(k));
                            if (index != -1) {
                                newStr2.setCharAt(index, '.');
                                n2++;
                            }
                        }
                        if (n1 == 4) {
                            addEdge(line, str);
                        }
                        if (n2 == 4) {
                            addEdge(str, line);
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println(filename + " not found! Returning empty graph");
        }
    }

    // add w to v's list of neighbors; self-loops allowed
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

    /**
     * Returns the vertices in this graph.
     *
     * @return the set of vertices in this graph
     */
    public Iterable<String> vertices() {
        return st.keySet();
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

}

class Graph4 {

    // symbol table: key = string vertex, value = set of neighboring vertices
    private TreeMap<String, TreeSet<String>> st;

    // number of edges
    private int E;

    /**
     * Initializes an empty graph with no vertices or edges.
     */
    public Graph4() {
        st = new TreeMap<String, TreeSet<String>>();
    }

    /**
     * Initializes a graph from the specified file, and adds edges between
     * vertices if they have 4 same characters at same positions.
     *
     * @param filename the name of the file
     */
    public Graph4(String filename) {
        st = new TreeMap<String, TreeSet<String>>();
        try {
            Scanner in = new Scanner(new File(filename));
            while (in.hasNextLine()) {
                String line = in.nextLine();
                addVertex(line);
                for (String str : vertices()) {
                    if (str != line) {
                        int n = 0;
                        for (int k = 0; k < 5; k++) {
                            if (str.charAt(k) == line.charAt(k)) {
                                n++;
                            }
                        }
                        if (n == 4) {
                            addEdge(str, line);
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println(filename + " not found! Returning empty graph");
        }
    }

    /**
     * Returns the number of vertices in this graph.
     *
     * @return the number of vertices in this graph
     */
    public int V() {
        return st.size();
    }

    /**
     * Returns the number of edges in this graph.
     *
     * @return the number of edges in this graph
     */
    public int E() {
        return E;
    }

    // throw an exception if v is not a vertex
    private void validateVertex(String v) {
        if (!hasVertex(v)) {
            throw new IllegalArgumentException(v + " is not a vertex");
        }
    }

    /**
     * Returns the degree of vertex v in this graph.
     *
     * @param v the vertex
     * @return the degree of {@code v} in this graph
     * @throws IllegalArgumentException if {@code v} is not a vertex in this
     * graph
     */
    public int degree(String v) {
        validateVertex(v);
        return st.get(v).size();
    }

    /**
     * Adds the edge v-w to this graph (if it is not already an edge).
     *
     * @param v one vertex in the edge
     * @param w the other vertex in the edge
     */
    public void addEdge(String v, String w) {
        if (!hasVertex(v)) {
            addVertex(v);
        }
        if (!hasVertex(w)) {
            addVertex(w);
        }
        if (!hasEdge(v, w)) {
            E++;
        }
        st.get(v).add(w);
        st.get(w).add(v);
    }

    /**
     * Adds vertex v to this graph (if it is not already a vertex).
     *
     * @param v the vertex
     */
    public void addVertex(String v) {
        if (!hasVertex(v)) {
            st.put(v, new TreeSet<String>());
        }
    }

    /**
     * Returns the vertices in this graph.
     *
     * @return the set of vertices in this graph
     */
    public Iterable<String> vertices() {
        return st.keySet();
    }

    /**
     * Returns the set of vertices adjacent to v in this graph.
     *
     * @param v the vertex
     * @return the set of vertices adjacent to vertex {@code v} in this graph
     * @throws IllegalArgumentException if {@code v} is not a vertex in this
     * graph
     */
    public Iterable<String> adjacentTo(String v) {
        validateVertex(v);
        return st.get(v);
    }

//    public TreeSet<String> neighbourlist(String v){
//        validateVertex(v);
//        return st.get(v);
//    }
    /**
     * Returns true if v is a vertex in this graph.
     *
     * @param v the vertex
     * @return {@code true} if {@code v} is a vertex in this graph,
     * {@code false} otherwise
     */
    public boolean hasVertex(String v) {
        return st.containsKey(v);
    }

    /**
     * Returns true if v-w is an edge in this graph.
     *
     * @param v one vertex in the edge
     * @param w the other vertex in the edge
     * @return {@code true} if {@code v-w} is a vertex in this graph,
     * {@code false} otherwise
     * @throws IllegalArgumentException if either {@code v} or {@code w} is not
     * a vertex in this graph
     */
    public boolean hasEdge(String v, String w) {
        validateVertex(v);
        validateVertex(w);
        return st.get(v).contains(w);
    }

    /**
     * Returns a string representation of this graph.
     *
     * @return string representation of this graph
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String v : st.keySet()) {
            s.append(v + ": ");
            for (String w : st.get(v)) {
                s.append(w + " ");
            }
            s.append('\n');
        }
        return s.toString();
    }

}
