package com.cmtech.dsp.filter.design;

import java.util.HashMap;
import java.util.Map;

import com.cmtech.dsp.filter.IIRFilter;
import com.cmtech.dsp.seq.RealSeq;

import static java.lang.Math.*;

public class IIRDesigner {
	private IIRDesigner() {
		
	}
	
	public static IIRFilter design(IIRSpec spec) {
		Map rtn = new HashMap();
		rtn = IIRDesign(spec.wp, spec.ws, spec.Rp, spec.As, spec.afType, spec.fType);
		
		RealSeq Bz = (RealSeq)rtn.get("Bz");
		RealSeq Az = (RealSeq)rtn.get("Az");
		
		IIRFilter filter = new IIRFilter(Bz,Az);
		//filter.setSpec(spec);
		return filter;
	}
	
	//根据设计规格设计各型IIR数字滤波器
	//wp：通带截止频率地址
	//ws：阻带截止频率地址
	//Rp：通带最大衰减 
	//As：阻带最小衰减
	//afType：采用的模拟滤波器的类型，目前支持三种：BUTT, CHEB1, CHEB2 
	//fType：滤波器类型
	//下面为返回值： 
	//pBz：滤波器系统函数分子多项式数组地址 
	//pAz：滤波器系统函数分母多项式数组地址
	private static Map IIRDesign(double[] wp, double[] ws, double Rp, double As, AFType afType, FilterType fType)//, RealSeq * pBz, RealSeq * pAz)
	{
	    double thetap = 0.0;    //相应数字低通滤波器通带截止频率
	    double thetas = 0.0;    //相应数字低通滤波器阻带截止频率 
	    
	    double T = 2.0;
	    
	    RealSeq Nz = null;
	    RealSeq Dz = null;
	    Map tmp = new HashMap();
	    
	    //第一步：获取对应数字低通滤波器的映射关系及其截止频率 
	    if(fType == FilterType.LOWPASS)    //本来就是低通，就不折腾了，不做频带变换了
	    {
	        thetap = wp[0];
	        thetas = ws[0];
	    }
	    else
	    {
	        tmp = GetLPMappingPara(wp, ws, fType);//, &thetap, &thetas, &Nz, &Dz);
	        thetap = (double)tmp.get("thetap");
	        thetas = (double)tmp.get("thetas");
	        Nz = (RealSeq)tmp.get("Nz");
	        Dz = (RealSeq)tmp.get("Dz");
	    }
	     

	    //第二步：获取相应的模拟低通滤波器的截止频率，因为A/D变换采用的是双线性变换，所以需要做频率预畸
	    double Qp = 0.0;
	    double Qs = 0.0;
	    tmp = PreventDistortForBilinear(thetap, thetas, T);//, &Qp, &Qs);
	    Qp = (double)tmp.get("Qp");
	    Qs = (double)tmp.get("Qs");
	    
	    //第三步：利用设计指标设计模拟低通滤波器 
	    RealSeq bs = null;
	    RealSeq as = null;
	    tmp = ALPDesigner.DesignAnalogLowPassFilter(Qp, Qs, Rp, As, afType);//, &bs, &as);
	    bs = (RealSeq)tmp.get("bs");
	    as = (RealSeq)tmp.get("as");
	    
	    //第四步：做A/D滤波器变换,得到相应的数字低通滤波器 
	    RealSeq bZ = null;
	    RealSeq aZ = null;
	    tmp = A2DTransformUsingBilinear(bs, as, T);//, &bZ, &aZ);
	    bZ = (RealSeq)tmp.get("Bz");
	    aZ = (RealSeq)tmp.get("Az");
	    
	    RealSeq Bz = null;
	    RealSeq Az = null;
	    //第五步：做频带变换，得到所需数字滤波器
	    if(fType == FilterType.LOWPASS)    //低通，不做频带变换 
	    {
	        //pBz->pData = bZ.pData;
	        //pBz->len = bZ.len;
	        //pAz->pData = aZ.pData;
	        //pAz->len = aZ.len;
	        Bz = bZ;
	        Az = aZ;
	    } 
	    else
	    {
	        tmp = FreqBandTransform(bZ, aZ, Nz, Dz);//, pBz, pAz);
	        Bz = (RealSeq)tmp.get("Bz");
	        Az = (RealSeq)tmp.get("Az");
	        //FreeSeq(bZ);
	        //FreeSeq(aZ);
	    }
	    
	    Map rtn = new HashMap();
	    rtn.put("Bz", Bz);
	    rtn.put("Az", Az);
	    return rtn;
	    
	    //FreeSeq(bs);
	    //FreeSeq(as);
	    //FreeSeq(Nz);
	    //FreeSeq(Dz);    
	}


	//获取从低通滤波器到所需类型滤波器的映射关系及低通滤波器的截止频率
	//wp：所需类型滤波器的通带截止频率地址
	//ws：所需类型滤波器的阻带截止频率地址
	//fType：滤波器类型
	//下面为返回值：
	//pThetap：对应的数字低通滤波器的通带截止频率
	//pThetas：对应的数字低通滤波器的阻带截止频率 
	//pNz：映射关系分子多项式数组地址 
	//pDz：映射关系分母多项式数组地址
	private static Map GetLPMappingPara(double[] wp, double[] ws, FilterType fType)//, double * pThetap, double * pThetas, RealSeq * pNz, RealSeq * pDz)
	{
	    switch(fType)
	    {
	    case LOWPASS:
	        return GetLP2LPMappingPara(wp[0], ws[0]);//, pThetap, pThetas, pNz, pDz);
	        //break;
	    case HIGHPASS:
	        return GetLP2HPMappingPara(wp[0], ws[0]);//, pThetap, pThetas, pNz, pDz);
	        //break;
	    case BANDPASS:
	        return GetLP2BPMappingPara(wp, ws);//, pThetap, pThetas, pNz, pDz);
	        //break;
	    case BANDSTOP:
	        return GetLP2BSMappingPara(wp, ws);//, pThetap, pThetas, pNz, pDz);
	    default:
	        break;
	    }
	    return null;
	}

	//获取相应的模拟低通滤波器的截止频率，因为A/D变换采用的是双线性变换，所以需要做频率预畸
	//wp：数字滤波器的通带截止频率
	//ws：数字滤波器的阻带截止频率
	//T：参数，这个可以任意设定，我默认设定为T=2.0 
	//下面为返回值：
	//pQp：对应的模拟低通滤波器的通带截止频率
	//pQs：对应的模拟低通滤波器的阻带截止频率 
	private static Map PreventDistortForBilinear(double wp, double ws, double T)//, double * pQp, double * pQs)
	{
	    double Qp = 2/T*tan(wp/2);
	    double Qs = 2/T*tan(ws/2);
	    Map rtn = new HashMap();
	    rtn.put("Qp", Qp);
	    rtn.put("Qs", Qs);
	    return rtn;
	}

	//用双线性变换实现A/D滤波器变换，即将模拟滤波器转换为相应的数字滤波器 
	//bs：模拟滤波器的系统函数分子多项式系数数组
	//as：模拟滤波器的系统函数分母多项式系数数组 
	//T：参数，这个要和PreventDistortForBilinear中用的值相同，我默认使用T=2.0
	//下面为返回值：
	//pBz：变换后的数字滤波器分子多项式系数数组
	//pAz：变换后的数字滤波器分母多项式系数数组 
	private static Map A2DTransformUsingBilinear(RealSeq bs, RealSeq as, double T)//, RealSeq * pBz, RealSeq * pAz)
	{
	    //RealSeq bs1 = TurnSeq(bs);
	    //RealSeq as1 = TurnSeq(as);
		RealSeq bs1 = bs.reverse();
		RealSeq as1 = as.reverse();
	    
	    //double Nzn[2] = {2/T, -2/T};
	    //double Dzn[2] = {1,1};
	    //RealSeq Nz = {Nzn, 2};
	    //RealSeq Dz = {Dzn, 2};
	    RealSeq Nz = new RealSeq(2/T, -2/T);
	    RealSeq Dz = new RealSeq(1.0, 1.0);
	    
	    //ZMapping(bs1, as1, Nz, Dz, pBz, pAz);
	    //FreeSeq(bs1);
	    //FreeSeq(as1);
	    return new IIRFilter(bs1, as1).FreqBandTransform(Nz, Dz);
	}

	//用指定的映射关系（Nz和Dz）实现滤波器的频带转换，即从(bZ, aZ)转换为(bz, az)
	// b(z)   b(Z)|
	// ---- = ----|     N(z)
	// a(z)   a(Z)|@Z = ----
//	                  D(z) 
	private static Map FreqBandTransform(RealSeq bZ, RealSeq aZ, RealSeq Nz, RealSeq Dz)//, RealSeq * pBz, RealSeq * pAz)
	{
		return new IIRFilter(bZ, aZ).FreqBandTransform(Nz, Dz);
	    //ZMapping(bZ, aZ, Nz, Dz, pBz, pAz);
	}


	//获取从低通滤波器到任意频率低通滤波器的映射关系及低通滤波器的截止频率
	//wp：通带截止频率地址
	//ws：阻带截止频率地址
	//下面为返回值：
	//pThetap：对应的数字低通滤波器的通带截止频率
	//pThetas：对应的数字低通滤波器的阻带截止频率 
	//pNz：映射关系分子多项式数组地址 
	//pDz：映射关系分母多项式数组地址
	private static Map GetLP2LPMappingPara(double wp, double ws)//, double * pThetap, double *pThetas, RealSeq * pNz, RealSeq * pDz)
	{
	    double thetap = 0.2*PI;
	    
	    double alpha = sin((thetap - wp)/2) / sin((thetap + wp)/2); //(7.12.11)
	    
	    //产生映射，见(7.12.5)
	    //N(z) = -alpha + z^-1
	    //double * pB = (double *)malloc(2 * sizeof(double));     
	    //pB[0] = -alpha; pB[1] = 1.0;
	    //pNz->pData = pB;
	    //pNz->len = 2;
	    RealSeq Nz = new RealSeq(-alpha, 1.0);
	    
	    //D(z) = 1 - alpha*z^-1
	    //double * pA = (double *)malloc(2 * sizeof(double));     
	    //pA[0] = 1.0; pA[1] = -alpha;
	    //pDz->pData = pA;
	    //pDz->len = 2;    
	    RealSeq Dz = new RealSeq(1.0, -alpha);
	    
	    //求*pThetas
	    //double wd[1] = {0.0};
	    //wd[0] = ws;
	    //RealSeq w = {wd, 1};
	    //RealSeq phase = AngleSeq( IIRFreqzWithW(*pNz, *pDz, w) );
	    //*pThetas = fabs(phase.pData[0]);   
	    RealSeq omega = new RealSeq(1);
	    omega.set(0, ws);
	    RealSeq phase = new IIRFilter(Nz, Dz).freq(omega).angle();
	    //RealSeq phase = AngleSeq( IIRFreqzWithW(*pNz, *pDz, w) );
	    double thetas = abs(phase.get(0));   
	    Map rtn = new HashMap();
	    rtn.put("thetap", thetap);
	    rtn.put("thetas", thetas);
	    rtn.put("Nz", Nz);
	    rtn.put("Dz", Dz);
	    return rtn;
	}

	//获取从低通滤波器到高通滤波器的映射关系及低通滤波器的截止频率
	//wp：高通滤波器通带截止频率地址
	//ws：高通滤波器阻带截止频率地址
	//下面为返回值：
	//pThetap：对应的数字低通滤波器的通带截止频率
	//pThetas：对应的数字低通滤波器的阻带截止频率 
	//pNz：映射关系分子多项式数组地址 
	//pDz：映射关系分母多项式数组地址
	private static Map GetLP2HPMappingPara(double wp, double ws)//, double * pThetap, double *pThetas, RealSeq * pNz, RealSeq * pDz)
	{
	    double thetap = 0.2*PI;
	    
	    double alpha = - cos((thetap + wp)/2) / cos((thetap - wp)/2); //(7.12.17)
	    
	    //产生映射，见(7.12.13)
	    //N(z) = -alpha - z^-1
	    //double * pB = (double *)malloc(2 * sizeof(double));     
	    //pB[0] = -alpha; pB[1] = -1.0;
	    //pNz->pData = pB;
	    //pNz->len = 2;
	    RealSeq Nz = new RealSeq(-alpha, -1.0);
	    
	    //D(z) = 1 + alpha*z^-1
	    //double * pA = (double *)malloc(2 * sizeof(double));     
	    //pA[0] = 1.0; pA[1] = alpha;
	    //pDz->pData = pA;
	    //pDz->len = 2;   
	    RealSeq Dz = new RealSeq(1.0, alpha);
	    
	    //求*pThetas
	    //double wd[1] = {0.0};
	    //wd[0] = ws;
	    //RealSeq w = {wd, 1};
	    RealSeq omega = new RealSeq(1);
	    omega.set(0, ws);
	    RealSeq phase = new IIRFilter(Nz, Dz).freq(omega).angle();
	    //RealSeq phase = AngleSeq( IIRFreqzWithW(*pNz, *pDz, w) );
	    double thetas = abs(phase.get(0));   
	    Map rtn = new HashMap();
	    rtn.put("thetap", thetap);
	    rtn.put("thetas", thetas);
	    rtn.put("Nz", Nz);
	    rtn.put("Dz", Dz);
	    return rtn;
	}

	//获取从低通滤波器到带通滤波器的映射关系及低通滤波器的截止频率
	//wp：带通滤波器通带截止频率地址
	//ws：带通滤波器阻带截止频率地址
	//下面为返回值：
	//pThetap：对应的数字低通滤波器的通带截止频率
	//pThetas：对应的数字低通滤波器的阻带截止频率 
	//pNz：映射关系分子多项式数组地址 
	//pDz：映射关系分母多项式数组地址
	private static Map GetLP2BPMappingPara(double[] wp, double[] ws)//, double * pThetap, double *pThetas, RealSeq * pNz, RealSeq * pDz)
	{
	    double thetap = 0.2*PI;
	    
	    double alpha = cos((wp[1] + wp[0])/2) / cos((wp[1] - wp[0])/2); //(7.12.25)
	    double k = tan(thetap/2)/tan((wp[1] - wp[0])/2);             //(7.12.24)
	    
	    double d1 = -2*alpha*k/(k+1);               //(7.12.22)
	    double d2 = (k-1)/(k+1);                    //(7.12.23)
	    
	    //产生映射，见(7.12.19)
	    //N(z) = -d2 - d1*z^-1 -z^-2
	    //double * pB = (double *)malloc(3 * sizeof(double));     
	    //pB[0] = -d2; pB[1] = -d1; pB[2] = -1.0;
	    //pNz->pData = pB;
	    //pNz->len = 3;
	    RealSeq Nz = new RealSeq(-d2, -d1, -1.0);
	    
	    //D(z) = 1 + d1*z^-1 + d2*z^-2
	    //double * pA = (double *)malloc(3 * sizeof(double));     
	    //pA[0] = 1.0; pA[1] = d1; pA[2] = d2;
	    //pDz->pData = pA;
	    //pDz->len = 3;    
	    RealSeq Dz = new RealSeq(1.0, d1, d2);
	    
	    //求*pThetas
	    //double wd[2] = {0.0};
	    //wd[0] = ws[0]; wd[1] = ws[1];
	    //RealSeq w = {wd, 2};
	    //RealSeq phase = AngleSeq( IIRFreqzWithW(*pNz, *pDz, w) );
	    RealSeq omega = new RealSeq(ws[0], ws[1]);
	    RealSeq phase = new IIRFilter(Nz, Dz).freq(omega).angle();	
	    double thetas = phase.abs().min();
	    //*pThetas = (fabs(phase.pData[0]) < fabs(phase.pData[1]))? fabs(phase.pData[0]) : fabs(phase.pData[1]);  
	    Map rtn = new HashMap();
	    rtn.put("thetap", thetap);
	    rtn.put("thetas", thetas);
	    rtn.put("Nz", Nz);
	    rtn.put("Dz", Dz);
	    return rtn;
	}

	//获取从低通滤波器到带阻滤波器的映射关系及低通滤波器的截止频率
	//wp：带阻滤波器通带截止频率地址
	//ws：带阻滤波器阻带截止频率地址
	//下面为返回值：
	//pThetap：对应的数字低通滤波器的通带截止频率
	//pThetas：对应的数字低通滤波器的阻带截止频率 
	//pNz：映射关系分子多项式数组地址 
	//pDz：映射关系分母多项式数组地址
	private static Map GetLP2BSMappingPara(double[] wp, double[] ws)//, double * pThetap, double *pThetas, RealSeq * pNz, RealSeq * pDz)
	{
	    double thetap = 0.2*PI;
	    
	    double alpha = cos((wp[1] + wp[0])/2) / cos((wp[1] - wp[0])/2); //(7.12.36)
	    double k = tan(thetap/2)*tan((wp[1] - wp[0])/2);              //(7.12.35)
	    
	    double d1 = -2*alpha/(1+k);                 //(7.12.33)
	    double d2 = (1-k)/(1+k);                    //(7.12.34)
	    
	    //产生映射，见(7.12.30)
	    //N(z) = d2 + d1*z^-1 + z^-2
	    //double * pB = (double *)malloc(3 * sizeof(double));     
	    //pB[0] = d2; pB[1] = d1; pB[2] = 1.0;
	    //pNz->pData = pB;
	    //pNz->len = 3;
	    RealSeq Nz = new RealSeq(d2, d1, 1.0);
	    
	    //D(z) = 1 + d1*z^-1 + d2*z^-2
	    //double * pA = (double *)malloc(3 * sizeof(double));     
	    //pA[0] = 1.0; pA[1] = d1; pA[2] = d2;
	    //pDz->pData = pA;
	    //pDz->len = 3;    
	    RealSeq Dz = new RealSeq(1.0, d1, d2);
	    
	    //求*pThetas
	    //double wd[2] = {0.0};
	    //wd[0] = ws[0]; wd[1] = ws[1];
	    //RealSeq w = {wd, 2};
	    RealSeq omega = new RealSeq(ws[0], ws[1]);
	    //RealSeq phase = AngleSeq( IIRFreqzWithW(*pNz, *pDz, w) );
	    RealSeq phase = new IIRFilter(Nz, Dz).freq(omega).angle();	
	    double thetas = phase.abs().min();
	    
	    //*pThetas = (fabs(phase.pData[0]) < fabs(phase.pData[1]))? fabs(phase.pData[0]) : fabs(phase.pData[1]);        
	    Map rtn = new HashMap();
	    rtn.put("thetap", thetap);
	    rtn.put("thetas", thetas);
	    rtn.put("Nz", Nz);
	    rtn.put("Dz", Dz);
	    return rtn;
	}

	//绝对指标转换为相对指标
	private static Map IIRDelta2Db(double delta1, double delta2)//, double * pRp, double * pAs)
	{
	    double Rp = -20*log10(1.0-delta1);
	    double As = -20*log10(delta2);
	    Map rtn = new HashMap();
	    rtn.put("Rp", Rp);
	    rtn.put("As", As);
	    return rtn;
	}

	//相对指标转换为绝对指标
	private static Map IIRDb2Delta(double Rp, double As)//, double * pDelta1, double * pDelta2)
	{
	    double delta1 = 1 - pow(10, -Rp/20);
	    double delta2 = pow(10, -As/20);
	    Map rtn = new HashMap();
	    rtn.put("delta1", delta1);
	    rtn.put("delta2", delta2);
	    return rtn;
	}


}
