package com.cmtech.dsp.filter.design;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.util.HashMap;
import java.util.Map;

import com.cmtech.dsp.filter.FIRFilter;
import com.cmtech.dsp.filter.para.FIRPara;
import com.cmtech.dsp.filter.para.KaiserPara;
import com.cmtech.dsp.filter.structure.StructType;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.util.SeqUtil;


public class FIRDesigner {
	private FIRDesigner() {
		
	}
	
	public static synchronized FIRFilter design(double[] wp, double[] ws, double Rp, double As, FilterType fType) {
		return design(wp,ws,Rp,As,fType,WinType.UNKNOWN).createStructure(StructType.FIR_DF);
	}
	
	public static synchronized FIRFilter design(double[] wp, double[] ws, double Rp, double As, FilterType fType, WinType wType) {
		if(wType != WinType.KAISER)
			return designFIRUsingWindow(wp, ws, Rp, As, fType).createStructure(StructType.FIR_DF);
		else
			return designFIRUsingKaiser(wp, ws, Rp, As, fType).createStructure(StructType.FIR_DF);
	}
	
	//根据相对设计规格，用窗函数法设计FIR滤波器。注：这里的窗不包括凯泽窗
	//pWp：通带截止频率地址
	//pWs：阻带截止频率地址
	//pRp：通带最大衰减地址 
	//pAs：阻带最小衰减地址
	//fType：滤波器类型
	//下面为返回值： 
	//pN：滤波器h(n)的长度地址
	//pWc：滤波器的截止频率地址
	//pWType：滤波器采用的窗类型地址 
	public static FIRFilter designFIRUsingWindow(double[] wp, double[] ws, double Rp, double As, FilterType fType)
	{
	    RealSeq outSeq = null;
	    
	    //步骤见幻灯45页 
	    //第一步：修正设计规格 
	    Map<String, Object> tmpMap = ReviseRpAsForWindow(Rp, As);
	    Rp = (double)tmpMap.get("RP");
	    As = (double)tmpMap.get("AS");
	    
	    //第二步：确定窗函数的参数
	    //2.1：确定窗类型 
	    WinType wType = DetermineWinType(As);
	    if(wType == WinType.UNKNOWN) return null;
	    
	    //2.2：计算带宽和理想滤波器的截止频率Qc 
	    double deltaw = 0.0;

	    double[] wc = {0.0,0.0};
	    if( (fType == FilterType.LOWPASS) || (fType == FilterType.HIGHPASS) )
	    {
	        deltaw = Math.abs(wp[0] - ws[0]);
	        wc[0] = (wp[0] + ws[0])/2;
	    }
	    else
	    {
	        deltaw = Math.min(Math.abs(wp[0] - ws[0]), Math.abs(wp[1] - ws[1]));
	        wc[0] = (wp[0] + ws[0])/2;
	        wc[1] = (wp[1] + ws[1])/2;
	    }
	    
	    //2.3：用带宽确定窗函数的长度 
	    int N = DeterminWinLength(deltaw, wType, fType);
	    if(N == 0) return null;
	    
	    //第三步：用窗函数的参数设计滤波器 
	    outSeq = FIRUsingWindow(N, wc, wType, fType);
	    
	    FIRFilter filter = new FIRFilter(outSeq);
	    FIRPara para = new FIRPara(wp,ws,Rp,As,fType,N,wc,wType);
	    filter.setFilterPara(para);
	    return filter;
	}
	
	//根据窗函数的参数设计滤波器，参数包括滤波器的类型、h(n)的长度N和理想滤波器的截止频率wc。注：这里的窗不包括凯泽窗
	//N：滤波器h(n)的长度
	//wc：理想滤波器的截止频率地址
	//wType：滤波器采用的窗类型 
	//fType：滤波器类型
	public static RealSeq FIRUsingWindow(int N, double[] wc, WinType wType, FilterType fType)
	{
	    //3.1：用窗长度和理想滤波器的截止频率确定理想滤波器的hd(n) 
	    RealSeq idealSeq = IdealFilter(N, wc, fType);
	    
	    //3.2：用窗类型和窗长度确定窗函数w(n) 
	    RealSeq winSeq = Window(N, wType);
	    
	    //3.3：求h(n) = hd(n)w(n) 
	    idealSeq = (RealSeq) SeqUtil.multiple(idealSeq, winSeq);

	    return idealSeq;
	}
	
	//根据相对设计规格，用凯泽窗设计FIR滤波器。
	//pWp：通带截止频率地址
	//pWs：阻带截止频率地址
	//pRp：通带最大衰减地址 
	//pAs：阻带最小衰减地址
	//fType：滤波器类型
	//下面为返回值： 
	//pN：滤波器h(n)的长度地址
	//pBeta：参数beta的地址 
	//pWc：滤波器的截止频率地址
	public static FIRFilter designFIRUsingKaiser(double[] wp, double[] ws, double Rp, double As, FilterType fType)
	{
	    RealSeq outSeq = null;
	    
	    Map<String, Object> tmpMap = ReviseRpAsForWindow(Rp, As);
	    Rp = (double)tmpMap.get("RP");
	    As = (double)tmpMap.get("AS");
	    
	    double deltaw = 0.0;

	    double[] wc = {0.0,0.0};
	    if( (fType == FilterType.LOWPASS) || (fType == FilterType.HIGHPASS) )
	    {
	        deltaw = Math.abs(wp[0] - ws[0]);
	        wc[0] = (wp[0] + ws[0])/2;
	    }
	    else
	    {
	        deltaw = Math.min(Math.abs(wp[0] - ws[0]), Math.abs(wp[1] - ws[1]));
	        wc[0] = (wp[0] + ws[0])/2;
	        wc[1] = (wp[1] + ws[1])/2;
	    }
	    
	    tmpMap = CalcKaiserPara(As, deltaw, fType);
	    int N = (int)tmpMap.get("N");
	    double beta = (double)tmpMap.get("BETA");	    
	    
	    if(N <= 0) return null;
	    
	    outSeq = FIRUsingKaiser(N, beta, wc, fType);
	    
	    FIRFilter filter = new FIRFilter(outSeq);
	    KaiserPara para = new KaiserPara(wp,ws,Rp,As,fType,N,wc,beta);
	    filter.setFilterPara(para);
	    return filter;
	}

	//根据凯泽窗函数的参数设计滤波器，参数包括h(n)的长度N、Beta和理想滤波器的截止频率wc。
	//N：滤波器h(n)的长度
	//beta：凯泽窗的参数beta 
	//wc：滤波器的截止频率地址
	//fType：滤波器类型
	public static RealSeq FIRUsingKaiser(int N, double beta, double[] wc, FilterType fType)
	{
	    RealSeq idealSeq = IdealFilter(N, wc, fType);
	    RealSeq winSeq = Kaiser(N, beta);
	    
	    idealSeq = (RealSeq) SeqUtil.multiple(idealSeq, winSeq);

	    return idealSeq;
	}

	//用窗函数法设计微分器，如果是凯泽窗，要指定beta，如果不是，设beta=0 
	//pN：滤波器h(n)的长度，注意可能输入N值会改变
	//wType：窗类型 
	//beta：凯泽窗的参数beta 
	public static Map<String, Object> DesignDiff(int N, WinType wType, double beta)
	{
		if( N % 2 == 1 ) N++;   //微分器建议用类型4，即长度为偶数的。你如果不想要，就去掉这一行
	    
	    RealSeq idealSeq = IdealDifferentiator(N);
	    RealSeq winSeq = null;  
	    if(wType == WinType.KAISER)  winSeq = Kaiser(N, beta);
	    else winSeq = Window(N, wType);

	    idealSeq = (RealSeq) SeqUtil.multiple(idealSeq, winSeq);

	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("OUTSEQ", idealSeq);
	    rtnMap.put("N", N);
	    return rtnMap;
	}

	//用窗函数法设计Hilbert变换器，如果是凯泽窗，要指定beta，如果不是，设beta=0 
	//pN：滤波器h(n)的长度，注意可能输入N值会改变
	//wType：窗类型 
	//beta：凯泽窗的参数beta
	public static Map<String, Object> DesignHilbert(int N, WinType wType, double beta)
	{
		if( N % 2 == 0 ) N++;   //Hilbert变换器建议用类型3，即长度为奇数的。你如果不想要，就去掉这一行
	    
	    RealSeq idealSeq = IdealHilbertTransformer(N);
	    RealSeq winSeq = null;  
	    if(wType == WinType.KAISER)  winSeq = Kaiser(N, beta);
	    else winSeq = Window(N, wType);

	    idealSeq = (RealSeq) SeqUtil.multiple(idealSeq, winSeq);

	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("OUTSEQ", idealSeq);
	    rtnMap.put("N", N);
	    return rtnMap;
	}


	//设计线性相位理想滤波器的h(n)
	//N：滤波器的h(n)长度
	//wc：滤波器的截止频率地址
	//fType：滤波器的类型 
	private static RealSeq IdealFilter(int N, double[] wc, FilterType fType)
	{
	    RealSeq outSeq = null;
	    
	    switch(fType)
	    {
	    case LOWPASS:
	         return IdealLowpassFilter(N, wc[0]);
	    case HIGHPASS:
	         return IdealHighpassFilter(N, wc[0]);
	    case BANDPASS:
	         return IdealBandpassFilter(N, wc);
	    case BANDSTOP:
	         return IdealBandstopFilter(N, wc);
	    case DIFF:
	         return IdealDifferentiator(N);
	    case HILBERT:
	         return IdealHilbertTransformer(N);
	    default:
	         return outSeq;
	    }
	}

	//获取窗函数，不包括凯泽窗 
	//N：窗长度
	//wType：窗类型 
	private static RealSeq Window(int N, WinType wType)
	{
	    switch(wType)
	    {
	    case RECT:
	        return Rectangle(N);
	    case BARTLETT:
	        return Bartlett(N);
	    case HANN:
	        return Hann(N);
	    case HAMMING:
	        return Hamming(N);
	    case BLACKMAN:
	        return Blackman(N);
	    default:
	        break;
	    }
	    return null;
	}

	//获取凯泽窗函数
	//N：窗长度
	//beta：参数beta 
	private static RealSeq Kaiser(int N, double beta)
	{
	    if(Math.abs(beta -0.0) < 0.0001) return Rectangle(N);   //beta==0, 用矩形窗
	    
	    RealSeq outSeq = new RealSeq(N);
	        
	    double deno = Bessel(beta);    
	    int i = 0;
	    double x = 0.0;
	    for(i = 0; i < N; i++)
	    {
	        x = beta*Math.sqrt( 1- ( 1 - 2.0*i/(N-1) )*( 1 - 2.0*i/(N-1) ) );
	        outSeq.set(i, Bessel(x)/deno);
	    }

	    return outSeq;          
	}

	//获取线性相位的理想低通滤波器的hd(n) 
	private static RealSeq IdealLowpassFilter(int N, double wc)
	{
	    RealSeq outSeq = new RealSeq(N);
	    
	    double alpha = (N-1)/2.0;   
	    
	    int i = 0;
	    for(i = 0; i < N; i++)
	    {
	        if(Math.abs(i - alpha) < 0.1) 
	            outSeq.set(i, wc/PI);        //if i==alpha
	        else 
	            outSeq.set(i, sin(wc*(i-alpha))/(PI*(i-alpha)));
	    }
	     
	    return outSeq;
	}

	//获取线性相位的理想高通滤波器的hd(n) 
	private static RealSeq IdealHighpassFilter(int N, double wc)
	{
	    RealSeq allSeq = IdealLowpassFilter(N, PI);
	    RealSeq lowSeq = IdealLowpassFilter(N, wc);
	    return (RealSeq) SeqUtil.subtract(allSeq, lowSeq);
	}

	//获取线性相位的理想带通滤波器的hd(n) 
	private static RealSeq IdealBandpassFilter(int N, double[] wc)
	{
	    RealSeq seq1 = IdealLowpassFilter(N, wc[1]);
	    RealSeq seq2 = IdealLowpassFilter(N, wc[0]);
	    return (RealSeq) SeqUtil.subtract(seq1, seq2);  
	}

	//获取线性相位的理想带阻滤波器的hd(n) 
	private static RealSeq IdealBandstopFilter(int N, double[] wc)
	{
	    RealSeq allSeq = IdealLowpassFilter(N, PI);
	    RealSeq bpSeq = IdealBandpassFilter(N, wc);
	    return (RealSeq) SeqUtil.subtract(allSeq, bpSeq);  
	}

	//获取线性相位的理想微分器的hd(n) 
	private static RealSeq IdealDifferentiator(int N)
	{
	    RealSeq outSeq = new RealSeq(N);
	    
	    double alpha = (N-1)/2.0;   
	    
	    int i = 0;
	    double i_alpha = 0.0;
	    for(i = 0; i < N; i++)
	    {
	        i_alpha = i - alpha;
	        if(Math.abs(i_alpha) < 0.1) 
	            outSeq.set(i, 0.0);        //if i==alpha
	        else 
	        {
	            outSeq.set(i, cos(PI*i_alpha)/i_alpha - sin(PI*i_alpha)/(PI*i_alpha*i_alpha));
	        }
	    }
	     
	    return outSeq;    
	}

	//获取线性相位的理想Hilbert变换器的hd(n) 
	private static RealSeq IdealHilbertTransformer(int N)
	{
	    RealSeq outSeq = new RealSeq(N);
	    
	    double alpha = (N-1)/2.0;   
	    
	    int i = 0;
	    double i_alpha = 0.0;
	    for(i = 0; i < N; i++)
	    {
	        i_alpha = i - alpha;
	        if(Math.abs(i_alpha) < 0.1) 
	            outSeq.set(i, 0.0);        //if i==alpha
	        else 
	        {
	            outSeq.set(i, 2*sin(PI*i_alpha/2)*sin(PI*i_alpha/2)/(PI*i_alpha));
	        }
	    }

	    return outSeq;     
	}

	//获取矩形窗的窗函数序列 
	private static RealSeq Rectangle(int N)
	{
	    RealSeq outSeq = new RealSeq(N);
	    
	    int i = 0;
	    for(i = 0; i < N; i++)
	        outSeq.set(i, 1.0);

	    return outSeq;    
	}

	//获取Bartlett窗的窗函数序列 
	private static RealSeq Bartlett(int N)
	{
	    RealSeq outSeq = new RealSeq(N);
	    
	    int i = 0;
	    for(i = 0; i <= (N-1)/2; i++)
	        outSeq.set(i, 2.0*i/(N-1));
	        
	    for(i = (N-1)/2+1; i <= N-1; i++)
	        outSeq.set(i, 2-2.0*i/(N-1));

	    return outSeq;              
	}

	//获取Hanning窗的窗函数序列 
	private static RealSeq Hann(int N)
	{
	    RealSeq outSeq = new RealSeq(N);
	    
	    int i = 0;
	    for(i = 0; i < N; i++)
	        outSeq.set(i, (1 - cos(2.0*PI*i/(N-1)) )/2);

	    return outSeq;        
	}

	//获取Hamming窗的窗函数序列 
	private static RealSeq Hamming(int N)
	{
	    RealSeq outSeq = new RealSeq(N);
	    
	    int i = 0;
	    for(i = 0; i < N; i++)
	        outSeq.set(i, 0.54 - 0.46*cos(2.0*PI*i/(N-1)));

	    return outSeq;          
	}

	//获取Blackman窗的窗函数序列 
	private static RealSeq Blackman(int N)
	{
	    RealSeq outSeq = new RealSeq(N);
	    
	    int i = 0;
	    double tmp = 0.0;
	    for(i = 0; i < N; i++)
	    {
	        tmp = 2.0*PI*i/(N-1);
	        outSeq.set(i, 0.42 - 0.5*cos(tmp) + 0.08*cos(2*tmp));
	    }

	    return outSeq;          
	}

	//计算零阶贝塞尔函数值 
	private static double Bessel(double x)
	{
	    int K = 25;
	    double sum = 1.0;
	    int k = 1;
	    double item = 1.0;
	    for(k = 1; k < K; k++)
	    {
	        item *= (x*x/k/k/4);
	        sum += item;
	    }
	    return sum;
	}

	//由于窗函数法设计出来的滤波器一定是delta1==delta2，为此，在设计之前要对Rp,As做一个修正 
	private static Map<String, Object> ReviseRpAsForWindow(double Rp, double As)
	{
		double delta1 = 0.0;
	    double delta2 = 0.0;
	    Map<String, Object> tmpMap = FIRDb2Delta(Rp, As);//, &delta1, &delta2);
	    delta1 = (double)tmpMap.get("DELTA1");
	    delta2 = (double)tmpMap.get("DELTA2");
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    //不用修改技术指标
	    if(delta2 <= delta1) {
	    		rtnMap.put("RP", Rp);
	    		rtnMap.put("AS", As);
	    		return rtnMap;
	    }
	     
	    delta2 = delta1;
	    rtnMap = FIRDelta2Db(delta1, delta2);
	    return rtnMap;
	}

	//根据As确定窗类型 
	private static WinType DetermineWinType(double As)
	{
	    if(As <= 21)
	        return WinType.RECT;
	    
	    if(As <= 25)
	        return WinType.BARTLETT;
	        
	    if(As <= 44)
	        return WinType.HANN;
	        
	    if(As <= 53)
	        return WinType.HAMMING;
	        
	    if(As <= 74)
	        return WinType.BLACKMAN;
	        
	    return WinType.UNKNOWN;
	}

	//根据带宽和窗类型，确定窗长度 
	private static int DeterminWinLength(double deltaw, WinType wType, FilterType fType)
	{
	    int N = 0;
	    switch(wType)
	    {
	    case RECT:
	        N = (int)(1.8*PI/deltaw + 0.5);
	        break;
	    case BARTLETT:
	        N = (int)(6.1*PI/deltaw + 0.5);
	        break;
	    case HANN:
	        N = (int)(6.2*PI/deltaw + 0.5);
	        break;
	    case HAMMING:
	        N = (int)(6.6*PI/deltaw + 0.5);
	        break;
	    case BLACKMAN:
	        N = (int)(11.0*PI/deltaw + 0.5);
	        break;
	    default:
	        return 0;
	    }
	    if( N % 2 == 0 )
	    {
	        if( fType == FilterType.HIGHPASS || fType == FilterType.BANDSTOP ) N++;
	    }
	    return N;
	}

	//根据经验公式，用设计指标计算Kaiser窗的参数 
	private static Map<String, Object> CalcKaiserPara(double As, double deltaw, FilterType fType)//, int * pN, double * pBeta)
	{
		int N = (int)( (As - 7.95)/2.285/deltaw + 1 + 0.5 );
	    if( N % 2 == 0 )
	    {
	        if( fType == FilterType.HIGHPASS || fType == FilterType.BANDSTOP ) N++;
	    }
	    
	    double beta = 0.0;
	    if(As <= 21)    
	    {
	    		beta = 0.0; 
	    	} 
	    else if(As < 50)     
	    {
	    		beta = 0.5842* Math.pow(As-21, 0.4) + 0.07886*(As-21); 
	    	} 
	    else
	    		beta = 0.1102*(As - 8.7);
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("N", N);
	    rtnMap.put("BETA", beta);
	    return rtnMap;
	}


	//绝对指标转换为相对指标
	private static Map<String, Object> FIRDelta2Db(double delta1, double delta2)
	{
		double Rp = -20*Math.log10((1-delta1)/(1+delta1));
	    double As = -20*Math.log10(delta2/(1+delta1));
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("RP", Rp);
	    rtnMap.put("AS", As);
	    return rtnMap;
	}

	//相对指标转换为绝对指标
	private static Map<String, Object> FIRDb2Delta(double Rp, double As)
	{
		double tmp = Math.pow(10, -Rp/20);
	    double delta1 = (1-tmp)/(1+tmp);
	    double delta2 = (1+delta1) * Math.pow(10, -As/20);
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("DELTA1", delta1);
	    rtnMap.put("DELTA2", delta2);
	    return rtnMap;
	}
}
