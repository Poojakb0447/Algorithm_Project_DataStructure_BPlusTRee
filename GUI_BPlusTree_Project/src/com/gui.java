package com;

import java.awt.EventQueue;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

import com.BPlusTree;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.Color;

public class gui {

	private JFrame frame;
	private JTextField txtAddId;
	private JTextField txtAddDesc;
	private JTextField txtDeleteId;
	private JTextField txtSearchId;
	private JTextField txtModifyId;
	private JTextField txtModifyDesc;
	File inputFile;
	
	/**
	 * Launch the application.
	 */
	
	BPlusTree<String, String> bPlusTree = new BPlusTree<String, String>(4);
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui window = new gui();
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
	public gui() {
		/*
		 * URL path = gui.class.getResource("partfile.txt"); File f = new
		 * File(path.getFile()); //String path = System.getProperty("user.dir");
		 * inputFile = f;
		 */
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//Start GUI controls
		
		inputFile = new File(System.getProperty("user.dir") + "/partfile.txt");
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1128, 722);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblAddNode = new JLabel("Add Node:");
		lblAddNode.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblAddNode.setBounds(52, 113, 110, 44);
		frame.getContentPane().add(lblAddNode);
		
		txtAddId = new JTextField();
		txtAddId.setBounds(189, 121, 169, 36);
		frame.getContentPane().add(txtAddId);
		txtAddId.setColumns(10);
		
		txtAddDesc = new JTextField();
		txtAddDesc.setBounds(420, 121, 224, 36);
		frame.getContentPane().add(txtAddDesc);
		txtAddDesc.setColumns(100);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 19));
		btnAdd.setBounds(713, 121, 110, 36);
		frame.getContentPane().add(btnAdd);
		
		JLabel lblDeleteNode = new JLabel("Delete Node:");
		lblDeleteNode.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblDeleteNode.setBounds(52, 189, 131, 44);
		frame.getContentPane().add(lblDeleteNode);
		
		txtDeleteId = new JTextField();
		txtDeleteId.setColumns(10);
		txtDeleteId.setBounds(189, 189, 169, 36);
		frame.getContentPane().add(txtDeleteId);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 19));
		btnDelete.setBounds(713, 189, 110, 36);
		frame.getContentPane().add(btnDelete);
		
		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblSearch.setBounds(52, 256, 131, 44);
		frame.getContentPane().add(lblSearch);
		
		txtSearchId = new JTextField();
		txtSearchId.setColumns(10);
		txtSearchId.setBounds(189, 264, 169, 36);
		frame.getContentPane().add(txtSearchId);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 19));
		btnSearch.setBounds(713, 264, 110, 36);
		frame.getContentPane().add(btnSearch);
		
		JLabel lblModify = new JLabel("Modify:");
		lblModify.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblModify.setBounds(52, 335, 131, 44);
		frame.getContentPane().add(lblModify);
		
		txtModifyId = new JTextField();
		txtModifyId.setColumns(10);
		txtModifyId.setBounds(189, 335, 169, 36);
		frame.getContentPane().add(txtModifyId);
		
		txtModifyDesc = new JTextField();
		txtModifyDesc.setColumns(10);
		txtModifyDesc.setBounds(420, 335, 224, 36);
		frame.getContentPane().add(txtModifyDesc);
		
		JButton btnModify = new JButton("Modify");
		btnModify.setFont(new Font("Tahoma", Font.PLAIN, 19));
		btnModify.setBounds(713, 335, 110, 36);
		frame.getContentPane().add(btnModify);
		
		JButton btnAddFile = new JButton("Load Input File");
		btnAddFile.setFont(new Font("Tahoma", Font.PLAIN, 21));
		btnAddFile.setBounds(850, 216, 254, 44);
		frame.getContentPane().add(btnAddFile);
		
		JButton btnSave = new JButton("Save");		
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 19));
		btnSave.setBounds(850, 300, 88, 37);
		frame.getContentPane().add(btnSave);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 19));
		btnExit.setBounds(1016, 300, 88, 37);
		frame.getContentPane().add(btnExit);
		
		JLabel lblSplits = new JLabel("Splits:");
		lblSplits.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblSplits.setBounds(52, 439, 122, 44);
		frame.getContentPane().add(lblSplits);
		
		JLabel lblSplits_Value = new JLabel("0");
		lblSplits_Value.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblSplits_Value.setBounds(186, 439, 122, 44);
		frame.getContentPane().add(lblSplits_Value);
		
		JLabel lblParentSplits = new JLabel("Parent Splits:");
		lblParentSplits.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblParentSplits.setBounds(52, 499, 122, 44);
		frame.getContentPane().add(lblParentSplits);
		
		JLabel lblParentSplits_Value = new JLabel("0");
		lblParentSplits_Value.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblParentSplits_Value.setBounds(186, 499, 122, 44);
		frame.getContentPane().add(lblParentSplits_Value);
		
		JLabel lblFusions = new JLabel("Fusions:");
		lblFusions.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblFusions.setBounds(321, 439, 122, 44);
		frame.getContentPane().add(lblFusions);
		
		JLabel lblParentFusions = new JLabel("Parent Fusions:");
		lblParentFusions.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblParentFusions.setBounds(318, 499, 143, 44);
		frame.getContentPane().add(lblParentFusions);
		
		JLabel lblFusions_Value = new JLabel("0");
		lblFusions_Value.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblFusions_Value.setBounds(471, 439, 122, 44);
		frame.getContentPane().add(lblFusions_Value);
		
		JLabel lblParentFusions_Value = new JLabel("0");
		lblParentFusions_Value.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblParentFusions_Value.setBounds(471, 499, 122, 44);
		frame.getContentPane().add(lblParentFusions_Value);
		
		JLabel lblDepth = new JLabel("Depth:");
		lblDepth.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblDepth.setBounds(568, 463, 122, 44);
		frame.getContentPane().add(lblDepth);
		
		JLabel lblDepth_Value = new JLabel("0");
		lblDepth_Value.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblDepth_Value.setBounds(700, 463, 122, 44);
		frame.getContentPane().add(lblDepth_Value);
		
		JTextArea txtAreaDisplay10Nodes = new JTextArea();
		txtAreaDisplay10Nodes.setWrapStyleWord(true);
		txtAreaDisplay10Nodes.setLineWrap(true);
		txtAreaDisplay10Nodes.setEditable(false);
		txtAreaDisplay10Nodes.setRows(10);
		txtAreaDisplay10Nodes.setBounds(941, 415, 100, 190);
		frame.getContentPane().add(txtAreaDisplay10Nodes);
		
		JLabel lblStatusValue = new JLabel("Message:");
		lblStatusValue.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblStatusValue.setBounds(52, 583, 1000, 50);
		frame.getContentPane().add(lblStatusValue);
		
		JLabel lblTitle = new JLabel("B Plus Tree Implementation");
		lblTitle.setForeground(Color.BLUE);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblTitle.setBounds(306, 28, 468, 36);
		frame.getContentPane().add(lblTitle);
		
		//End GUI controls
		
		//Start Events
		
		//to load the input file
		btnAddFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
//				 JFileChooser selectFile = new JFileChooser();
//				 
//				 int result = selectFile.showOpenDialog(frame); if (result ==
//				 JFileChooser.APPROVE_OPTION) {  //user selects a file 
//					 }
//				 
//				 
//				inputFile = selectFile.getSelectedFile();
				
				if (inputFile != null) {
                    try {

                        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
                        String data;
                        while ((data = bufferedReader.readLine()) != null) {
                            String partId = data.substring(0, 7).trim();
                            String partDescription = data.substring(15).trim();
                            bPlusTree.insert(partId, partDescription);
                            
                            lblStatusValue.setText("Input File Loaded as a B+ Tree");
                        }
                                         
                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }
                }
				else
				{
					lblStatusValue.setText("Input File not Loaded");
				}
			}
		});
		
		//to add a new node with key and description
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//lblStatusValue.setText("Added");
				String partId = txtAddId.getText();
                String partDescription = txtAddDesc.getText();
                lblStatusValue.setText("PART ADDED. " + "\nPART ID: " + partId + " DESCRIPTION: " + partDescription);
                bPlusTree.insert(partId, partDescription);

			}
		});
		
		//to delete a node with the search key
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String deleteKey = txtDeleteId.getText();

                if (bPlusTree.search(deleteKey) != null) {
                    bPlusTree.delete(deleteKey);

                    lblStatusValue.setText("PART ID " + deleteKey + " DELETED.");
                } else {
                	lblStatusValue.setText("PART ID " + deleteKey + " NOT DELETED. ID NOT FOUND.");
                }
			}
		});
		
		//to search a node with the key
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String searchKey = txtSearchId.getText();
                String partDescription = bPlusTree.search(searchKey);
                if (partDescription != null)
                {
                	lblStatusValue.setText("FOUND. " + "PART ID: " + searchKey +"\n Part Description: " + partDescription);
                	List<String> list = bPlusTree.searchRange(searchKey, BPlusTree.RangePolicy.INCLUSIVE);
                    txtAreaDisplay10Nodes.setText("Next 10 parts:" +"\n" +list.toString());
                }
                else
                {
                	lblStatusValue.setText("PART ID NOT FOUND.");
                }
                
                
			}
		});
		
		//to update the description of a node with the key
		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String searchKey = txtModifyId.getText();
                String updateValue = txtModifyDesc.getText();

                if (bPlusTree.search(searchKey) != null) {
                    bPlusTree.delete(searchKey);
                    bPlusTree.insert(searchKey, updateValue); 

                    lblStatusValue.setText("DESCRIPTION MODIFIED. PART ID: " + searchKey + "\n DESCRIPTION: " + updateValue);
                } else {
                    lblStatusValue.setText("DESCRIPTION NOT MODIFIED AS PART ID NOT FOUND");
                }
			}
		});
		
		//to save the changes into the file
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File finalFile = new File(inputFile.getAbsolutePath());
	            inputFile.delete();

	            try {
	                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(finalFile, true));

	                Map<String, String> data = bPlusTree.getData();
	                Iterator it = data.entrySet().iterator();
	                while (it.hasNext()) {
	                    Map.Entry pair = (Map.Entry) it.next();
	                    bufferedWriter.write(pair.getKey().toString()
	                            + "                             ".substring(8, 16)
	                            + pair.getValue().toString());
	                    bufferedWriter.newLine();
	                    it.remove(); 
	                }

	                bufferedWriter.flush();
	                bufferedWriter.close();
	                lblStatusValue.setText("SAVED.");
	                txtAddId.setText("");
	                txtAddDesc.setText("");
	                txtDeleteId.setText("");
	                txtSearchId.setText("");
	                txtModifyId.setText("");
	                txtModifyDesc.setText("");
	                lblSplits_Value.setText(Integer.toString(bPlusTree.Csplits + bPlusTree.Psplits));
	                lblParentSplits_Value.setText(Integer.toString(bPlusTree.Psplits));
	                lblFusions_Value.setText(Integer.toString(bPlusTree.Cfusions + bPlusTree.Pfusions));
	                lblParentFusions_Value.setText(Integer.toString(bPlusTree.Pfusions));
	                String depth = Integer.toString(bPlusTree.height);
	                lblDepth_Value.setText(depth);
	              //  lblStatusValue.setText("------- ------- -------");

	                Thread.sleep(2000);
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            } catch (InterruptedException e1) {
	                e1.printStackTrace();
	            }
			}
		});
		
		//to exit from the application
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		//End Events
		
	}
}
