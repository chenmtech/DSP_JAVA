package com.cmtech.dsp;

import java.util.Arrays;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.bmefile.BmeFileHead10;
import com.cmtech.dsp.exception.DspException;
import com.cmtech.dsp.filter.AnalogFilter;
import com.cmtech.dsp.filter.FIRFilter;
import com.cmtech.dsp.filter.IIRFilter;
import com.cmtech.dsp.filter.design.AFType;
import com.cmtech.dsp.filter.design.ALPDesigner;
import com.cmtech.dsp.filter.design.FIRDesigner;
import com.cmtech.dsp.filter.design.FilterType;
import com.cmtech.dsp.filter.design.IIRDesigner;
import com.cmtech.dsp.filter.design.WinType;
import com.cmtech.dsp.filter.para.AFPara;
import com.cmtech.dsp.filter.para.IIRPara;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.SeqFactory;

public class DspApp {

	public static void main(String[] args) throws DspException{
		BmeFile.setFileDirectory("/Users/bme/Documents/matlab");
		
		double[] wp = {0.4*Math.PI, 0.5*Math.PI};
		double[] ws = {0.3*Math.PI, 0.6*Math.PI};
		double Rp = 1;
		double As = 20;
		FilterType fType = FilterType.BANDPASS;
		WinType wType = WinType.KAISER;
		FIRFilter fir = FIRDesigner.design(wp,ws,Rp,As,fType,wType);
		System.out.println(fir);
		System.out.println(fir.freq(101).abs());

		AnalogFilter alp = ALPDesigner.design(10.0,20.0,1,30,AFType.CHEB2);
		System.out.println(alp);
		RealSeq freq = alp.freq(30.0, 31).abs();
		System.out.println(freq);		
		BmeFile.createBmeFile("alp.bme").writeData(freq.toArray()).close();
		
		AFType afType = AFType.ELLIP;
		IIRFilter iir = IIRDesigner.design(wp,ws,Rp,As,afType,fType);
		System.out.println(iir);
		freq = iir.freq(101).abs();
		System.out.println(freq);
		BmeFile.createBmeFile("iir.bme").writeData(freq.toArray()).close();
		BmeFile.createBmeFile("omega.bme").writeData(SeqFactory.linSpace(0, 1, 101).toArray()).close();
		
		
		
		RealSeq seq1 = SeqFactory.createRndSeq(10);
		RealSeq seq2 = SeqFactory.createRndSeq(10);
		ComplexSeq seq3 = new ComplexSeq(seq1, seq2);
		System.out.println(seq3);
		//seq1.changeSize(8);
		//ComplexSeq fft = seq3.fft();
		ComplexSeq fft = seq3.fft();
		seq3 = fft.ifft();
		System.out.println(seq3);

		ComplexSeq seq4 = new ComplexSeq(seq3);
		System.out.println(seq3.hashCode());
		System.out.println(seq4.hashCode());
		System.out.println(seq3.equals(seq1));

		
		BmeFile file1 = BmeFile.openBmeFile("dfs1.bme");
		int[] buf = file1.readData(new int[0]);
		file1.close();
		System.out.println(file1);
		System.out.println(Arrays.toString(buf));
		
		BmeFile file2 = BmeFile.createBmeFile("dfs.bme", 
				new BmeFileHead10(file1.getBmeFileHead()));
		file2.writeData(buf).writeData(10);
		file2.close();
		System.out.println(file2.getDataType());
		
		BmeFile file3 = BmeFile.openBmeFile("dfs.bme");
		System.out.println(file3);
		int[] buf2 = file3.readData(new int[0]);
		file3.close();
		System.out.println(Arrays.toString(buf2));
		
		/*Field f = head.getClass().getDeclaredField("VER");
		f.setAccessible(true);
		System.out.println(Arrays.toString((byte[])f.get(head)));
		
		BmeFileHead head1 = new BmeFileHead10();
		head1.setInfo("hi,all").setFs(111);
		BmeFile file1 = new BmeFile("dfs.bme", head1);
		double[] data = new double[] {1.11,2.22,3.33};
		file1.writeData(data).writeData(data);
		file1.close();
		
		BmeFile file2 = new BmeFile("dfs.bme");
		
		if(file2.getDataType() == BmeFileDataType.DOUBLE) {
			double[] buf = new double[0];
			buf = file2.readData(new double[0]);
			System.out.println(Arrays.toString(buf));
		}
		else if(file2.getDataType() == BmeFileDataType.INT32) {
			int[] buf = new int[0];
			buf = file2.readData(new int[0]);
			System.out.println(Arrays.toString(buf));
		}
			
		file2.close();		
		System.out.println(file2);*/
		
	}
	
	private static double[] changeSize(double[] a) {
		return a;
	}

}
