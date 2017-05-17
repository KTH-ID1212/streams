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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmdLineTest {
    @Test
    public void testExistingLowerCaseCmd() {
        String enteredLine = "copy";
        CmdLine instance = new CmdLine(enteredLine);
        Command expResult = Command.COPY;
        Command result = instance.getCmd();
        assertEquals("Did not find existing command.", expResult, result);
    }

    @Test
    public void testExistingCmdExtraSpaces() {
        String enteredLine = "   copy   ";
        CmdLine instance = new CmdLine(enteredLine);
        Command expResult = Command.COPY;
        Command result = instance.getCmd();
        assertEquals("Did not find existing command.", expResult, result);
    }

    @Test
    public void testExistingUpperCaseCmd() {
        String enteredLine = "MOVE";
        CmdLine instance = new CmdLine(enteredLine);
        Command expResult = Command.MOVE;
        Command result = instance.getCmd();
        assertEquals("Did not find existing command.", expResult, result);
    }

    @Test
    public void testNonExistingCmd() {
        String enteredLine = "does not exist";
        CmdLine instance = new CmdLine(enteredLine);
        Command expResult = Command.INVALID;
        Command result = instance.getCmd();
        assertEquals("Did not find non-existing command.", expResult, result);
    }

    @Test
    public void testEmptyCmd() {
        String enteredLine = "";
        CmdLine instance = new CmdLine(enteredLine);
        Command expResult = Command.INVALID;
        Command result = instance.getCmd();
        assertEquals("Did not find non-existing command.", expResult, result);
    }

    @Test
    public void testNullCmd() {
        String enteredLine = null;
        CmdLine instance = new CmdLine(enteredLine);
        Command expResult = Command.INVALID;
        Command result = instance.getCmd();
        assertEquals("Did not find non-existing command.", expResult, result);
    }

    @Test
    public void testGetExistingParameters() {
        String firstParam = "param1";
        String secondParam = "param2";
        String delimeter = " ";
        String enteredLine = "Copy" + delimeter + firstParam + delimeter + secondParam;
        CmdLine instance = new CmdLine(enteredLine);
        String expResult = firstParam;
        String result = instance.getParameter(0);
        assertEquals("Did not find first parameter.", expResult, result);
        expResult = secondParam;
        result = instance.getParameter(1);
        assertEquals("Did not find second parameter.", expResult, result);
    }

    @Test
    public void testGetExistingParametersExtraSpaces() {
        String firstParam = "param1";
        String secondParam = "param2";
        String delimeter = " ";
        String enteredLine = "Copy" + delimeter + delimeter + firstParam + delimeter + delimeter
                             + delimeter + secondParam + delimeter + delimeter;
        CmdLine instance = new CmdLine(enteredLine);
        String expResult = firstParam;
        String result = instance.getParameter(0);
        assertEquals("Did not find first parameter.", expResult, result);
        expResult = secondParam;
        result = instance.getParameter(1);
        assertEquals("Did not find second parameter.", expResult, result);
    }

    @Test
    public void testGetNonExistingParameterWhenThereAreParameters() {
        String firstParam = "param1";
        String secondParam = "param2";
        String delimeter = " ";
        String enteredLine = "Copy" + delimeter + firstParam + delimeter + secondParam;
        CmdLine instance = new CmdLine(enteredLine);
        String expResult = null;
        String result = instance.getParameter(2);
        assertEquals("Found non-existing parameter.", expResult, result);
    }

    @Test
    public void testGetNonExistingParameterWhenThereAreNoParameters() {
        String enteredLine = "Copy";
        CmdLine instance = new CmdLine(enteredLine);
        String expResult = null;
        String result = instance.getParameter(0);
        assertEquals("Found non-existing parameter.", expResult, result);
    }
}
