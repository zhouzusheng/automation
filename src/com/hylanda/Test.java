package com.hylanda;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.AutomatonMatcher;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;

public class Test {
	
	static RegExp auto = new RegExp("([a-zA-Z][a-zA-Z0-9]*)://([^ /]+)(/[^ ]*)?");
	static Pattern p = Pattern.compile("([a-zA-Z][a-zA-Z0-9]*)://([^ /]+)(/[^ ]*)?");
	
	static RunAutomaton r_auto = new RunAutomaton(auto.toAutomaton());
	

	public static void main(String[] args) {
		
		RegExp simple = new RegExp("a{1,3}");
		Automaton  a1 = simple.toAutomaton();
		a1.run("abc");
		
		
		int count = 20000;
		long st = System.currentTimeMillis();
		long end = 0;
		String input = "http://www.sina.com.cn/";
		st = System.currentTimeMillis();
		for(int i = 0; i < count; i++) {
			AutomatonMatcher result = r_auto.newMatcher(input);
			if(result.find()){
				int s = result.start();
				int e = result.end();
				//System.out.println("1:" + input.substring(s, e));
			}
		}
		 end = System.currentTimeMillis();
		System.out.println("t1=" + (end - st)*1.0/count);
		
		st = System.currentTimeMillis();
		
		for(int i = 0; i < count; i++) {
			Matcher m = p.matcher(input);
			
			if(m.find()){
				int s = m.start();
				int e = m.end();
				//System.out.println("2:" +input.substring(s, e));
			}
		}
		 end = System.currentTimeMillis();
		 System.out.println("t2=" + (end - st)*1.0/count);
	}

}
