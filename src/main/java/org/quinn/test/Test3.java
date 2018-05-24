//package org.quinn.test;
//
//import static org.bytedeco.javacpp.opencv_core.*;
//import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;
//import static org.bytedeco.javacpp.opencv_highgui.cvWaitKey;
//import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
//import static org.bytedeco.javacpp.opencv_imgproc.*;
//
//public class Test3 {
//    public static void main(String[] args) {
//
//    }
//    int highlight_remove_Chi(IplImage src,IplImage dst,double Re)
//    {
//        int height=src.height();
//        int width=src.width();
//        int step=src.widthStep();
//        int i=0,j=0;
//        char R,G,B,MaxC;
//        double alpha,beta,alpha_r,alpha_g,alpha_b,beta_r,beta_g,beta_b,temp=0,realbeta=0,minalpha=0;
//        double gama,gama_r,gama_g,gama_b;
//        String srcData;
//        String dstData;
//        for (i=0;i<height;i++)
//        {
//            srcData=src.imageData().toString()+i*step;
//            dstData=dst.imageData().toString()+i*step;
//            for (j=0;j<width;j++)
//            {
//                R=srcData.charAt(j*3);
//                G=srcData.charAt(j*3+1);
//                B=srcData.charAt(j*3+2);
//
//                alpha_r=(double)R/(double)(R+G+B);
//                alpha_g=(double)G/(double)(R+G+B);
//                alpha_b=(double)B/(double)(R+G+B);
//                alpha=Math.max(Math.max(alpha_r,alpha_g),alpha_b);
//                MaxC=(char)Math.max(Math.max(R,G),B);// compute the maximum of the rgb channels
//                minalpha=Math.min(Math.min(alpha_r,alpha_g),alpha_b);                 beta_r=1-(alpha-alpha_r)/(3*alpha-1);
//                beta_g=1-(alpha-alpha_g)/(3*alpha-1);
//                beta_b=1-(alpha-alpha_b)/(3*alpha-1);
//                beta=Math.max(Math.max(beta_r,beta_g),beta_b);//将beta当做漫反射系数，则有                 // gama is used to approximiate the beta
//                gama_r=(alpha_r-minalpha)/(1-3*minalpha);
//                gama_g=(alpha_g-minalpha)/(1-3*minalpha);
//                gama_b=(alpha_b-minalpha)/(1-3*minalpha);
//                gama=Math.max(Math.max(gama_r,gama_g),gama_b);
//
//                temp=(gama*(R+G+B)-MaxC)/(3*gama-1);
//                //beta=(alpha-minalpha)/(1-3*minalpha)+0.08;
//                //temp=(gama*(R+G+B)-MaxC)/(3*gama-1);
//                dstData.rep[j*3]=R-( char)(temp+0.5);
//                dstData[j*3+1]=G-( char)(temp+0.5);
//                dstData[j*3+2]=B-( char)(temp+0.5);
//            }
//        }
//        return 1;
//    }
//}