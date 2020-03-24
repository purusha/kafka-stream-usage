package org.at.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WhatDo {

	public static void main(String[] args) {
						
		List<String> l1 = Arrays.asList("this is my brain".toLowerCase().split("\\W+"));
		
		List<String> l2 = Arrays.asList("1 2 3 4".toLowerCase().split("\\W+"));
		
		List<List<String>> concat = new ArrayList<>();
		concat.add(l1);
		concat.add(l2);
		
		System.out.println(concat);
		
		List<String> flatted = concat.stream()
			.flatMap(x -> x.stream())
			.collect(Collectors.toList());
		
		System.out.println(flatted);
		

	}

}
