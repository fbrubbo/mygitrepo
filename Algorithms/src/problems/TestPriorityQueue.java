package problems;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class TestPriorityQueue {

	public static void main(String[] args) {
		Queue<Integer> queue = new PriorityQueue<>(10, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2) * -1;
			}
		});
		queue.add(10);
		queue.add(9);
		queue.add(100);
		queue.add(1);
		queue.add(75);
		while(!queue.isEmpty()) {
			System.out.println( queue.poll() );
		}
	}
}
