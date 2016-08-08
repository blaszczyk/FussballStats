package bn.blaszczyk.blstatistics.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.gui.filters.*;

public class FilterIO
{
	private static final String FOLDER = "filters";
	private static final String EXTENSION = "flt";

//	private FilterPanelManager<Season, Game> manager = null;


	private FilterParser parser;
	
	public FilterIO()
	{
	}

	public void setParser(FilterParser parser)
	{
		this.parser = parser;
	}

	public void saveFilter(BiFilterPanel<Season, Game> filter)
	{
		if (filter == null || filter instanceof NoFilterPanel)
		{
			JOptionPane.showMessageDialog(null, "No Filter to save.", "Save Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String name = JOptionPane.showInputDialog(null, "Namen f�r den Filter eingeben:", "Filter Speichern", JOptionPane.QUESTION_MESSAGE);
		if(name != null && name != "")
			saveFilter(filter, name);	
	}

	public void saveFilter(BiFilterPanel<Season, Game> filter, String fileName)
	{
		File directory = new File(String.format("%s/", FOLDER)  );
		if(!directory.exists())
			directory.mkdir();
		try
		{
			FileWriter file = new FileWriter(String.format("%s/%s.%s", FOLDER, fileName, EXTENSION));
			file.write( parser.writeFilter(filter) );
			file.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public BiFilterPanel<Season, Game> loadFilter()
	{
		File workingDirectory = new File(System.getProperty("user.dir") + "/" + FOLDER);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("Filter (*.flt)", "flt"));
		fileChooser.setDialogTitle("Filter laden");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setCurrentDirectory(workingDirectory);
		
		switch (fileChooser.showOpenDialog(null))
		{
		case JFileChooser.APPROVE_OPTION:
			return loadFilter(fileChooser.getSelectedFile());
		case JFileChooser.ERROR_OPTION:
			JOptionPane.showMessageDialog(fileChooser, "Kein Filter geladen.", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public BiFilterPanel<Season, Game> loadFilter(String filterName)
	{
		return loadFilter(new File(String.format("%s/%s.%s", FOLDER, filterName, EXTENSION)));
	}
	
	private BiFilterPanel<Season, Game> loadFilter(File file)
	{
		if(file.exists())
			try
			{
				return parser.parseFilter(new FileInputStream(file));
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		return parser.parseFilter("NoFilter");
	}


}