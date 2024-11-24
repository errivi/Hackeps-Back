package com.apache.poi;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CsvInsertions {

    protected static HashMap<String, PersonTramitator> tramitators= new HashMap<>();
    protected static HashMap<String, SessionNoLogin> noLoginSesions = new HashMap<>();
    private Graph<Tramit, DefaultWeightedEdge> tramitGraph;

    public CsvInsertions(String filePath, Graph<Tramit, DefaultWeightedEdge> tramitGraph) {

        if(CsvTramitInicialize.tramits.isEmpty()) {
            CsvTramitInicialize tramits = new CsvTramitInicialize("tramits.csv" ,tramitGraph);
        }
        this.tramitGraph = tramitGraph;
        readCsvFileInsertion(filePath);
    }

    public void readCsvFileInsertion(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> lines = reader.readAll();
            for (String[] line : lines) {

                if (line.length == 6 || line.length == 5) {
                    String data = line[0];
                    String accio = line[1];
                    String tramit = line[2];
                    String session = line[3];
                    String usuari = line[4];
                    String representat = line.length == 6 ? line[5].trim() : "";

                    if (representat.equals("true")) {
                        usuari = representat;
                    }

                    //case no login
                    if (usuari == null || usuari.isEmpty()) {
                        //if the session is not in the noLoginSesions, add it
                        noLogin(session, tramit);
                    }
                    //case login
                    else {
                        login(session, tramit, usuari);
                    }

                }else {
                    System.out.println("Línea con formato incorrecto: " + String.join(",", line));
                }
            }
        }catch(IOException | CsvException e){
                e.printStackTrace();
        }
    }






    public void noLogin(String session, String tramit) {
        //System.out.println("NoLogin detectado");
        //Check if the session is not in the noLoginSesions
        if (!noLoginSesions.containsKey(session)) {
            SessionNoLogin newSessionNoLogin = new SessionNoLogin(session);
            noLoginSesions.put(session, newSessionNoLogin);
            System.out.println("Sesion creada: " + session);
        }
        //Check if sesion exists and if the sesion is in the noLoginSesions
        if (noLoginSesions.containsKey(session)) {
            //take tramit from tramits
            Tramit newTramit = CsvTramitInicialize.tramits.get(tramit);
            //take current session from noLoginSesions
            SessionNoLogin currentSesion = noLoginSesions.get(session);
            //take last tramit from current session
            Tramit lastTramit = currentSesion.getLastTramit();
            //check if is the first tramitator tramit or not
            if (lastTramit != null) {
                //get the edge between the last tramit and the new tramit
                DefaultWeightedEdge edge = tramitGraph.getEdge(lastTramit, newTramit);
                Ranking rankingOrigin = lastTramit.getRanking(); // Obtener el ranking asociado
                //if the edge does not exist and the last tramit is different from the new tramit-> add edge
                if (edge == null && (lastTramit != newTramit)) {
                    DefaultWeightedEdge newEdge = tramitGraph.addEdge(lastTramit, newTramit);;
                    tramitGraph.setEdgeWeight(newEdge, 1.0); // Asignar el peso a la arista
                    rankingOrigin.updateRanking(newEdge, 1.0); // Actualizar el ranking con la nueva arista
                    System.out.println("crea arista de"+lastTramit + "a"+tramit);
                }
                //if edge exists, increment the weight
                else if (edge != null) {
                    double currentWeight = tramitGraph.getEdgeWeight(edge);
                    tramitGraph.setEdgeWeight(edge, currentWeight + 1.0);
                    rankingOrigin.updateRanking(edge, 1.0);
                    System.out.println("incrementa arista de"+lastTramit + "a"+tramit);
                }
                currentSesion.setLastTramit(newTramit);
                System.out.println("Se mueve a tramite"+tramit);
            }
            else {
                //if is the first tramit, add the tramit to user recent history
                currentSesion.setLastTramit(newTramit);
                newTramit.setTramitCounter(newTramit.getTramitCounter() + 1);
                System.out.println("Se situa en tramite"+tramit);
            }
        }
        else {
            System.out.println("Error initialazing session not exist");
        }
    }


    public void login(String session, String tramit, String usuari) {
        if (!tramitators.containsKey(usuari)) {
            PersonTramitator newPerson = new PersonTramitator(usuari, session);
            tramitators.put(usuari, newPerson);
        }
        //Check if tramit exists and if the user is in the tramitators
        else if (CsvTramitInicialize.tramits.containsKey(tramit) && tramitators.containsKey(usuari)) {
            //take tramit from tramits
            Tramit newTramit = CsvTramitInicialize.tramits.get(tramit);
            //take person from tramitators
            PersonTramitator person = tramitators.get(usuari);
            //take last tramit from tramitator
            Tramit lastTramit = person.getLastTramit();

            //check if is the first tramitator tramit or not
            if (lastTramit != null) {
                //get the edge between the last tramit and the new tramit
                DefaultWeightedEdge edge = tramitGraph.getEdge(lastTramit, newTramit);
                if (edge == null && (lastTramit != newTramit)) {
                    tramitGraph.addEdge(lastTramit, newTramit);
                    tramitGraph.setEdgeWeight(lastTramit, newTramit, 1.0);
                } else if (edge != null) {
                    double currentWeight = tramitGraph.getEdgeWeight(edge);
                    tramitGraph.setEdgeWeight(edge, currentWeight + 1.0);
                }
            } else {
                //if is the first tramit, add the tramit to user recent history
                person.setLastTramit(newTramit);
            }
        }
        else {
            System.out.println("Tramit no trobat: " + tramit);
        }
    }
}


