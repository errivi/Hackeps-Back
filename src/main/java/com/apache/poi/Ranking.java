package com.apache.poi;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * Clase Ranking que mantiene un conjunto ordenado de aristas basado en sus pesos.
 * Se utiliza para gestionar un ranking de las aristas más relevantes de un grafo.
 */
public class Ranking {

    /**
     * Número máximo de elementos en el ranking.
     */
    private final int maxNumRanking;

    /**
     * Cola de prioridad que almacena las aristas ordenadas por peso.
     */
    private final PriorityQueue<DefaultWeightedEdge> rankingQueue;

    /**
     * Referencia al grafo donde están definidas las aristas y sus pesos.
     */
    private final Graph<?, DefaultWeightedEdge> graph;

    /**
     * Peso mínimo de las aristas actualmente en el ranking.
     */
    private double minRanking;

    /**
     * Constructor de la clase Ranking.
     *
     * @param graph         Grafo que contiene las aristas y sus pesos.
     * @param maxNumRanking Número máximo de aristas que se pueden almacenar en el ranking.
     */
    public Ranking(Graph<?, DefaultWeightedEdge> graph, int maxNumRanking) {
        this.graph = graph;
        this.maxNumRanking = maxNumRanking;
        this.rankingQueue = new PriorityQueue<>(Comparator.comparingDouble(this::getEdgeWeight));
        this.minRanking = Double.MAX_VALUE;
    }

    /**
     * Actualiza el ranking añadiendo una nueva arista o actualizando su posición si ya existe.
     * Si la nueva arista tiene un peso mayor que el mínimo actual y el ranking está lleno,
     * la arista con menor peso será eliminada.
     *
     * @param connection Arista que se intenta añadir al ranking.
     * @param weight     Peso de la arista.
     */
    public void updateRanking(DefaultWeightedEdge connection, double weight) {
        // Si la arista ya está en el ranking, actualízala
        if (rankingQueue.contains(connection)) {
            rankingQueue.remove(connection); // Elimina la arista existente
            rankingQueue.add(connection);   // Añade la arista con el peso actualizado
            updateMinRanking();
            return;
        }

        // Si el ranking no está lleno, añade directamente
        if (rankingQueue.size() < maxNumRanking) {
            rankingQueue.add(connection);
            updateMinRanking();
        } else if (weight >= minRanking) {
            // Si el ranking está lleno y la nueva arista tiene un peso mayor al mínimo actual
            rankingQueue.poll(); // Elimina la arista con menor peso
            rankingQueue.add(connection);
            updateMinRanking();
        }
        sortRanking();
    }

    /**
     * Actualiza el valor del peso mínimo actual (`minRanking`) en el ranking.
     * Este valor representa el menor peso de las aristas en la cola.
     */
    private void updateMinRanking() {
        minRanking = rankingQueue.stream()
                .mapToDouble(this::getEdgeWeight)
                .min()
                .orElse(Double.MAX_VALUE);
    }

    /**
     * Obtiene el peso de una arista desde el grafo.
     *
     * @param edge Arista cuyo peso se desea consultar.
     * @return Peso de la arista.
     */
    private double getEdgeWeight(DefaultWeightedEdge edge) {
        return graph.getEdgeWeight(edge);
    }

    /**
     * Devuelve la cola de prioridad que contiene el ranking de aristas.
     *
     * @return Cola de prioridad con las aristas ordenadas por peso.
     */
    public PriorityQueue<DefaultWeightedEdge> getRankingQueue() {
        // Crear una nueva PriorityQueue con el orden inverso
        PriorityQueue<DefaultWeightedEdge> reversedQueue = new PriorityQueue<>(
                Comparator.comparingDouble(this::getEdgeWeight).reversed()
        );

        // Agregar todos los elementos de rankingQueue en el nuevo orden
        reversedQueue.addAll(rankingQueue);

        return reversedQueue;
    }

    private void sortRanking() {
        List<DefaultWeightedEdge> sortedEdges = rankingQueue.stream()
                .sorted(Comparator.comparingDouble(this::getEdgeWeight))
                .toList();

        // Crea una nueva PriorityQueue con los elementos ordenados
        rankingQueue.clear(); // Elimina los elementos actuales
        rankingQueue.addAll(sortedEdges); // Añade los elementos en orden
    }
}
