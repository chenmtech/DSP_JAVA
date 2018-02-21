package com.cmtech.dsp.filter;

import java.util.Map;

import com.cmtech.dsp.filter.structure.FSType;
import com.cmtech.dsp.filter.structure.IIRDF1Structure;
import com.cmtech.dsp.filter.structure.IIRDF2Structure;
import com.cmtech.dsp.filter.structure.IIRTDF2Structure;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.SeqUtil;
import com.cmtech.dsp.seq.ZT;

public class IIRFilter extends DigitalFilter {
	
	public static final int DF1 = 0;
	public static final int DF2 = 1;
	public static final int TDF2 = 2;
	
	public IIRFilter(double[] b, double[] a) {
		super(b, a);
	}
	
	public IIRFilter(RealSeq bseq, RealSeq aseq){
		super(bseq, aseq);
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
		RealSeq Bz = (RealSeq)tmpMap.get("BZ");
		RealSeq Az = (RealSeq)tmpMap.get("AZ");
	    return new IIRFilter(Bz, Az);
	}


	@Override
	public void createStructure(FSType sType) {
		if(sType == FSType.IIR_DF1) {
			structure = new IIRDF1Structure(b, a);
		} else if(sType == FSType.IIR_DF2) {
			structure = new IIRDF2Structure(b, a);
		} else if(sType == FSType.IIR_TDF2) {
			structure = new IIRTDF2Structure(b, a);
		} else
			structure = null;
		return;
	}


}
