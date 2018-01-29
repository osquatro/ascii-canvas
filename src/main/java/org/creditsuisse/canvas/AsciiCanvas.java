package org.creditsuisse.canvas;

import java.io.BufferedWriter;
import java.io.IOException;

import static org.creditsuisse.common.Constants.*;
import static org.creditsuisse.common.ValidationUtil.isValidCanvasSize;

public class AsciiCanvas {

    private BufferedWriter writer;
    private final int width;
    private final int height;
    private final byte[][] canvasMatrix;

    public AsciiCanvas(BufferedWriter writer, int width, int height) {
        if (!isValidCanvasSize(width, height))
            throw new IllegalArgumentException(
                    String.format("Unsupported canvas size: %d x %d", width, height)
            );

        this.writer = writer;
        this.width = width + 2; // extra spaces for frame
        this.height = height + 2; // extra spaces for frame
        this.canvasMatrix = new byte[this.height][this.width];
        this.clear();
    }

    public void clear() {
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                if (isCorner(h, w))
                    canvasMatrix[h][w] = PLUS;
                else if (isVerticalFrameLine(w))
                    canvasMatrix[h][w] = PIPE;
                else if (isHorizontalFrameLine(h))
                    canvasMatrix[h][w] = EQUALS;
                else
                    canvasMatrix[h][w] = SPACE;
            }
        }
    }

    public void line(int x1, int y1, int x2, int y2) {
        if (x1 == x2) {
            int min = Math.min(y1, y2);
            int max = Math.max(y1, y2);
            for (int i = min; i <= max; i ++) {
                if (isPointWithinCanvas(x1+1, i+1))
                    canvasMatrix[i+1][x1+1] = X;
                else
                    break;
            }
        } else if (y1 == y2) {
            int min = Math.min(x1, x2);
            int max = Math.max(x1, x2);
            for (int i = min; i <= max; i++) {
                if (isPointWithinCanvas(i+1, y1+1))
                    canvasMatrix[y1+1][i+1] = X;
                else
                    break; // stop drawing - out of canvas
            }
        } else {
            throw new UnsupportedOperationException("AsciiCanvas doesn't support lines which aren't horizontal or vertical");
        }
    }

    public void rectangle(int x1, int y1, int x2, int y2) {
        if (x1 == x2 || y1 == y2) {
            line(x1, y1, x2, y2);
        } else {
            line(x1, y1, x1, y2);
            line(x2, y1, x2, y2);
            line(x1, y1, x2, y1);
            line(x2, y2, x1, y2);
        }
    }

    public void fill(int x, int y, int color) {
        fillCanvas(x + 1, y + 1, color);
    }

    private void fillCanvas(int x, int y, int color) {
        if (isPointWithinCanvas(x,y)) {
            if (!isPainted(x, y)) {
                canvasMatrix[y][x] = (byte) color;
                fillCanvas(x + 1, y, color);
                fillCanvas(x - 1, y, color);
                fillCanvas(x, y + 1, color);
                fillCanvas(x, y - 1, color);
            }
        }
    }

    public void paint() {
        try {
            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    writer.write((int) canvasMatrix[h][w]);
                }
                writer.write('\n');
            }
            writer.flush();
        } catch (IOException io) {
            throw new CanvasIOException("Error during writing to output, check device", io);
        }
    }

    private boolean isPointWithinCanvas(int x, int y) {
        return y < this.height-1 && y > 0 && x < this.width-1 && x > 0;
    }

    private boolean isCorner(int h, int w) {
        return (w == 0 && h == 0)
                || (w == width -1 && h == 0)
                || (w == width - 1 && h == height -1)
                || (w == 0 && h == height -1);
    }

    private boolean isHorizontalFrameLine(int h) {
        return h == 0 || h == height -1;
    }

    private boolean isVerticalFrameLine(int w) {
        return w == 0 || w == width - 1;
    }

    private boolean isPainted(int x, int y) {
        return canvasMatrix[y][x] != SPACE;
    }
}
