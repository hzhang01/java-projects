package hw2;

import java.io.*;
import java.util.*;

public class TopSortExample {

	public static void main (String [] args) {
		// Reading input 
		FileReader file = null;
		BufferedReader in = null;
		try {
			// Reading the file
			file = new FileReader("test.txt");
			in = new BufferedReader(file);
			//in = new BufferedReader(new InputStreamReader(System.in));
			String curReadLine = in.readLine();
			String [] vars = curReadLine.split("\\s+");
			int stages = Integer.parseInt(vars[0]);
			int relations = Integer.parseInt(vars[1]);

			// Adjacency and In-degree matrices 
			int [][] adj = new int[stages] [stages];
			int [] inDegree = new int [stages];

			// Store relations
			curReadLine = in.readLine();
			for(int i = 0; i < relations; i++){
				String[] nums = curReadLine.split("\\s+");
				adj [Integer.parseInt(nums[1]) - 1][Integer.parseInt(nums[0]) - 1] = 1;
				curReadLine = in.readLine();
			}

			int [] sorted = topologicSort(adj, inDegree, stages);
			// If topological sort fails 
			if(sorted[0] == - 1){
				System.out.println(-1);
			}
			// Print out the content of the top sort 
			else{
				for(int i = 0; i < sorted.length; i++){
					System.out.print(sorted[i] + " ");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int [] topologicSort(int [][] adj, int [] inDegree, int stages){
		// Topological Sort
		// Adding in-degree zero elements 
		int [] sorted = new int [stages];
		MinHeap buffer = new MinHeap(stages);
		for (int i = 0; i < adj.length; i++) {
			for (int j = 0; j < adj[i].length; j++) {
				if(adj[i][j] == 1){
					inDegree[i]++;
				}
				//System.out.print(adj[i][j] + " ");
			}
			if(inDegree[i] == 0){
				buffer.insert(i);
			}
			//System.out.println(inDegree[i]);
		}

		// Decrementing in-degree to removed elements 
		int sortedCounter = 0;
		while(!buffer.isEmpty()){
			int temp = buffer.deleteMin();
			// If top sort fails
			if(temp == -1){
				sorted[0] = -1;
				break;
			}
			sorted[sortedCounter] = temp + 1;
			for (int i = 0; i < stages; i++) {
				if(i != temp){
					if(adj[i][temp] == 1){
						inDegree[i]--;
						if(inDegree[i] == 0){
							buffer.insert(i);
						}
					}
				}
			}
			sortedCounter++;
//			// Buffer content
//			System.out.print("Buffer content: ");
//			for(int i = 0; i < buffer.size; i ++){
//				System.out.print(buffer.data[i] + " ");
//			}
//			System.out.println();
			// Sorted content
//			System.out.print("Sorted content: ");
//			for(int i = 0; i < sorted.length; i ++){
//				System.out.print(sorted[i] + " ");
//			}
//			System.out.println();
//			System.out.println();
		}
		// Fail condition
		if(sortedCounter != sorted.length){
			sorted[0] = -1;
		}
		return sorted;
	}
}
// For lexicographical order 
class MinHeap {
	ArrayList<Integer> data;

	public MinHeap(int size){
		data = new ArrayList<>();
	}

	public int parent (int index){
		return (index - 1) / 2;
	}
	
	public int leftChild (int index){
		return (index * 2 + 1);
	}
	
	public int rightChild (int index){
		return (index * 2 + 2);
	}
	
	public int indexCheck (int index){
		if(leftChild(index) > data.size() -1){
			return -1;
		}
		if(rightChild(index) > data.size() - 1){
			return leftChild(index);
		}
		if(data.get(rightChild(index)) < data.get(leftChild(index))){
			return rightChild(index);
		}
		else{
			return leftChild(index);
		}
	}
	public boolean isEmpty(){
		if(data.size() ==  0){
			return true;
		}
		return false;
	}
	
	public void insert(int value){
		data.add(value);
		bubbleUp(data.size() - 1);
	}
	
	public int deleteMin(){
		if(this.isEmpty()){
			throw new NullPointerException();
		}
		int min = data.get(0);
		data.set(0, data.get(data.size() - 1));
		data.remove(data.size() - 1);
		bubbledown(0);
		return min;
	}
	
	public void bubbleUp (int index){
		int cur = parent(index);
		while(data.get(cur) > data.get(index) && index > 0){
			swap(data,cur,index);
			index = cur;
			cur = parent(index);
		}
	}


	private void bubbledown(int index) {
		int cur = indexCheck(index);
		while(cur != -1 && data.get(cur) < data.get(index)){
			swap(data,cur,index);
			index = cur;
			cur = indexCheck(index);
		}	
	}
	
	private void swap (ArrayList<Integer> data, int x, int y){
		int temp = data.get(x);
		data.set(x, data.get(y));
		data.set(y, temp);

	}

}