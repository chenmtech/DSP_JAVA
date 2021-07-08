package com.cmtech.dsp.filter.design;

/*
Copyright (c) 2008 chenm
*/

import static java.lang.Math.abs;

import java.util.HashMap;
import java.util.Map;

import com.cmtech.dsp.filter.AnalogFilter;
import com.cmtech.dsp.filter.para.AnalogFilterPara;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.util.ZT;


/**
 * This class is for the design of analog filter. 
 * The type of filter which can be designed contains Butt, Cheb1, Cheb2, and Elliptical.
 * @author chenm
 *@version 2008-07
 */
public class AnalogFilterDesigner {
	private AnalogFilterDesigner() {		
	}
	
	//根据设计规格设计各型模拟滤波器，用角频率 
	/**
	 * design an analog filter
	 * @param Qp analog angular frequency of passband, 
	 *        {2*PI*fp} for lowpass and highpass filter, 
	 *        {2*PI*fpl, 2*PI*fph} for bandpass and bandstop filter
	 * @param Qs analog angular frequency of stopband, 
	 *        {2*PI*fs} for lowpass and highpass, 
	 *        {2*PI*fsl, 2*PI*fsh} for bandpass and bandstop filter
	 * @param Rp ripple at Qp, unit:dB
	 * @param As attenuation at Qs, unit:dB
	 * @param afType analog filter type, which can be BUTT, CHEB1, CHEB2, ELLIP
	 * @param fType filter type, which can be LOWPASS, HIGHPASS, BANDPASS, BANDSTOP
	 * @return an AnalogFilter
	 */
	public static AnalogFilter design(double[] Qp, double[] Qs, double Rp, double As, AnalogFilterType afType, FilterType fType)
	{
		Map<String, Object> tmpMap = designAnalogFilter(Qp, Qs, Rp, As, afType, fType);
		if(tmpMap == null) return null;
		RealSeq bs = (RealSeq)tmpMap.get("BS");
		RealSeq as = (RealSeq)tmpMap.get("AS");
		
		AnalogFilter filter = new AnalogFilter(bs, as);
		AnalogFilterPara para = new AnalogFilterPara(Qp, Qs, Rp, As, fType, afType);
		filter.setFilterPara(para);
		return filter;
	}

	//根据设计规格设计各型模拟滤波器
	//Qp：通带截止角频率
	//Qs：阻带截止角频率
	//Rp：通带最大衰减 
	//As：阻带最小衰减
	//afType：采用的模拟滤波器的类型，目前支持四种：BUTT, CHEB1, CHEB2，ELLIP
	//fType：频率选择性滤波器类型
	//下面为返回值： 
	//Bs：模拟滤波器系统函数分子多项式数组 
	//As：模拟滤波器系统函数分母多项式数组
	private static Map<String, Object> designAnalogFilter(double[] Qp, double[] Qs, double Rp, double As, AnalogFilterType afType, FilterType fType)
	{
	    if(fType == FilterType.LOWPASS)    //低通，不用频带变换了，可以直接设计 
	    {
	        return AnalogLowpassFilterDesigner.design(Qp[0], Qs[0], Rp, As, afType);
	    }
	    else
	    {
	        //第一步：获取频带变换映射关系，以及相应模拟低通滤波器参数 
	    	Map<String, Object> tmpMap = getAnalogLPMappingPara(Qp, Qs, fType);
	    	if(tmpMap == null) return null;
	        double Q_p = (double)tmpMap.get("Q_P");
	        double Q_s = (double)tmpMap.get("Q_S");
	        RealSeq Ns = (RealSeq)tmpMap.get("NS");
	        RealSeq Ds = (RealSeq)tmpMap.get("DS");
	        
	        //第二步：设计模拟低通滤波器
	        tmpMap = AnalogLowpassFilterDesigner.design(Q_p, Q_s, Rp, As, afType);
	        if(tmpMap == null) return null;
	        RealSeq bS = (RealSeq)tmpMap.get("BS");
	        RealSeq aS = (RealSeq)tmpMap.get("AS");
	        //第三步：做频带变换，得到所需各型模拟滤波器
	        return analogFreqBandTransform(bS, aS, Ns, Ds);     
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
	private static Map<String, Object> getAnalogLPMappingPara(double[] Qp, double[] Qs, FilterType fType)
	{
	    switch(fType)
	    {
	    case LOWPASS:
	        return analogLP2LPMappingPara(Qp[0], Qs[0]);
	    case HIGHPASS:
	        return analogLP2HPMappingPara(Qp[0], Qs[0]);
	    case BANDPASS:
	        return analogLP2BPMappingPara(Qp, Qs);
	    case BANDSTOP:
	        return analogLP2BSMappingPara(Qp, Qs);
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
	private static Map<String, Object> analogLP2LPMappingPara(double Qp, double Qs)
	{
	    double Q_p = 1;
	    double Q_s = Qs/Qp;      //(7.5.97)
	    
	    //产生映射，见(7.5.97)
	    //N(s) = s
	    RealSeq Ns = new RealSeq(1.0,0.0); // 一定要加上常数项
	    
	    //D(s) = Qp
	    RealSeq Ds = new RealSeq(Qp);
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("Q_P", Q_p);
	    rtnMap.put("Q_S", Q_s);
	    rtnMap.put("NS", Ns);
	    rtnMap.put("DS", Ds);
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
	private static Map<String, Object> analogLP2HPMappingPara(double Qp, double Qs)//, double * pQ_p, double * pQ_s, RealSeq * pNs, RealSeq * pDs)
	{
	    double Q_p = 1;
	    double Q_s = Qp/Qs;      //(7.5.99)
	    
	    //产生映射，见(7.5.99)
	    //N(s) = Qp
	    RealSeq Ns = new RealSeq(Qp);
	    
	    //D(s) = s
	    RealSeq Ds = new RealSeq(1.0,0.0);
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("Q_P", Q_p);
	    rtnMap.put("Q_S", Q_s);
	    rtnMap.put("NS", Ns);
	    rtnMap.put("DS", Ds);
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
	private static Map<String, Object> analogLP2BPMappingPara(double[] Qp, double[] Qs)
	{
	    double Q_p = 1;
	    double Q_s1 = (Qs[0]*Qs[0] - Qp[0]*Qp[1])/( (Qp[1] - Qp[0]) * Qs[0] );  //(7.5.104)
	    double Q_s2 = (Qs[1]*Qs[1] - Qp[0]*Qp[1])/( (Qp[1] - Qp[0]) * Qs[1] );  //(7.5.104)
	    double Q_s = Math.min(abs(Q_s1), abs(Q_s2));     //见书本P.382 例7.3说明，应该取一个小的 
	    
	    //产生映射，见(7.5.103)
	    //N(s) = s^2 + Qp1*Qp2
	    RealSeq Ns = new RealSeq(1.0,0.0,Qp[0]*Qp[1]);
	    
	    //D(s) = (Qp2-Qp1)*s
	    RealSeq Ds = new RealSeq(Qp[1]-Qp[0], 0.0);
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("Q_P", Q_p);
	    rtnMap.put("Q_S", Q_s);
	    rtnMap.put("NS", Ns);
	    rtnMap.put("DS", Ds);
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
	private static Map<String, Object> analogLP2BSMappingPara(double[] Qp, double[] Qs)
	{
	    double Q_s = 1.0;
	    double Q_p1 = ( (Qs[1] - Qs[0]) * Qp[0] )/(Qs[0]*Qs[1] - Qp[0]*Qp[0]);  //(7.5.109)
	    double Q_p2 = ( (Qs[1] - Qs[0]) * Qp[1] )/(Qs[0]*Qs[1] - Qp[1]*Qp[1]);  //(7.5.109)
	    double Q_p = Math.max(abs(Q_p1), abs(Q_p2));    //显然应该取一个大的 

	    
	    //产生映射，见(7.5.108),注意此时：Q_s=1
	    RealSeq Ns = new RealSeq(Qs[1]-Qs[0], 0.0);
	    
	    //D(s) = s^2 + Qs1*Qs2
	    RealSeq Ds = new RealSeq(1.0, 0.0, Qs[0]*Qs[1]);
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("Q_P", Q_p);
	    rtnMap.put("Q_S", Q_s);
	    rtnMap.put("NS", Ns);
	    rtnMap.put("DS", Ds);
	    return rtnMap;
	    
	}

	//用指定的映射关系（Ns和Ds）实现模拟滤波器的频带转换，即从(bS, aS)转换为(bs, as)
	// b(s)   b(S)|
	// ---- = ----|     N(s)
	// a(s)   a(S)|@S = ----
//	                  D(s) 
	private static Map<String, Object> analogFreqBandTransform(RealSeq bS, RealSeq aS, RealSeq Ns, RealSeq Ds)
	{
	    bS = (RealSeq) bS.reverse();
	    aS = (RealSeq) aS.reverse();
	    Ns = (RealSeq) Ns.reverse();
	    Ds = (RealSeq) Ds.reverse();

	    Map<String, Object> tmpMap = ZT.ZMapping(bS, aS, Ns, Ds);
	    RealSeq Bs = (RealSeq)tmpMap.get("BZ");
	    RealSeq As = (RealSeq)tmpMap.get("AZ");
	    Bs = (RealSeq) Bs.reverse();
	    As = (RealSeq) As.reverse(); 
	    
	    //归一化，让As[0] = 1.0 
	    double norm = As.get(0);
	    Bs = (RealSeq) Bs.divide(norm);
	    As = (RealSeq) As.divide(norm);
           
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("BS", Bs);
	    rtnMap.put("AS", As);
	    return rtnMap;
	}

}