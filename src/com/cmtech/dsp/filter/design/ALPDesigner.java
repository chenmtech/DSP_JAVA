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

public class ALPDesigner {
	//鏈�灏忔暟鍊� 
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
	
	//璁捐妯℃嫙浣庨�氭护娉㈠櫒
	//Qp锛氶�氬甫鎴棰戠巼
	//Qs锛氶樆甯︽埅姝㈤鐜�
	//Rp锛氶�氬甫鏈�澶ц“鍑�
	//As锛氶樆甯︽渶灏忚“鍑�
	//afType锛氭ā鎷熸护娉㈠櫒鐨勭被鍨�
	//pBs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗗瓙澶氶」寮忕郴鏁版暟缁�
	//pAs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗘瘝澶氶」寮忕郴鏁版暟缁� 
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

	//鐢ㄨ璁℃寚鏍囪璁utterworth妯℃嫙浣庨�氭护娉㈠櫒
	//Qp锛氶�氬甫鎴棰戠巼
	//Qs锛氶樆甯︽埅姝㈤鐜�
	//Rp锛氶�氬甫鏈�澶ц“鍑�
	//As锛氶樆甯︽渶灏忚“鍑�
	//pBs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗗瓙澶氶」寮忕郴鏁版暟缁�
	//pAs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗘瘝澶氶」寮忕郴鏁版暟缁�
	private static Map<String, Object> DesignAnalogButter1(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
		Map<String, Object> tmpMap = GetButterMagHPara(Qp, Qs, Rp, As);
	    int N = (int)tmpMap.get("N");
	    double Qc = (double)tmpMap.get("QC");
	    return DesignAnalogButterWithPara(N, Qc);
	    //printf("Analog Butt: N = %d, Qc = %f\n", N, Qc);
	}

	//鐢ㄧ洿鎺ユ寚瀹氭护娉㈠櫒闃舵暟鏉ヨ璁utterworth妯℃嫙浣庨�氭护娉㈠櫒 
	//N锛氭ā鎷熸护娉㈠櫒鐨勯樁鏁�
	//Qc锛氭ā鎷熸护娉㈠櫒鐨勬埅姝㈤鐜囷紝瀵逛簬Butterworth鎸�3dB鎴棰戠巼
	//pBs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗗瓙澶氶」寮忕郴鏁版暟缁�
	//pAs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗘瘝澶氶」寮忕郴鏁版暟缁� 
	private static Map<String, Object> DesignAnalogButter2(int N, double Qc)//, RealSeq * pBs, RealSeq * pAs)
	{
	    return DesignAnalogButterWithPara(N, Qc);
	} 

	//鐢ㄦā鎷熸护娉㈠櫒鐨勮璁¤鏍艰幏鍙朆utterworth婊ゆ尝鍣ㄥ箙搴﹀搷搴斿嚱鏁颁腑鐨勫弬鏁帮紝闃舵暟N锛屾埅姝㈤鐜嘠c锛�3dB鎴棰戠巼锛�
	//Qp锛氶�氬甫鎴棰戠巼
	//Qs锛氶樆甯︽埅姝㈤鐜�
	//Rp锛氶�氬甫鏈�澶ц“鍑�
	//As锛氶樆甯︽渶灏忚“鍑�
	//pN锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑闃舵暟
	//pQc锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑鎴棰戠巼
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

	//鐢ㄥ箙搴﹀搷搴斿嚱鏁扮殑鍙傛暟璁捐Butterworth妯℃嫙浣庨�氭护娉㈠櫒
	//N锛氭ā鎷熸护娉㈠櫒鐨勯樁鏁�
	//Qc锛氭ā鎷熸护娉㈠櫒鐨勬埅姝㈤鐜囷紝瀵逛簬Butterworth鎸�3dB鎴棰戠巼
	//pBs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗗瓙澶氶」寮忕郴鏁版暟缁�
	//pAs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗘瘝澶氶」寮忕郴鏁版暟缁� 
	private static Map<String, Object> DesignAnalogButterWithPara(int N, double Qc)//, RealSeq * pBs, RealSeq * pAs)
	{
	    if( N <= 0) {return null;}
	    
	    //鐢熸垚Bm锛歈c^N      //(7.5.8)    
	    RealSeq bs = new RealSeq(pow(Qc, N));
	    
	    //鐢熸垚涓�涓竴闃惰妭锛歈c/(s+Qc)         (7.5.9) 
	    RealSeq as = null;
	    
	    if( N % 2 == 0 )    //N涓哄伓鏁帮紝娌℃湁涓�闃惰妭 
	    {
	        as = new RealSeq(1.0);
	    }
	    else                //N涓哄鏁帮紝鐢熸垚涓�闃惰妭鐨凙k 
	    {
	        as = new RealSeq(1.0, Qc);
	    }    
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    if( N == 1) {
	    		rtnMap.put("BS", bs);
	    		rtnMap.put("AS", as);
	    		return rtnMap;     //涓�闃舵护娉㈠櫒锛岀洿鎺ヨ繑鍥�
	    }
	    
	    int biN = N/2;  //浜岄樁鑺傜殑涓暟
	    
	    //鐢熸垚涓�涓簩闃惰妭锛歈c^2/(s^2 - 2*Qc*cos( (0.5+(2k-1)/(2N))*PI )*s + Qc^2)鐨勫垎姣嶇郴鏁版暟缁�  //锛�7.5.9锛�
	    RealSeq biSeq = new RealSeq(1.0, 0.0, Qc*Qc);
	    
	    //姹傚垎姣嶅椤瑰紡绯绘暟Ak        //(7.5.11)
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


	//鐢ㄨ璁℃寚鏍囪璁hebyshev-I鍨嬫ā鎷熶綆閫氭护娉㈠櫒
	//Qp锛氶�氬甫鎴棰戠巼
	//Qs锛氶樆甯︽埅姝㈤鐜�
	//Rp锛氶�氬甫鏈�澶ц“鍑�
	//As锛氶樆甯︽渶灏忚“鍑�
	//pBs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗗瓙澶氶」寮忕郴鏁版暟缁�
	//pAs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗘瘝澶氶」寮忕郴鏁版暟缁�
	private static Map<String, Object> DesignAnalogCheby11(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
		Map<String, Object> tmpMap = GetCheby1MagHPara(Qp, Qs, Rp, As);
	    int N = (int)tmpMap.get("N");
	    double Qc = (double)tmpMap.get("QC");
	    double E = (double)tmpMap.get("E");
	    return DesignAnalogCheby1WithPara(N, Qc, E);
	    //printf("Analog Cheb1: N = %d, Qc = %f, E = %f\n", N, Qc, E);   
	}

	//鐩存帴鎸囧畾婊ゆ尝鍣ㄩ樁鏁版潵璁捐Chebyshev-I鍨嬫ā鎷熶綆閫氭护娉㈠櫒
	//N锛氭ā鎷熸护娉㈠櫒鐨勯樁鏁�
	//Qp锛氭ā鎷熸护娉㈠櫒鐨勯�氬甫鎴棰戠巼Qp
	//Rp锛氭ā鎷熸护娉㈠櫒鐨勯�氬甫琛板噺 
	//pBs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗗瓙澶氶」寮忕郴鏁版暟缁�
	//pAs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗘瘝澶氶」寮忕郴鏁版暟缁�
	private static Map<String, Object> DesignAnalogCheby12(int N, double Qp, double Rp)//, RealSeq * pBs, RealSeq * pAs)
	{
	    double E = sqrt( pow(10, Rp/10) - 1 );           //7.5.58
	    return DesignAnalogCheby1WithPara(N, Qp, E);
	}

	//鐢ㄦā鎷熸护娉㈠櫒鐨勮璁¤鏍艰幏鍙朇hebyshev-I鍨嬫护娉㈠櫒骞呭害鍝嶅簲鍑芥暟涓殑鍙傛暟锛岄樁鏁癗锛屾埅姝㈤鐜嘠c锛堥�氬甫鎴棰戠巼锛夛紝閫氬甫娉㈢汗epsilon 
	//Qp锛氶�氬甫鎴棰戠巼
	//Qs锛氶樆甯︽埅姝㈤鐜�
	//Rp锛氶�氬甫鏈�澶ц“鍑�
	//As锛氶樆甯︽渶灏忚“鍑�
	//pN锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑闃舵暟
	//pQc锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑鎴棰戠巼
	//pE锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑閫氬甫娉㈢汗
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

	//鐢ㄥ箙搴﹀搷搴斿嚱鏁扮殑鍙傛暟璁捐Chebyshev-I鍨嬫ā鎷熶綆閫氭护娉㈠櫒
	//N锛氭ā鎷熸护娉㈠櫒鐨勯樁鏁�
	//Qc锛氭ā鎷熸护娉㈠櫒鐨勬埅姝㈤鐜囷紝瀵逛簬Chebyshev-I鍨嬫寚閫氬甫鎴棰戠巼Qp
	//E锛氭ā鎷熸护娉㈠櫒鐨勯�氬甫娉㈢汗 
	//pBs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗗瓙澶氶」寮忕郴鏁版暟缁�
	//pAs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗘瘝澶氶」寮忕郴鏁版暟缁�
	private static Map<String, Object> DesignAnalogCheby1WithPara(int N, double Qc, double E)//, RealSeq * pBs, RealSeq * pAs)
	{
	    if( N <= 0) {return null;}

	    //鐢熸垚Bm锛歈c^N/(E*2^(N-1))      //(7.5.47)  
	    RealSeq bs = new RealSeq(pow(Qc, N)/E/pow(2, N-1));
	    
	    double gama = 1.0/E + sqrt(1.0/E/E+1);  //(7.5.44)
	    double tmp = pow(gama, 1.0/N);
	    double a = (tmp - 1/tmp)/2;     //(7.5.43a)
	    double b = (tmp + 1/tmp)/2;     //(7.5.43b)   
	     
	    //鐢熸垚涓�涓竴闃惰妭鍒嗘瘝澶氶」寮忥細(s+Qc*a)  
	    RealSeq as = null;
	    
	    if( N % 2 == 0 )    //N涓哄伓鏁帮紝娌℃湁涓�闃惰妭 
	    {
	        as = new RealSeq(1.0);
	    }
	    else                //N涓哄鏁帮紝鐢熸垚涓�闃惰妭鐨凙k 
	    {
	        as = new RealSeq(1.0, Qc*a);
	    }    
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    if( N == 1) {
	    		rtnMap.put("BS", bs);
	    		rtnMap.put("AS", as);
	    		return rtnMap;     //涓�闃舵护娉㈠櫒锛岀洿鎺ヨ繑鍥�
	    }
	    
	    int biN = N/2;  //浜岄樁鑺傜殑涓暟
	    
	    //鐢熸垚涓�涓簩闃惰妭鍒嗘瘝澶氶」寮忥細s^2 - 2*Re[s_k]*s + |s_k|^2)绯绘暟鏁扮粍  //杩欎釜闇�瑕佽嚜宸辨帹瀵间竴涓嬩簡 
	    RealSeq biSeq = new RealSeq(1.0, 0.0, 0.0);

	    double re = 0.0;
	    double im = 0.0;
	    
	    //姹傚垎姣嶅椤瑰紡绯绘暟Ak 
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
		return rtnMap;     //涓�闃舵护娉㈠櫒锛岀洿鎺ヨ繑鍥�   
	}

	//鐢ㄨ璁℃寚鏍囪璁hebyshev-II鍨嬫ā鎷熶綆閫氭护娉㈠櫒
	//Qp锛氶�氬甫鎴棰戠巼
	//Qs锛氶樆甯︽埅姝㈤鐜�
	//Rp锛氶�氬甫鏈�澶ц“鍑�
	//As锛氶樆甯︽渶灏忚“鍑�
	//pBs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗗瓙澶氶」寮忕郴鏁版暟缁�
	//pAs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗘瘝澶氶」寮忕郴鏁版暟缁�
	private static Map<String, Object> DesignAnalogCheby21(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
		Map<String, Object> tmpMap = GetCheby2MagHPara(Qp, Qs, Rp, As);
	    int N = (int)tmpMap.get("N");
	    double Qc = (double)tmpMap.get("QC");
	    double E = (double)tmpMap.get("E");
	    return DesignAnalogCheby2WithPara(N, Qc, E);
	    //printf("Analog Cheb2: N = %d, Qc = %f, E = %f\n", N, Qc, E);      
	}

	//鐩存帴鎸囧畾婊ゆ尝鍣ㄩ樁鏁版潵璁捐Chebyshev-II鍨嬫ā鎷熶綆閫氭护娉㈠櫒
	//N锛氭ā鎷熸护娉㈠櫒鐨勯樁鏁�
	//Qs锛氭ā鎷熸护娉㈠櫒鐨勯樆甯︽埅姝㈤鐜�
	//As锛氭ā鎷熸护娉㈠櫒鐨勯樆甯﹁“鍑� 
	//pBs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗗瓙澶氶」寮忕郴鏁版暟缁�
	//pAs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗘瘝澶氶」寮忕郴鏁版暟缁�
	private static Map<String, Object> DesignAnalogCheby22(int N, double Qs, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
	    double E = 1.0/sqrt( pow(10, As/10) - 1 );           //7.5.75
	    return DesignAnalogCheby2WithPara(N, Qs, E);
	}

	//鐢ㄦā鎷熸护娉㈠櫒鐨勮璁¤鏍艰幏鍙朇hebyshev-II鍨嬫护娉㈠櫒骞呭害鍝嶅簲鍑芥暟涓殑鍙傛暟锛岄樁鏁癗锛屾埅姝㈤鐜嘠c锛堥樆甯︽埅姝㈤鐜囷級锛岄�氬甫娉㈢汗epsilon 
	//Qp锛氶�氬甫鎴棰戠巼
	//Qs锛氶樆甯︽埅姝㈤鐜�
	//Rp锛氶�氬甫鏈�澶ц“鍑�
	//As锛氶樆甯︽渶灏忚“鍑�
	//pN锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑闃舵暟
	//pQc锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑鎴棰戠巼
	//pE锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑閫氬甫娉㈢汗
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

	//鐢ㄥ箙搴﹀搷搴斿嚱鏁扮殑鍙傛暟璁捐Chebyshev-II鍨嬫ā鎷熶綆閫氭护娉㈠櫒
	//N锛氭ā鎷熸护娉㈠櫒鐨勯樁鏁�
	//Qc锛氭ā鎷熸护娉㈠櫒鐨勬埅姝㈤鐜囷紝瀵逛簬Chebyshev-II鍨嬫寚闃诲甫鎴棰戠巼Qp
	//E锛氭ā鎷熸护娉㈠櫒鐨勯�氬甫娉㈢汗 
	//pBs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗗瓙澶氶」寮忕郴鏁版暟缁�
	//pAs锛氳繑鍥炵殑妯℃嫙婊ゆ尝鍣ㄧ殑绯荤粺鍑芥暟Ha(s)鍒嗘瘝澶氶」寮忕郴鏁版暟缁�
	private static Map<String, Object> DesignAnalogCheby2WithPara(int N, double Qc, double E)//, RealSeq * pBs, RealSeq * pAs)
	{
		if( N <= 0) {return null;}
	    
	    double gama = 1.0/E + sqrt(1.0/E/E+1);  //(7.5.44)
	    double tmp = pow(gama, 1.0/N);
	    double a = (tmp - 1/tmp)/2;     //(7.5.43a)
	    double b = (tmp + 1/tmp)/2;     //(7.5.43b)       
	    
	    //鐢熸垚Bm锛� 
	    RealSeq bs = new RealSeq(1.0);
	     
	    //鐢熸垚涓�涓竴闃惰妭锛歈c/(s+Qc)         
	    RealSeq as = null;
	    
	    if( N % 2 == 0 )    //N涓哄伓鏁帮紝娌℃湁涓�闃惰妭 
	    {
	        as = new RealSeq(1.0);
	    }
	    else                //N涓哄鏁帮紝鐢熸垚涓�闃惰妭鐨凙k 
	    {
	        as = new RealSeq(1.0, Qc/a);
	    }    
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    if( N == 1) {
	    		bs.set(0, as.get(1));
	    		rtnMap.put("BS", bs);
	    		rtnMap.put("AS", as);
	    		return rtnMap;
	    	}     //涓�闃舵护娉㈠櫒锛岀洿鎺ヨ繑鍥�
	    
	    int biN = N/2;  //浜岄樁鑺傜殑涓暟
	    
	    //鐢熸垚涓�涓簩闃惰妭鍒嗗瓙澶氶」寮忥紝闆剁偣瑙�(7.5.81)
	    RealSeq biNomSeq = new RealSeq(1.0, 0.0, 0.0);
	    
	    //鐢熸垚涓�涓簩闃惰妭鍒嗘瘝澶氶」寮忥紝鏋佺偣瑙�(7.5.79)     
	    RealSeq biDenSeq = new RealSeq(1.0, 0.0, 0.0);

	    double theta = 0.0;
	    double re = 0.0;
	    double im = 0.0;

	    double alpha = 0.0;
	    double beta = 0.0;
	    
	    //姹傚垎瀛愬椤瑰紡绯绘暟bm鍜屽垎姣嶅椤瑰紡绯绘暟Ak 
	    int k = 0;
	    for(k = 1; k <= biN; k++)
	    {
	        theta = PI*(2.0*k-1)/2.0/N;
	        
	        biNomSeq.set(2, Qc*Qc/cos(theta)/cos(theta));        //鍒嗗瓙澶氶」寮忕殑鏈�鍚庝竴椤逛负闆剁偣鐨勫箙搴�
	        
	        alpha = -a*sin(theta);  //(7.5.80a)
	        beta = b*cos(theta);    //(7.5.80b) 
	        
	        re = Qc*alpha/(alpha*alpha+beta*beta);       //(7.5.79b)
	        im = Qc*beta/(alpha*alpha+beta*beta);        //(7.5.79c)
	        
	        biDenSeq.set(1, -2.0*re);
	        biDenSeq.set(2, re*re+im*im);
	        
	        bs = (RealSeq) SeqUtil.conv(bs, biNomSeq);
	        as = (RealSeq) SeqUtil.conv(as, biDenSeq);
	    }
	    
	    //涓嬮潰褰掍竴鍖栵紝淇濊瘉|H(j0)|=1
	    double factor = as.get(as.size()-1)/bs.get(bs.size()-1);
	    bs = (RealSeq) bs.multiply(factor);

	    rtnMap.put("BS", bs);
		rtnMap.put("AS", as);
		return rtnMap;      
	}

	//鐢ㄨ璁℃寚鏍囪璁lliptic妯℃嫙浣庨�氭护娉㈠櫒 
	private static Map<String, Object> DesignAnalogEllip1(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
		Map<String, Object> tmpMap = GetEllipOrder(Qp, Qs, Rp, As);
		int N = (int)tmpMap.get("N");
		double ActuralAs = (double)tmpMap.get("ACTURALAS");
		//printf("Analog Ellipti: N = %d, 瀹為檯杈惧埌鐨凙s = %lf\n", N, ActuralAs);
		return DesignAnalogEllip2(N, Qp, Qs, Rp);
	}

	//鐩存帴鎸囧畾婊ゆ尝鍣ㄩ樁鏁版潵璁捐Elliptic妯℃嫙浣庨�氭护娉㈠櫒 
	private static Map<String, Object> DesignAnalogEllip2(int N, double Qp, double Qs, double Rp)//, RealSeq * pBs, RealSeq * pAs)
	{
		//鏈嚱鏁板墠鍗婇儴鏉ヨ嚜绠楁硶5.2锛屾眰褰掍竴鍖栫殑妞渾鍨嬫护娉㈠櫒 
		 
		//璁＄畻閫夋嫨鎬у洜瀛恔 
		double k = Qp/Qs;
		
		//璁＄畻妯℃暟甯搁噺q 	
		double tmp = sqrt(sqrt(1-k*k));
		double u = (1-tmp)/2.0/(1+tmp);		//(5.11)
		double q = u + 2*pow(u, 5) + 15*pow(u, 9) + 150*pow(u, 13);		//(5.10)
		
		//璁＄畻V 
		tmp = pow(10.0, Rp/20);
		double V = log((tmp+1)/(tmp-1))	/ N / 2;		//(5.12)
		
		//璁＄畻p0 
		double p0 = CalculateP0ForEllip(q, V);		//(5.13)
		
		//璁＄畻W 
		double W = sqrt( (1.0+p0*p0/k)*(1+p0*p0*k) );		//(5.14)
		
		//纭畾浜岄樁鑺備釜鏁�
		int r = (N % 2 == 0) ? N/2 : (N-1)/2;
		
		//璁＄畻Xi鍜孻i 
		Map<String, Object> tmpMap = CalculateXandYForEllip(N, q, k);		//(5.15)鍜�(5.16) 
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
		
		//鏍规嵁(5.20)璁＄畻H0 
		double H0 = (N % 2 == 0) ? pow(10.0, -Rp/20) : p0;
		for(i = 0; i < r; i++)
		{
			H0 *= (ci.get(i)/ai.get(i));
		}
		
		//涓嬮潰鍋氶鐜囧昂搴﹀寲鎿嶄綔锛岃鏂囩尞绗�9椤� 
		double alpha = sqrt(Qp*Qs);
		 
		//绗竴椤� 	
		ai = (RealSeq) ai.multiply(alpha*alpha);
		
		//绗簩椤� 
		ci = (RealSeq) ci.multiply(alpha*alpha);
		
		//绗笁椤� 
		bi = (RealSeq) bi.multiply(alpha);

		RealSeq bs = new RealSeq(1);
		RealSeq as = null;
		
		//鏍规嵁(5.22)涓璌鐨勮鏄� 
		if(N % 2 == 0)
		{
			bs.set(0, H0);
			as = new RealSeq(1.0);
		}
		else
		{
			bs.set(0, H0*alpha);		//绗洓椤�
			as = new RealSeq(1.0, p0*alpha);		//绗簲椤� 
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

	//鐢ㄦā鎷熸护娉㈠櫒鐨勮璁¤鏍艰幏鍙朎lliptic鍨嬫护娉㈠櫒鐨勯樁鏁癗鍜屽疄闄呰揪鍒扮殑闃诲甫鏈�灏忚“鍑廇s
	private static Map<String, Object> GetEllipOrder(double Qp, double Qs, double Rp, double As)//, int * pN, double * pActuralAs)
	{
		//鏈嚱鏁版潵鑷畻娉�5.1
		 
		//璁＄畻閫夋嫨鎬у洜瀛恔 
		double k = Qp/Qs;
		
		//璁＄畻妯℃暟甯搁噺q 
		double tmp = sqrt(sqrt(1-k*k));
		double u = (1-tmp)/2.0/(1+tmp);		//(5.2)
		double q = u + 2*pow(u, 5) + 15*pow(u, 9) + 150*pow(u, 13);		//(5.1)
		
		//璁＄畻鍒ゅ埆鎬у洜瀛怐 
		double D = ( pow(10.0, As/10)-1 )/( pow(10.0, Rp/10)-1 );		//(5.3)
		
		//璁＄畻闃舵暟n 
		double n = log10(16*D)/log10(1.0/q);	//(5.4)
		int N = (int)n+1;
		
		//璁＄畻瀹為檯杈惧埌鐨凙s 
		double ActuralAs = 10*log10( 1+(pow(10.0, Rp/10)-1)/16.0/pow(q, N) );		//(5.5)
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("N", N);
		rtnMap.put("ACTURALAS", ActuralAs);
		return rtnMap;
	}

	//瀹炵幇绠楁硶5.2涓叕寮�(5.13)鐢ㄤ簬璁＄畻p0鍊� 
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
		nom = 2.0*sqrt(sqrt(q))*sum;	//鎴戣繖閲屾妸鍒嗗瓙鍜屽垎姣嶉兘涔樹互浜�2 
		sum = 0.0;
		for(m = 1; m < iternum; m++)
		{
			term = pow(-1.0, m)*pow(q, m*m)*cosh(2*m*V);
			sum += term;
		}
		den = 1.0+2*sum;		//鍒嗘瘝涔熶箻浠�2 
		return abs(nom/den);
	}

	//瀹炵幇绠楁硶5.2涓叕寮�(5.15)鐢ㄤ簬璁＄畻Xi,Yi,i=1,2,...,r鐨勫��
	private static Map<String, Object> CalculateXandYForEllip(int N, double q, double k)//, RealSeq * pXi, RealSeq * pYi)
	{
		//纭畾浜岄樁鑺備釜鏁�
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

	//鐢ㄤ簬璁＄畻绗竴绫诲畬鍏ㄦき鍦嗙Н鍒咾(k) 
	//鏆傛椂娌℃湁鐢� 
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
