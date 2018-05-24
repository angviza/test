package com.jk.fight.www.Utils;

public class Point3D {
    private int x;
    private int y;
    private int r;
    public Point3D(int x,int y,int r){
        this.x=x;
        this.y=y;
        this.r=r;
    }
    @Override
    public String toString() {
        StringBuffer x=new StringBuffer();
        x.append("x=");
        x.append(x);
        x.append(",");
        x.append("y=");
        x.append(y);
        x.append(",");
        x.append("r=");
        x.append(r);
        return x.toString();
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }
}
