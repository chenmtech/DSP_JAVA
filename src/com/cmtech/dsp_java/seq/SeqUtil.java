package com.cmtech.dsp_java.seq;

public class SeqUtil {
	private SeqUtil() {
	}
	
	public static RealSeq add(RealSeq seq, double a) {
		RealSeq out = new RealSeq(seq);
		out.add(a);
		return out;
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

	public static ComplexSeq add(ComplexSeq seq, Complex a) {
		ComplexSeq out = new ComplexSeq(seq);
		out.add(a);
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
	
	public static RealSeq multiple(RealSeq seq, double a) {
		RealSeq out = new RealSeq(seq);
		out.multiple(a);
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

	public static ComplexSeq multiple(ComplexSeq seq, Complex a) {
		ComplexSeq out = new ComplexSeq(seq);
		out.multiple(a);
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
	
	public static RealSeq abs(RealSeq seq) {
		RealSeq out = new RealSeq(seq.size());
		for(int i = 0; i < out.size(); i++) {			
			out.set(i, Math.abs(seq.get(i)));
		}
		return out;	
	}
	
	public static RealSeq abs(ComplexSeq seq) {
		RealSeq out = new RealSeq(seq.size());
		for(int i = 0; i < out.size(); i++) {			
			out.set(i, seq.get(i).abs());
		}
		return out;	
	}
	
	public static RealSeq angle(ComplexSeq seq) {
		RealSeq out = new RealSeq(seq.size());
		for(int i = 0; i < out.size(); i++) {			
			out.set(i, seq.get(i).angle());
		}
		return out;	
	}
	
	public static RealSeq dB(ComplexSeq seq) {
		RealSeq out = SeqUtil.abs(seq);
		double max = out.max();
		for(int i = 0; i < out.size(); i++) {			
			out.set(i, 20*Math.log10(out.get(i)/max));
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
	
	
	
}
