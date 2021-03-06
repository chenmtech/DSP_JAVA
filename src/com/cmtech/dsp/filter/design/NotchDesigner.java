package com.cmtech.dsp.filter.design;

import java.util.HashMap;
import java.util.Map;

import com.cmtech.dsp.filter.IIRFilter;
import com.cmtech.dsp.seq.RealSeq;

/**
 * ClassName: NotchDesigner
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月17日 下午4:23:49 
 *
 * @author bme
 * @version 0.0.1
 * @since JDK 1.6
 */
public class NotchDesigner {
	private NotchDesigner() {		
	}
	
	public static IIRFilter design(double w0, double deltaw) {
		Double[] b = new Double[3];
		Double[] a = new Double[3];
		double r = 1-deltaw/2;
		double cosw0 = Math.cos(w0);
		b[0] = (1+r*r-2*r*cosw0)/(2-2*cosw0);
		b[1] = -2*cosw0*b[0];
		b[2] = b[0];
		a[0] = 1.0;
		a[1] = -2*r*cosw0;
		a[2] = r*r;
		return new IIRFilter(b,a);
	}
	
	public static IIRFilter design(double f0, double deltaf, double fs) {
		return design(2*Math.PI*f0/fs, 2*Math.PI*deltaf/fs);
	}
	
	public static Map<String, Object> designHz(double w0, double deltaw) {
		Double[] b = new Double[3];
		Double[] a = new Double[3];
		double r = 1-deltaw/2;
		double cosw0 = Math.cos(w0);
		b[0] = (1+r*r-2*r*cosw0)/(2-2*cosw0);
		b[1] = -2*cosw0*b[0];
		b[2] = b[0];
		a[0] = 1.0;
		a[1] = -2*r*cosw0;
		a[2] = r*r;
		
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("BZ", new RealSeq(b));
	    rtnMap.put("AZ", new RealSeq(a));
	    return rtnMap;
	}
	
	public static Map<String, Object> designHz(double f0, double deltaf, double fs) {
		return designHz(2*Math.PI*f0/fs, 2*Math.PI*deltaf/fs);
	}
	
}
