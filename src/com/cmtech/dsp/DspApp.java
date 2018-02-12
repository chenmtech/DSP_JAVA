package com.cmtech.dsp;

import java.util.Arrays;

import com.cmtech.dsp.exception.FileException;
import com.cmtech.dsp.file.BmeFile;
import com.cmtech.dsp.file.BmeFileDataType;
import com.cmtech.dsp.file.BmeFileHead;
import com.cmtech.dsp.file.BmeFileHead10;
import com.cmtech.dsp.filter.FIRFilter;
import com.cmtech.dsp.filter.IIRFilter;
import com.cmtech.dsp.filter.design.AFType;
import com.cmtech.dsp.filter.design.FIRDesigner;
import com.cmtech.dsp.filter.design.FIRSpec;
import com.cmtech.dsp.filter.design.FilterType;
import com.cmtech.dsp.filter.design.IIRDesigner;
import com.cmtech.dsp.filter.design.IIRSpec;
import com.cmtech.dsp.filter.design.WinType;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.SeqFactory;

public class DspApp {

	public static void main(String[] args) throws FileException{

		double[] ws = {0.4*Math.PI, 0.5*Math.PI};
		double[] wp = {0.3*Math.PI, 0.6*Math.PI};
		double Rp = 1;
		double As = 20;
		FilterType fType = FilterType.BANDSTOP;
		
		FIRSpec firSpec = new FIRSpec(wp,ws,Rp,As,fType);
		firSpec.setWinType(WinType.KAISER);
		FIRFilter fir = FIRDesigner.design(firSpec);
		System.out.println(fir.getHn());
		
		System.out.println(fir.freq(101).abs());
		
		AFType afType = AFType.BUTT;
		IIRSpec iirSpec = new IIRSpec(wp,ws,Rp,As,fType,afType);
		IIRFilter iir = IIRDesigner.design(iirSpec);
		System.out.println(iir.getB());
		System.out.println(iir.getA());
		System.out.println(iir.freq(101).abs());
		System.out.println(iir.freq(101).dB());
		
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

		
		BmeFile.setFileDirectory("/Users/bme/Documents/matlab");
		
		BmeFile file1 = new BmeFile("dfs.bme");
		double[] buf = file1.readData(new double[0]);
		file1.close();
		System.out.println(file1);
		
		byte[] buf1 = new byte[buf.length];
		for(int i = 0; i < buf.length; i++) {
			buf1[i] = (byte)buf[i];
		}
		
		BmeFileHead head = new BmeFileHead10();
		head.setDataType(BmeFileDataType.UINT8);
		BmeFile file2 = new BmeFile("dfs1.bme", head);
		file2.writeData(buf1);
		file2.close();
		System.out.println(file2);
		
		BmeFile file3 = new BmeFile("dfs1.bme");
		byte[] buf2 = file3.readData(new byte[0]);
		file3.close();
		System.out.println(Arrays.toString(buf2));
		
		/*BmeFileHead head1 = new BmeFileHead10();
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
