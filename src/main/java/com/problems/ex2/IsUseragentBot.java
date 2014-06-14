package com.problems.ex2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.pig.FilterFunc;
import org.apache.pig.data.Tuple;

public class IsUseragentBot extends FilterFunc {

	private Set<String> blacklist = null;

	private void loadBlacklist() throws IOException {
		blacklist = new HashSet<String>();
		BufferedReader in = new BufferedReader(new FileReader("./blacklist"));

		String userAgent = null;
		while ((userAgent = in.readLine()) != null) {
			blacklist.add(userAgent);
		}
	}
	
	@Override
	public List<String> getCacheFiles() {
		List<String> list = new ArrayList<String>();
		list.add("/meta/blacklist.txt#blacklist");
		return list;
	}

	@Override
	public Boolean exec(Tuple tuple) throws IOException {
		if (blacklist == null) {
			loadBlacklist();
		}
		if (tuple == null || tuple.size() == 0) {
			return null;
		}
		String ua = (String) tuple.get(0);
		if (blacklist.contains(ua)) {
			return true;
		}
		return false;
	}

}
