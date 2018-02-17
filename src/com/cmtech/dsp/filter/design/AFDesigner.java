/**
 * Project Name:DSP_JAVA
 * File Name:AFDesigner.java
 * Package Name:com.cmtech.dsp.filter.design
 * Date:2018年2月17日下午5:13:57
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.filter.design;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

import java.util.HashMap;
import java.util.Map;

import com.cmtech.dsp.filter.AnalogFilter;
import com.cmtech.dsp.filter.para.AFPara;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.ZT;

/**
 * ClassName: AFDesigner
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月17日 下午5:13:57 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class AFDesigner {
	private AFDesigner() {
		
	}
	
	//根据设计规格设计各型模拟滤波器，用线频率 
	public static AnalogFilter design(double[] fp, double[] fs, double Rp, double As, AFType afType, FilterType fType)//, RealSeq * pBs, RealSeq * pAs)
	{
		double[] Qp = new double[2];
		double[] Qs = new double[2];
		//double Qp2[2] = {0.0};
		//double Qs2[2] = {0.0};
		
		if(fType == FilterType.LOWPASS || fType == FilterType.HIGHPASS)
		{
			Qp[0] = 2*PI*(fp[0]);
			Qs[0] = 2*PI*(fs[0]);
			//DesignAnalogFilter2(&Qp, &Qs, Rp, As, afType, fType, pBs, pAs);
		}
		else
		{
			Qp[0] = 2*PI*fp[0]; Qp[1] = 2*PI*fp[1];
			Qs[0] = 2*PI*fs[0]; Qs[1] = 2*PI*fs[1];
		}
		
		Map<String, Object> tmpMap = DesignAnalogFilter(Qp, Qs, Rp, As, afType, fType);//, pBs, pAs);
		RealSeq bs = (RealSeq)tmpMap.get("bs");
		RealSeq as = (RealSeq)tmpMap.get("as");
		
		AnalogFilter filter = new AnalogFilter(bs, as);
		AFPara para = new AFPara(Qp, Qs, Rp, As, fType, afType);
		filter.setFilterPara(para);
		return filter;
	}

	//根据设计规格设计各型模拟滤波器
	//Qp：通带截止频率地址
	//Qs：阻带截止频率地址
	//Rp：通带最大衰减 
	//As：阻带最小衰减
	//afType：采用的模拟滤波器的类型，目前支持三种：BUTT, CHEB1, CHEB2 
	//fType：频率选择性滤波器类型
	//下面为返回值： 
	//pBs：滤波器系统函数分子多项式数组地址 
	//pAs：滤波器系统函数分母多项式数组地址
	public static Map<String, Object> DesignAnalogFilter(double[] Qp, double[] Qs, double Rp, double As, AFType afType, FilterType fType)//, RealSeq * pBs, RealSeq * pAs)
	{
	    //double Q_p = 0.0;
	    //double Q_s = 0.0;
	    //RealSeq bS = {0};
	    //RealSeq aS = {0};
	    //RealSeq Ns = {0};
	    //RealSeq Ds = {0};
	    
	    if(fType == FilterType.LOWPASS)    //低通，不用频带变换了，可以直接设计 
	    {
	        return ALPDesigner.DesignAnalogLowPassFilter(Qp[0], Qs[0], Rp, As, afType);//, pBs, pAs);
	        //return;
	    }
	    else
	    {
	        //第一步：获取频带变换映射关系，以及相应模拟低通滤波器参数 
	    		Map<String, Object> tmpMap = GetAnalogLPMappingPara(Qp, Qs, fType);//, &Q_p, &Q_s, &Ns, &Ds);
	        double Q_p = (double)tmpMap.get("Q_p");
	        double Q_s = (double)tmpMap.get("Q_s");
	        RealSeq Ns = (RealSeq)tmpMap.get("Ns");
	        RealSeq Ds = (RealSeq)tmpMap.get("Ds");
	        
	        //第二步：设计模拟低通滤波器
	        tmpMap = ALPDesigner.DesignAnalogLowPassFilter(Q_p, Q_s, Rp, As, afType);//, &bS, &aS);
	        RealSeq bS = (RealSeq)tmpMap.get("bs");
	        RealSeq aS = (RealSeq)tmpMap.get("as");
	        //第三步：做频带变换，得到所需各型模拟滤波器
	        return AnalogFreqBandTransform(bS, aS, Ns, Ds);//, pBs, pAs);        
	    }   
	}

	//获取从低通到各型滤波器的映射关系以及相应低通滤波器的截止频率 
	//Qp：各型通带截止频率地址
	//Qs：各型阻带截止频率地址
	//fType：频率选择性滤波器类型
	//下面为返回值：
	//pQ_p：相应低通滤波器的通带截止频率
	//pQ_s：相应低通滤波器的阻带截止频率 
	//pNs：映射关系的分子多项式系数数组地址 
	//pDs：映射关系的分母多项式系数数组地址
	private static Map<String, Object> GetAnalogLPMappingPara(double[] Qp, double[] Qs, FilterType fType)//, double * pQ_p, double * pQ_s, RealSeq * pNs, RealSeq * pDs)
	{
	    switch(fType)
	    {
	    case LOWPASS:
	        return AnalogLP2LPMappingPara(Qp[0], Qs[0]);//, pQ_p, pQ_s, pNs, pDs);
	        //break;
	    case HIGHPASS:
	        return AnalogLP2HPMappingPara(Qp[0], Qs[0]);//, pQ_p, pQ_s, pNs, pDs);
	        //break;
	    case BANDPASS:
	        return AnalogLP2BPMappingPara(Qp, Qs);//, pQ_p, pQ_s, pNs, pDs);
	        //break;
	    case BANDSTOP:
	        return AnalogLP2BSMappingPara(Qp, Qs);//, pQ_p, pQ_s, pNs, pDs);
	        //break;
	    default:
	        return null;
	    }
	}

	//获取从低通到不同截止频率的低通滤波器的映射关系以及相应低通滤波器的截止频率 
	//Qp：各型通带截止频率地址
	//Qs：各型阻带截止频率地址
	//下面为返回值：
	//pQ_p：相应低通滤波器的通带截止频率
	//pQ_s：相应低通滤波器的阻带截止频率 
	//pNs：映射关系的分子多项式系数数组地址 
	//pDs：映射关系的分母多项式系数数组地址
	private static Map<String, Object> AnalogLP2LPMappingPara(double Qp, double Qs)//, double * pQ_p, double * pQ_s, RealSeq * pNs, RealSeq * pDs)
	{
	    double Q_p = 1;
	    double Q_s = Qs/Qp;      //(7.5.97)
	    
	    //产生映射，见(7.5.97)
	    //N(s) = s
	    //double * pB = (double *)malloc(2 * sizeof(double));     
	    //pB[0] = 1.0; pB[1] = 0.0;       //别忘了常数项 
	    //pNs->pData = pB;
	    //pNs->len = 2;
	    RealSeq Ns = new RealSeq(1.0,0.0);
	    
	    //D(s) = Qp
	    //double * pA = (double *)malloc(1 * sizeof(double));     
	    //pA[0] = Qp;
	    //pDs->pData = pA;
	    //pDs->len = 1; 
	    RealSeq Ds = new RealSeq(Qp);
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("Q_p", Q_p);
	    rtnMap.put("Q_s", Q_s);
	    rtnMap.put("Ns", Ns);
	    rtnMap.put("Ds", Ds);
	    return rtnMap;
	}

	//获取从低通到高通滤波器的映射关系以及相应低通滤波器的截止频率 
	//Qp：各型通带截止频率地址
	//Qs：各型阻带截止频率地址
	//下面为返回值：
	//pQ_p：相应低通滤波器的通带截止频率
	//pQ_s：相应低通滤波器的阻带截止频率 
	//pNs：映射关系的分子多项式系数数组地址 
	//pDs：映射关系的分母多项式系数数组地址
	private static Map<String, Object> AnalogLP2HPMappingPara(double Qp, double Qs)//, double * pQ_p, double * pQ_s, RealSeq * pNs, RealSeq * pDs)
	{
	    double Q_p = 1;
	    double Q_s = Qp/Qs;      //(7.5.99)
	    
	    //产生映射，见(7.5.99)
	    //N(s) = Qp
	    //double * pB = (double *)malloc(1 * sizeof(double));     
	    //pB[0] = Qp;
	    //pNs->pData = pB;
	    //pNs->len = 1;
	    RealSeq Ns = new RealSeq(Qp);
	    
	    //D(s) = s
	    //double * pA = (double *)malloc(2 * sizeof(double));     
	    //pA[0] = 1.0; pA[1] = 0.0;
	    //pDs->pData = pA;
	    //pDs->len = 2; 
	    RealSeq Ds = new RealSeq(1.0,0.0);
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("Q_p", Q_p);
	    rtnMap.put("Q_s", Q_s);
	    rtnMap.put("Ns", Ns);
	    rtnMap.put("Ds", Ds);
	    return rtnMap;
	}

	//获取从低通到带通滤波器的映射关系以及相应低通滤波器的截止频率 
	//Qp：各型通带截止频率地址
	//Qs：各型阻带截止频率地址
	//下面为返回值：
	//pQ_p：相应低通滤波器的通带截止频率
	//pQ_s：相应低通滤波器的阻带截止频率 
	//pNs：映射关系的分子多项式系数数组地址 
	//pDs：映射关系的分母多项式系数数组地址
	private static Map<String, Object> AnalogLP2BPMappingPara(double[] Qp, double[] Qs)//, double * pQ_p, double * pQ_s, RealSeq * pNs, RealSeq * pDs)
	{
	    double Q_p = 1;
	    double Q_s1 = (Qs[0]*Qs[0] - Qp[0]*Qp[1])/( (Qp[1] - Qp[0]) * Qs[0] );  //(7.5.104)
	    double Q_s2 = (Qs[1]*Qs[1] - Qp[0]*Qp[1])/( (Qp[1] - Qp[0]) * Qs[1] );  //(7.5.104)
	    double Q_s = Math.min(abs(Q_s1), abs(Q_s2)); //(fabs(Q_s1) < fabs(Q_s2)) ? fabs(Q_s1) : fabs(Q_s2);      //见书本P.382 例7.3说明，应该取一个小的 
	    
	    //产生映射，见(7.5.103)
	    //N(s) = s^2 + Qp1*Qp2
	    //double * pB = (double *)malloc(3 * sizeof(double));     
	    //pB[0] = 1.0; pB[1] = 0.0; pB[2] = Qp[0]*Qp[1];
	    //pNs->pData = pB;
	    //pNs->len = 3;
	    RealSeq Ns = new RealSeq(1.0,0.0,Qp[0]*Qp[1]);
	    
	    //D(s) = (Qp2-Qp1)*s
	    //double * pA = (double *)malloc(2 * sizeof(double));     
	    //pA[0] = Qp[1]-Qp[0]; pA[1] = 0.0;
	    //pDs->pData = pA;
	    //pDs->len = 2; 
	    RealSeq Ds = new RealSeq(Qp[1]-Qp[0], 0.0);
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("Q_p", Q_p);
	    rtnMap.put("Q_s", Q_s);
	    rtnMap.put("Ns", Ns);
	    rtnMap.put("Ds", Ds);
	    return rtnMap;
	}

	//获取从低通到带阻滤波器的映射关系以及相应低通滤波器的截止频率 
	//Qp：各型通带截止频率地址
	//Qs：各型阻带截止频率地址
	//下面为返回值：
	//pQ_p：相应低通滤波器的通带截止频率
	//pQ_s：相应低通滤波器的阻带截止频率 
	//pNs：映射关系的分子多项式系数数组地址 
	//pDs：映射关系的分母多项式系数数组地址
	private static Map<String, Object> AnalogLP2BSMappingPara(double[] Qp, double[] Qs)//, double * pQ_p, double * pQ_s, RealSeq * pNs, RealSeq * pDs)
	{
	    //这里与书上例7.4过程不同，书上必须得到Q_p=1的归一化低通滤波器，而我不需要。所以我这里令Q_s=1，来求Q_p，这样更加方便 
	    //*pQ_s = 1;
	    //double Q_p1 = ( (Qs[1] - Qs[0]) * Qp[0] )/(Qs[0]*Qs[1] - Qp[0]*Qp[0]);  //(7.5.109)
	    //double Q_p2 = ( (Qs[1] - Qs[0]) * Qp[1] )/(Qs[0]*Qs[1] - Qp[1]*Qp[1]);  //(7.5.109)
	    //*pQ_p = (fabs(Q_p1) > fabs(Q_p2)) ? fabs(Q_p1) : fabs(Q_p2);      //显然应该取一个大的 
	    double Q_s = 1.0;
	    double Q_p1 = ( (Qs[1] - Qs[0]) * Qp[0] )/(Qs[0]*Qs[1] - Qp[0]*Qp[0]);  //(7.5.109)
	    double Q_p2 = ( (Qs[1] - Qs[0]) * Qp[1] )/(Qs[0]*Qs[1] - Qp[1]*Qp[1]);  //(7.5.109)
	    double Q_p = Math.max(abs(Q_p1), abs(Q_p2)); //(fabs(Q_p1) > fabs(Q_p2)) ? fabs(Q_p1) : fabs(Q_p2);      //显然应该取一个大的 

	    
	    //产生映射，见(7.5.108),注意此时：Q_s=1
	    //N(s) = (Qs2-Qs1)*s
	    //double * pB = (double *)malloc(2 * sizeof(double));     
	    //pB[0] = Qs[1]-Qs[0]; pB[1] = 0.0; 
	    //pNs->pData = pB;
	    //pNs->len = 2;
	    RealSeq Ns = new RealSeq(Qs[1]-Qs[0], 0.0);
	    
	    //D(s) = s^2 + Qs1*Qs2
	    //double * pA = (double *)malloc(3 * sizeof(double));     
	    //pA[0] = 1.0; pA[1] = 0.0; pA[2] = Qs[0]*Qs[1];
	    //pDs->pData = pA;
	    //pDs->len = 3; 
	    RealSeq Ds = new RealSeq(1.0, 0.0, Qs[0]*Qs[1]);
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("Q_p", Q_p);
	    rtnMap.put("Q_s", Q_s);
	    rtnMap.put("Ns", Ns);
	    rtnMap.put("Ds", Ds);
	    return rtnMap;
	    
	}

	//用指定的映射关系（Ns和Ds）实现模拟滤波器的频带转换，即从(bS, aS)转换为(bs, as)
	// b(s)   b(S)|
	// ---- = ----|     N(s)
	// a(s)   a(S)|@S = ----
//	                  D(s) 
	private static Map<String, Object> AnalogFreqBandTransform(RealSeq bS, RealSeq aS, RealSeq Ns, RealSeq Ds)//, RealSeq * pBs, RealSeq * pAs)
	{
	    bS = bS.reverse();
	    aS = aS.reverse();
	    Ns = Ns.reverse();
	    Ds = Ds.reverse();

	    Map<String, Object> tmpMap = ZT.ZMapping(bS, aS, Ns, Ds);
	    RealSeq Bs = (RealSeq)tmpMap.get("Bz");
	    RealSeq As = (RealSeq)tmpMap.get("Az");
	    Bs = Bs.reverse();
	    As = As.reverse(); 
	    
	    //归一化，让As[0] = 1.0 
	    double norm = As.get(0);
	    Bs = Bs.divide(norm);
	    As = As.divide(norm);
           
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("bs", Bs);
	    rtnMap.put("as", As);
	    return rtnMap;
	}

}
