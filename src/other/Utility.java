package other;

import data.GITData;

import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;
import java.util.Timer;

/**
 * Created by matthias.hochrieser on 28.04.2017.
 */
public class Utility {

    private static Timer mTrayTimer;

    //Obtain the image URL
    public static Image createImage(String path, String description) {
        ImageIcon icon = new ImageIcon(path, description);
        return icon.getImage();
    }

    public static void resumeGITTimer(TrayIcon _trayIcon){
        mTrayTimer = new Timer();
        mTrayTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(GITData.getInstance().checkAllFiles()){
                    _trayIcon.setImage(Utility.createImage("update.png", "tray icon"));
                }else{
                    _trayIcon.setImage(Utility.createImage("okay.png", "tray icon"));
                }
            }
        }, 0, 30*1000);
    }

    public static void pauseGITTimer(){
        mTrayTimer.cancel();
    }

}
