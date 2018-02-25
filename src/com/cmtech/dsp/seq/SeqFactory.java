package com.cmtech.dsp.seq;

import com.cmtech.dsp.seq.Complex;

/**
 * 
 * ClassName: SeqFactory
 * Function: 用来创建多种特殊的序列，比如零序列，正弦序列等
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月20日 上午7:17:46 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class SeqFactory {
	/**
	 * 
	 * 防止类实例化
	 *
	 */
	private SeqFactory() {
	}
	
	/**
	 * 
	 * createZeroSeq:创建零序列
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param N 序列长度
	 * @param cl 序列的类型，可以实序列或复序列
	 * @return 序列
	 * @since JDK 1.6
	 */
	public static <T extends ISeq<T>> T createZeroSeq(int N, Class<T> cl) {
		try {
			return cl.getConstructor(int.class).newInstance(N);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	/**
	 * 
	 * createSinSeq:创建正弦序列（指定数字角频率）
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param A 幅度
	 * @param w 数字角频率
	 * @param initphi 初始相位 
	 * @param N 序列长度
	 * @return 正弦序列
	 * @since JDK 1.6
	 */
	public static RealSeq createSinSeq(double A, double w, double initphi, int N) {
		RealSeq out = new RealSeq(N);
		for(int i = 0; i < N; i++) {
			out.set(i, A*Math.sin(w*i+initphi));
		}
		return out;
	}
	
	/**
	 * 
	 * createSinSeq:创建正弦序列（指定模拟线频率和采样频率）
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param A 幅度
	 * @param f 模拟线频率
	 * @param initphi 初始相位
	 * @param fs 采样频率
	 * @param N 序列长度
	 * @return
	 * @since JDK 1.6
	 */
	public static RealSeq createSinSeq(double A, double f, double initphi, double fs, int N)	{
		return createSinSeq(A, 2*Math.PI*f/fs, initphi, N);
	}
	
	/**
	 * 
	 * createEJWSeq:创建复指数序列e^(j*(w*n+initphi))
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param w 数字角频率
	 * @param initphi 初始相位
	 * @param N 序列长度
	 * @return 复指数序列
	 * @since JDK 1.6
	 */
	public static ComplexSeq createEJWSeq(double w, double initphi, int N) {
		ComplexSeq out = new ComplexSeq(N);
		for(int i = 0; i < N; i++) {
			out.set(i, new Complex(Math.cos(w*i+initphi), Math.sin(w*i+initphi)));
		}
		return out;
	}
	
	/**
	 * 
	 * linSpace:创建等间隔采样点构成的序列（包含指定的起点和终点）
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param begin 范围的起点
	 * @param end 范围的终点
	 * @param N 采样点数（包括起点和终点）
	 * @return 序列
	 * @since JDK 1.6
	 */
	public static RealSeq linSpace(double begin, double end, int N)
	{
	    RealSeq out = new RealSeq(N);
	    
	    double delta = (end - begin)/(N-1);

	    for(int i = 0; i < N-1; i++)
	    {
	        out.set(i, begin + delta*i);
	    }    
	    out.set(N-1, end);

	    return out;
	}
	
	/**
	 * 
	 * createRandomSeq:创建随机序列
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param N 序列长度
	 * @return 随机序列
	 * @since JDK 1.6
	 */
	public static RealSeq createRandomSeq(int N)
	{
	    RealSeq out = new RealSeq(N);

	    for(int i = 0; i < N; i++)
	    {
	        out.set(i, Math.random());
	    }    

	    return out;
	}
}
