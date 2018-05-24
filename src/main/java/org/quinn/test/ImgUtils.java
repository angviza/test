package org.quinn.test;

import com.jk.fight.www.Utils.Point3D;
import com.jk.fight.www.dome.ShowImg;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_features2d;
import org.bytedeco.javacv.Blobs;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class ImgUtils {
    public static BufferedImage matToBufferedImage(Mat frame) {
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);
        return image;
    }

    public static org.bytedeco.javacpp.opencv_core.Mat bufferedImageToMat(BufferedImage bi) {
        OpenCVFrameConverter.ToMat cv = new OpenCVFrameConverter.ToMat();
        return cv.convertToMat(new Java2DFrameConverter().convert(bi));
    }

    /**
     * 图像边缘检测
     * 第一个参数，InputArray类型的image，输入图像，即源图像，填Mat类的对象即可，且需为单通道8位图像。
     * 第二个参数，OutputArray类型的edges，输出的边缘图，需要和源图片有一样的尺寸和类型。
     * 第三个参数，double类型的threshold1，第一个滞后性阈值。
     * 第四个参数，double类型的threshold2，第二个滞后性阈值。
     * 第五个参数，int类型的apertureSize，表示应用Sobel算子的孔径大小，其有默认值3。
     * 第六个参数，bool类型的L2gradient，一个计算图像梯度幅值的标识，有默认值false。
     * <p>
     * 需要注意的是，这个函数阈值1和阈值2两者的小者用于边缘连接，而大者用来控制强边缘的初始段，推荐的高低阈值比在2:1到3:1之间。
     */
    public static void canny(@ByVal opencv_core.Mat image, @ByVal opencv_core.Mat edges,
                             double threshold1, double threshold2,
                             int apertureSize/*=3*/, @Cast("bool") boolean L2gradient/*=false*/) {
        Canny(image, edges, threshold1, threshold2, apertureSize, L2gradient);

    }

    //找斑点
    public static List<Point3D> findBlobs(opencv_core.Mat src) {
        // 初始化BLOB参数
        opencv_features2d.SimpleBlobDetector.Params params = new opencv_features2d.SimpleBlobDetector.Params();
        params.thresholdStep(5);    //二值化的阈值步长，即公式1的t
        params.minThreshold(40);   //二值化的起始阈值，即公式1的T1
        params.maxThreshold(230);    //二值化的终止阈值，即公式1的T2
//        //重复的最小次数，只有属于灰度图像斑点的那些二值图像斑点数量大于该值时，该灰度图像斑点才被认为是特征点
        params.minRepeatability(4);
//        //最小的斑点距离，不同二值图像的斑点间距离小于该值时，被认为是同一个位置的斑点，否则是不同位置上的斑点
        params.minDistBetweenBlobs(2.0f);
        params.filterByInertia(false);//斑点惯性率的限制变量
//        params.minInertiaRatio(.6f);    //斑点的最小惯性率 ,默认 0.6
//        params.maxInertiaRatio(2000.0f);    //斑点的最大惯性率
        params.filterByColor(true);//斑点颜色的限制变量
//        params.blobColor((byte)0);    //表示只提取黑色斑点；如果该变量为255，表示只提取白色斑点
        params.filterByArea(true);// 声明根据面积过滤，)设置最大与最小面积
        params.minArea(10.0f);////斑点的最小面积
        params.maxArea(3000f);//斑点的最大面积
        params.filterByCircularity(true);// 声明根据圆度过滤，)设置最大与最小圆度..斑点圆度的限制变量，默认是不限制
        params.minCircularity(0.2f);//斑点的最小圆度
        params.maxCircularity(2000f);//斑点的最大圆度，所能表示的float类型的最大值
//        params.filterByConvexity(false);// 凸包形状分析 - 过滤凹包
        params.filterByConvexity(false);//斑点凸度的限制变量
//        params.minConvexity(.5f);//斑点的最小凸度
//        params.maxConvexity(200f); //斑点的最大凸度
        opencv_features2d.SimpleBlobDetector detector = opencv_features2d.SimpleBlobDetector.create(params);
        KeyPointVector keyPoints = new KeyPointVector();
        detector.detect(src, keyPoints, new opencv_core.Mat());
        ShowImg.printKeyPois(keyPoints);
        List<Point3D> pois= new ArrayList<>();
        Point3D poi;
        for(long i=0;i<keyPoints.size();i++){
            opencv_core.KeyPoint kp=keyPoints.get(i);
            Point3f p3=new Point3f(kp);
            poi=new Point3D((int)Math.rint(p3.x()),(int)Math.rint(p3.y()),(int)Math.rint(p3.z()));
            pois.add(poi);
        }
//        opencv_features2d.drawKeypoints(src, keyPoints, kpImage, new Scalar(255, 0, 0, 0), opencv_features2d.DrawMatchesFlags.DRAW_RICH_KEYPOINTS);
        return pois;
    }


    /**
     * 平滑函数
     * (2)smooth支持的类型：
     * CV_BLUR	简单模糊 对每个像素param1,param2求和,并缩放1/(param1*param2),亦即求简单平均值
     * CV_BLUR_NO_SCALE 简单无缩放变化的模糊 对每个像素param1,param2求和 特别说明的是输入图像和结果图像必须有不同的数值精度，以保证不会发生
     * 溢出，如果源图像是8u，则结果图像必须是16s或者32s
     * CV_MEDIAN 中值模糊 取中心像素的正方形领域类的每个像素的值用中间值代替
     * CV_GAUSSIAN 高斯模糊 param3为零时，高斯卷积核sigma通过以下公式计算
     * sigma(x) = (n(x)/2-1)*0.30+0.80，n(x)= param1
     * sigma(y) = (n(y)/2-1)*0.30+0.80，n(y)= param2
     * 如果第四个参数指定，则第三个和第四个参数分别表示sigma的水平方向和垂直方向的值
     * 如果第三个，第四个参数已经指定，而前两个参数为0，那么窗口的尺寸由sigma确定
     * 速度较慢但最有效
     * CV_BILATERAL 双向滤波 因为高斯模糊是在图像在空间内的像素是缓慢变化的，但随机的两个点可能形成很大的
     * 像素差，高斯滤波在保留信号的条件下减少噪声，但在接近边缘的地方无效，双向滤波可以解决这个问题，但需要更多的时间代价，
     * 其需要两个参数，param1表示空域中所使用的高斯核的宽度，param2表示颜色域高斯核的高度
     */

    public static void Smooth(@Const CvArr src, CvArr dst,
                              int smoothtype/*=CV_GAUSSIAN*/,
                              int size1/*=3*/,
                              int size2/*=0*/,
                              double sigma1/*=0*/,
                              double sigma2/*=0*/) {
        // cvSmooth(new opencv_core.IplImage(src),new opencv_core.IplImage(dst),CV_MEDIAN,9,9,1d,1d);
        cvSmooth(src, dst, smoothtype, size1, size2, sigma1, sigma2);
    }

    /**
     * opencv Mat 转 javacv Mat
     *
     * @param mat
     * @return
     */
    public static org.bytedeco.javacpp.opencv_core.Mat convertMat(Mat mat) {
        return bufferedImageToMat(matToBufferedImage(mat));
    }

    static OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

    /**
     * javaCV图像处理之Frame、Mat和IplImage三者相互转换(使用openCV进行Mat和IplImage转换)
     *
     * @param frame
     */
    public static void converter(Frame frame) {

        // 将Frame转为Mat
        opencv_core.Mat mat = converter.convertToMat(frame);

        // 将Mat转为Frame
        Frame convertFrame1 = converter.convert(mat);

        // 将Frame转为IplImage
        opencv_core.IplImage image1 = converter.convertToIplImage(frame);
        opencv_core.IplImage image2 = converter.convert(frame);

        // 将IplImage转为Frame
        Frame convertFrame2 = converter.convert(image1);

        //Mat转IplImage
        opencv_core.IplImage matImage = new opencv_core.IplImage(mat);

        //IplImage转Mat
        opencv_core.Mat mat2 = new opencv_core.Mat(matImage);

    }

    public static opencv_core.IplImage convertImageToGreyscale(opencv_core.IplImage imageSrc) {
        opencv_core.IplImage imageGrey;
        // Either convert the image to greyscale, or make a copy of the existing greyscale image.
        // This is to make sure that the user can always call cvReleaseImage() on the output, whether it was greyscale or not.
        if (imageSrc.nChannels() == 3) {
            imageGrey = cvCreateImage(cvGetSize(imageSrc), IPL_DEPTH_8U, 1);
            cvCvtColor(imageSrc, imageGrey, CV_BGR2GRAY);
        } else {
            imageGrey = cvCloneImage(imageSrc);
        }
        return imageGrey;
    }

}

