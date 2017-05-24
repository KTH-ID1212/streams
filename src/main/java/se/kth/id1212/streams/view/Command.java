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
package se.kth.id1212.streams.view;

/**
 * Defines all commands that can be performed by the file handling application.
 */
public enum Command {
    /**
     * Create an empty directory.
     */
    CREATEDIR, 
    
    /**
     * Append content to a file.
     */
    WRITE,
    
    /**
     * Print the content of a file.
     */
    READ,
    
    /**
     * Delete a file or directory.
     */
    DELETE,
    
    /**
     * List the files in a directory.
     */
    LIST,
    
    /**
     * Copy a file or directory.
     */
    COPY,
    
    /**
     * Move (rename) a file or directory.
     */
    MOVE,
    
    /**
     * Leave the file handler.
     */
    QUIT,
    
    /**
     * A very long running task, used to illustrate responsive UI.
     */
    SLOWCMD,
    
    /**
     * Invalid command
     */
    INVALID
}
