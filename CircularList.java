
/**
 *---Circular list---
 * An implementation of a circular list with key and info
 * 
 *@author Matan Ben Tov & Shir Frenkel
 *@version Final-1
 */


public class CircularList {

	private int maxLen,start,len;
	private Item[] array;
	
	
	/**
	 *--CONSTRUCTOR--
	 * @param maxLen >= 0 
	 * @return Creates CircularList instance with given maxLen
	 */
	public CircularList(int maxLen) {
		this.maxLen=maxLen;
		this.start=0; //initialized by default
		this.len=0;
		this.array=new Item[maxLen]; 
	}

	
	
	/**
	 *--RETRIEVE--
	 * @param i >= 0
	 * @return the element in the in the ith position, else null
	 */
	public Item retrieve(int i) {
		
		if (i < 0 || i >= this.len)
		{
			return null;
		}
		return this.array[(this.start+i)%this.maxLen];
	}

	
	
	/**
	 *--INSERT--
	 *@param i - index to insert 
	 *@param k - element's key
	 *@param s - element's info
	 *@post inserts an item to the ith position in list with key k and info s
	 *@return -1 if i<0 or i>len or len=maxLen; Otherwise, item inserted and returns 0
	 */
	public int insert(int i, int k, String s) {

		if (i < 0 || i > this.len || this.len==this.maxLen) 
		{//array is full OR i is illegal index
			return -1;
		}
		
		if (i + 1 < len - i) 
		{//implies there are less element before the ith position
			for (int j = 0; j < i; j++)
			{			
				this.array[fixed_mod(start+j-1)]=this.array[fixed_mod(start+j)]; 
			}
			this.start=fixed_mod(start-1);
		}
		
		else 
		{//implies there are less element after the ith position OR the same amount
			int last=(start+len-1)%maxLen;
			for (int j=0;j<=len - i - 1;j++)
			{
				this.array[fixed_mod(last-j+1)]=this.array[fixed_mod(last-j)];
			}
		}
		
		this.len++;
		this.array[(start+i)%maxLen]=new Item(k,s);
		return 0;
	}
	

	

	/**
	 *--DELETE--
	 * @param i - index to be deleted
	 * @post deletes the item in the ith position from the list
	 * @return -1 if i<0 or i>len-1 or n=maxLen; otherwise return 0.
	 */
	public int delete(int i) {
		if (i < 0 || i > this.len-1) 
		{//i is illegal index
			return -1;
		}
		if (i - 1 < len - i - 1)
		{//implies there are less element before the ith position
			for(int j=0;j<i;j++)
			{
				this.array[fixed_mod(start+i-j)]=this.array[fixed_mod(start+i-j-1)];
			}
			this.array[(start)%maxLen]=null; //deletes the remaining item
			this.start=fixed_mod(start+1);
		}
		else
		{//implies there are less element after the ith position OR the same amount
			for(int j=0;j<len-i-1;j++)
			{
				this.array[fixed_mod(start+i+j)]=this.array[fixed_mod(start+i+j+1)];
			}
			this.array[(start+len-1)%maxLen]=null;//deletes the remaining item
		}
		this.len--;
		return 0;
	}
	
	
	
	/**--ADDITIONAL:--
	 *methods that weren't required, but should be there for the user
	 */
	public int getLen() {
		return this.len;
	}
	
	
	/**--HELPERS:--
	 ** methods for inner use
	 *--FIXED_MOD--
	 ** fixes java's modulu so it'll result positive remainder in any case
	 */
	private int fixed_mod(int j) {
		int r = j % maxLen;
		if (r < 0) {
			return maxLen + r;
		}
		return r;
	}
	
}
