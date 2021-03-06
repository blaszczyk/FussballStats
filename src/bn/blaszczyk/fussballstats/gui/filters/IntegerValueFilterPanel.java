package bn.blaszczyk.fussballstats.gui.filters;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import bn.blaszczyk.fussballstats.filters.Filter;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings("serial")
public abstract class IntegerValueFilterPanel<T> extends AbstractFilterPanel<T> implements CompareToFilterPanel<T>, MouseWheelListener, KeyListener
{
	/*
	 * Components
	 */
	private final JLabel label = new JLabel();
	private final JComboBox<String> boxOperator = new MyComboBox<>(OPERATORS,50,false);;
	private final JTextField tfValue = new JTextField();
	
	/*
	 * Variables
	 */
	private final int defaultValue;
	private final int minValue;
	private final int maxValue;

	/*
	 * Constructor
	 */
	private IntegerValueFilterPanel(String labelText, int defaultValue, int minValue, int maxValue)
	{
		super(false);
		this.defaultValue = defaultValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		
		label.setText(labelText);
		label.setDisplayedMnemonic('f');
		
		boxOperator.addActionListener(setFilterListener);
		
		tfValue.setText(Integer.toString(defaultValue));
		tfValue.setMaximumSize(new Dimension(70,30));
		tfValue.setInheritsPopupMenu(true);
		tfValue.setOpaque(false);
		tfValue.setFont( tfValue.getFont().deriveFont( Font.BOLD ) );
		tfValue.addKeyListener(this);
		tfValue.addMouseWheelListener(this);

		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		setFilter();
	}
	
	protected IntegerValueFilterPanel(String labelText, String operator, int defaultValue, int minValue, int maxValue)
	{
		this(labelText,defaultValue, minValue, maxValue);
		boxOperator.setSelectedItem(operator);
	}

	/*
	 * Getter
	 */
	protected int getReferenceInt()
	{
		int result = defaultValue;
		try
		{
			result = Integer.parseInt(tfValue.getText());
		}
		catch(NumberFormatException e)
		{
		}	
		return result;
	}
	
	/*
	 * Abstract Mehtods
	 */
	protected abstract Filter<T> getFilter();


	/*
	 * Internal Methods
	 */
	private void checkValueBounds()
	{
		int refValue = getReferenceInt();
		if( refValue < minValue)
			tfValue.setText(minValue + "");
		else if(refValue > maxValue)
			tfValue.setText(maxValue + "");
	}	
	
	private void shiftReverenceValue(int diff)
	{
		int newValue = getReferenceInt() + diff;
		tfValue.setText( newValue + "" );
		checkValueBounds();
		setFilter();
	}
	
	
	/*
	 * CompareToFilterPanel Methods
	 */
	@Override
	public String getOperator()
	{
		return boxOperator.getSelectedItem().toString();
	}
	
	@Override
	public String getReferenceValString()
	{
		return String.valueOf(getReferenceInt());
	}

	@Override
	public void invertOperator()
	{
		switch(getOperator())
		{
		case EQ:
			boxOperator.setSelectedItem(NEQ);
			break;
		case NEQ:
			boxOperator.setSelectedItem(EQ);
			break;
		case LEQ:
			boxOperator.setSelectedItem(GG);
			break;
		case GG:
			boxOperator.setSelectedItem(LEQ);
			break;
		case GEQ:
			boxOperator.setSelectedItem(LL);
			break;
		case LL:
			boxOperator.setSelectedItem(GEQ);
			break;
		}
	}

	@Override
	public String getLabel()
	{
		return label.getText();
	}

	/*
	 * AbstractFilterPanel Methods
	 */
	@Override
	protected void addComponents()
	{
		add(label);
		add(boxOperator);
		add(tfValue);
	}
	
	@Override
	protected void setFilter()
	{
		setFilter(getFilter());
	}

	/*
	 * Object Methods
	 */
	@Override
	public String toString()
	{
		return getLabel() + " " + getOperator() + " " + getReferenceInt();
	}

	/*
	 * MouseWheelListener Methods
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int diff = (int) (4 * e.getPreciseWheelRotation());
		shiftReverenceValue(diff);
	}

	/*
	 * KeyListener Methods
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_UP:
			shiftReverenceValue(1);
			break;
		case KeyEvent.VK_DOWN:
			shiftReverenceValue(-1);
			break;
		}
		tfValue.requestFocusInWindow();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			checkValueBounds();
			setFilter();
		}
		tfValue.requestFocusInWindow();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		tfValue.replaceSelection(null);
		char c = e.getKeyChar();
		if (!Character.isISOControl(c) && !Character.isDigit(c) && c!='-')
		{
			Toolkit.getDefaultToolkit().beep();
			e.consume();
		}
	}

}
