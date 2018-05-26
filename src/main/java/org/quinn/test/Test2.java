package org.quinn.test;

import org.bytedeco.javacpp.DoublePointer;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;
import static org.bytedeco.javacpp.opencv_highgui.cvWaitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class Test2 {
    public static void main(String[] args) {
        IplImage src = cvLoadImage(FileUtils.getResPath("res/20180417142503.jpg"));
        IplImage gray = cvCreateImage(cvGetSize(src), 8, 1);

        cvCvtColor(src, gray, CV_BGR2GRAY);
        cvSmooth(gray, gray, CV_GAUSSIAN, 3, 5, 5, 5);
        cvShowImage("", gray);
        CvMemStorage mem = CvMemStorage.create();

        CvSeq circles = cvHoughCircles(
                gray, //Input image
                mem, //Memory Storage
                CV_HOUGH_GRADIENT, //Detection method
                10, //Inverse ratio
                1, //Minimum distance between the centers of the detected circles
                100, //Higher threshold for canny edge detector
                100, //Threshold at the center detection stage
                1, //min radius
                1000 //max radius
        );

        for (int i = 0; i < circles.total(); i++) {
            CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, i));
            CvPoint center = cvPointFrom32f(new CvPoint2D32f(circle.x(), circle.y()));
            int radius = Math.round(circle.z());
            cvCircle(src, center, radius, CvScalar.GREEN, 6, CV_AA, 0);
            System.out.println(radius);
        }

        cvShowImage("Result", src);
        cvWaitKey(0);

    }
}