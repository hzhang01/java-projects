
import java.io.*;
import java.util.*;

// Successful submit 
public class DFSExample {

	// Global variables
	static int time = 0;	
	static boolean loop = false;
	static LinkedList<Integer> list = new LinkedList<Integer>();
	
	public static void main (String [] args) {
		// Reading input 
		FileReader file = null;
		BufferedReader in = null;
		try {
			// Reading the file
			file = new FileReader("test.txt"); in = new BufferedReader(file);
			//in = new BufferedReader(new InputStreamReader(System.in));
			String curReadLine = in.readLine();
			String [] vars = curReadLine.split("\\s+");
			int stages = Integer.parseInt(vars[0]);
			int relations = Integer.parseInt(vars[1]);
			
			// Lists
			int [][] edges = new int[stages] [stages];
			int [] nodes = new int[stages];
			int [] colors = new int [stages];
			int [] discovery = new int [stages];
			int [] finish = new int [stages];
			int [] temp = new int [stages];
			
			// Store relations
			curReadLine = in.readLine();
			for(int i = 0; i < relations; i++){
				String[] nums = curReadLine.split("\\s+");
				edges[Integer.parseInt(nums[0]) - 1][Integer.parseInt(nums[1]) - 1] = 1;
				curReadLine = in.readLine();
			}
			
			// Running DFS
			dfs(nodes, edges, colors, discovery, finish, temp);
			//Print out value
			if(!loop){
				System.out.println(0);
			}
//			for(int i = 0; i < nodes.length; i++){
//				System.out.print("node = " + nodes[i]);
//				System.out.print(", color = " + colors[i]);
//				System.out.print(", discovery = " + discovery[i]);
//				System.out.print(", finish = " + finish[i]);
//				System.out.print(", temp = " + temp[i]);
//				System.out.print("\n");
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void dfs(int [] nodes,int [][] edges, int colors[],
			int discovery[], int finish[], int temp[]){
		for(int i = 0; i < nodes.length; i++){ // Populate lists
			colors[i] = 1; // Set to white
			temp[i] = 0; // Set to default
			nodes[i] = i; // Set node index
		}
		for(int i = 0; i < nodes.length; i++){
			if(colors[i] == 1){ // Color is white
				list.add(nodes[i] + 1);
				dfsRec(nodes, edges, colors, discovery, finish, temp, i);
			}
		}
	}
	
	private static void dfsRec(int[] nodes, int [][] edges,
			 int colors[],int discovery[], int finish[], int temp [], int i){
		colors[i] = 2; //Grey
		discovery[i] = ++time;
		for (int j = 0; j < nodes.length; j++){
			if(edges[i][j] == 1){ // Successor 
				if(colors[j] == 1){ // Successor is white 
					list.add(j + 1); 
					dfsRec(nodes,edges,colors,discovery,finish,temp,j);
				}
				else{
					if(colors[j] == 2 && !loop){ // Successor is grey (loop)
						// Remove unwanted elements 
						int node = list.peekFirst();
						while(node != (j + 1)){
							list.removeFirst();
							node = list.peekFirst();
						}
						System.out.println(1);
						// Add to a remote list 
						for(int k = 0; k < list.size(); k++){
							System.out.print(list.get(k) + " ");
						}
						System.out.print("\n");
						loop = true;
						return;
					}
				}
			}
		}
		// Finish 
		colors[i] = 3;
		finish[i] = ++time;
		if(i != 0){
			list.removeLast();
		}
		
	}
}






