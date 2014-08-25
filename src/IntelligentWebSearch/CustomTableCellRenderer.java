
package IntelligentWebSearch;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer
{
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		setHorizontalAlignment(CENTER);

		return cell;
	}
}
