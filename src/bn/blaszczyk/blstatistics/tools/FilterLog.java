package bn.blaszczyk.blstatistics.tools;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterPanel;

public class FilterLog
{

	private List<String> nameLog = new ArrayList<>();
	private List<String> filterLog = new ArrayList<>();
	
	private BiFilterPanel<Season,Game> lastSource;
	
	private BiFilterPanel<Season, Game> panel;
	
	private FilterParser parser;
	private ActionListener listener;
	
	private int selectedFilterIndex;
	private boolean ignoreNext = false;

	public FilterLog(FilterParser parser, ActionListener listener)
	{
		this.parser = parser;
		this.listener = listener;
	}


	public void populateBackwardsMenu(JMenu menuBackwards)
	{
		clearMenu(menuBackwards);
		if(selectedFilterIndex > 0)
			menuBackwards.addActionListener(createIndexedListener(selectedFilterIndex-1));
		else
			menuBackwards.setEnabled(false);
		for(int i = selectedFilterIndex - 1; i >= 0; i--)
		{
			JMenuItem mi = new JMenuItem( nameLog.get(i+1) );
			mi.addActionListener(createIndexedListener(i));
			menuBackwards.add(mi);
		}
	}
	
	private ActionListener createIndexedListener(int index)
	{
		final int panelIndex = index;
		return e -> {
			panel = parser.parseFilter( filterLog.get(panelIndex) );
			selectedFilterIndex = panelIndex;
			listener.actionPerformed(e);
		};
	}

	public void populateForwardsMenu(JMenu menuForewards)
	{
		clearMenu(menuForewards);
		if(selectedFilterIndex < filterLog.size() - 1 )
			menuForewards.addActionListener(createIndexedListener(selectedFilterIndex+1));
		else
			menuForewards.setEnabled(false);
		for(int i = selectedFilterIndex + 1; i < filterLog.size(); i++)
		{
			JMenuItem mi = new JMenuItem( nameLog.get(i).toString() );
			mi.addActionListener( createIndexedListener(i) );
			menuForewards.add(mi);
		}
	}
	
	public BiFilterPanel<Season,Game> getFilterPanel()
	{
		return panel;
	}
	
	public void pushFilter(BiFilterPanel<Season, Game> source, BiFilterPanel<Season, Game> fullFilter )
	{
		if(ignoreNext)
		{
			ignoreNext = false;
			return;
		}
		chopLog(selectedFilterIndex+1);
		if(source == lastSource)
			chopLog(selectedFilterIndex);
		lastSource = source;
		nameLog.add(source.toString());
		filterLog.add(parser.writeFilter(fullFilter));
		selectedFilterIndex = filterLog.size() - 1;
	}

	public void pushFilterIgnoreNext(BiFilterPanel<Season, Game> source, BiFilterPanel<Season, Game> fullFilter)
	{
		pushFilter(source, fullFilter);
		ignoreNext = true;
	}
	private void chopLog(int chopIndex)
	{
		if(chopIndex < 0)
			return;
		while(filterLog.size() > chopIndex)
		{
			filterLog.remove(chopIndex);
			nameLog.remove(chopIndex);
		}
	}
	
	private void clearMenu(JMenu menu)
	{
		menu.removeAll();
		ActionListener[] as = menu.getActionListeners();
		for(ActionListener a : as )
			menu.removeActionListener(a);
		menu.setEnabled(true);
	}



}
