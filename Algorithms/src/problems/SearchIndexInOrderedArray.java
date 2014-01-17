package problems;

/**
 * Given an array whose elements are sorted, 
 * return the index of a the first occurrence of a specific integer. 
 * Do this in sub-linear time. I.e. do not just go through each element searching for that element
 */
public class SearchIndexInOrderedArray {

	public static int searchIndex(int[] arr, int x){
		return searchIndex(arr, x, 0, arr.length-1);
	}
	
	static int searchIndex(int[] arr, int x, int begin, int end){
		if(begin==end && x==arr[begin])
			return begin;
		
		int m = begin + ((end-begin)/2);
		if(x==arr[m]) {
			return m;
		} else if(x < arr[m]) {
			return searchIndex(arr, x, begin, m-1);
		} else {
			return searchIndex(arr, x, m+1, end);
		}
	}
}
