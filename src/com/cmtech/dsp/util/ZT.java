/**
 * Project Name:DSP_JAVA
 * File Name:ZT.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018年2月15日上午6:54:20
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.util;

import java.util.HashMap;
import java.util.Map;

import com.cmtech.dsp.seq.RealSeq;

/**
 * ClassName: ZT
 * Function: 实现Z变换的相关运算. 
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
	
	
	/**
	 * 
	 * ZMapping: 实现Z变换函数的映射
	 * 将系统函数H(Z)=b(Z)/a(Z)，通过变量的映射关系Z=N(z)/D(z)，转换为系统函数H(z)=B(z)/A(z)
	 * 可用指定的映射关系（Nz和Dz）实现滤波器的频带转换，即从(bZ, aZ)转换为(Bz, Az)
	 * 实现:
	 * B(z)   b(Z)|
	 * ---- = ----|     N(z)
	 * A(z)   a(Z)|@Z = ----
	 * 	                D(z)
	 *
	 * @author bme
	 * @param bZ ZT函数的分子多项式系数组
	 * @param aZ ZT函数的分母多项式系数组
	 * @param Nz 映射关系分子多项式系数组
	 * @param Dz 映射关系分母多项式系数组
	 * @return 映射表，包含键值{"BZ","AZ"}
	 * BZ:RealSeq,代表变换后分子多项式系数组RealSeq
	 * AZ:RealSeq,代表变换后分母多项式系数组RealSeq
	 * @since JDK 1.6
	 */
	public static Map<String, Object> ZMapping(RealSeq bZ, RealSeq aZ, RealSeq Nz, RealSeq Dz) {
		int M = bZ.size();
	    int N = aZ.size();
	    int Max = (M > N)? M : N;

	    RealSeq oneSeq = new RealSeq(1.0);

	    RealSeq Bz = new RealSeq(0.0);
	    
	    int i = 0;
	    int j = 0;
	    for(i = 0; i < M; i++)
	    {
	        for(j = 0; j < i; j++)
	        {
	            oneSeq = (RealSeq) SeqUtil.conv(oneSeq, Nz);
	        }
	        for(j = 0; j < Max-i-1; j++)
	        {  
	            oneSeq = (RealSeq) SeqUtil.conv(oneSeq, Dz);
	        }
	        oneSeq = (RealSeq) oneSeq.multiple(bZ.get(i));

	        Bz = (RealSeq) SeqUtil.add(Bz, oneSeq);

	        oneSeq = new RealSeq(1.0);
	    }

	    RealSeq Az = new RealSeq(0.0);
	    
	    for(i = 0; i < N; i++)
	    {
	        for(j = 0; j < i; j++)
	        {
	            oneSeq = (RealSeq) SeqUtil.conv(oneSeq, Nz);
	        }
	        for(j = 0; j < Max-i-1; j++)
	        {
	            oneSeq = (RealSeq) SeqUtil.conv(oneSeq, Dz);
	        }

	        oneSeq = (RealSeq) oneSeq.multiple(aZ.get(i));

	        Az = (RealSeq) SeqUtil.add(Az, oneSeq);
     
	        oneSeq = new RealSeq(1.0);
	    }   
	    
	    //归一化，让Az[0] = 1.0 
	    double norm = Az.get(0);
	    Bz = (RealSeq) Bz.divide(norm);
	    Az = (RealSeq) Az.divide(norm);
	    
	    Map<String, Object> rtn = new HashMap<>();
	    rtn.put("BZ", Bz);
	    rtn.put("AZ", Az);
	    
	    return rtn;    
	}
}
