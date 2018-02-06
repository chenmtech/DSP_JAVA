package com.cmtech.dsp.seq;

public class SeqUtil {
	private SeqUtil() {
	}
	
	public static RealSeq add(RealSeq seq1, RealSeq seq2) {
		int N = Math.max(seq1.size(), seq2.size());
		RealSeq out = new RealSeq(N);
		double tmp1 = 0.0;
		double tmp2 = 0.0;
		for(int i = 0; i < N; i++) {
			tmp1 = (i < seq1.size()) ? seq1.get(i) : 0.0;
			tmp2 = (i < seq2.size()) ? seq2.get(i) : 0.0;
			out.set(i, tmp1+tmp2);
		}
		return out;	
	}
	
	public static ComplexSeq add(ComplexSeq seq1, ComplexSeq seq2) {
		int N = Math.max(seq1.size(), seq2.size());
		ComplexSeq out = new ComplexSeq(N);
		Complex tmp1 = new Complex();
		Complex tmp2 = new Complex();
		for(int i = 0; i < N; i++) {
			tmp1 = (i < seq1.size()) ? seq1.get(i) : new Complex();
			tmp2 = (i < seq2.size()) ? seq2.get(i) : new Complex();
			out.set(i, Complex.add(tmp1, tmp2));
		}
		return out;	
	}
	
	public static RealSeq multiple(RealSeq seq1, RealSeq seq2) {
		int N = Math.max(seq1.size(), seq2.size());
		RealSeq out = new RealSeq(N);
		double tmp1 = 0.0;
		double tmp2 = 0.0;
		for(int i = 0; i < N; i++) {
			tmp1 = (i < seq1.size()) ? seq1.get(i) : 0.0;
			tmp2 = (i < seq2.size()) ? seq2.get(i) : 0.0;
			out.set(i, tmp1*tmp2);
		}
		return out;	
	}
	
	public static ComplexSeq multiple(ComplexSeq seq1, ComplexSeq seq2) {
		int N = Math.max(seq1.size(), seq2.size());
		ComplexSeq out = new ComplexSeq(N);
		Complex tmp1 = new Complex();
		Complex tmp2 = new Complex();
		for(int i = 0; i < N; i++) {
			tmp1 = (i < seq1.size()) ? seq1.get(i) : new Complex();
			tmp2 = (i < seq2.size()) ? seq2.get(i) : new Complex();
			out.set(i, Complex.multiple(tmp1, tmp2));
		}
		return out;	
	}	
	
	public static ComplexSeq divide(ComplexSeq seq1, ComplexSeq seq2) {
		int N = Math.max(seq1.size(), seq2.size());
		ComplexSeq out = new ComplexSeq(N);
		Complex tmp1 = new Complex();
		Complex tmp2 = new Complex();
		for(int i = 0; i < N; i++) {
			tmp1 = (i < seq1.size()) ? seq1.get(i) : new Complex();
			tmp2 = (i < seq2.size()) ? seq2.get(i) : new Complex();
			out.set(i, Complex.divide(tmp1, tmp2));
		}
		return out;	
	}	
	
	public static RealSeq conv(RealSeq seq1, RealSeq seq2) {
	    int N1 = seq1.size();
	    int N2 = seq2.size();
	    int N = N1+N2-1;
	    
	    RealSeq out = new RealSeq(N);
	    
	    int n = 0;
	    int m = 0;
	    int n_m = 0;  
	    double tmp = 0.0;
	    for(n = 0; n < N; n++)
	    {
	    		tmp = 0.0;
	        for(m = 0; m < N1; m++)
	        {
	            n_m = n - m;
	            if( (n_m >= 0) && (n_m < N2) )
	            		tmp += seq1.get(m)*seq2.get(n_m);
	        }
	        out.set(n, tmp);
	    }
	    return out;
	}
	
	public static ComplexSeq conv(ComplexSeq seq1, ComplexSeq seq2) {
	    int N1 = seq1.size();
	    int N2 = seq2.size();
	    int N = N1+N2-1;
	    
	    ComplexSeq out = new ComplexSeq(N);
	    
	    int n = 0;
	    int m = 0;
	    int n_m = 0;  
	    Complex tmp = new Complex();
	    for(n = 0; n < N; n++)
	    {
	    		tmp = new Complex();
	        for(m = 0; m < N1; m++)
	        {
	            n_m = n - m;
	            if( (n_m >= 0) && (n_m < N2) )
	            		tmp.add(Complex.multiple(seq1.get(m), seq2.get(n_m)));
	        }
	        out.set(n, tmp);
	    }
	    return out;
	}
	
	public static int findPowerOfTwo(int N) {
		if(N <= 0) return 0;
		int rtn = 1;
		while((rtn *= 2) < N);
		return rtn;
	}
}
