package com.cmtech.dsp.seq;

import java.io.Serializable;

/**
 * ClassName: Complex
 * Function: 复数. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月2日 上午6:00:36 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class Complex implements Serializable{
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = 1L;
	private double real;
	private double imag;
	
	/**
	 * Creates a new instance of Complex.
	 *
	 */
	public Complex()
	{
		real = 0.0;
		imag = 0.0;
	}
	
	public Complex(double re, double im)
	{
		real = re;
		imag = im;
	}
	
	public Complex(Complex c) {
		real = c.real;
		imag = c.imag;
	}

	/**
	 * getReal:获取实部
	 *
	 * @author bme
	 * @return
	 * @since JDK 1.6
	 */
	public double getReal() {
		return real;
	}

	public void setReal(double real) {
		this.real = real;
	}

	public double getImag() {
		return imag;
	}

	public void setImag(double imag) {
		this.imag = imag;
	}
	
	public void set(double re, double im) {
		real = re;
		imag = im;
	}
	
	public Complex add(double a) {
		real += a;
		imag += a;
		return this;
	}
	
	/**
	 * subtract:减去一个实数
	 *
	 * @author bme
	 * @param a
	 * @since JDK 1.6
	 */
	public Complex subtract(double a) {
		real -= a;
		imag -= a;
		return this;
	}
	
	public Complex add(Complex c) {
		real += c.real;
		imag += c.imag;
		return this;
	}
	
	public Complex subtract(Complex c) {
		real -= c.real;
		imag -= c.real;
		return this;
	}
	
	
	public Complex multiple(double a) {
		real *= a;
		imag *= a;
		return this;
	}
	
	public Complex multiple(Complex c) {
		double re = real*c.real - imag*c.imag;
		double im = real*c.imag + imag*c.real;
		real = re;
		imag = im;
		return this;
	}
	
	public Complex divide(double a) {
		real /= a;
		imag /= a;
		return this;
	}
	
	public Complex divide(Complex c) {
		double mod = c.real*c.real + c.imag*c.imag;
		double re = (real*c.real + imag*c.imag)/mod;
		double im = (imag*c.real - real*c.imag)/mod;
		real = re;
		imag = im;
		return this;
	}
	
	public double abs() {
		return Math.sqrt(real*real+imag*imag);
	}
	
	public double angle() {
		return Math.atan2(imag, real);
	}

	@Override
	public String toString() {
		return real + "+i*" + imag;
	}
	
	@Override
	public boolean equals(Object otherObject) {
		if(this == otherObject) return true;
		if(otherObject == null) return false;
		if(getClass() != otherObject.getClass()) return false;
		Complex other = (Complex)otherObject;
		return  real == other.real && imag == other.imag;
	}
	
	
	
	@Override
	public int hashCode() {
		return 7*Double.hashCode(real) + 11*Double.hashCode(imag);
	}

	public static Complex add(Complex c, double a) {
		Complex out = new Complex(c);
		out.add(a);
		return out;
	}
	
	public static Complex add(Complex c1, Complex c2) {
		Complex out = new Complex(c1);
		out.add(c2);
		return out;
	}
	
	public static Complex multiple(Complex c, double a) {
		Complex out = new Complex(c);
		out.multiple(a);
		return out;
	}
	
	public static Complex multiple(Complex c1, Complex c2) {
		Complex out = new Complex(c1);
		out.multiple(c2);
		return out;
	}
	
	public static Complex divide(Complex c1, Complex c2) {
		Complex out = new Complex(c1);
		out.divide(c2);
		return out;
	}
	
	

}
