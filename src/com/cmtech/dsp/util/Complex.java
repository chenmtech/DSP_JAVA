																					package com.cmtech.dsp.util;

import java.io.Serializable;

/**
 * ClassName: Complex
 * Function: 复数类. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月2日 上午6:00:36 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public final class Complex implements Serializable{

	private static final long serialVersionUID = 1L;
	private double real;
	private double imag;
	
	/**
	 * 
	 * Creates a new instance of Complex.
	 * 实部和虚部都为0.0
	 */
	public Complex()
	{
		real = 0.0;
		imag = 0.0;
	}
	
	/**
	 * 
	 * Creates a new instance of Complex.
	 *
	 * @param re 实部
	 * @param im 虚部
	 */
	public Complex(double re, double im)
	{
		real = re;
		imag = im;
	}
	
	/**
	 * 
	 * Creates a new instance of Complex.
	 *
	 * @param c 复数
	 */
	public Complex(Complex c) {
		real = c.real;
		imag = c.imag;
	}

	/**
	 * getReal:获取实部
	 *
	 * @author bme
	 * @return 实部
	 * @since JDK 1.6
	 */
	public double getReal() {
		return real;
	}

	/**
	 * 
	 * setReal: 设置实部
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param real 实部
	 * @since JDK 1.6
	 */
	public void setReal(double real) {
		this.real = real;
	}

	/**
	 * 
	 * getImag: 获取虚部
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @return 虚部
	 * @since JDK 1.6
	 */
	public double getImag() {
		return imag;
	}

	/**
	 * 
	 * setImag: 设置虚部
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param imag 虚部
	 * @since JDK 1.6
	 */
	public void setImag(double imag) {
		this.imag = imag;
	}
	
	/**
	 * 
	 * set: 设置实部和虚部
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param re 实部
	 * @param im 虚部
	 * @since JDK 1.6
	 */
	public void set(double re, double im) {
		real = re;
		imag = im;
	}
	
	/**
	 * 
	 * add: 实部加上一个实数
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param a 实数
	 * @return this
	 * @since JDK 1.6
	 */
	public Complex add(double a) {
		real += a;
		return this;
	}
	
	/**
	 * subtract: 实部减去一个实数
	 *
	 * @author bme
	 * @param a 实数
	 * @return this
	 * @since JDK 1.6
	 */
	public Complex subtract(double a) {
		real -= a;
		return this;
	}
	
	/**
	 * 
	 * add: 加上一个复数
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param c 复数
	 * @return this
	 * @since JDK 1.6
	 */
	public Complex add(Complex c) {
		real += c.real;
		imag += c.imag;
		return this;
	}
	
	/**
	 * 
	 * subtract: 减去一个复数
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param c 复数
	 * @return this
	 * @since JDK 1.6
	 */
	public Complex subtract(Complex c) {
		real -= c.real;
		imag -= c.real;
		return this;
	}
	
	/**
	 * 
	 * multiple: 实部和虚部都乘以一个实数
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param a 实数
	 * @return this
	 * @since JDK 1.6
	 */
	public Complex multiple(double a) {
		real *= a;
		imag *= a;
		return this;
	}
	
	/**
	 * 
	 * multiple: 乘以一个复数
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param c 复数
	 * @return this
	 * @since JDK 1.6
	 */
	public Complex multiple(Complex c) {
		set(real*c.real - imag*c.imag, real*c.imag + imag*c.real);
		return this;
	}
	
	/**
	 * 
	 * divide: 实部和虚部都除以一个实数
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param a 实数
	 * @return this
	 * @since JDK 1.6
	 */
	public Complex divide(double a) {
		real /= a;
		imag /= a;
		return this;
	}
	
	/**
	 * 
	 * divide: 除以一个复数
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param c 复数
	 * @return this
	 * @since JDK 1.6
	 */
	public Complex divide(Complex c) {
		double mod = c.real*c.real + c.imag*c.imag;
		set((real*c.real + imag*c.imag)/mod, (imag*c.real - real*c.imag)/mod);
		return this;
	}
	
	/**
	 * 
	 * abs: 求幅度
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @return 幅度
	 * @since JDK 1.6
	 */
	public double abs() {
		return Math.sqrt(real*real+imag*imag);
	}
	
	/**
	 * 
	 * angle: 求相位角弧度，范围[-PI,PI]
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @return 相位角弧度
	 * @since JDK 1.6
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
	
	/**
	 * subtract:(这里用一句话描述这个方法的作用)
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param op1
	 * @param op2
	 * @return
	 * @since JDK 1.6
	 */
	public static Complex subtract(Complex c1, Complex c2) {
		Complex out = new Complex(c1);
		out.subtract(c2);
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
