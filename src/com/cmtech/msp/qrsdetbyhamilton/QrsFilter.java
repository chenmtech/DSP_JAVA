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
	
	public QrsFilter(int sampleRate) {
		this.sampleRate = sampleRate;
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

	public int filter(int datum, boolean init)
	{
		int fdatum ;
	
		if(init)
		{
			hpfilt( 0, init ) ;		// Initialize filters.
			lpfilt( 0, init ) ;
			mvwint( 0, 1 ) ;
			deriv1( 0, 1 ) ;
			deriv2( 0, 1 ) ;
		}
	
		fdatum = lpfilt( datum, false ) ;		// Low pass filter data.
		fdatum = hpfilt( fdatum, 0 ) ;	// High pass filter data.
		fdatum = deriv2( fdatum, 0 ) ;	// Take the derivative.
		fdatum = abs(fdatum) ;				// Take the absolute value.
		fdatum = mvwint( fdatum, 0 ) ;	// Average over an 80 ms window .
		return(fdatum) ;
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

	private int lpfilt( int datum , boolean init)
	{
		static long y1 = 0, y2 = 0 ;
		static int data[LPBUFFER_LGTH], ptr = 0 ;
		long y0 ;
		int output, halfPtr ;
		if(init)
		{
			for(ptr = 0; ptr < LPBUFFER_LGTH; ++ptr)
				data[ptr] = 0 ;
			y1 = y2 = 0 ;
			ptr = 0 ;
		}
		halfPtr = ptr-(LPBUFFER_LGTH/2) ;	// Use halfPtr to index
		if(halfPtr < 0)							// to x[n-6].
			halfPtr += LPBUFFER_LGTH ;
		y0 = (y1 << 1) - y2 + datum - (data[halfPtr] << 1) + data[ptr] ;
		y2 = y1;
		y1 = y0;
		output = y0 / ((LPBUFFER_LGTH*LPBUFFER_LGTH)/4);
		data[ptr] = datum ;			// Stick most recent sample into
		if(++ptr == LPBUFFER_LGTH)	// the circular buffer and update
			ptr = 0 ;					// the buffer pointer.
		return(output) ;
	}


}
