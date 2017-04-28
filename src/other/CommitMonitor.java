package other;

import data.GITData;
import gui.MainGUI;

import java.util.Timer;
import java.util.TimerTask;

public class CommitMonitor {

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's gui.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GITData.getInstance().loadData();

                new MainGUI();
            }
        });
    }
}