
//Zuha Ahmad 17777

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Question1{
    public static void main(String[] args) {
        //create graph
        Graph graph = new Graph("sgb-words.txt");
        // print out graph
        //System.out.println(graph);

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
                graph.BFS(start, end);
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
