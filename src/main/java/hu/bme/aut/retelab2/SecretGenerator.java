package hu.bme.aut.retelab2;

import java.util.Random;

public class SecretGenerator {
	private static final char[] CHARS =
			"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
	private static final Random RND = new Random();

	public static String generate(int N) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < N; i++)
			sb.append(CHARS[RND.nextInt(CHARS.length)]);
		return sb.toString();
	}
}
