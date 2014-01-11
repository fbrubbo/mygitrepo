package sorting;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class QuickSortWithForkJoin extends QuickSort {

	class Sorter extends RecursiveAction {
		private static final long serialVersionUID = 1L;
		int[] arr; 
		int begin, end;
		
		public Sorter(int[] arr, int begin, int end) {
			this.arr = arr;
			this.begin = begin;
			this.end = end;
		}
		
		public void compute(){
			print("To partitionig", arr);
			if (arr == null || end - begin < 1)
				return;

			int pos = partitioning(arr, begin, end);
			print("After partitionig", arr);
			System.out.println("	POS: " + pos);			

			invokeAll(new Sorter(arr, begin, pos), new Sorter(arr, pos + 1, end));
			print("Sorted", arr);
		}
	}
	
	protected void sort(int[] arr, int begin, int end) {
		ForkJoinPool pool = new ForkJoinPool();
		pool.invoke(new Sorter(arr, begin, end));
	}

}
