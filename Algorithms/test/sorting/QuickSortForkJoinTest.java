package sorting;

public class QuickSortForkJoinTest extends QuickSortTest {

	@Override
	public QuickSortWithForkJoin get() {
		return new QuickSortWithForkJoin();
	}

}