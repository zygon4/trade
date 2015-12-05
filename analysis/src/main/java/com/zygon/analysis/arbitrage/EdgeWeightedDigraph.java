package com.zygon.analysis.arbitrage;

import java.util.Random;
import java.util.Stack;
import org.apache.commons.math3.random.JDKRandomGenerator;

/**
 *
 * @author zygon
 */
public class EdgeWeightedDigraph {

    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;                // number of vertices in this digraph
    private int E;                      // number of edges in this digraph
    private Bag<DirectedEdge>[] adj;    // adj[v] = adjacency list for vertex v
    private int[] indegree;             // indegree[v] = indegree of vertex v

    /**
     * Initializes an empty edge-weighted digraph with <tt>V</tt> vertices and 0
     * edges.
     *
     * @param V the number of vertices
     * @throws IllegalArgumentException if <tt>V</tt> < 0
     */
    public EdgeWeightedDigraph(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        }
        this.V = V;
        this.E = 0;
        this.indegree = new int[V];
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<DirectedEdge>();
        }
    }

    /**
     * Initializes a random edge-weighted digraph with <tt>V</tt> vertices and
     * <em>E</em> edges.
     *
     * @param V the number of vertices
     * @param E the number of edges
     * @throws IllegalArgumentException if <tt>V</tt> < 0
     * @throws IllegalArgumentException if <tt>E</tt> < 0
     */
    public EdgeWeightedDigraph(int V, int E) {
        this(V);
        if (E < 0) {
            throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");
        }
        Random rand = new JDKRandomGenerator();
        for (int i = 0; i < E; i++) {
            int v = rand.nextInt(V);
            int w = rand.nextInt(V);
            double weight = .01 * rand.nextInt(100);
            DirectedEdge e = new DirectedEdge(v, w, weight);
            addEdge(e);
        }
    }

//    /**
//     * Initializes an edge-weighted digraph from the specified input stream. The
//     * format is the number of vertices <em>V</em>, followed by the number of
//     * edges <em>E</em>, followed by <em>E</em> pairs of vertices and edge
//     * weights, with each entry separated by whitespace.
//     *
//     * @param in the input stream
//     * @throws IndexOutOfBoundsException if the endpoints of any edge are not in
//     * prescribed range
//     * @throws IllegalArgumentException if the number of vertices or edges is
//     * negative
//     */
//    public EdgeWeightedDigraph(In in) {
//        this(in.readInt());
//        int E = in.readInt();
//        if (E < 0) {
//            throw new IllegalArgumentException("Number of edges must be nonnegative");
//        }
//        for (int i = 0; i < E; i++) {
//            int v = in.readInt();
//            int w = in.readInt();
//            if (v < 0 || v >= V) {
//                throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V - 1));
//            }
//            if (w < 0 || w >= V) {
//                throw new IndexOutOfBoundsException("vertex " + w + " is not between 0 and " + (V - 1));
//            }
//            double weight = in.readDouble();
//            addEdge(new DirectedEdge(v, w, weight));
//        }
//    }

    /**
     * Initializes a new edge-weighted digraph that is a deep copy of
     * <tt>G</tt>.
     *
     * @param G the edge-weighted digraph to copy
     */
    public EdgeWeightedDigraph(EdgeWeightedDigraph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            this.indegree[v] = G.indegree(v);
        }
        for (int v = 0; v < G.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<DirectedEdge> reverse = new Stack<DirectedEdge>();
            for (DirectedEdge e : G.adj[v]) {
                reverse.push(e);
            }
            for (DirectedEdge e : reverse) {
                adj[v].add(e);
            }
        }
    }

    /**
     * Returns the number of vertices in this edge-weighted digraph.
     *
     * @return the number of vertices in this edge-weighted digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted digraph.
     *
     * @return the number of edges in this edge-weighted digraph
     */
    public int E() {
        return E;
    }

    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= V) {
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    /**
     * Adds the directed edge <tt>e</tt> to this edge-weighted digraph.
     *
     * @param e the edge
     * @throws IndexOutOfBoundsException unless endpoints of edge are between 0
     * and V-1
     */
    public void addEdge(DirectedEdge e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        indegree[w]++;
        E++;
    }

    /**
     * Returns the directed edges incident from vertex <tt>v</tt>.
     *
     * @param v the vertex
     * @return the directed edges incident from vertex <tt>v</tt> as an Iterable
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<DirectedEdge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the number of directed edges incident from vertex <tt>v</tt>.
     * This is known as the <em>outdegree</em> of vertex <tt>v</tt>.
     *
     * @param v the vertex
     * @return the outdegree of vertex <tt>v</tt>
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    /**
     * Returns the number of directed edges incident to vertex <tt>v</tt>. This
     * is known as the <em>indegree</em> of vertex <tt>v</tt>.
     *
     * @param v the vertex
     * @return the indegree of vertex <tt>v</tt>
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public int indegree(int v) {
        validateVertex(v);
        return indegree[v];
    }

    /**
     * Returns all directed edges in this edge-weighted digraph. To iterate over
     * the edges in this edge-weighted digraph, use foreach notation:
     * <tt>for (DirectedEdge e : G.edges())</tt>.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Returns a string representation of this edge-weighted digraph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of
     * edges <em>E</em>, followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (DirectedEdge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

//    /**
//     * Unit tests the <tt>EdgeWeightedDigraph</tt> data type.
//     */
//    public static void main(String[] args) {
//        In in = new In(args[0]);
//        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
//        StdOut.println(G);
//    }
}
