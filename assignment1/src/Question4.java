
//Zuha Ahmad 17777

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Question4 {

    String finalCycle = null;
    boolean stop = false;
    
    //finds cycle in undirected graph and prints
    public void isCycle(Graph g, String start, String v, LinkedList<String> visited, String cycle) {
        if (!stop) {
            visited.add(v);
            cycle = cycle + " " + v;
//            System.out.println(cycle);
            for (String x : g.adjacentTo(v)) {
                if (!visited.contains(x)) {
                    isCycle(g, start, x, visited, cycle);
                } else if (x.equals(start)) {
                    if (cycle.length() > 12) {
                        finalCycle = cycle + " " + start;
                        stop = true;
                        return;
                    }
                }
            }
        }
    }

    //finds cycle in directed graph and prints
    public void isCycle(Digraph g, String start, String v, LinkedList<String> visited, String cycle) {
        if (!stop) {
            visited.add(v);
            cycle = cycle + " " + v;
            //System.out.println(cycle);
            for (String x : g.adjacentTo(v)) {
                if (!visited.contains(x)) {
                    isCycle(g, start, x, visited, cycle);
                } else if (x.equals(start)) {
                    if (cycle.length() > 12) {
                        finalCycle = cycle + " " + start;
                        stop = true;
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        Question4 q = new Question4();

        boolean exit = false;
        while (!exit) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter word (length != 5 to end): ");
            String word = sc.next();

            if (word.length() == 5) {
                System.out.print("Enter g for graph or d for digraph : ");
                String g = sc.next();
                if (g.equals("g")) {
                    Graph d = new Graph("sgb-words.txt");
                    if (d.hasVertex(word)) {
                        LinkedList<String> v = new LinkedList();
                        q.isCycle(d, word, word, v, "");
                        System.out.println("here");
                        if (q.finalCycle != null) {
                            System.out.println("cycle: " + q.finalCycle);
                            q.finalCycle = null;
                            q.stop = false;
                        } else {
                            System.out.println("No cycle containing this word found");
                        }
                    } else {
                        System.out.println("word not found");
                    }
                } else if (g.equals("d")) {
                    Digraph d = new Digraph("sgb-words.txt");
                    if (d.hasVertex(word)) {
                        LinkedList<String> v = new LinkedList();
                        q.isCycle(d, word, word, v, "");
                        if (q.finalCycle != null) {
                            System.out.println("cycle: " + q.finalCycle);
                            q.finalCycle = null;
                            q.stop = false;
                        } else {
                            System.out.println("No cycle containing this word found");
                        }
                    } else {
                        System.out.println("word not found");
                    }
                } else {
                    System.out.println("Enter g or d");
                }
            } else {
                exit = true;
            }
        }
    }
}

class Graph {

    // symbol table: key = string vertex, value = set of neighboring vertices
    private TreeMap<String, TreeSet<String>> st;

    // number of edges
    private int E;

    /**
     * Initializes an empty graph with no vertices or edges.
     */
    public Graph() {
        st = new TreeMap<String, TreeSet<String>>();
    }

    /**
     * Initializes a graph from the specified file, using the specified
     * delimiter.
     *
     * @param filename the name of the file
     */
    public Graph(String filename) {
        st = new TreeMap<String, TreeSet<String>>();
        try {
            Scanner in = new Scanner(new File(filename));
            while (in.hasNextLine()) {
                String line = in.nextLine();
                addVertex(line);
                Iterator<String> iter = st.keySet().iterator();
                while (iter.hasNext()) {
                    String s = iter.next();
                    if (!s.equals(line)) {
                        makeEdge(s, line);
                    }
                }

            }
        } catch (FileNotFoundException ex) {
            System.err.println(filename + " not found! Returning empty graph");
        }
    }

    //makes edge is words differ at 1 position only
    public void makeEdge(String v, String w) {
        int count = 0;

        for (int i = 0; i < v.length(); i++) {
            if ((v.charAt(i) != w.charAt(i))) {
                count++;
            }
        }
        if (count == 1) {
            addEdge(v, w);
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

    //prints shortest path using bfs
    public void BFS(String start, String end) {
        String[] prev = new String[V()];
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


class Digraph {

    // symbol table of linked lists
    TreeMap<String, TreeSet<String>> st;

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

    public boolean hasVertex(String v) {
        return st.containsKey(v);
    }

    // throw an exception if v is not a vertex
    private void validateVertex(String v) {
        if (!hasVertex(v)) {
            throw new IllegalArgumentException(v + " is not a vertex");
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
}
