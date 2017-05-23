/*
 * The MIT License
 *
 * Copyright 2017 Leif Lindbäck <leifl@kth.se>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package se.kth.id1212.streams.filehandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Defines the functionality of the <code>filehandler</code> package.
 */
public class FileHandler {
    private final String root = ".";
    private Path workingDir = Paths.get(root);
    private static final int HEX_RADIX = 16;
    private static final String LINE_SEPARATOR = " ";

    /**
     * Files with this extension are supposed to contain text.
     */
    public static final String TEXT_FILE_EXTENSION = ".txt";

    /**
     * Files with this extension are supposed to contain serialized lists of strings
     * (java.util.List<String>).
     */
    public static final String OBJ_FILE_EXTENSION = ".ser";

    /**
     * Files with this extension are supposed to contain hexadecimal numbers;
     */
    public static final String HEX_FILE_EXTENSION = ".dat";

    /**
     * Creates an empty directory with the specified path, relative to the current working
     * directory. Nothing happens if the specified directory already exists.
     *
     * @param path The path of the newly created directory.
     * @throws IOException If failed to create directory.
     */
    public void createDir(String path) throws IOException {
        Path newDirPath = createAbsolutePathFromPathRelativeToWorkingDir(path);
        if (Files.exists(newDirPath)) {
            return;
        }
        Files.createDirectory(newDirPath);
    }

    /**
     * Lists the content of the specified path. If the specified path is a directory, returns a
     * space-separated list of the content of that directory. If the path is a file, returns the
     * name of that file.
     *
     * @param path The path to list.
     * @return The content of the specified path.
     */
    public String listDir(String path) throws IOException {
        Path listDirPath = createAbsolutePathFromPathRelativeToWorkingDir(path);
        if (Files.isDirectory(listDirPath)) {
            StringBuilder content = new StringBuilder();
            Files.list(listDirPath).forEach(
                    fileInDir -> appendElement(content, fileInDir.toString()));
            return stripCurrentDir(createReturnString(content));
        }
        return listDirPath.toString();
    }

    /**
     * Writes the specified content to the specified file. The file is created if it does not exist,
     * and the content is appended if the file already exists.
     *
     * @param path    The path of the file to which the content shall be written. File content is
     *                treated as either text, hex values or serialized objects, as specified by the
     *                extension. Note that the current version allows only one object, which means
     *                one write operation, per file. For text and hex files, there can be any number
     *                of write operations per file.
     * @param content The content that shall be written.
     * @throws IOException If failed to create file or write to it.
     */
    public void write(String path, String content) throws IOException, ClassNotFoundException {
        String file = workingDir.resolve(Paths.get(path)).toString();
        if (hasExtension(file, TEXT_FILE_EXTENSION)) {
            writeText(file, content);
        } else if (hasExtension(file, HEX_FILE_EXTENSION)) {
            writeHex(file, content);
        } else if (hasExtension(file, OBJ_FILE_EXTENSION)) {
            writeObj(file, content);
        }
    }

    /**
     * Returns a string containing the entire content of the specified file. The lines of the
     * specified files are concatenated into one single string, separated by a space character.
     *
     * @param path path to the file to read. File content is treated as either text, hex values or
     *             serialized objects, as specified by the extension.
     */
    public String read(String path) throws IOException, ClassNotFoundException {
        String file = workingDir.resolve(Paths.get(path)).toString();
        if (hasExtension(file, TEXT_FILE_EXTENSION)) {
            return readText(file);
        } else if (hasExtension(file, HEX_FILE_EXTENSION)) {
            return readHex(file);
        } else if (hasExtension(file, OBJ_FILE_EXTENSION)) {
            return readObj(file);
        }
        return null;
    }

    private String stripCurrentDir(String path) {
        return path.replaceAll("\\./", "");
    }

    private boolean hasExtension(String file, String extension) {
        return file.endsWith(extension);
    }

    private void writeText(String file, String content) throws IOException {
        try (PrintWriter toFile = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            toFile.println(content);
        }
    }

    private String readText(String file) throws IOException {
        try (BufferedReader fromFile = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            fromFile.lines().forEachOrdered(line -> appendElement(content, line));
            return createReturnString(content);
        }
    }

    private void writeHex(String file, String content) throws IOException {
        try (DataOutputStream toFile = new DataOutputStream(new BufferedOutputStream(
                new FileOutputStream(file, true)))) {
            for (String hexVal : content.split(" ")) {
                toFile.writeInt(Integer.parseInt(hexVal, HEX_RADIX));
            }
        }
    }

    private String readHex(String file) throws IOException {
        try (DataInputStream fromFile = new DataInputStream(new BufferedInputStream(
                new FileInputStream(file)))) {
            StringBuilder content = new StringBuilder();
            try {
                for (;;) {
                    appendElement(content, Integer.toString(fromFile.readInt(), HEX_RADIX));
                }
            } catch (EOFException doneReading) {
            }
            return createReturnString(content);
        }
    }

    private void writeObj(String file, String content) throws IOException {
        String[] elems = content.split(" ");
        List<String> contentAsList = Arrays.asList(elems);
        try (ObjectOutputStream toFile = new ObjectOutputStream(new BufferedOutputStream(
                new FileOutputStream(file, true)))) {
            toFile.writeObject(contentAsList);
        }
    }

    private String readObj(String file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream fromFile = new ObjectInputStream(new BufferedInputStream(
                new FileInputStream(file)))) {
            StringBuilder content = new StringBuilder();
            List<String> contentAsList = (List<String>) fromFile.readObject();
            contentAsList.stream().forEachOrdered(line -> appendElement(content, line));
            return createReturnString(content);
        }
    }

    private void appendElement(StringBuilder lines, String line) {
        lines.append(line);
        lines.append(LINE_SEPARATOR);
    }

    private String createReturnString(StringBuilder builder) {
        return builder.toString().trim();
    }

    private Path createAbsolutePathFromPathRelativeToWorkingDir(String relativePath) {
        return workingDir.resolve(Paths.get(relativePath));
    }
}
