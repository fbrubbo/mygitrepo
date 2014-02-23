package dynamicprogramming;

/**
 * There is an array A[N] of N numbers. 
 * You have to compose an array Output[N] such that Output[i] will be equal to multiplication of 
 * all the elements of A[N] except A[i]. For example Output[0] will be multiplication of A[1] to A[N-1] 
 * and Output[1] will be multiplication of A[0] and from A[2] to A[N-1]. 
 * 
 * Solve it without division operator and in O(n).
 */
public class ArrayMultiplication {

	public static int[] multiplyAllButCurrentElement(int[] a) {
		if(a.length==0)
			return a;
		
 		int[] output = new int[a.length];
		
		output[0] = 1;
		for(int i=0; i<a.length-1; i++) {
			output[i+1] = a[i] * output[i];
		}
		
		int total = 1;
		for(int i=a.length-1; i>=0; i--) {
			output[i] = total * output[i];
			total *= a[i];			
		}
		
		return output;

		
/*		
 		int[] output = new int[a.length];
		
		int total = 1;
		for(int i=0; i<a.length; i++) {
			total *= a[i];
		}
		
		for(int i=0; i<a.length; i++) {
			output[i] = total/a[i];
		}
		
		return output;
*/
	}
}
