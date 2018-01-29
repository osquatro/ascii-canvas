package org.creditsuisse.common;

import static org.creditsuisse.common.Constants.MAX_CANVAS_HEIGHT;
import static org.creditsuisse.common.Constants.MAX_CANVAS_WIDTH;

public class ValidationUtil {
    /**
     * Canvas size is limited
     * @param width
     * @param height
     * @return
     */
    public static boolean isValidCanvasSize(int width, int height) {
        return (height <= MAX_CANVAS_HEIGHT && width <= MAX_CANVAS_WIDTH && height >= 0 && width >= 0);
    }

    /**
     * Only horizontal and vertical lines are allowed
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static boolean isValidLine(int x1, int y1, int x2, int y2){
        return x1 == x2 || y1 == y2;
    }
}
