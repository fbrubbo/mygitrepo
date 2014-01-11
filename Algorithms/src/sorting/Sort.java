package sorting;

public abstract class Sort {
	abstract int[] sort(int[] sort);
	
	void print(String msg, int[] arr2) {
		boolean first = true; 
		System.out.print(msg);
		for (int i : arr2) {
			System.out.print((first? ": " : ", ") + i);
			first=false;
		}
		System.out.println();
	}
}
