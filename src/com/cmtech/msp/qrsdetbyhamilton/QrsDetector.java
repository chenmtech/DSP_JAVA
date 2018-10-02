/**
 * Project Name:DSP_JAVA
 * File Name:QrsDetector.java
 * Package Name:com.cmtech.msp.qrsdetbyhamilton
 * Date:2018年9月27日上午5:42:57
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.msp.qrsdetbyhamilton;

/**
 * ClassName: QrsDetector
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年9月27日 上午5:42:57 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class QrsDetector {
	private static final double TH = 0.3125;
	
	private final int sampleRate;		// 采样频率
	private final int value1mV;			// 1mV量化值
	private final int PRE_BLANK;
	private final int MIN_PEAK_AMP;
	
	private final int MS95;
	private final int MS150;
	private final int MS220;
	private final int MS360;
	private final int MS1000;
	private final int MS1500;
	
	private final int FILTER_DELAY;
	private final int DER_DELAY;
	private final int WINDOW_WIDTH;
	
	private final QrsFilter filter;
	private final Derivative derivative;
	
	private int det_thresh;		// detection threshold
	private int qpkcnt = 0 ;   	// QRS波峰的个数
	private final int[] qrsbuf = new int[8];	// QRS波峰值的8个缓存
	private final int[] noise = new int[8];		// 噪声峰值的8个缓存
	private final int[] rrbuf = new int[8];		// R-R间隔的8个缓存
	private final int[] rsetBuff = new int[8];	// 未知
	private int rsetCount = 0 ;
	private int nmean;		// 噪声缓存的均值
	private int qmean;		// QRS波峰缓存的均值
	private int rrmean;		// R-R间隔缓存的均值
	private int count;		// 未知
	private int sbpeak = 0;	// 未知
	private int sbloc;
	private int sbcount;
	private int[] maxder = new int[1];
	private int lastmax;
	private int initBlank;
	private int initMax;
	private int preBlankCnt;
	private int tempPeak;
	
	private final int[] DDBuffer;	// derivative data buffer；
	private int DDPtr;				// derivative data buffer point
	private int Dly = 0;
	
	private int max = 0;
	private int timeSinceMax = 0;
	private int lastDatum = 0;
	
	private int RRCount = 0;
	private boolean firstPeak = true;
	
	public QrsDetector(int sampleRate, int value1mV) {
		this.sampleRate = sampleRate;
		this.value1mV = value1mV;
		
		double MS_PER_SAMPLE =	( (double) 1000/ (double) sampleRate);

		MS95 =	((int) (95/MS_PER_SAMPLE + 0.5));
		MS150 =	((int) (150/MS_PER_SAMPLE + 0.5));
		MS220 =	((int) (220/MS_PER_SAMPLE + 0.5));
		MS360 = ((int) (360/MS_PER_SAMPLE + 0.5));
		MS1000 = sampleRate;		
		MS1500 = ((int) (1500/MS_PER_SAMPLE));
		
		int MS195 =	((int) (195/MS_PER_SAMPLE + 0.5));
		PRE_BLANK = MS195;
		
		MIN_PEAK_AMP = value1mV*7/200;
		
		filter = new QrsFilter(sampleRate);
		derivative = new Derivative(sampleRate);
		
		FILTER_DELAY = filter.getFilterDelay();
		WINDOW_WIDTH = filter.getWindowWidth();
		
		int MS100 =	((int) (100/MS_PER_SAMPLE + 0.5));
		DER_DELAY = WINDOW_WIDTH + FILTER_DELAY + MS100;
		
		DDBuffer = new int[DER_DELAY];

		initialize();
	}
	
	private void initialize() {
		for(int i = 0; i < 8; ++i)
		{
			noise[i] = 0 ;	/* Initialize noise buffer */
			rrbuf[i] = MS1000 ;/* and R-to-R interval buffer. */
		}
		maxder[0] = 0; 
		qpkcnt = lastmax = count = sbpeak = 0 ;
		initBlank = initMax = preBlankCnt = DDPtr = 0 ;
		sbcount = MS1500 ;
		
		filter.initialize();
		derivative.initialize();
		
		Peak(0, true) ;
		
		RRCount = 0;
		firstPeak = true;
	}
	
	public int outputRRInterval(int datum) {
		int RRInterval = 0;
		
		int delay = detectQrs(datum);
		if(delay != 0) {
			if(firstPeak) {
				firstPeak = false;
			} else {
				RRInterval = RRCount-delay+1;
			}
			RRCount = delay;
		} else {
			RRCount++;
		}
		return RRInterval;
	}
	
	public int outputHR(int datum) {
		int RRInterval = outputRRInterval(datum);
		if(RRInterval != 0) {
			return (int)((double)(sampleRate*60)/RRInterval + 0.5);
		} else {
			return 0;
		}
	}
	
	public int detectQrs( int datum )
	{
		int fdatum, QrsDelay = 0 ;
		int i, newPeak, aPeak ;
	
		fdatum = filter.filter(datum) ;	/* Filter data. */
	
		/* Wait until normal detector is ready before calling early detections. */
	
		aPeak = Peak(fdatum, false) ;
		if(aPeak < MIN_PEAK_AMP)
			aPeak = 0 ;
	
		// Hold any peak that is detected for 200 ms
		// in case a bigger one comes along.  There
		// can only be one QRS complex in any 200 ms window.
	
		newPeak = 0 ;
		if((aPeak!= 0) && (preBlankCnt == 0))			// If there has been no peak for 200 ms
		{												// save this one and start counting.
			tempPeak = aPeak ;
			preBlankCnt = PRE_BLANK ;			// MS200
		}
	
		else if((aPeak == 0) && (preBlankCnt != 0))	// If we have held onto a peak for
		{										// 200 ms pass it on for evaluation.
			if(--preBlankCnt == 0)
				newPeak = tempPeak ;
		}
	
		else if(aPeak != 0)							// If we were holding a peak, but
		{										// this ones bigger, save it and
			if(aPeak > tempPeak)				// start counting to 200 ms again.
			{
				tempPeak = aPeak ;
				preBlankCnt = PRE_BLANK ; // MS200
			}
			else if(--preBlankCnt == 0)
				newPeak = tempPeak ;
		}
	
		/* Save derivative of raw signal for T-wave and baseline
		   shift discrimination. */
		
		DDBuffer[DDPtr] = derivative.filter(datum);
		if(++DDPtr == DER_DELAY)
			DDPtr = 0 ;
	
		/* Initialize the qrs peak buffer with the first eight 	*/
		/* local maximum peaks detected.						*/
	
		if( qpkcnt < 8 )
		{
			++count ;
			if(newPeak > 0) count = WINDOW_WIDTH ;
			if(++initBlank == MS1000)
			{
				//System.out.println("Get one peak!");
				
				initBlank = 0 ;
				qrsbuf[qpkcnt] = initMax ;
				initMax = 0 ;
				++qpkcnt ;
				if(qpkcnt == 8)
				{
					qmean = mean( qrsbuf ) ;
					nmean = 0 ;
					rrmean = MS1000 ;
					sbcount = MS1500+MS150 ;
					det_thresh = thresh(qmean,nmean) ;
				}
			}
			if( newPeak > initMax )
				initMax = newPeak ;
		}
	
		else	/* Else test for a qrs. */
		{
			
			++count ;
			if(newPeak > 0)
			{
				
				
				/* Check for maximum derivative and matching minima and maxima
				   for T-wave and baseline shift rejection.  Only consider this
				   peak if it doesn't seem to be a base line shift. */
				   
				if(!BLSCheck(DDBuffer, DDPtr, maxder))
				{
	
	
					// Classify the beat as a QRS complex
					// if the peak is larger than the detection threshold.
	
					if(newPeak > det_thresh)
					{
						pushToHead(qrsbuf, newPeak);
						qmean = mean(qrsbuf) ;
						det_thresh = thresh(qmean,nmean) ;
						pushToHead(rrbuf, count - WINDOW_WIDTH);
						rrmean = mean(rrbuf) ;
						sbcount = rrmean + (rrmean >> 1) + WINDOW_WIDTH ;
						count = WINDOW_WIDTH ;
	
						sbpeak = 0 ;
	
						lastmax = maxder[0] ;
						maxder[0] = 0 ;
						QrsDelay =  WINDOW_WIDTH + FILTER_DELAY ;
						initBlank = initMax = rsetCount = 0 ;
					}
	
					// If a peak isn't a QRS update noise buffer and estimate.
					// Store the peak for possible search back.
	
	
					else
					{
						pushToHead(noise, newPeak);
						nmean = mean(noise) ;
						det_thresh = thresh(qmean,nmean) ;
	
						// Don't include early peaks (which might be T-waves)
						// in the search back process.  A T-wave can mask
						// a small following QRS.
	
						if((newPeak > sbpeak) && ((count-WINDOW_WIDTH) >= MS360))
						{
							sbpeak = newPeak ;
							sbloc = count  - WINDOW_WIDTH ;
						}
					}
				}
			}
			
			/* Test for search back condition.  If a QRS is found in  */
			/* search back update the QRS buffer and det_thresh.      */
	
			if((count > sbcount) && (sbpeak > (det_thresh >> 1)))
			{
				pushToHead(qrsbuf, sbpeak);
				qmean = mean(qrsbuf) ;
				det_thresh = thresh(qmean,nmean) ;
				pushToHead(rrbuf, sbloc);
				rrmean = mean(rrbuf) ;
				sbcount = rrmean + (rrmean >> 1) + WINDOW_WIDTH ;
				QrsDelay = count = count - sbloc ;
				QrsDelay += FILTER_DELAY ;
				sbpeak = 0 ;
				lastmax = maxder[0] ;
				maxder[0] = 0 ;
	
				initBlank = initMax = rsetCount = 0 ;
			}
		}
	
		// In the background estimate threshold to replace adaptive threshold
		// if eight seconds elapses without a QRS detection.
	
		if( qpkcnt == 8 )
		{
			if(++initBlank == MS1000)
			{
				initBlank = 0 ;
				rsetBuff[rsetCount] = initMax ;
				initMax = 0 ;
				++rsetCount ;
	
				// Reset threshold if it has been 8 seconds without
				// a detection.
	
				if(rsetCount == 8)
				{
					for(i = 0; i < 8; ++i)
					{
						qrsbuf[i] = rsetBuff[i] ;
						noise[i] = 0 ;
					}
					qmean = mean( rsetBuff ) ;
					nmean = 0 ;
					rrmean = MS1000 ;
					sbcount = MS1500+MS150 ;
					det_thresh = thresh(qmean,nmean) ;
					initBlank = initMax = rsetCount = 0 ;
				}
			}
			
			if( newPeak > initMax )
				initMax = newPeak ;
		}
	
		return(QrsDelay) ;
	}
	
	
	
	
	
	/**************************************************************
	* peak() takes a datum as input and returns a peak height
	* when the signal returns to half its peak height. If the datum doesn't stand for 
	* a peak, then a zero is returned. 
	**************************************************************/

	private int Peak( int datum, boolean init )
	{
		int pk = 0 ;
	
		if(init)
			max = timeSinceMax = 0 ;
			
		if(timeSinceMax > 0)
			++timeSinceMax ;
	
		if((datum > lastDatum) && (datum > max))
		{
			max = datum ;
			if(max > 2)
				timeSinceMax = 1 ;
		}
	
		else if(datum < (max >> 1))
		{
			pk = max ;
			max = 0 ;
			timeSinceMax = 0 ;
			Dly = 0 ;
		}
	
		else if(timeSinceMax > MS95)
		{
			pk = max ;
			max = 0 ;
			timeSinceMax = 0 ;
			Dly = 3 ;
		}
		
		lastDatum = datum ;
		return(pk) ;
	}

	/********************************************************************
	mean returns the mean of an array of integers.  It uses a slow
	sort algorithm, but these arrays are small, so it hardly matters.
	********************************************************************/

	private int mean(int[] array)
	{
		int length = array.length;
		long sum ;
		int i ;
	
		for(i = 0, sum = 0; i < length; ++i)
			sum += array[i] ;
		sum /= length ;
		return((int)sum) ;
	}
	
	/****************************************************************************
	 thresh() calculates the detection threshold from the qrs mean and noise
	 mean estimates.
	****************************************************************************/

	private int thresh(int qmean, int nmean)
	{
		int thrsh, dmed ;
		double temp ;
		dmed = qmean - nmean ;
	/*	thrsh = nmean + (dmed>>2) + (dmed>>3) + (dmed>>4); */
		temp = dmed ;
		temp *= TH ;
		dmed = (int)temp ;
		thrsh = nmean + dmed ; /* dmed * THRESHOLD */
		return(thrsh) ;
	}
	
	/***********************************************************************
	BLSCheck() reviews data to see if a baseline shift has occurred.
	This is done by looking for both positive and negative slopes of
	roughly the same magnitude in a 220 ms window.
***********************************************************************/

	private boolean BLSCheck(int[] dBuf,int dbPtr,int[] maxder)
	{
		int max, min, maxt, mint, t, x ;
		max = min = 0 ;
		maxt = mint = 0;
	
		for(t = 0; t < MS220; ++t)
		{
			x = dBuf[dbPtr] ;
			if(x > max)
			{
				maxt = t ;
				max = x ;
			}
			else if(x < min)
			{
				mint = t ;
				min = x;
			}
			if(++dbPtr == DER_DELAY)
				dbPtr = 0 ;
		}
	
		maxder[0] = max ;
		min = -min ;
		
		/* Possible beat if a maximum and minimum pair are found
			where the interval between them is less than 150 ms. */
		   
		if((max > (min>>3)) && (min > (max>>3)) &&
			(Math.abs(maxt - mint) < MS150))
			return false ;
			
		else
			return true ;
	}

	private void pushToHead(int[] buf, int data) {
		for(int i = buf.length-1; i > 0; i--) {
			buf[i] = buf[i-1];
		}
		buf[0] = data;
	}
	

}
