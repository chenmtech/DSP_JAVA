package com.cmtech.msp.qrsdetbyhamilton;

public class HighpassFilter {
	private final int HPBUFFER_LGTH;
	private final int[] data;
	
	private long y=0 ;
	private int ptr = 0 ;
	
	public HighpassFilter(int SAMPLE_RATE) {
		double MS_PER_SAMPLE =  (double) 1000/ (double) SAMPLE_RATE;
		int MS125 =	((int) (125/MS_PER_SAMPLE + 0.5));
		
		HPBUFFER_LGTH = (MS125/2)*2+1;	// 保证为奇数
		data = new int[HPBUFFER_LGTH];
		
		initialize();
	
	}
	
	public int getLength() {
		return HPBUFFER_LGTH;
	}
	
	public double getDelay() {
		return ((double)HPBUFFER_LGTH-1.0)/2;
	}
	
	public void initialize() {
		for(ptr = 0; ptr < HPBUFFER_LGTH; ++ptr)
			data[ptr] = 0 ;
		ptr = 0 ;
		y = 0 ;
		
		filter(0);
	}
	
	/******************************************************************************
	*  hpfilt() implements the high pass filter represented by the following
	*  difference equation:
	*
	*	y[n] = y[n-1] + x[n] - x[n-128 ms]
	*	z[n] = x[n-64 ms] - y[n] ;
	*
	*  Filter delay is (HPBUFFER_LGTH-1)/2
	******************************************************************************/

	public int filter( int datum )
	{
		int z, halfPtr ;
	
		y += datum - data[ptr];
		halfPtr = ptr-(HPBUFFER_LGTH/2);
		if(halfPtr < 0)
			halfPtr += HPBUFFER_LGTH ;
		z = (int) (data[halfPtr] - (y / HPBUFFER_LGTH));
	
		data[ptr] = datum ;
		if(++ptr == HPBUFFER_LGTH)
			ptr = 0 ;
	
		return( z );
	}

}
