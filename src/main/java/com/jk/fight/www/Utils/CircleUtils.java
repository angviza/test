package com.jk.fight.www.Utils;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static com.jk.fight.www.dome.ShowImg.*;

import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point3f;
import org.bytedeco.javacv.Blobs;

public class CircleUtils {

    /**
     * 传入参数必须为单通道灰度图，查找反光点（圆）的的坐标并返回
     * @param source
     */
    public static List<Point3f> searchHeight(Mat graySrc){
        Mat gray=graySrc.clone();
        List<Point3f> pois= new ArrayList<>();
        threshold(gray, gray, 205, 255, THRESH_TOZERO);
//		Canny(gray, gray, 255,255);
        bitwise_not(gray, gray);
        ShowImage(gray);
        Blobs Regions = new Blobs();
        Regions.BlobAnalysis(
                new IplImage(gray),               // image
                -1, -1,                     // ROI start col, row
                -1, -1,                    // ROI cols, rows
                0,                          // border (0 = black; 1 = white)
                2);                   // minarea
        Regions.PrintRegionData();
        IplImage src= new IplImage(graySrc);
        for(int i = 1; i <= Blobs.MaxLabel; i++)
        {
            double [] Region = Blobs.RegionData[i];
            int Parent = (int) Region[Blobs.BLOBPARENT];
            int Color = (int) Region[Blobs.BLOBCOLOR];
            int MinX = (int) Region[Blobs.BLOBMINX];
            int MaxX = (int) Region[Blobs.BLOBMAXX];
            int MinY = (int) Region[Blobs.BLOBMINY];
            int MaxY = (int) Region[Blobs.BLOBMAXY];
            Highlight(src,  MinX, MinY, MaxX, MaxY, 5);
//            int radius=MaxX-MinX<=MaxY-MinY?(MaxY-MinY)/2:(MaxX-MinX)/2;
//            int x=MinX+(MaxX-MinX)/2;
//            int y=MinY+(MaxY-MinY)/2;
//            boolean flag=checkPosition(x, y, radius,mat.cols(), mat.rows());
//            System.out.println("point["+i+"]:flag["+flag+"]:x["+x+"],y["+y+"],radius["+radius+"]");
//            if(!flag) continue;
//            Highlight(new IplImage(graySrc),  MinX, MinY, MaxX, MaxY, 5);
        }

        ShowImage(src, "src--blobs");
//		Mat ret=new Mat(gray.rows(),gray.cols(),CV_8UC3);
//		HoughCircles(gray,ret , CV_HOUGH_GRADIENT,
//        		1,50,50,100,1,20);
//		for(int row=0;row<ret.rows();row++) {
//    		for(int col=0;col<ret.cols();col++) {
//    			pois.add(new Point3f(ret.ptr(row,col)));
//    		}
//    	}
        circleLight(graySrc, pois);
        ShowImage(graySrc,"searchHeight");
        return pois;
    }

    public static Point3f searchSpotBackup(Mat src,Mat srcRealy){
        Mat gray=src.clone();
        cvtColor(gray, gray, CV_BGR2GRAY);
        threshold(gray, gray,90,255, CV_THRESH_TOZERO);
//        ShowImage(SkewGrayImage(GrayImage, 1), "SkewGrayImage-GrayImage", 512);

//        ShowImage(gray);
//        erode(gray, gray,new Mat(5,5),new Point(-1,-1),1,BORDER_CONSTANT,morphologyDefaultBorderValue());
        dilate(gray, gray,new Mat(),new Point(-1,-1),3,BORDER_CONSTANT,morphologyDefaultBorderValue());
        GaussianBlur(src,src,new Size(5,5),10d);
        Canny(gray, gray, 80, 150);
        ShowImage(gray);
//        ShowImage(WorkingImage, "WorkingImage", 512);
//        if(1==1) return null;
        IplImage src2= new IplImage(gray);
        IplImage RawImage= new IplImage(srcRealy);
        CvSeq results =cvHoughCircles(src2, CvMemStorage.create(), CV_HOUGH_GRADIENT,
                1,100,200,10,20,50);
        for(int i=0;i<results.total();i++) {
            BytePointer p =cvGetSeqElem(results,i);
            Point3f poi =new Point3f(p);
            System.out.println(i+"=poi=x="+poi.get(0)+",y="+poi.get(1)+",radius="+poi.get(2));
            circleLight(RawImage, (int)poi.get(0), (int)poi.get(1),(int)poi.get(2));
        }
        ShowImage(RawImage, "jieguo");
        return null;
    }

    public static Point3f searchSpot(Mat src,Mat srcRealy){
        ShowImage(src);
        Mat gray=src.clone();
        cvtColor(gray, gray, CV_BGR2GRAY);
        threshold(gray, gray,100,255, CV_THRESH_BINARY);
//        ShowImage(SkewGrayImage(GrayImage, 1), "SkewGrayImage-GrayImage", 512);

//        ShowImage(gray);
//        erode(gray, gray,new Mat(),new Point(-1,-1),3,BORDER_CONSTANT,morphologyDefaultBorderValue());
//        dilate(gray, gray,new Mat(),new Point(-1,-1),3,BORDER_CONSTANT,morphologyDefaultBorderValue());
//        GaussianBlur(src,src,new Size(3,3),2d);
//        Canny(gray, gray, 45, 90);
        ShowImage(gray);
//        ShowImage(WorkingImage, "WorkingImage", 512);
//        if(1==1) return null;
        Blobs Regions = new Blobs();
        Regions.BlobAnalysis(
                new IplImage(gray),               // image
                -1, -1,                     // ROI start col, row
                -1, -1,                    // ROI cols, rows
                0,                          // border (0 = black; 1 = white)
                10);                   // minarea
        Regions.PrintRegionData();
        IplImage src2= new IplImage(srcRealy);
        System.out.println("Blobs.MaxLabel="+Blobs.MaxLabel);
        for(int i = 1; i <= Blobs.MaxLabel; i++)
        {
            double [] Region = Blobs.RegionData[i];
            int Parent = (int) Region[Blobs.BLOBPARENT];
            int Color = (int) Region[Blobs.BLOBCOLOR];
            int MinX = (int) Region[Blobs.BLOBMINX];
            int MaxX = (int) Region[Blobs.BLOBMAXX];
            int MinY = (int) Region[Blobs.BLOBMINY];
            int MaxY = (int) Region[Blobs.BLOBMAXY];
            Highlight(src2,  MinX, MinY, MaxX, MaxY, 5);
        }
        ShowImage(src2, "jieguo");
        return null;
    }
    /**
     * 传入参数必须为单通道灰度图，查找虹膜（外圆）的的坐标并返回
     * @param grayImage
     * @return
     */
    public static Point3f searchIris(Mat gray){
        IplImage src = new IplImage(gray);
        IplImage newGray = cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1);
        cvThreshold(src, newGray, 50, 255, CV_THRESH_BINARY | CV_THRESH_OTSU);
//        cvErode(newGray, newGray, null, 3);
        cvCanny(newGray, newGray, 50,100, 3);
        ShowImage(newGray, "searchIris-newGray");
        CvSeq results =cvHoughCircles(newGray, CvMemStorage.create(), CV_HOUGH_GRADIENT,
                1,1000,100,10,20,300);
        BytePointer p =cvGetSeqElem(results,0);
        cvReleaseImage(newGray);
        newGray=null;
        return new Point3f(p);
    }
    public static Point3f searchPupil(Mat gray){
        IplImage src = new IplImage(gray);
        IplImage newGray = cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1);
        cvThreshold(src, newGray, 60, 80, CV_THRESH_BINARY);
//        ShowImage(newGray, "searchPupil-newGray1");
//		cvDilate(newGray, newGray, null, 1);
//		ShowImage(newGray, "searchPupil-newGray2");
//		cvErode(newGray, newGray, null, 10);
        cvCanny(newGray, newGray, 50,100, 3);
//        ShowImage(newGray, "searchPupil-newGray3");
        CvSeq results =cvHoughCircles(newGray, CvMemStorage.create(), CV_HOUGH_GRADIENT,
                1,1000,100,10,20,300);
        BytePointer p =cvGetSeqElem(results,0);
        cvReleaseImage(newGray);
        newGray=null;
        return new Point3f(p);
    }

    public static Mat exeGray(Mat src) {
        Mat bw = new Mat();
        cvtColor(src, bw, CV_BGR2GRAY);
        return bw;
    }


}
