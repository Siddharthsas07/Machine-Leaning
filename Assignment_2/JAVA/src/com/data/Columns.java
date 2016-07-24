package com.data;

import java.util.ArrayList;
import java.util.List;

public class Columns {

	private int index;
	private String name;
	private List<Integer> values;
	
	
	
	public Columns(int index, String name, List<Integer> values) {
		this.index = index;
		this.name = name;
		this.values = values;
	}
	
	public Columns(int index, String name) {
		this.index = index;
		this.name = name;
		values = new  ArrayList<Integer>();
		values.add(0);
		values.add(1);
	}

	@Override
	public boolean equals(Object data){
		if(data instanceof Columns)
			if(((Columns)data).getIndex() == this.index)
				return true;
		
		return false;
	}

	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Integer> getValues() {
		return values;
	}
	public void setValues(List<Integer> values) {
		this.values = values;
	} 
	
}
