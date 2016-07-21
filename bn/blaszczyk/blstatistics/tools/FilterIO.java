package bn.blaszczyk.blstatistics.tools;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.gui.corefilters.*;
import bn.blaszczyk.blstatistics.gui.filters.*;

public class FilterIO
{
	private static final String FOLDER = "filters";
	private static final String EXTENSION = "flt";
	
	private BiFilterPanel<Season,Game> panel;
	private StringBuilder outerBuilder;
	private int panelCount;
	private Map<String,BiFilterPanel<Season,Game>> filters;
	private FilterPanelManager<Season, Game> manager;
	private List<String> teams;
	
	public FilterIO(GameFilterPanelManager factory)
	{
		this.manager = factory;
		teams = factory.getTeams();
	}

	public void saveFilter(BiFilterPanel<Season, Game> filter)
	{
		if(filter == null || filter instanceof BlankFilterPanel)
		{
			JOptionPane.showMessageDialog(null, "No Filter to save.", "Save Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		outerBuilder = new StringBuilder();
		panelCount = 0;
		saveSubFilter(filter);
		String name = null;
		while(name == null || name == "")
			name = JOptionPane.showInputDialog(null, "Namen f�r den Filter eingeben:", "Filter Speichern", JOptionPane.QUESTION_MESSAGE );
		String path = String.format("%s/%s.%s", FOLDER, name, EXTENSION  );
		try
		{
			FileWriter file = new FileWriter(path);
			file.write(outerBuilder.toString());
			file.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
//		return builder.toString();
	}
	
	private int saveSubFilter(BiFilterPanel<Season,Game> filter)
	{
		StringBuilder innerBuilder = new StringBuilder();
		innerBuilder.append("F" + (++panelCount) + ";");
		if(filter instanceof MultiOperatorFilterPanel)
		{
			MultiOperatorFilterPanel<Season,Game> mFilter = (MultiOperatorFilterPanel<Season,Game>) filter;
			innerBuilder.append("MultiOperator;" + mFilter.getOperator());
			for( BiFilterPanel<Season,Game> p : mFilter )
				innerBuilder.append(";F" + saveSubFilter(p));
		}
		else if(filter instanceof IfThenElseFilterPanel)
		{
			IfThenElseFilterPanel<Season, Game> iteFilter = (IfThenElseFilterPanel<Season, Game>) filter;
			int ifInt = saveSubFilter(iteFilter.getIfFilter());
			int thenInt = saveSubFilter(iteFilter.getThenFilter());
			int elseInt = saveSubFilter(iteFilter.getElseFilter());
			innerBuilder.append("IfThenElse;F" + ifInt + ";F" + thenInt + ";F" + elseInt);
		}
		else if(filter instanceof UnaryOperatorFilterPanel)
		{
			UnaryOperatorFilterPanel<Season, Game> uFilter = (UnaryOperatorFilterPanel<Season, Game>) filter;
			innerBuilder.append( "UnaryOperator;F" + saveSubFilter(uFilter.getInnerPanel()));
		}
		else if(filter instanceof AbsoluteOperatorFilterPanel)
		{
			AbsoluteOperatorFilterPanel<Season, Game> aFilter = (AbsoluteOperatorFilterPanel<Season, Game>) filter;
			innerBuilder.append(aFilter.toString());
		}
		else if(filter instanceof RoundFilterPanel)
		{
			RoundFilterPanel rFilter = (RoundFilterPanel) filter;
			innerBuilder.append("Runde;" + rFilter.isFirstRound() + ";" + rFilter.isSecondRound() );
		}
		else if(filter instanceof FilterPanelAdapter.FirstArgAdapter)
		{
			FilterPanel<Season> sFilter = ((FilterPanelAdapter.FirstArgAdapter<Season,Game>) filter).getInnerPanel();
			if(sFilter instanceof IntegerValueFilterPanel)
			{
				IntegerValueFilterPanel<Season> iPanel = (IntegerValueFilterPanel<Season>) sFilter;
				innerBuilder.append(iPanel.getLabel() + ";" + iPanel.getSelectedOperator() + ";" + iPanel.getReferenceInt() );
			}
			else
			{
				System.err.println("Unknown Filter" + sFilter);
			}
		}
		else if(filter instanceof FilterPanelAdapter.SecondArgAdapter)
		{
			FilterPanel<Game> gFilter = ((FilterPanelAdapter.SecondArgAdapter<Season,Game>) filter).getInnerPanel();
			if(gFilter instanceof IntegerValueFilterPanel)
			{
				IntegerValueFilterPanel<Game> iPanel = (IntegerValueFilterPanel<Game>) gFilter;
				innerBuilder.append(iPanel.getLabel() + ";" + iPanel.getSelectedOperator() + ";" + iPanel.getReferenceInt() );
			}
			else if(gFilter instanceof TeamFilterPanel)
			{
				TeamFilterPanel tFilter = (TeamFilterPanel) gFilter;
				innerBuilder.append("Team;" + tFilter.getTeam() + ";" + tFilter.getHome() + ";" + tFilter.getAway());
			}
			else if(gFilter instanceof DuelFilterPanel)
			{
				DuelFilterPanel dFilter = (DuelFilterPanel) gFilter;
				innerBuilder.append("Duell;" + dFilter.getTeam1() + ";" + dFilter.getTeam2() );
			}
			else if(gFilter instanceof SubLeagueFilterPanel)
			{
				SubLeagueFilterPanel slFilter = (SubLeagueFilterPanel) gFilter;
				innerBuilder.append("DirekterVergleich");
				for(int i = 0; i < slFilter.getTeamCount(); i++)
					innerBuilder.append(";" + slFilter.getTeam(i));
			}
			else				
			{
				System.err.println("Unknown Filter" + gFilter);
			}
		}
		else			
		{
			System.err.println("Unknown Filter" + filter);
		}
		innerBuilder.append("\n");
		outerBuilder.append(innerBuilder.toString());
		return panelCount;
	}
	
	public void loadFilter(ActionListener listener)
	{
		JFrame frame = new JFrame("W�hle Gespeicherten Filter");
		
		File[] files = new File(FOLDER + "/").listFiles();
		JComboBox<File> fileBox = new JComboBox<>(files);
		fileBox.addActionListener( e -> {
			frame.dispose();
			setPanel(loadFilter((File)fileBox.getSelectedItem()));
			listener.actionPerformed(e);
		});
		fileBox.setMinimumSize(new Dimension(110,30));
		fileBox.setMaximumSize(new Dimension(110,30));
		fileBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel label = new JLabel("W�hle Filter");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(label);
		panel.add(fileBox);
		
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	
	public BiFilterPanel<Season,Game> loadFilter(File file)
	{
		filters = new HashMap<>();
		BiFilterPanel<Season,Game> lastPanel = new BlankFilterPanel<>(manager);
		LineIterator iterator;
		try
		{
			iterator = FileUtils.lineIterator(file);
			while(iterator.hasNext())
				lastPanel = loadSubFilter(iterator.next());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return lastPanel;
	}

	private BiFilterPanel<Season, Game> loadSubFilter(String in)
	{
		String[] split = in.split(";");
		BiFilterPanel<Season,Game> panel = null;
		if(split.length < 2)
			return null;
		switch(split[1])
		{
		case "MultiOperator":
			List<BiFilterPanel<Season,Game>> pList = new ArrayList<>();
			for(int i = 3; i < split.length; i++)
				pList.add( filters.get(split[i]));
			panel = new MultiOperatorFilterPanel<>(manager, pList, split[2]);
			break;
		case "IfThenElse":
			panel = new IfThenElseFilterPanel<>(manager, filters.get(split[2]),filters.get(split[3]),filters.get(split[4]) );
			break;
		case "UnaryOperator":
			panel = new UnaryOperatorFilterPanel<>(manager, filters.get(split[2]));
			break;
		case "TRUE":
			panel = new AbsoluteOperatorFilterPanel<>(true, manager);
			break;
		case "FALSE":
			panel = new AbsoluteOperatorFilterPanel<>(false, manager);
			break;
		case "Runde":
			System.out.println(in);
			System.out.println(getBool(split[2]));
			System.out.println(getBool(split[3]));
			panel = new RoundFilterPanel(manager, getBool(split[2]), getBool(split[3]));
			break;
		case "Saison":
			panel = FilterPanelAdapter.getFirstArgAdapter(new SeasonFilterPanel(split[2],Integer.parseInt(split[3])), manager);
			break;
		case "Spieltag":
			panel = FilterPanelAdapter.getSecondArgAdapter(new MatchDayFilterPanel(split[2],Integer.parseInt(split[3])), manager);
			break;
		case "Tore":
			panel = FilterPanelAdapter.getSecondArgAdapter(GoalFilterPanel.getGoalFilterPanel(split[2],Integer.parseInt(split[3])), manager);
			break;
		case "Heimtore":
			panel = FilterPanelAdapter.getSecondArgAdapter(GoalFilterPanel.getHomeGoalFilterPanel(split[2],Integer.parseInt(split[3])), manager);
			break;
		case "Ausw�rtstore":
			panel = FilterPanelAdapter.getSecondArgAdapter(GoalFilterPanel.getAwayGoalFilterPanel(split[2],Integer.parseInt(split[3])), manager);
			break;
		case "Tordifferenz":
			panel = FilterPanelAdapter.getSecondArgAdapter(GoalFilterPanel.getGoalDiffFilterPanel(split[2],Integer.parseInt(split[3])), manager);
			break;
		case "Team":
			panel = FilterPanelAdapter.getSecondArgAdapter( new TeamFilterPanel(teams, split[2], getBool(split[3]), getBool(split[4])), manager);
			break;
		case "Duell":
			panel = FilterPanelAdapter.getSecondArgAdapter( new DuelFilterPanel(teams, split[2], split[3]), manager);
			break;
		case "DirekterVergleich":
			panel = FilterPanelAdapter.getSecondArgAdapter( new SubLeagueFilterPanel(teams, Arrays.asList(split).subList(2, split.length) ), manager);
			break;
		}
		filters.put(split[0], panel);
		panel.getPanel().setAlignmentX(Component.LEFT_ALIGNMENT);
		return panel;
	}
	
	private boolean getBool(String in)
	{
		if(in.equals("true"))
			return true;
		else
			return false;
	}

	public BiFilterPanel<Season,Game> getPanel()
	{
		return panel;
	}

	private void setPanel(BiFilterPanel<Season,Game> panel)
	{
		this.panel = panel;
	}
}
