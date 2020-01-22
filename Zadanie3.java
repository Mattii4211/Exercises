import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Zadanie3 {
	private static int vertices;
	private static int[][] matrix;
	private static int[] adj;

	List[] convertToList(int[][] m) {
		int v = m.length;
		List[] adj = new List[v];
		for (int i = 0; i < v; ++i)
			adj[i] = new List();

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				if (m[i][j] == 1) {
					adj[i].add(j);
				}
			}
		}
		return adj;
	}

	int[][] convertToMatrix(List[] adj) {
		int v = adj.length;
		int[][] m = new int[v][v];
		for (int i = 0; i < v; i++) {
			for (int j = 0; j < adj[i].size(); i++) {
				m[i][j] = adj[i].get(j);
			}
		}
		return m;
	}

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(new File("dane.txt"));
			String line;
			int a, b, x, vertices = 0, l = 0;
			int[] t = new int[100];
			scanner.nextLine();

			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				x = line.indexOf(",");
				a = Integer.valueOf(line.substring(1, x));
				b = Integer.valueOf(line.substring(x + 1, line.length() - 1));
				vertices = Math.max(a, b);
				t[l] = vertices;
				l++;
			}

			int max = t[0];

			for (int i = 1; i < t.length; i++) {
				if (max < t[i])
					max = t[i];
			}
			vertices = max;
			System.out.println("Vertices: " + (vertices + 1));
			Graphh g = new Graphh(vertices + 1);
			matrix = new int[vertices + 1][vertices + 1];
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					matrix[i][j] = 0;
				}
			}
			scanner.close();
			Scanner scanner1 = new Scanner(new File("dane.txt"));
			String line1;

			scanner1.nextLine();
			while (scanner1.hasNextLine()) {
				line1 = scanner1.nextLine();
				x = line1.indexOf(",");
				a = Integer.valueOf(line1.substring(1, x));
				b = Integer.valueOf(line1.substring(x + 1, line1.length() - 1));
				matrix[a][b] = 1;
				// matrix[b][a] = 1;
				g.addEdge(a, b);
			}

			/*
			 * for (int i = 0; i < matrix.length; i++) { for (int j = 0; j <
			 * matrix[i].length; j++) { System.out.print(matrix[i][j]); }
			 * System.out.println(""); }
			 */
			scanner1.close();
			Bipartite bip = new Bipartite(vertices);
			System.out.println("");
			if (bip.isBipartite(matrix, 0))
				System.out.println("Graph is bipartite");
			else
				System.out.println("Graph isn't bipartite");
			System.out.println("");
			g.topologicalSort();
			System.out.println("");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

}

class Stack {
	private Integer[] tab;
	private int free = 0;

	public Stack() {
		tab = new Integer[20];
		for (int i = 0; i < tab.length; i++) {
			tab[i] = null;
		}
	}

	public void push(int v) {
		if (free < tab.length) {
			tab[free] = v;
			free++;
		}
	}

	public int pop() {
		int tmp = tab[free - 1];
		free--;
		return tmp;
	}

	public int size() {
		int size = 0;
		for (int i = 0; i < free; i++)
			size++;
		return size;
	}
}

class List {
	private Integer[] tab;
	private int s = 0, start = 0;

	public List(int v) {
		tab = new Integer[v + 1];
		for (int i = 0; i < tab.length; i++) {
			tab[i] = null;
		}
	}

	public List() {
		tab = new Integer[512];
		for (int i = 0; i < tab.length; i++) {
			tab[i] = null;
		}
	}

	public int get(int i) {
		return tab[i].intValue();
	}

	public int size() {
		return s;
	}

	public int poll() {
		int head = 0;
		for (int i = 0; i < tab.length; i++)
			if (tab[i] != null) {
				head = tab[i];
				tab[i] = null;
				s--;
				break;
			}
		return head;
	}

	public void add(int v) {
		if (start <= tab.length) {
			for (int i = start; i < tab.length; i++)
				if (tab[i] == null) {
					tab[i] = v;
					start++;
					s++;
					break;
				}
		}
	}

	public void addFirst(int v) {
		for (int i = start; i > -1; i--) {
			tab[i + 1] = tab[i];
		}
		s++;
		tab[0] = v;
		start++;
	}

	public void print() {
		for (int i = 0; i < tab.length; i++)
			System.out.print(tab[i] + " ");
		System.out.println("");
	}
}

class Bipartite {

	private static int V;
	private int[] colorTab;

	public Bipartite(int v) {
		V = v;
		colorTab = new int[V];
	}

	public boolean isBipartite(int[][] G, int start) {
		for (int i = 0; i < V; i++)
			colorTab[i] = -1;

		colorTab[start] = 1;
		List q = new List();
		q.add(start);
		while (q.size() != 0) {
			int u = q.poll();

			if (G[u][u] == 1)
				return false;

			for (int v = 0; v < V; ++v) {
				if (G[u][v] == 1 && colorTab[v] == -1) {
					colorTab[v] = 1 - colorTab[u];
					q.add(v);
				} else if (G[u][v] == 1 && colorTab[v] == colorTab[u])
					return false;
			}
		}
		return true;
	}
}

class Graphh {
	private int v;
	private List[] adj;

	public Graphh(int v) {
		this.v = v;
		adj = new List[v];
		for (int i = 0; i < v; i++)
			adj[i] = new List();
	}

	public void addEdge(int v, int u) {
		adj[v].addFirst(u);
	}

	private boolean isCycleUtil(int vertex, boolean[] visited, boolean[] recursiveArr) {
		visited[vertex] = true;
		recursiveArr[vertex] = true;

		for (int i = 0; i < adj[vertex].size(); i++) {
			int adjVertex = adj[vertex].get(i);
			if (!visited[adjVertex] && isCycleUtil(adjVertex, visited, recursiveArr)) {
				return true;
			} else if (recursiveArr[adjVertex])
				return true;
		}
		recursiveArr[vertex] = false;
		return false;
	}

	private boolean isCyclic() {
		boolean[] visited = new boolean[v];
		boolean[] recStack = new boolean[v];

		for (int i = 0; i < adj.length; i++) {
			if (isCycleUtil(i, visited, recStack))
				return true;
		}

		return false;
	}

	public void topologicalSort() {
		if (!isCyclic()) {
			boolean[] visited = new boolean[v];
			Stack st = new Stack();
			for (int i = 0; i < v; i++) {
				if (!visited[i]) {
					topologicalSortUtil(i, visited, st);
				}
			}
			System.out.println("Topological Sort: ");
			int size = st.size();
			for (int i = 0; i < size; i++) {
				System.out.print(st.pop() + " ");
			}
		} else
			System.out.println("Is cycle in graph.");
	}

	void topologicalSortUtil(int start, boolean[] visited, Stack stack) {
		visited[start] = true;
		for (int i = 0; i < adj[start].size(); i++) {
			int vertex = adj[start].get(i);
			if (!visited[vertex])
				topologicalSortUtil(vertex, visited, stack);
		}
		stack.push(start);
	}
}
