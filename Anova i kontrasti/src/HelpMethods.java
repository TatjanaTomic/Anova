import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.TableModel;

public class HelpMethods
{
	public static boolean checkIsNumber(String s) 
	{
		Pattern pattern = Pattern.compile("[0-9]+");
		Matcher matcher = pattern.matcher(s);
		
		if(s.contentEquals("1")) 
		{
			return false;
		}
		if(matcher.matches()) 
		{
			return true;
		}
		else
			return false;
	}
	
	public static boolean provjeriVrijednosti(TableModel model)
	{
		int n = model.getRowCount();
		int m = model.getColumnCount()-1;
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 1; j <= m; j++)
			{
				if( model.getValueAt(i, j) == null)
					return false;
			}
		}
		return true;
	}
}
