package org.quinn.test;

import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_features2d;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import static org.bytedeco.javacpp.RealSense.color;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.eclipse.swt.SWT.Close;
import static org.opencv.imgcodecs.Imgcodecs.imread;

public class GoConvertJava {
    public static void main(String[] args) {

        Mat grayMat = ImgUtils.convertMat(imread(FileUtils.getResPath("res/1.jpg"), 0));

        medianBlur(grayMat, grayMat, 5);

        Mat bgrMat = new Mat();

        cvtColor(grayMat, bgrMat, CV_GRAY2BGR);

        Mat circles = new Mat();

        // 找外圆
        cvHoughCircles(
                new IplImage(grayMat), circles, CV_HOUGH_GRADIENT,
                1,
                (float) grayMat.rows(),
                75,
                20,
                grayMat.rows() / 8,
                grayMat.rows() * 2 / 3);
        if (circles.cols() != 1) {
            System.err.println("out circle error");
        }

//        Scalar blue = new opencv_core.Scalar(0, 0, 255, 0);
//        Scalar red = new opencv_core.Scalar(255, 0, 0, 0);
//        // 外圆参数
//        byte[] outV = new byte[circles.data().sizeof()];
//        circles.data().get(outV, 0, 0);// circles.get.getUMat(0, 0);
//        circles.data().getInt();
//        int outX = int(outV[0]);
//        int outY = int(outV[1])
//        int outR = int(outV[2])
//        // 画圈圈标记圆
//        circle(bgrMat, image.pt(outX, outY), outR, blue, 2);
//        gocv.Circle( & bgrMat, image.Pt(outX, outY), 2, red, 3)
//
//        // 缩小参数找内圆
//        gocv.HoughCirclesWithParams(
//                grayMat,
//                & circles,
//                gocv.HoughGradient,
//                1,
//                float64(grayMat.Rows()),
//                75,
//                20,
//                grayMat.Rows() / 16,
//                outR / 2,
//	)
//        if circles.Cols() != 1 {
//            panic("内圆查找异常")
//        }
//        inV:=circles.GetVecfAt(0, 0)
//        inX:= int(inV[0])
//        inY:= int(inV[1])
//        inR:= int(inV[2])
//        gocv.Circle( & bgrMat, image.Pt(inX, inY), inR, blue, 2)
//        gocv.Circle( & bgrMat, image.Pt(inX, inY), 2, red, 3)
//        go IMShow (bgrMat, "内外圆")
//
//        // 内外圆像素差集拷贝
//        // 3通道BGR原图
//        oriMat:=gocv.IMRead("doublecircle.jpg", gocv.IMReadColor)
//        go IMShow (oriMat, "原图")
//        // 新建空Mat,避免内存共用
//        region:=gocv.NewMatWithSize(oriMat.Rows(), oriMat.Cols(), oriMat.Type())
//        fmt.Println(oriMat.Type(), oriMat.Channels(), oriMat.Rows(), oriMat.Cols())
//        chs:=oriMat.Channels()
//        fmt.Println(outR, outX, outY)
//        for r:=0;
//        r<oriMat.Rows (); r++ {
//            for c:=0;
//            c<oriMat.Cols (); c++ {
//                // 判断像素坐标是否在差集内
//                if inCirle(outR, outX, outY, c, r) && !inCirle(inR, inX, inY, c, r) {
//                    for ch:=0;
//                    ch<chs ;
//                    ch++ {
//                        if oriMat.Type() & gocv.MatTypeCV8U == gocv.MatTypeCV8U {
//                            region.SetUCharAt(r, c * chs + ch, oriMat.GetUCharAt(r, c * chs + ch))
//                        } else{
//                            //TODO
//                            panic("不支持的类型")
//                        }
//                    }
//                }
//            }
//        }
//        //  IMShow(region, "裁剪图")
    }

}

