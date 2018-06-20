package org.jk.demo;

        import org.bytedeco.javacpp.*;
        import static org.bytedeco.javacpp.opencv_core.*;
        import static org.bytedeco.javacpp.opencv_imgcodecs.*;
        import static org.bytedeco.javacpp.opencv_imgproc.*;
        import static org.bytedeco.javacpp.opencv_highgui.*;
        import static org.bytedeco.javacpp.opencv_features2d.*;
public class TestJM {
    //平滑
    public static void smooth(String filename) {
        IplImage image = cvLoadImage(filename);
        if (image != null) {
            cvSmooth(image, image, CV_GAUSSIAN, 3,0,0,0);
            cvSaveImage(filename, image);
            cvReleaseImage(image);
        }
    }
    //增加外框
    public static void padding(String filename)
    {
        CvMat src,dst;
        int top=10,  left=10;
        int borderType=IPL_BORDER_CONSTANT;
        CvScalar value;
/// Load an image
        src = cvLoadImageM(filename);
        //dst =cvCreateImage( cvSize( src.cvSize().width()+left, src.cvSize().height()+top ), IPL_DEPTH_8U, 3 ).asCvMat();
        dst =  cvCreateMat(src.rows()+left,src.cols()+top,CV_8UC3);
        value = new CvScalar( 0,0,0,0);
        //point为src 在dst图像上的左上角坐标
        CvPoint point=cvPoint(left/2,top/2);
        cvCopyMakeBorder( src, dst, point, borderType, value );
        cvSaveImage("D:\\IBM\\pad_new.JPG", dst);
    }
    //金字塔放大
    public static void pyramid_up(String filename)
    {
        CvMat src, dst, tmp;
/// Load an image
        src = cvLoadImageM(filename);
        //tmp = src;
        dst = cvCreateMat(src.rows()*2,src.cols()*2,src.type());
        cvPyrUp( src, dst, CV_GAUSSIAN_5x5);
        //cvPyrDown( tmp, dst,2);
        //tmp = dst;
        cvSaveImage("D:\\IBM\\pyramid_up.JPG", dst);
    }
    //金字塔缩小
    public static void pyramid_down(String filename)
    {
        CvMat src, dst, tmp;
/// Load an image
        src = cvLoadImageM(filename);
        //tmp = src;
        dst = cvCreateMat(src.rows()/2,src.cols()/2,src.type());
        cvPyrDown( src, dst, CV_GAUSSIAN_5x5);
        //cvPyrDown( tmp, dst,2);
        // tmp = dst;
        cvSaveImage("D:\\IBM\\pyramid_down.JPG", dst);
    }

    //扩张，将目标的边缘的“毛刺”踢除掉
    public static void morphology_Dilation(String filename,int dilation_elem)
    {
        CvMat src, dilation_dst;
        src = cvLoadImageM(filename);
        dilation_dst=src;
        int dilation_type=CV_SHAPE_RECT;
        if( dilation_elem == 0 ){ dilation_type = CV_SHAPE_RECT; }
        else if( dilation_elem == 1 ){ dilation_type = CV_SHAPE_CROSS; }
        else if( dilation_elem == 2) { dilation_type = CV_SHAPE_ELLIPSE; }
//   CvMat element = cvGetStructuringElement( dilation_type,
//   cvSize( 2*1 + 1, 2*1+1 ),
//   cvPoint( 2, 2 ) );
        /// Apply the dilation operation
        IplConvKernel kernel=cvCreateStructuringElementEx(3,3,1,1,dilation_type);
        cvDilate( src, dilation_dst, kernel,1);
        cvReleaseStructuringElement( kernel );
        cvSaveImage("D:\\IBM\\morphology_Dilation_"+dilation_type+".JPG", dilation_dst);
    }

    //侵蚀，将目标的边缘或者是内部的坑填掉
    public static void morphology_Erosion(String filename,int dilation_elem)
    {
        CvMat src, erosion_dst;
        src = cvLoadImageM(filename);
        erosion_dst=src;
        int dilation_type=CV_SHAPE_RECT;
        if( dilation_elem == 0 ){ dilation_type = CV_SHAPE_RECT; }
        else if( dilation_elem == 1 ){ dilation_type = CV_SHAPE_CROSS; }
        else if( dilation_elem == 2) { dilation_type = CV_SHAPE_ELLIPSE; }
//   CvMat element = cvGetStructuringElement( dilation_type,
//   cvSize( 2*1 + 1, 2*1+1 ),
//   cvPoint( 2, 2 ) );
        /// Apply the dilation operation
        IplConvKernel kernel=cvCreateStructuringElementEx(3,3,1,1,dilation_type);
        cvErode( src, erosion_dst, kernel,1);
        cvReleaseStructuringElement( kernel );
        cvSaveImage("D:\\IBM\\morphology_Erosion_"+dilation_type+".JPG", erosion_dst);
    }

    //The simplest segmentation method
    public static void Thresholding(String filename,int type)
    {
        IplImage src, pGrayImg,dst;
        int threshold_value = 0;

        int threshold_type = type;
        int  max_BINARY_value = 255;
        src=cvLoadImage(filename);
        pGrayImg=gray(src);
        dst=cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1);

        cvThreshold( pGrayImg, dst, threshold_value, max_BINARY_value,threshold_type );
        cvSaveImage("D:\\IBM\\morphology_Thresholding_"+type+".JPG",dst);
        cvReleaseImage(src);
        cvReleaseImage(dst);
        cvReleaseImage(pGrayImg);
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
    //sobel边缘检测
    public static void sobel(String filename)
    {
        CvMat src, src_gray;
        CvMat grad=null;
        int scale = 1;
        int delta = 0;
        int ddepth = CV_16S;
        int c;
        src=cvLoadImageM(filename);
        //GaussianBlur( src, src, cvSize(3,3), 0, 0, BORDER_DEFAULT );
        cvSmooth(src, src, CV_GAUSSIAN, 3,0,0,0);
        src_gray=gray(src.asIplImage()).asCvMat();
        CvMat grad_x=null, grad_y=null;
        CvMat abs_grad_x=null, abs_grad_y=null;
/// Gradient X
//Scharr( src_gray, grad_x, ddepth, 1, 0, scale, delta, BORDER_DEFAULT );
        grad_x=cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1).asCvMat();
        cvSobel( src_gray, grad_x, 1, 0, 3);
        abs_grad_x=cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1).asCvMat();
        cvConvertScaleAbs( grad_x, abs_grad_x,1,0);
/// Gradient Y
//Scharr( src_gray, grad_y, ddepth, 0, 1, scale, delta, BORDER_DEFAULT );
        grad_y=cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1).asCvMat();
        abs_grad_y=cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1).asCvMat();
        cvSobel( src_gray, grad_y, 0, 1, 3);
        cvConvertScaleAbs( grad_y, abs_grad_y,1,0 );
/// Total Gradient (approximate)
        grad=cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1).asCvMat();
        cvAddWeighted( abs_grad_x, 0.5, abs_grad_y, 0.5, 0,grad );
        cvSaveImage("D:\\IBM\\sobel.jpg",grad);

    }

    public static void laplacian(String filename)
    {
        CvMat src, src_gray,dst,abs_dst;
        src=cvLoadImageM(filename);
        cvSmooth(src, src, CV_GAUSSIAN, 3,0,0,0);
        src_gray=gray(src.asIplImage()).asCvMat();
        dst=cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1).asCvMat();
        abs_dst=cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1).asCvMat();

        cvLaplace( src_gray, dst,3);
        cvConvertScaleAbs( dst, abs_dst,1,0);
        cvWaitKey(0);
    }

    public static void canny(String filename)
    {

        CvMat src,src_gray, detected_edges,dst;
        src=cvLoadImageM(filename);
        src_gray=gray(src.asIplImage()).asCvMat();
        detected_edges=cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1).asCvMat();
        cvSmooth(src_gray, detected_edges, CV_GAUSSIAN, 3,0,0,0);

        cvCanny(detected_edges, detected_edges,90,90*3,3);
        //dst = Scalar::all(0);
        dst=cvCreateMat(src.rows(),src.cols(), src.type());
        //cvSetIdentity(dst,cvRealScalar(0));
        cvSet(dst, cvScalar(0,0,0,0),null);
        cvCopy( src,dst,detected_edges);
        cvSaveImage("D:\\IBM\\canny.jpg",dst);
    }

    public static void standardHoughLine(String filename)
    {
        CvMat src, detected_edges,color_dst;
        src=cvLoadImageM(filename,0);//加载灰度图
        detected_edges=cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1).asCvMat();//创建一通道图像
        color_dst = cvCreateImage( cvGetSize( src ), IPL_DEPTH_8U, 3).asCvMat();  //创建三通道图像
        //cvSmooth(src, detected_edges, CV_GAUSSIAN, 3);
        //边缘检测
        cvCanny(src, detected_edges,50,200,3);
        //src_gray=gray(detected_edges.asIplImage()).asCvMat();
        cvCvtColor( detected_edges, color_dst, CV_GRAY2BGR ); //色彩空间转换，将dst转换到另外一个色彩空间即3通道图像

        CvMemStorage storage=cvCreateMemStorage(0);
        CvSeq lines  = cvHoughCircles( new IplImage(detected_edges), storage, CV_HOUGH_STANDARD, 1, Math.PI/180, 150, 0, 0,500);
//        CvSeq lines  = cvHoughLines2( detected_edges, storage, CV_HOUGH_STANDARD, 1, Math.PI/180, 150, 0, 0);
        //循环直线序列
        for( int i = 0; i < lines.total(); i++ )
        {
            FloatPointer line=new FloatPointer(cvGetSeqElem(lines,i));//用GetSeqElem获得直线
//  CvPoint2D32f point = new CvPoint2D32f(cvGetSeqElem(lines, i));
//
//                  float rho=point.x();
//              float theta=point.y();
            //对于SHT和MSHT（标准变换）这里line[0]，line[1]是rho（与像素相干单位的距离精度）和theta（弧度测量的角度精度）
            float rho = line.get(0);
            float theta =line.get(1);
            System.out.println(rho+"::"+theta);
            CvPoint pt1, pt2;
            float a = (float)Math.cos(theta), b = (float)Math.sin(theta);
            float x0 = a*rho, y0 = b*rho;
            //pt1.position(0).x(Math.round(x0 + 1000*(-b))) ;
            //pt1.position(0).y(Math.round(y0 + 1000*(a)));
            pt1=new CvPoint(Math.round(x0 + 1000*(-b)),Math.round(y0 + 1000*(a)));
            //pt2.position(0).x(Math.round(x0 - 1000*(-b)));
            //pt2.position(0).y(Math.round(y0 - 1000*(a)));
            pt2=new CvPoint(Math.round(x0 - 1000*(-b)),Math.round(y0 - 1000*(a)));
            cvLine( color_dst, pt1, pt2, CV_RGB(0,0,255), 1, CV_AA,0);
        }
        cvNamedWindow("Hough");
        cvShowImage( "Hough", color_dst );
        cvWaitKey();
    }

    //The Probabilistic Hough Line Transform
    public static void houghLine(String filename)
    {
        CvMat src, detected_edges,color_dst;
        src=cvLoadImageM(filename,0);//加载灰度图
        detected_edges=cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1).asCvMat();//创建一通道图像
        color_dst = cvCreateImage( cvGetSize( src ), IPL_DEPTH_8U, 3 ).asCvMat();  //创建三通道图像
        //cvSmooth(src, detected_edges, CV_GAUSSIAN, 3);
        //边缘检测
        cvCanny(src, detected_edges,50,200,3);
        //src_gray=gray(detected_edges.asIplImage()).asCvMat();
        cvCvtColor( detected_edges, color_dst, CV_GRAY2BGR ); //色彩空间转换，将dst转换到另外一个色彩空间即3通道图像

        CvMemStorage storage=cvCreateMemStorage(0);
        CvSeq lines  = cvHoughCircles( new IplImage(detected_edges), storage, CV_HOUGH_STANDARD, 1, Math.PI/180, 150, 0, 0,500);
//        CvSeq lines  = cvHoughLines2( detected_edges, storage, CV_HOUGH_PROBABILISTIC, 1, Math.PI/180, 150, 50, 10);
        //循环直线序列
        for( int i = 0; i < lines.total(); i++ )  //lines存储的是直线
        {
            CvPoint line = new CvPoint(cvGetSeqElem(lines,i));

            cvLine( color_dst,new CvPoint(line.position(0)),new CvPoint(line.position(1)), CV_RGB( 0, 255, 0 ),1,CV_AA,0 );  //将找到的直线标记为红色
            //color_dst是三通道图像用来存直线图像
        }
        cvNamedWindow("Hough");
        cvShowImage( "Hough", color_dst );
        cvWaitKey();
        //Canvas.showImage(color_dst);
        // cvSaveImage("D:\\IBM\\houghLineCV_AA.jpg",color_dst);
    }


    public static void houghCircle(String filename){
        CvMat src, src_gray,color_dst;
        src=cvLoadImageM(filename);//加载灰度图
        src_gray=gray(src.asIplImage()).asCvMat();
        cvSmooth(src_gray, src_gray, CV_GAUSSIAN, 3,0,0,0);
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
            cvCircle( src, center, 3, CV_RGB(0,255,0), -1, 8, 0 );
            // circle outline
            cvCircle( src, center, radius, CV_RGB(255,0,0), 3, 8, 0 );
        }
        /// Show your results
        cvNamedWindow( "Hough Circle Transform Demo", CV_WINDOW_AUTOSIZE );
        cvShowImage( "Hough Circle Transform Demo", src );
        cvWaitKey(0);

    }

    public static void  histogramEqualization(String filename)
    {
        CvMat src,src_gray, detected_edges,dst;
        src=cvLoadImageM(filename);
        src_gray=gray(src.asIplImage()).asCvMat();
        dst =  cvCreateMat(src.rows(),src.cols(),CV_8UC1);
        /// Apply Histogram Equalization
        cvEqualizeHist( src_gray, dst );
        /// Display results
        cvNamedWindow( "source_window", CV_WINDOW_AUTOSIZE );
        cvNamedWindow( "equalized_window", CV_WINDOW_AUTOSIZE );
        cvShowImage( "source_window", src );
        cvShowImage( "equalized_window", dst );
        /// Wait until user exits the program
        cvWaitKey(0);
    }

    public static void histogramCalculationFor1(String filename)
    {
        CvMat src,redImage,greenImage,blueImage;
        src=cvLoadImageM(filename);
        redImage=cvCreateImage(cvGetSize(src),IPL_DEPTH_8U,1).asCvMat();
        greenImage=cvCreateImage(cvGetSize(src),IPL_DEPTH_8U,1).asCvMat();
        blueImage=cvCreateImage(cvGetSize(src),IPL_DEPTH_8U,1).asCvMat();

        cvSplit(src,blueImage,greenImage,redImage,null);
        IplImage  b_planes[] = {blueImage.asIplImage()};
        IplImage  g_planes[] = {greenImage.asIplImage()};
        IplImage  r_planes[] = {redImage.asIplImage()};
/// Establish the number of bins
        int histSize = 256;
        /// Set the ranges ( for B,G,R) )
        float range[] = { 0, 255} ;
        float[] histRange[] = { range };
        int hist_size[] = {histSize};

        CvHistogram b_hist = cvCreateHist(1,hist_size,CV_HIST_ARRAY,histRange,1);
        /// Compute the histograms:

        cvCalcHist(b_planes,b_hist,0,null);

        CvHistogram g_hist = cvCreateHist(1,hist_size,CV_HIST_ARRAY,histRange,1);
        /// Compute the histograms:
        cvCalcHist(g_planes,g_hist,0,null);
        CvHistogram r_hist = cvCreateHist(1,hist_size,CV_HIST_ARRAY,histRange,1);
        /// Compute the histograms:
        cvCalcHist(r_planes,r_hist,0,null);

// Draw the histograms for B, G and R
        int hist_w = 512; int hist_h = 400;
        int bin_w = Math.round(hist_w/histSize );
        CvMat histImage=cvCreateMat(hist_h, hist_w, CV_8UC3);
        cvSet(histImage,CV_RGB( 0,0,0),null);
        ///归一化， Normalize the result to [ 0, histImage.rows ]
        cvNormalize(b_hist.mat(), b_hist.mat(), 1, histImage.rows(), NORM_MINMAX ,null);
        cvNormalize(g_hist.mat(),g_hist.mat(), 1, histImage.rows(), NORM_MINMAX ,null);
        cvNormalize(r_hist.mat(),r_hist.mat(), 1, histImage.rows(), NORM_MINMAX ,null);
        /// Draw for each channel
        for( int i = 1; i < histSize; i++ )
        {
            cvLine( histImage, cvPoint( bin_w*(i-1), hist_h -(int)(Math.round( cvGetReal1D( b_hist.bins(), i-1 ))) ) ,
                    cvPoint( bin_w*(i), hist_h - (int)Math.round(cvGetReal1D( b_hist.mat(),i)) ),
                    CV_RGB( 255, 0, 0), 2, 8, 0 );
            cvLine( histImage, cvPoint( bin_w*(i-1), hist_h - (int)Math.round(cvGetReal1D( g_hist.bins(),i-1)) ) ,
                    cvPoint( bin_w*(i), hist_h - (int)Math.round(cvGetReal1D( g_hist.bins(),i)) ),
                    CV_RGB( 0, 255, 0), 2, 8, 0 );
            cvLine( histImage, cvPoint( bin_w*(i-1), hist_h - (int)Math.round(cvGetReal1D( r_hist.bins(),i-1)) ) ,
                    cvPoint( bin_w*(i), hist_h - (int)Math.round(cvGetReal1D( r_hist.bins(),i)) ),
                    CV_RGB( 0, 0, 255), 2, 8, 0 );
        }
/// Display
        cvNamedWindow("calcHist Demo", CV_WINDOW_AUTOSIZE );
        cvShowImage("calcHist Demo", histImage );
        cvWaitKey(0);
    }

    public static void histogramCalculationFor0(String filename)
    {
        CvMat src,redImage,greenImage,blueImage;
        src=cvLoadImageM(filename);
        redImage=cvCreateImage(cvGetSize(src),IPL_DEPTH_8U,1).asCvMat();
        greenImage=cvCreateImage(cvGetSize(src),IPL_DEPTH_8U,1).asCvMat();
        blueImage=cvCreateImage(cvGetSize(src),IPL_DEPTH_8U,1).asCvMat();

        cvSplit(src,blueImage,greenImage,redImage,null);
        IplImage  b_planes[] = {blueImage.asIplImage()};
        IplImage  g_planes[] = {greenImage.asIplImage()};
        IplImage  r_planes[] = {redImage.asIplImage()};
/// Establish the number of bins
        int histSize = 3;
        /// Set the ranges ( for B,G,R) )
        float range[] = { 0, 100,101,200,201,255} ;
        float[] histRange[] = { range };
        int hist_size[] = {histSize};

        CvHistogram b_hist = cvCreateHist(1,hist_size,CV_HIST_ARRAY,histRange,0);
        /// Compute the histograms:
        cvCalcHist(b_planes,b_hist,0,null);

        CvHistogram g_hist = cvCreateHist(1,hist_size,CV_HIST_ARRAY,histRange,0);
        /// Compute the histograms:
        cvCalcHist(g_planes,g_hist,0,null);
        CvHistogram r_hist = cvCreateHist(1,hist_size,CV_HIST_ARRAY,histRange,0);
        /// Compute the histograms:
        cvCalcHist(r_planes,r_hist,0,null);

// Draw the histograms for B, G and R
        int hist_w = 512; int hist_h = 400;
        int bin_w = Math.round(hist_w/histSize );
        //CvMat histImage=new CvMat( hist_h, hist_w, CV_8UC3);
//dst = Scalar::all(0);
        CvMat histImage=cvCreateMat(hist_h, hist_w, CV_8UC3);
        //cvSetIdentity(dst,cvRealScalar(0));
        cvSet(histImage,CV_RGB( 0,0,0),null);
        /// Normalize the result to [ 0, histImage.rows ]
        cvNormalize(b_hist.mat(), b_hist.mat(), 1, histImage.rows(), NORM_MINMAX ,null);
        cvNormalize(g_hist.mat(),g_hist.mat(), 1, histImage.rows(), NORM_MINMAX ,null);
        cvNormalize(r_hist.mat(),r_hist.mat(), 1, histImage.rows(), NORM_MINMAX ,null);
        /// Draw for each channel
        for( int i = 1; i < histSize; i++ )
        {
            cvLine( histImage, cvPoint( bin_w*(i-1), hist_h -(int)(Math.round( cvGetReal1D( b_hist.bins(), i-1 ))) ) ,
                    cvPoint( bin_w*(i), hist_h - (int)Math.round(cvGetReal1D( b_hist.mat(),i)) ),
                    CV_RGB( 255, 0, 0), 2, 8, 0 );
            cvLine( histImage, cvPoint( bin_w*(i-1), hist_h - (int)Math.round(cvGetReal1D( g_hist.bins(),i-1)) ) ,
                    cvPoint( bin_w*(i), hist_h - (int)Math.round(cvGetReal1D( g_hist.bins(),i)) ),
                    CV_RGB( 0, 255, 0), 2, 8, 0 );
            cvLine( histImage, cvPoint( bin_w*(i-1), hist_h - (int)Math.round(cvGetReal1D( r_hist.bins(),i-1)) ) ,
                    cvPoint( bin_w*(i), hist_h - (int)Math.round(cvGetReal1D( r_hist.bins(),i)) ),
                    CV_RGB( 0, 0, 255), 2, 8, 0 );
        }
/// Display
        cvNamedWindow("calcHist Demo", CV_WINDOW_AUTOSIZE );
        cvShowImage("calcHist Demo", histImage );
        cvWaitKey(0);
    }

    public static void histogramComparison(String src,String test1,String test2)
    {
        CvMat src_base,src_half,src_test1,src_test2,hsv_base,hsv_test1,hsv_test2,hsv_half_down;
        src_base=cvLoadImageM(src);
        src_test1=cvLoadImageM(test1);
        src_test2=cvLoadImageM(test2);
        System.out.println("坐标起始点:"+ src_base.asIplImage().origin());
        src_half=cvCreateMatHeader(src_test2.rows()/2,src_test2.cols(),src_test2.type());
        CvRect rect=cvRect(0,  src_base.rows()/2, src_base.cols(), src_base.rows()/2);
        cvGetSubRect(src_base,src_half, rect);
        hsv_base = cvCreateImage(cvGetSize(src_base),8,3).asCvMat();
        hsv_test1 = cvCreateImage(cvGetSize(src_test1),8,3).asCvMat();
        hsv_test2 = cvCreateImage(cvGetSize(src_test2),8,3).asCvMat();
        hsv_half_down=cvCreateImage(cvGetSize(src_half),8,3).asCvMat();
/// Convert to HSV
        cvCvtColor( src_base, hsv_base, CV_BGR2HSV );
        cvCvtColor( src_test1, hsv_test1, CV_BGR2HSV );
        cvCvtColor( src_test2, hsv_test2, CV_BGR2HSV );
        cvCvtColor( src_half, hsv_half_down, CV_BGR2HSV );
        //获得h和s通道的值
        IplImage hsv_base_h_plane = cvCreateImage(cvGetSize(src_base),8,1);
        IplImage hsv_base_s_plane = cvCreateImage(cvGetSize(src_base),8,1);
        cvSplit(hsv_base,hsv_base_h_plane,hsv_base_s_plane,null,null);
        IplImage[] hsv_base_array={hsv_base_h_plane,hsv_base_s_plane};
//获得h和s通道的值
        IplImage hsv_test1_h_plane = cvCreateImage(cvGetSize(src_test1),8,1);
        IplImage hsv_test1_s_plane = cvCreateImage(cvGetSize(src_test1),8,1);
        cvSplit(hsv_test1,hsv_test1_h_plane,hsv_test1_s_plane,null,null);
        IplImage[] hsv_test1_array={hsv_test1_h_plane,hsv_test1_s_plane};
//获得h和s通道的值
        IplImage hsv_test2_h_plane = cvCreateImage(cvGetSize(src_test2),8,1);
        IplImage hsv_test2_s_plane = cvCreateImage(cvGetSize(src_test2),8,1);
        cvSplit(hsv_test2,hsv_test2_h_plane,hsv_test2_s_plane,null,null);
        IplImage[] hsv_test2_array={hsv_test2_h_plane,hsv_test2_s_plane};
//获得h和s通道的值
        IplImage hsv_half_down_h_plane = cvCreateImage(cvGetSize(hsv_half_down),8,1);
        IplImage hsv_half_down_s_plane = cvCreateImage(cvGetSize(hsv_half_down),8,1);
        cvSplit(hsv_half_down,hsv_half_down_h_plane,hsv_half_down_s_plane,null,null);
        IplImage[] hsv_half_down_array={hsv_half_down_h_plane,hsv_half_down_s_plane};

/// Using 30 bins for hue and 32 for saturation
        int h_bins = 50; int s_bins = 60;
        int histSize[] = { h_bins, s_bins };
        // hue varies from 0 to 256, saturation from 0 to 180
        float h_ranges[] = { 0, 256 };
        float s_ranges[] = { 0, 180 };
        float[] ranges[] = { h_ranges, s_ranges };
// Use the o-th and 1-st channels
        //redImage=cvCreateImage(cvGetSize(src),IPL_DEPTH_8U,1).asCvMat();
        CvHistogram hist_base = cvCreateHist(2,histSize,CV_HIST_ARRAY,ranges,1);
        /// Compute the histograms:
        cvCalcHist(hsv_base_array,hist_base,0,null);
        cvNormalize(hist_base.mat(), hist_base.mat(), 0,1, NORM_MINMAX ,null);

        CvHistogram hist_half_down = cvCreateHist(2,histSize,CV_HIST_ARRAY,ranges,1);
        /// Compute the histograms:
        cvCalcHist(hsv_half_down_array,hist_half_down,0,null);
        cvNormalize(hist_half_down.mat(), hist_half_down.mat(), 0,1, NORM_MINMAX ,null);

        CvHistogram hist_test1 = cvCreateHist(2,histSize,CV_HIST_ARRAY,ranges,1);
        /// Compute the histograms:
        cvCalcHist(hsv_test1_array,hist_test1,0,null);
        cvNormalize(hist_test1.mat(), hist_test1.mat(), 0,1, NORM_MINMAX ,null);

        CvHistogram hist_test2 = cvCreateHist(2,histSize,CV_HIST_ARRAY,ranges,1);
        /// Compute the histograms:
        cvCalcHist(hsv_test2_array,hist_test2,0,null);
        cvNormalize(hist_test2.mat(), hist_test2.mat(), 0,1, NORM_MINMAX ,null);

        for( int i = 0; i < 4; i++ )
        {
            int compare_method = i;
            double base_base = cvCompareHist( hist_base, hist_base, compare_method );
            double base_half = cvCompareHist( hist_base, hist_half_down, compare_method );
            double base_test1 = cvCompareHist( hist_base, hist_test1, compare_method );
            double base_test2 = cvCompareHist( hist_base, hist_test2, compare_method );
            System.out.println( " Method [%d] Perfect, Base-Half, Base-Test(1)," +
                    " Base-Test(2) : %f, %f, %f, %f \n"+i+","+ base_base+","+ base_half+","+base_test1+","+base_test2);
        }
    }

    public static void backProjection(String filename,int bins){


        IplImage target=cvLoadImage(filename,1);  //装载图片
        IplImage target_hsv=cvCreateImage( cvGetSize(target), IPL_DEPTH_8U, 3 );
        IplImage target_hue=cvCreateImage( cvGetSize(target), IPL_DEPTH_8U, 1);
        cvCvtColor(target,target_hsv,CV_BGR2HSV);       //转化到HSV空间
        cvSplit( target_hsv, target_hue, null, null, null );    //获得H分量
//   int ch[] = { 0, 0 };
//   IplImage[] target_hsv_s={target_hsv};
//   IplImage[] target_hue_s={target_hue};
//   cvMixChannels(target_hsv_s, 1, target_hue_s, 1, ch, 1 );
        if(bins<2) bins=2;
        int hist_size[]={bins};          //将H分量的值量化到[0,255]
        float[] ranges[]={ {0,180} };    //H分量的取值范围是[0,360)
        CvHistogram hist=cvCreateHist(1, hist_size, CV_HIST_ARRAY,ranges, 1);
        IplImage[] target_hues={target_hue};
        cvCalcHist(target_hues,hist, 0, null);
        cvNormalize(hist.mat(), hist.mat(), 0,255, NORM_MINMAX ,null);
        IplImage result=cvCreateImage(cvGetSize(target),IPL_DEPTH_8U,1);

        cvCalcBackProject(target_hues,result,hist);
        cvShowImage( "BackProj", result );
        //cvShowImage( "src", target );
        cvWaitKey(0);
    }


    public static void templateMatching(String src,String template,int match_method)
    {
        CvMat img=cvLoadImageM(src,1);  //装载图片
        CvMat templ=cvLoadImageM(template,1);  //装载图片
/// Create the result matrix
        int result_cols = img.cols() - templ.cols() + 1;
        int result_rows = img.rows() - templ.rows() + 1;
        CvMat result=cvCreateMat( result_cols, result_rows, CV_32FC1 );
        cvMatchTemplate( img, templ, result, 0 );
        cvNormalize(result, result, 0,1, NORM_MINMAX ,null);
/// Localizing the best match with minMaxLoc
//        double minVal[]=new double[5]; double maxVal[]=new double[5];
        DoublePointer minVal= new DoublePointer(5);
        DoublePointer maxVal= new DoublePointer(5);
        CvPoint minLoc=new CvPoint();
        CvPoint maxLoc=new CvPoint();
        CvPoint matchLoc=new CvPoint();
        cvMinMaxLoc( result, minVal, maxVal, minLoc, maxLoc,null);
/// For SQDIFF and SQDIFF_NORMED, the best matches are lower values. For all the other methods, the higher the better
        if( match_method == CV_TM_SQDIFF || match_method == CV_TM_SQDIFF_NORMED )
        { matchLoc = minLoc; }
        else
        { matchLoc = maxLoc; }
/// Source image to display
        CvMat img_display=cvCreateMat( img.cols(),img.rows(), img.type() );;
        cvCopy(img,img_display,null);
/// Show me what you got
        cvRectangle( img_display, matchLoc,new CvPoint( matchLoc.x() + templ.cols() , matchLoc.y() + templ.rows() ), new CvScalar(0,0,0,0), 2, 8, 0);
        cvRectangle( result, matchLoc, new CvPoint( matchLoc.x() + templ.cols() , matchLoc.y() + templ.rows() ), new CvScalar(0,0,0,0), 2, 8, 0 );
        cvShowImage( "image_window", img_display );
        cvShowImage( "result_window", result );
    }
    public static void main(String...strings) {
        //Smoother.smooth("D:\\IBM\\test.JPG");
        //Smoother.padding("D:\\IBM\\pad.JPG");
        // Smoother.pyramid_up("D:\\IBM\\pad.JPG");
        //Smoother.pyramid_down("D:\\IBM\\pad.JPG");
//   morphology_Dilation("D:\\IBM\\morp.JPG",0);
//   morphology_Dilation("D:\\IBM\\morp.JPG",1);
//   morphology_Dilation("D:\\IBM\\morp.JPG",2);

//   morphology_Erosion("D:\\IBM\\morp.JPG",0);
//   morphology_Erosion("D:\\IBM\\morp.JPG",1);
//   morphology_Erosion("D:\\IBM\\morp.JPG",2);
//   Thresholding("D:\\IBM\\pad32.jpg",0);
//   Thresholding("D:\\IBM\\morp.jpg",1);
//   Thresholding("D:\\IBM\\morp.jpg",2);
//   Thresholding("D:\\IBM\\morp.jpg",3);
//   Thresholding("D:\\IBM\\morp.jpg",4);
        //gray("D:\\IBM\\pad32.jpg");
        //sobel("D:\\IBM\\pad32.jpg");
        //laplacian("D:\\IBM\\pad32.jpg");
//canny("D:\\IBM\\pad32.jpg");
        // houghLine("D:\\IBM\\luoma.jpg");
        //standardHoughLine("D:\\IBM\\luoma.jpg");
        //houghCircle("D:\\IBM\\177.jpg");
        //histogramEqualization("D:\\IBM\\040.jpg");
        // histogramCalculationFor1("D:\\IBM\\pad32.jpg");
        //histogramCalculationFor0("D:\\IBM\\pad32.jpg");
        //histogramComparison("D:\\IBM\\morp.JPG","D:\\IBM\\177.jpg","D:\\IBM\\pad32.jpg");
        backProjection("D:\\IBM\\hand2.jpg", 2);
        backProjection("D:\\IBM\\hand2.jpg", 5);
//   backProjection("D:\\IBM\\hand2.jpg",10);
//   backProjection("D:\\IBM\\hand2.jpg",12);
//   backProjection("D:\\IBM\\hand2.jpg",20);
//   backProjection("D:\\IBM\\hand2.jpg",40);
//   backProjection("D:\\IBM\\hand2.jpg",80);
//   backProjection("D:\\IBM\\hand2.jpg",120);
//   backProjection("D:\\IBM\\hand2.jpg",200);
//   backProjection("D:\\IBM\\hand2.jpg",255);
//  }
    }}