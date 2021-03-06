package org.quinn.test;/*
 * JavaCV version of OpenCV caffe_googlenet.cpp
 * https://github.com/ludv1x/opencv_contrib/blob/master/modules/dnn/samples/caffe_googlenet.cpp
 *
 * Paolo Bolettieri <paolo.bolettieri@gmail.com>
 */

import static org.bytedeco.javacpp.opencv_core.minMaxLoc;
import static org.bytedeco.javacpp.opencv_dnn.blobFromImage;
import static org.bytedeco.javacpp.opencv_dnn.readNetFromCaffe;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_dnn.Net;

public class CaffeGooglenet {

    /* Find best class for the blob (i. e. class with maximal probability) */
    public static void getMaxClass(Mat probBlob, Point classId, double[] classProb) {
        Mat probMat = probBlob.reshape(1, 1); //reshape the blob to 1x1000 matrix
        minMaxLoc(probMat, null, classProb, null, classId, null);
    }

    public static List<String> readClassNames() {
        String filename = "D:\\Downloads\\synset_words.txt";
        List<String> classNames = null;

        try (BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {
            classNames = new ArrayList<String>();
            String name = null;
            while ((name = br.readLine()) != null) {
                classNames.add(name.substring(name.indexOf(' ')+1));
            }
        } catch (IOException ex) {
            System.err.println("File with classes labels not found " + filename);
            System.exit(-1);
        }
        return classNames;
    }

    public static void main(String[] args) throws Exception {
        String modelTxt = "E:\\WORKSPACES\\TEST\\iris\\src\\main\\resources\\bvlc_googlenet.prototxt";
        String modelBin = "D:\\Downloads\\bvlc_googlenet.caffemodel";
        String imageFile = (args.length > 0) ? args[0] : "E:\\WORKSPACES\\TEST\\iris\\src\\main\\resources\\BlackBalls.jpg";

        //! [Initialize network]
        Net net = null;
        try {                                 //Try to import Caffe GoogleNet model
            net = readNetFromCaffe(modelTxt, modelBin);
        } catch (Exception e) {               //Importer can throw errors, we will catch them
            e.printStackTrace();
        }

        if (net == null || net.empty()) {
            System.err.println("Can't load network by using the following files: ");
            System.err.println("prototxt:   " + modelTxt);
            System.err.println("caffemodel: " + modelBin);
            System.err.println("bvlc_googlenet.caffemodel can be downloaded here:");
            System.err.println("http://dl.caffe.berkeleyvision.org/bvlc_googlenet.caffemodel");
            System.exit(-1);
        }
        //! [Initialize network]

        //! [Prepare blob]
        Mat img = imread(imageFile);

        if (img.empty()) {
            System.err.println("Can't read image from the file: " + imageFile);
            System.exit(-1);
        }

        resize(img, img, new Size(224, 224)); //GoogLeNet accepts only 224x224 RGB-images
        Mat inputBlob = blobFromImage(img);   //Convert Mat to 4-dimensional dnn::Blob from image
        //! [Prepare blob]

        //! [Set input blob]
        net.setInput(inputBlob, "data");      //set the network input
        //! [Set input blob]

        //! [Make forward pass]
        Mat prob = net.forward("prob");       //compute output
        //! [Make forward pass]

        //! [Gather output]
        Point classId = new Point();
        double[] classProb = new double[1];
        getMaxClass(prob, classId, classProb);//find the best class
        //! [Gather output]

        //! [Print results]
        List<String> classNames = readClassNames();

        System.out.println("Best class: #" + classId.x() + " '" + classNames.get(classId.x()) + "'");
        System.out.println("Best class: #" + classId.x());
        System.out.println("Probability: " + classProb[0] * 100 + "%");
        //! [Print results]
    } //main
}
