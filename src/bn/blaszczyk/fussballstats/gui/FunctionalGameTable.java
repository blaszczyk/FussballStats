package bn.blaszczyk.fussballstats.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;

import bn.blaszczyk.fussballstats.core.Game;

@SuppressWarnings("serial")
public class FunctionalGameTable extends JPanel
{
	private static final int ROW_HEIGHT = 25;
	private static final int ROW_BORDER = 50;
	
	private static final Font FONT = new Font("Arial",Font.BOLD,16);
	
	private JLabel header = new JLabel("Spiele", SwingConstants.CENTER);
	private JPanel pnlSummary = new JPanel();
	private GameTable gameTable = new GameTable();
	
	private int summaryRowCount = 0;
	private int nrGames;
	private int nrGoals;
	private int nrHomeGoals;
	private int nrAwayGoals;
	private int nrHomeWins;
	private int nrDraws;
	private int nrAwayWins;
	
	
	public FunctionalGameTable()
	{
		super(new BorderLayout(5,5));

		header.setBounds(0, 0, 620, 50);
		header.setFont(new Font("Arial", Font.BOLD, 28));
		
		pnlSummary.setLayout(null);
		pnlSummary.setPreferredSize(new Dimension(350 , 7 * ROW_HEIGHT + ROW_BORDER));
				
		add(pnlSummary, BorderLayout.NORTH);
		add( new JScrollPane(gameTable), BorderLayout.CENTER);
	}
	
	public void setGames( Iterable<Game> games)
	{
		gameTable.setSource(games);
		clearStatistics();
		for(Game game : games)
			addToStatistics(game);
		drawSummaryPanel();
	}	

	public void setSelectedTeams(List<String> teams)
	{
		gameTable.setSelectedTeams(teams);
	}
	
	public List<String> getSelectedTeams()
	{
		return gameTable.getSelectedTeams();
	}
	
	public void addListSelectionListener(ListSelectionListener l)
	{
		gameTable.getSelectionModel().addListSelectionListener(l);
	}
	
	public void removeListSelectionListener(ListSelectionListener l)
	{
		gameTable.getSelectionModel().removeListSelectionListener(l);
	}

	private void clearStatistics()
	{
		nrGames = 0;
		nrGoals = 0;
		nrHomeGoals = 0;
		nrAwayGoals = 0;
		nrHomeWins = 0;
		nrDraws = 0;
		nrAwayWins = 0;
	}

	private void addToStatistics(Game game)
	{
		nrGames++;
		nrGoals += game.getGoals();
		nrHomeGoals += game.getGoalsH();
		nrAwayGoals += game.getGoalsA();
		switch(game.getWinner())
		{
		case Game.HOME:
			nrHomeWins++;
			break;
		case Game.AWAY:
			nrAwayWins++;
			break;
		case Game.DRAW:
			nrDraws++;
			break;
		}
	}
	
	private void drawSummaryPanel()
	{
		summaryRowCount = 0;
		pnlSummary.removeAll();
		pnlSummary.add(header);
		addSummaryRow("Spiele", nrGames);
		addSummaryRow("Tore", nrGoals);
		addSummaryRow("Heimtore", nrHomeGoals);
		addSummaryRow("Auswärtstore", nrAwayGoals);
		addSummaryRow("Heimsiege", nrHomeWins);
		addSummaryRow("Auswärtssiege", nrAwayWins);
		addSummaryRow("Unentschieden", nrDraws);
		repaint();
	}
	
	private void addSummaryRow(String text, int value)
	{
		JLabel textLabel = new JLabel(text + ":", SwingConstants.RIGHT);
		textLabel.setBounds(150, ROW_BORDER + summaryRowCount * ROW_HEIGHT, 150, ROW_HEIGHT);
		textLabel.setFont(FONT);
		pnlSummary.add(textLabel);
		
		JLabel valueLabel = new JLabel( NumberFormat.getIntegerInstance().format(value), SwingConstants.RIGHT);
		valueLabel.setBounds(310, ROW_BORDER + summaryRowCount * ROW_HEIGHT, 80, ROW_HEIGHT);
		valueLabel.setFont(FONT);
		pnlSummary.add(valueLabel);
		
		summaryRowCount++;
	}
}
