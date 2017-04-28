package data;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by matthias.hochrieser on 28.04.2017.
 */
public class MonitorConfigFile {

    public static final String FILE_NAME = "./commitMonitorConfig.txt";
    public static final String PREFIX_CONFIG = "[CONFIG_START]";
    public static final String PREFIX_FILES = "[FILES_START]";

    final static Charset ENCODING = StandardCharsets.UTF_8;

    public static void writeConfig(DefaultListModel<GITFile> _gitFiles, List<String> _config) throws IOException {
        Path path = Paths.get(FILE_NAME);

        // Create TXT Text
        ArrayList<String> builder = new ArrayList<>();
        builder.add(PREFIX_CONFIG);
        builder.addAll(_config);
        builder.add(PREFIX_FILES);

        for(int i = 0; i < _gitFiles.size(); i++){
            builder.add(_gitFiles.get(i).getFile().getAbsolutePath() + "#:#" + _gitFiles.get(i).getAddedCommit());
        }

        // Write the Config to the TXT File
        Files.write(path, builder , ENCODING);
    }

    public static List<String> readFile(){
        Path path = Paths.get(FILE_NAME);
        try {
            return Files.readAllLines(path, ENCODING);
        } catch (NoSuchFileException e) {
            System.out.println("Could not load any data because config file is not there!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
