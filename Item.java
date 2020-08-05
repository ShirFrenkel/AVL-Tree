
/**--ITEM--
 * a list element contains key [int] and info [string]
 * 
 *@author Matan Ben Tov & Shir Frenkel
 *@version Final-1
 */
public class Item{
	
	private int key;
	private String info;
	
	public Item (int key, String info){
		this.key = key;
		this.info = info;
	}
	
	public int getKey(){
		return key;
	}
	
	public String getInfo(){
		return info;
	}
}