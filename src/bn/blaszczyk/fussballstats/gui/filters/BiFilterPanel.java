package bn.blaszczyk.fussballstats.gui.filters;

import javax.swing.JPanel;

import bn.blaszczyk.fussballstats.filters.BiFilter;

public interface BiFilterPanel<T,U> extends BiFilter<T,U> {
	
	public void paint();
	public JPanel getPanel();
	
	public void addFilterListener(BiFilterListener<T,U> listener);
	public void removeFilterListener(BiFilterListener<T,U> listener);
	
	public void replaceMe(BiFilterPanel<T,U> panel);
	
	public void setActive(boolean active);
	public boolean isActive();
	
}
