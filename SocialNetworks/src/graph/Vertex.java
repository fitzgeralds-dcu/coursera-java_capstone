package graph;

import java.util.HashSet;

public class Vertex {
	
	private int id;
	private HashSet<Integer> neighbours;
	
	public Vertex (int id) {
		
		this.id = id;
		this.neighbours = new HashSet<>();
		
	}
	
	public int getVertexId() {
		
		return id;
		
	}
	
	public void addNeighbour(int neighbour) {
		
		neighbours.add(neighbour);
		
	}
	
	public HashSet<Integer> getNeighbours(){
		
		return neighbours;
		
	}
	

}
