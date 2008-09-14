package com.blueskyminds.framework.tools;

import com.blueskyminds.framework.tools.filters.StringFilter;
import com.blueskyminds.framework.tools.text.StringTools;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.List;
import java.util.LinkedList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 1/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class FileTools {

    private static final Log LOG = LogFactory.getLog(FileTools.class);

    private static final String WIN_SEPARATOR = "\\";
    private static final String STD_SEPARATOR = "/";
    private static final String ALL_SEPARATORS = STD_SEPARATOR + WIN_SEPARATOR;
    /**
     * A string of canonical file separators of the same length as the ALL_SEPARATORS string
     */
    private static final String REPLACEMENT_SEPAPATOR_STRING = StringTools.fill(File.separator, ALL_SEPARATORS.length());

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the FileTools with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates/overwrites a text file with the lines of text
     */
    public static void writeTextFile(String filename, String[] lines) throws IOException {
        File file = new File(filename);
        PrintWriter writer = new PrintWriter(file);
        for (String line : lines) {
            writer.println(line);
        }
        writer.close();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Reads a text file into an array of strings.
     */
    public static String[] readTextFile(URI location) throws IOException {

        StringFilter filter = new StringFilter() {
            public boolean accept(String string) {
                return true;
            }
        };
        return readTextFile(location, filter);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Reads a text file into an array of strings.  Each line is included only if it passes the filter
     */
    public static String[] readTextFile(URI location, StringFilter filter) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceTools.openStream(location)));

        List<String> lines = new LinkedList<String>();
        String line;

        while ((line = reader.readLine()) != null) {
            if (filter.accept(line)) {
                lines.add(line);
            }
        }

        reader.close();

        String[] result = new String[lines.size()];
        return lines.toArray(result);
    }

    /**
     * Concatenate two canonical filesystem path strings together detecting the need for a path separator character
     *
     * If  a separator needs to be inserted the canonical File.separatorChar is used
     *
     * @param pathA left path coponent
     * @param pathB right path component
     * @return contatenated path
     */
    public static String concatenateCanonicalPath(String pathA, String pathB) {
        return concatenatePaths(pathA, pathB, File.separatorChar);
    }

    /**
     * Concatenate multiple canonical path components
     *
     * @param paths to concatenate
     * @return contatenated canonical filesystem path.  Null if no parameteters
     */
    public static String concatenateCanonicalPaths(String... paths) {
        String result = null;

        if (paths.length > 0) {
            result = paths[0];

            for (int index = 1; index < paths.length; index++) {
                result = concatenateCanonicalPath(result, paths[index]);
            }
        }
        return result;
    }

    /**
     *  Concatenate two path strings together detecting the need for a path separator character.
     *  If a separator is needed the supplied separator char is used
     *
     * @param pathA left path component
     * @param pathB right path component
     * @return concatenated path
     */
    public static String concatenatePaths(String pathA, String pathB, char separator) {
        String result;

        // ensure the pathA doesn't end with any of the permitted file separators
        String trimmedPathA = StringUtils.stripEnd(pathA, ALL_SEPARATORS);

        // ensure the pathB doesn't start with any of the permitted separators
        String trimmedPathB = StringUtils.stripStart(pathB, ALL_SEPARATORS);

        if (StringUtils.isNotBlank(trimmedPathA)) {
            if (StringUtils.isNotBlank(trimmedPathB)) {
                // join the two strings with a canonical file separator
                result = trimmedPathA + separator + trimmedPathB;
            } else {
                result = pathA;
            }
        } else {
            if (StringUtils.isNotBlank(trimmedPathB)) {
                result = pathB;
            } else {
                result = "";
            }
        }

        return result;
    }

    /**
     *  Concatenate two path strings together detecting the need for a path separator character.
     *  Uses File.pathSeparatorChar
     *
     * @param pathA left path component
     * @param pathB right path component
     * @return concatenated path
     */
    public static String concatenatePaths(String pathA, String pathB) {
        return concatenatePaths(pathA, pathB, File.separatorChar);
    }

    /** 
     * Concatenate all the paths accounting for the path separator character.
     *
     * @param paths to concatenate
     * @return contatenated path
     * */
    public static String concatenatePaths(char separator, String... paths) {
        String result = "";

        if (paths.length > 0) {
            result = paths[0];

            for (int index = 1; index < paths.length; index++) {
                result = concatenatePaths(result, paths[index], separator);
            }
        }
        return result;

    }

    /**
     * Concatenate path2 onto the uri accounting for the path separator character.
     *
     * There's probably better ways to implement this (eg. accounting for ../ properly)
     *
     * @param uri first path
     * @param path2 appended to url1
     * @return contatenated path onto url
     **/
    public static URI concatenateURI(URI uri, String path2) {
        String result;

        String path1 = uri.toString();

        return concatenateURI(path1, path2);
    }

    /**
     * Concatenate path2 onto the uri accounting for the path separator character.
     * There's probably better ways to implement this (eg. accounting for ../ properly)
     *
     * @param uri first path
     * @param path2 appended to url
     * @return contatenated path onto url
     **/
    public static URI concatenateURI(String uri, String path2) {
        String result;

        String path1 = uri;

        // ensure the path1 doesn't end with either possible URL file separator
        path1 = StringUtils.removeEnd(path1, ALL_SEPARATORS);

        // ensure the path2 doesn't start with either possible file separator
        path2 = StringUtils.removeStart(path2, ALL_SEPARATORS);
        path2 = StringUtils.replaceChars(path2, WIN_SEPARATOR, STD_SEPARATOR);

        result = path1 + STD_SEPARATOR + path2;

        try {
            URI finalUri = new URI(result);
            return finalUri;
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * Determine if a path is absolute or not (relative)
     *
     * @param path path for any typical filesystem
     * @return true if absolute
     */
    public static boolean isAbsolutePath(String path) {
        return (path.startsWith(STD_SEPARATOR) || (path.startsWith(WIN_SEPARATOR)));
    }

    /**
     * Converts the file separators in a path to the canonical form.
     *
     * @param path a file system path
     * @return the path in the canonical filesepartor form
     */
    public static String toCanonicalPath(String path) {
        /// need two strings of the same length
        return StringUtils.replaceChars(path, ALL_SEPARATORS, REPLACEMENT_SEPAPATOR_STRING);
    }

    /**
     * Determines if a file exists in the filesystem
     *
     * @param name name of the file (including path if required)
     * @return true if that file exists (and is is accessible) in the filesystem
     */
    public static boolean fileExists(String name) {
        if (name != null) {
            File f = new File(name);
            return f.exists();
        } else {
            return false;
        }
    }

    /**
     * Read an entire input stream into a byte[] buffer
     *
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        int bufSize = 1024 * 1024;
        byte[] content;

        List<byte[]> parts = new LinkedList();
        InputStream in = new BufferedInputStream(inputStream);

        byte[] readBuffer = new byte[bufSize];
        byte[] part;
        int bytesRead = 0;

        // read everyting into a list of byte arrays
        while ((bytesRead = in.read(readBuffer, 0, bufSize)) != -1) {
            part = new byte[bytesRead];
            System.arraycopy(readBuffer, 0, part, 0, bytesRead);
            parts.add(part);
        }

        // calculate the total size
        int totalSize = 0;
        for (byte[] partBuffer : parts) {
            totalSize += partBuffer.length;
        }

        // allocate the array
        content = new byte[totalSize];
        int offset = 0;
        for (byte[] partBuffer : parts) {
            System.arraycopy(partBuffer, 0, content, offset, partBuffer.length);
            offset += partBuffer.length;
        }

        return content;
    }

    public static void serializeToDisk(Serializable object, String fileName) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
        out.writeObject(object);
    }

    public static Object serializeFromDisk(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
        return in.readObject();
    }

    public static Object serializeFromDisk(URI uri) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(ResourceTools.openStream(uri));
        return in.readObject();
    }

    /**
     * Returns the containing folder of the given file/folder
     *
     * Trailing slash = true:
     * eg. parentFolder("/usr/bin") -> "/usr/"
     *     parentFolder("/usr") -> "/"
     *     parentFolder("c:\program files\tools") -> "c:\program files\"
     *     parentFolder("c:\program files\") -> "c:\"
     *
     * Trailing slash = false:
     * eg. parentFolder("/usr/bin") -> "/usr"
     *     parentFolder("/usr") -> "/"
     *     parentFolder("c:\program files\tools") -> "c:\program files"
     *     parentFolder("c:\program files\") -> "c:\"
     *
     * @return the result always has a trailing slash, except in the case where there's no containing folder in which case
     *  the result will be blank.
     *
     */
    public static String containingFolder(String path, boolean trailingSlash) {
        if (path != null) {
            path = StringUtils.stripEnd(path, ALL_SEPARATORS);
            int lastWin = path.lastIndexOf(WIN_SEPARATOR);
            int lastStd = path.lastIndexOf(STD_SEPARATOR);

            int last = Math.max(lastWin, lastStd);
            if (last >= 0) {
                String container = path.substring(0, last);

                if (lastWin > 0) {
                    container = container+WIN_SEPARATOR;
                } else {
                    container = container+STD_SEPARATOR;
                }

                if (!trailingSlash) {
                    if (((container.length() == 3) && (container.charAt(1) == ':'))) {
                        return container;
                    } else {
                        if (container.length() == 1) {
                            return container;
                        } else {
                            return StringUtils.stripEnd(container,  ALL_SEPARATORS);
                        }
                    }
                } else {
                    return container;
                }
            } else {
                return "";
            }
        } else {
            return null;
        }
    }

    /**
     * Visits all of the files at the specified URL.  Works with directories within the file system
     *  (file://) and jars (jar://)
     *
     * @param path
     * @param recursive
     * @param visitor
     * @throws IOException
     */
    public static void visitFiles(URI path, boolean recursive, FileVisitor visitor) throws IOException {
        String uri = path.toString();
        String jarFile = null;
        String directory = null;

        LOG.debug("Visiting files in: "+path);

        if (uri.startsWith("jar:")) {
            jarFile = StringUtils.substringAfter(StringUtils.substringBefore(uri, "!"), "file:");
            File file = new File(StringUtils.substringAfter(uri, "jar!/"));
            if (file.isDirectory()) {
                directory = StringUtils.substringAfter(uri, "jar!/");
            } else {
                directory = StringUtils.remove(StringUtils.substringAfter(uri, "jar!/"), file.getName());
            }
        } else {
            if (uri.startsWith("file:")) {
                directory = new File(path).getAbsolutePath();
            }
        }

        if (jarFile == null) {
            visitFilesInDirectory(directory, recursive, visitor);
        } else {
            visitFilesInJar(jarFile, directory, recursive, visitor);
        }

    }

    /** Recursive method to find all the files in a directory */
    private static void visitFilesInDirectory(String directory, boolean recursive, FileVisitor vistor) throws IOException {
        List<String> list = new LinkedList<String>();

        File currentDir = new File(directory);
        if (currentDir.isDirectory()) {
            File[] files = currentDir.listFiles();

            for (File item : files) {
                LOG.debug("   Found file in directory: "+item.getAbsolutePath());
                vistor.visit(item.toURI(), item.isDirectory());
                if (item.isDirectory() && (recursive)) {
                    visitFilesInDirectory(item.getPath(), recursive, vistor);
                }
            }
        }
    }

    /**
     * Calls the visitor for each file in the specified jar directory
     *
     * @param jarFileName
     * @param visitor to use when a class is found
     *
     * @throws IOException
     */
    public static void visitFilesInJar(String jarFileName, String baseDirectory, boolean recursive, FileVisitor visitor) throws IOException {
        JarFile jarFile = new JarFile(jarFileName);

        Enumeration entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String entryName = entry.getName();

            if (recursive) {
                if (entryName.startsWith(baseDirectory)) {
                    try {
                        visitor.visit(new URI("jar:file://"+jarFileName+"!/"+entryName), entry.isDirectory());
                    } catch(URISyntaxException e) {

                    }
                }
            } else {
                // ensure it's only at this directory level
                if (entryName.lastIndexOf('/') == baseDirectory.lastIndexOf('/')) {
                    LOG.debug("Found a file in a jar: "+entryName);
                    try {
                        visitor.visit(new URI("jar:file:/"+jarFileName+"!/"+entryName), entry.isDirectory());
                    } catch(URISyntaxException e) {

                    }
                }
            }
        }

        jarFile.close();
    }

    /** Non-recursive list of subdirectories */
    public static List<URI> listDirectories(URI baseDir) throws IOException {
        final List<URI> subDirs = new LinkedList<URI>();
        LOG.debug("searching for directories in : "+baseDir);
        visitFiles(baseDir, false, new FileVisitor() {
            public void visit(URI pathToFile, boolean isDirectory) {
                if (isDirectory) {
                    LOG.debug("visited directory: "+pathToFile);
                    subDirs.add(pathToFile);
                }
            }
        });
        return subDirs;
    }

    /** Non-recursive list of all files and subdirectories */
    public static List<URI> listFiles(URI baseDir) throws IOException {
        final List<URI> files = new LinkedList<URI>();
        LOG.debug("searching for files in : "+baseDir);
        visitFiles(baseDir, false, new FileVisitor() {
            public void visit(URI pathToFile, boolean isDirectory) {
                if (!isDirectory) {
                    LOG.debug("visited file: "+pathToFile);
                    files.add(pathToFile);
                }
            }
        });
        return files;
    }

     /**
      * Remove the trailing path separator(s)
      **/
    public static String stripTrailingPathSeparator(String path) {
        return StringUtils.stripEnd(path, ALL_SEPARATORS);
    }

    private static String decodeUrl(String encodedUrl) {
        try {
            return URLDecoder.decode(encodedUrl, "UTF-8");
        } catch(UnsupportedEncodingException e) {
            return encodedUrl;
        }
    }

    /**
     * Extract the last filename component from a URL, or the path name is the URL is not to a file
     *
     *  Unlike the various URL methods, this exludes the path and query
     *
     * @return the filename component, or null if it's not specified */
    public static String extractLastFilenameOrPathname(URI uri) {
        String path = decodeUrl(uri.getPath());
        return extractLastFilenameOrPathname(path);
    }


    /**
     * Extract the last filename component from a path, or the path name is the path is not to a file
     *
     *  Unlike the various URL methods, this  exludes the path and query
     *
     * @return the filename component, or null if it's not specified */
    public static String extractLastFilenameOrPathname(String path) {
        String file = null;

        String[] parts = StringUtils.split(path, "/");
        if (parts.length > 1) {
            file = parts[parts.length-1];
        } else {
            file = parts[0];
        }

        return file;
    }

    public static boolean createDirectory(String path) {
        return new File(path).mkdirs();
    }
}
