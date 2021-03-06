package bn.blaszczyk.fussballstats.gui;

import java.util.Date;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import bn.blaszczyk.fussballstats.model.Game;

public class GameTableModel implements TableModel
{	
	private final List<Game> games;
	
	public GameTableModel(List<Game> games)
	{
		this.games = games;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if(columnIndex == 0)
			return Date.class;
		if(columnIndex == 2 || columnIndex == 4)
			return Integer.class;
		return String.class;
	}

	@Override
	public int getColumnCount()
	{
		return 6;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		switch(columnIndex)
		{
		case 0:
			return "Datum";
		case 1:
			return "Heim";
		case 5:
			return "Gast";
		}
		return null;
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Game game = games.get(rowIndex);
		switch(columnIndex)
		{
		case 0:
			return game.getDate();
		case 1:
			return game.getTeamHome().getName();
		case 2:
			return game.getGoalsHome();
		case 3:
			return " : ";
		case 4:
			return game.getGoalsAway();
		case 5:
			return game.getTeamAway().getName();
		}
		return "";
	}




	@Override
	public int getRowCount()
	{
		return games.size();
	}



	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}

	@Override
	public void addTableModelListener(TableModelListener l)
	{
	}

	@Override
	public void removeTableModelListener(TableModelListener l)
	{
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
	}

}
