package Core;




import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class DisplayModel extends AbstractTableModel {
	private Core c;
	private ArrayList <String> implementation;
	public DisplayModel(Core c) {
		this.c = c;
	}
	public void setVocabulary() {
		this.implementation = c.getVocabulary();
	}
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return implementation.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 1;
	}
	@Override
	public String getColumnName(int c) {
		return "Phrases";
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return implementation.get(rowIndex);
	}
	public void implement(ArrayList <String> implementation) {
		this.implementation = implementation;
		fireTableDataChanged();
	}
}
