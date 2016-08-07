package bn.blaszczyk.blstatistics;

import java.awt.Font;
import java.util.List;

import javax.swing.UIManager;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.LeagueManager;
import bn.blaszczyk.blstatistics.gui.MainFrame;
import bn.blaszczyk.blstatistics.tools.FileIO;
import bn.blaszczyk.blstatistics.tools.TeamAlias;

public class BLStatistics
{

	private static void initUIManager()
	{
		Font plainFont = new Font("Arial",Font.PLAIN,16);
		Font boldFont = new Font("Arial",Font.BOLD,16);
		Font tableFont = new Font("Arial",Font.PLAIN,14);
		
		UIManager.put("Table.font", tableFont);
		
		UIManager.put("TableHeader.font", boldFont);
		UIManager.put("Label.font", boldFont);
		UIManager.put("Button.font", boldFont);
		UIManager.put("ProgressBar.font", boldFont);
		UIManager.put("ObtionPane.buttonFont", boldFont);
		UIManager.put("CheckBox.font", boldFont);
		UIManager.put("MenuBar.font", boldFont);
		UIManager.put("Menu.font", boldFont);
		UIManager.put("MenuItem.font", boldFont);
		UIManager.put("ComboBox.font", boldFont);
		UIManager.put("PopupMenu.font", boldFont);
		
		UIManager.put("TextPane.font", plainFont);
		UIManager.put("OptionPane.messageFont", plainFont);
		UIManager.put("List.font", plainFont);
		UIManager.put("PopupMenu.font", plainFont);

//		UIManager.put("JTree.font", plainFont);
//		UIManager.put("TabbedPane.font", plainFont);
//		UIManager.put("Tree.font", plainFont);
//		UIManager.put("RadioButton.font", plainFont);
//		UIManager.put("RadioButtonMenuItem.font", plainFont);
//		UIManager.put("TextField.font", plainFont);
	}	
	
	public static void main(String[] args)
	{
		initUIManager();
		TeamAlias.loadAliases();
		
		List<League> leagues = FileIO.loadLeagues();
		
		MainFrame mf = new MainFrame(leagues);
		mf.showFrame();
		
		LeagueManager lm = new LeagueManager(mf, leagues);
		lm.showDialog();
	}

}