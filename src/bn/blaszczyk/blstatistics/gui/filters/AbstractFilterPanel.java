package bn.blaszczyk.blstatistics.gui.filters;


import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;

@SuppressWarnings("serial")
public abstract class AbstractFilterPanel<T> extends JPanel implements FilterPanel<T>
{
	private static final Border ACTIVE_BORDER = BorderFactory.createLoweredBevelBorder();
	private static final Border INACTIVE_BORDER = BorderFactory.createRaisedBevelBorder();
	
	private Filter<T> filter = LogicalFilter.getTRUEFilter();
	
	private List<FilterListener<T>> listeners = new ArrayList<>();

	private JPopupMenu popup;
	private JLabel popupHeader = new JLabel("Filter");
	private JMenuItem popupSetActive;
	
	private boolean active = true;
	
	/*
	 * Constructors
	 */
	public AbstractFilterPanel()
	{
		popupSetActive = new JMenuItem("Deaktivieren");
		popupSetActive.addActionListener( e -> setActive(!active));
		
		popup = new JPopupMenu();
		popup.add(popupHeader);
		popup.addSeparator();
		addPopupMenuItems();
		setComponentPopupMenu(popup);
		
		setActive(true);
		addFilterListener(e -> popupHeader.setText(this.toString()));
	}


	protected abstract void addComponents();
	
	protected void addPopupMenuItems()
	{
		popup.add(popupSetActive);
	}
	
	protected void setFilter(Filter<T> filter)
	{
		this.filter = filter;
		notifyListeners(new FilterEvent<>(this, filter, FilterEvent.RESET_FILTER));
	}
	
	private void setActive(boolean active)
	{
		if(active)
		{
			setBorder(ACTIVE_BORDER);
			this.active = true;
			popupSetActive.setText("Deaktivieren");
		}
		else
		{
			setBorder(INACTIVE_BORDER);
			this.active = false;
			popupSetActive.setText("Aktivieren");
		}
		notifyListeners(new FilterEvent<T>(this, this, FilterEvent.RESET_FILTER));
	}

	private void notifyListeners(FilterEvent<T> e)
	{
		for(FilterListener<T> listener : listeners)
			listener.filter(e);
	}	

	/*
	 * Filter Methods
	 */
	
	@Override
	public boolean check(T t)
	{
		return !active || filter.check(t);
	}
	
	/*
	 * FilterPanel Methods
	 */

	@Override 
	public JPanel getPanel()
	{
		return this;
	}
	
	@Override
	public void paint()
	{
		removeAll();
		addComponents();
		revalidate();
	}
	@Override
	public void addFilterListener(FilterListener<T> listener)
	{
		listeners.add(listener);
	}

	@Override
	public void removeFilterListener(FilterListener<T> listener)
	{
		int i = listeners.indexOf(listener);
		if( i >= 0 )
			listeners.remove(i);
	}

	@Override
	public void addPopupMenuItem(JMenuItem item)
	{
		popup.add(item);
	}

	@Override
	public void removePopupMenuItem(JMenuItem item)
	{
		popup.remove(item);
	}
	
}