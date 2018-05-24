package com.jk.fight.www.Utils;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static com.jk.fight.www.dome.ShowImg.*;

import org.bytedeco.javacpp.BytePointer;

public class CircleUtils {
    /**
     * 传入参数必须为单通道灰度图，查找虹膜（外圆）的的坐标并返回
     *
     * @param grayImage
     * @return
     */
    public static Point3f searchIris(Mat gray) {
        IplImage src = new IplImage(gray);
        IplImage newGray = cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1);
        cvThreshold(src, newGray, 50, 255, CV_THRESH_BINARY | CV_THRESH_OTSU);
        cvErode(newGray, newGray, null, 3);
        cvCanny(newGray, newGray, 50, 100, 3);
//        ShowImage(newGray, "searchIris-newGray");
        CvSeq results = cvHoughCircles(newGray, CvMemStorage.create(), CV_HOUGH_GRADIENT,
                1, 1000, 100, 10, 20, 300);
        BytePointer p = cvGetSeqElem(results, 0);
        cvReleaseImage(newGray);
        newGray = null;
        return new Point3f(p);
    }

    public static Point3f searchPupil(Mat gray) {
        IplImage src = new IplImage(gray);
        IplImage newGray = cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1);
        cvThreshold(src, newGray, 60, 80, CV_THRESH_BINARY);
//        ShowImage(newGray, "searchPupil-newGray1");
//		cvDilate(newGray, newGray, null, 1);
//		ShowImage(newGray, "searchPupil-newGray2");
//		cvErode(newGray, newGray, null, 10);
        cvCanny(newGray, newGray, 50, 100, 3);
//        ShowImage(newGray, "searchPupil-newGray3");
        CvSeq results = cvHoughCircles(newGray, CvMemStorage.create(), CV_HOUGH_GRADIENT,
                1, 1000, 100, 10, 20, 300);
        BytePointer p = cvGetSeqElem(results, 0);
        cvReleaseImage(newGray);
        newGray = null;
        return new Point3f(p);
    }

    public static Mat exeGray(Mat src) {
        Mat bw = new Mat();
        cvtColor(src, bw, CV_BGR2GRAY);
        return bw;
    }
}
