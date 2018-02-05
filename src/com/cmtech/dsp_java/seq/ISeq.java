/**
 * 
 */
package com.cmtech.dsp_java.seq;


/**
 * ClassName: ISeq
 * Function: 序列接口. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月2日 上午6:06:20 
 *
 * @author bme
 * @version @param <E> ：序列元素的类型参数，E可设置Double或Complex
 * @since JDK 1.6
 */
public interface ISeq<E> {
	/**
	 * setToZeroSequence:将序列设置为指定长度的全零序列
	 * @author bme
	 * @param N 长度
	 * @since JDK 1.6
	 */
	void setToAllZeroSequence(int N);
	/**
	 * get:获取序列中指定序号的元素
	 * 如果序号超出范围，则返回null
	 * @author bme
	 * @param i 序号
	 * @return 序列元素
	 * @since JDK 1.6
	 */
	E get(int i);
	/**
	 * set:设置指定序号的元素值
	 * @author bme
	 * @param i 序号
	 * @param element  指定的元素值
	 * @return  设置有效则返回true，无效返回false
	 * @since JDK 1.6
	 */
	boolean set(int i, E element);
	/**
	 * toArray:将序列转换为数组
	 * @author bme
	 * @return 序列包含的元素构成的数组
	 * @since JDK 1.6
	 */
	E[] toArray();
	/**
	 * clear:将序列清空，长度为0
	 * @author bme
	 * @since JDK 1.6
	 */
	void clear();
	/**
	 * size:获取序列的元素个数
	 * @author bme
	 * @return 序列元素个数
	 * @since JDK 1.6
	 */
	int size();
	/**
	 * changeSize:改变序列长度。如果原长度大于N，则多余的元素删除；
	 * 如果原长度小于N，则多余的元素缺省构造。
	 *
	 * @author bme
	 * @param N 改变的序列长度
	 * @since JDK 1.6
	 */
	void changeSize(int N);	
	/**
	 * reverse:序列翻转。注意本序列保持不变，返回翻转后序列。
	 *
	 * @author bme
	 * @return 翻转后序列
	 * @since JDK 1.6
	 */
	ISeq<E> reverse();	
	/**
	 * plus:加一个常数。注意本序列不变，返回加法后序列。
	 *
	 * @author bme
	 * @param a 被加常数
	 * @return 加法后序列
	 * @since JDK 1.6
	 */
	ISeq<E> plus(E a);
	/**
	 * minus:减一个常数。注意本序列不变，返回减法后序列。
	 *
	 * @author bme
	 * @param a 被减常数
	 * @return 减法后序列
	 * @since JDK 1.6
	 */
	ISeq<E> minus(E a);
	/**
	 * multiple:乘以一个常数。注意本序列不变，返回乘法后序列。
	 *
	 * @author bme
	 * @param a 乘以常数
	 * @return 乘法后序列
	 * @since JDK 1.6
	 */
	ISeq<E> multiple(E a);
	/**
	 * multiple:除以一个常数。注意本序列不变，返回除法后序列。
	 *
	 * @author bme
	 * @param a 除以常数
	 * @return 除法后序列
	 * @since JDK 1.6
	 */
	ISeq<E> divide(E a);
	
	/**
	 * abs:求序列每个元素的模。
	 *
	 * @author bme
	 * @return 模序列
	 * @since JDK 1.6
	 */
	RealSeq abs();
	RealSeq angle();
	E sum();
	ComplexSeq dtft(RealSeq omega);
	ComplexSeq dtft(int N);
}
