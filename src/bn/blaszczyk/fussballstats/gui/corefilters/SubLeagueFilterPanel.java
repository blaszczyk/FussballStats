package bn.blaszczyk.fussballstats.gui.corefilters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.filters.GameFilter;
import bn.blaszczyk.fussballstats.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings("serial")
public class SubLeagueFilterPanel extends AbstractFilterPanel<Game> {

	public static final String NAME = "DirekterVergleich";
	
	private static final String[] EMPTY_TWO_ARRAY = {"",""};
	
	private JLabel label = new JLabel("Direkter Vergleich");
	private List<JComboBox<String>> teamBoxes = new ArrayList<>();
	private JButton btnNewTeam = new JButton("Team Hinzuf�gen");

	private JMenu popupRemoveTeam = new JMenu("Team Entfernen");
	
	public SubLeagueFilterPanel()
	{
		this(Arrays.asList(EMPTY_TWO_ARRAY));
	}

	public SubLeagueFilterPanel(Iterable<String> selectedTeams)
	{
		super(true);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		label.setAlignmentX(LEFT_ALIGNMENT);
		
		btnNewTeam.setMaximumSize(new Dimension(250,30));
		btnNewTeam.setMinimumSize(new Dimension(250,30));
		btnNewTeam.setAlignmentX(LEFT_ALIGNMENT);
		btnNewTeam.addActionListener( e -> addTeamBox(null) );
		
		teamBoxes.clear();
		for(String team : selectedTeams)
			addTeamBox(team);
		
		setFilter();
	}
	
	public int getTeamCount()
	{
		return teamBoxes.size();
	}
	
	public String getTeam(int index)
	{
		return (String) teamBoxes.get(index).getSelectedItem();
	}
	
	protected void setFilter()
	{
		List<String> teams = new ArrayList<>();
		for(JComboBox<String> box : teamBoxes)
			if(box.getSelectedItem() != null )
				teams.add(box.getSelectedItem().toString());
		setDeleteMenu();
		setFilter(GameFilter.getSubLeagueFilter(teams));
	}
	
	private JComboBox<String> addTeamBox(String team)
	{
		JComboBox<String> box =  new MyComboBox<>(TeamFilterPanel.getTeamList(),250,false);
		box.addActionListener(setFilterListener);
		box.setAlignmentX(LEFT_ALIGNMENT);
		if(team != null)
			box.setSelectedItem(team);
		teamBoxes.add(box);
		return box;
	}

	private void removeTeam(JComboBox<String> box)
	{
		teamBoxes.remove(box);
		setFilter();
	}
	
	private void setDeleteMenu()
	{
		popupRemoveTeam.removeAll();
		for(JComboBox<String> box : teamBoxes)
		{
			JMenuItem remove = new JMenuItem(box.getSelectedItem().toString());
			remove.addActionListener( e -> removeTeam(box));
			popupRemoveTeam.add(remove);
		}
	}
	
	
	@Override
	protected void addComponents()
	{
		add(label);
		for(JComboBox<String> box : teamBoxes)
			add(box);
		add(btnNewTeam);			
	}

	@Override
	public String toString()
	{
		return "Direkter Vergleich " + getTeamCount() + " Teams";
	}

	public JMenuItem getMiRemoveTeam()
	{
		return popupRemoveTeam;
	}
}