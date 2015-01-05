package tests.system.testprograms.multi_dimentional_array;

public class Program {

	public int value1 = 1;
	public int value2 = 2;
	
	public static void main(String[] args) {

		int[][] ints = {{1,2,3}, {4,5,6}, {7,8,9}};
		Program[][] ps = {{new Program(),new Program(),new Program()}, {new Program(),new Program(),new Program()}, {new Program(),new Program(),new Program()}};
	}

}
