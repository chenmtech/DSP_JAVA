package com.cmtech.dsp.filter;

import java.util.HashMap;
import java.util.Map;

import com.cmtech.dsp.exception.FilterException;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.SeqUtil;

public class IIRFilter extends DigitalFilter {
	
	public IIRFilter(RealSeq bseq, RealSeq aseq){
		super(bseq, aseq);
	}


	@Override
	public ComplexSeq freq(int N) {
		ComplexSeq fenzi = new RealSeq(b).dtft(N);
		ComplexSeq fenmu = new RealSeq(a).dtft(N);
		
		return SeqUtil.divide(fenzi, fenmu);
	}

	@Override
	public ComplexSeq freq(RealSeq omega) {
		ComplexSeq fenzi = new RealSeq(b).dtft(omega);
		ComplexSeq fenmu = new RealSeq(a).dtft(omega);
		
		return SeqUtil.divide(fenzi, fenmu);
	}
	
	//将系统函数H(Z)=b(Z)/a(Z)，通过变量的映射关系Z=N(z)/D(z)，转换为系统函数H(z)=b(z)/a(z)
	//用指定的映射关系（Nz和Dz）实现滤波器的频带转换，即从(bZ, aZ)转换为(bz, az)
	// 实现:
	// b(z)   b(Z)|
	// ---- = ----|     N(z)
	// a(z)   a(Z)|@Z = ----
	//	                D(z)
	public Map<String, Object> FreqBandTransform(RealSeq Nz, RealSeq Dz) {
	    int M = b.length;
	    int N = a.length;
	    int Max = (M > N)? M : N;

	    RealSeq oneSeq = new RealSeq(1.0);

	    RealSeq Bz = new RealSeq(0.0);
	    
	    int i = 0;
	    int j = 0;
	    for(i = 0; i < M; i++)
	    {
	        for(j = 0; j < i; j++)
	        {
	            oneSeq = SeqUtil.conv(oneSeq, Nz);
	        }
	        for(j = 0; j < Max-i-1; j++)
	        {  
	            oneSeq = SeqUtil.conv(oneSeq, Dz);
	        }
	        oneSeq = oneSeq.multiple(b[i]);

	        Bz = SeqUtil.add(Bz, oneSeq);

	        oneSeq = new RealSeq(1.0);
	    }

	    RealSeq Az = new RealSeq(0.0);
	    
	    for(i = 0; i < N; i++)
	    {
	        for(j = 0; j < i; j++)
	        {
	            oneSeq = SeqUtil.conv(oneSeq, Nz);
	        }
	        for(j = 0; j < Max-i-1; j++)
	        {
	            oneSeq = SeqUtil.conv(oneSeq, Dz);
	        }

	        oneSeq = oneSeq.multiple(a[i]);

	        Az = SeqUtil.add(Az, oneSeq);
     
	        oneSeq = new RealSeq(1.0);
	    }   
	    
	    //归一化，让az[0] = 1.0 
	    double norm = Az.get(0);
	    Bz = Bz.divide(norm);
	    Az = Az.divide(norm);
	    
	    Map<String, Object> rtn = new HashMap<>();
	    rtn.put("Bz", Bz);
	    rtn.put("Az", Az);
	    
	    return rtn;    
	}
	
	


}
