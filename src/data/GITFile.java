package data;

import git.GITCommands;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

/**
 * Created by matthias.hochrieser on 28.04.2017.
 */
public class GITFile {

    private File mFile;
    private File mRepo;
    private String mAddedCommit;
    private boolean isUpToDate;

    // First Time a GIT file is added
    public GITFile(File _file){
        this.mFile = _file;
        this.mRepo = GITCommands.getGITRepoForFile(_file);

        try {
            this.mAddedCommit = GITCommands.getLatestCommitForFile(this);
            this.isUpToDate = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    // Used when you load a GIT File because you know already everything
    public GITFile(File _file, String _addedCommit){
        this.mFile = _file;
        this.mAddedCommit = _addedCommit;
        this.isUpToDate = true;
        this.mRepo = GITCommands.getGITRepoForFile(_file);
    }

    public File getFile() {
        return mFile;
    }

    public File getRepo() {
        return mRepo;
    }

    public String getAddedCommit() {
        return mAddedCommit;
    }

    public boolean isUpToDate(){
        return isUpToDate;
    }

    public void setUpToDate(boolean _status){
        this.isUpToDate = _status;
    }

    public void setAddedCommit(String _addedCommit){
        this.mAddedCommit = _addedCommit;
    }
}
