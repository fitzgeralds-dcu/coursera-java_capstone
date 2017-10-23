/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author Stuart Fitzgerald
 * October 2017 Cousera Data Structures Capstone
 * Warmup analysis
 */
public class CapGraph implements Graph {

	private int size;

	private Map<Integer, HashSet<Integer>> neighbourMap = new HashMap<Integer, HashSet<Integer>>();

	private List<CapGraph> SCCs = new ArrayList<CapGraph>();

	/**
	 * add vertex to graph
	 */
	@Override
	public void addVertex(int num) {
		
		neighbourMap.put(num, new HashSet<Integer>());
		
		size++;
	}

	/**
	 * add edge to graph
	 */
	@Override
	public void addEdge(int from, int to) {
		
		if (this.exportGraph().containsKey(from) && this.exportGraph().containsKey(to))
			
			neighbourMap.get(from).add(to);
		
	}

	/**
	 * @return graph's size
	 */
	public int size() {
		
		return size;
		
	}

	/**
	 * @return egonet for specified vertex
	 */ 
	@Override
	public Graph getEgonet(int center) {
		
		CapGraph egonet = new CapGraph();

		try {	

			egonet.addVertex(center);

			for (Integer vertex: neighbourMap.get(center))
				
				egonet.addVertex(vertex);

			for (Integer from: egonet.neighbourMap.keySet())
				
				for (Integer to: egonet.neighbourMap.keySet())
				
					if (neighbourMap.get(from).contains(to))
					
						egonet.addEdge(from, to);
			
		}
		
		catch (Exception e) {
			
			//return empty graph if center not found in neighbourMap
			
		}

		return egonet;
	}

	/**
	 * @return strongly connected components from the graph 
	 */
	@Override
	public List<Graph> getSCCs() {

		Stack<Integer> finished = DFS(this.vertices(), false);

		CapGraph transpose = this.graphTanspose();

		transpose.DFS(finished, true);

		for (CapGraph graph: transpose.SCCs) {
		
			this.SCCs.add(graph.graphTanspose());
			
		}

		List<Graph> SCCsGraphs = new ArrayList<Graph>();

		for (CapGraph graph: SCCs) {
			
			SCCsGraphs.add(graph);
			
		}

		return SCCsGraphs;
	}

	/**
	 * @return mapping of vertices to neighbours
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		
		return (HashMap<Integer, HashSet<Integer>>)neighbourMap;
		
	}

	/**
	 * Depth-first search
	 * @param Stack of vertices
	 * @param boolean boolSCC to differentiate between straight DFS 
	 * and DFS on transposed graph; adds vertices to graph when boolSCC true
	 * 
	 * @return Stack<Integer> 
	 */
	public Stack<Integer> DFS(Stack<Integer> vertices, boolean boolSCC){
		
		HashSet<Integer> visited = new HashSet<Integer>();
		
		Stack<Integer> finished = new Stack<Integer>();

		while (!vertices.isEmpty()){
			
			Integer v = vertices.pop();

			if (!visited.contains(v)){
				
				DFSVisit(v, visited, finished);

				if (boolSCC)
					
					SCCs(finished);
				
			}
		}
		
		return finished;
	}

	/**
	 * Helper method for DFS
	 * @param vertex being visited
	 * @param Stack of finished vertices 
	 * @param Set of visited vertices
	 */
	public void DFSVisit(Integer vertex, HashSet<Integer> visited, Stack<Integer> finished){

		visited.add(vertex);

		for (Integer n: exportGraph().get(vertex))
			if (!visited.contains(n))
				DFSVisit(n, visited, finished);

		finished.push(vertex);
	}


	/**
	 * Constructs SCC graphs and adds them to SCCs List<CapGraph>
	 *  
	 * @param Stack<Integer> finished 
	 */
	public void SCCs(Stack<Integer> finished){

		CapGraph graph = new CapGraph();
		
		List<Integer> vertices = new ArrayList<Integer>();

		int sizeSCC = SCCs.size();

		int i = 0, j = 0;
		
		while (j < sizeSCC){
			
			i += SCCs.get(j).size();
			
			j++;
			
		}

		while (i < finished.size()){
			
			vertices.add(finished.get(i));
			
			i++;
			
		}

		// add strongly connected vertices
		
		for (Integer vertex: vertices)
			
			graph.addVertex(vertex);

		// add edges to SCCs
		
		for (Integer vertex: vertices)
			
			for (Integer a: neighbourMap.get(vertex))
				
				graph.addEdge(vertex, a);

		SCCs.add(graph);
	}


	/**
	 * @return Stack of all vertices
	 */
	public Stack<Integer> vertices(){
		
		Stack<Integer> vertices = new Stack<Integer>();

		for (Integer vertex: neighbourMap.keySet()){
			
			vertices.push(vertex);
			
		}

		return vertices;
	}
	

	/**
	 * Transpose the graph
	 * 
	 * @return transposed CapGraph
	 */
	public CapGraph graphTanspose(){

		CapGraph transposed = new CapGraph();
		
		for (Integer vertex: neighbourMap.keySet())
			
			transposed.addVertex(vertex);
		
		for (Integer vertex: neighbourMap.keySet()){
			
			HashSet<Integer> edgeSet = neighbourMap.get(vertex);
			
			for (Integer v: edgeSet)
				
				transposed.addEdge(v, vertex);
			
		}
		
		return transposed;
	}


}
