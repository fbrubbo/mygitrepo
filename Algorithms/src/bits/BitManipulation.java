package bits;


public class BitManipulation {
	
	public static final int LINUX_READ 		= 0b100;
	public static final int LINUX_WRITE 	= 0b010;
	public static final int LINUX_EXECUTE 	= 0b001;
	public static final int LINUX_ALL 		= LINUX_READ + LINUX_WRITE + LINUX_EXECUTE;

	public static int minusOne(){
		return -0b001;
	}
	
	public static int zero(){
		return 0b000;
	}
	
	public static int one(){
		return 0b001;
	}
	
	public static int two(){
		return 0b010;
	}
	
	public static int four(){
		return 0b100;
	}
	
	public static int eight(){
		return 0b1000;
	}
	
	public static int sexteen(){
		return 0b10000;
	}


}
