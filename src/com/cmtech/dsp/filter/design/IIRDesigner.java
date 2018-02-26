package com.cmtech.dsp.filter.design;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

import java.util.HashMap;
import java.util.Map;

import com.cmtech.dsp.filter.IIRFilter;
import com.cmtech.dsp.filter.para.IIRPara;
import com.cmtech.dsp.filter.structure.StructType;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.util.ZT;

public class IIRDesigner {
	private IIRDesigner() {
		
	}
	
	public static IIRFilter design(double[] wp, double[] ws, double Rp, double As, AFType afType, FilterType fType) {
		IIRFilter filter = designIIRFilter(wp, ws, Rp, As, afType, fType);
		IIRPara para = new IIRPara(wp,ws,Rp,As,afType,fType);
		filter.setFilterPara(para);
		filter.createStructure(StructType.IIR_DF2);
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
	private static IIRFilter designIIRFilter(double[] wp, double[] ws, double Rp, double As, AFType afType, FilterType fType) //, RealSeq * pBz, RealSeq * pAz)
	{
	    double thetap = 0.0;    //相应数字低通滤波器通带截止频率
	    double thetas = 0.0;    //相应数字低通滤波器阻带截止频率 
	    
	    double T = 2.0;
	    
	    RealSeq Nz = null;
	    RealSeq Dz = null;
	    Map<String, Object> tmpMap = new HashMap<>();
	    
	    //第一步：获取对应数字低通滤波器的映射关系及其截止频率 
	    if(fType == FilterType.LOWPASS)    //本来就是低通，就不折腾了，不做频带变换了
	    {
	        thetap = wp[0];
	        thetas = ws[0];
	    }
	    else
	    {
	        tmpMap = GetLPMappingPara(wp, ws, fType);
	        thetap = (double)tmpMap.get("THETAP");
	        thetas = (double)tmpMap.get("THETAS");
	        Nz = (RealSeq)tmpMap.get("NZ");
	        Dz = (RealSeq)tmpMap.get("DZ");
	    }
	     
	    //第二步：获取相应的模拟低通滤波器的截止频率，因为A/D变换采用的是双线性变换，所以需要做频率预畸
	    double Qp = 0.0;
	    double Qs = 0.0;
	    tmpMap = PreventDistortForBilinear(thetap, thetas, T);//, &Qp, &Qs);
	    Qp = (double)tmpMap.get("QP");
	    Qs = (double)tmpMap.get("QS");
	    
	    //第三步：利用设计指标设计模拟低通滤波器 
	    RealSeq bs = null;
	    RealSeq as = null;
	    tmpMap = ALPDesigner.DesignAnalogLowPassFilter(Qp, Qs, Rp, As, afType);//, &bs, &as);
	    bs = (RealSeq)tmpMap.get("BS");
	    as = (RealSeq)tmpMap.get("AS");
	    
	    //第四步：做A/D滤波器变换,得到相应的数字低通滤波器 
	    RealSeq bZ = null;
	    RealSeq aZ = null;
	    tmpMap = A2DTransformUsingBilinear(bs, as, T);//, &bZ, &aZ);
	    bZ = (RealSeq)tmpMap.get("BZ");
	    aZ = (RealSeq)tmpMap.get("AZ");
	    
	    RealSeq Bz = null;
	    RealSeq Az = null;
	    //第五步：做频带变换，得到所需数字滤波器
	    if(fType == FilterType.LOWPASS)    //低通，不做频带变换 
	    {
	        Bz = bZ;
	        Az = aZ;
	    } 
	    else
	    {
	        tmpMap = ZT.ZMapping(bZ, aZ, Nz, Dz);
	        Bz = (RealSeq)tmpMap.get("BZ");
	        Az = (RealSeq)tmpMap.get("AZ");
	    }
	    
	    IIRFilter filter = new IIRFilter(Bz, Az);
	    return filter;
	    /*Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("BZ", Bz);
	    rtnMap.put("AZ", Az);
	    return rtnMap;*/
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
	private static Map<String, Object> GetLPMappingPara(double[] wp, double[] ws, FilterType fType) //, double * pThetap, double * pThetas, RealSeq * pNz, RealSeq * pDz)
	{
	    switch(fType)
	    {
	    case LOWPASS:
	        return GetLP2LPMappingPara(wp[0], ws[0]);
	    case HIGHPASS:
	        return GetLP2HPMappingPara(wp[0], ws[0]);
	    case BANDPASS:
	        return GetLP2BPMappingPara(wp, ws);
	    case BANDSTOP:
	        return GetLP2BSMappingPara(wp, ws);
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
	private static Map<String, Object> PreventDistortForBilinear(double wp, double ws, double T)//, double * pQp, double * pQs)
	{
	    double Qp = 2/T*tan(wp/2);
	    double Qs = 2/T*tan(ws/2);
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("QP", Qp);
	    rtnMap.put("QS", Qs);
	    return rtnMap;
	}

	//用双线性变换实现A/D滤波器变换，即将模拟滤波器转换为相应的数字滤波器 
	//bs：模拟滤波器的系统函数分子多项式系数数组
	//as：模拟滤波器的系统函数分母多项式系数数组 
	//T：参数，这个要和PreventDistortForBilinear中用的值相同，我默认使用T=2.0
	//下面为返回值：
	//pBz：变换后的数字滤波器分子多项式系数数组
	//pAz：变换后的数字滤波器分母多项式系数数组 
	private static Map<String, Object> A2DTransformUsingBilinear(RealSeq bs, RealSeq as, double T) //, RealSeq * pBz, RealSeq * pAz)
	{
		RealSeq bs1 = (RealSeq) bs.reverse();
		RealSeq as1 = (RealSeq) as.reverse();

	    RealSeq Nz = new RealSeq(2/T, -2/T);
	    RealSeq Dz = new RealSeq(1.0, 1.0);
	    
	    return ZT.ZMapping(bs1, as1, Nz, Dz);
	}


	//获取从低通滤波器到任意频率低通滤波器的映射关系及低通滤波器的截止频率
	//wp：通带截止频率地址
	//ws：阻带截止频率地址
	//下面为返回值：
	//pThetap：对应的数字低通滤波器的通带截止频率
	//pThetas：对应的数字低通滤波器的阻带截止频率 
	//pNz：映射关系分子多项式数组地址 
	//pDz：映射关系分母多项式数组地址
	private static Map<String, Object> GetLP2LPMappingPara(double wp, double ws) //, double * pThetap, double *pThetas, RealSeq * pNz, RealSeq * pDz)
	{
	    double thetap = 0.2*PI;
	    
	    double alpha = sin((thetap - wp)/2) / sin((thetap + wp)/2); //(7.12.11)
	    
	    //产生映射，见(7.12.5)
	    //N(z) = -alpha + z^-1
	    RealSeq Nz = new RealSeq(-alpha, 1.0);
	    
	    //D(z) = 1 - alpha*z^-1
	    RealSeq Dz = new RealSeq(1.0, -alpha);
	    
	    //求*pThetas
	    RealSeq omega = new RealSeq(ws);
	    RealSeq phase = new IIRFilter(Nz, Dz).freq(omega).angle();
	    double thetas = abs(phase.get(0));  
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("THETAP", thetap);
	    rtnMap.put("THETAP", thetas);
	    rtnMap.put("NZ", Nz);
	    rtnMap.put("DZ", Dz);
	    return rtnMap;
	}

	//获取从低通滤波器到高通滤波器的映射关系及低通滤波器的截止频率
	//wp：高通滤波器通带截止频率地址
	//ws：高通滤波器阻带截止频率地址
	//下面为返回值：
	//pThetap：对应的数字低通滤波器的通带截止频率
	//pThetas：对应的数字低通滤波器的阻带截止频率 
	//pNz：映射关系分子多项式数组地址 
	//pDz：映射关系分母多项式数组地址
	private static Map<String, Object> GetLP2HPMappingPara(double wp, double ws) //, double * pThetap, double *pThetas, RealSeq * pNz, RealSeq * pDz)
	{
	    double thetap = 0.2*PI;
	    
	    double alpha = - cos((thetap + wp)/2) / cos((thetap - wp)/2); //(7.12.17)
	    
	    //产生映射，见(7.12.13)
	    //N(z) = -alpha - z^-1
	    RealSeq Nz = new RealSeq(-alpha, -1.0);
	    
	    //D(z) = 1 + alpha*z^-1 
	    RealSeq Dz = new RealSeq(1.0, alpha);
	    
	    //求*pThetas
	    RealSeq omega = new RealSeq(ws);
	    RealSeq phase = new IIRFilter(Nz, Dz).freq(omega).angle();
	    double thetas = abs(phase.get(0));   
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("THETAP", thetap);
	    rtnMap.put("THETAS", thetas);
	    rtnMap.put("NZ", Nz);
	    rtnMap.put("DZ", Dz);
	    return rtnMap;
	}

	//获取从低通滤波器到带通滤波器的映射关系及低通滤波器的截止频率
	//wp：带通滤波器通带截止频率地址
	//ws：带通滤波器阻带截止频率地址
	//下面为返回值：
	//pThetap：对应的数字低通滤波器的通带截止频率
	//pThetas：对应的数字低通滤波器的阻带截止频率 
	//pNz：映射关系分子多项式数组地址 
	//pDz：映射关系分母多项式数组地址
	private static Map<String, Object> GetLP2BPMappingPara(double[] wp, double[] ws) //, double * pThetap, double *pThetas, RealSeq * pNz, RealSeq * pDz)
	{
	    double thetap = 0.2*PI;
	    
	    double alpha = cos((wp[1] + wp[0])/2) / cos((wp[1] - wp[0])/2); //(7.12.25)
	    double k = tan(thetap/2)/tan((wp[1] - wp[0])/2);             //(7.12.24)
	    
	    double d1 = -2*alpha*k/(k+1);               //(7.12.22)
	    double d2 = (k-1)/(k+1);                    //(7.12.23)
	    
	    //产生映射，见(7.12.19)
	    //N(z) = -d2 - d1*z^-1 -z^-2
	    RealSeq Nz = new RealSeq(-d2, -d1, -1.0);
	    
	    //D(z) = 1 + d1*z^-1 + d2*z^-2  
	    RealSeq Dz = new RealSeq(1.0, d1, d2);
	    
	    //求*pThetas
	    RealSeq omega = new RealSeq(ws[0], ws[1]);
	    RealSeq phase = new IIRFilter(Nz, Dz).freq(omega).angle();	
	    double thetas = phase.abs().min();
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("THETAP", thetap);
	    rtnMap.put("THETAS", thetas);
	    rtnMap.put("NZ", Nz);
	    rtnMap.put("DZ", Dz);
	    return rtnMap;
	}

	//获取从低通滤波器到带阻滤波器的映射关系及低通滤波器的截止频率
	//wp：带阻滤波器通带截止频率地址
	//ws：带阻滤波器阻带截止频率地址
	//下面为返回值：
	//pThetap：对应的数字低通滤波器的通带截止频率
	//pThetas：对应的数字低通滤波器的阻带截止频率 
	//pNz：映射关系分子多项式数组地址 
	//pDz：映射关系分母多项式数组地址
	private static Map<String, Object> GetLP2BSMappingPara(double[] wp, double[] ws) //, double * pThetap, double *pThetas, RealSeq * pNz, RealSeq * pDz)
	{
	    double thetap = 0.2*PI;
	    
	    double alpha = cos((wp[1] + wp[0])/2) / cos((wp[1] - wp[0])/2); //(7.12.36)
	    double k = tan(thetap/2)*tan((wp[1] - wp[0])/2);              //(7.12.35)
	    
	    double d1 = -2*alpha/(1+k);                 //(7.12.33)
	    double d2 = (1-k)/(1+k);                    //(7.12.34)
	    
	    //产生映射，见(7.12.30)
	    //N(z) = d2 + d1*z^-1 + z^-2
	    RealSeq Nz = new RealSeq(d2, d1, 1.0);
	    
	    //D(z) = 1 + d1*z^-1 + d2*z^-2 
	    RealSeq Dz = new RealSeq(1.0, d1, d2);
	    
	    //求*pThetas
	    RealSeq omega = new RealSeq(ws[0], ws[1]);
	    RealSeq phase = new IIRFilter(Nz, Dz).freq(omega).angle();	
	    double thetas = phase.abs().min();
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("THETAP", thetap);
	    rtnMap.put("THETAS", thetas);
	    rtnMap.put("NZ", Nz);
	    rtnMap.put("DZ", Dz);
	    return rtnMap;
	}

	//绝对指标转换为相对指标
	private static Map<String, Object> IIRDelta2Db(double delta1, double delta2)//, double * pRp, double * pAs)
	{
	    double Rp = -20*log10(1.0-delta1);
	    double As = -20*log10(delta2);
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("RP", Rp);
	    rtnMap.put("AS", As);
	    return rtnMap;
	}

	//相对指标转换为绝对指标
	private static Map<String, Object> IIRDb2Delta(double Rp, double As)//, double * pDelta1, double * pDelta2)
	{
	    double delta1 = 1 - pow(10, -Rp/20);
	    double delta2 = pow(10, -As/20);
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("DELTA1", delta1);
	    rtnMap.put("DELTA2", delta2);
	    return rtnMap;
	}


}
