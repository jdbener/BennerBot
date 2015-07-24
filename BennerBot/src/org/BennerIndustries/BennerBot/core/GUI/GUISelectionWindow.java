package org.BennerIndustries.BennerBot.core.GUI;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class GUISelectionWindow extends JFrame {

	private JPanel contentPane = new JPanel();
	private JProgressBar progressBar = new JProgressBar(0);
	private JLabel progressBarLabel = new JLabel("0%");
	JPanel SelectionPannel = new JPanel();
	private final JLabel buttonInfoLabel = new JLabel("");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					GUISelectionWindow frame = new GUISelectionWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUISelectionWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(675,420); 
		setContentPane(contentPane);
		this.setUndecorated(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		contentPane.setLayout(null);
		
		//Sets up the label that displays infromation about the loading process
		progressBarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		progressBarLabel.setBounds(10, 395, 655, 14);
		progressBarLabel.setVisible(false);
		contentPane.add(progressBarLabel);
		
		//Sets up the progress bar and then hides it
		progressBar.setValue(50);
		progressBar.setBounds(10, 395, 655, 14);
		progressBar.setVisible(false);
		contentPane.add(progressBar);
		SelectionPannel.setBorder(null);
		
		//Sets up the sub panel that holds the choice elements
		SelectionPannel.setSize(337, 210);
		SelectionPannel.setLocation(this.getSize().width/2-SelectionPannel.getSize().width/2, this.getSize().height/2-SelectionPannel.getSize().height/2);
		SelectionPannel.setOpaque(false);
		contentPane.add(SelectionPannel);
		SelectionPannel.setLayout(null);
		
		JButton btnLiteMode = new JButton("LITE Mode");
		btnLiteMode.setBackground(null);
		btnLiteMode.addMouseListener(new MouseListener(){
			@Override
			public void mouseEntered(MouseEvent arg0) {
				buttonInfoLabel.setText("This mode adds a simple interface for users who are willing to trade usability for performance. It removes all access to graphical settings changes but should boost performance significantly.");
			}
			@Override public void mouseExited(MouseEvent arg0) {}@Override public void mouseClicked(MouseEvent arg0) {}@Override public void mousePressed(MouseEvent arg0) {}@Override public void mouseReleased(MouseEvent arg0) {}
		});
		btnLiteMode.setBounds(10, 176, 153, 23);
		SelectionPannel.add(btnLiteMode);
		
		JButton btnFullMode = new JButton("Full Mode");
		btnFullMode.setBackground(null);
		btnFullMode.addMouseListener(new MouseListener(){
			@Override
			public void mouseEntered(MouseEvent arg0) {
				buttonInfoLabel.setText("This mode provides all of the graphical features that plugins provide. As well as an easier way to manage bot settings.");
			}
			@Override public void mouseExited(MouseEvent arg0) {}@Override public void mouseClicked(MouseEvent arg0) {}@Override public void mousePressed(MouseEvent arg0) {}@Override public void mouseReleased(MouseEvent arg0) {}
		});
		btnFullMode.setBounds(174, 176, 153, 23);
		SelectionPannel.add(btnFullMode);
		
		buttonInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		buttonInfoLabel.setBounds(10, 11, 317, 154);
		SelectionPannel.add(buttonInfoLabel);
		
		//Applies the Splash Screen Image to the GUI
		JLabel ImageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(GUISelectionWindow.class.getResource("/org/BennerIndustries/BennerBot/core/GUI/splash.png"))));
		ImageLabel.setBounds(0, 0, 678, 424);
		contentPane.add(ImageLabel);
		
		
	}
	public void setProgress(String message, double percential){
		progressBarLabel.setText(message);
		progressBar.setValue((int)(percential*100));
	}
}
