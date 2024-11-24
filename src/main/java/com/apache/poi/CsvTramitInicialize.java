package com.apache.poi;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class CsvTramitInicialize {

    protected static HashMap<String, Tramit> tramits = new HashMap<>();
    private final Graph<Tramit, DefaultWeightedEdge> tramitGraph;

    //recuerda enviar path desde main->mirar funcion que obtenga path
    public CsvTramitInicialize(String filePath, Graph<Tramit, DefaultWeightedEdge> tramitGraph){
        this.tramitGraph = tramitGraph;
        readCsvFile(filePath);
    }

    public void readCsvFile(String filePath) {
        try (CSVReader inicialize = new CSVReader(new FileReader(filePath))) {
            List<String[]> lines = inicialize.readAll();
            for (String[] line : lines) {
                if (line.length == 3) {
                    String id = line[0];
                    String titol = line[1];
                    Boolean vigent = Boolean.parseBoolean(line[2]);

                    Tramit newTramit = new Tramit(id, titol, vigent, tramitGraph);
                    tramits.put(id, newTramit);
                    tramitGraph.addVertex(newTramit);

                } else {
                    System.out.println("Línea con formato incorrecto: " + String.join(",", line));
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public Collection<Tramit> getTramits() {
        return tramits.values();
    }
}
