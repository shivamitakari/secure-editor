package com.masterminds.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;

import com.masterminds.constants.ApplicationConstants;
import com.masterminds.entity.FileInfo;
import com.masterminds.security.PdfSecurity;

/**
 * 
 * @author  this class is for generating text editor
 */

public class TextEditor extends JFrame {
	public byte [] cipherData=null;
	private JComboBox cbox;
	private JTextField lineField;
	private String[] colourNames = { "WHITE", "ORANGE", "CYAN" };

	private Highlighter.HighlightPainter painter;

	private String title;
	// private FilePageRenderer pageRenderer;
	private PageFormat pageFormat;
	Boolean isCopyEnabled = false;
	Boolean isPrintable = false;
	// UndoableTextArea central=null;
	FileInfo fileInfo = new FileInfo();
	Date expiryDate = null;
	private JTextArea central = null;
	private PdfSecurity pdfSecurity = new PdfSecurity();

	public JTextArea getCentral() {
		return central;
	}

	public void setCentral(JTextArea central) {
		this.central = central;
	}

	private static final long serialVersionUID = 1L;
	JTextArea jtAreaOutput = null;

	// public UndoableTextArea getCentral() {
	// return central;
	// }
	// public void setCentral(UndoableTextArea central) {
	// this.central = central;
	// }

	public TextEditor() {
		Container container = getContentPane();

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu filemenu = new JMenu(ApplicationConstants.FILE);
		JMenuItem newItem = new JMenuItem(ApplicationConstants.NEW);
		newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				Event.CTRL_MASK));
		newItem.addActionListener(new NewListener());
		JMenuItem openItem = new JMenuItem(ApplicationConstants.OPEN);
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				Event.CTRL_MASK));
		openItem.addActionListener(new OpenListener());
		JMenuItem saveItem = new JMenuItem(ApplicationConstants.SAVE);
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Event.CTRL_MASK));
		saveItem.addActionListener(new SaveListener());
		JMenuItem saveAsItem = new JMenuItem(ApplicationConstants.SAVE_AS);
		saveAsItem.addActionListener(new SaveAsListener());
		saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Event.CTRL_MASK));
		JMenuItem printItem = new JMenuItem(ApplicationConstants.PRINT_PREVIEW);
		printItem.addActionListener(new PrintPreview());
		printItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				Event.CTRL_MASK | Event.SHIFT_MASK));
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				Event.CTRL_MASK));
		exitItem.addActionListener(new ExitListener());
		JMenuItem pItem = new JMenuItem("Print");
		pItem.addActionListener(new PrintListener());
		pItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				Event.CTRL_MASK));
		filemenu.add(newItem);
		filemenu.addSeparator();
		filemenu.add(openItem);
		filemenu.addSeparator();
		filemenu.add(saveItem);
		filemenu.addSeparator();
		filemenu.add(saveAsItem);
		filemenu.addSeparator();
		filemenu.add(pItem);
		filemenu.addSeparator();
		filemenu.add(printItem);
		filemenu.addSeparator();
		filemenu.add(exitItem);
		menuBar.add(filemenu);
		JMenu editMenu = new JMenu("Edit");
		JMenuItem undoItem = new JMenuItem("Undo");
		undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				Event.CTRL_MASK));
		undoItem.addActionListener(new UndoListener());
		JMenuItem cutItem = new JMenuItem("Cut");
		cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				Event.CTRL_MASK));
		cutItem.addActionListener(new CutListener());

		JMenuItem copyItem = new JMenuItem("Copy");
		copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				Event.CTRL_MASK));
		copyItem.addActionListener(new CopyListener());
		JMenuItem pasteItem = new JMenuItem("Paste");
		pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				Event.CTRL_MASK));

		JMenuItem gotoItem = new JMenuItem("Go to Line number..");
		gotoItem.addActionListener(new GotoListener());
		editMenu.add(undoItem);
		editMenu.addSeparator();
		editMenu.add(cutItem);
		editMenu.addSeparator();
		editMenu.add(copyItem);
		editMenu.addSeparator();
		editMenu.add(pasteItem);
		editMenu.addSeparator();

		editMenu.add(gotoItem);
		menuBar.add(editMenu);

		JMenu securityMenu = new JMenu("Security");
		JMenuItem dsItem = new JMenu("Digital signature");
		JMenuItem gs = new JMenuItem("Generate Signature");
		dsItem.add(gs);
		JMenuItem vs = new JMenuItem("validate Signature");
		dsItem.add(vs);
		gs.addActionListener(new GenerateDigitalSignatureListener());
		vs.addActionListener(new ValidateDigitalSignatureListener());

		JMenu ppMenu = new JMenu("Password Protection");
		JMenuItem ppEnable = new JMenuItem("Enable");
		JMenuItem ppDisable = new JMenuItem("Disable");
		ppDisable.addActionListener(new DisableProtectionListener());
		JMenuItem exdItem = new JMenuItem("Expiry date");
		exdItem.addActionListener(new ExpiryDateListener());
		JMenu newMenu = new JMenu("Advanced security");
		JMenuItem key = new JMenuItem("Generate Key");
		key.addActionListener(new GenerateKeyListener());
		newMenu.add(key);
		JMenuItem enc = new JMenuItem("Encrypt");
		newMenu.add(enc);
		enc.addActionListener(new EncryptListener());
		JMenuItem dec = new JMenuItem("Decrypt");
		newMenu.add(dec);
		dec.addActionListener(new DecryptListener());
		securityMenu.add(newMenu);
		// securityMenu.addSeparator();
		securityMenu.add(dsItem);
		securityMenu.addSeparator();
		ppEnable.addActionListener(new PasswordProtection());
		securityMenu.add(ppEnable);
		ppMenu.add(ppEnable);
		ppMenu.add(ppDisable);
		securityMenu.add(ppMenu);
		securityMenu.addSeparator();
		securityMenu.add(exdItem);
		menuBar.add(securityMenu);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		central = new JTextArea();
		final JTextArea lines;

		JScrollPane jsp = new JScrollPane();

		lines = new JTextArea("1");

		lines.setBackground(Color.LIGHT_GRAY);
		lines.setEditable(false);
		central.getDocument().addDocumentListener(new DocumentListener() {
			public String getText() {
				int caretPosition = central.getDocument().getLength();
				Element root = central.getDocument().getDefaultRootElement();
				String text = "1" + System.getProperty("line.separator");
				for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
					text += i + System.getProperty("line.separator");
				}
				return text;
			}

			@Override
			public void changedUpdate(DocumentEvent de) {
				lines.setText(getText());
			}

			@Override
			public void insertUpdate(DocumentEvent de) {
				lines.setText(getText());
			}

			@Override
			public void removeUpdate(DocumentEvent de) {
				lines.setText(getText());
			}

		});
		jsp.getViewport().add(central);
		jsp.setRowHeaderView(lines);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JScrollPane scrolltxt = new JScrollPane(jsp);
		scrolltxt.setBounds(0, 0, 333, 333);
		container.add(scrolltxt);
		JTextArea left = new JTextArea();
		Color color = new Color(153, 204, 255);
		left.setBackground(color);
		left.setText("\t\t");
		container.add(left, BorderLayout.WEST);
		left.setVisible(true);
		left.setEditable(false);
		left.setSize(200, 500);

		JTextArea right = new JTextArea();
		color = new Color(153, 204, 255);
		right.setBackground(color);
		right.setText("\t\t");
		right.setEditable(false);
		container.add(right, BorderLayout.EAST);
		right.setVisible(true);

		add(menuBar, BorderLayout.NORTH);
		PrinterJob pj = PrinterJob.getPrinterJob();
		pageFormat = pj.defaultPage();
		setVisible(true);
	}

	class NewListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			central.setText("");
			fileInfo = new FileInfo();

		}
	}

	class GenerateDigitalSignatureListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String digitalSignature  =null;
			if(fileInfo.getFileData()!=null)  {
			digitalSignature = new Integer(fileInfo.getFileData()
					.hashCode()).toString();
			}else {
				JOptionPane.showMessageDialog(new JLabel(""),
						"Please Save File To Apply Digital Signature");
				return;
			}
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Save Digital Signature with .pfx extension");
			int result = chooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				try {

					String filePath = chooser.getSelectedFile()
							.getCanonicalPath();
					if (filePath.endsWith(".pfx")) {
						pdfSecurity.saveDigitalSignature(filePath,
								digitalSignature);
					} else {
						JOptionPane.showMessageDialog(new JLabel(""),
								"save with .pfx extension only");
						actionPerformed(e);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}

	}

	class ValidateDigitalSignatureListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser openFile = new JFileChooser();
			openFile.setDialogTitle("Select Signature for validate data");
			int status = openFile.showOpenDialog(null);
		//	FileNameExtensionFilter filter = new FileNameExtensionFilter("ds");
			//openFile.setFileFilter(filter);
			if (status == JFileChooser.APPROVE_OPTION) {
				File file = openFile.getSelectedFile();
				try {
					if (file == null) {
						JOptionPane.showMessageDialog(new JLabel(""),
								"open .ds file only");
						actionPerformed(e);
						return;
					}
					String filePath = file.getCanonicalPath();
					String ds = pdfSecurity.getDigitalSignature(filePath);
					String dsOfCurrentData=new Integer(central.getText().hashCode()).toString();
					if(ds.endsWith(dsOfCurrentData)) {
						JOptionPane.showMessageDialog(new JLabel(""),
								"File Validated successfully.Data is valid");
						
					}else {
						JOptionPane.showMessageDialog(new JLabel(""),
								"File Data is Invalid. File Changed from external source");
					}
					
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

		}
	}

	class DisableProtectionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			fileInfo.setPassword(null);

		}
	}

	class SaveAsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Save File with .pdf/.txt extenstion");
			int result = chooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {

				try {
					fileInfo.setFilePath(chooser.getSelectedFile()
							.getCanonicalPath());
					System.out.println(fileInfo);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				pdfSecurity.writeFile(fileInfo);
			}

		}
	}

	class OpenListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser openFile = new JFileChooser();
			openFile.setDialogTitle("Open File txt/pdf");
			int status = openFile.showOpenDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				File file = openFile.getSelectedFile();
				try {
					if (file == null) {
						JOptionPane.showMessageDialog(new JLabel(""),
								"open Text/Pdf file only");
						actionPerformed(e);
						return;
					}
					String filePath = file.getCanonicalPath();
					fileInfo.setFilePath(filePath);

					if (pdfSecurity.isFileProtected(fileInfo)) {
						String[] ConnectOptionNames = { "Submit", "Cancel" };
						String ConnectTitle = "This document is protected";
						JLabel passwordLabel = new JLabel("Password",
								JLabel.CENTER);
						JTextField passwordField = new JPasswordField("");
						JPanel connectionPanel = new JPanel(false);
						connectionPanel.setLayout(new GridLayout(2, 1));
						connectionPanel.add(passwordLabel);
						connectionPanel.add(passwordField);
						while (true) {
							if (JOptionPane.showOptionDialog(null,
									connectionPanel, ConnectTitle,
									JOptionPane.OK_CANCEL_OPTION,
									JOptionPane.INFORMATION_MESSAGE, null,
									ConnectOptionNames, ConnectOptionNames[0]) == 0) {

								String pasword = passwordField.getText();
								fileInfo.setPassword(pasword);
								if (pdfSecurity.validatePassword(fileInfo)) {
									central.setText(pdfSecurity.readFile(
											fileInfo).getFileData());
									break;

								} else {
									fileInfo.setPassword(null);
									JOptionPane.showMessageDialog(new JLabel(),
											"Wrong Password", "Error",
											JOptionPane.ERROR_MESSAGE);
									passwordField.setText("");
									continue;
								}

							}

							break;
						}

					} else {

						central.setText(pdfSecurity.readFile(fileInfo)
								.getFileData());
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} else {
				System.out.println("GGG");
			}
		}

	}

	class SaveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (fileInfo.getFilePath() != null) {
				fileInfo.setFileData(central.getText());
				fileInfo.setExpiryDate(expiryDate);
				fileInfo.setisCopyEnabled(isCopyEnabled);
				fileInfo.setIsPrintable(isPrintable);

				pdfSecurity.writeFile(fileInfo);
				JOptionPane.showMessageDialog(new JLabel(""),
						"File Saved successfully");
		

			} else {

				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("save File with .txt/.pdf extension");
				int result = chooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {

					try {
						fileInfo.setFileData(central.getText());
						fileInfo.setExpiryDate(expiryDate);
						fileInfo.setisCopyEnabled(isCopyEnabled);
						fileInfo.setIsPrintable(isPrintable);

						String filePath = chooser.getSelectedFile()
								.getCanonicalPath();
						if (filePath.endsWith(".pdf")
								|| filePath.endsWith(".txt")) {

							fileInfo.setFilePath(filePath);
							fileInfo.setFileData(central.getText());
							pdfSecurity.writeFile(fileInfo);
							
						} else {
							JOptionPane.showMessageDialog(new JLabel(""),
									"save with .pdf/.txt extension only");
							actionPerformed(e);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(new JLabel(""),
							"File Saved successfully");
			

				}
			}
		}
	}

	class PrintListener implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			// central.pri
			PrinterJob pj = PrinterJob.getPrinterJob();
			// pj.setPrintable(pageRenderer, pageFormat);
			if (pj.printDialog()) {
				try {
					pj.print();
				} catch (PrinterException e) {
					System.out.println(e);
				}
			}
		}
	}

	class PasswordProtection implements ActionListener {

		public void actionPerformed(ActionEvent ae) {

			String[] ConnectOptionNames = { "Submit", "Cancel" };
			String ConnectTitle = "This document is protected";
			JLabel passwordLabel = new JLabel("Password", JLabel.CENTER);
			JTextField passwordField = new JPasswordField("");
			JPanel connectionPanel = new JPanel(false);
			connectionPanel.setLayout(new GridLayout(2, 1));
			connectionPanel.add(passwordLabel);
			connectionPanel.add(passwordField);

			if (JOptionPane.showOptionDialog(null, connectionPanel,
					ConnectTitle, JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, ConnectOptionNames,
					ConnectOptionNames[0]) == 0) {

				String pasword = passwordField.getText();
				fileInfo.setPassword(pasword);
			}

		}
	}

	class PrintPreview implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			PrinterJob pj = PrinterJob.getPrinterJob();
			pageFormat = pj.pageDialog(pageFormat);
			// if (pageRenderer != null) {
			// pageRenderer.pageInit(pageFormat);
			// showTitle();
		}

	}

	class ExitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	class GenerateKeyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String[] ConnectOptionNames = { "OK", "Cancel" };
			String ConnectTitle = "This document is protected";
			JLabel keyLabel = new JLabel("Key length", JLabel.CENTER);
			final JTextField keyField = new JTextField("512");

			JPanel connectionPanel = new JPanel(false);
			connectionPanel.setLayout(new GridLayout(4, 1));
			connectionPanel.add(keyLabel);
			connectionPanel.add(keyField);
			connectionPanel.add(new JSeparator());
			final JSlider keyRange = new JSlider(512, 1024, 512);
			connectionPanel.add(keyRange);
			keyRange.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					Integer val = keyRange.getValue();
					keyField.setText(val.toString());
				}
			});

			if (JOptionPane.showOptionDialog(null, connectionPanel,
					ConnectTitle, JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, ConnectOptionNames,
					ConnectOptionNames[0]) == 0) {
				String keylength = keyField.getText();
				KeyPair keyPair = pdfSecurity.genrateKeyPair(new Integer(
						keylength));
				savePublicKey(keyPair);
				savePrivateKey(keyPair);
			}

		}

		private void savePublicKey(KeyPair keyPair) {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Save public key with .pub extension");
			int result = chooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {

				try {
					String filePath = chooser.getSelectedFile()
							.getCanonicalPath();
					if (filePath.endsWith(".pub")) {
						try {
							pdfSecurity.writeKeyInFile(keyPair.getPublic(),
									filePath);
						} catch (Exception exception) {
							JOptionPane
									.showMessageDialog(new JLabel(""),
											"Unable to save public key!! Please check access permissions");
						}

					} else {
						JOptionPane.showMessageDialog(new JLabel(""),
								"save with .pub extension only");
						savePublicKey(keyPair);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

		private void savePrivateKey(KeyPair keyPair) {
			JFileChooser chooserPri = new JFileChooser();
			chooserPri.setDialogTitle("save File with .pri extension");
			int resultPri = chooserPri.showSaveDialog(null);
			if (resultPri == JFileChooser.APPROVE_OPTION) {

				try {
					String filePath = chooserPri.getSelectedFile()
							.getCanonicalPath();
					if (filePath.endsWith(".pri")) {
						try {
							pdfSecurity.writeKeyInFile(keyPair.getPrivate(),
									filePath);
						} catch (Exception exception) {
							JOptionPane
									.showMessageDialog(new JLabel(""),
											"Unable to save private key!! Please check access permissions");
						}

					} else {
						JOptionPane.showMessageDialog(new JLabel(""),
								"save with .pri extension only");
						savePrivateKey(keyPair);

					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}

	}

	class ExpiryDateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// central.setText(new DatePicker().setPickedDate());
			/*
			 * JLabel label = new JLabel("Selected Date:"); final JTextField
			 * text = new JTextField(20); JButton b = new JButton("popup");
			 * JPanel p = new JPanel(); p.add(label); p.add(text); p.add(b);
			 * final JFrame f = new JFrame(); f.getContentPane().add(p);
			 * f.pack(); f.setVisible(true); b.addActionListener(new
			 * ActionListener() { public void actionPerformed(ActionEvent ae) {
			 * 
			 * } });
			 */

			// setting date to fileInfo object
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			Date date = null;
			try {
				expiryDate = format.parse(new DatePicker().setPickedDate());

				System.out.println("DATE ====================" + date);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// fileInfo.setExpiryDate(date);
			System.out.println("fileInfo=================" + fileInfo);
		}

	}

	class EncryptListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser openFile = new JFileChooser();
			openFile.setDialogTitle("Select Private key for Encryption");
			int status = openFile.showOpenDialog(null);

			
			  //FileNameExtensionFilter filter = new
			  //FileNameExtensionFilter("pri"); openFile.setFileFilter(filter);
			  
			 if (status == JFileChooser.APPROVE_OPTION) {
				File file = openFile.getSelectedFile();
				try {
					if (file == null) {
						JOptionPane.showMessageDialog(new JLabel(""),
								"open pri file only");
						actionPerformed(e);
						return;
					}
					String filePath = file.getCanonicalPath();
					Key key = pdfSecurity.readKeyFromFile(filePath);
					cipherData = pdfSecurity.encryptData(key, central.getText());
					central.setText(new String(cipherData));

				} catch (Exception exception) {

				}
			}
		}

	}

	class CutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			central.cut();

		}

	}

	class CopyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			central.copy();

		}

	}

	class PasteListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			central.paste();

		}

	}

	class GotoListener implements ActionListener {
		final JFrame frame = new JFrame("Text HIGHLIGHT");

		@Override
		public void actionPerformed(ActionEvent e) {

			int selection = JOptionPane.showConfirmDialog(frame,
					getOptionPanel(), "Highlighting Options : ",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (selection == JOptionPane.OK_OPTION) {
				System.out.println("OK Selected");
				int lineNumber = Integer.parseInt(lineField.getText().trim());
				lineNumber--;
				try {
					int startIndex = central.getLineStartOffset(lineNumber);
					int endIndex = central.getLineEndOffset(lineNumber);
					String colour = (String) cbox.getSelectedItem();

					if (colour == colourNames[0]) {
						System.out.println("WHITE Colour");
						painter = new DefaultHighlighter.DefaultHighlightPainter(
								Color.WHITE);
						central.getHighlighter().addHighlight(startIndex,
								endIndex, painter);
					} else if (colour == colourNames[1]) {
						System.out.println("ORANGE Colour");
						painter = new DefaultHighlighter.DefaultHighlightPainter(
								Color.ORANGE);
						central.getHighlighter().addHighlight(startIndex,
								endIndex, painter);
					} else if (colour == colourNames[2]) {
						System.out.println("CYAN Colour");
						painter = new DefaultHighlighter.DefaultHighlightPainter(
								Color.CYAN);
						central.getHighlighter().addHighlight(startIndex,
								endIndex, painter);
					}
				} catch (BadLocationException ble) {
					ble.printStackTrace();
				}
			} else if (selection == JOptionPane.CANCEL_OPTION) {
				System.out.println("CANCEL Selected");
			} else if (selection == JOptionPane.CLOSED_OPTION) {
				System.out.println("JOptionPane closed deliberately.");
			}

		}

		private JPanel getOptionPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(0, 2, 5, 5));

			JLabel lineNumberLabel = new JLabel("Enter Line Number : ");
			lineField = new JTextField(10);

			JLabel colourLabel = new JLabel("Select One Colour : ");
			cbox = new JComboBox(colourNames);

			panel.add(lineNumberLabel);
			panel.add(lineField);
			panel.add(colourLabel);
			panel.add(cbox);

			return panel;
		}

	}

	class UndoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

		}

	}

	
	class DecryptListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			JFileChooser openFile = new JFileChooser();
			openFile.setDialogTitle("Select Public Key for Decryption");
			int status = openFile.showOpenDialog(null);

			/*
			 * FileNameExtensionFilter filter = new
			 * FileNameExtensionFilter("pub"); openFile.setFileFilter(filter);
			 */if (status == JFileChooser.APPROVE_OPTION) {
				File file = openFile.getSelectedFile();
				try {
					if (file == null) {
						JOptionPane.showMessageDialog(new JLabel(""),
								"open .pub file only");
						actionPerformed(e);
						return;
					}
					String filePath = file.getCanonicalPath();
					Key key = pdfSecurity.readKeyFromFile(filePath);
					System.out.println(key.getFormat()
							+ "kkkkkkkkkkkkkkkkkkkkkk");
					String data = new String (central.getText());
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!"
							+ data.getBytes());
					central.setText(pdfSecurity.decryptData(key, cipherData));

				} catch (Exception exception) {

				}
			}

		}

	}

	class CopyEnabledListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			fileInfo.setisCopyEnabled(false);
		}

	}

}

class DatePicker {
	int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
	int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);;
	JLabel l = new JLabel("", JLabel.CENTER);
	String day = "";
	JDialog d;
	JButton[] button = new JButton[49];

	public DatePicker() {
		d = new JDialog();
		d.setModal(true);
		String[] header = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
		JPanel p1 = new JPanel(new GridLayout(7, 7));
		p1.setPreferredSize(new Dimension(430, 120));

		for (int x = 0; x < button.length; x++) {
			final int selection = x;
			button[x] = new JButton();
			button[x].setFocusPainted(false);
			button[x].setBackground(Color.white);
			if (x > 6)
				button[x].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						day = button[selection].getActionCommand();
						d.dispose();
					}
				});
			if (x < 7) {
				button[x].setText(header[x]);
				button[x].setForeground(Color.red);
			}
			p1.add(button[x]);
		}
		JPanel p2 = new JPanel(new GridLayout(1, 3));
		JButton previous = new JButton("<< Previous");
		previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				month--;
				displayDate();
			}
		});
		p2.add(previous);
		p2.add(l);
		JButton next = new JButton("Next >>");
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				month++;
				displayDate();
			}
		});
		p2.add(next);
		d.add(p1, BorderLayout.CENTER);
		d.add(p2, BorderLayout.SOUTH);
		d.pack();
		// d.setLocationRelativeTo(parent);
		displayDate();
		d.setVisible(true);
	}

	public void displayDate() {
		for (int x = 7; x < button.length; x++)
			button[x].setText("");
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"dd-MM-yyyy");
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(year, month, 1);
		int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
		int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
		for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++)
			button[x].setText("" + day);
		l.setText(sdf.format(cal.getTime()));
		d.setTitle("Date Picker");
	}

	public String setPickedDate() {
		if (day.equals(""))
			return day;
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"dd-MM-yyyy");
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(year, month, Integer.parseInt(day));
		return sdf.format(cal.getTime());
	}
}