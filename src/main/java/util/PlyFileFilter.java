package util;

import java.io.File;
import java.io.FileFilter;

public class PlyFileFilter implements FileFilter {
	@Override
	public boolean accept(File pathname) {
		return pathname.isFile() && pathname.getName().endsWith(".ply");
	}
}