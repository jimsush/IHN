package com.ihn.ihnandroid.beacon;

/**
 * Created by tong on 2015/10/28.
 */
public class NearestDistance {

    // p1: (a, b), p2 (c, d)
    // p3-p1: m,  p3-p2: n
    // (a-x)^2 + (b-y)^2 = m^2
    // (c-x)^2 + (d-y)^2 = n^2
    //
    // delta= b^2-4ac
    // x=-b+sqrt(b^2-4ac)/2a
    // y=
    public static void main(String[] args){
        System.out.println(getP3(0,0,8,8,8,8));
        System.out.println(getP3(1,1,3,4,5,Math.sqrt(10)));
        System.out.println(getP3(1,1,3,4,1,Math.sqrt(10)));
    }

    public static PositionResult getP3(double a, double b, double c, double d, double m, double n){
        double A=getA(a,b,c,d,m,n);
        double B=getB(a,b,c,d);

        double bb=2*(b*B-a-A*B);
        double aa=1+B*B;
        double cc=a*a+b*b+A*A-2*b*A-m*m;
        double delta=bb * bb - 4*aa*cc;
        PositionResult result=null;
        if(Double.isNaN(delta) || delta<0){
            return new PositionResult(0);
        }else if(delta<=0.01){
            result=new PositionResult(1);
        }else{
            result=new PositionResult(2);
        }

        result.p1x=(-bb+Math.sqrt(delta))/(2*aa);
        result.p1y=A-B*result.p1x;
        //System.out.println("x1:"+result.p1x+",y1:"+result.p1y);
        if(result.valueNum<=1) {
            return result;
        }

        result.p2x=(-bb-Math.sqrt(delta))/(2*aa);
        result.p2y=A-B*result.p2x;
        //System.out.println("x2:"+result.p2x+",y2:"+result.p2y);
        return result;
    }

    private static double getA(double a, double b, double c, double d, double m, double n){
        return (m*m - n*n + c*c - a*a + d*d - b*b)/(2*(d-b));
    }

    private static double getB(double a, double b, double c, double d){
        return (c-a)/(d-b);
    }

}

class PositionResult{
    int valueNum;
    double p1x;
    double p1y;
    double p2x;
    double p2y;

    public PositionResult(int valueNum){
        this.valueNum=valueNum;
    }

    public int getValueNum(){
        return this.valueNum;
    }

    @Override
    public String toString(){
        if(valueNum==0){
            return "N";
        }else if(valueNum==1){
            return "("+p1x+", "+p1y+")";
        }else if(valueNum==2){
            return "("+p1x+", "+p1y+")" + ", ("+p2x+", "+p2y+")";
        }
        return null;
    }
}