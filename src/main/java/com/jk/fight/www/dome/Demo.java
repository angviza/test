package com.jk.fight.www.dome;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.Blobs;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import com.jk.fight.www.Utils.CircleUtils;
import org.quinn.test.FileUtils;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static com.jk.fight.www.dome.ShowImg.*;

public class Demo {
    public static void main(String[] args) {
        try {
//		String[] names={"iris2.jpg","iris2.jpg","iris3.jpg"};
            String[] names = {"20180417142503.jpg", "20180418145543.jpg", "1.jpg", "2.jpg"};
            for (String name : names) {
                Mat source = imread(FileUtils.getResPath("res/"+name));
                if (source == null || source.empty() || source.data() == null) {
                    System.out.println(name + " not found");
                    continue;

                }
                checkAndShowImg(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkAndShowImg(Mat source) {
        //stemp 1:转灰度
        Mat gray = CircleUtils.exeGray(source);
//		ShowImage(gray);
//		ShowImage(source);
        //stemp 2：寻找外圆
        Point3f poi = CircleUtils.searchIris(gray);
        if (poi != null) circleLight(source, poi);
//		ShowImage(source);
        //stemp 3：寻找内圆
        Point3f poi2 = CircleUtils.searchPupil(gray);
        if (poi2 != null) circleLight(source, poi2);
        ShowImage(source);
    }


}
