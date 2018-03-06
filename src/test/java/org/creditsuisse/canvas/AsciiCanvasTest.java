package org.creditsuisse.canvas;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class AsciiCanvasTest {
    ByteArrayOutputStream outContent;
    BufferedWriter writer;

    String emptyCanvas20x4 = "+====================+\n|                    |\n|                    |\n|                    |\n|                    |\n+====================+\n";
    String canvas20x4WithLine = "+====================+\n|                    |\n|                    |\n|  x                 |\n|  x                 |\n+====================+\n";
    String canvas20x4WithRectangle = "+====================+\n|     xxxxxxxxxxx    |\n|     x         x    |\n|     x         x    |\n|     xxxxxxxxxxx    |\n+====================+\n";
    String canvas20x4Filled = "+====================+\n|oooooooooooooooooooo|\n|oooooooooooooooooooo|\n|oooooooooooooooooooo|\n|oooooooooooooooooooo|\n+====================+\n";

    @Before
    public void init() {
        outContent = new ByteArrayOutputStream();
        writer = new BufferedWriter(new OutputStreamWriter(outContent));
        //System.setOut(new PrintStream(outContent));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalCanvasSize() {
        new AsciiCanvas(writer, 200, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedLine() {
        AsciiCanvas canvas = new AsciiCanvas(writer, 100, 100);
        canvas.line(10, 20, 11, 21);
    }

    @Test
    public void testEmptyCanvas() throws Exception {
        AsciiCanvas canvas = new AsciiCanvas(writer, 20, 4);
        canvas.paint();
        String out = new String( outContent.toByteArray(), "UTF-8" );
        assertEquals(emptyCanvas20x4, out);
    }

    @Test
    public void testLine() throws Exception {
        AsciiCanvas canvas = new AsciiCanvas(writer, 20, 4);
        canvas.line(2, 2, 2, 15);
        canvas.paint();
        String out = new String( outContent.toByteArray(), "UTF-8" );
        assertEquals(canvas20x4WithLine, out);
    }

    @Test
    public void testLineOutOfCanvas() throws Exception {
        AsciiCanvas canvas = new AsciiCanvas(writer, 20, 4);
        canvas.line(21, 5, 21, 10);
        canvas.paint();
        String out = new String( outContent.toByteArray(), "UTF-8" );
        assertEquals(emptyCanvas20x4, out);
    }

    @Test
    public void testClear() throws Exception {
        AsciiCanvas canvas = new AsciiCanvas(writer, 20, 4);
        canvas.line(2, 2, 2, 15);
        canvas.paint();
        String out = new String( outContent.toByteArray(), "UTF-8" );
        assertEquals(canvas20x4WithLine, out);
        outContent.reset();
        canvas.clear();
        canvas.paint();
        out = new String( outContent.toByteArray(), "UTF-8" );
        assertEquals(emptyCanvas20x4, out);
    }

    @Test
    public void testRectangle() throws Exception {
        AsciiCanvas canvas = new AsciiCanvas(writer, 20, 4);
        canvas.rectangle(5,0, 15, 3);
        canvas.paint();
        String out = new String( outContent.toByteArray(), "UTF-8" );
        assertEquals(canvas20x4WithRectangle, out);
    }

    @Test
    public void testFill() throws Exception {
        AsciiCanvas canvas = new AsciiCanvas(writer, 20, 4);
        canvas.fill(2,2, 0x6F);
        canvas.paint();
        String out = new String( outContent.toByteArray(), "UTF-8" );
        assertEquals(canvas20x4Filled, out);
    }

    @Test
    public void testFillOutOfCanvas() throws Exception {
        AsciiCanvas canvas = new AsciiCanvas(writer, 20, 4);
        canvas.fill(100,100, 0x6F);
        canvas.paint();
        String out = new String( outContent.toByteArray(), "UTF-8" );
        assertEquals(emptyCanvas20x4, out);
    }

    @Test
    public void testPaintToFile() throws Exception {
        String out = String.format("File_ascii_graphics_%d.dat", System.currentTimeMillis());
        FileOutputStream fstream = new FileOutputStream(out);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fstream));
        AsciiCanvas canvas = new AsciiCanvas(writer, 20, 4);
        canvas.fill(100,100, 0x6F);
        canvas.paint();
        writer.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(out)));
        String line = null;
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            buffer.append(line).append("\n");
        }
        assertEquals(emptyCanvas20x4, buffer.toString());
        new File(out).delete();
    }
}