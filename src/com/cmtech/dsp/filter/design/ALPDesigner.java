package com.cmtech.dsp.filter.design;

import java.util.HashMap;
import java.util.Map;

import com.cmtech.dsp.filter.AnalogFilter;
import com.cmtech.dsp.filter.FIRFilter;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.SeqUtil;

import static java.lang.Math.*;

public class ALPDesigner {
	//最小数值 
	private static final double EPS = 2.220446049250313E-016;
	
	private ALPDesigner() {			
		}
	
	public static AnalogFilter design(AFSpec spec) {
		Map rtn = new HashMap();
		rtn = DesignAnalogLowPassFilter(spec.wp[0], spec.ws[0], spec.Rp, spec.As, spec.afType);
		
		RealSeq bs = (RealSeq)rtn.get("bs");
		RealSeq as = (RealSeq)rtn.get("as");
		
		AnalogFilter filter = new AnalogFilter(bs, as);
		filter.setSpec(spec);
		return null;
	}
	
	//设计模拟低通滤波器
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//afType：模拟滤波器的类型
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组 
	public static Map DesignAnalogLowPassFilter(double Qp, double Qs, double Rp, double As, AFType afType)//, RealSeq * pBs, RealSeq * pAs)
	{
	    switch(afType)
	    {
	    case BUTT:
	        return DesignAnalogButter1(Qp, Qs, Rp, As);//, pBs, pAs);
	        //break;
	    case CHEB1:
	        return DesignAnalogCheby11(Qp, Qs, Rp, As);//, pBs, pAs);
	        //break; 
	    case CHEB2:
	        return DesignAnalogCheby21(Qp, Qs, Rp, As);//, pBs, pAs);
	        //break;
	    case ELLIP:
	    		return DesignAnalogEllip1(Qp, Qs, Rp, As);//, pBs, pAs);
	        //break;  
	    default:
	    		return null;
	        //break;            
	    }
	}

	//用设计指标设计Butterworth模拟低通滤波器
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map DesignAnalogButter1(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
	    Map tmp = GetButterMagHPara(Qp, Qs, Rp, As);//, &N, &Qc);
	    int N = (int)tmp.get("N");
	    double Qc = (double)tmp.get("Qc");
	    return DesignAnalogButterWithPara(N, Qc);//, pBs, pAs);
	    //printf("Analog Butt: N = %d, Qc = %f\n", N, Qc);
	}

	//用直接指定滤波器阶数来设计Butterworth模拟低通滤波器 
	//N：模拟滤波器的阶数
	//Qc：模拟滤波器的截止频率，对于Butterworth指3dB截止频率
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组 
	private static Map DesignAnalogButter2(int N, double Qc)//, RealSeq * pBs, RealSeq * pAs)
	{
	    return DesignAnalogButterWithPara(N, Qc);//, pBs, pAs);
	} 

	//用模拟滤波器的设计规格获取Butterworth滤波器幅度响应函数中的参数，阶数N，截止频率Qc（3dB截止频率）
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//pN：返回的模拟滤波器的阶数
	//pQc：返回的模拟滤波器的截止频率
	private static Map GetButterMagHPara(double Qp, double Qs, double Rp, double As)//, int * pN, double * pQc)
	{
	    double tmp1 = pow(10, Rp/10) - 1;
	    double tmp2 = pow(10, As/10) - 1;
	    double lambda = Qs/Qp;                          //7.5.25a
	    double g = sqrt(tmp2/tmp1);                     //7.5.25b
	    int N = (int)( log10(g)/log10(lambda) ) + 1;      //7.5.26a
	    
	    double Qc1 = Qp/pow( tmp1, 1.0/2/N );       //7.5.27a
	    double Qc2 = Qs/pow( tmp2, 1.0/2/N );       //7.5.27b
	    double Qc= (Qc1 + Qc2)/2;                            //7.5.27c
	    Map rtn = new HashMap();
	    rtn.put("N", N);
	    rtn.put("Qc", Qc);
	    return rtn;
	}

	//用幅度响应函数的参数设计Butterworth模拟低通滤波器
	//N：模拟滤波器的阶数
	//Qc：模拟滤波器的截止频率，对于Butterworth指3dB截止频率
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组 
	private static Map DesignAnalogButterWithPara(int N, double Qc)//, RealSeq * pBs, RealSeq * pAs)
	{
	    if( N <= 0) {return null;}
	    Map rtn = new HashMap();
	    
	    //生成Bm：Qc^N      //(7.5.8) 
	    //double * bm = (double *)malloc(1 * sizeof(double));    
	    //bm[0] = pow(Qc, N);
	    //pBs->pData = bm;
	    //pBs->len = 1;     
	    RealSeq bs = new RealSeq(1);
	    bs.set(0, pow(Qc, N));
	    
	    //生成一个一阶节：Qc/(s+Qc)         (7.5.9) 
	    //double * ak = null;
	    RealSeq as = null;
	    
	    if( N % 2 == 0 )    //N为偶数，没有一阶节 
	    {
	        //ak = (double *)malloc(1 * sizeof(double));
	        //ak[0] = 1.0;
	        //pAs->pData = ak;
	        //pAs->len = 1;
	        as = new RealSeq(1);
	        as.set(0, 1.0);
	    }
	    else                //N为奇数，生成一阶节的Ak 
	    {
	        //ak = (double *)malloc(2 * sizeof(double));
	        //ak[0] = 1.0; ak[1] = Qc;
	        //pAs->pData = ak;
	        //pAs->len = 2;
	        as = new RealSeq(1.0, Qc);
	    }    
	    
	    if( N == 1) {
	    		rtn.put("bs", bs);
	    		rtn.put("as", as);
	    		return rtn;     //一阶滤波器，直接返回
	    }
	    
	    int biN = N/2;  //二阶节的个数
	    
	    //生成一个二阶节：Qc^2/(s^2 - 2*Qc*cos( (0.5+(2k-1)/(2N))*PI )*s + Qc^2)的分母系数数组  //（7.5.9）
	    //double * biQ = (double *)malloc(3 * sizeof(double)); 
	    //biQ[0] = 1.0; biQ[1] = 0.0; biQ[2] = Qc*Qc;
	    //RealSeq biSeq = {0};
	    //biSeq.pData = biQ; biSeq.len = 3;
	    RealSeq biSeq = new RealSeq(1.0, 0.0, Qc*Qc);
	    
	    //RealSeq tmpSeq = null;
	    
	    //求分母多项式系数Ak        //(7.5.11)
	    int k = 0;
	    for(k = 1; k <= biN; k++)
	    {
	        biSeq.set(1, -2.0*Qc*cos( 0.5*PI*(1.0+(2.0*k-1.0)/N) ));
	        //tmpSeq = Conv(*pAs, biSeq);
	        //FreeSeq(*pAs);
	        //pAs->pData = tmpSeq.pData;
	        //pAs->len = tmpSeq.len;
	        as = SeqUtil.conv(as, biSeq);
	    }
	    //FreeSeq(biSeq);
		rtn.put("bs", bs);
		rtn.put("as", as);
		return rtn;    
	}


	//用设计指标设计Chebyshev-I型模拟低通滤波器
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map DesignAnalogCheby11(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
	    Map tmp = GetCheby1MagHPara(Qp, Qs, Rp, As);//, &N, &Qc, &E);
	    int N = (int)tmp.get("N");
	    double Qc = (double)tmp.get("Qc");
	    double E = (double)tmp.get("E");
	    return DesignAnalogCheby1WithPara(N, Qc, E);//, pBs, pAs); 
	    //printf("Analog Cheb1: N = %d, Qc = %f, E = %f\n", N, Qc, E);   
	}

	//直接指定滤波器阶数来设计Chebyshev-I型模拟低通滤波器
	//N：模拟滤波器的阶数
	//Qp：模拟滤波器的通带截止频率Qp
	//Rp：模拟滤波器的通带衰减 
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map DesignAnalogCheby12(int N, double Qp, double Rp)//, RealSeq * pBs, RealSeq * pAs)
	{
	    double E = sqrt( pow(10, Rp/10) - 1 );           //7.5.58
	    return DesignAnalogCheby1WithPara(N, Qp, E);//, pBs, pAs);
	}

	//用模拟滤波器的设计规格获取Chebyshev-I型滤波器幅度响应函数中的参数，阶数N，截止频率Qc（通带截止频率），通带波纹epsilon 
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//pN：返回的模拟滤波器的阶数
	//pQc：返回的模拟滤波器的截止频率
	//pE：返回的模拟滤波器的通带波纹
	private static Map GetCheby1MagHPara(double Qp, double Qs, double Rp, double As)//, int * pN, double * pQc, double * pE)
	{
	    double Qc = Qp;    
	    double E = sqrt( pow(10, Rp/10) - 1 );           //7.5.58
	    
	    double tmp1 = pow(10, Rp/10) - 1;
	    double tmp2 = pow(10, As/10) - 1;
	    double lbd = Qs/Qp;                             //7.5.25a
	    double g = sqrt(tmp2/tmp1);                     //7.5.25b
	    int N = (int)( log10(g+sqrt(g*g-1))/log10(lbd+sqrt(lbd*lbd-1)) ) + 1;      //7.5.68
	    
	    Map rtn = new HashMap();
	    rtn.put("N", N);
	    rtn.put("Qc", Qc);
	    rtn.put("E", E);
	    return rtn; 
	}

	//用幅度响应函数的参数设计Chebyshev-I型模拟低通滤波器
	//N：模拟滤波器的阶数
	//Qc：模拟滤波器的截止频率，对于Chebyshev-I型指通带截止频率Qp
	//E：模拟滤波器的通带波纹 
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map DesignAnalogCheby1WithPara(int N, double Qc, double E)//, RealSeq * pBs, RealSeq * pAs)
	{
	    if( N <= 0) {return null;}
	    Map rtn = new HashMap();
	    
	    //生成Bm：Qc^N/(E*2^(N-1))      //(7.5.47) 
	    //double * bm = (double *)malloc(1 * sizeof(double));    
	    //bm[0] = pow(Qc, N)/E/pow(2, N-1);
	    //pBs->pData = bm;
	    //pBs->len = 1;    
	    RealSeq bs = new RealSeq(1);
	    bs.set(0, pow(Qc, N)/E/pow(2, N-1));
	    
	    
	    double gama = 1.0/E + sqrt(1.0/E/E+1);  //(7.5.44)
	    double tmp = pow(gama, 1.0/N);
	    double a = (tmp - 1/tmp)/2;     //(7.5.43a)
	    double b = (tmp + 1/tmp)/2;     //(7.5.43b)   
	     
	    //生成一个一阶节分母多项式：(s+Qc*a)         
	    //double * ak = null;
	    RealSeq as = null;
	    
	    if( N % 2 == 0 )    //N为偶数，没有一阶节 
	    {
	        //ak = (double *)malloc(1 * sizeof(double));
	        //ak[0] = 1.0;
	        //pAs->pData = ak;
	        //pAs->len = 1;
	        as = new RealSeq(1);
	        as.set(0, 1.0);
	    }
	    else                //N为奇数，生成一阶节的Ak 
	    {
	        //ak = (double *)malloc(2 * sizeof(double));
	        //ak[0] = 1.0; ak[1] = Qc*a;
	        //pAs->pData = ak;
	        //pAs->len = 2;
	        as = new RealSeq(1.0, Qc*a);
	    }    
	    
	    if( N == 1) {
	    		rtn.put("bs", bs);
	    		rtn.put("as", as);
	    		return rtn;     //一阶滤波器，直接返回
	    }
	    
	    int biN = N/2;  //二阶节的个数
	    
	    //生成一个二阶节分母多项式：s^2 - 2*Re[s_k]*s + |s_k|^2)系数数组  //这个需要自己推导一下了 
	    //double * biQ = (double *)malloc(3 * sizeof(double)); 
	    //biQ[0] = 1.0; biQ[1] = 0.0; biQ[2] = 0.0;
	    //RealSeq biSeq = {0};
	    //biSeq.pData = biQ; biSeq.len = 3;
	    RealSeq biSeq = new RealSeq(1.0, 0.0, 0.0);
	    
	    RealSeq tmpSeq = null;

	    double re = 0.0;
	    double im = 0.0;
	    
	    //求分母多项式系数Ak 
	    int k = 0;
	    for(k = 1; k <= biN; k++)
	    {
	        re = -Qc*a*sin(PI*(2.0*k-1)/2.0/N);     //(7.5.41)
	        im = Qc*b*cos(PI*(2.0*k-1)/2.0/N);      //(7.5.41)
	        
	        //biSeq.pData[1] = -2.0*re;
	        //biSeq.pData[2] = re*re+im*im;
	        biSeq.set(1, -2.0*re);
	        biSeq.set(2, re*re+im*im);
	        //tmpSeq = Conv(*pAs, biSeq);
	        //FreeSeq(*pAs);
	        //pAs->pData = tmpSeq.pData;
	        //pAs->len = tmpSeq.len;
	        as = SeqUtil.conv(as, biSeq);
	    }
	    //FreeSeq(biSeq);
	     
		rtn.put("bs", bs);
		rtn.put("as", as);
		return rtn;     //一阶滤波器，直接返回   
	}

	//用设计指标设计Chebyshev-II型模拟低通滤波器
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map DesignAnalogCheby21(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
	    Map tmp = GetCheby2MagHPara(Qp, Qs, Rp, As);//, &N, &Qc, &E);
	    int N = (int)tmp.get("N");
	    double Qc = (double)tmp.get("Qc");
	    double E = (double)tmp.get("E");
	    return DesignAnalogCheby2WithPara(N, Qc, E);//, pBs, pAs);  
	    //printf("Analog Cheb2: N = %d, Qc = %f, E = %f\n", N, Qc, E);      
	}

	//直接指定滤波器阶数来设计Chebyshev-II型模拟低通滤波器
	//N：模拟滤波器的阶数
	//Qs：模拟滤波器的阻带截止频率
	//As：模拟滤波器的阻带衰减 
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map DesignAnalogCheby22(int N, double Qs, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
	    double E = 1.0/sqrt( pow(10, As/10) - 1 );           //7.5.75
	    return DesignAnalogCheby2WithPara(N, Qs, E);//, pBs, pAs); 
	}

	//用模拟滤波器的设计规格获取Chebyshev-II型滤波器幅度响应函数中的参数，阶数N，截止频率Qc（阻带截止频率），通带波纹epsilon 
	//Qp：通带截止频率
	//Qs：阻带截止频率
	//Rp：通带最大衰减
	//As：阻带最小衰减
	//pN：返回的模拟滤波器的阶数
	//pQc：返回的模拟滤波器的截止频率
	//pE：返回的模拟滤波器的通带波纹
	private static Map GetCheby2MagHPara(double Qp, double Qs, double Rp, double As)//, int * pN, double * pQc, double * pE)
	{
	    double Qc = Qs;    
	    double E = 1.0/sqrt( pow(10, As/10) - 1 );           //7.5.75
	    
	    double tmp1 = pow(10, Rp/10) - 1;
	    double tmp2 = pow(10, As/10) - 1;
	    double lbd = Qs/Qp;                             //7.5.25a
	    double g = sqrt(tmp2/tmp1);                     //7.5.25b
	    int N = (int)( log10(g+sqrt(g*g-1))/log10(lbd+sqrt(lbd*lbd-1)) ) + 1;      //7.5.77
	    
	    Map rtn = new HashMap();
	    rtn.put("N", N);
	    rtn.put("Qc", Qc);
	    rtn.put("E", E);
	    return rtn;    
	}

	//用幅度响应函数的参数设计Chebyshev-II型模拟低通滤波器
	//N：模拟滤波器的阶数
	//Qc：模拟滤波器的截止频率，对于Chebyshev-II型指阻带截止频率Qp
	//E：模拟滤波器的通带波纹 
	//pBs：返回的模拟滤波器的系统函数Ha(s)分子多项式系数数组
	//pAs：返回的模拟滤波器的系统函数Ha(s)分母多项式系数数组
	private static Map DesignAnalogCheby2WithPara(int N, double Qc, double E)//, RealSeq * pBs, RealSeq * pAs)
	{
		Map rtn = new HashMap();
	    if( N <= 0) {return null;}
	    
	    double gama = 1.0/E + sqrt(1.0/E/E+1);  //(7.5.44)
	    double tmp = pow(gama, 1.0/N);
	    double a = (tmp - 1/tmp)/2;     //(7.5.43a)
	    double b = (tmp + 1/tmp)/2;     //(7.5.43b)       
	    
	    //生成Bm：
	    //double * bm = (double *)malloc(1 * sizeof(double));    
	    //bm[0] = 1.0;
	    //pBs->pData = bm;
	    //pBs->len = 1;      
	    RealSeq bs = new RealSeq(1);
	    bs.set(0, 1.0);
	     
	    //生成一个一阶节：Qc/(s+Qc)         
	    //double * ak = null;
	    RealSeq as = null;
	    
	    if( N % 2 == 0 )    //N为偶数，没有一阶节 
	    {
	        //ak = (double *)malloc(1 * sizeof(double));
	        //ak[0] = 1.0;
	        //pAs->pData = ak;
	        //pAs->len = 1;
	        as = new RealSeq(1);
	        as.set(0, 1.0);
	    }
	    else                //N为奇数，生成一阶节的Ak 
	    {
	        //ak = (double *)malloc(2 * sizeof(double));
	        //ak[0] = 1.0; ak[1] = Qc/a;
	        //pAs->pData = ak;
	        //pAs->len = 2;
	        as = new RealSeq(1.0, Qc/a);
	    }    
	    
	    if( N == 1) {
	    		bs.set(0, as.get(1));
	    		rtn.put("bs", bs);
	    		rtn.put("as", as);
	    		//bm[0] = ak[1]; 
	    		return rtn;
	    	}     //一阶滤波器，直接返回
	    
	    int biN = N/2;  //二阶节的个数
	    
	    //生成一个二阶节分子多项式，零点见(7.5.81)
	    //double * biNom = (double *)malloc(3 * sizeof(double));
	    //biNom[0] = 1.0; biNom[1] = 0.0; biNom[2] = 0.0;
	    //RealSeq biNomSeq = {0};
	    //biNomSeq.pData = biNom; biNomSeq.len = 3;
	    RealSeq biNomSeq = new RealSeq(1.0, 0.0, 0.0);
	    
	    //生成一个二阶节分母多项式，极点见(7.5.79)     
	    //double * biDen = (double *)malloc(3 * sizeof(double)); 
	    //biDen[0] = 1.0; biDen[1] = 0.0; biDen[2] = 0.0;
	    //RealSeq biDenSeq = {0};
	    //biDenSeq.pData = biDen; biDenSeq.len = 3;
	    RealSeq biDenSeq = new RealSeq(1.0, 0.0, 0.0);
	    
	    //RealSeq tmpNomSeq = null;   
	    //RealSeq tmpDenSeq = null;

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
	        
	        //tmpNomSeq = Conv(*pBs, biNomSeq);
	        //tmpDenSeq = Conv(*pAs, biDenSeq);
	        
	        //FreeSeq(*pBs);        
	        //FreeSeq(*pAs);
	        
	        //pBs->pData = tmpNomSeq.pData; pBs->len = tmpNomSeq.len;
	        //pAs->pData = tmpDenSeq.pData; pAs->len = tmpDenSeq.len;
	        
	        bs = SeqUtil.conv(bs, biNomSeq);
	        as = SeqUtil.conv(as, biDenSeq);
	    }
	    //FreeSeq(biNomSeq);
	    //FreeSeq(biDenSeq);
	    
	    //下面归一化，保证|H(j0)|=1
	    int i = 0;
	    //double factor = pAs->pData[pAs->len-1]/pBs->pData[pBs->len-1];   //分母的常数项除以分子的常数项 
	    double factor = as.get(as.size()-1)/bs.get(bs.size()-1);
	    
	    bs = bs.multiple(factor);
	    
	    /*for(i = 0; i < pBs->len; i++)
	    {
	        pBs->pData[i] *= factor;
	    } */
	    rtn.put("bs", bs);
		rtn.put("as", as);
		return rtn;      
	}

	//用设计指标设计Elliptic模拟低通滤波器 
	private static Map DesignAnalogEllip1(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{

		Map tmp = new HashMap();
		tmp = GetEllipOrder(Qp, Qs, Rp, As);//, &N, &ActuralAs);
		int N = (int)tmp.get("N");
		double ActuralAs = (double)tmp.get("ActuralAs");
		//printf("Analog Ellipti: N = %d, 实际达到的As = %lf\n", N, ActuralAs);
		return DesignAnalogEllip2(N, Qp, Qs, Rp);//, pBs, pAs);
	}

	//直接指定滤波器阶数来设计Elliptic模拟低通滤波器 
	private static Map DesignAnalogEllip2(int N, double Qp, double Qs, double Rp)//, RealSeq * pBs, RealSeq * pAs)
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
		//RealSeq Xi = CreateSeq(0);
		//RealSeq Yi = CreateSeq(0);
		Map XiYi = CalculateXandYForEllip(N, q, k);//, &Xi, &Yi);		//(5.15)和(5.16) 
		RealSeq Xi = (RealSeq)XiYi.get("Xi");
		RealSeq Yi = (RealSeq)XiYi.get("Yi");
		
		RealSeq ai = new RealSeq(r);
		RealSeq bi = new RealSeq(r);
		RealSeq ci = new RealSeq(r);
		double X = 0.0;
		double Y = 0.0;
		int i = 0;
		for(i = 0; i< r; i++)
		{
			X = Xi.get(i);//pData[i];
			Y = Yi.get(i);//pData[i];
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
		/*RealSeq rltSeq = CreateSeq(0);
		rltSeq = MultipleConstant(ai, alpha*alpha);
		FreeSeq(ai);
		ai.pData = rltSeq.pData;
		ai.len = rltSeq.len;*/
		ai = ai.multiple(alpha*alpha);
		
		//第二项 
		/*rltSeq = CreateSeq(0);
		rltSeq = MultipleConstant(ci, alpha*alpha);
		FreeSeq(ci);
		ci.pData = rltSeq.pData;
		ci.len = rltSeq.len;*/
		ci = ci.multiple(alpha*alpha);
		
		//第三项 
		/*rltSeq = CreateSeq(0);
		rltSeq = MultipleConstant(bi, alpha);
		FreeSeq(bi);
		bi.pData = rltSeq.pData;
		bi.len = rltSeq.len;	*/
		bi = bi.multiple(alpha);
		
		
		//*pBs = CreateSeq(1);	
		RealSeq bs = new RealSeq(1);
		RealSeq as = null;
		
		//根据(5.22)中K的说明 
		if(N % 2 == 0)
		{
			//pBs->pData[0] = H0;		
			//*pAs = CreateSeq(1);
			//pAs->pData[0] = 1.0;
			bs.set(0, H0);
			as = new RealSeq(1);
			as.set(0, 1.0);
		}
		else
		{
			//pBs->pData[0] = H0*alpha;		//第四项 
			//*pAs = CreateSeq(2);
			//pAs->pData[0] = 1.0; pAs->pData[1] = p0*alpha;		//第五项 
			bs.set(0, H0*alpha);
			as = new RealSeq(1.0, p0*alpha);
		}
		
		//RealSeq NomSeq = CreateSeq(3);
		//NomSeq.pData[0] = 1.0; NomSeq.pData[1] = 0.0;
		//RealSeq DenSeq = CreateSeq(3);
		//DenSeq.pData[0] = 1.0;
		//RealSeq tmpSeq = CreateSeq(0);
		RealSeq NomSeq = new RealSeq(1.0, 0.0, 0.0);
		RealSeq DenSeq = new RealSeq(1.0, 0.0, 0.0);
		//RealSeq tmpSeq;
		for(i = 0; i < r; i++)
		{
			//NomSeq.pData[2] = ai.pData[i];
			//tmpSeq = Conv(*pBs, NomSeq);
			//FreeSeq(*pBs);
			//pBs->pData = tmpSeq.pData;
			//pBs->len = tmpSeq.len;
			NomSeq.set(2, ai.get(i));
			bs = SeqUtil.conv(bs, NomSeq);
			
			//DenSeq.pData[1] = bi.pData[i]; DenSeq.pData[2] = ci.pData[i];
			//tmpSeq = Conv(*pAs, DenSeq);
			//FreeSeq(*pAs);
			//pAs->pData = tmpSeq.pData;
			//pAs->len = tmpSeq.len;	
			DenSeq.set(1, bi.get(i));
			DenSeq.set(2, ci.get(i));
			as = SeqUtil.conv(as, DenSeq);
		}
		//FreeSeq(NomSeq);
		//FreeSeq(DenSeq);
		//千万不能释放tmpSeq，害得我多好了一天的时间 
		Map rtn = new HashMap();
		rtn.put("bs", bs);
		rtn.put("as", as);
		return rtn;
	}

	//用模拟滤波器的设计规格获取Elliptic型滤波器的阶数N和实际达到的阻带最小衰减As
	private static Map GetEllipOrder(double Qp, double Qs, double Rp, double As)//, int * pN, double * pActuralAs)
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
		
		Map rtn = new HashMap();
		rtn.put("N", N);
		rtn.put("ActuralAs", ActuralAs);
		return rtn;
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
	private static Map CalculateXandYForEllip(int N, double q, double k)//, RealSeq * pXi, RealSeq * pYi)
	{
		//确定二阶节个数
		int r = (N % 2 == 0) ? N/2 : (N-1)/2;
			
		//*pXi = CreateSeq(r);
		//*pYi = CreateSeq(r);
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
			//pXi->pData[i-1] = rlt;
			//pYi->pData[i-1] = sqrt( (1-rlt*rlt/k)*(1-k*rlt*rlt) );		//(5.16)
		}		
		Map rtn = new HashMap();
		rtn.put("Xi", Xi);
		rtn.put("Yi", Yi);
		return rtn;
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
