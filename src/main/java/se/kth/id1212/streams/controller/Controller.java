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
package se.kth.id1212.streams.controller;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import se.kth.id1212.streams.filehandler.FileHandler;

/**
 * This controller decouples the view from the file handling.  All methods submit their task to the
 * common thread pool, provided by <code>ForkJoinPool.commonPool</code>, and then return immediately.
 */
public class Controller {
    private FileHandler fileHandler = new FileHandler();

    /**
     * @see FileHandler#createDir(java.lang.String)
     */
    public void createDir(String path) {
        CompletableFuture.runAsync(() -> {
            try {
                fileHandler.createDir(path);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * @see FileHandler#listDir(java.lang.String)
     */
    public void listDir(String path, Consumer showOutput) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return fileHandler.listDir(path);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        }).thenAccept(showOutput);
    }

    /**
     * @see FileHandler#write(java.lang.String, java.lang.String)
     */
    public void write(String path, String content) {
        CompletableFuture.runAsync(() -> {
            try {
                fileHandler.write(path, content);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            } catch (ClassNotFoundException cnfe) {
                throw new RuntimeException(cnfe);
            }
        });
    }

    /**
     * @see FileHandler#read(java.lang.String)
     */
    public void read(String path, Consumer showOutput) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return fileHandler.read(path);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            } catch (ClassNotFoundException ioe) {
                throw new RuntimeException(ioe);
            }
        }).thenAccept(showOutput);
    }

    /**
     * Takes very long time to complete. Used to illustrate responsive UI.
     */
    public void longRunningTask() {
        int tenSecs = 10000;
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(tenSecs);
            } catch (InterruptedException ignore) {
            }
        });
    }
}
