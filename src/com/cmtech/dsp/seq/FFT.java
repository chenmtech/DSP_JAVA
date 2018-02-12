/**
 * Project Name:DSP_JAVA
 * File Name:FFT.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018年2月7日上午9:29:17
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.seq;

import static java.lang.Math.*;


/**
 * ClassName: FFT
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月7日 上午9:29:17 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class FFT {
	private static int N = 0;				// FFT的点数，必须为2的幂
	private static int L = 0;				// N = 2^L
	private static double[] re = null;
	private static double[] im = null;
	
	private FFT() {
	}
	
	public static ComplexSeq fft(RealSeq seq) {
		return fft(seq, seq.size());
	}
	
	public static ComplexSeq fft(RealSeq seq, int wishN) {
		if(seq == null || wishN <= 0) return null;
		if(!initFFT(seq, new RealSeq(seq.size()), wishN)) return null;
		bitReverse();
		doFFT();
		return new ComplexSeq(re, im);
	}
	
	public static ComplexSeq ifft(RealSeq seq) {
		return ifft(seq, seq.size());
	}
	
	//FFT反变换 
	public static ComplexSeq ifft(RealSeq seq, int wishN)
	{
		if(seq == null || wishN <= 0) return null;
	    if(!initFFT(seq, new RealSeq(seq.size()), wishN)) return null;
	    
	    //取共轭 
	    int i = 0;
	    for(i = 0; i < N; i++)
	         im[i] = -im[i];
	    
	    bitReverse();
	    doFFT();   
	    
	    for(i = 0; i < N; i++)
	    {
	         re[i] /= N;
	         im[i] /= -N;
	    } 
	    
	    return new ComplexSeq(re, im);
	}
	
	public static ComplexSeq fft(ComplexSeq seq) {
		return fft(seq, seq.size());
	}
	
	public static ComplexSeq fft(ComplexSeq seq, int wishN) {
		if(seq == null || wishN <= 0) return null;
		if(!initFFT(seq, wishN)) return null;
		bitReverse();
		doFFT();
		return new ComplexSeq(re, im);
	}
	
	public static ComplexSeq ifft(ComplexSeq seq) {
		return ifft(seq, seq.size());
	}
	
	//FFT反变换 
	public static ComplexSeq ifft(ComplexSeq seq, int wishN)
	{
		if(seq == null || wishN <= 0) return null;
	    if(!initFFT(seq, wishN)) return null;
	    
	    //取共轭 
	    int i = 0;
	    for(i = 0; i < N; i++)
	         im[i] = -im[i];
	    
	    bitReverse();
	    doFFT();   
	    
	    for(i = 0; i < N; i++)
	    {
	         re[i] /= N;
	         im[i] /= -N;
	    } 
	    
	    return new ComplexSeq(re, im);
	}
	
	
	
	private static boolean initFFT(ComplexSeq seq, int wishN) {
		if(seq == null || seq.size() == 0) return false;
		
	    N = 1;
	    L = 0;
	    
	    while(N < wishN) 
	    {
	        N *= 2;
	        L++;
	    }
	    
	    re = seq.realToArray(N);
	    im = seq.imagToArray(N);
	    
	    return true;
	}
	
	private static boolean initFFT(RealSeq reSeq, RealSeq imSeq, int wishN) {
		if(reSeq == null || imSeq == null || reSeq.size() == 0 || imSeq.size() == 0) return false;
		
	    N = 1;
	    L = 0;
	    
	    while(N < wishN) 
	    {
	        N *= 2;
	        L++;
	    }
	    
	    re = reSeq.toArray(N);
	    im = imSeq.toArray(N);
	    
	    return true;
	}
	
	private static void bitReverse()
	{
	    int I = 1;
	    int halfN = N>>1;
	    int J = halfN; 
	    double tmp = 0.0;
	    
	    int K = halfN;
	    
	    for(I = 1; I < N-1; I++)
	    {
	          if(I < J)
	          {
	              tmp = re[I];
	              re[I] = re[J];
	              re[J] = tmp;
	              
	              tmp = im[I];
	              im[I] = im[J];
	              im[J] = tmp;
	          }
	          
	          K = halfN;
	          while(J >= K) //J的最高位为1
	          {
	               J -= K;  //置0
	               K = (K>>1); 
	          } 
	          J += K;
	    }       
	} 
	
	private static void doFFT()
	{
	    int LE = 0;
	    int LE1 = 0;
	    double URe = 1.0;
	    double UIm = 0.0;
	    double WRe = 0.0;
	    double WIm = 0.0;
	    int IP = 0;
	    double TRe = 0.0;
	    double TIm = 0.0;    
	    
	    int M = 0;
	    int J = 0;
	    int I = 0;
	    for(M = 1; M <= L; M++)  //层循环 
	    {
	          LE = (int)pow(2,M);  
	          LE1 = LE>>1;
	          URe = 1.0; UIm = 0.0;
	          WRe = cos(PI/LE1); WIm = -sin(PI/LE1);
	          
	          for(J = 0; J < LE1; J++)   //蝶形运算循环 
	          { 
	               for(I = J; I < N; I += LE)   //小DFT循环
	               { 
	                    IP = I + LE1;
	                    
	                    //下面为蝶形同址运算 
	                    TRe = URe*re[IP] - UIm*im[IP];
	                    TIm = URe*im[IP] + re[IP]*UIm;
	                    
	                    re[IP] = re[I] - TRe;
	                    im[IP] = im[I] - TIm;
	                    
	                    re[I] += TRe;
	                    im[I] += TIm;        
	            
	                    //蝶形运算结束 
	               }
	               
	               //更新乘法系数
	               TRe = URe*WRe - UIm*WIm;
	               UIm = URe*WIm + WRe*UIm;
	               URe = TRe; 
	          }   
	    }
	}
}