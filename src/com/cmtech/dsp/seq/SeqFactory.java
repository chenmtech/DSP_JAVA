package com.cmtech.dsp.seq;

public class SeqFactory {
	private SeqFactory() {
	}
	
	public static <T> T createZeroSeq(int N, Class<T> cl) {
		try {
			return cl.getConstructor(int.class).newInstance(N);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public static RealSeq createSinSeq(double A, double w, double initphi, int N) {
		RealSeq out = new RealSeq(N);
		for(int i = 0; i < N; i++) {
			out.set(i, A*Math.sin(w*i+initphi));
		}
		return out;
	}
	
	public static RealSeq createSinSeq(double A, double f, double initphi, double fs, int N)	{
		return createSinSeq(A, 2*Math.PI*f/fs, initphi, N);
	}
	
	public static ComplexSeq createEJWSeq(double w, double initphi, int N) {
		ComplexSeq out = new ComplexSeq(N);
		for(int i = 0; i < N; i++) {
			out.set(i, new Complex(Math.cos(w*i+initphi), Math.sin(w*i+initphi)));
		}
		return out;
	}
	
	public static RealSeq linSpace(double begin, double end, int N)
	{
	    RealSeq out = new RealSeq(N);
	    
	    double delta = (end - begin)/(N-1);

	    for(int i = 0; i < N-1; i++)
	    {
	        out.set(i, begin + delta*i);
	    }    
	    out.set(N-1, end);

	    return out;
	}
	
	public static RealSeq createRandomSeq(int N)
	{
	    RealSeq out = new RealSeq(N);

	    for(int i = 0; i < N; i++)
	    {
	        out.set(i, Math.random());
	    }    

	    return out;
	}
}
