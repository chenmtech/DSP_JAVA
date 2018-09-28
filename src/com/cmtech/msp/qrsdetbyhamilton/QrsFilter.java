/**
 * Project Name:DSP_JAVA
 * File Name:QrsFilter.java
 * Package Name:com.cmtech.msp.qrsdetbyhamilton
 * Date:2018年9月27日上午5:44:41
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.msp.qrsdetbyhamilton;

/**
 * ClassName: QrsFilter
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年9月27日 上午5:44:41 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class QrsFilter {
	private final int sampleRate;
	private final LowpassFilter lpFilter;
	private final HighpassFilter hpFilter;
	private final Derivative derivative;
	private final MAverageFilter maFilter;
	
	public QrsFilter(int sampleRate) {
		this.sampleRate = sampleRate;
		lpFilter = new LowpassFilter(sampleRate);
		hpFilter = new HighpassFilter(sampleRate);
		derivative = new Derivative(sampleRate);
		maFilter = new MAverageFilter(sampleRate);
		
	}
	
	public void initialize() {
		lpFilter.initialize();
		hpFilter.initialize();
		derivative.initialize();
		maFilter.initialize();
	}
	
	/******************************************************************************
	* Syntax:
	*	int QRSFilter(int datum, int init) ;
	* Description:
	*	QRSFilter() takes samples of an ECG signal as input and returns a sample of
	*	a signal that is an estimate of the local energy in the QRS bandwidth.  In
	*	other words, the signal has a lump in it whenever a QRS complex, or QRS
	*	complex like artifact occurs.  The filters were originally designed for data
	*  sampled at 200 samples per second, but they work nearly as well at sample
	*	frequencies from 150 to 250 samples per second.
	*
	*	The filter buffers and static variables are reset if a value other than
	*	0 is passed to QRSFilter through init.
	*******************************************************************************/

	public int filter(int datum)
	{
		int fdatum ;
	
		fdatum = lpFilter.filter(datum) ;		// Low pass filter data.
		fdatum = hpFilter.filter(fdatum); 	// High pass filter data.
		fdatum = derivative.filter(fdatum) ;	// Take the derivative.
		fdatum = Math.abs(fdatum) ;				// Take the absolute value.
		fdatum = maFilter.filter(fdatum) ;	// Average over an 80 ms window .
		return(fdatum) ;
	
	}
	
	public String getLengthInfo() {
		String str = "LPLength:" + lpFilter.getLength() +
						  "; HPLength:" + hpFilter.getLength() +
						  "; Derivative Length:" + derivative.getLength() +
						  "; MA Length: " + maFilter.getLength();
		return str;
	}
	
	public int getFilterDelay() {
		double MS_PER_SAMPLE =	( (double) 1000/ (double) sampleRate);
		int MS195 =	((int) (195/MS_PER_SAMPLE + 0.5));
		int PRE_BLANK = MS195;
		double delay = PRE_BLANK;
		delay += (derivative.getDelay()+lpFilter.getDelay()+hpFilter.getDelay());
		return (int)delay;
	}
	
	public int getWindowWidth() {
		return maFilter.getLength();
	}
	
}
