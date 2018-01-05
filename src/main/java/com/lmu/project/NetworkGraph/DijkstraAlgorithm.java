/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.NetworkGraph;

import java.util.*;

/**
 * DijkstraAlgrotithm implementation based on http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
 * 
 * @author Patrick
 */
public class DijkstraAlgorithm {
   
    
    private final List<Vertex> nodes;
    private final List<Edge> edges;
    private Set<Vertex> settledNodes;
    private Set<Vertex> unSettledNodes;
    private Map<Vertex, Vertex> predecessors;
    private Map<Vertex, Double> distance;

    public DijkstraAlgorithm(RoadNetworkGraph graph) {
        // create copy of node and edge lists to perform operations on it
        nodes = new ArrayList <Vertex>(graph.vertex);
        edges = new ArrayList <Edge> (graph.edges);
    }
    
    /**
     * traverses all vertexes of the given vertex set (e.g., unvisited vertexes) and 
     * returns the one with the minimum distance
     * @param vertexes
     * @return 
     */
    private Vertex getMinimum(Set<Vertex> vertexes) {
        Vertex minimum = null;
        for (Vertex vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }  
    
    /**
     * returns the shortest distance of a given vertex from the source vertex; If the vertex hasn't been
     * visited yet and the distance assigned, infinity will be returned.
     * @param destination
     * @return 
     */
    public double getShortestDistance(Vertex destination) {
        Double d = distance.get(destination);
        if (d == null) {
            return Double.MAX_VALUE;
        } else {
            return d;
        }
    }    
    
    /**
     * Explores all neighbors of a given node and updates the minimum distances  to the source of these neighbor nodes
     * by checking if a shorter path can be found by using the alternative edge
     * 
     * @param node 
     */
    private void findMinimalDistances(Vertex node) {
        List<Vertex> adjacentNodes = getNeighbors(node);
        for (Vertex target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node) + getDistance(node, target)); //update shortest distance
                predecessors.put(target, node); //update optimal path
                unSettledNodes.add(target); //
            }
        }

    }
    
    /**
     * Returns a list of adjacent neighbors of a given node, which have been visited already
     * @param node
     * @return 
     */
    private List<Vertex> getNeighbors(Vertex node) {
        List<Vertex> neighbors = new ArrayList<Vertex>();
        for (Edge edge : edges) {
            
            Vertex temp_or = new Vertex( edge.getOrigin());
            Vertex temp_des = new Vertex( edge.getDestination());
            
            // if one of the edges has the edges contains the param "node" and it's adjacent counterpart
            // has been visited already, add it to the neighbor list
            if (temp_or.equals(node) && !isSettled(temp_des)) {
                neighbors.add( temp_des);
            }
            if (temp_des.equals(node) && !isSettled(temp_or)) { //symmetric counterpart as bidirected graph
                neighbors.add( temp_or);
            }
        }
        return neighbors;
    }   
    
    /**
     * Returns true if a Vertex has been visited already 
     * @param vertex
     * @return 
     */
    private boolean isSettled(Vertex vertex) {
        return settledNodes.contains(vertex);
    }    
     
    /**
     * Checks the in the list of edges if an edge exists that links two vertexes and
     * returns the distance of the edge
     * @param node
     * @param target
     * @return 
     */
    private double getDistance(Vertex node, Vertex target) {
        for (Edge edge : edges) {
            
            if (edge.getOrigin().equals(node.getCoordinate())
                    && edge.getDestination().equals(target.getCoordinate())) {
                return edge.getDistance();
            }
            
            if (edge.getOrigin().equals(target.getCoordinate())
                    && edge.getDestination().equals(node.getCoordinate())) {
                return edge.getDistance();
            } 
        }
        throw new RuntimeException("Should not happen");
    }    
    
    /**
     * Returns the shortest/cheapest path from the source to the selected target vertex; 
     * If no such path exists Null will be returned
     * @param target
     * @return 
     */
    public LinkedList<Vertex> getPath(Vertex target) {
        LinkedList<Vertex> path = new LinkedList<>();
        Vertex step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }    
   
    public void execute(Vertex source) {
        settledNodes = new HashSet<Vertex>(); // List / Marker that keeps track if a vertex has been visited
        unSettledNodes = new HashSet<Vertex>(); //List / Marker that keeps track of unvisited vertexes
        distance = new HashMap<Vertex, Double>(); // Distance from the source vertex to a particualar vertex
        predecessors = new HashMap<Vertex, Vertex>(); // Previous nodes in optimal path
        
        distance.put(source, 0.0); // Set Distance from source to source to 0
        unSettledNodes.add(source); // mark as visited
       
        
        while (unSettledNodes.size() > 0) {
            Vertex node = getMinimum(unSettledNodes); // Priority Queuy according to greedy principle - which one to visit next?
            settledNodes.add(node); // Mark the vertex to be visited 
            unSettledNodes.remove(node); // remove from invisited list
            findMinimalDistances(node);
        }
    } 


    
    
    
    
}
