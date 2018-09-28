package com.cmtech.msp.qrsdetbyhamilton;

public class LowpassFilter {
	private final int LPBUFFER_LGTH;
	private final int[] data;
	
	private long y1 = 0;
	private long y2 = 0;
	private int ptr = 0;
	
	public LowpassFilter(int SAMPLE_RATE) {
		double MS_PER_SAMPLE =  (double) 1000/ (double) SAMPLE_RATE;
		int MS25 = (int) (25/MS_PER_SAMPLE + 0.5);
		
		LPBUFFER_LGTH = 2*MS25;		// 一定是偶数
		data = new int[LPBUFFER_LGTH];
		
		initialize();
		
	}
	
	public int getLength() {
		return LPBUFFER_LGTH;
	}
	
	public double getDelay() {
		return (double)LPBUFFER_LGTH/2-1.0;
	}

	public void initialize() {
		for(ptr = 0; ptr < LPBUFFER_LGTH; ++ptr)
			data[ptr] = 0 ;
		y1 = y2 = 0 ;
		ptr = 0 ;
		
		filter(0);
	}
	
	/*************************************************************************
	*  lpfilt() implements the digital filter represented by the difference
	*  equation:
	*
	* 	y[n] = 2*y[n-1] - y[n-2] + x[n] - 2*x[t-24 ms] + x[t-48 ms]
	*
	*	Note that the filter delay is (LPBUFFER_LGTH/2)-1
	*
	**************************************************************************/
	public int filter( int datum)
	{
		long y0 ;
		int output, halfPtr ;

		halfPtr = ptr-(LPBUFFER_LGTH/2) ;	// Use halfPtr to index
		if(halfPtr < 0)							// to x[n-6].
			halfPtr += LPBUFFER_LGTH ;
		y0 = (y1 << 1) - y2 + datum - (data[halfPtr] << 1) + data[ptr] ;
		y2 = y1;
		y1 = y0;
		output = (int) (y0 / ((LPBUFFER_LGTH*LPBUFFER_LGTH)/4));
		data[ptr] = datum ;			// Stick most recent sample into
		if(++ptr == LPBUFFER_LGTH)	// the circular buffer and update
			ptr = 0 ;					// the buffer pointer.
		return(output) ;
	}
	
	
}
