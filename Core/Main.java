package Core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.text.DefaultCaret;

public class Main extends JFrame implements ActionListener, WindowListener {
	private Core c;
	private Display d;
	private JToolBar north;
	private JTextField input;
	private JTextArea output;
	private JButton showDisplay = new JButton("Show Display");
	private JToggleButton monitor = new JToggleButton("Monitor");
	private JButton makeDecision = new JButton("Make Decision");
	private JButton add = new JButton("+");
	private JButton delete = new JButton("-");
	private JComboBox <String> vocabularies;
	private JButton showForms = new JButton("Show Forms");
	private JToggleButton phraseConstruction = new JToggleButton("Phrase Construction");
	private JToggleButton speech = new JToggleButton("Speech");
	public Main() {
		c = new Core(this);
		d = new Display(c);
		d.setVocabulary();
		setTitle("Project XZ");
		setSize(1000, 470);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(this);
		north = new JToolBar();
		north.setLayout(new FlowLayout(FlowLayout.CENTER));
		north.setFloatable(false);
		input = new JTextField(20);
		input.addActionListener(this);
		north.add(input);
		showDisplay.addActionListener(this);
		north.add(showDisplay);
		monitor.addActionListener(this);
		north.add(monitor);
		makeDecision.addActionListener(this);
		north.add(makeDecision);
		add.addActionListener(this);
		north.add(add);
		delete.addActionListener(this);
		north.add(delete);
		vocabularies = new JComboBox <String> (c.getNames());
		vocabularies.setSelectedIndex(0);
		vocabularies.addActionListener(this);
		north.add(vocabularies);
		showForms.addActionListener(this);
		north.add(showForms);
		phraseConstruction.addActionListener(this);
		north.add(phraseConstruction);
		speech.addActionListener(this);
		north.add(speech);
		add(north, BorderLayout.NORTH);
		output = new JTextArea(20, 20);
		output.setEditable(false);
		output.setLineWrap(true);
		output.setWrapStyleWord(true);
		DefaultCaret caret = (DefaultCaret)output.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		output.setText("Welcome to Project XZ.  Enter text and then press enter for response.\n"
				+ "Input 's' to save.\n\n");
		add(new JScrollPane(output), BorderLayout.CENTER);
		setVisible(true);
	}
	public static void main(String[] args) {
		Main m = new Main();
	}
	public String makeDecision(String question, ArrayList <String> options) {
		return c.makeDecision(question, options);
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		c.setRunning(false);
		c.save();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void speak(String output) {
		this.output.setText(this.output.getText() + "Response: " + output + "\n\n");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == input) {
			if (input.getText().equalsIgnoreCase("s")) {
				c.save();
				c.begin();
				output.setText(output.getText() + "Saved.\n\n");
				input.setText("");
				return;
			}
			String response = c.getResponse(input.getText());
			if (response == null) {
				output.setText(output.getText() + "You said: " + input.getText() + "\n");
				input.setText("");
				return;
			}
			output.setText(output.getText() + "You said: " + input.getText() + 
					"\n" + "Response: " + response + "\n\n");
			input.setText("");
		} else if (e.getSource() == showDisplay) {
			d.setVocabulary();
			d.setVisible(true);
		} else if (e.getSource() == monitor) {
			c.setMonitor(monitor.isSelected());
		} else if (e.getSource() == makeDecision) {
			Decision d = new Decision(this);
			output.setText(output.getText() + "Question was: " + d.getQuestion() + " and answer chosen was: " + d.getDecision() + "\n\n");
		} else if (e.getSource() == add) {
			String name = JOptionPane.showInputDialog(this, "Enter the name of\nthe new vocabulary:");
			if (name == null || name.equals("")) {
				return;
			}
			c.addVocabulary(name);
			vocabularies.addItem(name);
			d.setVocabulary();
		} else if (e.getSource() == delete) {
			if (vocabularies.getSelectedIndex() <= 0) {
				return;
			}
			c.deleteVocabulary(vocabularies.getSelectedIndex());
			int index = vocabularies.getSelectedIndex();
			vocabularies.removeItemAt(index);
			vocabularies.setSelectedIndex(index-1);
			d.setVocabulary();
		} else if (e.getSource() == vocabularies) {
			if (vocabularies.getSelectedIndex() > -1) {
				c.setCurrentVocabulary(vocabularies.getSelectedIndex());
				d.setVocabulary();
			}
		} else if (e.getSource() == showForms) {
			c.showForms();
		} else if (e.getSource() == phraseConstruction) {
			c.setPhraseConstruction(phraseConstruction.isSelected());
		} else if (e.getSource() == speech) {
			c.setSpeaking(speech.isSelected());
		}
	}

}
