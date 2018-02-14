/**
 * Project Name:DSP_JAVA
 * File Name:ZT.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018年2月15日上午6:54:20
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.seq;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: ZT
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月15日 上午6:54:20 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class ZT {
	private ZT() {
		
	}
	
	//将系统函数H(Z)=b(Z)/a(Z)，通过变量的映射关系Z=N(z)/D(z)，转换为系统函数H(z)=B(z)/A(z)
	//用指定的映射关系（Nz和Dz）实现滤波器的频带转换，即从(bZ, aZ)转换为(Bz, Az)
	// 实现:
	// B(z)   b(Z)|
	// ---- = ----|     N(z)
	// A(z)   a(Z)|@Z = ----
	//	                D(z)
	public static Map<String, Object> ZMapping(RealSeq bZ, RealSeq aZ, RealSeq Nz, RealSeq Dz) {
	    double[] b = bZ.toArray();
	    double[] a = aZ.toArray();
	    
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
