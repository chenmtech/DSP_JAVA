package com.cmtech.dsp.filter;

import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.util.Complex;
import com.cmtech.dsp.util.SeqUtil;

public class AnalogFilter extends AbstractFilter implements IAnalogFilter {
	
	public AnalogFilter(double[] b, double[] a) {
		super(b, a);
	}
	
	public AnalogFilter(RealSeq bseq, RealSeq aseq){
		super(bseq, aseq);
	}


	@Override
	public ComplexSeq freq(double Qmax, int Num) {
	    RealSeq QSeq = SeqUtil.linSpace(0, Qmax, Num);
	    return freq(QSeq);
	}
	
	@Override
	public RealSeq mag(double Qmax, int Num) {
	    return freq(Qmax, Num).abs();
	}
	
	@Override
	public RealSeq pha(double Qmax, int Num) {
	    return freq(Qmax, Num).abs();
	}

	@Override
	public ComplexSeq freq(RealSeq QSeq) {
		int M = b.length;
	    int N = a.length;
	    int Num = QSeq.size();
	    
	    ComplexSeq fenzi = new ComplexSeq(Num);
	    ComplexSeq fenmu = new ComplexSeq(Num);	       
	    
	    int i = 0;
	    int j = 0;
	    double Q = 0.0;
	    double re = 0.0;
	    double im = 0.0;
	    int mi = 0;
	    for(i = 0; i < Num; i++)
	    {
	        Q = QSeq.get(i);
	        
	        re = 0.0;
	        im = 0.0;
	        for(j = 0; j < M; j++)
	        {
	            mi = M-1-j;
	            if( mi % 2 == 0 )
	            {
	                if( mi % 4 == 0 )
	                    re += b[j]*Math.pow(Q, mi);
	                else
	                    re -= b[j]*Math.pow(Q, mi);    
	            } 
	            else
	            {
	                if( (mi-1) % 4 == 0)
	                    im += b[j]*Math.pow(Q, mi);
	                else
	                    im -= b[j]*Math.pow(Q, mi);
	            }
	        }
	        fenzi.set(i, new Complex(re, im));
	        
	        re = 0.0;
	        im = 0.0;
	        for(j = 0; j < N; j++)
	        {
	            mi = N-1-j;
	            if( mi % 2 == 0 )
	            {
	                if( mi % 4 == 0 )
	                    re += a[j]*Math.pow(Q, mi);
	                else
	                    re -= a[j]*Math.pow(Q, mi);    
	            } 
	            else
	            {
	                if( (mi-1) % 4 == 0)
	                    im += a[j]*Math.pow(Q, mi);
	                else
	                    im -= a[j]*Math.pow(Q, mi);
	            }
	        }
	        fenmu.set(i, new Complex(re, im));
	    }
	    return (ComplexSeq) SeqUtil.divide(fenzi, fenmu);
	}

}
