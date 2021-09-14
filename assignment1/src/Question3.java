
//Zuha Ahmad 17777

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Question3 {
    public static void main(String[] args) {
        //create digraph
        Digraph digraph = new Digraph("sgb-words.txt");
        //find SCC
        digraph.SCC();
    }
}

class Digraph {

    // symbol table of linked lists
    TreeMap<String, TreeSet<String>> st;
    Stack stack = new Stack();//postnumber
    int count;//keep track of largest SCC

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

    //recursive function for DFS and post number tracking using stack
    public void DFS(String v, LinkedList<String> visited) {
        visited.add(v);
        for (String s : adjacentTo(v)) {
            if (!visited.contains(s)) {
                DFS(s, visited);
            }
        }
        stack.push(v);
    }

    //DFS for printing SCC
    public void DFS2(String v, LinkedList<String> visited) {
        visited.add(v);
        count++;
        System.out.print(v + " ");
        for (String s : adjacentTo(v)) {
            if (!visited.contains(s)) {
                DFS2(s, visited);
            }
        }
    }

    public Digraph transpose() {
        Digraph d = new Digraph();
        for (String s1 : st.keySet()) {
            d.addVertex(s1);
            for (String s2 : adjacentTo(s1)) {
                d.addVertex(s2);
                d.addEdge(s2, s1);
            }
        }
        return d;
    }

    //prints all SCC and size of largest SCC
    public void SCC() {
        LinkedList<String> visited = new LinkedList<String>();
        LinkedList<String> visited1 = new LinkedList<String>();
        int maxComponentSize = 0;
        Digraph t = transpose();
        for (String s : t.st.keySet()) {
            if (!visited.contains(s)) {
                t.DFS(s, visited);
            }
        }
        System.out.println("STRONGLY CONNECTED COMPONENTS: ");
        int num = 0;
        while (!t.stack.isEmpty()) {
            String v = (String) t.stack.pop();
            if (!visited1.contains(v)) {
                num++;
                DFS2(v, visited1);
                if (count > maxComponentSize) {
                    maxComponentSize = count;
                }
                count = 0;
                System.out.println("");
            }
        }
        System.out.println("\n\nSIZE OF LARGEST COMPONENT: " + maxComponentSize);
        System.out.println("\nNumber of COMPONENTs: " + num);
    }
}
