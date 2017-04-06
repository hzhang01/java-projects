
import java.io.*;
import java.util.*;

public class TwoThreeTreeExample {

    public static void main(String[] args) {
    	Scanner input = new Scanner(System.in);
        int entries = Integer.parseInt(input.next());
        TwoThreeTree tree = new TwoThreeTree();
        for(int count = 0;count < entries;count++){
            if(Integer.parseInt(input.next()) == 1){
            	tree.insert(input.next(),Integer.parseInt(input.next()));
            }
            else{
            	System.out.println(tree.search(input.next()));
            }
        }
        input.close();
    }
}

class Node {
   String guide;
   // guide points to max key in subtree rooted at node
}

class InternalNode extends Node {
   Node child0, child1, child2;
   // child0 and child1 are always non-null
   // child2 is null iff node has only 2 children
}

class LeafNode extends Node {
   // guide points to the key

   int value;
}

class TwoThreeTree {
   private Node root;
   private int height;
   
  public void insert(String key, int value){
	  	// Empty tree case
		if(this.root == null){
			LeafNode leaf = new LeafNode();
			leaf.guide = key; leaf.value = value;
			root = leaf; 
			root.guide = leaf.guide;
		}
		// Single leaf case
		else if(this.height == 0 && this.root != null){
			if(key.equals(root.guide)){
				LeafNode leaf = (LeafNode) root;
				leaf.value = value;
			}
			else{
				InternalNode nodeIn = new InternalNode();
				LeafNode leaf = new LeafNode();
				leaf.guide = key; leaf.value = value;
				if(key.compareTo(root.guide) < 0){
					nodeIn.child0 = leaf; nodeIn.child1 = root;
				}
				else{
					nodeIn.child0 = root; nodeIn.child1 = leaf;
				}
				nodeIn.guide = nodeIn.child1.guide;
				root = nodeIn; root.guide = nodeIn.guide;
				height++;
			}
			
		}
		// Other cases
		else{
			Node [] nodes = insert(this.root, key, value, this.height);
			InternalNode temp = (InternalNode) root;
			if(nodes[1] != null){
				InternalNode nodeIn = new InternalNode();
				nodeIn.child0 = nodes[0]; nodeIn.child1 = nodes[1];
				nodeIn.guide = nodeIn.child1.guide;
				this.root = nodeIn; height++;
			}
			else if(nodes[1] != null && temp.child2 != null){
				InternalNode nodeIn = new InternalNode();
				InternalNode left = new InternalNode();
				InternalNode right = new InternalNode();
				left.child0 = temp.child0; left.child1 = temp.child1;
				right.child0 = nodes[0]; right.child1 = nodes[1];
				left.guide = left.child1.guide; right.guide = right.child1.guide;
				nodeIn.guide = right.guide;
				nodeIn.child0 = left; nodeIn.child1 = right;
				this.root = nodeIn; height++;
			}
			else{
				if(nodes[0]== null){
					return;
				}
				// Questionable code
				if(root.guide.compareTo(nodes[0].guide) < 0){
					root.guide = nodes[0].guide;
				}
			}
		}
	}
  
  
  private Node [] insert(Node node, String key, int value, int height){
	  Node [] returnNodes = new Node [2];
	  InternalNode nodeIn = (InternalNode) node;
	  if(height == 1){
		  if(nodeIn.child2 != null){
			  if(key.equals(nodeIn.child0.guide) 
					  || key.equals(nodeIn.child1.guide) 
					  || key.equals((nodeIn.child2.guide))){
				  if(key.equals(nodeIn.child0.guide)){
					  LeafNode leaf = (LeafNode) nodeIn.child0;
					  leaf.value = value;
				  }
				  else if(key.equals(nodeIn.child1.guide)){
					  LeafNode leaf = (LeafNode) nodeIn.child1;
					  leaf.value = value;
				  }
				  else{
					  LeafNode leaf = (LeafNode) nodeIn.child2;
					  leaf.value = value;
				  }
				  returnNodes[0] = null;
				  return returnNodes;
			  }
			  
			  // Make two new nodes and appropriate children and return them 
			  InternalNode left = new InternalNode();
			  InternalNode right = new InternalNode();
			  LeafNode leaf = new LeafNode();
			  leaf.guide = key; leaf.value = value;
			  if(key.compareTo(nodeIn.child0.guide) < 0){
				  left.child0 = leaf; left.child1 = nodeIn.child0;
				  right.child0 = nodeIn.child1; right.child1 = nodeIn.child2;
			  }
			  else if(key.compareTo(nodeIn.child1.guide) < 0){
				  left.child0 = nodeIn.child0; left.child1 = leaf;
				  right.child0 = nodeIn.child1; right.child1 = nodeIn.child2;
			  }
			  else if(key.compareTo(nodeIn.child2.guide) < 0){
				  left.child0 = nodeIn.child0; left.child1 = nodeIn.child1;
				  right.child0 = leaf; right.child1 = nodeIn.child2;
			  }
			  else{
				  left.child0 = nodeIn.child0; left.child1 = nodeIn.child1;
				  right.child0 = nodeIn.child2; right.child1 = leaf;
			  }
			  left.guide = left.child1.guide; right.guide = right.child1.guide;
			  returnNodes[0] = left; returnNodes[1] = right;
			  return returnNodes;
		  }
		  else{
			  if(key.equals(nodeIn.child0.guide) || key.equals(nodeIn.child1.guide)){
				  if(key.equals(nodeIn.child0.guide)){
					  LeafNode leaf = (LeafNode) nodeIn.child0;
					  leaf.value = value;
				  }
				  else{
					  LeafNode leaf = (LeafNode) nodeIn.child1;
					  leaf.value = value;
				  }
				  returnNodes[0] = null;
				  return returnNodes;
			  }
			  LeafNode leaf = new LeafNode();
			  leaf.guide = key; leaf.value = value;
			  if(key.compareTo(nodeIn.child0.guide) < 0){
				  nodeIn.child2 = nodeIn.child1; nodeIn.child1 = nodeIn.child0; nodeIn.child0 = leaf; 
			  }
			  else if(key.compareTo(nodeIn.child1.guide) < 0){
				  nodeIn.child2 = nodeIn.child1; nodeIn.child1 = leaf;
			  }
			  else{
				  nodeIn.child2 = leaf; 
			  }
			  nodeIn.guide = nodeIn.child2.guide;
			  returnNodes[0] = nodeIn; returnNodes[1] = null;
			  return returnNodes;
		  }
	  }
	  else{
		  if(key.compareTo(nodeIn.child0.guide) <= 0){
			  returnNodes = insert(nodeIn.child0,key,value,height-1);
		  }
		  else if(key.compareTo(nodeIn.child1.guide) <= 0 || nodeIn.child2 == null){
			  returnNodes = insert(nodeIn.child1,key,value,height-1);
		  }
		  else{
			  returnNodes = insert(nodeIn.child2,key,value,height-1);
		  }
		  if(returnNodes[0] != null && returnNodes[1] != null){
			  // We had to split the child as it had four children
			  if(nodeIn.child2 != null){
				  InternalNode left = new InternalNode();
				  InternalNode right = new InternalNode();
				  if(returnNodes[1].guide.compareTo(nodeIn.child1.guide) < 0){
					  right.child0 = nodeIn.child1; right.child1 = nodeIn.child2;
					  left.child0 = returnNodes[0]; left.child1 = returnNodes[1];
					  
				  }
				  else if(returnNodes[1].guide.compareTo(nodeIn.child2.guide) < 0){
					  left.child0 = nodeIn.child0; right.child1 = nodeIn.child2;
					  left.child1 = returnNodes[0]; right.child0 = returnNodes[1];
				  }
				  else{
					  left.child0 = nodeIn.child0; left.child1 = nodeIn.child1;
					  right.child0 = returnNodes[0]; right.child1 = returnNodes[1];
				  }
				  left.guide = left.child1.guide; right.guide = right.child1.guide;
				  returnNodes[0] = left; returnNodes[1] = right;
				  return returnNodes;
			  }
			  else{
				  if(returnNodes[0] == null){
					  return returnNodes;
				  }
				  //returnNodes[0] = nodeIn; returnNodes[1] = null;
				  else if(returnNodes[1].guide.compareTo(nodeIn.child0.guide) <= 0){
					  nodeIn.child2 = nodeIn.child1; nodeIn.child0 = returnNodes[0]; nodeIn.child1 = returnNodes[1]; 
				  }
				  else{
					  nodeIn.child1 = returnNodes[0]; nodeIn.child2 = returnNodes[1];
				  }
				  nodeIn.guide = nodeIn.child2.guide;
				  returnNodes[0] = nodeIn; returnNodes[1] = null;
				  return returnNodes;
			  }
		  }
	  }
	return returnNodes;
  }
  
  public int search(String key){
	  if(this.height == 0 && key.equals(this.root.guide)){
		  LeafNode leaf = (LeafNode) root;
		  return leaf.value;
	  }
	  return search(key,this.root,this.height);
  }
  
  private int search(String key, Node node, int height){
	  
	  if(height > 0){
		  InternalNode nodeIn = (InternalNode) node;
		  if(key.compareTo(nodeIn.child0.guide) <= 0){
			  return search(key,nodeIn.child0,height-1);
		  }
		  else if(key.compareTo(nodeIn.child1.guide) <= 0 || nodeIn.child2 == null){
			  return search(key,nodeIn.child1,height-1);
		  }
		  else{
			  return search(key,nodeIn.child2,height-1);
		  }
	  }
	  else{
		  if (key.equals(node.guide)){
			  LeafNode leaf = (LeafNode) node;
			  return leaf.value;
		  }
		  else{
			  return -1;
		  }
	  }
  }
}
