package com.problems.ex3;

import java.io.IOException;

import org.apache.pig.Accumulator;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;

public class Sessionize extends EvalFunc<DataBag> implements Accumulator<DataBag> {
	
	private long sessionLength = 0;
	private Long lastSession = null;
	private DataBag sessionBag = null;
	
	public Sessionize(String seconds) {
		this.sessionLength = Integer.parseInt(seconds) * 1000;
		sessionBag = BagFactory.getInstance().newDefaultBag();
	}

	@Override
	public void accumulate(Tuple tuple) throws IOException {
		
		if(tuple == null || tuple.size() == 0) {
			return;
		}
		DataBag input = (DataBag)tuple.get(0);
		for(Tuple t : input) {
			Long timestamp = (Long)t.get(1);
			if(lastSession == null) {
				sessionBag.add(t);
			} else if ((timestamp - lastSession) >= sessionLength){
				sessionBag.add(t);
			}
			lastSession = timestamp;
		}
	}

	@Override
	public void cleanup() {
		lastSession = null;
		sessionBag = BagFactory.getInstance().newDefaultBag();
		
	}

	@Override
	public DataBag getValue() {
		return sessionBag;
	}

	@Override
	public DataBag exec(Tuple tuple) throws IOException {
		accumulate(tuple);
		DataBag value = getValue();
		cleanup();
		return value;
	}
	

}
