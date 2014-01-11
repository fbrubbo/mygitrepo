package sorting;

public class MergeSortForkJoinTest extends MergeSortTest {

	@Override
	public MergeSortWithForkJoin get() {
		return new MergeSortWithForkJoin();
	}

}