package com.jk.fight.www.dome;

import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateMat;
import static org.bytedeco.javacpp.opencv_core.cvFlip;
import static org.bytedeco.javacpp.opencv_core.cvNot;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_core.cvScalar;
import static org.bytedeco.javacpp.opencv_core.cvSetZero;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_core.cvSum;
import static org.bytedeco.javacpp.opencv_core.cvTranspose;
import static org.bytedeco.javacpp.opencv_imgproc.cvCircle;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangle;

import java.util.List;

import com.jk.fight.www.Utils.Point3D;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point3f;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

public class ShowImg {
    // Versions with 2, 3, and 4 parms respectively
    public static void ShowImage(IplImage image, String caption)
    {
        CvMat mat = image.asCvMat();
        int width = mat.cols(); if(width < 1) width = 1;
        int height = mat.rows(); if(height < 1) height = 1;
        double aspect = 1.0 * width / height;
        if(height < 128) { height = 128; width = (int) ( height * aspect ); }
        if(width < 128) width = 128;
        height = (int) ( width / aspect );
        ShowImage(image, caption, width, height);
    }
    public static void ShowImage(IplImage image, String caption, int size)
    {
        if(size < 128) size = 128;
        CvMat mat = image.asCvMat();
        int width = mat.cols(); if(width < 1) width = 1;
        int height = mat.rows(); if(height < 1) height = 1;
        double aspect = 1.0 * width / height;
        if(height != size) { height = size; width = (int) ( height * aspect ); }
        if(width != size) width = size;
        height = (int) ( width / aspect );
        ShowImage(image, caption, width, height);
    }
    public static void ShowImage(Mat mat)
    {
        ShowImage(new IplImage(mat), "test");
    }
    public static void ShowImage(Mat mat,String caption)
    {
        ShowImage(new IplImage(mat),caption);
    }
    public static void ShowImage(Mat mat, String caption, int size)
    {
        ShowImage(new IplImage(mat), caption, size);
    }
    public static void imshow(String txt, Mat img) {
        CanvasFrame canvasFrame = new CanvasFrame(txt);
        canvasFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        canvasFrame.setCanvasSize(img.cols(), img.rows());
        canvasFrame.showImage(new OpenCVFrameConverter.ToMat().convert(img));
    }
    public static void ShowImage(IplImage image, String caption, int width, int height)
    {
        CanvasFrame canvas = new CanvasFrame(caption, 1);   // gamma=1
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        canvas.setCanvasSize(width, height);
        OpenCVFrameConverter converter = new OpenCVFrameConverter.ToIplImage();
        canvas.showImage(converter.convert(image));
    }

    public static void Highlight(IplImage image, int [] inVec)
    {
        Highlight(image, inVec[0], inVec[1], inVec[2], inVec[3], 1);
    }
    public static void Highlight(IplImage image, int [] inVec, int Thick)
    {
        Highlight(image, inVec[0], inVec[1], inVec[2], inVec[3], Thick);
    }
    public static void Highlight(IplImage image, int xMin, int yMin, int xMax, int yMax)
    {
        Highlight(image, xMin, yMin, xMax, yMax, 1);
    }

    public static void pointLight(IplImage RawImage,int x,int y,int radius) {
    }

    public static void circleLight(IplImage RawImage,int x,int y,int radius) {
//    	System.out.println("is cvhc3 ="+x+"-"+y+"-"+radius);
        cvCircle(RawImage, cvPoint(x,y), 3, cvScalar(0,255, 0, 0),-1,8,0);
        cvCircle(RawImage, cvPoint(x,y), radius, cvScalar(255,0, 0, 0),4,8,0);
    }

    public static void printKeyPois(opencv_core.KeyPointVector keyPoints){
        for(long i=0;i<keyPoints.size();i++){
            opencv_core.KeyPoint kp=keyPoints.get(i);
            Point3f p3=new Point3f(kp);
            System.out.println(i+"\t x="+(int)p3.x()+"\t y="+(int)p3.y()+"\t radis="+p3.z());
//            opencv_core.Point2f p2 = kp.pt();
//            System.out.println((int)p2.x()+"\t"+(int)p2.y());
//            System.out.println(kp.angle()+"\t"+kp.class_id()+"\t"+kp.octave()+"\t"+kp.pt().position()+"\t"+kp.response());//-1.0	-1	0	0	0.0
        }

//        System.out.println(keyPoints);//
//        System.out.println(keyPoints.size());//66
//        System.out.println("keyPoints.empty()="+keyPoints.empty());//false
//        opencv_core.KeyPoint[] keys = keyPoints.get();
//        System.out.println("keys="+keys);//xx
//        System.out.println("keys.leng="+keys.length); //66
//        System.out.println("keys0="+keys[0]);//org.bytedeco.javacpp.opencv_core$KeyPoint[address=0x1e76ba80,position=0,limit=0,capacity=0,deallocator=null]

    }

    public static void circleLight(Mat mat,Point3D poi) {
        circleLight(new IplImage(mat), poi.getX(), poi.getY(),poi.getR());
    }

    public static void circleLight(Mat mat,List<Point3D> pois) {
        for (Point3D poi:pois) circleLight(new IplImage(mat), poi.getX(), poi.getY(),poi.getR());
    }

    public static void Highlight(IplImage image, int xMin, int yMin, int xMax, int yMax, int Thick)
    {
//    	System.out.println("is cvRectangle,xMin ="+xMin+",yMin="+yMin+",xMax="+xMax+",yMax="+yMax);
        CvPoint pt1 = cvPoint(xMin,yMin);
        CvPoint pt2 = cvPoint(xMax,yMax);
        CvScalar color = cvScalar(255,0,0,0);       // blue [green] [red]
        cvRectangle(image, pt1, pt2, color, Thick, 4, 0);
    }

    public static void PrintGrayImage(IplImage image, String caption)
    {
        int size = 512; // impractical to print anything larger
        CvMat mat = image.asCvMat();
        int cols = mat.cols(); if(cols < 1) cols = 1;
        int rows = mat.rows(); if(rows < 1) rows = 1;
        double aspect = 1.0 * cols / rows;
        if(rows > size) { rows = size; cols = (int) ( rows * aspect ); }
        if(cols > size) cols = size;
        rows = (int) ( cols / aspect );
        PrintGrayImage(image, caption, 0, cols, 0, rows);
    }
    public static void PrintGrayImage(IplImage image, String caption, int MinX, int MaxX, int MinY, int MaxY)
    {
        int size = 512; // impractical to print anything larger
        CvMat mat = image.asCvMat();
        int cols = mat.cols(); if(cols < 1) cols = 1;
        int rows = mat.rows(); if(rows < 1) rows = 1;

        if(MinX < 0) MinX = 0; if(MinX > cols) MinX = cols;
        if(MaxX < 0) MaxX = 0; if(MaxX > cols) MaxX = cols;
        if(MinY < 0) MinY = 0; if(MinY > rows) MinY = rows;
        if(MaxY < 0) MaxY = 0; if(MaxY > rows) MaxY = rows;

        System.out.println("\n" + caption);
        System.out.print("   +");
        for(int icol = MinX; icol < MaxX; icol++) System.out.print("-");
        System.out.println("+");

        for(int irow = MinY; irow < MaxY; irow++)
        {
            if(irow<10) System.out.print(" ");
            if(irow<100) System.out.print(" ");
            System.out.print(irow);
            System.out.print("|");
            for(int icol = MinX; icol < MaxX; icol++)
            {
                int val = (int) mat.get(irow,icol);
                String C = " ";
                if(val == 0) C = "*";
                System.out.print(C);
            }
            System.out.println("|");
        }
        System.out.print("   +");
        for(int icol = MinX; icol < MaxX; icol++) System.out.print("-");
        System.out.println("+");
    }

    public static void PrintImageProperties(IplImage image)
    {
        CvMat mat = image.asCvMat();
        int cols = mat.cols();
        int rows = mat.rows();
        int depth = mat.depth();
        System.out.println("ImageProperties for " + image + " : cols=" + cols + " rows=" + rows + " depth=" + depth);
    }

    public static float BinaryHistogram(IplImage image)
    {
        CvScalar Sum = cvSum(image);
        float WhitePixels = (float) ( Sum.getVal(0) / 255 );
        CvMat mat = image.asCvMat();
        float TotalPixels = mat.cols() * mat.rows();
        //float BlackPixels = TotalPixels - WhitePixels;
        return WhitePixels / TotalPixels;
    }

    // Counterclockwise small angle rotation by skewing - Does not stretch border pixels
    public static IplImage SkewGrayImage(IplImage Src, double angle)    // angle is in radians
    {
        //double radians = - Math.PI * angle / 360.0;   // Half because skew is horizontal and vertical
        double sin = - Math.sin(angle);
        double AbsSin = Math.abs(sin);

        int nChannels = Src.nChannels();
        if(nChannels != 1)
        {
            System.out.println("ERROR: SkewGrayImage: Require 1 channel: nChannels=" + nChannels);
            System.exit(1);
        }

        CvMat SrcMat = Src.asCvMat();
        int SrcCols = SrcMat.cols();
        int SrcRows = SrcMat.rows();

        double WidthSkew = AbsSin * SrcRows;
        double HeightSkew = AbsSin * SrcCols;

        int DstCols = (int) ( SrcCols + WidthSkew );
        int DstRows = (int) ( SrcRows + HeightSkew );

        CvMat DstMat = cvCreateMat(DstRows, DstCols, CV_8UC1);  // Type matches IPL_DEPTH_8U
        cvSetZero(DstMat);
        cvNot(DstMat, DstMat);

        for(int irow = 0; irow < DstRows; irow++)
        {
            int dcol = (int) ( WidthSkew * irow / SrcRows );
            for(int icol = 0; icol < DstCols; icol++)
            {
                int drow = (int) ( HeightSkew - HeightSkew * icol / SrcCols );
                int jrow = irow - drow;
                int jcol = icol - dcol;
                if(jrow < 0 || jcol < 0 || jrow >= SrcRows || jcol >= SrcCols) DstMat.put(irow, icol, 255);
                else DstMat.put(irow, icol, (int) SrcMat.get(jrow,jcol));
            }
        }

        IplImage Dst = cvCreateImage(cvSize(DstCols, DstRows), IPL_DEPTH_8U, 1);
        Dst = DstMat.asIplImage();
        return Dst;
    }

    public static IplImage TransposeImage(IplImage SrcImage)
    {
        CvMat mat = SrcImage.asCvMat();
        int cols = mat.cols();
        int rows = mat.rows();
        IplImage DstImage = cvCreateImage(cvSize(rows, cols), IPL_DEPTH_8U, 1);
        cvTranspose(SrcImage, DstImage);
        cvFlip(DstImage,DstImage,1);
        return DstImage;
    }
}
