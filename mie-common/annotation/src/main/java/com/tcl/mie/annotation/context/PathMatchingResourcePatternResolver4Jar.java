package com.tcl.mie.annotation.context;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class PathMatchingResourcePatternResolver4Jar extends org.springframework.core.io.support.PathMatchingResourcePatternResolver {
	private PathMatcher pathMatcher4Jar = new AntPathMatcher();

	public PathMatchingResourcePatternResolver4Jar() {
		super();
	}

	public PathMatchingResourcePatternResolver4Jar(ClassLoader classLoader) {
		super(classLoader);
	}

	public PathMatchingResourcePatternResolver4Jar(ResourceLoader resourceLoader) {
		super(resourceLoader);
	}

	public Resource[] getResources4Jar(String[] libPaths, String... locationPatterns) throws IOException {
		if (libPaths == null)
			return new Resource[0];
		String[][] pathPatterns = new String[locationPatterns.length][2];
		for (int i = 0; i < locationPatterns.length; i++) {
			String locationPattern = locationPatterns[i];
			pathPatterns[i] = new String[2];
			pathPatterns[i][0] = determineRootDir(locationPattern);
			pathPatterns[i][1] = locationPattern.substring(pathPatterns[i][0].length());
		}
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Set<Resource> result = new HashSet<Resource>(32);
		String classPath = PathMatchingResourcePatternResolver4Jar.class.getResource("/").getPath();
		for (String libPath : libPaths) {
			// 相对路径
			if (!libPath.startsWith("/")) {
				libPath = classPath + libPath;
			}
			File libRoot = new File(libPath);
			if (!libRoot.exists()) {
				continue;
			}
			File[] libs = libRoot.listFiles(new FileFilter() {
				public boolean accept(File dir) {
					String name = dir.getName().toLowerCase();
					return name.endsWith("jar");
				}
			});
			for (File file : libs) {
				JarFile jarFile = null;
				try {
					jarFile = new JarFile(file.getCanonicalFile());
					for (Enumeration entries = jarFile.entries(); entries.hasMoreElements();) {
						JarEntry entry = (JarEntry) entries.nextElement();
						String entryPath = entry.getName();
						for (String[] pathPattern : pathPatterns) {
							String rootDirPath = pathPattern[0];
							String subPattern = pathPattern[1];
							if (entryPath.startsWith(rootDirPath)) {
								String relativePath = entryPath.substring(rootDirPath.length());
								if (getPathMatcher().match(subPattern, relativePath)) {
									URL url = classLoader.getResource(entry.getName());
									result.add(new UrlResource(url));
								}
							}
						}
					}
				} finally {
					jarFile.close();
				}
			}
		}
		return result.toArray(new Resource[result.size()]);
	}

	@Override
	public PathMatcher getPathMatcher() {
		return pathMatcher4Jar;
	}

}
