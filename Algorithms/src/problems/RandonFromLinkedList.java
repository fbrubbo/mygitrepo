package problems;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * There is a linked list of numbers of length N. 
 * N is very large and you don’t know N. 
 * You have to write a function that will return k random numbers from the list. 
 * Numbers should be completely random. 
 * It should be done in O(n).
 * 
 */
public class RandonFromLinkedList {

	public int[] rand(LinkedList<Integer> list, int k) {
		int[] randon = new int[k];
		Random rand = new Random();
		
		int count = 0;
		Iterator<Integer> iter = list.iterator();
		while(iter.hasNext()){
			Integer value = iter.next();
			if(count<k) {
				randon[count] = value;
			} else {				
				int posi = Math.abs(rand.nextInt()) % (count + 1);
				if(posi<k) {
					randon[posi] = value;
				}
			}
			count++;
		}

		return randon;
	}

}
