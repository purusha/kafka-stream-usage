package org.at;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Starter {

	public static void main(String[] args) {
		
		//final ExecutorService executor = Executors.newSingleThreadExecutor();
		final ExecutorService executor = Executors.newFixedThreadPool(5);
		executor.submit(new WordCount());
		executor.shutdown();
		
	}

}
