package com.cmtech.dsp.util;

import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.ISeq;
import com.cmtech.dsp.seq.ISeqBaseOperator;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.Seq;

/**
 * 
 * ClassName: SeqUtil
 * Function: 搴忓垪宸ュ叿绫伙紝涓昏鐢ㄤ簬瀹炵幇涓や釜搴忓垪涔嬮棿鐨勭畻鏈繍绠楋紝姣斿鍔犲噺涔橀櫎锛屼互鍙婂嵎绉拰 
 * Reason: TODO ADD REASON(鍙��). 
 * date: 2018骞�2鏈�20鏃� 涓婂崍11:31:37 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class SeqUtil {
	private SeqUtil() {
	}
	
	private interface IBiOperator<T> {
		T operator(T op1, T op2);
	}
	
	/**
	 * 
	 * add: 涓や釜瀹炲簭鍒楃浉鍔�
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 鍜�
	 * @since JDK 1.6
	 */
	public static <T> ISeq<T> add(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getSeqBaseOperator().add(op1, op2);
			}
		});
	}
	
	/**
	 * 
	 * subtract: 涓や釜瀹炲簭鍒楃浉鍑�
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 宸�
	 * @since JDK 1.6
	 */
	public static <T> ISeq<T> subtract(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getSeqBaseOperator().subtract(op1, op2);
			}
		});
	}
	
	/**
	 * 
	 * multiple: 涓や釜瀹炲簭鍒楃浉涔�
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 绉�
	 * @since JDK 1.6
	 */
	public static <T> ISeq<T> multiple(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getSeqBaseOperator().multiple(op1, op2);
			}
		});
	}
	
	/**
	 * 
	 * divide: 涓や釜瀹炲簭鍒楃浉闄�
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 鍟�
	 * @since JDK 1.6
	 */
	public static <T> ISeq<T> divide(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getSeqBaseOperator().divide(op1, op2);
			}
		});
	}
	
	private static <T> ISeq<T> process(ISeq<T> seq1, ISeq<T> seq2, IBiOperator<T> op) {
		int N = Math.max(seq1.size(), seq2.size());
		seq1.changeSize(N);
		seq2.changeSize(N);
		Seq<T> out = seq1.getSeqBaseOperator().newInstance();
		
		for(int i = 0; i < N; i++) {
			out.append(op.operator(seq1.get(i), seq2.get(i)));
		}
		return out;	
	}
	
	/**
	 * 
	 * conv:姹備袱涓搴忓垪鐨勭嚎鎬у嵎绉拰
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 绾挎�у嵎绉拰
	 * @since JDK 1.6
	 */
	public static <T> ISeq<T> conv(ISeq<T> seq1, ISeq<T> seq2) {
	    int N1 = seq1.size();
	    int N2 = seq2.size();
	    int N = N1+N2-1;
	    
	    ISeqBaseOperator<T> op = seq1.getSeqBaseOperator();
	    ISeq<T> out = op.newInstance();
	    
	    int n = 0;
	    int m = 0;
	    int n_m = 0;  
	    T tmp = op.zeroElement();
	    for(n = 0; n < N; n++)
	    {
	    		tmp = op.zeroElement();
	        for(m = 0; m < N1; m++)
	        {
	            n_m = n - m;
	            if( (n_m >= 0) && (n_m < N2) )
	            		tmp = op.add(tmp, op.multiple(seq1.get(m), seq2.get(n_m)));
	        }
	        out.append(tmp);
	    }
	    return out;
	}
	
	/**
	 * 
	 * cirConvUsingDFT: 鐢‵FT姹備袱涓簭鍒楃殑N鐐瑰渾鍛ㄥ嵎绉�
	 * 浣嗘槸鐢变簬FFT鍙兘姹�2鐨勬暣鏁板箓闀垮害锛屾墍浠ュ疄闄呰繑鍥炵殑搴忓垪闀垮害鍙兘姣擭瑕侀暱
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @param N 鍦嗗懆鍗风Н鐐规暟
	 * @return 鍦嗗懆鍗风Н
	 * @since JDK 1.6
	 */
	public static <T> ComplexSeq cirConvUsingDFT(ISeq<T> seq1, ISeq<T> seq2, int N)
	{
	    ComplexSeq seq1DFT = seq1.fft(N);
	    ComplexSeq seq2DFT = seq2.fft(N);
	    ComplexSeq dft = (ComplexSeq) SeqUtil.multiple(seq1DFT, seq2DFT);
	    return dft.ifft();
	}

	/**
	 * 
	 * convUsingDFT: 鐢‵FT姹備袱涓搴忓垪鐨勭嚎鎬у嵎绉拰锛堝疄闄呬笂姹傚緱鏄渾鍛ㄥ嵎绉級
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 绾挎�у嵎绉拰
	 * @since JDK 1.6
	 */
	public static <T> ComplexSeq convUsingDFT(ISeq<T> seq1, ISeq<T> seq2)
	{
		int N = seq1.size()+seq2.size()-1;
	    return (ComplexSeq) cirConvUsingDFT(seq1, seq2, N).changeSize(N);
	}
	
	/**
	 * 
	 * createZeroSeq:鍒涘缓闆跺簭鍒�
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param N 搴忓垪闀垮害
	 * @param cl 搴忓垪鐨勭被鍨嬶紝鍙互瀹炲簭鍒楁垨澶嶅簭鍒�
	 * @return 搴忓垪
	 * @since JDK 1.6
	 */
	public static RealSeq createZeroRealSeq(int N) {
		return new RealSeq(N);		
	}
	
	public static ComplexSeq createZeroComplexSeq(int N) {
		return new ComplexSeq(N);		
	}
	
	/**
	 * 
	 * createSinSeq:鍒涘缓姝ｅ鸡搴忓垪锛堟寚瀹氭暟瀛楄棰戠巼锛�
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param A 骞呭害
	 * @param w 鏁板瓧瑙掗鐜�
	 * @param initphi 鍒濆鐩镐綅 
	 * @param N 搴忓垪闀垮害
	 * @return 姝ｅ鸡搴忓垪
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
	 * createSinSeq:鍒涘缓姝ｅ鸡搴忓垪锛堟寚瀹氭ā鎷熺嚎棰戠巼鍜岄噰鏍烽鐜囷級
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param A 骞呭害
	 * @param f 妯℃嫙绾块鐜�
	 * @param initphi 鍒濆鐩镐綅
	 * @param fs 閲囨牱棰戠巼
	 * @param N 搴忓垪闀垮害
	 * @return
	 * @since JDK 1.6
	 */
	public static RealSeq createSinSeq(double A, double f, double initphi, double fs, int N)	{
		return createSinSeq(A, 2*Math.PI*f/fs, initphi, N);
	}
	
	/**
	 * 
	 * createEJWSeq:鍒涘缓澶嶆寚鏁板簭鍒梕^(j*(w*n+initphi))
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param w 鏁板瓧瑙掗鐜�
	 * @param initphi 鍒濆鐩镐綅
	 * @param N 搴忓垪闀垮害
	 * @return 澶嶆寚鏁板簭鍒�
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
	 * linSpace:鍒涘缓绛夐棿闅旈噰鏍风偣鏋勬垚鐨勫簭鍒楋紙鍖呭惈鎸囧畾鐨勮捣鐐瑰拰缁堢偣锛�
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param begin 鑼冨洿鐨勮捣鐐�
	 * @param end 鑼冨洿鐨勭粓鐐�
	 * @param N 閲囨牱鐐规暟锛堝寘鎷捣鐐瑰拰缁堢偣锛�
	 * @return 搴忓垪
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
	 * createRandomSeq:鍒涘缓闅忔満搴忓垪
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉� 鈥� 鍙��)
	 * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤� 鈥� 鍙��)
	 *
	 * @author bme
	 * @param N 搴忓垪闀垮害
	 * @return 闅忔満搴忓垪
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
