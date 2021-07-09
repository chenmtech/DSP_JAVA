package com.cmtech.dsp.util;
/*
Copyright (c) 2008 chenm
*/
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;



public final class FFT {
	private static int N = 0;				// FFT point number
	private static int L = 0;				// N = 2^L
	private static double[] re = new double[0];
	private static double[] im = new double[0];
	
	private FFT() {
	}
	
	public static class FFTResult {
	    public final double[] re;   
	    public final double[] im;  
	      
	    public FFTResult(double[] real,double[] imag)  
	    {  
	        re=real;  
	        im=imag;  
	    }
	}
	
	public synchronized static <T> FFTResult fft(T[] real) {
		return fft(real, null, real.length);
	}
	
	public synchronized static <T> FFTResult fft(T[] real, int wishN) {
		return fft(real, null, wishN);
	}
	
	public synchronized static <T> FFTResult fft(T[] real, T[] imag) {
		return fft(real, imag, real.length);
	}
	
	public synchronized static <T> FFTResult fft(T[] real, T[] imag, int wishN) {
		if(real == null || real.length == 0 || wishN <= 0) return null;
		if(imag != null && real.length != imag.length) return null;
		initFFT(real, imag, real.length);
		bitReverse();
		doFFT();
		return new FFTResult(re, im);
	}	

	public synchronized static <T> FFTResult ifft(T[] real) {
		return ifft(real, null, real.length);
	}
	
	public synchronized static <T> FFTResult ifft(T[] real, int wishN) {
		return ifft(real, null, wishN);
	}
	
	public synchronized static <T> FFTResult ifft(T[] real, T[] imag) {
		return ifft(real, imag, real.length);
	}
	
	public synchronized static <T> FFTResult ifft(T[] real, T[] imag, int wishN)
	{
		if(real == null || real.length == 0 || wishN <= 0) return null;
		if(imag != null && real.length != imag.length) return null;
		initFFT(real, imag, wishN);

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
	    
		return new FFTResult(re, im);
	}
	
	private static <T> void initFFT(T[] real, T[] imag, int wishN) {
		N = 1;
	    L = 0;
	    
	    while(N < wishN) 
	    {
	        N *= 2;
	        L++;
	    }
	    
	    re = new double[N];
	    im = new double[N];
	    int minS = Math.min(N, real.length);
	    
	    for(int i = 0; i < minS; i++) {
	    	re[i] = (double) real[i];
	    	if(imag != null)
	    		im[i] = (double) imag[i];
	    }
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
	          while(J >= K)
	          {
	               J -= K;
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
	    for(M = 1; M <= L; M++)
	    {
	          LE = (int)pow(2,M);  
	          LE1 = LE>>1;
	          URe = 1.0; UIm = 0.0;
	          WRe = cos(PI/LE1); WIm = -sin(PI/LE1);
	          
	          for(J = 0; J < LE1; J++) 
	          { 
	               for(I = J; I < N; I += LE)
	               { 
	                    IP = I + LE1;
	                    
	                    TRe = URe*re[IP] - UIm*im[IP];
	                    TIm = URe*im[IP] + re[IP]*UIm;
	                    
	                    re[IP] = re[I] - TRe;
	                    im[IP] = im[I] - TIm;
	                    
	                    re[I] += TRe;
	                    im[I] += TIm;  
	               }
	               TRe = URe*WRe - UIm*WIm;
	               UIm = URe*WIm + WRe*UIm;
	               URe = TRe; 
	          }   
	    }
	}
}
