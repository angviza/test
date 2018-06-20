package org.jk.research;

import org.bytedeco.javacpp.opencv_core;
import org.jk.Utils.OpenCVUtilsJava;
import org.jk.research.chapter04.Ex2ComputeHistogramGraphJava;
import org.quinn.test.FileUtils;

import java.io.File;

import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class PixelAnalysis {
    public static OpenCVUtilsJava CV=new OpenCVUtilsJava();
    public static void main(String[] args){
        String path = FileUtils.getResPath("iris/bk");
        //String[] names={"20180417142503.jpg","20180418145543.jpg","1.jpg","2.jpg"};
        String names = "r20180418145543.jpg";
//        String names = "test.jpg";
        File source = new File(path + File.separator + names);
        test(source);
    }
    public static void test(File src){
//        opencv_core.Mat srcMat = CV.loadAndShowOrExit(src);
        opencv_core.Mat srcMat = CV.loadAndShowOrExit(src, IMREAD_GRAYSCALE);
        Ex2ComputeHistogramGraphJava.findZFT(srcMat);
        opencv_core.Mat newGray =new opencv_core.Mat();
        threshold(srcMat, newGray, 50, 255, CV_THRESH_BINARY | CV_THRESH_OTSU);
        OpenCVUtilsJava.show(newGray,"newGray");
        Ex2ComputeHistogramGraphJava.findZFT(newGray);
    }
}
