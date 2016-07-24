package com.data;

import java.util.ArrayList;
import java.util.List;

public class TreeObject {

	private int objectNumber;
	private int value ;
	private List<TreeObject> subTreeList = new ArrayList<TreeObject>();
	private Columns column ;
	private int[] resultCounts;
	
	
	public TreeObject() {
	}
	
	public TreeObject(int value, List<TreeObject> subTreeList,Columns column, int objectNumber){
		this.objectNumber = objectNumber;
		this.subTreeList  =  subTreeList;
		this.value  = value;
		this.column  = column;
	}

	
	public int getObjectNumber() {
		return objectNumber;
	}

	public void setObjectNumber(int objectNumber) {
		this.objectNumber = objectNumber;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public List<TreeObject> getSubTreeList() {
		return subTreeList;
	}

	public void setSubTreeList(List<TreeObject> subTreeList) {
		this.subTreeList = subTreeList;
	}
	
	public void addSubTreeList(TreeObject subTreeObj) {
		subTreeList.add(subTreeObj);
	}

	public Columns getColumns() {
		return column;
	}

	public void setColumns(Columns columns) {
		this.column = columns;
	}

	public int[] getResultCounts() {
		return resultCounts;
	}

	public void setResultCounts(int[] resultCounts) {
		this.resultCounts = resultCounts;
	}

}
