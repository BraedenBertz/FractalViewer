/*
 * @author Braeden Bertz
 * Purpose: Draw Two Dimensional Fractal Images on the JavaFx Graphics Context
 * Version: 1.0
 */
package ImageViews;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.awt.*;
import java.awt.image.BufferedImage;

class DrawTwoDImages{

    /*
     * @Param: Graphics Context, level(level of recursion), Point 1(Lower left side), Point 2(Lower right side), Point 3(Upper Middle)
     * @Method: Recursively construct a Sierpinski Triangle with the given points and level of recursion on a Graphics Context
     */
    static void drawSierpinskiTriangle(GraphicsContext gc, int level, Point p1, Point p2, Point p3, Color color, double LineWidth) {
        //Draw the triangle
        gc.setStroke(color);
        gc.setLineWidth(LineWidth);
        gc.strokeLine(p1.x, p1.y, p2.x, p2.y);
        gc.strokeLine(p2.x, p2.y, p3.x, p3.y);
        gc.strokeLine(p3.x, p3.y, p1.x, p1.y);

        if (level > 0) {
            //find the mid-points of each of the sides of the triangle
            Point midP1P2 = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
            Point midP2P3 = new Point((p2.x + p3.x) / 2, (p2.y + p3.y) / 2);
            Point midP3P1 = new Point((p3.x + p1.x) / 2, (p3.y + p1.y) / 2);

            //use the new midpoints to draw the smaller triangles
            drawSierpinskiTriangle(gc, level - 1, p1, midP1P2, midP3P1, color, LineWidth);
            drawSierpinskiTriangle(gc, level - 1, p2, midP2P3, midP1P2, color, LineWidth);
            drawSierpinskiTriangle(gc, level - 1, p3, midP3P1, midP2P3, color, LineWidth);

        }
    }

    /*
     * @Param: Graphics Context, X starting coordinate, Y starting coordinate, area you want to occupy, Level of recursion
     * @Method: Construct a Sierpinski Carpet based off of a starting point and side length. 8X Recursive call for each level of recursion
     */
    static void drawSierpinskiCarpet(GraphicsContext gc, int x, int y, int side, int level, Color color) {
        // draw single black square in middle
        int sub = side / 3; // length of sub-squares
        gc.setFill(color);
        gc.fillRect(x + sub, y + sub, sub - 1, sub - 1);

        if (level != 0) {
                // now draw eight sub-gaskets around the white square
                drawSierpinskiCarpet(gc, x + sub, y, sub, level-1, color);//Top Middle
                drawSierpinskiCarpet(gc, x, y, sub, level-1, color);//Top Left
                drawSierpinskiCarpet(gc, x + 2 * sub, y, sub, level-1, color);//Top Right
                drawSierpinskiCarpet(gc, x, y + sub, sub, level-1, color);//Middle Left
                drawSierpinskiCarpet(gc, x + 2 * sub, y + sub, sub, level-1, color);//Middle Right
                drawSierpinskiCarpet(gc, x, y + 2 * sub, sub, level-1, color);//Bottom Left
                drawSierpinskiCarpet(gc, x + sub, y + 2 * sub, sub, level-1, color);//Bottom Middle
                drawSierpinskiCarpet(gc, x + 2 * sub, y + 2 * sub, sub, level-1, color);//Bottom Right

        }
    }

    /*
     * @Param: Graphics Context, Level of recursion, Point 1(Starting point of line), Point 5(Ending point of line)
     * @Method: Find the midpoint of the two given points and set that as the protruding point. Find the two connecting points, (Points 2 - 3 & points 3 - 4)
     */
    static void drawKochSnowflake(GraphicsContext gc, int level, Point FirstPoint, Point FifthPoint, Color color){
        int deltaX, deltaY;
        gc.setStroke(color);
        if (level == 0){
            gc.strokeLine(FirstPoint.x, FirstPoint.y, FifthPoint.x, FifthPoint.y);
        }
        else{
            deltaX = FifthPoint.x - FirstPoint.x;
            deltaY = FifthPoint.y - FirstPoint.y;
            
            Point SecondPoint = new Point();
            SecondPoint.setLocation(FirstPoint.x + deltaX / 3, FirstPoint.y + deltaY / 3);

            Point ThirdPoint = new Point();
            ThirdPoint.setLocation((0.5 * (FirstPoint.x+FifthPoint.x) + Math.sqrt(3) * (FirstPoint.y-FifthPoint.y)/6), (0.5 * (FirstPoint.y+FifthPoint.y) + Math.sqrt(3) * (FifthPoint.x-FirstPoint.x)/6));

            Point FourthPoint = new Point();
            FourthPoint.setLocation(FirstPoint.x + 2 * deltaX /3, FirstPoint.y + 2 * deltaY /3);

            drawKochSnowflake (gc,level-1, FirstPoint, SecondPoint, color);
            drawKochSnowflake (gc,level-1, SecondPoint, ThirdPoint, color);
            drawKochSnowflake (gc,level-1, ThirdPoint, FourthPoint, color);
            drawKochSnowflake (gc,level-1, FourthPoint, FifthPoint, color);
        }
    }

    /*
     * @Param:
     * @Method:
     */
    static void drawMandelbrotSet(GraphicsContext gc, int MAX_ITER, double Zoom){
        double zx;
        double zy;
        double cX;
        double cY;
        double tmp;

        BufferedImage image;
        image = new BufferedImage(805, 805, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 805; y++) {
            for (int x = 0; x < 805; x++) {
                zx = zy = 0;
                cX = (x - 400) / Zoom;
                cY = (y - 300) / Zoom;
                int iter = MAX_ITER;
                while (zx * zx + zy * zy < 4 && iter > 0) {
                    tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
                image.setRGB(x, y, iter | (iter << 8));
            }
        }
        javafx.scene.image.Image mandelbrot = SwingFXUtils.toFXImage(image, null);
        gc.drawImage(mandelbrot,0,0);
    }

    /*
     * @Param:
     * @Method:
     */

    static void drawJuliaSet(GraphicsContext gc, int w, int h, int MAXITER, double cX, double cY, double moveX, double moveY, double zoom){
        double zx;
        double zy;
        BufferedImage image = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);
        /*
       This math is too difficult to explain via inline-comments
       go HERE: http://lodev.org/cgtutor/juliamandelbrot.html
       for more info
       */
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                zx = 1.5 * (x - w / 2) / (0.5 * zoom * w) + moveX;
                zy = (y - h / 2) / (0.5 * zoom * h) + moveY;
                float i = MAXITER;
                while (zx * zx + zy * zy < 4 && i > 0) {
                    double tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    i--;
                }
                int c = java.awt.Color.HSBtoRGB((MAXITER / i) % 1, 1, i > 0 ? 1 : 0);
                image.setRGB(x, y, c);
            }
        }
        javafx.scene.image.Image julia = SwingFXUtils.toFXImage(image, null);
        gc.drawImage(julia, 0, 0);
    }

}