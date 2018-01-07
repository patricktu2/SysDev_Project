/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lmu.project.NetworkGraph;

import java.util.*;

/**
 * Class for Dijkstra Algrotithm implementation based on http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
 * adjusted and extended for a GEO JSON usage
 * @author Patrick
 */
public class DijkstraAlgorithm {
   
    
    private final List<Vertex> nodes;
    private final List<Edge> edges;
    private Set<Vertex> settledNodes;
    private Set<Vertex> unSettledNodes;
    private Hashtable<Vertex, Vertex> predecessors; 
    private Hashtable<Vertex, Double> distance;

    public DijkstraAlgorithm(RoadNetworkGraph graph) {
        // create copy of node and edge lists to perform operations on it
        nodes = new ArrayList <Vertex>(graph.vertex);
        edges = new ArrayList <Edge> (graph.edges);
    }
    
    
    public static String performShortestPathSearch(double origin_lat,double origin_lon, double dest_lat, double dest_lon){
        //Fetch parameters and create vertex object
        Vertex origin = new Vertex(origin_lon,origin_lat);
        Vertex destination = new Vertex (dest_lon, dest_lat);            
        System.out.println("Parameters taken by markers on the map: Origin=" +origin + " Destination="+ destination);
        
        //Create and instantiate Network Graph
        RoadNetworkGraph graph = NetworkInstantiation.createGraph();
        NetworkInstantiation.instantiateEdgesOfVertexes(graph);
        
        //If the coordinate cannot be found within the graph get the closest vertex as an approximate
        origin = findClosestVertex(origin,graph);
        destination = findClosestVertex(destination,graph);
        
        System.out.println("Approximating a node within the graph: Origin=" +origin + " Destination="+ destination);
        
        //Perform Dijkstra Algorithm; Return Object with attribute "Distance" and "Path"
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        HashMap <String, Object > returnObject = dijkstra.execute(origin,destination);
        
        System.out.println("Distance="+returnObject.get("distance"));
        System.out.println("Path: " + returnObject.get("path"));
        
        // Construct JSON response in the needed form
        String json_part1 = "{\n" +
                            "	\"type\":\"FeatureCollection\",\n" +
                            "	\"features\":[{\n" +
                            "		\"type\":\"Feature\",\n" +
                            "		\"geometry\":\n" +
                            "			{\"type\":\"LineString\",\"coordinates\":";
        String path = returnObject.get("path").toString();
        String json_part2 = "},\n" +
                            "           \"properties\":{\"costs\":{\"Distance\":";
        String distance = returnObject.get("distance").toString();
        String json_part3 = ",\"Travel_Time\": \"???\"},\n" +
                            "		\"instructions\":[]}\n" +
                            "		}]\n" +
                            "	}";
        
        String json_string = json_part1 + path + json_part2 + distance + json_part3;
        
        //System.out.println(json_string);
        
        return json_string;
        
    }
    
    /**
     * Given a Vertex and a Graph this method returns the Vertex in the Graph which has the
     * least great circle distance from the original vertex
     * @param node
     * @param graph
     * @return 
     */
    public static Vertex findClosestVertex (Vertex node, RoadNetworkGraph graph){
        List <Vertex> vertexes = graph.vertex;
        
        Vertex approximate_node = null;
        
        double min_distance = Double.MAX_VALUE;
        double temp_distance;
        
        for(Vertex v: vertexes){
            temp_distance = Coordinate.computeDistance(node.coordinate, v.coordinate);
            if (temp_distance < min_distance){
                min_distance = temp_distance;
                approximate_node = v; 
            }
        }
        
        Vertex closestVertex = new Vertex(approximate_node.coordinate);
        return closestVertex;
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
        //System.out.println("Amount of Neghbours="+adjacentNodes.size());
        for (Vertex target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node) + getDistance(node, target)); //update shortest distance
                //System.out.println("distance:"+distance);
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
            //System.out.println(edge);
            
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
        
        // WTF WHY IS THIS ALWAYS NULL IN A CALL WITHIN THE CLASS AFTER EXCECUTION?
        //System.out.println(predecessors.get(step)); 
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
    
    
    /**
     * Returns at the moment a hashmap with the keys "distance" for the minimal distance to the target node 
     * and the key "path" with the shortest path from source vertex to target vertex
     * @param source
     * @param target
     * @return 
     */
    public HashMap <String, Object > execute(Vertex source, Vertex target) {
        
        System.out.println("--- DIJKSTRA ALGORITHM start ----");
        settledNodes = new HashSet<Vertex>(); // List / Marker that keeps track if a vertex has been visited
        unSettledNodes = new HashSet<Vertex>(); //List / Marker that keeps track of unvisited vertexes
        distance = new Hashtable<Vertex, Double>(); // Distance from the source vertex to a particualar vertex
        predecessors = new Hashtable<Vertex, Vertex>(); // Previous nodes in optimal path
        
        distance.put(source, 0.0); // Set Distance from source to source to 0
        unSettledNodes.add(source); // mark as visited
       
        int counter = 1;
        while (unSettledNodes.size() > 0) {
            Vertex node = getMinimum(unSettledNodes); // Priority Queuy according to greedy principle - which one to visit next?
            //System.out.println(counter + ". Iteration - Current node: " + node);
            
            settledNodes.add(node); // Mark the vertex to be visited 
            unSettledNodes.remove(node); // remove from invisited list
            findMinimalDistances(node);
            //System.out.println(getPath(node));
            counter++;
        }
        System.out.println("---Dijkstra Algorithm terminated with "+ counter + " iterations---");
        
        //System.out.println(predecessors.get(target));
        HashMap <String, Object >returnObject = new HashMap();
        returnObject.put("distance", distance.get(target));
        returnObject.put("path", getPath(target));
        returnObject.put("target", target);
        return returnObject;
        
    } 

    public Set<Vertex> getSettledNodes() {
        return settledNodes;
    }

    public void setSettledNodes(Set<Vertex> settledNodes) {
        this.settledNodes = settledNodes;
    }

    public Set<Vertex> getUnSettledNodes() {
        return unSettledNodes;
    }

    public void setUnSettledNodes(Set<Vertex> unSettledNodes) {
        this.unSettledNodes = unSettledNodes;
    }

    public Map<Vertex, Vertex> getPredecessors() {
        return predecessors;
    }


    public Map<Vertex, Double> getDistance() {
        return distance;
    }



    public List<Vertex> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    
    
    
    
}
