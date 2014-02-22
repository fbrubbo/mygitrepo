package graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 
ESTOU ASSUMINDO QUE O VALOR DO V é IGUAL A POSICAO DO ARRAY. ISTO SIMPLIFICA A IMPLEMENTACAO
 * 
 * 

Based upon the BFS, there are O(V + E)-time algorithms for the following problems:
	Testing whether graph is connected. - Se tem mais que um root, não é 100% conected
	Computing a spanning forest of graph. - Se pegar os root eu sei quantas arvores
	Computing, for every vertex in graph, a path with the minimum number of edges between start vertex and current vertex or reporting that no such path exists.
	Computing a cycle in graph or reporting that no such cycle exists. - se um vertice que for navegar já tiver sido navegado. eu tenho ciclo


Based upon DFS, there are O(V + E)-time algorithms for the following problems:
	Testing whether graph is connected. - Se tem mais que um root, não é 100% conected
	Computing a spanning forest of G. - Se pegar os root eu sei quantas arvores
	Computing the connected components of G. - navegando a partir do root eu sei dizer quem está conectado
	Computing a path between two vertices of G or reporting that no such path exists. - se eu souber o destino eu consigo dizer se é atingível ou não
	Computing a cycle in G or reporting that no such cycle exists. - se um vertice que for navegar já tiver sido navegado. eu tenho ciclo

 *
 */

public class AdjacentListGraphVisitor {
	public static final int WHITE = 0;
	public static final int GRAY = 1;
	public static final int BLACK = 2;

	int time = 0;
	public int[] color;
	public int[] parent;
	public int[] discovered;
	public int[] finished;
	int[] vertices; 
	int[][] edges;

	public AdjacentListGraphVisitor(int[] vertices, int[][] edges){
		this.vertices = vertices;
		this.edges = edges;
		
		color = new int[vertices.length];
		parent = new int[vertices.length];
		discovered = new int[vertices.length];
		finished = new int[vertices.length];
		
		for (int i=0; i<vertices.length; i++){
			color[i] = WHITE;		
			parent[i] = -1;
		}
	}
	
	
	public void breadthFirstSearch(){
		Queue<Integer> queue = new LinkedList<>();
		for(int i=0; i<vertices.length; i++) {
			if(color[i]==WHITE){			
				queue.add(vertices[i]);
			}
			while(!queue.isEmpty()) {
				Integer v = queue.poll();
				breadthFirstSearchVisit(queue, v);
			}
		}
	}
	
	private void breadthFirstSearchVisit(Queue<Integer> queue, Integer posi) {
		color[posi] = GRAY;
		time++;
		discovered[posi] = time;
		System.out.println("Discovered: " + posi + " in " + time);
		for(int[] e : edges) {
			if(e[0]==posi){					// adjacent
				if(color[e[1]]==WHITE){
					parent[e[1]] = posi;
					queue.add(e[1]);
				}
			}
		}
		color[posi] = BLACK;
		time++;
		finished[posi] = time;
		System.out.println("Finished: " + posi + " in " + time);		
	}


	public void depthFirstSearch(){
		for (int i=0; i<vertices.length; i++){
			if(color[i] == WHITE){
				depthFirstSearchVisit(i);
			}
		}
	}
	
	private void depthFirstSearchVisit(int posi) {
		color[posi] = GRAY;
		time++;
		discovered[posi] = time;
		System.out.println("Discovered: " + posi + " in " + time);
		for(int[] e : edges) {
			if(e[0]==posi){					// adjacent
				if(color[e[1]]==WHITE){
					parent[e[1]] = posi;
					depthFirstSearchVisit(e[1]);
				}
			}
		}
		color[posi] = BLACK;
		time++;
		finished[posi] = time;
		System.out.println("Finished: " + posi + " in " + time);
	}
	
	
}
