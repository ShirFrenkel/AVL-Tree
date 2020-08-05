/**
 * ---AVLTree---
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 * 
 *@author Matan Ben Tov & Shir Frenkel
 *@version Final-1
 */

public class AVLTree {

	private AVLNode root, min, max;


	/**
	 * --CONSTRUCTOR--
	 * Complexity - O(1)
	 */
	public AVLTree() {
		this.root = null;
		this.min = null;
		this.max = null;
	}

	/**
	 * --EMPTY--
	 * Complexity - O(1)
	 */
	public boolean empty() {
		return this.root == null;
	}

	/**
	 * --MIN--
	 * Complexity - O(1)
	 * @return the info of the AVLNode with the smallest key
	 */
	public String min() {
		if (this.min == null) {  // this.empty() == true
			return null;
		}
		return this.min.getValue();
	}

	/**
	 * --MAX--
	 * @return the info of the AVLNode with the biggest key
	 */
	public String max() {
		if (this.max == null) {  // this.empty() == true
			return null;
		}
		return this.max.getValue();
	}


	/**
	 * --SIZE--
	 * Complexity - O(1)
	 */
	public int size() {
		if(this.root==null)
		{
			return 0;
		}
		return this.root.getSize();
	}

	/**
	 * --GETROOT--
	 * Complexity - O(1)
	 * @return the root AVL node, or null if the tree is empty
	 */
	public IAVLNode getRoot() 
	{
		return this.root;
	}


	/**
	 * --INSERT--
	 * Complexity - O(logn)
	 * @param k - key to be inserted
	 * @param i - info value to be inserted
	 * @post if key not in the the tree: inserts key to the tree, else nothing
	 * @return number of rotations done
	 */
	public int insert(int k, String i) {

		AVLNode parent = findByKey(k);

		if (parent == null) {// empty tree
			this.root = new AVLNode(k, i);
			this.max = this.root;
			this.min = this.root;
			return 0;
		}

		if (parent.getKey() == k) {// key is already in the tree
			return -1;
		}

		// insert the item:
		AVLNode new_node = new AVLNode(k, i, parent);
		if (k < parent.getKey()) {
			parent.setLeft(new_node);
		} else {
			parent.setRight(new_node);
		}

		//updates min/max if needed
		if (new_node.getKey() < this.min.getKey()) {
			this.min = new_node;
		}
		if (this.max.getKey() < new_node.getKey()) {
			this.max = new_node;
		}

		// rebalances and returns the rotations counter:
		return rebalanceUpwards(new_node);
	}

	/**
	 * --SEARCH--
	 * Complexity - O(log n)
	 * @param k - key to be searched
	 */
		public String search(int k) {
			AVLNode node = findByKey(k); // can be the node or the suitable parent
			if (node == null) {
				return null;
			}
			if (node.getKey() == k) {
				return node.getValue();
			}
			return null;
		}
		
	/**
	 * --FIND BY KEY--
	 * Complexity - O(logn)
	 * @param k - key to be found
	 * @return if k in the tree: the node in this tree that Node.getKey() == k <br>
	 *         else: the suitable parent for new AVLNode with key == k
	 */
	public AVLNode findByKey(int k) {
		AVLNode node = this.root, parent = null;

		while (node != null) {
			parent = node;

			if (k == node.getKey()) {// key found
				return node;
			}

			if (k < node.getKey()) {
				node = (AVLNode) node.getLeft();
			} else {
				node = (AVLNode) node.getRight();
			}
		}

		// parent: null for empty tree OR suitable parent for the key
		return parent;
	}


	/**
	 * --REBALANCE UPWARDS-- 
	 * Complexity - O(logn)
	 * @post fixed tree (no AVL criminals & updated sizes+heights)
	 */
	private int rebalanceUpwards(AVLNode node) {
		AVLNode curr_parent = (AVLNode) node;

		int rotation_cou = 0;

		while (curr_parent != null) {

			// updates height and size
			curr_parent.updateHeight();
			curr_parent.updateSize();

			int curr_BF = curr_parent.getBF();

			// IF AVL criminal found, 4 cases possible:
			if (Math.abs(curr_BF) == 2) {
				if (curr_BF < 0) { // -->subtree tends to the Right
					AVLNode right_child = (AVLNode) curr_parent.getRight();

					if (right_child.getBF() <= 0) { // RR Case
						curr_parent = rotateLeft(curr_parent);

					} else if (right_child.getBF() > 0) { // RL Case
						rotateRight(right_child); // 
						curr_parent = rotateLeft(curr_parent);
						rotation_cou++;
					}
				}

				if (curr_BF > 0) { // -->subtree tends to the Left
					AVLNode left_child = (AVLNode) curr_parent.getLeft();

					if (left_child.getBF() >= 0) { // LL Case
						curr_parent = rotateRight(curr_parent);

					} else if (left_child.getBF() < 0) { // LR Case
						rotateLeft(left_child);
						curr_parent = rotateRight(curr_parent);
						rotation_cou++;
					}
				}
				rotation_cou++; // in any case one rotation was done
			} 
			// end of criminal cases


			// go up the path:
			curr_parent = (AVLNode) curr_parent.getParent();

		}
		return rotation_cou;
	}

	/**
	 * --REBALANCE HELPER: ROTATE LEFT--
	 * Complexity - O(1)
	 * @param node - the node on which the desired rotation
	 * @pre assumes input has right child (otherwise its not left-rotatable)
	 * @post this.tree was rotated left by given node
	 * @return substitute of the given node, after being rotated
	 */
	private AVLNode rotateLeft(AVLNode node) // will return the new parent
	{// edge case updates root
		AVLNode new_parent = (AVLNode) node.getRight();

		// rotation - part 1 of 2:
		node.setRight(new_parent.getLeft());

		// parents updates:
		new_parent.setParent(node.getParent());

		if (new_parent.getParent() == null) {// node was the root
			this.root = new_parent;
		} else {// node had a parent
			if (new_parent.getParent().getLeft() == node) {
				new_parent.getParent().setLeft(new_parent);
			}
			if (new_parent.getParent().getRight() == node) {
				new_parent.getParent().setRight(new_parent);
			}

		}

		node.setParent(new_parent);
		if (node.getRight() != null) {
			node.getRight().setParent(node);
		}

		// rotation - part 2 of 2:
		new_parent.setLeft(node);
		
		return new_parent;
	}

	/**
	 * --REBALANCE HELPER: ROTATE RIGHT--
	 * Complexity - O(1)
	 * @param node - the node on which the desired rotation
	 * @pre assumes input has right child (otherwise its not right-rotatable)
	 * @post this.tree was rotated left by given node
	 * @return substitute of the given node, after being rotated
	 */
	private AVLNode rotateRight(AVLNode node) {
		AVLNode new_parent = (AVLNode) node.getLeft();

		// rotation - part 1 of 2:
		node.setLeft(new_parent.getRight());

		// parents updates:
		new_parent.setParent(node.getParent());
		if (new_parent.getParent() == null) {// node was the root
			this.root = new_parent;
		} else { // node had a parent
			if (new_parent.getParent().getLeft() == node) {
				new_parent.getParent().setLeft(new_parent);
			}
			if (new_parent.getParent().getRight() == node) {
				new_parent.getParent().setRight(new_parent);
			}
		}

		node.setParent(new_parent);
		if (node.getLeft() != null) {
			node.getLeft().setParent(node);
		}

		// rotation - part 2 of 2:
		new_parent.setRight(node);

		return new_parent;
	}

	/**
	 * --DELETE NODE--
	 * Complexity - O(logn)
	 * @pre nodeToDel != null
	 * @param nodeToDel := the node that need to be deleted
	 * @post delete nodeToDel, @return number of rotations done
	 * 
	 */
	public int deleteNode(AVLNode deleteNode) {
		
		// update min or max if needed
		if (deleteNode == this.max) {
			this.max = (AVLNode) this.getPredecessor(this.max); // works for root too
		}
		if (deleteNode == this.min) {
			this.min = (AVLNode) this.getSuccessor(this.min); // works for root too
		}

		// option 1 - deleteNode is leaf
		if (deleteNode.isLeaf()) {
			return this.deleteLeaf(deleteNode);
		}

		// option 2 - deleteNode has one child
		if (deleteNode.hasOneChild()) {
			return this.deleteNodeOneChild(deleteNode);
		}

		// option 3 - deleteNode has two children --> deleteNode has to have a successor
		AVLNode del_succes = this.getSuccessor(deleteNode); // del_succes:= deleteNode's successor
		
		// delete del_succes from the tree and than replace deleteNode with del_succes
		// del_succes can have 1 child / 0 child
		// no need to check if del_succes is min/max because it is deleted temporarily!
		int rotationCount = -2;  // rotation counter (tmp value)
		// option 1 - del_succes is leaf
		if (del_succes.isLeaf()) {
			rotationCount = this.deleteLeaf(del_succes);
		}

		// option 2 - del_succes has one child
		if (del_succes.hasOneChild()) {
			rotationCount = this.deleteNodeOneChild(del_succes);
		}
		
		// replace deleteNode by del_succes:
		
		if(deleteNode == this.root) {
			this.root = del_succes;
		}
		
		// set deleteNode's right son as del_succes's right son
		del_succes.setRight(deleteNode.getRight());  // del_succes's size, height updates
		if(del_succes.getRight() != null) {
			del_succes.getRight().setParent(del_succes);
		}
		
		// set deleteNode's left son as del_succes's left son
		del_succes.setLeft(deleteNode.getLeft());  // del_succes's size, height updates
		if(del_succes.getLeft() != null) {
			del_succes.getLeft().setParent(del_succes);
		}
		
		if(deleteNode.getParent() != null) {
			// set deleteNode's parent as del_succes parent instead
			boolean side = ((AVLNode) deleteNode.getParent()).getSideOf(deleteNode);  
			((AVLNode) deleteNode.getParent()).setSide(side, del_succes);
			del_succes.setParent(deleteNode.getParent());
		}
		
		// disconnect deleteNode
		deleteNode.setParent(null);
		deleteNode.setLeft(null);
		deleteNode.setRight(null);
		
		return rotationCount;  
		
	}
	
	/**
	 * --DELETE--
	 * Complexity - O(logn)
	 * @param k == key of the node that need to be deleted
	 * if k in the tree: @post delete the specified node, @return number of
	 * rotations done otherwise, @return -1
	 * 
	 */
	public int delete(int k) {
		AVLNode deleteNode = this.findByKey(k);
		if (deleteNode == null) {
			return -1;
		}
		if (deleteNode.getKey() != k) { // k is not in this tree
			return -1;
		}

		return this.deleteNode(deleteNode);

	}

	/**
	 * --DELETE HELPER: DELETE LEAF--
	 * Complexity - O(logn)
	 * @pre deleteNode.isLeaf()
	 * @param deleteNode := the node that need to be deleted
	 * @return number of rotations done
	 */
	private int deleteLeaf(AVLNode deleteNode) {
		if (deleteNode == this.root) {
			this.root = null;
			return 0; // tree --> empty
		}
		AVLNode del_parent = (AVLNode) deleteNode.getParent(); // del_parent is the parent of deleteNode
		boolean side = del_parent.getSideOf(deleteNode);
		this.disconnect(del_parent, side);
		// rebalances and returns the rotations counter:
		return rebalanceUpwards(del_parent);
	}

	/**
	 * --DELETE HELPER: deleteNodeOneChild--
	 * Complexity - O(logn)
	 * @pre deleteNode.hasOneChild()
	 * @param deleteNode := the node that need to be deleted
	 * @return number of rotations done
	 */
	private int deleteNodeOneChild(AVLNode deleteNode) {
		AVLNode del_child = deleteNode.getOnlyChild(); // del_child := deleteNode's child
		AVLNode del_parent = (AVLNode) deleteNode.getParent(); // del_parent := deleteNode's parent
		if (del_parent == null) { // deleteNode == this.root
			this.root = del_child;
			del_child.setParent(null);
			return 0; // there are no nodes to fix upwards because there are no nodes above the root
		}
		boolean side = del_parent.getSideOf(deleteNode);
		this.connect(del_parent, side, del_child);
		if (deleteNode == this.root) {
			this.root = del_child;
		}
		// rebalances and returns the rotations counter:
		return rebalanceUpwards(del_parent);
	}

	/**
	 * --DELETE HELPER: connect--
	 * Complexity - O(1)
	 * @pre parent, newChild != null
	 * @param side == false (left) || side == true (right)
	 * @post connect newChild to parent side, previous child's parent --> null 
	 * NOT updating sizes, heights (rebalanceUpwards() do that)
	 *       
	 */
	private void connect(AVLNode parent, boolean side, AVLNode newChild) {
		if(parent.getSide(side) != null) {
			parent.getSide(side).setParent(null); // preChild.setParent(null);
		}
		parent.setSide(side, newChild);
		newChild.setParent(parent);
	}

	/**
	 * --DELETE HELPER: disconnect--
	 * Complexity - O(1)
	 * @pre parent != null
	 * @param side == false (left) || side == true (right) (the side of the child
	 *             that need to be disconnected)
	 * @post disconnect parent from child (parent.getSide), child.getParent --> null
	 *       && parent.getSide --> null 
	 *       NOT updating sizes&heights(rebalanceUpwards() do that)
	 *       
	 */
	private void disconnect(AVLNode parent, boolean side) {
		if(parent.getSide(side) != null) {
			parent.getSide(side).setParent(null);
			parent.setSide(side, null);
		}
	}

	/**
	 * --getSuccessor--
	 * Complexity - O(logn)
	 * @pre node != null
	 * @param node if node has a successor @return node's successor
	 *             otherwise, @return null
	 */
	private AVLNode getSuccessor(AVLNode node) {
		if (node.getRight() != null) { // Go right once, and then left all the way
			node = (AVLNode) node.getRight();
			while (node.getLeft() != null) {
				node = (AVLNode) node.getLeft();
			}
			return node;
		}

		// Go up from node until the first turn right
		AVLNode nodePar = (AVLNode) node.getParent();
		while (nodePar != null && (nodePar.getSideOf(node))) { // while nodePar != null && side of father is right (turn left from child)
			node = nodePar;
			nodePar = (AVLNode) node.getParent();
		}
		return nodePar;
	}

	/**
	 * --getPredecessor--
	 * Complexity - O(logn)
	 * @param node if node has a predecessor @return node's predecessor otherwise, @return
	 *             null
	 */
	private AVLNode getPredecessor(AVLNode node) {
		if (node.getLeft() != null) { // Go left once, and then right all the way
			node = (AVLNode) node.getLeft();
			while (node.getRight() != null) {  
				node = (AVLNode) node.getRight();
			}
			return node;
		}

		// Go up from node until the first turn left
		AVLNode nodePar = (AVLNode) node.getParent();
		while (nodePar != null && (!nodePar.getSideOf(node))) {  // while nodePar != null && side of father is left (turn right from child)
			node = nodePar;
			nodePar = (AVLNode) node.getParent();
		}
		return nodePar;
	}


	/**
	 * --KEYS TO ARRAY-- calls keysToArrayRec()
	 * Complexity - O(n)
	 * @return sorted array which contains all keys in the tree, or an empty array
	 *         if the tree is empty.
	 */
	public int[] keysToArray() {
		return this.keysToArrayRec(this.root);
	}

	/**
	 * --KEYS TO ARRAY - REC--
	 * Complexity - O(n)
	 * @return int[] res (a sorted array which contains all keys in the tree that
	 *         node is it's root) res.length == node.getSize
	 */

	private int[] keysToArrayRec(AVLNode node) {
		if (node == null) {
			return new int[0];
		}
		AVLNode left = (AVLNode) (node.getLeft());
		AVLNode right = (AVLNode) (node.getRight());
		int[] leftArr ,rightArr;

		leftArr = keysToArrayRec(left);
		rightArr = keysToArrayRec(right);

		int[] res = new int[node.getSize()];
		System.arraycopy(leftArr, 0, res, 0, leftArr.length); // res[0:leftArr.length] = leftArr
		res[leftArr.length] = node.getKey();
		System.arraycopy(rightArr, 0, res, leftArr.length + 1, rightArr.length); // res[leftArr.length + 1:] = rightArr

		return res;
	}

	/**
	 * --INFO TO ARRAY-- calls infoToArrayRec()
	 * Complexity - O(n)
	 * @return an array which contains all info in the tree, sorted by their
	 *         respective keys, or an empty array if the tree is empty.
	 */
	public String[] infoToArray() {
		return this.infoToArrayRec(this.root);
	}

	/**
	 * --INFO TO ARRAY - REC--
	 * Complexity - O(n)
	 * @return String[] res- an array which contains all info in the tree(*), sorted
	 *         by their respective keys 
	 *         (*)tree that node is it's root
	 *         res.length == node.getSize
	 * 
	 */
	private String[] infoToArrayRec(AVLNode node) {
		if (node == null) {
			return new String[0];
		}
		AVLNode left = (AVLNode) (node.getLeft());
		AVLNode right = (AVLNode) (node.getRight());
		String[] leftArr,rightArr;

		leftArr = infoToArrayRec(left);
		rightArr = infoToArrayRec(right);

		String[] res = new String[node.getSize()];
		System.arraycopy(leftArr, 0, res, 0, leftArr.length); // res[0:leftArr.length) = leftArr
		res[leftArr.length] = node.getValue();
		System.arraycopy(rightArr, 0, res, leftArr.length + 1, rightArr.length); // res[leftArr.length + 1:] = rightArr

		return res;
	}

	
	/**
	 * --SELECT--
	 * O(log n)
	 * @param k - rank to be looked for
	 * @pre 1 <= k <= size (=max-rank)!
	 * @pre tree is not empty (if it is empty, the input is invalid)
	 * @return node ranked with k
	 */
	public AVLNode selectNodeByRank(int k)
	{
		if (k == 1) {// rank 1 is minimun
			return this.min;
		}
		if (k == this.size()) {// rank size is maximum
			return this.max;
		}
		
		AVLNode node=this.root;
		while(node!=null)
		{
			int curr_rank = node.getSubtreeRank();
			if (k == curr_rank) {
				return node;
			}
			
			if (k < curr_rank) {
				node = (AVLNode) node.getLeft();
			}
			
			else {// k > curr_rank
				node = (AVLNode) node.getRight();
				k = k - curr_rank;
			}
		}
		System.out.println("erorrrrrr");
		return null; //shouldn't be here if input is valid
	}
	
	/**
	 * --SELECT ITEM--
	 *SAME as selectNode, returns *ITEM* instead of node
	 */
	public Item selectItemByRank(int k)
	{
		AVLNode selected=selectNodeByRank(k);
		if(selected==null)
			return null;//shouldnt be here
		return selected.getItem();
	}
	
	
	/**
	 *--INSERT BY INDEX--
	 * !WARNING! this function is not maintaining keys order, designed for implementing TreeList insert
	 *@param i := index to insert new item (index = rank - 1)
	 *@param k := key of the new item
	 *@param s := info of new item
	 *if 0 <= i <= this.size() @return 0 & insert new item(k,s) in i'th position, otherwise @return -1
	 */
	public int insertByIndex(int i, int k, String s) {
		if(i < 0 || this.size() < i ) { 
			return -1;
		}
		AVLNode newNode = new AVLNode(k, s);  // the node we need to insert to the tree in the i'th position
		
		if(this.empty()) {
			this.root = newNode;
			this.max = newNode;
			this.min = newNode;
			return 0;
		}
		
		AVLNode parent ;  // future parent of newNode
		boolean side;  // the side of the parent to connect newNode (right := true, left := false)
		
		// tree is not empty --> this.max, this.min != null
		if(i == this.size()) {
			parent = this.max;
			side = true;  // right
			this.max = newNode;
		}
		else {
			if(i == 0) {
				parent = this.min;
				side = false;  // left
				this.min = newNode;
			}
			else {
				AVLNode curr_i = selectNodeByRank(i+1);  // the current node in the tree in index i
				if(curr_i.getLeft() == null) {
					parent = curr_i;
					side = false;  // left
				}
				else {
					parent = this.getPredecessor(curr_i);
					side = true;  // right
				}
			}
		}

		// connect the new node to the parent & fix the tree
		this.connect(parent, side, newNode);
		this.rebalanceUpwards(parent);
		//this.rebalanceUpwards(parent);
		return 0;
		
	}
	
	/**
	 * --DELETE BY INDEX--
	 * !WARNING! this function is not maintaining keys order, designed for implementing TreeList delete
	 * @param i index @pre 0 <= i < this.size()  --> this.empty = false
	 * @post delete the i'th item
	 */
	public void deleteByIndex(int i) {
		AVLNode nodeToDel = selectNodeByRank(i+1);  // the node in the i'th position
		this.deleteNode(nodeToDel);
	}
	
	
	
	/**
	 * --AVLNode INTERFACE-- ! Do not delete or modify this - otherwise all tests
	 * will fail !
	 */
	public interface IAVLNode {
		public int getKey(); // returns node's key

		public String getValue(); // returns node's value [info]

		public void setLeft(IAVLNode node); // sets left child

		public IAVLNode getLeft(); // returns left child (if there is no left child return null)

		public void setRight(IAVLNode node); // sets right child

		public IAVLNode getRight(); // returns right child (if there is no right child return null)

		public void setParent(IAVLNode node); // sets parent

		public IAVLNode getParent(); // returns the parent (if there is no parent return null)

		public void setHeight(int height); // sets the height of the node

		public int getHeight(); // Returns the height of the node
	}

	/**
	 * --AVLNode CLASS--
	 */
	public class AVLNode implements IAVLNode {

		/**
		 * size := size of the tree that this is it's root
		 * 
		 * height := the length of the path from root of that tree to its farthest node
		 * (i.e. leaf node farthest from the root). A tree with only root node has
		 * height 0 and a tree with zero nodes has height -1
		 * 
		 */

		private Item node_item; // contains key & val
		private IAVLNode left, right, parent;
		private int height, size;

		/**
		 * --CONSTRUCTOR-- creates a new AVLNode that is not connected to others
		 *  Complexity - O(1)
		 */
		public AVLNode(int key, String info) {
			this.node_item = new Item(key, info);
			this.left = null;
			this.right = null;
			this.parent = null;
			this.height = 0;
			this.size = 1;
		}

		/**
		 * --CONSTRUCTOR-- creates a new AVLNode leaf & set this.parent <-- parent  
		 *  Complexity - O(1)
		 */
		public AVLNode(int key, String info, AVLNode parent) {
			this.node_item = new Item(key, info);
			this.left = null;
			this.right = null;
			this.parent = parent;
			this.height = 0;
			this.size = 1;
		}

		/**
		 * --GETBF--
		 * Complexity - O(1)
		 */
		public int getBF() {
			int[] heights = this.getChildrenHeights();
			return heights[0] - heights[1]; // this.left.getHeight() - this.right.getHeight()
		}

		/**
		 * --GETKEY--
		 * Complexity - O(1)
		 */
		public int getKey() {
			return this.node_item.getKey();
		}

		/**
		 * --GETVALUE--
		 * Complexity - O(1)
		 */
		public String getValue() {
			return this.node_item.getInfo();
		}

		/**
		 * --SETLEFT--
		 * Complexity - O(1)
		 */
		public void setLeft(IAVLNode node) {
			this.left = node;
			this.updateHeight();
			this.updateSize();
		}

		/**
		 * --GETLEFT--
		 * Complexity - O(1)
		 */
		public IAVLNode getLeft() {
			return this.left;
		}

		/**
		 * --SETRIGHT--
		 * Complexity - O(1)
		 */
		public void setRight(IAVLNode node) {
			this.right = node;
			this.updateHeight();
			this.updateSize();
		}

		/**
		 * --GETRIGHT--
		 * Complexity - O(1)
		 */
		public IAVLNode getRight() {
			return this.right;
		}

		/**
		 * --SETPARENT--
		 * Complexity - O(1)
		 */
		public void setParent(IAVLNode node) {
			this.parent = node;
		}

		/**
		 * --GETPARENT--
		 * Complexity - O(1)
		 */
		public IAVLNode getParent() {
			return this.parent;
		}

		/**
		 * --SETHEIGHT--
		 * Complexity - O(1)
		 */
		public void setHeight(int height) {
			this.height = height;
		}

		/**
		 * --SETITEM--
		 * Complexity - O(1)
		 */
		public void setItem(Item new_item) {
			this.node_item = new_item;
		}

		/**
		 * --updateHeight--
		 * Complexity - O(1)
		 * @post updates node's height according to children's heights
		 */
		public void updateHeight() {
			int[] heights = this.getChildrenHeights(); // heights[0] == left height, heights[1] == right height
			this.setHeight(1 + Math.max(heights[0], heights[1]));
		}

		/**
		 * --updateSize--
		 * Complexity - O(1)
		 * @post updates node's size according to children's sizes
		 */
		public void updateSize() {
			int[] sizes = this.getChildrenSizes();
			this.size = 1 + sizes[0] + sizes[1];
		}

		/**
		 * --getChildrenHeights--
		 * Complexity - O(1)
		 * @return heights array: heights[0] == left height, heights[1] == right height
		 * @defines empty tree height==-1
		 */
		private int[] getChildrenHeights() { 
			int[] heights = new int[2];
			if (this.left != null) {
				heights[0] = this.left.getHeight();
			} else {
				heights[0] = -1;
			}
			if (this.right != null) {
				heights[1] = this.right.getHeight();
			} else {
				heights[1] = -1;
			}
			return heights;
		}

		/**
		 * --getChildrenSizes--
		 * Complexity - O(1)
		 * @return sizes array: sizes[0] == left size, sizes[1] == right size
		 * @defines empty tree size == 0
		 */
		private int[] getChildrenSizes() {
			int[] sizes = new int[2];
			if (this.left != null) {
				sizes[0] = ((AVLNode) this.left).size;
			} else {
				sizes[0] = 0;
			}
			if (this.right != null) {
				sizes[1] = ((AVLNode) this.right).size;
			} else {
				sizes[1] = 0;
			}
			return sizes;
		}

		/**
		 * --GETHEIGHT--
		 * Complexity - O(1)
		 */
		public int getHeight() {
			return this.height;
		}

		/**
		 * --GETSIZE--
		 * Complexity - O(1)
		 */
		public int getSize() {
			return this.size;
		}
		
		 /**
		  *  --GET-SUBTREE-RANK--
		 * Complexity - O(1)
		 */
		public int getSubtreeRank() {
			return this.getChildrenSizes()[0] + 1;
		}
		 
		/**

		/**
		 * --SetSide--
		 * Complexity - O(1)
		 * @param side == false (left) || side == true (right)
		 * @post set this node's side @param side to @param newChild
		 */
		public void setSide(boolean side, AVLNode newChild) {
			if (side) { // side is right
				this.setRight(newChild);
			} else { // side is left
				this.setLeft(newChild);
			}
		}

		/**
		 * --getSide--
		 * Complexity - O(1)
		 * @param side == false (left) || side == true (right)
		 * @return AVLNode that in side @param side of this node
		 */
		public AVLNode getSide(boolean side) {
			if (side) { // side is right
				return (AVLNode) this.getRight();
			}
			// side is left
			return (AVLNode) this.getLeft();
		}

		/**
		 * --GETITEM--
		 * Complexity - O(1)
		 */
		public Item getItem() {
			return this.node_item;
		}

		/**
		 * --IS LEAF--
		 * Complexity - O(1)
		 */
		public boolean isLeaf() {
			return this.getLeft() == null && this.getRight() == null;
		}

		/**
		 * --HAS ONE CHILD--
		 * Complexity - O(1)
		 */
		public boolean hasOneChild() {
			int[] sizes = this.getChildrenSizes();
			return (sizes[0] == 0 && sizes[1] != 0) || (sizes[1] == 0 && sizes[0] != 0);
		}

		/**
		 * --getSideof--
		 * Complexity - O(1)
		 * @param child @pre child is child of this 
		 * if child == this.getLeft() @return false (left) otherwise, @return true (right)
		 */
		public boolean getSideOf(AVLNode child) {
			if (child == this.getLeft()) {
				return false;
			}
			return true;
		}

		/**
		 * --getOnlyChild--
		 * Complexity - O(1)
		 * @pre this.hasOneChild() == true
		 * @return only child of this
		 */
		public AVLNode getOnlyChild() {
			if (this.getLeft() != null) {
				return (AVLNode) this.getLeft();
			}
			return (AVLNode) this.getRight();
		}

	}

}
