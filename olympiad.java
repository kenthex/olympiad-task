package kosaraju;

import java.util.*;

public class kosaraju {

	public void DFS(List<Integer>[] graph, int v, boolean[] visited, List<Integer> comp) {
		visited[v] = true;
		for (int i = 0; i < graph[v].size(); i++)
			if (!visited[graph[v].get(i)])
				DFS(graph, graph[v].get(i), visited, comp);
		comp.add(v);
	}

	public List<Integer> fillOrder(List<Integer>[] graph, boolean[] visited) {
		int V = graph.length;
		List<Integer> order = new ArrayList<Integer>();

		for (int i = 0; i < V; i++)
			if (!visited[i])
				DFS(graph, i, visited, order);
		return order;
	}

	public List<Integer>[] getTranspose(List<Integer>[] graph) {
		int V = graph.length;
		List<Integer>[] g = new List[V];

		for (int i = 0; i < V; i++)
			g[i] = new ArrayList<Integer>();
		for (int v = 0; v < V; v++)
			for (int i = 0; i < graph[v].size(); i++)
				g[graph[v].get(i)].add(v);
		return g;
	}

	public List<List<Integer>> getSCComponents(List<Integer>[] graph) {
		int V = graph.length;
		boolean[] visited = new boolean[V];
		List<Integer> order = fillOrder(graph, visited);

		List<Integer>[] reverseGraph = getTranspose(graph);

		visited = new boolean[V];

		Collections.reverse(order);

		/* get all scc */
		List<List<Integer>> SCComp = new ArrayList<>();
		for (int i = 0; i < order.size(); i++) {
			int v = order.get(i);
			if (!visited[v]) {
				List<Integer> comp = new ArrayList<>();
				DFS(reverseGraph, v, visited, comp);
				SCComp.add(comp);
			}
		}
		

		return SCComp;
	}
	
	public List<Integer> DFS_depots(List<Integer>[] graph, int v, int t, boolean[] visited, List<Integer> result) {
		visited[v] = true;
		for (int i = 0; i < graph[v].size(); i++) 
		{
				if (!visited[graph[v].get(i)]) 
				{
					DFS_depots(graph, graph[v].get(i), t, visited, result);
				}
		}
		
		if(visited[t]) {
			result.add(0);
		}
		
		return result;
	}
		
	public List<List<Integer>> findDepots(List<Integer>[] graph, List<List<Integer>> SCC) {
		int V = graph.length;
		boolean[] visited = new boolean[V];

		visited = new boolean[V];

		List<Integer> depotsList = new ArrayList<>();
		for(int i = 0; i < SCC.size() - 1; i++) {
			for(int j = 0; j < SCC.get(i).size(); j++) {
				try {
					for(int k = 0; k < SCC.get(i + 1).size(); k++) {
						int start = SCC.get(i).get(j);
						int finish = SCC.get(i + 1).get(k);
						System.out.println("Ïðîâåðÿåì: " + start + " -> " + finish);
						
						List<Integer> depots = new ArrayList<>();
						depots.clear();
	
						visited = new boolean[V];
						
						DFS_depots(graph, start, finish, visited, depots);
						
						if(!depots.isEmpty()) {
							System.out.println("Ðåçóëüòàò: äà, çíà÷èò äåïî áóäåò: " + finish);
							depotsList.add(finish);
							i++;
							depots.clear();
						} else {
							visited = new boolean[V];
							System.out.println("Ðåçóëüòàò: íåò, ïðîâåðÿåì îáðàòíîå");
							System.out.println("Ïðîâåðÿåì: " + finish + " -> " + start);
							DFS_depots(graph, finish, start, visited, depots);
							if(!depots.isEmpty()) {
								System.out.println("Ðåçóëüòàò: äà");
								System.out.println(depots.get(0));
								depots.clear();
							} else {
								System.out.println("Ðåçóëüòàò: íåò, çíà÷èò äåïî áóäåò: " + finish);
								depotsList.add(finish);
								depots.clear();
							}
						}

					} 
				} catch(Exception e) {}
			}
		}
		
		visited = new boolean[V];
		System.out.println("\n- Ïðîâåðêà ìåæäó ïîëó÷åííûìè äåïî: " + depotsList);
		List<Integer> finalRecieve = new ArrayList<Integer>();
		for(int i = 0; i < depotsList.size() - 1; i++) {
			DFS_depots(graph, depotsList.get(i), depotsList.get(i + 1), visited, finalRecieve);
			if(finalRecieve.isEmpty()) {
				System.out.println("Ñâÿçè " + depotsList.get(i) + " -> " + depotsList.get(i + 1) + " íåò, îñòàâëÿåì êàê åñòü.");
				DFS_depots(graph, depotsList.get(i + 1), depotsList.get(i), visited, finalRecieve);
			} else {
				System.out.println("Ñâÿçü " + depotsList.get(i) + " -> " + depotsList.get(i + 1) + " åñòü, óäàëÿåì " + depotsList.get(i));
				depotsList.remove(i);
			}
		}
		
		System.out.println("Èòîãîâûå äåïî: " + depotsList);
		System.out.println("Êîë-âî äåïî: " + depotsList.size());
		
		return SCC;
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		kosaraju k = new kosaraju();

		System.out.println("Ââåäèòå êîë-âî âåðøèí:");
		int V = scan.nextInt();
		List<Integer>[] g = new List[V];
		for (int i = 0; i < V; i++)
			g[i] = new ArrayList<Integer>();

		System.out.println("Ââåäèòå êîë-âî ð¸áåð:");
		int E = scan.nextInt();
		System.out.println("Óêàæèòå " + E + " ñâÿçåé(-è) ìåæäó âåðøèíàìè (V1, V2)");
		for (int i = 0; i < E; i++) {
			int x = scan.nextInt();
			int y = scan.nextInt();
			g[x - 1].add(y - 1);
		}

		List<List<Integer>> scComponents = k.getSCComponents(g);
		
		for(int i = 0; i < scComponents.size(); i++) {
		Collections.sort(scComponents.get(i)); }
		System.out.println("\n- Êîìïîíåíòû ñèëüíîé ñâÿçè: " + scComponents);
		
		List<List<Integer>> depot = k.findDepots(g, scComponents);

	}
}
