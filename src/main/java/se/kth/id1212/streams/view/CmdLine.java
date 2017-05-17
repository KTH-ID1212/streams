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

import java.util.ArrayList;
import java.util.List;

/**
 * One line of user input, which should be a command and parameters associated with that command (if
 * any).
 */
class CmdLine {
    private static final String PARAM_DELIMETER = " ";
    private String[] params;
    private Command cmd;

    /**
     * Creates a new instance representing the specified line.
     *
     * @param enteredLine A line that was entered by the user.
     */
    CmdLine(String enteredLine) {
        parseCmd(enteredLine);
        extractParams(enteredLine);
    }

    /**
     * @return The command represented by this object.
     */
    Command getCmd() {
        return cmd;
    }

    /**
     * Returns the parameter with the specified index. The first parameter has index zero.
     * Parameters are separated by a blank character (" "). A Character sequence enclosed in quotes
     * form one single parameter, even if it contains blanks.
     *
     * @param index The index of the searched parameter.
     * @return The parameter with the specified index, or <code>null</code> if there is no parameter
     *         with that index.
     */
    String getParameter(int index) {
        if (params == null) {
            return null;
        }
        if (index >= params.length) {
            return null;
        }
        return params[index];
    }

    private String removeExtraSpaces(String source) {
        if (source == null) {
            return source;
        }
        String oneOrMoreOccurences = "+";
        return source.trim().replaceAll(PARAM_DELIMETER + oneOrMoreOccurences, PARAM_DELIMETER);
    }

    private void parseCmd(String enteredLine) {
        int cmdNameIndex = 0;
        try {
            String[] enteredTokens = removeExtraSpaces(enteredLine).split(PARAM_DELIMETER);
            cmd = Command.valueOf(enteredTokens[cmdNameIndex].toUpperCase());
        } catch (Throwable failedToReadCmd) {
            cmd = Command.INVALID;
        }
    }

    private void extractParams(String enteredLine) {
        if (enteredLine == null) {
            return;
        }
        if (cmd.equals(Command.INVALID)) {
            return;
        }
        String withoutCmd = removeCmd(enteredLine);
        String readyForParsing = removeExtraSpaces(withoutCmd);
        List<String> foundParams = new ArrayList<>();
        int start = 0;
        boolean inQuotes = false;
        for (int index = 0; index < readyForParsing.length(); index++) {
            if (readyForParsing.charAt(index) == '\"') {
                inQuotes = !inQuotes;
            }
            if (isLastChar(index, readyForParsing)) {
                foundParams.add(readyForParsing.substring(start));
            } else if (timeToSplit(index, readyForParsing, inQuotes)) {
                foundParams.add(readyForParsing.substring(start, index));
                start = index + 1;
            }
        }
        params = foundParams.toArray(new String[0]);
    }

    private String removeCmd(String enteredLine) {
        int indexAfterCmd = enteredLine.toUpperCase().indexOf(cmd.name()) + cmd.name().length();
        String withoutCmd =  enteredLine.substring(indexAfterCmd, enteredLine.length());
        return withoutCmd.trim();
    }

    private boolean timeToSplit(int index, String source, boolean dontSplit) {
        return source.charAt(index) == PARAM_DELIMETER.charAt(0) && !dontSplit;
    }

    private boolean isLastChar(int index, String source) {
        return index == (source.length() - 1);
    }
}
