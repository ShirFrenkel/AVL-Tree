
/**
 *
 * ---Tree list---
 * An implementation of a Tree list with key and info
 * 
 *@author Matan Ben Tov & Shir Frenkel
 *@version Final-1
 */

public class TreeList {

	private AVLTree tree;
	// length of list = tree.size()

	/**
	 * --CONSTRUCTOR-- O(1)
	 */
	public TreeList() {
		tree = new AVLTree();
	}

	public int length() {
		return tree.size();
	}

	/**
	 * --RETRIEVE-- O(log n)
	 * 
	 * @param i - index to be returned
	 * @return if 0 <= i < len --> Item in index i else --> null;
	 */
	public Item retrieve(int i) {
		if (tree.empty() || i < 0 || i > tree.size()) {
			return null;
		}
		return tree.selectItemByRank(i + 1);
	}

	/**
	 * --INSERT--
	 * @param i := index to insert new item
	 * @param k := key of the new item
	 * @param s := info of new item if 0 <= i <= this.length(), @return 0 & insert
	 *          new item(k,s), otherwise, @return -1
	 */
	public int insert(int i, int k, String s) {
		return this.tree.insertByIndex(i, k, s);
	}

	/**
	 * --DELETE--
	 * @param i := index of the item we need to delete in the list if 0 <= i <
	 *          this.length(), @return 0 & delete the i'th item, otherwise, @return -1
	 */
	public int delete(int i) {
		if (i < 0 || this.length() <= i) {
			return -1;
		}
		this.tree.deleteByIndex(i);
		return 0;
	}

}