package com.cmtech.dsp.filter.design;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.cosh;
import static java.lang.Math.log;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sinh;
import static java.lang.Math.sqrt;

import java.util.HashMap;
import java.util.Map;

import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.util.SeqUtil;
import com.cmtech.dsp.util.TwoTuple;

public class ALPDesigner {
	//最小数值 
	private static final double EPS = 2.220446049250313E-016;
	
	private ALPDesigner() {			
	}
	
	/*public static AnalogFilter design(double Qp, double Qs, double Rp, double As, AFType afType){
		Map<String, Object> rtnMap = DesignAnalogLowPassFilter(Qp, Qs, Rp, As, afType);
		
		RealSeq bs = (RealSeq)rtnMap.get("BS");
		RealSeq as = (RealSeq)rtnMap.get("AS");
		
		AnalogFilter filter = new AnalogFilter(bs, as);
		AFPara para = new AFPara(new double[] {Qp,0.0}, new double[] {Qs,0.0},
						Rp, As, FilterType.LOWPASS, afType);
		filter.setFilterPara(para);
		return filter;
	}*/
	
	//设计模拟低通滤波器
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//afType：模拟滤波器的类型
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组 
	public static Map<String, Object> DesignAnalogLowPassFilter(double Qp, double Qs, double Rp, double As, AFType afType)//, RealSeq * pBs, RealSeq * pAs)
	{
	    switch(afType)
	    {
	    case BUTT:
	        return DesignAnalogButter1(Qp, Qs, Rp, As);
	    case CHEB1:
	        return DesignAnalogCheby11(Qp, Qs, Rp, As);
	    case CHEB2:
	        return DesignAnalogCheby21(Qp, Qs, Rp, As);
	    case ELLIP:
	    		return DesignAnalogEllip1(Qp, Qs, Rp, As);
	    default:
	    		return null;          
	    }
	}

	//用设计指标设计Butterworth模拟低通滤波器
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map<String, Object> DesignAnalogButter1(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
		Map<String, Object> tmpMap = GetButterMagHPara(Qp, Qs, Rp, As);
	    int N = (int)tmpMap.get("N");
	    double Qc = (double)tmpMap.get("QC");
	    return DesignAnalogButterWithPara(N, Qc);
	    //printf("Analog Butt: N = %d, Qc = %f\n", N, Qc);
	}

	//用直接指定滤波器阶数来设计Butterworth模拟低通滤波器 
	//N：模拟滤波器的阶数
	//Qc：模拟滤波器的截止频率，对于Butterworth指3dB截止频率
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组 
	private static Map<String, Object> DesignAnalogButter2(int N, double Qc)//, RealSeq * pBs, RealSeq * pAs)
	{
	    return DesignAnalogButterWithPara(N, Qc);
	} 

	//用模拟滤波器的设计规格获取Butterworth滤波器幅度响应函数中的参数，阶数N，截止频率Qc（3dB截止频率）
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//pN：返回的模拟滤波器的阶数
	//pQc：返回的模拟滤波器的截止频率
	private static Map<String, Object> GetButterMagHPara(double Qp, double Qs, double Rp, double As)//, int * pN, double * pQc)
	{
	    double tmp1 = pow(10, Rp/10) - 1;
	    double tmp2 = pow(10, As/10) - 1;
	    double lambda = Qs/Qp;                          //7.5.25a
	    double g = sqrt(tmp2/tmp1);                     //7.5.25b
	    int N = (int)( log10(g)/log10(lambda) ) + 1;      //7.5.26a
	    
	    double Qc1 = Qp/pow( tmp1, 1.0/2/N );       //7.5.27a
	    double Qc2 = Qs/pow( tmp2, 1.0/2/N );       //7.5.27b
	    double Qc= (Qc1 + Qc2)/2;                            //7.5.27c
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("N", N);
	    rtnMap.put("QC", Qc);
	    return rtnMap;
	}

	//用幅度响应函数的参数设计Butterworth模拟低通滤波器
	//N：模拟滤波器的阶数
	//Qc：模拟滤波器的截止频率，对于Butterworth指3dB截止频率
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组 
	private static Map<String, Object> DesignAnalogButterWithPara(int N, double Qc)//, RealSeq * pBs, RealSeq * pAs)
	{
	    if( N <= 0) {return null;}
	    
	    //生成Bm：Qc^N      //(7.5.8)    
	    RealSeq bs = new RealSeq(pow(Qc, N));
	    
	    //生成一个一阶节：Qc/(s+Qc)         (7.5.9) 
	    RealSeq as = null;
	    
	    if( N % 2 == 0 )    //N为偶数，没有一阶节 
	    {
	        as = new RealSeq(1.0);
	    }
	    else                //N为奇数，生成一阶节的Ak 
	    {
	        as = new RealSeq(1.0, Qc);
	    }    
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    if( N == 1) {
	    		rtnMap.put("BS", bs);
	    		rtnMap.put("AS", as);
	    		return rtnMap;     //一阶滤波器，直接返回
	    }
	    
	    int biN = N/2;  //二阶节的个数
	    
	    //生成一个二阶节：Qc^2/(s^2 - 2*Qc*cos( (0.5+(2k-1)/(2N))*PI )*s + Qc^2)的分母系数数组  //（7.5.9）
	    RealSeq biSeq = new RealSeq(1.0, 0.0, Qc*Qc);
	    
	    //求分母多项式系数Ak        //(7.5.11)
	    int k = 0;
	    for(k = 1; k <= biN; k++)
	    {
	        biSeq.set(1, -2.0*Qc*cos( 0.5*PI*(1.0+(2.0*k-1.0)/N) ));
	        as = (RealSeq) SeqUtil.conv(as, biSeq);
	    }
		rtnMap.put("BS", bs);
		rtnMap.put("AS", as);
		return rtnMap;    
	}


	//用设计指标设计Chebyshev-I型模拟低通滤波器
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map<String, Object> DesignAnalogCheby11(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
		Map<String, Object> tmpMap = GetCheby1MagHPara(Qp, Qs, Rp, As);
	    int N = (int)tmpMap.get("N");
	    double Qc = (double)tmpMap.get("QC");
	    double E = (double)tmpMap.get("E");
	    return DesignAnalogCheby1WithPara(N, Qc, E);
	    //printf("Analog Cheb1: N = %d, Qc = %f, E = %f\n", N, Qc, E);   
	}

	//直接指定滤波器阶数来设计Chebyshev-I型模拟低通滤波器
	//N：模拟滤波器的阶数
	//Qp：模拟滤波器的通带截止频率Qp
	//Rp：模拟滤波器的通带衰减 
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map<String, Object> DesignAnalogCheby12(int N, double Qp, double Rp)//, RealSeq * pBs, RealSeq * pAs)
	{
	    double E = sqrt( pow(10, Rp/10) - 1 );           //7.5.58
	    return DesignAnalogCheby1WithPara(N, Qp, E);
	}

	//用模拟滤波器的设计规格获取Chebyshev-I型滤波器幅度响应函数中的参数，阶数N，截止频率Qc（通带截止频率），通带波纹epsilon 
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//pN：返回的模拟滤波器的阶数
	//pQc：返回的模拟滤波器的截止频率
	//pE：返回的模拟滤波器的通带波纹
	private static Map<String, Object> GetCheby1MagHPara(double Qp, double Qs, double Rp, double As)//, int * pN, double * pQc, double * pE)
	{
	    double Qc = Qp;    
	    double E = sqrt( pow(10, Rp/10) - 1 );           //7.5.58
	    
	    double tmp1 = pow(10, Rp/10) - 1;
	    double tmp2 = pow(10, As/10) - 1;
	    double lbd = Qs/Qp;                             //7.5.25a
	    double g = sqrt(tmp2/tmp1);                     //7.5.25b
	    int N = (int)( log10(g+sqrt(g*g-1))/log10(lbd+sqrt(lbd*lbd-1)) ) + 1;      //7.5.68
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("N", N);
	    rtnMap.put("QC", Qc);
	    rtnMap.put("E", E);
	    return rtnMap; 
	}

	//用幅度响应函数的参数设计Chebyshev-I型模拟低通滤波器
	//N：模拟滤波器的阶数
	//Qc：模拟滤波器的截止频率，对于Chebyshev-I型指通带截止频率Qp
	//E：模拟滤波器的通带波纹 
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map<String, Object> DesignAnalogCheby1WithPara(int N, double Qc, double E)//, RealSeq * pBs, RealSeq * pAs)
	{
	    if( N <= 0) {return null;}

	    //生成Bm：Qc^N/(E*2^(N-1))      //(7.5.47)  
	    RealSeq bs = new RealSeq(pow(Qc, N)/E/pow(2, N-1));
	    
	    double gama = 1.0/E + sqrt(1.0/E/E+1);  //(7.5.44)
	    double tmp = pow(gama, 1.0/N);
	    double a = (tmp - 1/tmp)/2;     //(7.5.43a)
	    double b = (tmp + 1/tmp)/2;     //(7.5.43b)   
	     
	    //生成一个一阶节分母多项式：(s+Qc*a)  
	    RealSeq as = null;
	    
	    if( N % 2 == 0 )    //N为偶数，没有一阶节 
	    {
	        as = new RealSeq(1.0);
	    }
	    else                //N为奇数，生成一阶节的Ak 
	    {
	        as = new RealSeq(1.0, Qc*a);
	    }    
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    if( N == 1) {
	    		rtnMap.put("BS", bs);
	    		rtnMap.put("AS", as);
	    		return rtnMap;     //一阶滤波器，直接返回
	    }
	    
	    int biN = N/2;  //二阶节的个数
	    
	    //生成一个二阶节分母多项式：s^2 - 2*Re[s_k]*s + |s_k|^2)系数数组  //这个需要自己推导一下了 
	    RealSeq biSeq = new RealSeq(1.0, 0.0, 0.0);

	    double re = 0.0;
	    double im = 0.0;
	    
	    //求分母多项式系数Ak 
	    int k = 0;
	    for(k = 1; k <= biN; k++)
	    {
	        re = -Qc*a*sin(PI*(2.0*k-1)/2.0/N);     //(7.5.41)
	        im = Qc*b*cos(PI*(2.0*k-1)/2.0/N);      //(7.5.41)

	        biSeq.set(1, -2.0*re);
	        biSeq.set(2, re*re+im*im);
	        
	        as = (RealSeq) SeqUtil.conv(as, biSeq);
	    }
	     
		rtnMap.put("BS", bs);
		rtnMap.put("AS", as);
		return rtnMap;     //一阶滤波器，直接返回   
	}

	//用设计指标设计Chebyshev-II型模拟低通滤波器
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map<String, Object> DesignAnalogCheby21(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
		Map<String, Object> tmpMap = GetCheby2MagHPara(Qp, Qs, Rp, As);
	    int N = (int)tmpMap.get("N");
	    double Qc = (double)tmpMap.get("QC");
	    double E = (double)tmpMap.get("E");
	    return DesignAnalogCheby2WithPara(N, Qc, E);
	    //printf("Analog Cheb2: N = %d, Qc = %f, E = %f\n", N, Qc, E);      
	}

	//直接指定滤波器阶数来设计Chebyshev-II型模拟低通滤波器
	//N：模拟滤波器的阶数
	//Qs：模拟滤波器的阻带截止频率
	//As：模拟滤波器的阻带衰减 
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map<String, Object> DesignAnalogCheby22(int N, double Qs, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
	    double E = 1.0/sqrt( pow(10, As/10) - 1 );           //7.5.75
	    return DesignAnalogCheby2WithPara(N, Qs, E);
	}

	//用模拟滤波器的设计规格获取Chebyshev-II型滤波器幅度响应函数中的参数，阶数N，截止频率Qc（阻带截止频率），通带波纹epsilon 
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//pN：返回的模拟滤波器的阶数
	//pQc：返回的模拟滤波器的截止频率
	//pE：返回的模拟滤波器的通带波纹
	private static Map<String, Object> GetCheby2MagHPara(double Qp, double Qs, double Rp, double As)//, int * pN, double * pQc, double * pE)
	{
	    double Qc = Qs;    
	    double E = 1.0/sqrt( pow(10, As/10) - 1 );           //7.5.75
	    
	    double tmp1 = pow(10, Rp/10) - 1;
	    double tmp2 = pow(10, As/10) - 1;
	    double lbd = Qs/Qp;                             //7.5.25a
	    double g = sqrt(tmp2/tmp1);                     //7.5.25b
	    int N = (int)( log10(g+sqrt(g*g-1))/log10(lbd+sqrt(lbd*lbd-1)) ) + 1;      //7.5.77
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("N", N);
	    rtnMap.put("QC", Qc);
	    rtnMap.put("E", E);
	    return rtnMap;    
	}

	//用幅度响应函数的参数设计Chebyshev-II型模拟低通滤波器
	//N：模拟滤波器的阶数
	//Qc：模拟滤波器的截止频率，对于Chebyshev-II型指阻带截止频率Qp
	//E：模拟滤波器的通带波纹 
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map<String, Object> DesignAnalogCheby2WithPara(int N, double Qc, double E)//, RealSeq * pBs, RealSeq * pAs)
	{
		if( N <= 0) {return null;}
	    
	    double gama = 1.0/E + sqrt(1.0/E/E+1);  //(7.5.44)
	    double tmp = pow(gama, 1.0/N);
	    double a = (tmp - 1/tmp)/2;     //(7.5.43a)
	    double b = (tmp + 1/tmp)/2;     //(7.5.43b)       
	    
	    //生成Bm： 
	    RealSeq bs = new RealSeq(1.0);
	     
	    //生成一个一阶节：Qc/(s+Qc)         
	    RealSeq as = null;
	    
	    if( N % 2 == 0 )    //N为偶数，没有一阶节 
	    {
	        as = new RealSeq(1.0);
	    }
	    else                //N为奇数，生成一阶节的Ak 
	    {
	        as = new RealSeq(1.0, Qc/a);
	    }    
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    if( N == 1) {
	    		bs.set(0, as.get(1));
	    		rtnMap.put("BS", bs);
	    		rtnMap.put("AS", as);
	    		return rtnMap;
	    	}     //一阶滤波器，直接返回
	    
	    int biN = N/2;  //二阶节的个数
	    
	    //生成一个二阶节分子多项式，零点见(7.5.81)
	    RealSeq biNomSeq = new RealSeq(1.0, 0.0, 0.0);
	    
	    //生成一个二阶节分母多项式，极点见(7.5.79)     
	    RealSeq biDenSeq = new RealSeq(1.0, 0.0, 0.0);

	    double theta = 0.0;
	    double re = 0.0;
	    double im = 0.0;

	    double alpha = 0.0;
	    double beta = 0.0;
	    
	    //求分子多项式系数bm和分母多项式系数Ak 
	    int k = 0;
	    for(k = 1; k <= biN; k++)
	    {
	        theta = PI*(2.0*k-1)/2.0/N;
	        
	        biNomSeq.set(2, Qc*Qc/cos(theta)/cos(theta));        //分子多项式的最后一项为零点的幅度
	        
	        alpha = -a*sin(theta);  //(7.5.80a)
	        beta = b*cos(theta);    //(7.5.80b) 
	        
	        re = Qc*alpha/(alpha*alpha+beta*beta);       //(7.5.79b)
	        im = Qc*beta/(alpha*alpha+beta*beta);        //(7.5.79c)
	        
	        biDenSeq.set(1, -2.0*re);
	        biDenSeq.set(2, re*re+im*im);
	        
	        bs = (RealSeq) SeqUtil.conv(bs, biNomSeq);
	        as = (RealSeq) SeqUtil.conv(as, biDenSeq);
	    }
	    
	    //下面归一化，保证|H(j0)|=1
	    double factor = as.get(as.size()-1)/bs.get(bs.size()-1);
	    bs = (RealSeq) bs.multiple(factor);

	    rtnMap.put("BS", bs);
		rtnMap.put("AS", as);
		return rtnMap;      
	}

	//用设计指标设计Elliptic模拟低通滤波器 
	private static Map<String, Object> DesignAnalogEllip1(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
		Map<String, Object> tmpMap = GetEllipOrder(Qp, Qs, Rp, As);
		int N = (int)tmpMap.get("N");
		double ActuralAs = (double)tmpMap.get("ACTURALAS");
		//printf("Analog Ellipti: N = %d, 实际达到的As = %lf\n", N, ActuralAs);
		return DesignAnalogEllip2(N, Qp, Qs, Rp);
	}

	//直接指定滤波器阶数来设计Elliptic模拟低通滤波器 
	private static Map<String, Object> DesignAnalogEllip2(int N, double Qp, double Qs, double Rp)//, RealSeq * pBs, RealSeq * pAs)
	{
		//本函数前半部来自算法5.2，求归一化的椭圆型滤波器 
		 
		//计算选择性因子k 
		double k = Qp/Qs;
		
		//计算模数常量q 	
		double tmp = sqrt(sqrt(1-k*k));
		double u = (1-tmp)/2.0/(1+tmp);		//(5.11)
		double q = u + 2*pow(u, 5) + 15*pow(u, 9) + 150*pow(u, 13);		//(5.10)
		
		//计算V 
		tmp = pow(10.0, Rp/20);
		double V = log((tmp+1)/(tmp-1))	/ N / 2;		//(5.12)
		
		//计算p0 
		double p0 = CalculateP0ForEllip(q, V);		//(5.13)
		
		//计算W 
		double W = sqrt( (1.0+p0*p0/k)*(1+p0*p0*k) );		//(5.14)
		
		//确定二阶节个数
		int r = (N % 2 == 0) ? N/2 : (N-1)/2;
		
		//计算Xi和Yi 
		Map<String, Object> tmpMap = CalculateXandYForEllip(N, q, k);		//(5.15)和(5.16) 
		RealSeq Xi = (RealSeq)tmpMap.get("XI");
		RealSeq Yi = (RealSeq)tmpMap.get("YI");
		
		RealSeq ai = new RealSeq(r);
		RealSeq bi = new RealSeq(r);
		RealSeq ci = new RealSeq(r);
		double X = 0.0;
		double Y = 0.0;
		int i = 0;
		for(i = 0; i< r; i++)
		{
			X = Xi.get(i);
			Y = Yi.get(i);
			ai.set(i, 1.0/X/X);		//(5.17)
			bi.set(i, 2*p0*Y/(1+p0*p0*X*X));			//(5.18)
			ci.set(i, (p0*p0*Y*Y + W*W*X*X)/(1.0+p0*p0*X*X)/(1.0+p0*p0*X*X));	//(5.19)
		}
		
		//根据(5.20)计算H0 
		double H0 = (N % 2 == 0) ? pow(10.0, -Rp/20) : p0;
		for(i = 0; i < r; i++)
		{
			H0 *= (ci.get(i)/ai.get(i));
		}
		
		//下面做频率尺度化操作，见文献第9页 
		double alpha = sqrt(Qp*Qs);
		 
		//第一项 	
		ai = (RealSeq) ai.multiple(alpha*alpha);
		
		//第二项 
		ci = (RealSeq) ci.multiple(alpha*alpha);
		
		//第三项 
		bi = (RealSeq) bi.multiple(alpha);

		RealSeq bs = new RealSeq(1);
		RealSeq as = null;
		
		//根据(5.22)中K的说明 
		if(N % 2 == 0)
		{
			bs.set(0, H0);
			as = new RealSeq(1.0);
		}
		else
		{
			bs.set(0, H0*alpha);		//第四项
			as = new RealSeq(1.0, p0*alpha);		//第五项 
		}

		RealSeq NomSeq = new RealSeq(1.0, 0.0, 0.0);
		RealSeq DenSeq = new RealSeq(1.0, 0.0, 0.0);
		for(i = 0; i < r; i++)
		{
			NomSeq.set(2, ai.get(i));
			bs = (RealSeq) SeqUtil.conv(bs, NomSeq);
			
			DenSeq.set(1, bi.get(i));
			DenSeq.set(2, ci.get(i));
			as = (RealSeq) SeqUtil.conv(as, DenSeq);
		}

		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("BS", bs);
		rtnMap.put("AS", as);
		return rtnMap;
	}

	//用模拟滤波器的设计规格获取Elliptic型滤波器的阶数N和实际达到的阻带最小衰减As
	private static Map<String, Object> GetEllipOrder(double Qp, double Qs, double Rp, double As)//, int * pN, double * pActuralAs)
	{
		//本函数来自算法5.1
		 
		//计算选择性因子k 
		double k = Qp/Qs;
		
		//计算模数常量q 
		double tmp = sqrt(sqrt(1-k*k));
		double u = (1-tmp)/2.0/(1+tmp);		//(5.2)
		double q = u + 2*pow(u, 5) + 15*pow(u, 9) + 150*pow(u, 13);		//(5.1)
		
		//计算判别性因子D 
		double D = ( pow(10.0, As/10)-1 )/( pow(10.0, Rp/10)-1 );		//(5.3)
		
		//计算阶数n 
		double n = log10(16*D)/log10(1.0/q);	//(5.4)
		int N = (int)n+1;
		
		//计算实际达到的As 
		double ActuralAs = 10*log10( 1+(pow(10.0, Rp/10)-1)/16.0/pow(q, N) );		//(5.5)
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("N", N);
		rtnMap.put("ACTURALAS", ActuralAs);
		return rtnMap;
	}

	//实现算法5.2中公式(5.13)用于计算p0值 
	private static double CalculateP0ForEllip(double q, double V)
	{
		int m  = 0;
		double sum = 0.0;
		double term = 0.0;
		double nom = 0.0;
		double den = 0.0;
		int iternum = 10;
		for(m = 0; m < iternum; m++)
		{
			term = pow(-1.0, m)*pow(q, m*(m+1))*sinh((2*m+1)*V);
			sum += term;
		}
		nom = 2.0*sqrt(sqrt(q))*sum;	//我这里把分子和分母都乘以了2 
		sum = 0.0;
		for(m = 1; m < iternum; m++)
		{
			term = pow(-1.0, m)*pow(q, m*m)*cosh(2*m*V);
			sum += term;
		}
		den = 1.0+2*sum;		//分母也乘以2 
		return abs(nom/den);
	}

	//实现算法5.2中公式(5.15)用于计算Xi,Yi,i=1,2,...,r的值
	private static Map<String, Object> CalculateXandYForEllip(int N, double q, double k)//, RealSeq * pXi, RealSeq * pYi)
	{
		//确定二阶节个数
		int r = (N % 2 == 0) ? N/2 : (N-1)/2;

		RealSeq Xi = new RealSeq(r);
		RealSeq Yi = new RealSeq(r);
		
		int i = 0;
		int m = 0;
		double miu = 0.0;
		double term = 0.0;
		double sum = 0.0;
		double nom = 0.0;
		double den = 0.0;
		double rlt = 0.0;
		int iternum = 10;
		for(i = 1; i <= r; i++)
		{
			sum = 0.0;		
			miu = (N % 2 == 0) ? i-0.5 : i;
			for(m = 0; m < iternum; m++)
			{
				term = pow(-1.0, m) * pow(q, m*(m+1)) * sin((2*m+1)*miu*PI/N);
				sum += term;			
			}
			nom = 2.0*sqrt(sqrt(q))*sum;
			sum = 0.0;
			for(m = 1; m < iternum; m++)
			{
				term = pow(-1.0, m) * pow(q, m*m) * cos(2*m*miu*PI/N);
				sum += term;			
			}	
			den = 1.0+2*sum;
			rlt = nom/den;
			Xi.set(i-1, rlt);
			Yi.set(i-1, sqrt( (1-rlt*rlt/k)*(1-k*rlt*rlt) ));		//(5.16)
		}		
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("XI", Xi);
		rtnMap.put("YI", Yi);
		return rtnMap;
	}

	//用于计算第一类完全椭圆积分K(k) 
	//暂时没有用 
	private static double CalculateCompleteEllipIntegral(double k)
	{
		double a = 1-k;
		double b = 1+k;
		double tmp = 0.0;
		while(abs(a-b) > EPS)
		{
			tmp = (a+b)/2.0;
			b = sqrt(a*b);
			a = tmp;
		}
		return (PI/2.0/a);
	}


}
