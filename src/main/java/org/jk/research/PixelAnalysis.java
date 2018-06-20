package org.jk.research;

import org.bytedeco.javacpp.opencv_core;
import org.jk.Utils.OpenCVUtilsJava;
import org.jk.research.chapter04.Ex2ComputeHistogramGraphJava;
import org.quinn.test.FileUtils;

import java.io.File;

import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class PixelAnalysis {
    public static OpenCVUtilsJava CV=new OpenCVUtilsJava();
    public static void main(String[] args){
        String path = FileUtils.getResPath("iris/bk");
        //String[] names={"20180417142503.jpg","20180418145543.jpg","1.jpg","2.jpg"};
        String names = "r20180418145543.jpg";
//        String names = "test.jpg";
        File source = new File(path + File.separator + names);
        test(source);
    }
    public static void test(File src){
//        opencv_core.Mat srcMat = CV.loadAndShowOrExit(src);
        opencv_core.Mat srcMat = CV.loadAndShowOrExit(src, IMREAD_GRAYSCALE);
        Ex2ComputeHistogramGraphJava.findZFT(srcMat);
        int[] HistGram=Ex2ComputeHistogramGraphJava.getZFT(srcMat);
//        int thresh = GetMeanThreshold(HistGram);
        int thresh = GetPTileThreshold(HistGram,50);
        System.out.println("thresh="+thresh);
        opencv_core.Mat newGray =new opencv_core.Mat();
        threshold(srcMat, newGray, thresh, 255, CV_THRESH_BINARY | CV_THRESH_OTSU);
        OpenCVUtilsJava.show(newGray,"newGray");
//        Ex2ComputeHistogramGraphJava.findZFT(newGray);
    }

    //灰度平局值值法：
    public static int GetMeanThreshold(int[] HistGram)
    {
        int Sum = 0, Amount = 0;
        for (int Y = 0; Y < 256; Y++)
        {
            Amount += HistGram[Y];
            Sum += Y * HistGram[Y];
        }
        return Sum / Amount;
    }

    /// <summary>
    /// 百分比阈值
    /// </summary>
    /// <param name="HistGram">灰度图像的直方图</param>
    /// <param name="Tile">背景在图像中所占的面积百分比</param>
    /// <returns></returns>
    public static int GetPTileThreshold(int[] HistGram, int Tile)
    {
        if(Tile<=0) Tile=50;
        int Y, Amount = 0, Sum = 0;
        for (Y = 0; Y < 256; Y++) Amount += HistGram[Y];        //  像素总数
        System.out.println("像素总数Amount="+Amount);
        for (Y = 0; Y < 256; Y++)
        {
            Sum = Sum + HistGram[Y];
            if (Sum >= Amount * Tile / 100) return Y;
        }
        return -1;
    }
}
