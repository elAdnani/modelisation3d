package util;

import java.io.File;
import java.io.FileFilter;
/**
 * 
 * Cette classe sert à savoir si un fichier est en format PLY
 * 
 */
public class PlyFileFilter implements FileFilter {
	@Override
	public boolean accept(File pathname) {
		return pathname.isFile() && pathname.getName().endsWith(".ply");
	}
	
	public boolean accept(String name) {
		File pathname = new File(name);
		return accept(pathname);
	}
}