import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.io.File;
import java.util.Scanner;

import javax.swing.JTable;


public class entry {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					entry window = new entry();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public entry() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setBounds(100, 100, 727, 470);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		

		
		JButton btnNewButton = new JButton("Minimize");
		btnNewButton.setBackground(Color.BLACK);
		btnNewButton.setFont(new Font("Sitka Banner", Font.BOLD | Font.ITALIC, 30));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
						int vars;
						vars = Integer.parseInt(textField .getText());
						MyLinkedList input = new DLinkedList();
						String inTerms = textField_1.getText();
						String[] tempS = inTerms.split(",");
						for(int i=0;i<tempS.length;i++){
							try{
								int temp = Integer.parseInt(tempS[i]);
								MyLinkedList tempL = new DLinkedList();
								tempL.add(temp);
								input.add(tempL);
							}catch(Exception ex){}
						}
						MyLinkedList dont = new DLinkedList();
						String dontTerms = textField_2.getText();
						String[] dontTempS = dontTerms.split(",");
						for(int i=0;i<dontTempS.length;i++){
							try{
								int temp = Integer.parseInt(dontTempS[i]);
								MyLinkedList tempL1 = new DLinkedList();
								tempL1.add(temp);
								dont.add(tempL1);
								input.add(tempL1);
							}catch(Exception ex){}
						}
						MinTermFirst print = new MinTermFirst (vars);
						print.FirstStep(input, dont);
						frame.dispose();
			}
		});
		btnNewButton.setBounds(241, 287, 255, 51);
		frame.getContentPane().add(btnNewButton);
		
		textField = new JTextField();
		textField.setBackground(Color.LIGHT_GRAY);
		textField.setBounds(303, 68, 116, 35);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBackground(Color.LIGHT_GRAY);
		textField_1.setBounds(241, 155, 394, 35);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Enter the number of variables:");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
		lblNewLabel.setBounds(26, 67, 235, 35);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Enter the minterms:");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
		lblNewLabel_1.setBounds(26, 155, 181, 35);
		frame.getContentPane().add(lblNewLabel_1);
		
		textField_2 = new JTextField();
		textField_2.setBackground(Color.LIGHT_GRAY);
		textField_2.setBounds(241, 220, 394, 35);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Enter the don't care ");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 17));
		lblNewLabel_2.setBounds(26, 229, 181, 26);
		frame.getContentPane().add(lblNewLabel_2);
		

		JButton btnRead = new JButton("Read From File");
		btnRead.setBackground(Color.BLACK);
		btnRead.setFont(new Font("Sitka Banner", Font.BOLD | Font.ITALIC, 15));
		btnRead.setBounds(501, 64, 200, 41);
		btnRead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					Scanner reader = new Scanner(new File("input.txt"));
					textField.setText(reader.nextLine());
					textField_1.setText(reader.nextLine());
					if(reader.hasNextLine())
						textField_2.setText(reader.nextLine());
					
				}catch(Exception Ex){
					JOptionPane.showMessageDialog(frame, "File Currupted or not found ! , make sure that file is composed of input in consecutive lines !");
				}
			}
		});
		frame.getContentPane().add(btnRead);
	}
}
