package others;

import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;

/**
 * ADAPTED from Baptiste Wicht's class
 * (http://baptiste-wicht.developpez.com/tutoriels/java/swing/debutant/?page=listes#L7.1.2)
 *
 */
public class StringComboBoxModel extends DefaultComboBoxModel<String> {
	private LinkedList<String> strings;

	public StringComboBoxModel() {
		super();
		this.strings = new LinkedList<String>();
	}

	public StringComboBoxModel(LinkedList<String> strings) {
		super();
		this.strings = new LinkedList<String>(strings);
	}

	protected LinkedList<String> getStrings(){
		return strings;
	}

	public void setStrings (LinkedList<String> strings) {
		this.strings = new LinkedList<String>(strings);
	}

	public String getSelectedString() {
		return (String)getSelectedItem();
	}

	@Override
	public String getElementAt(int index) {
		return strings.get(index);
	}

	@Override
	public int getSize() {
		return strings.size();
	}

	@Override
	public int getIndexOf(Object element) {
		return strings.indexOf(element);
	}
}
