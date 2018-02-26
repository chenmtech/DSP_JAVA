/**
 * Project Name:DSP_JAVA
 * File Name:FFT.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018年2月7日上午9:29:17
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.util;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

import com.cmtech.dsp.seq.Seq;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.ISeq;
import com.cmtech.dsp.seq.RealSeq;


/**
 * ClassName: FFT
 * Function: 快速傅里叶变换
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月7日 上午9:29:17 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public final class FFT {
	private static int N = 0;				// FFT的点数，必须为2的幂
	private static int L = 0;				// N = 2^L
	private static double[] re = new double[0];
	private static double[] im = new double[0];
	
	private FFT() {
	}
	
	/**
	 * 
	 * fft: 序列FFT. FFT的点数为大于等于序列长度的最小2的整数次幂
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq 序列
	 * @return FFT
	 * @since JDK 1.6
	 */
	public synchronized static <T> ComplexSeq fft(ISeq<T> seq) {
		return fft(seq, seq.size());
	}
	
	/**
	 * 
	 * fft: 序列指定点数的FFT. FFT的点数为大于等于wishN的最小2的整数次幂
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq 序列
	 * @param wishN 指定的点数
	 * @return FFT
	 * @since JDK 1.6
	 */
	public synchronized static <T> ComplexSeq fft(ISeq<T> seq, int wishN) {
		if(wishN <= 0) return null;
		if(!initFFT(seq, wishN)) return null;
		bitReverse();
		doFFT();
		return new ComplexSeq(re, im);
	}
	
	/**
	 * 
	 * ifft: 序列IFFT. IFFT的点数为大于等于序列长度的最小2的整数次幂
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq 序列
	 * @return IFFT
	 * @since JDK 1.6
	 */
	public synchronized static <T> ComplexSeq ifft(ISeq<T> seq) {
		return ifft(seq, seq.size());
	}
	
	/**
	 * 
	 * ifft: 序列指定点数的IFFT. IFFT的点数为大于等于wishN的最小2的整数次幂
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq 序列
	 * @param wishN 指定的点数
	 * @return IFFT
	 * @since JDK 1.6
	 */ 
	public synchronized static <T> ComplexSeq ifft(ISeq<T> seq, int wishN)
	{
		if(wishN <= 0) return null;
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
	
	private static <T> boolean initFFT(ISeq<T> seq, int wishN) {
		if(seq == null || seq.size() == 0) return false;
		
	    N = 1;
	    L = 0;
	    
	    while(N < wishN) 
	    {
	        N *= 2;
	        L++;
	    }
	    
	    re = new double[N];
	    im = new double[N];
	    int S = Math.min(N, seq.size());

	    Class<?> cl = ((Seq<T>)seq).getClass();
	    if(cl == ComplexSeq.class) {
		    	for(int i = 0; i < S; i++) {
		    		re[i] = ((ComplexSeq)seq).get(i).getReal();
		    		im[i] = ((ComplexSeq)seq).get(i).getImag();
		    }
	    } else if(cl == RealSeq.class) {
		    	for(int i = 0; i < S; i++) {
		    		re[i] = ((RealSeq)seq).get(i);
		    		im[i] = 0.0;
		    }
	    } else {
	    		return false;
	    }
	    
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
