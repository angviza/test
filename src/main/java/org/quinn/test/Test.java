//package org.quinn.test;
//
///**
// * Created by QUINN_WORK_A on 2018/5/11.
// */
//
//import org.bytedeco.javacpp.opencv_cudaimgproc;
//
//import static org.bytedeco.javacpp.opencv_core.*;
//import static org.bytedeco.javacpp.opencv_imgcodecs.*;
//import static org.bytedeco.javacpp.opencv_video.*;
//
//import org.bytedeco.javacpp.BytePointer;
//import org.bytedeco.javacpp.FloatPointer;
//import org.bytedeco.javacpp.IntPointer;
//import org.bytedeco.javacpp.Loader;
//import org.bytedeco.javacv.*;
//
//import static org.bytedeco.javacpp.opencv_core.*;
//import static org.bytedeco.javacpp.opencv_imgproc.*;
//import static org.bytedeco.javacpp.opencv_video.*;
//import static org.bytedeco.javacpp.opencv_highgui.*;
//import static org.bytedeco.javacpp.opencv_imgcodecs.*;
//
//public class Test {
//    public static void main(String[] args) {
//
//    }
//
//    private static float CheckCircle(IplImage img) {
//        CvPoint center;
//        int width = img.width();
//        int height = img.height();
//        int r = width / 2;
//        int value; //pixel value
//        int count = 0;
//        float ratio = 0;
//        center.x(cvRound(width / 2));
//        center.y(cvRound(height / 2));
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                if (sqrt(cvPow((float) (center.x() - j), 2) + pow(float(center.y - i), 2))<r)
//                {
//                    value = (int)cvGetReal2D(img, i, j);
//                    if (value == 0)
//                        count++;
//                }
//            }
//        }
//        ratio = ((float) count) / (float) (3.14 * r * r);
//        return ratio;
//    }
//}
