package data;

/**
 * Created by matthias.hochrieser on 28.04.2017.
 */

import git.GITCommands;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwat-twirth on 01.12.2014.
 */
public class GITData {

    private static final String CONFIG_USERNAME = "USERNAME";

    private static GITData mInstance;

    private DefaultListModel<GITFile> mFiles = null;
    private String mUsername = "DEFAULT";

    private GITData() {
        mFiles = new DefaultListModel<>();
    }

    public static synchronized GITData getInstance(){
        if(mInstance == null){
            mInstance = new GITData();
        }

        return mInstance;
    }

    public DefaultListModel<GITFile> getFilesListModel(){
        return mFiles;
    }

    public void addFile(File _file){
        if(!isFileAlreadyAdded(_file)){
            if(GITCommands.isFileWithinGIT(_file)) {
                mFiles.addElement(new GITFile(_file));
                storeData();
            }else{
                System.out.println("File is not within GIT!");
            }
        }else{
            System.out.println("Already available in the List!");
        }
    }

    public void removeFile(GITFile _file){
        if(mFiles.removeElement(_file)){
            storeData();
        }
    }

    public void storeData(){
        ArrayList<String> _config = new ArrayList<>();
        _config.add(CONFIG_USERNAME + ":" + mUsername);

        try {
            MonitorConfigFile.writeConfig(mFiles, _config);
            System.out.println("data saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData(){
        List<String> data = MonitorConfigFile.readFile();

        if(data != null){
            int indexConfigStart = data.indexOf(MonitorConfigFile.PREFIX_CONFIG);
            int indexFileStart = data.indexOf(MonitorConfigFile.PREFIX_FILES);

            List<String> config = data.subList(indexConfigStart + 1, indexFileStart);
            List<String> files = data.subList(indexFileStart + 1, data.size());

            for(int i = 0; i < files.size(); i++){
                String line = files.get(i);
                String[] fields = line.split("#:#");
                mFiles.addElement(new GITFile(new File(fields[0]), fields[1]));
            }

            System.out.println("data loaded!");
        }
    }

    private boolean isFileAlreadyAdded(File _file){
        for(int i = 0; i < this.mFiles.size(); i++){
            if(this.mFiles.get(i).equals(_file)){
                return true;
            }
        }

        return false;
    }

    public boolean checkAllFiles(){
        boolean oneFileUpdatedAtLeast = false;
        for(int i = 0; i < this.mFiles.size(); i++){
            try {
                if(GITCommands.checkIfGITFileNeedsUpdate(this.mFiles.get(i))){
                    oneFileUpdatedAtLeast = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Checked all files!");
        return oneFileUpdatedAtLeast;
    }

    public void clearData(){

    }
}

