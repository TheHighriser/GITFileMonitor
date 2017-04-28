# GITFileMonitor

Be aware that this tool can contain errors as it is pretty limited in the current development state. This is a little Java tool which is monitoring files which are within local GIT. 

## How-To

You can add files which are within a GIT repository. Other files will not be allowed by the tool. Currently you have to clone the repository with another software. This software is only able to monitor a local git repository.  After adding the file and sending the tool via File => System Tray to the System Tray, a Timer will check every 30 minutes if there was an update for the file. 

If you select a file in the list you can do a right click on a file and will get a context menu which allows you to remove the file or manually check if there is an update for the file.

## Limitations

Currently you have to do a pull request yourself. So the tool is not automatically doing a pull request, but this feature will come asap.
