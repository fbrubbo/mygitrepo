package sorting;

public class HeapSort {

	public void sort(int[] arr){
		buildHeap(arr);
		for(int i=arr.length-1; i>=0; i--){
			int aux = arr[0];
			arr[0] = arr[i];
			arr[i] = aux;
			heapfy(arr, 1, i);
		}
	}
	
	void buildHeap(int[] arr) {
		for (int i = arr.length / 2; i > 0; i--) {
			heapfy(arr, i);
		}
	}

	void heapfy(int[] arr, int i) {
		heapfy(arr, i, arr.length);
	}
	
	void heapfy(int[] arr, int i, int length) {
		if (i <= 0)
			throw new IllegalArgumentException("start index can not be <= 0");

		int ep = i - 1;
		int lp = (i * 2) - 1;
		if (lp > length - 1)
			return;

		int biggerp = lp;
		int rp = lp + 1;
		if (rp <= length - 1)
			biggerp = arr[lp] >= arr[rp] ? lp : rp;

		if (arr[biggerp] > arr[ep]) {
			int aux = arr[ep];
			arr[ep] = arr[biggerp];
			arr[biggerp] = aux;
		}
		heapfy(arr, biggerp + 1, length);
	}
}
