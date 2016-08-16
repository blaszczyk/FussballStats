package bn.blaszczyk.fussballstats.gui.filters;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import bn.blaszczyk.fussballstats.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class IfThenElseFilterPanel<T,U> extends AbstractBiFilterPanel<T, U> implements BiFilterListener<T, U>
{
	public static final String NAME = "IfThenElse";
	
	private JLabel ifLabel = new JLabel("IF");
	private JLabel thenLabel = new JLabel("THEN");
	private JLabel elseLabel = new JLabel("ELSE");
	
	private BiFilterPanel<T,U> ifFilter;
	private BiFilterPanel<T,U> thenFilter;
	private BiFilterPanel<T,U> elseFilter;

	public IfThenElseFilterPanel( BiFilterPanel<T,U> ifFilter, BiFilterPanel<T,U> thenFilter, BiFilterPanel<T,U> elseFilter)
	{
		super(true);
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		ifLabel.setAlignmentX(LEFT_ALIGNMENT);
		thenLabel.setAlignmentX(LEFT_ALIGNMENT);
		elseLabel.setAlignmentX(LEFT_ALIGNMENT);
		
		setIfFilter(ifFilter);
		setThenFilter(thenFilter);
		setElseFilter(elseFilter);
		
		setFilter();
	}

	public BiFilterPanel<T, U> getIfFilter()
	{
		return ifFilter;
	}


	public BiFilterPanel<T, U> getThenFilter()
	{
		return thenFilter;
	}


	public BiFilterPanel<T, U> getElseFilter()
	{
		return elseFilter;
	}
	
	private void setIfFilter(BiFilterPanel<T,U> newPanel)
	{
		if(ifFilter != null)
			ifFilter.removeFilterListener(this);
		newPanel.addFilterListener(this);
		newPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		ifFilter = newPanel;
		setFilter();
	}
	private void setThenFilter(BiFilterPanel<T,U> newPanel)
	{
		if(thenFilter != null)
			thenFilter.removeFilterListener(this);
		newPanel.addFilterListener(this);
		newPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		thenFilter = newPanel;
		setFilter();
	}
	
	private void setElseFilter(BiFilterPanel<T,U> newPanel)
	{
		if(elseFilter != null)
			elseFilter.removeFilterListener(this);
		newPanel.addFilterListener(this);
		newPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		elseFilter = newPanel;
		setFilter();
	}
	
	protected void setFilter()
	{
		setFilter( LogicalBiFilter.getIF_THEN_ELSEBiFilter(ifFilter, thenFilter, elseFilter));
	}

	@Override
	protected void addComponents()
	{
		add(ifLabel);
		add(ifFilter.getPanel());
		add(thenLabel);
		add(thenFilter.getPanel());
		add(elseLabel);
		add(elseFilter.getPanel());
	}

	@Override
	public void paint()
	{
		ifFilter.paint();
		thenFilter.paint();
		elseFilter.paint();
		super.paint();
	}

	@Override
	public void filter(BiFilterEvent<T, U> e)
	{
		passFilterEvent(e);
		if(e.getType() == BiFilterEvent.SET_PANEL && e.getSource() != null)
		{
			if(e.getSource().equals(ifFilter))
				setIfFilter(e.getNewPanel());
			if(e.getSource().equals(thenFilter))
				setThenFilter(e.getNewPanel());
			if(e.getSource().equals(elseFilter))
				setElseFilter(e.getNewPanel());
		}
	}
	
	@Override
	public String toString()
	{
		return "IF_THEN_ELSE";
	}
}