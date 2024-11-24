package com.apache.poi;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;


public class Tramit {
    private String idTramit, title;
    private Boolean available;
    private final Ranking ranking;
    private final double minRanking = 0;
    private int tramitCounter;

    public Tramit(String idTramit, String title, Boolean available, Graph<Tramit, DefaultWeightedEdge> workingGraph) {
        this.idTramit = idTramit;
        this.title = title;
        this.available = available;
        int maxNumRanking = 4;
        this.ranking = new Ranking(workingGraph, maxNumRanking);
        tramitCounter = 0;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdTramit() {
        return idTramit;
    }

    public void setIdTramit(String idTramit) {
        this.idTramit = idTramit;
    }

    public void updateRanking(DefaultWeightedEdge connection, double weight) {
        ranking.updateRanking(connection, weight);
    }

    public PriorityQueue<DefaultWeightedEdge> getRankingQueue() {
        return ranking.getRankingQueue();
    }

    public Ranking getRanking() {
        return ranking;
    }


    public int getTramitCounter() {
        return tramitCounter;
    }


    public void setTramitCounter(int i) {
        tramitCounter = i;
    }
}
