package main

import (
	"fmt"
	"gocv.io/x/gocv"
	"image"
	"image/color"
)

func main() {

	grayMat := gocv.IMRead("doublecircle.jpg", gocv.IMReadGrayScale)
	defer grayMat.Close()

	gocv.MedianBlur(grayMat, &grayMat, 5)

	bgrMat := gocv.NewMat()
	defer bgrMat.Close()

	gocv.CvtColor(grayMat, &bgrMat, gocv.ColorGrayToBGR)

	circles := gocv.NewMat()
	defer circles.Close()

	// 找外圆
	gocv.HoughCirclesWithParams(
		grayMat,
		&circles,
		gocv.HoughGradient,
		1,
		float64(grayMat.Rows()),
		75,
		20,
		grayMat.Rows()/8,
		grayMat.Rows()*2/3,
	)
	if circles.Cols() != 1 {
		panic("外圆查找异常")
	}

	blue := color.RGBA{0, 0, 255, 0}
	red := color.RGBA{255, 0, 0, 0}
	// 外圆参数
	outV := circles.GetVecfAt(0, 0)
	outX := int(outV[0])
	outY := int(outV[1])
	outR := int(outV[2])
	// 画圈圈标记圆
	gocv.Circle(&bgrMat, image.Pt(outX, outY), outR, blue, 2)
	gocv.Circle(&bgrMat, image.Pt(outX, outY), 2, red, 3)

	// 缩小参数找内圆
	gocv.HoughCirclesWithParams(
		grayMat,
		&circles,
		gocv.HoughGradient,
		1,
		float64(grayMat.Rows()),
		75,
		20,
		grayMat.Rows()/16,
		outR/2,
	)
	if circles.Cols() != 1 {
		panic("内圆查找异常")
	}
	inV := circles.GetVecfAt(0, 0)
	inX := int(inV[0])
	inY := int(inV[1])
	inR := int(inV[2])
	gocv.Circle(&bgrMat, image.Pt(inX, inY), inR, blue, 2)
	gocv.Circle(&bgrMat, image.Pt(inX, inY), 2, red, 3)
	go IMShow(bgrMat, "内外圆")

	// 内外圆像素差集拷贝
	// 3通道BGR原图
	oriMat := gocv.IMRead("doublecircle.jpg", gocv.IMReadColor)
	go IMShow(oriMat, "原图")
	// 新建空Mat,避免内存共用
	region := gocv.NewMatWithSize(oriMat.Rows(), oriMat.Cols(), oriMat.Type())
	fmt.Println(oriMat.Type(), oriMat.Channels(), oriMat.Rows(), oriMat.Cols())
	chs := oriMat.Channels()
	fmt.Println(outR, outX, outY)
	for r := 0; r < oriMat.Rows(); r++ {
		for c := 0; c < oriMat.Cols(); c++ {
			// 判断像素坐标是否在差集内
			if inCirle(outR, outX, outY, c, r) && !inCirle(inR, inX, inY, c, r) {
				for ch := 0; ch < chs; ch++ {
					if oriMat.Type()&gocv.MatTypeCV8U == gocv.MatTypeCV8U {
						region.SetUCharAt(r, c*chs+ch, oriMat.GetUCharAt(r, c*chs+ch))
					} else {
						//TODO
						panic("不支持的类型")
					}
				}
			}
		}
	}
	IMShow(region, "裁剪图")
}

func IMShow(mat gocv.Mat, title string) {
	mat = mat.Clone()
	window := gocv.NewWindow(title)
	defer window.Close()
	for {
		window.IMShow(mat)

		if window.WaitKey(10) >= 0 {
			break
		}
	}
}

func inCirle(r, rx, ry, x, y int) bool {
	// 矩形快速排除
	if x < (r-rx) || x > (r+rx) {
		return false
	}
	if y < (r-ry) || y > (r+ry) {
		return false
	}
	// 圆公式
	x0 := x - rx
	y0 := y - ry
	if (x0*x0 + y0*y0) > r*r {
		return false
	}
	return true
}
