package com.cmtech.dsp.util;
/*
Copyright (c) 2008 chenm
*/

import java.io.Serializable;

/**
 * A Complex stands for a complex value.
 * @author chenm
 * @version 2008-04
 */
public final class Complex implements Serializable{

	private static final long serialVersionUID = 1L;
	private double real;  // real value
	private double imag;  // imaginary value
	
	/**
	 * Creates a new instance of a Complex with real 0 and imag 0..
	 */
	public Complex()
	{
		real = 0.0;
		imag = 0.0;
	}
	
	/**
	 * 
	 * Creates a new instance of a Complex.
	 *
	 * @param re real value
	 * @param im imaginary value
	 */
	public Complex(double re, double im)
	{
		real = re;
		imag = im;
	}
	
	/**
	 * 
	 * Creates a new instance of a Complex.
	 *
	 * @param c complex value
	 */
	public Complex(Complex c) {
		real = c.real;
		imag = c.imag;
	}

	/**
	 * to get the real value of this complex.
	 *
	 * @return real
	 */
	public double getReal() {
		return real;
	}

	/**
	 * to set the real value of this complex.
	 * @param real
	 */
	public void setReal(double real) {
		this.real = real;
	}

	/**
	 * to get the imaginary value of this complex.
	 *
	 * @return real
	 */
	public double getImag() {
		return imag;
	}

	/**
	 * to set the imaginary value of this complex.
	 * @param imag
	 */
	public void setImag(double imag) {
		this.imag = imag;
	}
	
	/**
	 * to set the real and imaginary value of this complex.
	 * @param re real value
	 * @param im imaginary value
	 */
	public void set(double re, double im) {
		real = re;
		imag = im;
	}

	/**
	 * to add a real value to the real part, and return this complex
	 * @param a a real value 
	 * @return this complex
	 */
	public Complex add(double a) {
		real += a;
		return this;
	}
	
	/**
	 * to subtract a real value to the real part, and return this complex
	 * @param a a real value 
	 * @return this complex
	 */
	public Complex subtract(double a) {
		real -= a;
		return this;
	}
	
	/**
	 * to add a complex value and return this complex
	 * @param c a complex value 
	 * @return this complex
	 */
	public Complex add(Complex c) {
		real += c.real;
		imag += c.imag;
		return this;
	}
	
	
	/**
	 * to subtract a complex value and return this complex
	 * @param c a complex value 
	 * @return this complex
	 */
	public Complex subtract(Complex c) {
		real -= c.real;
		imag -= c.real;
		return this;
	}
	
	
	/**
	 * to multiply the real and imaginary part by a real value and return this complex
	 * @param a a real value 
	 * @return this complex
	 */
	public Complex multiply(double a) {
		real *= a;
		imag *= a;
		return this;
	}
	
	/**
	 * to multiply by a complex and return this complex
	 * @param c a complex value 
	 * @return this complex
	 */
	public Complex multiply(Complex c) {
		set(real*c.real - imag*c.imag, real*c.imag + imag*c.real);
		return this;
	}
	
	/**
	 * to divide the real and imaginary part by a real value and return this complex
	 * @param a a real value 
	 * @return this complex
	 */
	public Complex divide(double a) {
		real /= a;
		imag /= a;
		return this;
	}
	
	/**
	 * to divide by a complex and return this complex
	 * @param c a complex value 
	 * @return this complex
	 */
	public Complex divide(Complex c) {
		double mod = c.real*c.real + c.imag*c.imag;
		set((real*c.real + imag*c.imag)/mod, (imag*c.real - real*c.imag)/mod);
		return this;
	}
	
	/**
	 * to get the modulus
	 * @return the modulus
	 */
	public double abs() {
		return Math.sqrt(real*real+imag*imag);
	}

	/**
	 * to get the angle
	 * @return the angle
	 */
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

	/**
	 * to add a real value to a complex
	 * @param c a complex
	 * @param a a real value
	 * @return the add result
	 */
	public static Complex add(Complex c, double a) {
		Complex out = new Complex(c);
		out.add(a);
		return out;
	}
	
	/**
	 * to add two complexes
	 * @param c1 a complex
	 * @param c2 another complex
	 * @return the add result
	 */
	public static Complex add(Complex c1, Complex c2) {
		Complex out = new Complex(c1);
		out.add(c2);
		return out;
	}
	
	/**
	 * to subtract two complexes
	 * @param c1 a complex
	 * @param c2 another complex
	 * @return the subtract result
	 */
	public static Complex subtract(Complex c1, Complex c2) {
		Complex out = new Complex(c1);
		out.subtract(c2);
		return out;
	}
	
	/**
	 * to multiply a real value to a complex
	 * @param c a complex
	 * @param a a real value
	 * @return the multiply result
	 */
	public static Complex multiply(Complex c, double a) {
		Complex out = new Complex(c);
		out.multiply(a);
		return out;
	}
	
	/**
	 * to multiply two complexes
	 * @param c1 a complex
	 * @param c2 another complex
	 * @return the multiply result c1*c2
	 */
	public static Complex multiply(Complex c1, Complex c2) {
		Complex out = new Complex(c1);
		out.multiply(c2);
		return out;
	}
	
	/**
	 * to divide a real value to a complex
	 * @param c a complex
	 * @param a a real value
	 * @return the divide result
	 */
	public static Complex divide(Complex c, double a) {
		Complex out = new Complex(c);
		out.divide(a);
		return out;
	}
	
	/**
	 * to divide two complexes
	 * @param c1 a complex
	 * @param c2 another complex
	 * @return the divide result c1/c2
	 */
	public static Complex divide(Complex c1, Complex c2) {
		Complex out = new Complex(c1);
		out.divide(c2);
		return out;
	}
	

}
