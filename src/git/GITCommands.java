package git;

import data.GITFile;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthias.hochrieser on 28.04.2017.
 */
public class GITCommands {

    public static boolean isFileWithinGIT(File _file){
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.findGitDir(_file);

        if(repositoryBuilder.getGitDir() != null){
            return true;
        }

        return false;
    }

    public static File getGITRepoForFile(File _file) {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.findGitDir(_file);

        return repositoryBuilder.getGitDir();
    }

    public static String getLatestCommitForFile(GITFile _file) throws IOException, GitAPIException {
        return getCommitsForFile(_file, 1).get(0);
    }

    public static List<String> getAllCommitsForFile(GITFile _file) throws IOException, GitAPIException {
        return getCommitsForFile(_file, 0);
    }

    private static List<String> getCommitsForFile(GITFile _file, int _count) throws IOException, GitAPIException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(_file.getRepo()).build();
        Git git = new Git(repository);

        String relative = _file.getRepo().getParentFile().toURI().relativize(_file.getFile().toURI()).getPath();

        Iterable<RevCommit> logs;

        LogCommand logCommand = git.log();

        if(_count > 0){
            logCommand.setMaxCount(_count);
        }

        if(!relative.isEmpty()){
            logCommand = logCommand.addPath(relative);
        }

        logs = logCommand.call();

        List<String> commits = new ArrayList<>();

        for (RevCommit rev : logs) {
            commits.add(rev.getName());
        }

        return commits;
    }

    public static boolean checkIfGITFileNeedsUpdate(GITFile _file) throws IOException, GitAPIException {
        String latest = getLatestCommitForFile(_file);
        if(latest.equals(_file.getAddedCommit())){
            // Everything is up to date
            _file.setUpToDate(true);
            return false;
        }else{
            // File has been updated
            _file.setUpToDate(false);
            return true;
        }
    }

    public static String getMessageFromCommit(GITFile _file) throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(_file.getRepo()).build();
        RevWalk revWalk = new RevWalk(repository);
        RevCommit commit = revWalk.parseCommit(ObjectId.fromString(_file.getAddedCommit()));
        return commit.getFullMessage();
    }
}
