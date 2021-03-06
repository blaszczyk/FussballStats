package bn.blaszczyk.fussballstats.tools;

import java.util.ArrayList;
import java.util.List;


import bn.blaszczyk.fussballstats.model.Game;
import bn.blaszczyk.fussballstats.model.Season;
import bn.blaszczyk.fussballstats.gui.filters.*;

public class FilterLog
{
	/*
	 * Variables
	 */
	private final List<String> logFilter = new ArrayList<>();
	
	private String currentFilterString;
	private int currentFilterIndex = 0;
	
	/*
	 * Undo, Redo
	 */
	public boolean hasUndo()
	{
		return currentFilterIndex > 0;
	}
	
	public BiFilterPanel<Season, Game> undo()
	{
		return reconstructFilter(currentFilterIndex-1);
	}
	
	public boolean hasRedo()
	{
		return currentFilterIndex < logFilter.size() - 1;
	}
	
	public BiFilterPanel<Season, Game> redo()
	{
		return reconstructFilter(currentFilterIndex+1);
	}

	/*
	 * Adds Filter To Log
	 */
	public void logFilter(BiFilterPanel<Season, Game> currentFilter)
	{
		String newFilterString = FilterParser.writeFilter(currentFilter);
		if(newFilterString.equals(currentFilterString))
			return;
		this.currentFilterString = newFilterString;
		chopLog(currentFilterIndex+1);
		logFilter.add(newFilterString);
		currentFilterIndex = logFilter.size() - 1;
	}
	
	/*
	 * Internal Methods
	 */
	private BiFilterPanel<Season, Game> reconstructFilter(int index)
	{
		currentFilterIndex = index;
		currentFilterString = logFilter.get(index);
		return FilterParser.parseFilter(currentFilterString);
	}
	
	private void chopLog(int chopIndex)
	{
		if(chopIndex >= 0)
			while(logFilter.size() > chopIndex)
				logFilter.remove(chopIndex);
	}

}
