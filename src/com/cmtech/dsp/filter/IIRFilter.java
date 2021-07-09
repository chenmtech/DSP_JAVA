package com.cmtech.dsp.filter;

import java.util.Map;

import com.cmtech.dsp.filter.structure.IIRDCBlockStructure;
import com.cmtech.dsp.filter.structure.IIRDF1Structure;
import com.cmtech.dsp.filter.structure.IIRDF2Structure;
import com.cmtech.dsp.filter.structure.IIRNotchStructure;
import com.cmtech.dsp.filter.structure.IIRTDF2Structure;
import com.cmtech.dsp.filter.structure.DigitalFilterStructType;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.util.SeqUtil;
import com.cmtech.dsp.util.ZT;

public class IIRFilter extends AbstractDigitalFilter {
	
	public IIRFilter(Double[] b, Double[] a) {
		super(b, a);
		createStructure(DigitalFilterStructType.IIR_DF2);
	}

	@Override
	public ComplexSeq freqResponse(RealSeq omega) {
		ComplexSeq fenzi = new RealSeq(b).dtft(omega);
		ComplexSeq fenmu = new RealSeq(a).dtft(omega);
		
		return (ComplexSeq) SeqUtil.divide(fenzi, fenmu);
	}
	
	//将系统函数H(Z)=b(Z)/a(Z)，通过变量的映射关系Z=N(z)/D(z)，转换为系统函数H(z)=B(z)/A(z)
	//用指定的映射关系（Nz和Dz）实现滤波器的频带转换，即从(bZ, aZ)转换为(Bz, Az)
	// 实现:
	// B(z)   b(Z)|
	// ---- = ----|     N(z)
	// A(z)   a(Z)|@Z = ----
	//	                D(z)
	public IIRFilter FreqBandTransform(RealSeq Nz, RealSeq Dz) {
		Map<String, Object> tmpMap = ZT.zMapping(new RealSeq(getB()), new RealSeq(getA()), Nz, Dz);
		RealSeq Bz = (RealSeq)tmpMap.get("BZ");
		RealSeq Az = (RealSeq)tmpMap.get("AZ");
	    return new IIRFilter(Bz.toArray(), Az.toArray());
	}


	@Override
	public void createStructure(DigitalFilterStructType sType) {
		if(sType == DigitalFilterStructType.IIR_DF1) {
			structure = new IIRDF1Structure(b, a);
		} else if(sType == DigitalFilterStructType.IIR_DF2) {
			structure = new IIRDF2Structure(b, a);
		} else if(sType == DigitalFilterStructType.IIR_TDF2) {
			structure = new IIRTDF2Structure(b, a);
		} else if(sType == DigitalFilterStructType.IIR_DCBLOCK) {
			structure = new IIRDCBlockStructure(b, a);
		} else if(sType == DigitalFilterStructType.IIR_NOTCH) {
			structure = new IIRNotchStructure(b, a);
		} else
			structure = null;
	}


}
