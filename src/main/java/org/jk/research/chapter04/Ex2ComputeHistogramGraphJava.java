package org.jk.research.chapter04;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.jk.Utils.OpenCVUtilsJava.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;

/**
 * The second example for section "Computing the image histogram" in Chapter 4, page 92.
 * Displays a graph of a histogram created using utility class [[opencv_cookbook.chapter04.Histogram1D]].
 */
public class Ex2ComputeHistogramGraphJava {

    public static void main(final String[] args) {

        Mat src = loadAndShowOrExit(new File("data/group.jpg"), IMREAD_GRAYSCALE);
        findZFT(src);

    }

    public static void findZFT(Mat src) {
        // Calculate histogram
        Histogram1DJava h = new Histogram1DJava();
        BufferedImage histogram = h.getHistogramImage(src);
        // Display the graph
        show(histogram, "Histogram");
    }

    public static int[] getZFT(Mat src) {
        // Calculate histogram
        Histogram1DJava h = new Histogram1DJava();
        return h.getHistogramInt(src);
    }
}