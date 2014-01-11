package sorting;
import java.util.Arrays;

public class MergeSort extends Sort{

	public int[] sort(int[] arr) {
		print("to divide", arr);
		if (arr == null || arr.length <= 1)
			return arr.clone();

		int len = arr.length;
		int half = len / 2;
		int[] left = sort(Arrays.copyOfRange(arr, 0, half));
		int[] rigth = sort(Arrays.copyOfRange(arr, half, len));
		int[] result = merge(left, rigth);
		print("merged", result);
		return result;
	}

	public int[] merge(int[] left, int[] rigth) {
		int[] ret = new int[left.length + rigth.length];

		int l = 0, r = 0;
		for (int i = 0; i < ret.length; i++) {
			if (noMoreElementst(rigth, r)
					|| (stillHaveElements(left, l) && left[l] <= rigth[r])) {
				ret[i] = left[l];
				l++;
			} else {
				ret[i] = rigth[r];
				r++;
			}
		}

		return ret;
	}

	private boolean stillHaveElements(int[] left, int l) {
		return l <= left.length - 1;
	}

	private boolean noMoreElementst(int[] rigth, int r) {
		return r > rigth.length - 1;
	}
}