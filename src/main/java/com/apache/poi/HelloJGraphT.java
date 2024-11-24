package com.apache.poi;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.nio.*;

import java.net.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple introduction to using JGraphT.
 */
public class HelloJGraphT {

    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     */
    public static void main(String[] args) throws URISyntaxException, ExportException {
        Graph<Tramit, DefaultWeightedEdge> tramitGraph = createTramitGraph();

        // Ruta ajustada para Maven: "src/main/resources"
        CsvTramitInicialize tramits = new CsvTramitInicialize("tramits.csv",tramitGraph);
        CsvInsertions tramitsInicialize = new CsvInsertions("accions.csv",tramitGraph);

        int counter = 0;
        Collection<Tramit> tramitCollection = tramits.getTramits();
        for (Tramit tramit : tramitCollection) {
            System.out.println(tramit.getTitle());
            counter++;
        }
        System.out.println("Total tramits: " + counter);
        for (DefaultWeightedEdge edge : tramitGraph.edgeSet()) {
            Tramit source = tramitGraph.getEdgeSource(edge);
            Tramit target = tramitGraph.getEdgeTarget(edge);
            double weight = tramitGraph.getEdgeWeight(edge);
            System.out.println(source.getTitle() + " -> " + target.getTitle() + " : " + weight);
        }

        for (Tramit tramit : tramitGraph.vertexSet()) {
            System.out.println("\ntitle: "+tramit.getTitle());
            for (DefaultWeightedEdge edge : tramit.getRanking().getRankingQueue()) {
                Tramit targetTramit = tramitGraph.getEdgeTarget(edge);
                System.out.print(targetTramit.getTitle() +" ");
            }
        }


    }

    /**
     * Create a toy graph based on Tramit objects.
     *
     * @return a graph based on Tramit objects.
     */
    private static Graph<Tramit, DefaultWeightedEdge> createTramitGraph() {
        return new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    }

    public List<Tramit> getTop4Tramits(Collection<Tramit> tramits) {
        return tramits.stream()
                .sorted((t1, t2) -> Integer.compare(t2.getTramitCounter(), t1.getTramitCounter()))
                .limit(4)
                .collect(Collectors.toList());
    }
}
