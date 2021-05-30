import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class FileListAccessory extends JComponent implements PropertyChangeListener {
	//#region properties
	private static final long serialVersionUID = 3383822926317671395L;
	private File file = null;
	private DefaultListModel model;
	private JList list;
	private JButton removeItem;
	//#endregion

	//#region constructors
	public FileListAccessory(JFileChooser chooser) {
		chooser.addPropertyChangeListener(this);

		model = new DefaultListModel();
		list = new JList(model);
		JScrollPane pane = new JScrollPane(list);
		pane.setPreferredSize(new Dimension(200, 250));

		removeItem = createRemoveItemButton();

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout());
		add(pane);
		add(removeItem, BorderLayout.SOUTH);
	}
	//#endregion

	//#region methods
	/** 
	 * @return DefaultListModel
	 */
	public DefaultListModel getModel() {
		return model;
	}

	private void addFileToList() {
		model.addElement(file);
	}

	private void removeFileFromList() {
		if (list.getSelectedIndex() != -1) {
			 model.remove(list.getSelectedIndex());
		}
	}

	/** 
	 * @return JButton
	 */
	private JButton createRemoveItemButton() {
		JButton button = new JButton("Remove");
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				removeFileFromList();
			}
		});
		return button;
	}

	/** 
	 * @param e
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		boolean update = false;
		String prop = e.getPropertyName();

		//If the directory changed, don't do anything
		if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
			file = null;
			update = true;
			//If a file became selected, find out which one.
		} else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
			file = (File) e.getNewValue();
			update = true;
		}

		if (update && file != null) {
			addFileToList();
		}
	}
	//#endregion
}
