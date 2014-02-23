package problems;

/**
 * Write a method to decide if two strings are anagrams or not
 * 
 * Estou assumindo que a codificação é ASCII pois ai usa apenas um byte (256 bits)
 */
public class StringsAreAnagrams {
	public static boolean anagrams(String s1, String s2) {
		if(s1==null || s2==null || s1.length()!=s2.length())
			return false;
		
		int[] chars = new int[256];
		for(int c: s1.toCharArray()){
			chars[c] = chars[c]+1; 
		}
		
		for(int c: s2.toCharArray()){
			chars[c] = chars[c]-1;
			if(chars[c]<0)
				return false;
		}
		
		return true;
	}
}
