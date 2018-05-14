package main

import (
	"os"
	"fmt"
	"gocv.io/x/gocv"
	"image"
	"time"
)

func main(){
	Findtxy()
}

// 尝试解决同心圆问题
func Findtxy() {
	if len(os.Args) < 2 {
		fmt.Println("How to run:\n\tfind-circles [imgfile]")
		return
	}
	filename := os.Args[1]

	oriIMg:=gocv.IMRead(filename, gocv.IMReadAnyColor)
	showImg(oriIMg, "原图")
	grayOriImg := gocv.IMRead(filename, gocv.IMReadGrayScale)
	defer grayOriImg.Close()
	gocv.MedianBlur(grayOriImg, &grayOriImg, 5)
	cvtGrayOriImg := gocv.NewMat()
	defer cvtGrayOriImg.Close()
	gocv.CvtColor(grayOriImg, &cvtGrayOriImg, gocv.ColorGrayToBGR)
	// 裁剪算法
	cpyGrayOriImg:=grayOriImg.Clone()
	maxRect:=findMaxRadiusCircle(cpyGrayOriImg)
	cpyGrayOriImg=cpyGrayOriImg.Region(maxRect)
	showImg(cpyGrayOriImg, "外圆裁剪图")
	minRect:=findMinRadiusCircle(cpyGrayOriImg)
	cpyGrayOriImg=cpyGrayOriImg.Region(minRect)
	showImg(cpyGrayOriImg, "内圆裁剪图")

	make(chan int)<-1
	//blue := color.RGBA{0, 0, 255, 0}
	//red := color.RGBA{255, 0, 0, 0}
	//for i := 0; i < circles.Cols(); i++ {
	//	v := circles.GetVecfAt(0, i)
	//	x := int(v[0])
	//	y := int(v[1])
	//	r := int(v[2])
	//
	//	gocv.Circle(&cvtGrayOriImg, image.Pt(x, y), r, blue, 2)
	//	gocv.Circle(&cvtGrayOriImg, image.Pt(x, y), 2, red, 3)
	//}
}

func showImg(mat gocv.Mat,windowTitle string){
	go func() {
		cmat:=mat.Clone()
		window := gocv.NewWindow(windowTitle)
		defer window.Close()
		for {
			window.IMShow(cmat)
			if window.WaitKey(10) >= 0 {
				break
			}
			time.Sleep(1*time.Second)
		}
	}()
}

func findMaxRadiusCircle(mat gocv.Mat) image.Rectangle{
	circles:=gocv.NewMat()
	defer circles.Close()
	gocv.HoughCirclesWithParams(
		mat,
		&circles,
		gocv.HoughGradient,
		1,
		float64(mat.Rows()), //只找一个圆
		75,
		20,
		mat.Rows()/8,
		mat.Rows()*2/3, //找内圆的时候忽视外圆,并且一般不会影响第一次找外圆
	)


	var x,y,r int
	for i := 0; i < circles.Cols(); i++ {
		v := circles.GetVecfAt(0, i)
		if r==0{
			x = int(v[0])
			y = int(v[1])
			r = int(v[2])
		}else{
			nr:=int(v[2])
			if nr>r{
				x = int(v[0])
				y = int(v[1])
				r = nr
			}
		}
	}
	return image.Rect(x-r, y-r,x+r,y+r)
}

func findMinRadiusCircle(mat gocv.Mat) image.Rectangle{
	circles:=gocv.NewMat()
	defer circles.Close()
	gocv.HoughCirclesWithParams(
		mat,
		&circles,
		gocv.HoughGradient,
		1,
		float64(mat.Rows()), //只找一个圆
		75,
		20,
		mat.Rows()/16,
		mat.Rows()/3, //找内圆的时候忽视外圆,并且一般不会影响第一次找外圆
	)


	var x,y,r int
	for i := 0; i < circles.Cols(); i++ {
		v := circles.GetVecfAt(0, i)
		if r==0{
			x = int(v[0])
			y = int(v[1])
			r = int(v[2])
		}else{
			nr:=int(v[2])
			if nr<r{
				x = int(v[0])
				y = int(v[1])
				r = nr
			}
		}
	}
	return image.Rect(x-r, y-r,x+r,y+r)
}