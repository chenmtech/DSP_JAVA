package com.cmtech.dsp.filter.design;

/*
Copyright (c) 2008 chenm
*/

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.cosh;
import static java.lang.Math.log;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sinh;
import static java.lang.Math.sqrt;

import java.util.HashMap;
import java.util.Map;

import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.util.SeqUtil;

/**
 * This class is for the design of analog lowpass filter. 
 * The type of filter which can be designed contains Butt, Cheb1, Cheb2, and Elliptical.
 * @author chenm
 *@version 2008-07
 */
public class AnalogLowpassFilterDesigner {
	private static final double EPS = 2.220446049250313E-016;
	
	private AnalogLowpassFilterDesigner() {			
	}
	
	/*public static AnalogFilter design(double Qp, double Qs, double Rp, double As, AFType afType){
		Map<String, Object> rtnMap = DesignAnalogLowPassFilter(Qp, Qs, Rp, As, afType);
		
		RealSeq bs = (RealSeq)rtnMap.get("BS");
		RealSeq as = (RealSeq)rtnMap.get("AS");
		
		AnalogFilter filter = new AnalogFilter(bs, as);
		AFPara para = new AFPara(new double[] {Qp,0.0}, new double[] {Qs,0.0},
						Rp, As, FilterType.LOWPASS, afType);
		filter.setFilterPara(para);
		return filter;
	}*/
	
	/**
	 * design an analog lowpass filter
	 * @param Qp analog angular frequency of passband, 2*PI*fp
	 * @param Qs analog angular frequency of stopband, 2*PI*fs
	 * @param Rp ripple at Qp, unit:dB
	 * @param As attenuation at Qs, unit:dB
	 * @param afType analog filter type, which can be BUTT, CHEB1, CHEB2, ELLIP
	 * @return a map including two elements, which stand for the system transfer function H(s). 
	 *               using RealSeq bs = (RealSeq)map.get("BS"), you can get the numerator of H(s)
	 *               using RealSeq as = (RealSeq)map.get("AS"), you can get the denominator of H(s)
	 */
	public static Map<String, Object> design(double Qp, double Qs, double Rp, double As, AnalogFilterType afType)//, RealSeq * pBs, RealSeq * pAs)
	{
	    switch(afType)
	    {
	    case BUTT:
	        return designAnalogButter(Qp, Qs, Rp, As);
	    case CHEB1:
	        return designAnalogCheby1(Qp, Qs, Rp, As);
	    case CHEB2:
	        return designAnalogCheby2(Qp, Qs, Rp, As);
	    case ELLIP:
	    		return designAnalogEllip(Qp, Qs, Rp, As);
	    default:
	    		return null;          
	    }
	}

	public static Map<String, Object> designAnalogButter(int N, double Qc)//, RealSeq * pBs, RealSeq * pAs)
	{
	    return designAnalogButterWithPara(N, Qc);
	} 

	public static Map<String, Object> designAnalogCheby1(int N, double Qp, double Rp)//, RealSeq * pBs, RealSeq * pAs)
	{
	    double E = sqrt( pow(10, Rp/10) - 1 );           //7.5.58
	    return designAnalogCheby1WithPara(N, Qp, E);
	}

	public static Map<String, Object> designAnalogCheby2(int N, double Qs, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
	    double E = 1.0/sqrt( pow(10, As/10) - 1 );           //7.5.75
	    return designAnalogCheby2WithPara(N, Qs, E);
	}	
	
	private static Map<String, Object> designAnalogButter(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
		Map<String, Object> tmpMap = getButterMagHPara(Qp, Qs, Rp, As);
	    int N = (int)tmpMap.get("N");
	    double Qc = (double)tmpMap.get("QC");
	    return designAnalogButterWithPara(N, Qc);
	    //printf("Analog Butt: N = %d, Qc = %f\n", N, Qc);
	}

	private static Map<String, Object> getButterMagHPara(double Qp, double Qs, double Rp, double As)//, int * pN, double * pQc)
	{
	    double tmp1 = pow(10, Rp/10) - 1;
	    double tmp2 = pow(10, As/10) - 1;
	    double lambda = Qs/Qp;                          //7.5.25a
	    double g = sqrt(tmp2/tmp1);                     //7.5.25b
	    int N = (int)( log10(g)/log10(lambda) ) + 1;      //7.5.26a
	    
	    double Qc1 = Qp/pow( tmp1, 1.0/2/N );       //7.5.27a
	    double Qc2 = Qs/pow( tmp2, 1.0/2/N );       //7.5.27b
	    double Qc= (Qc1 + Qc2)/2;                            //7.5.27c
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("N", N);
	    rtnMap.put("QC", Qc);
	    return rtnMap;
	}

	private static Map<String, Object> designAnalogButterWithPara(int N, double Qc)//, RealSeq * pBs, RealSeq * pAs)
	{
	    if( N <= 0) {return null;}
	    
	    //(7.5.8)    
	    RealSeq bs = new RealSeq(pow(Qc, N));
	    
	    //  (7.5.9) 
	    RealSeq as = null;
	    
	    if( N % 2 == 0 )   
	    {
	        as = new RealSeq(1.0);
	    }
	    else               
	    {
	        as = new RealSeq(1.0, Qc);
	    }    
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    if( N == 1) {
	    		rtnMap.put("BS", bs);
	    		rtnMap.put("AS", as);
	    		return rtnMap;    
	    }
	    
	    int biN = N/2;  
	    
	    RealSeq biSeq = new RealSeq(1.0, 0.0, Qc*Qc);
	    
	    int k = 0;
	    for(k = 1; k <= biN; k++)
	    {
	        biSeq.set(1, -2.0*Qc*cos( 0.5*PI*(1.0+(2.0*k-1.0)/N) ));
	        as = (RealSeq) SeqUtil.conv(as, biSeq);
	    }
		rtnMap.put("BS", bs);
		rtnMap.put("AS", as);
		return rtnMap;    
	}

	private static Map<String, Object> designAnalogCheby1(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
		Map<String, Object> tmpMap = getCheby1MagHPara(Qp, Qs, Rp, As);
	    int N = (int)tmpMap.get("N");
	    double Qc = (double)tmpMap.get("QC");
	    double E = (double)tmpMap.get("E");
	    return designAnalogCheby1WithPara(N, Qc, E);
	    //printf("Analog Cheb1: N = %d, Qc = %f, E = %f\n", N, Qc, E);   
	}

	private static Map<String, Object> getCheby1MagHPara(double Qp, double Qs, double Rp, double As)//, int * pN, double * pQc, double * pE)
	{
	    double Qc = Qp;    
	    double E = sqrt( pow(10, Rp/10) - 1 );           //7.5.58
	    
	    double tmp1 = pow(10, Rp/10) - 1;
	    double tmp2 = pow(10, As/10) - 1;
	    double lbd = Qs/Qp;                             //7.5.25a
	    double g = sqrt(tmp2/tmp1);                     //7.5.25b
	    int N = (int)( log10(g+sqrt(g*g-1))/log10(lbd+sqrt(lbd*lbd-1)) ) + 1;      //7.5.68
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("N", N);
	    rtnMap.put("QC", Qc);
	    rtnMap.put("E", E);
	    return rtnMap; 
	}

	private static Map<String, Object> designAnalogCheby1WithPara(int N, double Qc, double E)//, RealSeq * pBs, RealSeq * pAs)
	{
	    if( N <= 0) {return null;}

	    RealSeq bs = new RealSeq(pow(Qc, N)/E/pow(2, N-1));
	    
	    double gama = 1.0/E + sqrt(1.0/E/E+1);  //(7.5.44)
	    double tmp = pow(gama, 1.0/N);
	    double a = (tmp - 1/tmp)/2;     //(7.5.43a)
	    double b = (tmp + 1/tmp)/2;     //(7.5.43b)   
	     
	    RealSeq as = null;
	    
	    if( N % 2 == 0 )   
	    {
	        as = new RealSeq(1.0);
	    }
	    else  
	    {
	        as = new RealSeq(1.0, Qc*a);
	    }    
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    if( N == 1) {
	    		rtnMap.put("BS", bs);
	    		rtnMap.put("AS", as);
	    		return rtnMap;   
	    }
	    
	    int biN = N/2;  
	    
	    RealSeq biSeq = new RealSeq(1.0, 0.0, 0.0);

	    double re = 0.0;
	    double im = 0.0;
	    
	    int k = 0;
	    for(k = 1; k <= biN; k++)
	    {
	        re = -Qc*a*sin(PI*(2.0*k-1)/2.0/N);     //(7.5.41)
	        im = Qc*b*cos(PI*(2.0*k-1)/2.0/N);      //(7.5.41)

	        biSeq.set(1, -2.0*re);
	        biSeq.set(2, re*re+im*im);
	        
	        as = (RealSeq) SeqUtil.conv(as, biSeq);
	    }
	     
		rtnMap.put("BS", bs);
		rtnMap.put("AS", as);
		return rtnMap;   
	}

	private static Map<String, Object> designAnalogCheby2(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
		Map<String, Object> tmpMap = getCheby2MagHPara(Qp, Qs, Rp, As);
	    int N = (int)tmpMap.get("N");
	    double Qc = (double)tmpMap.get("QC");
	    double E = (double)tmpMap.get("E");
	    return designAnalogCheby2WithPara(N, Qc, E);
	    //printf("Analog Cheb2: N = %d, Qc = %f, E = %f\n", N, Qc, E);      
	}

	private static Map<String, Object> getCheby2MagHPara(double Qp, double Qs, double Rp, double As)//, int * pN, double * pQc, double * pE)
	{
	    double Qc = Qs;    
	    double E = 1.0/sqrt( pow(10, As/10) - 1 );           //7.5.75
	    
	    double tmp1 = pow(10, Rp/10) - 1;
	    double tmp2 = pow(10, As/10) - 1;
	    double lbd = Qs/Qp;                             //7.5.25a
	    double g = sqrt(tmp2/tmp1);                     //7.5.25b
	    int N = (int)( log10(g+sqrt(g*g-1))/log10(lbd+sqrt(lbd*lbd-1)) ) + 1;      //7.5.77
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    rtnMap.put("N", N);
	    rtnMap.put("QC", Qc);
	    rtnMap.put("E", E);
	    return rtnMap;    
	}

	private static Map<String, Object> designAnalogCheby2WithPara(int N, double Qc, double E)//, RealSeq * pBs, RealSeq * pAs)
	{
		if( N <= 0) {return null;}
	    
	    double gama = 1.0/E + sqrt(1.0/E/E+1);  //(7.5.44)
	    double tmp = pow(gama, 1.0/N);
	    double a = (tmp - 1/tmp)/2;     //(7.5.43a)
	    double b = (tmp + 1/tmp)/2;     //(7.5.43b)       
	    
	    RealSeq bs = new RealSeq(1.0);
	       
	    RealSeq as = null;
	    
	    if( N % 2 == 0 )  
	    {
	        as = new RealSeq(1.0);
	    }
	    else              
	    {
	        as = new RealSeq(1.0, Qc/a);
	    }    
	    
	    Map<String, Object> rtnMap = new HashMap<>();
	    if( N == 1) {
	    		bs.set(0, as.get(1));
	    		rtnMap.put("BS", bs);
	    		rtnMap.put("AS", as);
	    		return rtnMap;
	    } 
	    
	    int biN = N/2; 
	    
	    RealSeq biNomSeq = new RealSeq(1.0, 0.0, 0.0);
	    
	    RealSeq biDenSeq = new RealSeq(1.0, 0.0, 0.0);

	    double theta = 0.0;
	    double re = 0.0;
	    double im = 0.0;

	    double alpha = 0.0;
	    double beta = 0.0;
	    
	    int k = 0;
	    for(k = 1; k <= biN; k++)
	    {
	        theta = PI*(2.0*k-1)/2.0/N;
	        
	        biNomSeq.set(2, Qc*Qc/cos(theta)/cos(theta));  
	        
	        alpha = -a*sin(theta);  //(7.5.80a)
	        beta = b*cos(theta);    //(7.5.80b) 
	        
	        re = Qc*alpha/(alpha*alpha+beta*beta);       //(7.5.79b)
	        im = Qc*beta/(alpha*alpha+beta*beta);        //(7.5.79c)
	        
	        biDenSeq.set(1, -2.0*re);
	        biDenSeq.set(2, re*re+im*im);
	        
	        bs = (RealSeq) SeqUtil.conv(bs, biNomSeq);
	        as = (RealSeq) SeqUtil.conv(as, biDenSeq);
	    }
	    
	    double factor = as.get(as.size()-1)/bs.get(bs.size()-1);
	    bs = (RealSeq) bs.multiply(factor);

	    rtnMap.put("BS", bs);
		rtnMap.put("AS", as);
		return rtnMap;      
	}

	private static Map<String, Object> designAnalogEllip(double Qp, double Qs, double Rp, double As)//, RealSeq * pBs, RealSeq * pAs)
	{
		Map<String, Object> tmpMap = getEllipOrder(Qp, Qs, Rp, As);
		int N = (int)tmpMap.get("N");
		//double ActuralAs = (double)tmpMap.get("ACTURALAS");
		//printf("Analog Ellipti: N = %d, 瀹為檯杈惧埌鐨凙s = %lf\n", N, ActuralAs);
		return designAnalogEllip(N, Qp, Qs, Rp);
	}

	public static Map<String, Object> designAnalogEllip(int N, double Qp, double Qs, double Rp)//, RealSeq * pBs, RealSeq * pAs)
	{
		double k = Qp/Qs;
		
		double tmp = sqrt(sqrt(1-k*k));
		double u = (1-tmp)/2.0/(1+tmp);		//(5.11)
		double q = u + 2*pow(u, 5) + 15*pow(u, 9) + 150*pow(u, 13);		//(5.10)
		
		tmp = pow(10.0, Rp/20);
		double V = log((tmp+1)/(tmp-1))	/ N / 2;		//(5.12)
		
		double p0 = calculateP0ForEllip(q, V);		//(5.13)
		
		double W = sqrt( (1.0+p0*p0/k)*(1+p0*p0*k) );		//(5.14)
		
		int r = (N % 2 == 0) ? N/2 : (N-1)/2;
		
		Map<String, Object> tmpMap = calculateXandYForEllip(N, q, k);		//(5.15)鍜�(5.16) 
		RealSeq Xi = (RealSeq)tmpMap.get("XI");
		RealSeq Yi = (RealSeq)tmpMap.get("YI");
		
		RealSeq ai = new RealSeq(r);
		RealSeq bi = new RealSeq(r);
		RealSeq ci = new RealSeq(r);
		double X = 0.0;
		double Y = 0.0;
		int i = 0;
		for(i = 0; i< r; i++)
		{
			X = Xi.get(i);
			Y = Yi.get(i);
			ai.set(i, 1.0/X/X);		//(5.17)
			bi.set(i, 2*p0*Y/(1+p0*p0*X*X));			//(5.18)
			ci.set(i, (p0*p0*Y*Y + W*W*X*X)/(1.0+p0*p0*X*X)/(1.0+p0*p0*X*X));	//(5.19)
		}
		
		double H0 = (N % 2 == 0) ? pow(10.0, -Rp/20) : p0;
		for(i = 0; i < r; i++)
		{
			H0 *= (ci.get(i)/ai.get(i));
		}
		
		double alpha = sqrt(Qp*Qs);
		 
		ai = (RealSeq) ai.multiply(alpha*alpha);
		
		ci = (RealSeq) ci.multiply(alpha*alpha);
		
		bi = (RealSeq) bi.multiply(alpha);

		RealSeq bs = new RealSeq(1);
		RealSeq as = null;
		
		if(N % 2 == 0)
		{
			bs.set(0, H0);
			as = new RealSeq(1.0);
		}
		else
		{
			bs.set(0, H0*alpha);		
			as = new RealSeq(1.0, p0*alpha);		
		}

		RealSeq NomSeq = new RealSeq(1.0, 0.0, 0.0);
		RealSeq DenSeq = new RealSeq(1.0, 0.0, 0.0);
		for(i = 0; i < r; i++)
		{
			NomSeq.set(2, ai.get(i));
			bs = (RealSeq) SeqUtil.conv(bs, NomSeq);
			
			DenSeq.set(1, bi.get(i));
			DenSeq.set(2, ci.get(i));
			as = (RealSeq) SeqUtil.conv(as, DenSeq);
		}

		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("BS", bs);
		rtnMap.put("AS", as);
		return rtnMap;
	}

	private static Map<String, Object> getEllipOrder(double Qp, double Qs, double Rp, double As)//, int * pN, double * pActuralAs)
	{
		double k = Qp/Qs;
		
		double tmp = sqrt(sqrt(1-k*k));
		double u = (1-tmp)/2.0/(1+tmp);		//(5.2)
		double q = u + 2*pow(u, 5) + 15*pow(u, 9) + 150*pow(u, 13);		//(5.1)
		
		double D = ( pow(10.0, As/10)-1 )/( pow(10.0, Rp/10)-1 );		//(5.3)
		
		double n = log10(16*D)/log10(1.0/q);	//(5.4)
		int N = (int)n+1;
		
		double ActuralAs = 10*log10( 1+(pow(10.0, Rp/10)-1)/16.0/pow(q, N) );		//(5.5)
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("N", N);
		rtnMap.put("ACTURALAS", ActuralAs);
		return rtnMap;
	}

	private static double calculateP0ForEllip(double q, double V)
	{
		int m  = 0;
		double sum = 0.0;
		double term = 0.0;
		double nom = 0.0;
		double den = 0.0;
		int iternum = 10;
		for(m = 0; m < iternum; m++)
		{
			term = pow(-1.0, m)*pow(q, m*(m+1))*sinh((2*m+1)*V);
			sum += term;
		}
		nom = 2.0*sqrt(sqrt(q))*sum;	
		sum = 0.0;
		for(m = 1; m < iternum; m++)
		{
			term = pow(-1.0, m)*pow(q, m*m)*cosh(2*m*V);
			sum += term;
		}
		den = 1.0+2*sum;		
		return abs(nom/den);
	}

	private static Map<String, Object> calculateXandYForEllip(int N, double q, double k)//, RealSeq * pXi, RealSeq * pYi)
	{
		int r = (N % 2 == 0) ? N/2 : (N-1)/2;

		RealSeq Xi = new RealSeq(r);
		RealSeq Yi = new RealSeq(r);
		
		int i = 0;
		int m = 0;
		double miu = 0.0;
		double term = 0.0;
		double sum = 0.0;
		double nom = 0.0;
		double den = 0.0;
		double rlt = 0.0;
		int iternum = 10;
		for(i = 1; i <= r; i++)
		{
			sum = 0.0;		
			miu = (N % 2 == 0) ? i-0.5 : i;
			for(m = 0; m < iternum; m++)
			{
				term = pow(-1.0, m) * pow(q, m*(m+1)) * sin((2*m+1)*miu*PI/N);
				sum += term;			
			}
			nom = 2.0*sqrt(sqrt(q))*sum;
			sum = 0.0;
			for(m = 1; m < iternum; m++)
			{
				term = pow(-1.0, m) * pow(q, m*m) * cos(2*m*miu*PI/N);
				sum += term;			
			}	
			den = 1.0+2*sum;
			rlt = nom/den;
			Xi.set(i-1, rlt);
			Yi.set(i-1, sqrt( (1-rlt*rlt/k)*(1-k*rlt*rlt) ));		//(5.16)
		}		
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("XI", Xi);
		rtnMap.put("YI", Yi);
		return rtnMap;
	}

	@SuppressWarnings("unused")
	private static double CalculateCompleteEllipIntegral(double k)
	{
		double a = 1-k;
		double b = 1+k;
		double tmp = 0.0;
		while(abs(a-b) > EPS)
		{
			tmp = (a+b)/2.0;
			b = sqrt(a*b);
			a = tmp;
		}
		return (PI/2.0/a);
	}


}
