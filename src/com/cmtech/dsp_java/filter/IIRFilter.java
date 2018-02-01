package com.cmtech.dsp_java.filter;

import java.util.HashMap;
import java.util.Map;

import com.cmtech.dsp_java.seq.ComplexSeq;
import com.cmtech.dsp_java.seq.RealSeq;
import com.cmtech.dsp_java.seq.SeqUtil;

public class IIRFilter extends DigitalFilter {
	
	public IIRFilter(RealSeq bseq, RealSeq aseq) {
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
	public Map FreqBandTransform(RealSeq Nz, RealSeq Dz) {
	    int M = b.length;
	    int N = a.length;
	    int Max = (M > N)? M : N;
	    
	    //RealSeq oneSeq = CreateSeq(1);
	    //oneSeq.pData[0] = 1.0;
	    RealSeq oneSeq = new RealSeq(1);
	    oneSeq.set(0, 1.0);
	    
	    //RealSeq tmpSeq = {0};

	    //*pBz = CreateSeq(1);
	    //pBz->pData[0] = 0.0;   
	    RealSeq Bz = new RealSeq(1);
	    Bz.set(0, 0.0);
	    
	    int i = 0;
	    int j = 0;
	    for(i = 0; i < M; i++)
	    {
	        for(j = 0; j < i; j++)
	        {
	            //tmpSeq = Conv(oneSeq, Nz);
	            //FreeSeq(oneSeq);
	            //oneSeq.pData = tmpSeq.pData;
	            //oneSeq.len = tmpSeq.len;
	            oneSeq = SeqUtil.conv(oneSeq, Nz);
	        }
	        for(j = 0; j < Max-i-1; j++)
	        {
	            //tmpSeq = Conv(oneSeq, Dz);
	            //FreeSeq(oneSeq);
	            //oneSeq.pData = tmpSeq.pData;
	            //oneSeq.len = tmpSeq.len;   
	            oneSeq = SeqUtil.conv(oneSeq, Dz);
	        }
	        //tmpSeq = MultipleConstant(oneSeq, bZ.pData[i]);
	        //FreeSeq(oneSeq);
	        //oneSeq.pData = tmpSeq.pData;
	        //oneSeq.len = tmpSeq.len; 
	        oneSeq = oneSeq.multiple(b[i]);
	        
	        //tmpSeq = AddSeq(*pBz, oneSeq);
	        //FreeSeq(*pBz);
	        //pBz->pData = tmpSeq.pData;
	        //pBz->len = tmpSeq.len;
	        Bz = SeqUtil.add(Bz, oneSeq);

	        //FreeSeq(oneSeq);
	        
	        //oneSeq = CreateSeq(1);
	        //oneSeq.pData[0] = 1.0;
	        oneSeq = new RealSeq(1);
		    oneSeq.set(0, 1.0);
	    }

	    //*pAz = CreateSeq(1);
	    //pAz->pData[0] = 0.0;  
	    RealSeq Az = new RealSeq(1);
	    Az.set(0, 0.0);
	    
	    for(i = 0; i < N; i++)
	    {
	        for(j = 0; j < i; j++)
	        {
	            //tmpSeq = Conv(oneSeq, Nz);
	            //FreeSeq(oneSeq);
	            //oneSeq.pData = tmpSeq.pData;
	            //oneSeq.len = tmpSeq.len;
	            oneSeq = SeqUtil.conv(oneSeq, Nz);
	        }
	        for(j = 0; j < Max-i-1; j++)
	        {
	            //tmpSeq = Conv(oneSeq, Dz);
	            //FreeSeq(oneSeq);
	            //oneSeq.pData = tmpSeq.pData;
	            //oneSeq.len = tmpSeq.len;    
	            oneSeq = SeqUtil.conv(oneSeq, Dz);
	        }
	        //tmpSeq = MultipleConstant(oneSeq, aZ.pData[i]);
	        //FreeSeq(oneSeq);
	        //oneSeq.pData = tmpSeq.pData;
	        //oneSeq.len = tmpSeq.len;    
	        oneSeq = oneSeq.multiple(a[i]);
	        
	        //tmpSeq = AddSeq(*pAz, oneSeq);
	        //FreeSeq(*pAz);
	        //pAz->pData = tmpSeq.pData;
	        //pAz->len = tmpSeq.len;
	        Az = SeqUtil.add(Az, oneSeq);

	        //FreeSeq(oneSeq);
	        
	        //oneSeq = CreateSeq(1);
	        //oneSeq.pData[0] = 1.0;       
	        oneSeq = new RealSeq(1);
		    oneSeq.set(0, 1.0);
	    }   
	    
	    //归一化，让az[0] = 1.0 
	    double norm = Az.get(0);//pAz->pData[0];
/*	    for(i = 0; i < pBz->len; i++)
	    {
	        pBz->pData[i] /= norm;
	    } 
	    for(i = 0; i < pAz->len; i++)
	    {
	        pAz->pData[i] /= norm;
	    }*/
	    Bz = Bz.multiple(1/norm);
	    Az = Az.multiple(1/norm);
	    
	    Map rtn = new HashMap();
	    rtn.put("Bz", Bz);
	    rtn.put("Az", Az);
	    
	    return rtn;    
	}
	
	


}
