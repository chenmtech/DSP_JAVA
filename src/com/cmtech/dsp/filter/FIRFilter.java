package com.cmtech.dsp.filter;

import com.cmtech.dsp.filter.structure.FIRDFStructure;
import com.cmtech.dsp.filter.structure.FIRLPF1Structure;
import com.cmtech.dsp.filter.structure.FIRLPF2Structure;
import com.cmtech.dsp.filter.structure.FIRLPF3Structure;
import com.cmtech.dsp.filter.structure.FIRLPF4Structure;
import com.cmtech.dsp.filter.structure.FIRLPFStructure;
import com.cmtech.dsp.filter.structure.StructType;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;

public class FIRFilter extends DigitalFilter{
	private static final double EPS = 2.220446049250313e-016;
	
	public static final int DF = 0;
	public static final int LPF = 1;
	 
	public FIRFilter(double[] b) {
		super(b, new double[]{1.0});
	}
	
	public FIRFilter(RealSeq hseq){
		super(hseq, new RealSeq(1.0));
	}
	
	public FIRFilter(RealSeq hseq, double a){
		super((RealSeq)hseq.multiple(1/a), new RealSeq(1.0));
	}

	public RealSeq getHn() {
		return getB();
	}

	@Override
	public void setA(RealSeq a) {
		return;
	}

	@Override
	public ComplexSeq freq(RealSeq omega) {
		return new RealSeq(b).dtft(omega);
	}
	
	public LPFType whichType() {
		RealSeq hn = getB();
		if(hn == null || hn.size() == 0) return LPFType.NOTLP;
		
	    int N = hn.size();
	    
	    int halfN = 0;
	    if( N % 2 == 0 ) halfN = N/2;
	    else halfN = (N-1)/2;
	    
	    boolean isEvenSymm = true;
	    boolean isOddSymm = true;
	    int i = 0;
	    for(i = 0; i < halfN; i++)
	    {
	    		if(Math.abs(hn.get(i) - hn.get(N-i-1)) > EPS) isEvenSymm = false;
	    		if(Math.abs(hn.get(i) + hn.get(N-i-1)) > EPS) isOddSymm = false;
	    }  
		
		//奇对称且长度为奇数，中间数必须为0，否则不是线性相位
		if(isOddSymm && N % 2 == 1 && Math.abs(hn.get(halfN)) >EPS)  
			return LPFType.NOTLP;  
	    
	    if(!isEvenSymm && !isOddSymm)	//不满足对称性，不是线性相位 
	    	return LPFType.NOTLP;
			
		if(isEvenSymm)
		{ 
			if(N % 2 == 1)	//偶对称，长度为奇数，类型1
				return LPFType.TYPE1;
			else
				return LPFType.TYPE2;	//偶对称，长度为偶数，类型2
		}
		else
		{
			if(N % 2 == 1)	//奇对称，长度为奇数，类型3
				return LPFType.TYPE3;
			else
				return LPFType.TYPE4; 	//奇对称，长度为偶数，类型4
		}
	}

	@Override
	public FIRFilter createStructure(StructType sType) {
		if(sType == StructType.FIR_DF) {
			structure = new FIRDFStructure(b);
		} else if(sType == StructType.FIR_LPF) {
			structure = createLPFStructure();
		} else
			structure = null;
		return this;
	}
	
	private FIRLPFStructure createLPFStructure() {
		LPFType lpfType = whichType();
		switch(lpfType) {
		case TYPE1:
			return new FIRLPF1Structure(b);
		case TYPE2:
			return new FIRLPF2Structure(b);
		case TYPE3:
			return new FIRLPF3Structure(b);
		case TYPE4:
			return new FIRLPF4Structure(b);	
		case NOTLP:
		default:
			return null;
		}
	}
	
	

}
