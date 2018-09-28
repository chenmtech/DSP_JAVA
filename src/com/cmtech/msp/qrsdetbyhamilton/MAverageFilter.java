package com.cmtech.msp.qrsdetbyhamilton;

public class MAverageFilter {
	private final int WINDOW_WIDTH;
	private final int[] data;
	
	private long sum = 0 ;
	private int ptr = 0 ;

	public MAverageFilter(int SAMPLE_RATE) {
		double MS_PER_SAMPLE =  (double) 1000/ (double) SAMPLE_RATE;
		int MS80	= ((int) (80/MS_PER_SAMPLE + 0.5));
		
		WINDOW_WIDTH = MS80;
		data = new int[WINDOW_WIDTH];
		
		initialize();
	
	}
	
	public int getLength() {
		return WINDOW_WIDTH;
	}
	
	public void initialize() {
		for(ptr = 0; ptr < WINDOW_WIDTH ; ++ptr)
			data[ptr] = 0 ;
		sum = 0 ;
		ptr = 0 ;
		
		filter(0);
	}
	
	/*****************************************************************************
	* mvwint() implements a moving window integrator.  Actually, mvwint() averages
	* the signal values over the last WINDOW_WIDTH samples.
	*****************************************************************************/

	public int filter(int datum)
	{
		int output;
		
		sum += datum ;
		sum -= data[ptr] ;
		data[ptr] = datum ;
		if(++ptr == WINDOW_WIDTH)
			ptr = 0 ;
		
		if((sum / WINDOW_WIDTH) > 32000)
			output = 32000 ;
		else
			output = (int) (sum / WINDOW_WIDTH) ;
		return(output) ;
	}
}
