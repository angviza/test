package org.jk.demo;

import java.io.File;
import java.time.Clock;
import java.util.List;

import org.jk.Utils.Point3D;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.indexer.UByteRawIndexer;

import org.jk.Utils.CircleUtils;
import org.quinn.test.FileUtils;
import org.quinn.test.ImgUtils;

import static org.bytedeco.javacpp.helper.opencv_imgproc.cvCreateHist;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class Demo {
    public static void main(String[] args) {
        try {
            String path = FileUtils.getResPath("iris/bk");
            //  String path= FileUtils.getResPath("res");
//		String[] names={"iris2.jpg","iris2.jpg","iris3.jpg"};
            System.out.println(path);
            //String[] names={"20180417142503.jpg","20180418145543.jpg","1.jpg","2.jpg"};
            String[] names = {"r20180418145543.jpg"};
//            String[] names={"2.jpg"};
            for (String name : names) {
                Mat source = imread(path + File.separator + name);
                ShowImg.ShowImage(source, "源图");
                checkAndShowImg(source);
//				searchSpot2(source);
//                showZFT(source);
//                test(source);
//				isInCircle(100, 100, 20, 50, 50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test(Mat source) {
        int[] hdims = {16};
        //划分直方图bins的个数，越多越精确
        float[][] hranges = {{0,255}};
        CvHistogram histv = cvCreateHist( 1,hdims, CV_HIST_ARRAY, hranges, 1 );

        IplImage src = new IplImage(CircleUtils.exeGray(source));
        cvCalcHist(src,histv);
//        System.out.println(histv.bins());
        for(int i=0;i<256;i++){
//            System.out.println(cvGetReal1D(histv.bins(),i));
        }

    }

    public static void showZFT(Mat source){
        IplImage src,redImage,greenImage,blueImage;
        src=new IplImage(source);
        redImage=cvCreateImage(cvGetSize(src),IPL_DEPTH_8U,1);
        greenImage=cvCreateImage(cvGetSize(src),IPL_DEPTH_8U,1);
        blueImage=cvCreateImage(cvGetSize(src),IPL_DEPTH_8U,1);

        cvSplit(src,blueImage,greenImage,redImage,null);
        IplImage  b_planes[] = {blueImage};
        IplImage  g_planes[] = {greenImage};
        IplImage  r_planes[] = {redImage};
/// Establish the number of bins
        int histSize = 256;
        /// Set the ranges ( for B,G,R) )
        float range[] = { 0, 255} ;
        float[] histRange[] = { range };
        int hist_size[] = {histSize};

        CvHistogram b_hist = cvCreateHist(1,hist_size,CV_HIST_ARRAY,histRange,1);
        /// Compute the histograms:

        cvCalcHist(b_planes,b_hist,0,null);

        CvHistogram g_hist = cvCreateHist(1,hist_size,CV_HIST_ARRAY,histRange,1);
        /// Compute the histograms:
        cvCalcHist(g_planes,g_hist,0,null);
        CvHistogram r_hist = cvCreateHist(1,hist_size,CV_HIST_ARRAY,histRange,1);
        /// Compute the histograms:
        cvCalcHist(r_planes,r_hist,0,null);

        // Draw the histograms for B, G and R
        int hist_w = 512; int hist_h = 400;
        int bin_w = Math.round(hist_w/histSize );
        CvMat histImage=cvCreateMat(hist_h, hist_w, CV_8UC3);
        cvSet(histImage,CV_RGB( 0,0,0),null);
        ///归一化， Normalize the result to [ 0, histImage.rows ]
        cvNormalize(b_hist.mat(), b_hist.mat(), 1, histImage.rows(), NORM_MINMAX ,null);
        cvNormalize(g_hist.mat(),g_hist.mat(), 1, histImage.rows(), NORM_MINMAX ,null);
        cvNormalize(r_hist.mat(),r_hist.mat(), 1, histImage.rows(), NORM_MINMAX ,null);
        /// Draw for each channel
        for( int i = 1; i < histSize; i++ )
        {
            System.out.println(i+"=="+(int)Math.round(cvGetReal1D( b_hist.mat(),i)));
            System.out.println(i+"=="+(int)(Math.round( cvGetReal1D( b_hist.bins(), i-1 ))));

            cvLine( histImage, cvPoint( bin_w*(i-1), hist_h -(int)(Math.round( cvGetReal1D( b_hist.bins(), i-1 ))) ) ,
                    cvPoint( bin_w*(i), hist_h - (int)Math.round(cvGetReal1D( b_hist.mat(),i)) ),
                    CV_RGB( 255, 0, 0), 2, 8, 0 );
            cvLine( histImage, cvPoint( bin_w*(i-1), hist_h - (int)Math.round(cvGetReal1D( g_hist.bins(),i-1)) ) ,
                    cvPoint( bin_w*(i), hist_h - (int)Math.round(cvGetReal1D( g_hist.bins(),i)) ),
                    CV_RGB( 0, 255, 0), 2, 8, 0 );
            cvLine( histImage, cvPoint( bin_w*(i-1), hist_h - (int)Math.round(cvGetReal1D( r_hist.bins(),i-1)) ) ,
                    cvPoint( bin_w*(i), hist_h - (int)Math.round(cvGetReal1D( r_hist.bins(),i)) ),
                    CV_RGB( 0, 0, 255), 2, 8, 0 );
        }
/// Display
        cvNamedWindow("calcHist Demo", CV_WINDOW_AUTOSIZE );
        cvShowImage("calcHist Demo", histImage );
    }

    public static Mat getCircle(Mat source, int yx, int yy, int yr, int yx2, int yy2, int yr2) {
        Mat bw = new Mat(source.rows(), source.cols(), CV_8UC3);
//        Mat bw = source.clone();
        UByteRawIndexer indexer = bw.createIndexer();
        for (int row = 0; row < source.rows(); row++) {
            for (int col = 0; col < source.cols(); col++) {
                if (!isInCircle(yx, yy, yr, yx2, yy2, yr2, col, row)) {
                    for (int i = 0; i < source.channels(); i++) {
                        indexer.put(row, col, i, 255);
                    }
                } else {
                    for (int i = 0; i < source.channels(); i++) {
                        byte ele = getMatElement(source, row, col, i);
                        indexer.put(row, col, i, ele);
                    }
                }
            }
        }
        return bw;
    }

    public static byte getMatElement(Mat img, int row, int col, int channel) {
        //获取字节指针
        BytePointer bytePointer = img.ptr(row, col);
        byte value = (byte) bytePointer.get(channel);
        return value;
    }

    public static void searchSpot(Mat source) {
        //stemp 1:转灰度
        ShowImg.ShowImage(source);
        Mat gray = CircleUtils.exeGray(source);
        threshold(gray, gray, 225, 255, THRESH_TOZERO);
//		GaussianBlur(gray, gray, new Size(5, 5), 11d);
//		erode(gray, gray,new Mat(3,3));
//		erode(gray, gray,new Mat(3,3),new Point(-1, -1),3,BORDER_CONSTANT,new Scalar(0, 0, 255, 0));
//        dilate(gray, gray, null);
        Canny(gray, gray, 50, 100);
        ShowImg.ShowImage(gray, "gb");

//		Canny(gray, gray, 40, 100);
//		ShowImage(gray,"canny");
        //stemp 1:转灰度
//		Mat gray=exeGray(source);
//		CircleUtils.searchIris(gray);

    }

    public static void searchSpot2(Mat source) {
        //stemp 1:转灰度
        ShowImg.ShowImage(source);
        Mat gray = CircleUtils.exeGray(source);
        //stemp 2:寻找反光点
        List<Point3D> pois = CircleUtils.searchHeight(gray);
//		if(!pois.isEmpty()) {
//			circleLight(source, pois);
//			ShowImage(source,"searchHeight");
//		}
        //stemp 3:针对反光点做修复

        //stemp 4：寻找斑点

        //stemp 5： 根据外圆、内圆做无效斑点过滤（以获取内圆外圆的坐标）

    }

    public static void checkAndShowImg(Mat source) {
        //stemp 1:转灰度
        Mat gray = CircleUtils.exeGray(source);
//		ShowImage(gray);
//		ShowImage(source);
        //stemp 2：寻找外圆
        Point3D poi = CircleUtils.searchIris(gray);
//		if(poi!=null) circleLight(source, poi);
//		ShowImage(source);
        //stemp 3：寻找内圆
        Point3D poi2 = CircleUtils.searchPupil(gray);
//		if(poi2!=null) circleLight(source, poi2);
//		ShowImage(source);
        //截取外圆
        Mat circle = getCircle(source, poi.getX(), poi.getY(), poi.getR(), poi2.getX(), poi2.getY(), poi2.getR());
        ShowImg.ShowImage(circle, "定位待查询的范围图");
        //
//        CircleUtils.searchSpot(circle,source);
//        Mat ret = new Mat(circle.rows(),circle.cols(),CV_8UC3);
        List<Point3D> pois = ImgUtils.findBlobs(circle);
        if (!pois.isEmpty()) ShowImg.circleLight(source, pois);
        ShowImg.ShowImage(source, "查找斑点图结果图");
    }

    public static boolean isInCircle(int yx, int yy, int yr, int yx2, int yy2, int yr2, int x, int y) {
        int x1 = Math.abs(x - yx);
        int y1 = Math.abs(y - yy);
        if (Math.sqrt(x1 * x1 + y1 * y1) > yr) return false;
        int x2 = Math.abs(x - yx2);
        int y2 = Math.abs(y - yy2);
        return Math.sqrt(x2 * x2 + y2 * y2) >= yr2;
    }

}
