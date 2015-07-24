package com.masterminds.programs;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.masterminds.editor.TextEditor;


public class ProgramStart {

	public static void main(String[] args) {
		
		JFrame fm=new TextEditor();
        fm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        fm.setSize(screenSize.width, screenSize.height-40);
        fm.setVisible(true);
        fm.setTitle("Secure text editor");
        fm.setLayout(new BorderLayout());
	}
}
