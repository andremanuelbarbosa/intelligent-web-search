
package IntelligentWebSearch;

import javax.swing.table.*;

public class CustomTableModel extends AbstractTableModel
{
	private String columnNames[];
	private Object data[][];

	public CustomTableModel(int rows, int columns)
	{
		columnNames = new String[columns];
		data = new Object[rows][columns];
	}

	public Class getColumnClass(int column) 
	{
		return getValueAt(0,column).getClass();
	}

	public int getColumnCount() 
	{
		return columnNames.length;
	}

	public void setColumnName(int column, String name)
	{
		columnNames[column] = name;
	}

	public String getColumnName(int column) 
	{
		return columnNames[column];
	}

	public int getRowCount() 
	{
		return data.length;
	}

	public void setValueAt(Object value, int row, int column)
	{
		data[row][column] = value;
	}

	public Object getValueAt(int row, int column) 
	{
		return data[row][column];
	}
}
