import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class FileChooser {

	static JFrame frame = new JFrame("Add Files");
	public static void checkForSavedDirectories() throws IOException, ParseException {
		System.out.println("checkForSavedDirectories");
		File directoriesFile = new File("data-file-locations.txt");
		if(directoriesFile.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(directoriesFile)); 
			
			String line = br.readLine();
			
			if(line == null) {
				getDirectories();
				return;
			}
			while(line != null) {
				if(!Master.directories.contains(line)) {
					Master.directories.add(line);
				}
				line=br.readLine();
				
			}
			
			br.close();
			Master.three();
		}
		else {
			getDirectories();
		}
	}
	
	public static void manuallyGetFiles() {
		frame.dispose();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("manual");
					PrintWriter out = new PrintWriter(new File("data-file-locations.txt"));
					JFileChooser fc = new JFileChooser();
					FileListAccessory accessory = new FileListAccessory(fc);
					fc.setAccessory(accessory);
					//fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					int open = fc.showOpenDialog(fc);
					if (open == JFileChooser.APPROVE_OPTION) {
						DefaultListModel model = accessory.getModel();
						for (int i = 0; i < model.getSize(); i++) {
							out.println(((File) model.getElementAt(i)).getPath());
						}
						System.out.println("Done choosing files");
					}
					out.close();
					Master.three();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void automaticallyGetFiles(){
		frame.dispose();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("automatic");
					PrintWriter out = new PrintWriter(new File("data-file-locations.txt"));
					JFileChooser fc = new JFileChooser();
					//FileListAccessory accessory = new FileListAccessory(fc);
					//fc.setAccessory(accessory);
					File wowFolder = new File("C:\\Program Files (x86)\\World of Warcraft\\_retail_\\WTF\\");
					fc.setCurrentDirectory(wowFolder);
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int open = fc.showOpenDialog(fc);
					if (open == JFileChooser.APPROVE_OPTION) {
						File file = new File(fc.getSelectedFile()+"\\Account\\");
						String[] temp_directories = file.list(new FilenameFilter() {
							  public boolean accept(File current, String name) {
								return new File(current, name).isDirectory()&&!name.equals("SavedVariables")&&name.contains("#");
							  }
							});
							for(String dir : Data_processing.cleanArrayListToString(new ArrayList<String>(Arrays.asList(Arrays.toString(temp_directories).split(","))))) {
								String fullFilePath = file.getPath()+"\\"+dir+"\\SavedVariables\\\\TradeSkillMaster.lua";
								out.println(fullFilePath);
								Master.directories.add(fullFilePath);
							}
					}
					out.close();
					Master.three();
				} catch (IOException | ParseException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public static void getDirectories(){
		System.out.println("fileChooser.getDirectories()");
		JTextArea text = new JTextArea(5,20);
		text.setText("Add your TradeSkillMaster.lua files.\n\nYou can select manual to add \nspecific files or files in other locations, \nor select automatic to select your \nWorld of Warcraft/WTF/ folder.\n Automatic is the easier method.\nManually editing your directories.txt is also an option");
		text.setMargin( new Insets(10,10,10,10) );
		//text.setFont(text.getFont().deriveFont(12f));
		//text.setFont(new Font(text.getFont().getName(), Font.PLAIN, 20));
		frame.add(text,BorderLayout.NORTH);
		JPanel panel = new JPanel();
		JButton manual = new JButton("Manual");
		manual.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				manuallyGetFiles();
				
			}
			
		});
		JButton automatic  = new JButton("Automatic");
		automatic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				automaticallyGetFiles();
			}
			
		});
		panel.add(manual);
		panel.add(automatic);
		panel.setLayout(new FlowLayout());
		frame.add(panel,BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
