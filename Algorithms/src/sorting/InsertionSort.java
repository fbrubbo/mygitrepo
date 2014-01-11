package sorting;

public class InsertionSort extends Sort{
	
	public int[] sort(int[] arr) {
		int[] cloned = arr.clone();
		_sort(cloned);
		return cloned;
	}
	
	private void _sort(int[] arr) {		
		for(int i=1; i<arr.length; i++){
			int key = arr[i];			
			int j=i-1;
			while(j>=0 && arr[j]>key){
				arr[j+1] = arr[j];
				j--;
			}
			arr[j+1] = key;
		}
	}
}
