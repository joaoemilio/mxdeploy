package com.mxssh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.operatingsystem.UnixDir;
import com.mxdeploy.api.domain.operatingsystem.UnixFile;
import com.mxdeploy.api.util.StringUtils;
import com.mxscript.MXBeanShellException;
import com.mxterminal.ssh.exception.UnknownFileSystemException;

public class SSHFileSystemService {

	private SSHServiceNew ssh = null;
	
	public SSHFileSystemService(SSHServiceNew ssh){
		this.ssh = ssh;
	}
	
	public UnixDir getUnixDirectory(String path, Boolean recursive, String filter) throws UnknownFileSystemException, IOException, InterruptedException, MXBeanShellException{
		//ssh.executeCommand("ls -ltr " + path );
		
		int index = path.lastIndexOf("/");
		String dir = path.substring(0,index+1);
		String item = path.substring(index+1);

		System.out.println("getUnixDirectory(" + path + ", " + recursive + ", " + filter + " ) ");
		String msg = "";
		
		if(!path.equals("/") ){
			if(item.substring(item.length()-1, item.length()).equals("/")){
				item = item.substring(0, item.length()-1);
			}
			msg = ssh.executeCommand("ls -ltr " + dir + " | grep " + item);
		} else {
			msg = ssh.executeCommand("ls -ltr /");
		}
		
		List<String> list = convertArrayToList(msg);

		System.out.println("getUnixDirectory: " + path);
		String aux = list.get(0);
		UnixFile unixFile = getUnixFile(aux);
		if(unixFile == null) {
			System.out.println("unixFile is null");
			return null;
		}
		
		UnixDir unixDir = new UnixDir();
		unixDir.setPath(path);
		unixDir.setGroup(unixFile.getGroup());
		unixDir.setOwner(unixFile.getOwner());
		unixDir.setPermissions(unixFile.getPermissions());
		unixDir.setName(unixFile.getName());
		unixDir.setActualPath(unixFile.getActualPath());

		System.out.println("unixDir = " + unixDir +  " - " + unixDir.getPath() );
		
		if(recursive){
			//String outputFile2 = System.currentTimeMillis() + "_files_" + path.replaceAll("/", "_") + "_" + ".txt";
			msg = ssh.executeCommand("ls -ltr " + path + "/" + (filter != null?" | grep " + filter:"" ) );
			List<String> list2 = convertArrayToList(msg);
			
			for(String line: list2){
				//System.out.println(line);
				UnixFile uFile = getUnixFile(line);
				if(uFile!= null && uFile.isDirectory() && !uFile.isSymlink()){
					UnixDir subDir = getUnixDirectory(path + "/" + uFile.getName() + "/");
					unixDir.addSubDir(subDir);
					subDir.setGroup(uFile.getGroup());
					subDir.setOwner(uFile.getOwner());
					subDir.setPermissions(uFile.getPermissions());
					subDir.setName(uFile.getName());
					subDir.setPath(path + "/" + uFile.getName());
				}else if(uFile != null && !uFile.isDirectory() && !uFile.isSymlink()){
					uFile.setPath(path + "/" + uFile.getName());
					unixDir.addFile(uFile);
				}
			}
		}else{
			msg = ssh.executeCommand("ls -ltr " + path + "/" + (filter != null?" | grep " + filter:"" ) );
			List<String> list2 = convertArrayToList(msg);
			
			for(String line: list2){
				//System.out.println(line);
				UnixFile uFile = getUnixFile(line);
				if(uFile != null && !uFile.isDirectory() && !uFile.isSymlink()){
					uFile.setPath(path + "/" + uFile.getName());
					if(filter != null) {
						if(uFile.getName().contains(filter)) {
							unixDir.addFile(uFile);
						}
					}else{
						unixDir.addFile(uFile);
					}
				}
			}
		}
		
		return unixDir;
	}
	
	public UnixDir getUnixDirectory(String path, Boolean recursive) throws UnknownFileSystemException, IOException, InterruptedException, MXBeanShellException{
		System.out.println("getUnixDirectory(String path, Boolean recursive, null) ");
		return getUnixDirectory(path, recursive, null);
	}
	
	public UnixDir getUnixDirectory(String path) throws UnknownFileSystemException, IOException, InterruptedException, MXBeanShellException{
		return getUnixDirectory(path, true);
	}
	
	private List<String> convertArrayToList(String lines) {
		List<String> result = new ArrayList<String>();
		if(lines == null) return result;
		lines = lines.replaceAll("\r", "");
		String[] aLines = lines.split("\n");
		
		for(int i = 0; i < aLines.length; i++){
			result.add(aLines[i]);
		}
		return result;
	}
	
	public List<UnixDir> getUnixDirectoriesSymbolicLinks(String path) throws UnknownFileSystemException, IOException, InterruptedException, MXBeanShellException{
		//String outputFile2 = System.currentTimeMillis() + "_files_" + path.replaceAll("/", "_") + "_" + ".txt";
		String msg = ssh.executeCommand("ls -ltr " + path + "/");
		List<String> list2 = convertArrayToList(msg);
		List<UnixDir> symbolicLinkDirs = new ArrayList<UnixDir>(); 
	
		for(String line: list2){
			//System.out.println(line);
			UnixFile uFile = getUnixFile(line);
			if(uFile == null){
				System.err.println("File could not be parsed. Contact application team. fsbsilva@gmai.com: \n" + line);
				continue;
			}
			if(!uFile.isSymlink()){
				continue;
			}
			
			String actualPath = uFile.getActualPath();
			String symdirpath = "";
			if(actualPath.substring(0,1).equals("/")){
				symdirpath = actualPath;
			}else if(actualPath.substring(0, 1).equals(".")){
				symdirpath = path + "/" + actualPath;
			}
			UnixDir subDir = getUnixDirectory(symdirpath);
			symbolicLinkDirs.add(subDir);
		}
		
		return symbolicLinkDirs;
	}

	//given a ls -ltr result line, transform it in a UnixFile object (perm, owner, group...)
	public UnixFile getUnixFile(String line) throws UnknownFileSystemException {
		if(line == null || line.length() == 0) {
			return null;
		}
		
		if(line.contains("No such file or") || line.contains("ermission denied")) {
			return null;
		}
		
		line = StringUtils.replaceSpacesAndTabs(line);
		System.out.println(line);
		
		String[] args = line.split(" ");
		
		UnixFile uf = null;
		try{
			Integer.parseInt(args[0].substring(0, 1));
			uf = getUnixFileFindLs(args);
		}catch(Exception e){
			uf = getUnixFileLsLtr(args);
		}

		if(uf != null && uf.isSymlink()){
			String[] symlink = parseSymbolicLinkName(line);
			uf.setName(symlink[0]);
			uf.setActualPath(symlink[1]);
		}

		return uf;
	}
	
	public UnixFile getUnixFileLsLtr(String[] args) throws UnknownFileSystemException {
		if(args.length < 6) {
			return null;
		}
		
		UnixFile uf = new UnixFile();
		uf.setPermissions(args[0]);
		uf.setOwner(args[2]);
		uf.setGroup(args[3]);
		try{
			uf.setSize(new Double(args[4]));
		}catch(Exception e){
			return null;
			//throw new UnknownFileSystemException("Could not retrieve file size", e);
		}
		
		uf.setLastUpdate(args[5] + " " + args[6] + " " + args[7]);
		/*
		System.out.println(args.toString());
		System.out.println("file owner: " + uf.getOwner());
		System.out.println("file group owner: " + uf.getGroup());
		*/
		if(!uf.isSymlink()){
			uf.setName(args[args.length-1]);
		}
		return uf;
	}
	
	public UnixFile getUnixFileFindLs(String[] args) throws UnknownFileSystemException{
		if(args.length < 9) {
			return null;
		}
	
		UnixFile uf = new UnixFile();
		uf.setPermissions(args[2]);
		uf.setOwner(args[4]);
		uf.setGroup(args[5]);
		try{
			uf.setSize(new Double(args[6]));
		}catch(Exception e){
			//throw new UnknownFileSystemException("Could not retrieve file size", e);
			return null;
		}

		uf.setLastUpdate(args[7] + " " + args[8] + " " + args[9]);

		if(!uf.isSymlink()){
			uf.setName(args[args.length-1]);
		}
		return uf;
	}
	
	public UnixFile getUnixFile(String dir, String fileName) throws UnknownFileSystemException, IOException, InterruptedException, MXBeanShellException {
		if(dir != null){
			if(dir.length() > 1) {
				if( dir.substring(dir.length()-1,dir.length()).equals("/") ){
					dir = dir.substring(0, dir.length()-1);
				}
			}
		}
		String msg = ssh.executeCommand("ls -ltr " + dir + "/" + fileName);
		if(msg != null && msg.contains("not found")) {
			return null;
		}
		UnixFile uf = getUnixFile(msg);
		return uf;
	}
	
	
	public UnixFile getUnixFilebyPath(String filePath) throws UnknownFileSystemException, IOException, InterruptedException, MXBeanShellException {
		String msg = ssh.executeCommand("ls -ltr " + filePath );
		if(msg != null && (msg.contains("not found") || msg.contains("Not found") || msg.contains("ermission denied") ) ) {
			return null;
		}
		UnixFile uf = getUnixFile(msg);
		return uf;
	}
	
	private String[] parseSymbolicLinkName(String line){
		line = StringUtils.replaceSpacesAndTabs(line);
		String[] args = line.split("->");
		String[] part1 = args[0].split(" ");
		String name = part1[part1.length-1].trim();
		String[] result = new String[]{name, args[1].trim()};
		return result;
	}
	/*
	private void followSymbolicLinks(String outputFile) throws IOException, InterruptedException {
		Config config = Config.getInstance();
		String tempDir = config.getTempDir();
		String output2 = tempDir + outputFile;

		List<String> list = FileUtils.getInstance().getContents(output2);

		for(String line: list){
			if(line!= null && line.substring(0,1).equals("l") ){
				String[] lsargs = line.split(" ");
				String link = lsargs[lsargs.length-1];
				int index = link.lastIndexOf("/");
				String dir = link.substring(0,index);
				String item = link.substring(index+1);

				//lsGrep( dir, outputFile, item);
			}
		}	
	}
	public List<String> getDirectoriesFromPath(String path){
		//System.out.println("getDirectoriesFromPath: " + path);

		List<String> listDirs  = new ArrayList<String>();
		URL url = OSService.class.getResource("/unix/scripts/_ls.ksh");
		String cmd = url.getPath();
		
		//String output = new Long(System.currentTimeMillis()).toString();
		//String outputFile = Config.getInstance().getTempDir() + output + ".txt";
		String outputFile = Config.getInstance().getTempDir() + "aux.txt";

		this.exec(cmd, path, outputFile);
		
		File file = new File(outputFile);
		List<String> listFiles = FileUtils.getInstance().getContents(file);
		
		for(String line: listFiles){
			//System.out.println(line);
			UnixFile ufile = getUnixFile(line);
			//System.out.println("ufile: " + ufile);
			if(ufile != null && ufile.isDirectory() && !ufile.isSymlink()){
				String dirpath = path + ufile.getName() + "/";
				//System.out.println(dirpath);
				listDirs.add(dirpath);
			}else if(ufile != null && ufile.isDirectory() && ufile.isSymlink()){
				
			}
		}

		//System.out.println("getDirectoriesFromPath: " + path + "=" + listDirs.size());
		return listDirs;
	}

	public List<String> getDirectoriesFromPathRecursively(String path, int depth){
		List<String> listDirsResult = new ArrayList<String>();
		
		//System.out.println("getDirectoriesFromPathRecursively: " + depth);
		
		if(depth == 0) depth = 1;
		List<String> listDirs = getDirectoriesFromPath(path); 
		listDirsResult.addAll(listDirs);
		if(depth == 1){
			for(String dir: listDirs){
				listDirsResult.addAll(getDirectoriesFromPath(dir));
			}
		}else{
			for(String dir: listDirs){
				listDirsResult.addAll(getDirectoriesFromPathRecursively(dir, depth-1));
			}
		}
		//System.out.println("getDirectoriesFromPathRecursively: " + path + "=" + listDirsResult.size());
		return listDirsResult;	
	}
	
	public List<String> findFilesWithWritablePermissionsGrantedToGeneralUsers(String path, int depth){

		File fpath = new File(path);
		System.out.println(path);
		if(fpath.isDirectory()){
			if(!path.substring(path.length()-1).equals("/")){
				path = path + "/";
			}
		}
		String[] pathdepth = path.split("/");
		int ipathDepth = pathdepth.length;
		
		//System.out.println(path);
		List<String> listDirs  = new ArrayList<String>();
		
		URL urlWW = OSService.class.getResource("/unix/scripts/_findWorldWritableAppend.sh");
		String cmdWW = urlWW.getPath();

		listDirs.addAll(getDirectoriesFromPathRecursively(path, depth));
		//System.out.println("listDirs.size" + listDirs.size());
		int currentDir = 1;
		String output = new Long(System.currentTimeMillis()).toString();
		//String outputFile = Config.getInstance().getTempDir() + "WW_" + output + ".txt";
		String outputFile = Config.getInstance().getTempDir() + "aux.txt";
		if(listDirs.size() > 0){
			//System.out.println(outputFile);
			File file = new File(outputFile);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(String line: listDirs){
				//System.out.println("Looking for World Writable Files/Directories " + currentDir + " of " + listDirs.size());
				//System.out.println("Current dir: " + line);
				String[] currentdepth = line.split("/");
				int icurrentDepth = currentdepth.length;
				currentDir ++;
				if(icurrentDepth != ipathDepth+depth){
					continue;
				}
				//System.out.println(cmdWW + " " + line);
				this.exec(cmdWW, line, outputFile);
			}
		}else{
			System.out.println("Looking for World Writable Files/Directories ");
			System.out.println("Current dir: " + path);
			this.exec(cmdWW, path, outputFile);
		}
		
		List<String> lines = FileUtils.getInstance().getContents(outputFile);
		//System.out.println("World Writable Permission: " + outputFile);
		
		return lines;
	}
	
	public String findFilesWithWritablePermissionsGrantedToOwner(String path, String owner){
		File fpath = new File(path);
		//System.out.println(path);
		if(fpath.isDirectory()){
			if(!path.substring(path.length()-1).equals("/")){
				path = path + "/";
			}
		}
		//System.out.println(path);
		List<String> listDirs  = new ArrayList<String>();
		
		URL urlWW = OSService.class.getResource("/unix/scripts/_findOwnedByUserWritable.sh");
		String cmdWW = urlWW.getPath();

		listDirs.addAll(getDirectoriesFromPathRecursively(path, 3));
		//System.out.println("listDirs.size" + listDirs.size());
		int currentDir = 1;
		//String output = new Long(System.currentTimeMillis()).toString();
		//String outputFile = Config.getInstance().getTempDir() + "_owner_" + owner + "_WW_" + output + ".txt";
		String outputFile = Config.getInstance().getTempDir() + "aux.txt";
		if(listDirs.size() > 0){
			//System.out.println(outputFile);
			File file = new File(outputFile);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(String line: listDirs){
				//System.out.println("Looking for World Writable Files/Directories " + currentDir + " of " + listDirs.size());
				//System.out.println("Current dir: " + line);
				//System.out.println(cmdWW + " " + line);
				this.exec(cmdWW, line, owner, outputFile);
				currentDir ++;
			}
		}
		
		return outputFile;
	}	
	
	public String findFilesWithWritablePermissionsGrantedToGroup(String path, String group){
		File fpath = new File(path);
		//System.out.println(path);
		if(fpath.isDirectory()){
			if(!path.substring(path.length()-1).equals("/")){
				path = path + "/";
			}
		}
		//System.out.println(path);
		List<String> listDirs  = new ArrayList<String>();
		
		URL urlWW = OSService.class.getResource("/unix/scripts/_findOwnedByGroupWritable.sh");
		String cmdWW = urlWW.getPath();

		listDirs.addAll(getDirectoriesFromPathRecursively(path, 3));
		//System.out.println("listDirs.size" + listDirs.size());
		int currentDir = 1;
		//String output = new Long(System.currentTimeMillis()).toString();
		//String outputFile = Config.getInstance().getTempDir() + "_group_" + group + "_WW_" + output + ".txt";
		String outputFile = Config.getInstance().getTempDir() + "aux.txt";
		if(listDirs.size() > 0){
			//System.out.println(outputFile);
			File file = new File(outputFile);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(String line: listDirs){
				//System.out.println("Looking for World Writable Files/Directories " + currentDir + " of " + listDirs.size());
				//System.out.println("Current dir: " + line);
				//System.out.println(cmdWW + " " + line);
				this.exec(cmdWW, line, group, outputFile);
				currentDir ++;
			}
		}
		
		return outputFile;		
	}	
	
	*/
	
}
