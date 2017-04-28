package gui;

import data.GITFile;

import javax.swing.*;
import java.awt.*;

/**
 * Created by matthias.hochrieser on 28.04.2017.
 */
public class GITListRenderer extends JLabel implements ListCellRenderer{

    public GITListRenderer(){
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        GITFile gitFile = (GITFile) value;
        setText(gitFile.getFile().getAbsolutePath());

        if(gitFile.isUpToDate()){
            setBackground(Color.GREEN);
        }else{
            setBackground(Color.RED);
        }

        return this;
    }
}
