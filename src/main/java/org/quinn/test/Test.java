package org.quinn.test;

/**
 * Created by QUINN_WORK_A on 2018/5/11.
 */

import org.bytedeco.javacpp.opencv_cudaimgproc;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_video.*;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_video.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;

public class Test {
    public static void main(String[] args) {
        houghCircle("d:\\1.jpg");
        Mat image=imread(FileUtils.getResPath("res/20180417142503.jpg"));	//加载图像
        cvLoadImage(FileUtils.getResPath("res/20180417142503.jpg"));
        if(image.empty())
        {
            System.out.println("图像加载错误，请检查图片路径！");
            return ;
        }
        //图像格式转换 https://blog.csdn.net/eguid_1/article/details/53218461
        imshow("原始图像",image);
        Mat gray=new Mat();
        cvtColor(image,gray,COLOR_RGB2GRAY);		//彩色图像转为灰度图像
        //imwrite("D:\\lakeResult.jpg",gray);
        imshow("灰度图像",gray);
        Mat bin=new Mat();
        threshold(gray,bin,50,255,THRESH_TOZERO); 	//图像二值化
        imshow("二值图像",bin);
        waitKey(0);
    }
    public static IplImage gray(IplImage src)
    {

//将RGB色彩空间转换成BGR色彩空间 8位 3通道
        IplImage  pImg = cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 3);

        cvConvertImage(src, pImg, 2);


        //将RGB转换成Gray度图
        IplImage  pGrayImg = cvCreateImage(
                cvGetSize(pImg),
                IPL_DEPTH_8U,
                1);
        cvCvtColor(pImg, pGrayImg, CV_RGB2GRAY);
        cvReleaseImage(pImg);
        return pGrayImg;
        //cvSaveImage("D:\\IBM\\gray.jpg",pGrayImg);
    }
    public static void houghCircle(String filename){
        CvMat src, src_gray,color_dst;
        src=cvLoadImageM(filename);//加载灰度图
        src_gray=gray(src.asIplImage()).asCvMat();
        cvSmooth(src_gray, src_gray, CV_GAUSSIAN, 3,0,0d,0d);
        CvMemStorage storage=cvCreateMemStorage(0);
        /// Apply the Hough Transform to find the circles

        CvSeq circles=cvHoughCircles( src_gray, storage, CV_HOUGH_GRADIENT, 1, src.rows()/8, 200, 100, 0, 0 );
        /// Draw the circles detected
        for( int i = 0; i < circles.total(); i++ )
        {
            FloatPointer seq=new FloatPointer(cvGetSeqElem(circles,i));
            System.out.println(seq.get(0)+","+seq.get(1)+","+seq.get(2));
            CvPoint center=new CvPoint (Math.round(seq.get(0)), Math.round(seq.get(1)));
            int radius = Math.round(seq.get(2));
            // circle center
            cvCircle( src, center, 60, CV_RGB(0,255,0), -1, 8, 0 );
            // circle outline
            cvCircle( src, center, radius, CV_RGB(255,0,0), 3, 8, 0 );
        }
        /// Show your results
        cvNamedWindow( "Hough Circle Transform Demo", CV_WINDOW_AUTOSIZE );
        cvShowImage( "Hough Circle Transform Demo", src );
        cvWaitKey(0);

    }
    private static float CheckCircle(IplImage img) {
        CvPoint center=new CvPoint();
        int width = img.width();
        int height = img.height();
        int r = width / 2;
        int value; //pixel value
        int count = 0;
        float ratio = 0;
        center.x(cvRound(width / 2));
        center.y(cvRound(height / 2));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (Math.sqrt(Math.pow((float) (center.x() - j), 2) + Math.pow((float)(center.y() - i), 2))<r)
                {
                    value = (int)cvGetReal2D(img, i, j);
                    if (value == 0)
                        count++;
                }
            }
        }
        ratio = ((float) count) / (float) (3.14 * r * r);
        return ratio;
    }
//    float FindInnerCircle(IplImage src)
//    {
//        float []result=new float[3]; //r[0] r[2] are the coordinate, r[3] is radius
//
//        float maxratio = 0;
//        float ratio = 0;
//
//        IplImage dst;
//
//
//        dst = cvCreateImage (cvGetSize(src), IPL_DEPTH_8U, 1);
//        dst = cvCloneImage (src);
//        double threshold = cvThreshold(src,dst,0,0,0); //FindThreshold(src);
//
//        cvSmooth (dst, dst, CV_GAUSSIAN, 5, 5);
//        cvSmooth(dst,dst,CV_MEDIAN,3);
//
//
//        CvMemStorage* storage = cvCreateMemStorage (0);
//        //CvSeq* circles = cvHoughCircles (dst, storage, CV_HOUGH_GRADIENT, 2, dst->width / 30, 50, 80, 10, 70);
//        //50 img04; 100 img009
//        CvSeq* circles = cvHoughCircles (dst, storage, CV_HOUGH_GRADIENT, 2, dst->width / 30, 50, 70, 20, 90);
//
//        //60 for 009; 60 img12;  100 img16,img19, img20
//        cvThreshold(dst,dst,100,255,CV_THRESH_BINARY);
//        cvNamedWindow("threshold",1);
//        cvShowImage("threshold",dst);
//
//        CvPoint center;
//        int r;
//
//        if (circles->total<0)
//        {
//            printf("No Circle Detected!!Please Check！！\n");
//            system("pause");
//        }
//
//        for (int i = 0; i < circles->total; i++)
//        //for (int i = 0; i < 35; i++)
//        {
//
//            float* p = (float*)cvGetSeqElem (circles, i);
//            CvPoint pt = cvPoint (cvRound(p[0]), cvRound(p[1]));
//            center = pt;
//            r = cvRound(p[2]);
//
//            cvSetImageROI(dst,cvRect(center.x-r,center.y-r,2*r,2*r));
//            IplImage* rect_img=NULL;
//            rect_img = cvCreateImage(cvGetSize(dst),8,1);
//            cvCopy(dst,rect_img);
//            cvResetImageROI(dst);
//
//            ratio = CheckCirle(rect_img);
//            if (ratio >= maxratio)
//            {
//                result[0] = pt.x;
//                result[1] = pt.y;
//                result[2] = r;
//                maxratio = ratio;
//            }
//            rect_img = NULL;
//        }
//
//        cvNamedWindow ("src", 1);
//        cvShowImage ("src", src);
//
//        printf("Inner circle have been found!!\n");
//        cvReleaseMemStorage (&storage);
//        return result;
//    }
}
