/**
 * Project Name:DSP_JAVA
 * File Name:FFT.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018骞�2鏈�7鏃ヤ笂鍗�9:29:17
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.util;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.ISeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.Seq;


/**
 * ClassName: FFT
 * Function: 蹇�熷倕閲屽彾鍙樻崲
 * Reason: TODO ADD REASON(鍙��). 
 * date: 2018骞�2鏈�7鏃� 涓婂崍9:29:17 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public final class FFT {
	private static int N = 0;				// FFT鐨勭偣鏁帮紝蹇呴』涓�2鐨勫箓
	private static int L = 0;				// N = 2^L
	private static double[] re = new double[0];
	private static double[] im = new double[0];
	
	private FFT() {
	}
	
	/**
	 * 
	 * fft: 搴忓垪FFT. FFT鐨勭偣鏁颁负澶т簬绛変簬搴忓垪闀垮害鐨勬渶灏�2鐨勬暣鏁版骞�
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param seq 搴忓垪
	 * @return FFT
	 * @since JDK 1.6
	 */
	public synchronized static <T> ComplexSeq fft(ISeq<T> seq) {
		return fft(seq, seq.size());
	}
	
	/**
	 * 
	 * fft: 搴忓垪鎸囧畾鐐规暟鐨凢FT. FFT鐨勭偣鏁颁负澶т簬绛変簬wishN鐨勬渶灏�2鐨勬暣鏁版骞�
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param seq 搴忓垪
	 * @param wishN 鎸囧畾鐨勭偣鏁�
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
	 * ifft: 搴忓垪IFFT. IFFT鐨勭偣鏁颁负澶т簬绛変簬搴忓垪闀垮害鐨勬渶灏�2鐨勬暣鏁版骞�
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param seq 搴忓垪
	 * @return IFFT
	 * @since JDK 1.6
	 */
	public synchronized static <T> ComplexSeq ifft(ISeq<T> seq) {
		return ifft(seq, seq.size());
	}
	
	/**
	 * 
	 * ifft: 搴忓垪鎸囧畾鐐规暟鐨処FFT. IFFT鐨勭偣鏁颁负澶т簬绛変簬wishN鐨勬渶灏�2鐨勬暣鏁版骞�
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param seq 搴忓垪
	 * @param wishN 鎸囧畾鐨勭偣鏁�
	 * @return IFFT
	 * @since JDK 1.6
	 */ 
	public synchronized static <T> ComplexSeq ifft(ISeq<T> seq, int wishN)
	{
		if(wishN <= 0) return null;
	    if(!initFFT(seq, wishN)) return null;
	    
	    //鍙栧叡杞� 
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
	          while(J >= K) //J鐨勬渶楂樹綅涓�1
	          {
	               J -= K;  //缃�0
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
	    for(M = 1; M <= L; M++)  //灞傚惊鐜� 
	    {
	          LE = (int)pow(2,M);  
	          LE1 = LE>>1;
	          URe = 1.0; UIm = 0.0;
	          WRe = cos(PI/LE1); WIm = -sin(PI/LE1);
	          
	          for(J = 0; J < LE1; J++)   //铦跺舰杩愮畻寰幆 
	          { 
	               for(I = J; I < N; I += LE)   //灏廌FT寰幆
	               { 
	                    IP = I + LE1;
	                    
	                    //涓嬮潰涓鸿澏褰㈠悓鍧�杩愮畻 
	                    TRe = URe*re[IP] - UIm*im[IP];
	                    TIm = URe*im[IP] + re[IP]*UIm;
	                    
	                    re[IP] = re[I] - TRe;
	                    im[IP] = im[I] - TIm;
	                    
	                    re[I] += TRe;
	                    im[I] += TIm;        
	            
	                    //铦跺舰杩愮畻缁撴潫 
	               }
	               
	               //鏇存柊涔樻硶绯绘暟
	               TRe = URe*WRe - UIm*WIm;
	               UIm = URe*WIm + WRe*UIm;
	               URe = TRe; 
	          }   
	    }
	}
}
