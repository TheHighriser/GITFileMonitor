package gui;

import data.GITData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by matthias.hochrieser on 28.04.2017.
 */
public class AddFile implements ActionListener{

    JFileChooser mFileChooser;
    JFrame mParentFrame;

    public AddFile(JFrame _frame){
        mFileChooser = new JFileChooser();
        mParentFrame = _frame;

        // Allow Files and Directories
        mFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int returnVal = mFileChooser.showOpenDialog(mParentFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = mFileChooser.getSelectedFile();
            GITData.getInstance().addFile(file);
            //This is where a real application would open the file.
            System.out.println("Opening: " + file.getAbsolutePath() + ".\n");
        } else {
            System.out.println("Open command cancelled by user.\n");
        }
    }
}
