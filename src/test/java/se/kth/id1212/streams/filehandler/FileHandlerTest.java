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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileHandlerTest {
    private static final String TEST_AREA_PATH = "testarea";
    private Path testArea = Paths.get(TEST_AREA_PATH);
    private FileHandler instance;

    @Before
    public void setUp() throws IOException {
        Files.createDirectory(testArea);
        instance = new FileHandler();
    }

    @After
    public void tearDown() throws IOException {
        Files.delete(testArea);
        instance = null;
    }

    @Test
    public void testListDir() throws IOException {
        String result = instance.listDir(".");
        String expResult = TEST_AREA_PATH;
        assertTrue("Did not create correct directory.", result.contains(expResult));
    }

    @Test
    public void testCreateAndListDir() throws IOException {
        String path = TEST_AREA_PATH + "/testDir";
        try {
            instance.createDir(path);
            String result = instance.listDir(TEST_AREA_PATH);
            String expResult = path;
            assertEquals("Did not create correct directory.", expResult, result);
        } finally {
            Files.deleteIfExists(Paths.get(path));
        }
    }

    @Test
    public void testWriteReadTextFile() throws IOException, ClassNotFoundException {
        String path = TEST_AREA_PATH + "/test.txt";
        String line1 = "1 sdf rt  yj sdssc ss rew";
        String line2 = "2";
        String line3 = "3 df t rth  ds   sd sdw ww ww";
        String lineSep = " ";
        try {
            instance.write(path, line1);
            instance.write(path, line2);
            instance.write(path, line3);
            String expResult = line1 + lineSep + line2 + lineSep + line3;
            String result = instance.read(path);
            assertEquals("Did not read what was written.", expResult, result);
        } finally {
            Files.deleteIfExists(Paths.get(path));
        }
    }

    @Test
    public void testWriteReadHexFile() throws IOException, ClassNotFoundException {
        String path = TEST_AREA_PATH + "/test.dat";
        String line1 = "1 2 3 4 5";
        String line2 = "2";
        String line3 = "3 45 546 657";
        String lineSep = " ";
        try {
            instance.write(path, line1);
            instance.write(path, line2);
            instance.write(path, line3);
            String expResult = line1 + lineSep + line2 + lineSep + line3;
            String result = instance.read(path);
            assertEquals("Did not read what was written.", expResult, result);
        } finally {
            Files.deleteIfExists(Paths.get(path));
        }
    }

    @Test
    public void testWriteReadSerFile() throws IOException, ClassNotFoundException {
        String path = TEST_AREA_PATH + "/test.ser";
        String line = "1 sdf rt  yj sdssc ss rew";
        try {
            instance.write(path, line);
            String expResult = line;
            String result = instance.read(path);
            assertEquals("Did not read what was written.", expResult, result);
        } finally {
            Files.deleteIfExists(Paths.get(path));
        }
    }
}
