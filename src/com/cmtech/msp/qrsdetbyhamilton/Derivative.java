package com.cmtech.msp.qrsdetbyhamilton;

public class Derivative {
	private final int DERIV_LENGTH;
	private final int[] derBuff;
	
	private int derI = 0 ;
	
	public Derivative(int SAMPLE_RATE) {
		double MS_PER_SAMPLE =  (double) 1000/ (double) SAMPLE_RATE;
		int MS10 = ((int) (10/ MS_PER_SAMPLE + 0.5));
		
		DERIV_LENGTH = MS10;
		derBuff = new int[DERIV_LENGTH];
		
		initialize();
	
	}
	
	public int getLength() {
		return DERIV_LENGTH;
	}
	
	public double getDelay() {
		return (double)DERIV_LENGTH/2;
	}
	
	public void initialize() {
		for(derI = 0; derI < DERIV_LENGTH; ++derI)
			derBuff[derI] = 0 ;
		derI = 0 ;
	}
	
	/*****************************************************************************
	*  deriv1 and deriv2 implement derivative approximations represented by
	*  the difference equation:
	*
	*	y[n] = x[n] - x[n - 10ms]
	*
	*  Filter delay is DERIV_LENGTH/2
	*****************************************************************************/

	public int filter(int x)
	{
		int y ;
	
		y = x - derBuff[derI] ;
		derBuff[derI] = x ;
		if(++derI == DERIV_LENGTH)
			derI = 0 ;
		return(y) ;
	}
}
