package org.quinn.test;

import org.bytedeco.javacpp.DoublePointer;

import java.util.Arrays;

import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

public class Test1 {
    public static void main(String[] args) {
//Original Image
        IplImage src = cvLoadImage(FileUtils.getResPath("res/20180417142503.jpg"), 0);
//Template Image
        IplImage tmp = cvLoadImage(FileUtils.getResPath("res/20180418145543.jpg"), 0);
//The Correlation Image Result
        IplImage result = cvCreateImage(cvSize(src.width() - tmp.width() + 1, src.height() - tmp.height() + 1), IPL_DEPTH_32F, 1);
//Init our new Image
        cvZero(result);
        cvMatchTemplate(src, tmp, result, CV_TM_CCORR_NORMED);

//        double[] min_val = new double[2];
//        double[] max_val = new double[2];
        DoublePointer min_val = new DoublePointer();
        DoublePointer max_val = new DoublePointer();
//Where are located our max and min correlation points
        CvPoint minLoc = new CvPoint();
        CvPoint maxLoc = new CvPoint();
        cvMinMaxLoc(result, min_val, max_val, minLoc, maxLoc, null); //the las null it's for  optional mask mat()

        System.out.println(min_val); //Min Score.
        System.out.println(max_val); //Max Score

        CvPoint point = new CvPoint();
        point.x(maxLoc.x() + tmp.width());
        point.y(maxLoc.y() + tmp.height());
        cvRectangle(src, maxLoc, point, CvScalar.WHITE, 2, 8, 0); //Draw the rectangule result in original img.
        cvShowImage("Lena Image", src);
        cvWaitKey(0);
//Release
        cvReleaseImage(src);
        cvReleaseImage(tmp);
        cvReleaseImage(result);
    }
}