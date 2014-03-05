package greedy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Intercalar as listas (que já chegam ordenadas) utilizando a menor quantidade de comparações possíveis..
 */
public class ListsIntersection {
	public int[] intercalate(int[]... lists) {
		return _intercalate(lists).arr;
	}

	/** The lists[x] can never be null */
	Pair _intercalate(int[]... lists) {
		if (lists.length == 0)
			return new Pair(0, new int[]{});
		if (lists.length == 1)
			return new Pair(0, lists[0].clone());

		Queue<int[]> queue = new PriorityQueue<int[]>(lists.length, new Comparator<int[]>() {
			@Override
			public int compare(int[] l1, int[] l2) {
				return l1.length == l2.length ? 0 : l1.length > l2.length ? 1 : -1;
			}
		});
		queue.addAll(Arrays.asList(lists));

		int operations = 0;
		while (queue.size() > 1) {
			int[] first = queue.poll();
			int[] sec = queue.poll();
			operations += first.length + sec.length - 1;
			int[] interc = interc(first, sec);
			queue.add(interc);
		}

		return new Pair(operations, queue.poll());
	}

	int[] interc(int[] l1, int[] l2) {
		int[] l3 = new int[l1.length + l2.length];

		int i = 0, j = 0;
		for (int k = 0; k < l3.length; k++) {

			if (i < l1.length) {
				if (j < l2.length && l2[j] <= l1[i])
					l3[k] = l2[j++];
				else
					l3[k] = l1[i++];
			} else {
				l3[k] = l2[j++];
			}
		}
		return l3;
	}

	static class Pair {
		public int operations;
		public int[] arr;

		public Pair(int operations, int[] arr) {
			this.operations = operations;
			this.arr = arr;
		}
	}

}
