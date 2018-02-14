package com.cmtech.dsp.filter;

import java.util.Map;

import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.SeqUtil;
import com.cmtech.dsp.seq.ZT;

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
	
	//将系统函数H(Z)=b(Z)/a(Z)，通过变量的映射关系Z=N(z)/D(z)，转换为系统函数H(z)=B(z)/A(z)
	//用指定的映射关系（Nz和Dz）实现滤波器的频带转换，即从(bZ, aZ)转换为(Bz, Az)
	// 实现:
	// B(z)   b(Z)|
	// ---- = ----|     N(z)
	// A(z)   a(Z)|@Z = ----
	//	                D(z)
	public IIRFilter FreqBandTransform(RealSeq Nz, RealSeq Dz) {
		Map<String, Object> tmpMap = ZT.ZMapping(getB(), getA(), Nz, Dz);
		RealSeq Bz = (RealSeq)tmpMap.get("Bz");
		RealSeq Az = (RealSeq)tmpMap.get("Az");
	    return new IIRFilter(Bz, Az);
	}
	
	


}
