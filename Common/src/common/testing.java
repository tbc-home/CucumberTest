package common;

import java.util.Random;

public class testing {
	public int generateRandom(int a, int b) {
		Random rand = new Random();
		int randomNum = rand.nextInt((b - a) + 1) + a;
		return randomNum;

	}

	public static void main(String ar[]) {
		testing test = new testing();
		System.out.println(test.generateRandom(2, 10));
	}
}
