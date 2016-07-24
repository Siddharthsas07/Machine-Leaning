package com.data;

import java.util.List;

public class Entities {

	
	private List<int[]> rowLst;
	private List<String> columnNames;

	
	
	public Entities() {
	}

	public Entities(List<int[]> rowLst, List<String> columnNames) {
		super();
		this.rowLst = rowLst;
		this.columnNames = columnNames;
	}

	public List<int[]> getRowLst() {
		return rowLst;
	}

	public int getRowCount() {
		return rowLst.size();
	}
	
	public int getColCount() {
		return columnNames.size();
	}
	
	public void setRowLst(List<int[]> rowLst) {
		this.rowLst = rowLst;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public String getColumnName(int rowIndex) {
		return columnNames.get(rowIndex);
	}
	
	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}
	
	public int[] getRow(int rowNum){
		return rowLst.get(rowNum);
	}
	
	public int getRowColumnVal(int rowNum, int column){
		return rowLst.get(rowNum)[column];
	}
}
