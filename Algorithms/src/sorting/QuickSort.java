package sorting;

public class QuickSort extends Sort{

	public int[] sort(int[] arr) {
		int[] cloned = arr.clone();
		sort(cloned, 0, arr.length - 1);
		return cloned;
	}

	protected void sort(int[] arr, int begin, int end) {
		print("To partitionig", arr);
		if (arr == null || end - begin < 1)
			return;

		int pos = partitioning(arr, begin, end);
		print("After partitionig", arr);
		System.out.println("	POS: " + pos);
		sort(arr, begin, pos);
		sort(arr, pos + 1, end);
		print("Sorted", arr);
	}

	int partitioning(int[] arr, int begin, int end) {
		int x = arr[begin];
		int i = begin - 1;
		int j = end + 1;
		while (true) {
			while (arr[--j] > x) {}
			while (arr[++i] < x) {}
			if (i < j) {
				int aux = arr[i];
				arr[i] = arr[j];
				arr[j] = aux;
			} else {
				return j;
			}
		}
	}
}