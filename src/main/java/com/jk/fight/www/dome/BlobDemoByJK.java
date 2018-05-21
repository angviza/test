package com.jk.fight.www.dome;

import org.bytedeco.javacv.Blobs;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
//import org.opencv.core.Mat;
//import org.opencv.imgcodecs.Imgcodecs;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.javacpp.opencv_core.Mat;

//import org.bytedeco.javacpp.BytePointer;
//import org.bytedeco.javacpp.opencv_core.CvSeq;


///////////////////////////////////////////////////////////////////
//*                                                             *//
//* As the author of this code, I place all of this code into   *//
//* the public domain. Users can use it for any legal purpose.  *//
//*                                                             *//
//*             - Dave Grossman                                 *//
//*                                                             *//
///////////////////////////////////////////////////////////////////
public class BlobDemoByJK
{
    public static void main(String[] args)
    {
//        System.out.println("STARTING...\n");
//        demo();
//        System.out.println("ALL DONE");
    	james();
//    	testFContours();

    }

	/**
     * Title: 测试 cvHoughCircles 、 HoughCircles 找圆，失败。总结：返回参数异常，怀疑java对接c++出现数据异常。
     * Description: 
     * @author james mo
     * @date 2018年5月17日
     */
    public static void james() {
//    	IplImage RawImage = cvLoadImage("F:\\Users\\james\\eclipseSpacse\\iris\\src\\main\\java\\org\\quinn\\test\\hongmo.jpg"); 
//    	IplImage RawImage = cvLoadImage("F:\\Users\\james\\eclipseSpacse\\iris\\src\\main\\java\\org\\quinn\\test\\hongmo2.jpg"); 
    	IplImage RawImage = cvLoadImage("D:\\SoftwareData\\jamesMot\\myProject\\test\\src\\main\\resources\\res\\2.jpg");
//    	IplImage RawImage = cvLoadImage("F:\\Users\\james\\eclipseSpacse\\iris\\src\\main\\java\\org\\quinn\\test\\yuan.jpg");
//    	IplImage RawImage = cvLoadImage("F:\\Users\\james\\eclipseSpacse\\iris\\src\\main\\java\\org\\quinn\\test\\yingbi.jpg");
//    	IplImage RawImage = cvLoadImage("F:\\Users\\james\\eclipseSpacse\\iris\\src\\main\\java\\org\\quinn\\test\\y1.png");
//    	ShowImage(RawImage, "RawImage", 512);
    	Mat mat;
    	ShowImage(TransposeImage(RawImage), "test");
//    	IplImage newdd = cvCreateImage(cvGetSize(RawImage), IPL_DEPTH_8U, 1);
////    	ShowImage(RawImage, "GaussianBlur-RawImage", 512);
//    	cvSmooth( RawImage, newdd, CV_GAUSSIAN, 5, 5 ,0,0);  //降噪
//    	ShowImage(newdd, "newdd", 512);
    	
    	
//    	mat = new Mat(RawImage);
//        printCvMat(mat);
//        Mat circles = new Mat(); 
//    	GaussianBlur( mat, circles, new Size(9, 9), 1); 
//    	RawImage = new IplImage(circles);
//    	ShowImage(RawImage, "GaussianBlur-RawImage", 512);
    	
//    	int MinArea = 100;
        int ErodeCount =3;
        int DilateCount = 3;
        IplImage GrayImage = cvCreateImage(cvGetSize(RawImage), IPL_DEPTH_8U, 1);
        cvCvtColor(RawImage, GrayImage, CV_BGR2GRAY);
        ShowImage(GrayImage, "GrayImage1", 512);
    	
//    	int huozhe1=50;
//    	int huozhi2=100;
        
    	//寻找内园只需要膨胀 灰度图片即可，膨胀次数根据原图的的灰度MAT数据而定。demo图片数据为3次即可。
        //霍夫算法参数：1,1000,100,50,50,250);//虹膜的参数 内圆
//        cvThreshold(GrayImage, GrayImage, 40, 255, CV_THRESH_BINARY | CV_THRESH_OTSU);
//        cvDilate(GrayImage, GrayImage, null, 3);
        
        //查找外圆，cvHoughCircles(GrayImage, CvMemStorage.create(), CV_HOUGH_GRADIENT,1,1000,100,10,20,300);
        //cvHoughCircles(GrayImage, CvMemStorage.create(), CV_HOUGH_GRADIENT,1,1000,100,10,20,300);
        cvThreshold(GrayImage, GrayImage, 40, 255, CV_THRESH_BINARY | CV_THRESH_OTSU);
        cvErode(GrayImage, GrayImage, null, 3);
        cvCanny(GrayImage, GrayImage, 50,100, 3);
      
       
//        cvThreshold(GrayImage, GrayImage, 40, 255, CV_THRESH_BINARY | CV_THRESH_OTSU);
////        cvDilate(GrayImage, GrayImage, null, 3);
//        cvErode(GrayImage, GrayImage, null, 33);
//        cvSmooth( GrayImage, GrayImage, CV_GAUSSIAN,25, 25 ,0,0);
        
//        cvErode(GrayImage, GrayImage, null, 1);
//        checkMat(GrayImage.asCvMat());
        
//        cvThreshold(GrayImage, GrayImage, 70, 70, CV_THRESH_BINARY);
//        cvThreshold(GrayImage, GrayImage, 40, 255, CV_THRESH_BINARY | CV_THRESH_OTSU);
//        cvErode(GrayImage, GrayImage, null, 10);    
//        cvDilate(GrayImage, GrayImage, null, 3);
//        cvCanny(GrayImage, GrayImage, 20,100, 3);
        ShowImage(GrayImage, "GrayImage2", 512);
        
//        Mat kernel = Mat.ones(3, 3, CV_32F).asMat();
//        FloatIndexer kernelIndexer = kernel.createIndexer();
//        kernelIndexer.put(1, 1, -8); 
//        
//        Mat src =new Mat(RawImage);
//        Mat imgLaplacian = new Mat();
//        Mat sharp = src; 
//        filter2D(sharp, imgLaplacian, CV_32F, kernel);
//        src.convertTo(sharp, CV_32F);
//        Mat imgResult = subtract(sharp, imgLaplacian).asMat();
//        imgResult.convertTo(imgResult, CV_8UC3);
//        imgLaplacian.convertTo(imgLaplacian, CV_8UC3);
//        
//    	ShowImage(new IplImage(imgResult), "new-imgResult", 512);
    	
//        
////        IplImage WorkingImage =GrayImage;
//        ShowImage(WorkingImage, "WorkingImage", 512);
//        mat = new Mat(RawImage);
//        printCvMat(mat);
        
//        Mat circles = new Mat(3,3);
//        HoughCircles(mat, circles, CV_HOUGH_GRADIENT, 5,1,100,100,900,4000);
//        printCvMat(circles);
//        HoughCircles( RawImage, CvMemStorage.create(), CV_HOUGH_GRADIENT, 1, RawImage.asCvMat().rows()/20, 100, 60, 0, 0 );
        
//        Mat circles = new Mat(); 
//        HoughCircles(mat, circles, CV_HOUGH_GRADIENT, 1, 30, huozhe1, huozhi2, 70, 1000);
//        printCvMat(circles);
        
        CvSeq results =cvHoughCircles(GrayImage, CvMemStorage.create(), CV_HOUGH_GRADIENT,
//        		1,1000,10,1,20,250);
        
//        		1,1000,150,20,20,500);//虹膜2的参数 外圆
//        		1,1000,100,10,20,500);//虹膜的参数 外圆
        		1,1000,100,10,20,300);//虹膜的参数 内圆  0=poi=x=450.5,y=282.5,radius=98.7
//        CvSeq results =cvHoughCircles(GrayImage, CvMemStorage.create(), CV_HOUGH_GRADIENT, 
//        		1,30,200,100,0,0);//普通园的参数
//        CvSeq results =cvHoughCircles(GrayImage, CvMemStorage.create(), CV_HOUGH_GRADIENT, 
//        		1,20);//普通园的参数
        System.out.println("results.address():"+results.address()+",results.total()="+results.total());
        mat = new Mat(results);
//        printCvMat(mat);
        System.out.println(mat);
//        public static native CvSeq cvHoughCircles( CvArr image, Pointer circle_storage,
//                int method, double dp, double min_dist,
//                double param1/*=100*/,
//                double param2/*=100*/,
//                int min_radius/*=0*/,
//                int max_radius/*=0*/);
        System.out.println("CvSeq results:limit="+results.limit()+",position="+results.position());
        for(int i=0;i<results.total();i++) {
        	BytePointer p =cvGetSeqElem(results,i);
        	Point3f poi =new Point3f(p);
        	System.out.println(i+"=poi=x="+poi.get(0)+",y="+poi.get(1)+",radius="+poi.get(2));
        	circleLight(RawImage, (int)poi.get(0), (int)poi.get(1),(int)poi.get(2));
        }
//        	if(p==null) continue;
//        	System.out.println(i+"=x="+p.get(1)+",y="+p.get(2)+",p3="+p.get(3));
//        	int x = p.get(1);  
//            if(x<0) x=x+256;
//            int y = p.get(2);  
//            if(y<0) y=y+256;
//            int p3 = p.get(3);  
//            if(p3<0) p3=p3+256;
//            System.out.println(i+"=x="+x+",y="+y+",p3="+p3);
//            circleLight(RawImage, x, y, p3);
//            circleLight(RawImage, (int)poi.get(0), (int)poi.get(1),(int)poi.get(2));
//            CvCircle rect = cvBoundingRect(cvGetSeqElem(results, i),0);
            
//        }
        ShowImage(RawImage, "RawImage", 512);
//        cvReleaseImage(GrayImage); GrayImage = null;
//        cvReleaseImage(BWImage); BWImage = null;
//        cvReleaseImage(WorkingImage); WorkingImage = null;
        cvReleaseImage(RawImage); RawImage = null;
    }
    
    public static void demo()
    {
        int MinArea = 6;
        int ErodeCount =0;
        int DilateCount = 0;
        Mat mat;
        IplImage RawImage = null;
        // Read an image.
        for(int k = 6; k < 7; k++)
        {
            if(k == 0) { RawImage = cvLoadImage("F:\\Users\\james\\eclipseSpacse\\iris\\src\\main\\resources\\BlackBalls.jpg"); MinArea = 250; ErodeCount = 0; DilateCount = 1; }
            else if(k == 1) { RawImage = cvLoadImage("F:\\Users\\james\\eclipseSpacse\\iris\\src\\main\\resources\\Shapes1.jpg"); MinArea = 6; ErodeCount = 0; DilateCount = 1; }
            else if(k == 2) { RawImage = cvLoadImage("F:\\Users\\james\\eclipseSpacse\\iris\\src\\main\\resources\\Shapes2.jpg"); MinArea = 250; ErodeCount = 0; DilateCount = 1; }
//            else if(k == 3) {
//                RawImage = cvLoadImage("‪C:\\Users\\QUINN_WORK_A\\Desktop\\res\\20180511103049.png");
//                MinArea = 2800;
//                ErodeCount = 1;
//                DilateCount = 1;
//            }
            else if(k == 3) { RawImage = cvLoadImage("F:\\Users\\james\\eclipseSpacse\\iris\\src\\main\\resources\\Blob2.jpg"); MinArea = 2800; ErodeCount = 1; DilateCount = 1; }
            else if(k == 4) { RawImage = cvLoadImage("F:\\Users\\james\\eclipseSpacse\\iris\\src\\main\\resources\\Blob3.jpg"); MinArea = 2800; ErodeCount = 1; DilateCount = 1; }
            else if(k == 5) { RawImage = cvLoadImage("F:\\Users\\james\\eclipseSpacse\\iris\\src\\main\\resources\\Rice.jpg"); MinArea = 10; ErodeCount = 2; DilateCount = 1; }
//            else if(k == 6) { RawImage = cvLoadImage("F:\\Users\\james\\eclipseSpacse\\iris\\src\\main\\resources\\hongmo.jpg"); MinArea = 10; ErodeCount = 2; DilateCount = 1; }
            else if(k == 6) {
            	RawImage = cvLoadImage("F:\\Users\\james\\eclipseSpacse\\iris\\src\\main\\java\\org\\quinn\\test\\hongmo.jpg"); 
            	MinArea = 100; 
            	ErodeCount = 11; 
            	DilateCount = 20; 
            }
            
            ShowImage(RawImage, "RawImage", 512);
            
//            PrintGrayImage(RawImage, "PrintGrayImage");
            
            
            
//            mat = new Mat(RawImage.address());
//            mat.eye(1, 1,CV_64F);
//            System.out.println("mat1:"+mat);
//            mat=Imgcodecs.imread("BlackBalls.jpg");
//            System.out.println("mat2:"+mat);
//            cvRectangle(RawImage, pt1, pt2, color, Thick, 4, 0);
//            cvRectangleR(img, r, color, thickness, line_type, shift);
//            cvRectangleR(RawImage, cvRect(0, 0, 600, 200), cvScalar(0, 255, 0,0),2,1, 1); //将感兴趣区域框出来
            
//            cvCircle(RawImage, cvPoint(150,150), 50, cvScalar(0, 255, 0,0));
            
            IplImage GrayImage = cvCreateImage(cvGetSize(RawImage), IPL_DEPTH_8U, 1);
            cvCvtColor(RawImage, GrayImage, CV_BGR2GRAY);
            
//            ShowImage(SkewGrayImage(GrayImage, 1), "SkewGrayImage-GrayImage", 512);

            IplImage BWImage = cvCreateImage(cvGetSize(GrayImage), IPL_DEPTH_8U, 1); 
            cvThreshold(GrayImage, BWImage, 127, 255, CV_THRESH_BINARY);
//            ShowImage(BWImage, "BWImage");
            
            IplImage WorkingImage = cvCreateImage(cvGetSize(BWImage), IPL_DEPTH_8U, 1);     
            cvErode(BWImage, WorkingImage, null, ErodeCount);    
            cvDilate(WorkingImage, WorkingImage, null, DilateCount);
            ShowImage(WorkingImage, "WorkingImage", 512);
            
            //cvSaveImage("Working.jpg", WorkingImage);
            //PrintGrayImage(WorkingImage, "WorkingImage");
            //BinaryHistogram(WorkingImage);
            mat = new Mat(WorkingImage);
            System.out.println("mat.cols():"+mat.cols());
            System.out.println("mat.rows():"+mat.rows());
            Blobs Regions = new Blobs();
            Regions.BlobAnalysis(
                    WorkingImage,               // image
                    -1, -1,                     // ROI start col, row
                    -1, -1,                    // ROI cols, rows
                    0,                          // border (0 = black; 1 = white)
                    MinArea);                   // minarea
            Regions.PrintRegionData();
            
//            mat = cvMat(WorkingImage.asCvMat().rows(), WorkingImage.asCvMat().rows(), WorkingImage.asCvMat().type());
//            CvMemStorage storage = cvCreateMemStorage(0);  
//            CvSeq results =cvHoughCircles(WorkingImage, storage, CV_HOUGH_GRADIENT, 1, 1
//            		,100,100,10,500);
//            System.out.println("mat.cols():"+results.address());
//            System.out.println("mat.rows():"+mat.rows());
//            public static native CvSeq cvHoughCircles( CvArr image, Pointer circle_storage,
//                    int method, double dp, double min_dist,
//                    double param1/*=100*/,
//                    double param2/*=100*/,
//                    int min_radius/*=0*/,
//                    int max_radius/*=0*/);
//            System.out.println("CvSeq results:limit="+results.limit()+",position="+results.position());
//            for(int i=0;i<results.cvSize().get();i++) {
//            	BytePointer p =cvGetSeqElem(results,1);
            	
            	
////            	p.
////            	cvRound
//            }
            	
            
            for(int i = 1; i <= Blobs.MaxLabel; i++)
            {
                double [] Region = Blobs.RegionData[i];
                int Parent = (int) Region[Blobs.BLOBPARENT];
                int Color = (int) Region[Blobs.BLOBCOLOR];
                int MinX = (int) Region[Blobs.BLOBMINX];
                int MaxX = (int) Region[Blobs.BLOBMAXX];
                int MinY = (int) Region[Blobs.BLOBMINY];
                int MaxY = (int) Region[Blobs.BLOBMAXY];
//                Highlight(RawImage,  MinX, MinY, MaxX, MaxY, 1);  
                int radius=MaxX-MinX<=MaxY-MinY?(MaxY-MinY)/2:(MaxX-MinX)/2;
                int x=MinX+(MaxX-MinX)/2;
                int y=MinY+(MaxY-MinY)/2;
                boolean flag=checkPosition(x, y, radius,mat.cols(), mat.rows());
                System.out.println("point["+i+"]:flag["+flag+"]:x["+x+"],y["+y+"],radius["+radius+"]");
                if(!flag) continue;
//                Highlight(RawImage,  MinX, MinY, MaxX, MaxY, 5);
                circleLight(RawImage, x, y, radius);
            }
            
            ShowImage(RawImage, "RawImage", 512);

            cvReleaseImage(GrayImage); GrayImage = null;
            cvReleaseImage(BWImage); BWImage = null;
            cvReleaseImage(WorkingImage); WorkingImage = null;
        }
        cvReleaseImage(RawImage); RawImage = null;
    }
    
    public static void printCvMat(Mat mat) {
    	System.out.println(mat);
    	for(int row=0;row<mat.rows();row++) {
    		for(int col=0;col<mat.cols();col++) {
    			if(mat.channels()==3) {
    				int c1=getMatElement(mat, row, col, 1);
    				int c2=getMatElement(mat, row, col, 2);
    				int c3=getMatElement(mat, row, col, 3);
//    			System.out.println("=="+mat.row(row).col(col));
    				System.out.println("value=p.get()="+mat.ptr(row,col).get()+",=c1="+c1+",c2="+c2+",c3="+c3);
    			}else {
//    				int c1=getMatElement(mat, row, col, 1);
//    				System.out.println("value==c1="+c1);
    			}
    		}
    	}
    }
    public static int getMatElement(Mat img,int row,int col,int channel){  
        //获取字节指针  
        BytePointer bytePointer = img.ptr(row, col);  
        int value = bytePointer.get(channel);  
        if(value<0){  
            value=value+256;  
        }  
        return value;  
    } 
    
    public static boolean checkPosition(int x,int y,int radius,int maxX,int maxY) {
    	int checkX=6;
    	int checkY=8;
    	if(x<maxX/checkX || x>(maxX*(checkX-1)/checkX)) return false;
    	if(y<maxY/checkY || y>(maxY*(checkY-1)/checkY)) return false;
    	if(radius*2>(maxX*(checkX-2)/checkX) || radius*2>maxY) return false;
    	return true;
    }
    
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
    	cvCircle(RawImage, cvPoint(x,y), 3, cvScalar(0,255, 0, 0),-1,8,0);
    	cvCircle(RawImage, cvPoint(x,y), radius, cvScalar(255,0, 0, 0),4,8,0);
    }
    
    public static void Highlight(IplImage image, int xMin, int yMin, int xMax, int yMax, int Thick)
    {
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


