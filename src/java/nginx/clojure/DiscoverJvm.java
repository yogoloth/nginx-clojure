/**
 *  Copyright (C) Zhang,Yuexiang (xfeep)
 *
 */
package nginx.clojure;

import java.io.File;


public class DiscoverJvm {

	public DiscoverJvm() {
	}

	public static File search(File dir) {
		if (!dir.exists()) {
			return null;
		}
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				File r = search(f);
				if (r != null) {
					return r;
				}
			}else {
				String fn = f.getName();
				if (fn.endsWith("jvm.so") || fn.endsWith("jvm.dll") || fn.endsWith("jvm.dylib")) {
					return f;
				}
			}
		}
		return null;
	}
	
	public static String getJvm() {
		//jrePath e.g. usr/lib/jvm/java-7-oracle/jre/lib/amd64
		String jrePath = System.getProperty("sun.boot.library.path");
		if (jrePath == null) {
			System.err.println("System property sun.boot.library.path not found!");
			return null;
		}
		//pick server jvm first
		File jvm = search(new File(jrePath, "server"));
		if (jvm == null) {
			jvm = search(new File(jrePath));
			if (jvm == null) {
				System.err.println("jvm not found!");
				return null;
			}
		}
		
		return (jvm.getAbsolutePath());
	}
	
	public static String getJniIncludes() {
		String home = System.getProperty("java.home");
		if (home.endsWith("jre")) {
			home = new File(home).getParent();
		}
		String incRoot = home + "/include";
		StringBuilder sb = new StringBuilder("-I " + incRoot);
		for (File f : new File(incRoot).listFiles()) {
			if (f.isDirectory()) {
				sb.append(" -I ").append(f.getAbsolutePath());
			}
		}
		return sb.toString(); 
	}
	
	public static void main(String[] args) {
		System.out.println(getJvm());
	}

}
