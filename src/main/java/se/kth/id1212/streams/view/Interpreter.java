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

import java.io.IOException;
import java.util.Scanner;
import se.kth.id1212.streams.filehandler.FileHandler;

/**
 * Reads and interprets user commands. The command interpreter will run in a separate thread, which
 * is started by calling the <code>start</code> method.
 */
public class Interpreter implements Runnable {
    private static final String PROMPT = "> ";
    private Scanner console = new Scanner(System.in);
    private boolean receivingCmds = false;

    /**
     * Starts the interpreter. The interpreter will be waiting for user input when this method
     * returns. Calling <code>start</code> on an interpreter that is already started has no effect.
     */
    public void start() {
        if (receivingCmds) {
            return;
        }
        receivingCmds = true;
        new Thread(this).start();
    }

    /**
     * Interprets and performs user commands.
     */
    @Override
    public void run() {
        FileHandler fileHandler = new FileHandler();
        while (receivingCmds) {
            try {
                CmdLine cmdLine = new CmdLine(readNextLine());
                switch (cmdLine.getCmd()) {
                    case QUIT:
                        receivingCmds = false;
                        break;
                    case CREATEDIR:
                        fileHandler.createDir(cmdLine.getParameter(0));
                        break;
                    case WRITE:
                        fileHandler.write(cmdLine.getParameter(0), cmdLine.getParameter(1));
                        break;
                    default:
                        output("Invalid command");
                }
            } catch (IOException ioe) {
                output("Operation failed");
            }
        }
    }

    private String readNextLine() {
        System.out.print(PROMPT);
        return console.nextLine();
    }

    private void output(String msg) {
        System.out.println(msg);
    }

}
