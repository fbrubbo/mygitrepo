package sorting;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MergeSortWithForkJoin extends MergeSort {
	
	class Sorter extends RecursiveAction {
		private static final long serialVersionUID = 1L;
		int[] arr;
		int[] result;
		
		public Sorter(int[] arr) {
			this.arr = arr;
		}
		
		@Override
		protected void compute() {
			print("to divide", arr);
			if (arr == null || arr.length <= 1) {
				result = arr.clone();
				return;
			}

			int len = arr.length;
			int half = len / 2;
			Sorter left = new Sorter(Arrays.copyOfRange(arr, 0, half));
			Sorter rigth = new Sorter(Arrays.copyOfRange(arr, half, len));
			invokeAll(left, rigth);
			result = merge(left.result, rigth.result);
			print("merged", result);
		}		
	}

	public int[] sort(int[] arr) {
		ForkJoinPool pool = new ForkJoinPool();
		Sorter sorter = new Sorter(arr);
		pool.invoke(sorter);
		return sorter.result;
	}
}