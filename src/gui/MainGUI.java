package gui;

import data.GITData;
import data.GITFile;
import git.GITCommands;
import org.eclipse.jgit.api.errors.GitAPIException;
import other.Utility;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

/**
 * Created by matthias.hochrieser on 27.04.2017.
 */
public class MainGUI extends JFrame implements ActionListener, ListSelectionListener{

    private JList mFileList;
    private JLabel mFileInfo;
    private JSplitPane mSplitPane;
    private JPopupMenu mPopup;

    JMenuItem mMenuAddFile;
    JMenuItem mMenuCheckAll;
    JMenuItem mMenuSystemTray;
    JMenuItem mMenuAbout;

    JMenuItem mPopupCheck;
    JMenuItem mPopupDelete;
    JMenuItem mPopupAdd;
    JMenuItem mPopupMark;
    JMenuItem mPopupOpenFileInExplorer;
    JMenuItem mPopupOpenGITLog;

    AddFile mAddFile;

    TrayIcon mTrayIcon;
    boolean mTrayMode;

    public MainGUI(){
        super("GIT Commit Monitor");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mAddFile = new AddFile(this);
        this.createPopUpList();

        //Create and set up the content pane.
        this.setJMenuBar(createMenuBar());
        this.setContentPane(createContentPane());

        //Display the window.
        this.setSize(800, 400);
        this.setVisible(true);

        mTrayIcon = new TrayIcon(Utility.createImage("okay.png", "tray icon"));
        mTrayMode = false;
    }

    private void createPopUpList(){
        mPopup = new JPopupMenu();

        mPopupMark = new JMenuItem("Mark as solved");
        mPopupMark.addActionListener(this);
        mPopup.add(mPopupMark);

        mPopupCheck = new JMenuItem("Check");
        mPopupCheck.addActionListener(this);
        mPopup.add(mPopupCheck);
        mPopup.add(new JPopupMenu.Separator());

        mPopupDelete = new JMenuItem("Delete");
        mPopupDelete.addActionListener(this);
        mPopup.add(mPopupDelete);

        mPopupAdd = new JMenuItem("Add");
        mPopupAdd.addActionListener(mAddFile);
        mPopup.add(mPopupAdd);

        mPopupOpenFileInExplorer = new JMenuItem("Open File in Explorer");
        mPopupOpenFileInExplorer.addActionListener(this);
        mPopup.add(mPopupOpenFileInExplorer);

        mPopupOpenGITLog = new JMenuItem("Open GIT Log");
        mPopupOpenGITLog.addActionListener(this);
        mPopup.add(mPopupOpenGITLog);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;

        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menuBar.add(menu);

        mMenuAddFile = new JMenuItem("Add Directory/File");
        mMenuAddFile.addActionListener(mAddFile);
        menu.add(mMenuAddFile);

        mMenuCheckAll = new JMenuItem("Check All");
        mMenuCheckAll.addActionListener(this);
        menu.add(mMenuCheckAll);

        mMenuSystemTray = new JMenuItem("System Tray");
        mMenuSystemTray.addActionListener(this);
        menu.add(mMenuSystemTray);

        menu = new JMenu("Help");
        menuBar.add(menu);

        mMenuAbout = new JMenuItem("About");
        mMenuAbout.addActionListener(this);
        menu.add(mMenuAbout);

        return menuBar;
    }

    private Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);

        mFileList = new JList();
        mFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mFileList.setModel(GITData.getInstance().getFilesListModel());
        mFileList.setCellRenderer(new GITListRenderer());
        mFileList.addListSelectionListener(this);

        mFileList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                // if right mouse button clicked (or me.isPopupTrigger())
                if (SwingUtilities.isRightMouseButton(me)){
                    if(!mFileList.isSelectionEmpty() && mFileList.locationToIndex(me.getPoint()) == mFileList.getSelectedIndex()){
                        GITFile file = (GITFile) mFileList.getSelectedValue();
                        mPopupMark.setVisible(!file.isUpToDate());
                        mPopup.show(mFileList, me.getX(), me.getY());
                    }
                }
            }
        });

        mFileList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){
                    try {
                        GITFile gitFile = (GITFile) mFileList.getSelectedValue();
                        mFileInfo.setText(GITCommands.getMessageFromCommit((GITFile) mFileList.getSelectedValue()));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        mFileInfo = new JLabel("Click on Commit to see the \nCommit Message.");
        JScrollPane fileScrollPane = new JScrollPane(mFileList);
        JScrollPane infoScrollPane = new JScrollPane(mFileInfo);

        mSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                fileScrollPane, infoScrollPane);
        mSplitPane.setOneTouchExpandable(true);
        mSplitPane.setDividerLocation(500);

        fileScrollPane.setMinimumSize(new Dimension(100, 50));
        infoScrollPane.setMinimumSize(new Dimension(100, 50));

        mSplitPane.setPreferredSize(new Dimension(800, 400));

        //Add the text area to the content pane.
        contentPane.add(mSplitPane, BorderLayout.CENTER);

        return contentPane;
    }

    public boolean goToTray(){
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return false;
        }

        final PopupMenu popup = new PopupMenu();
        final SystemTray tray = SystemTray.getSystemTray();

        MenuItem openItem = new MenuItem("Open");
        MenuItem exitItem = new MenuItem("Exit");
        popup.add(openItem);
        popup.add(exitItem);

        mTrayIcon.setPopupMenu(popup);

        try {
            tray.add(mTrayIcon);
            setVisible(false);
        } catch (AWTException e1) {
            e1.printStackTrace();
            return false;
        }

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(mTrayIcon);
                System.exit(0);
            }
        });

        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(mTrayIcon);
                setVisible(true);
                Utility.pauseGITTimer();
            }
        });

        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(mPopupDelete)){
            GITData.getInstance().removeFile((GITFile) mFileList.getSelectedValue());
        }else if(e.getSource().equals(mPopupCheck)){
            try {
                GITCommands.checkIfGITFileNeedsUpdate((GITFile) mFileList.getSelectedValue());
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (GitAPIException e1) {
                e1.printStackTrace();
            }
        }else if(e.getSource().equals(mMenuCheckAll)){
            if(GITData.getInstance().checkAllFiles() && mTrayMode){
                mTrayIcon.setImage(Utility.createImage("update.png", "tray icon"));
            }else{
                mTrayIcon.setImage(Utility.createImage("okay.png", "tray icon"));
            }
        }else if(e.getSource().equals(mMenuSystemTray)){
            this.mTrayMode = goToTray();
            Utility.resumeGITTimer(mTrayIcon);
        }else if(e.getSource().equals(mPopupMark)){
            try {
                GITFile gitFile = (GITFile) mFileList.getSelectedValue();
                gitFile.setAddedCommit(GITCommands.getLatestCommitForFile(gitFile));
                gitFile.setUpToDate(true);
                GITData.getInstance().storeData();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (GitAPIException e1) {
                e1.printStackTrace();
            }
        }else if(e.getSource().equals(mPopupOpenFileInExplorer)){
            GITFile gitFile = (GITFile) mFileList.getSelectedValue();

            try {
                Runtime.getRuntime().exec("explorer.exe /select," + gitFile.getFile().getAbsolutePath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }else if(e.getSource().equals(mPopupOpenGITLog)){
            GITFile gitFile = (GITFile) mFileList.getSelectedValue();

            try {
                Runtime.getRuntime().exec("TortoiseGitProc /command:log /path:" + gitFile.getFile().getAbsolutePath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println();
    }
}
