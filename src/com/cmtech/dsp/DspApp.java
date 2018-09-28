package com.cmtech.dsp;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.bmefile.BmeFileDataType;
import com.cmtech.dsp.bmefile.BmeFileHead;
import com.cmtech.dsp.bmefile.BmeFileHeadFactory;
import com.cmtech.dsp.exception.DspException;
import com.cmtech.msp.qrsdetbyhamilton.Derivative;
import com.cmtech.msp.qrsdetbyhamilton.HighpassFilter;
import com.cmtech.msp.qrsdetbyhamilton.LowpassFilter;
import com.cmtech.msp.qrsdetbyhamilton.MAverageFilter;
import com.cmtech.msp.qrsdetbyhamilton.QrsFilter;

public class DspApp {

	public static void main(String[] args) throws DspException{
		BmeFile.setFileDirectory("/Users/bme/360企业云盘同步版/matlabcode/QRSDetectorbyHamilton");
		//BmeFile.setFileDirectory("F:\\360云盘\\matlabcode\\QRSDetectorbyHamilton");
		
		BmeFile ecgFile = BmeFile.openBmeFile("chenm.bme");
		//System.out.println(ecgFile);
		int sampleRate = ecgFile.getFs();
		int[] ecgData = ecgFile.readData(new int[0]);
		
		/*
		LowpassFilter lpFilter = new LowpassFilter(sampleRate);
		HighpassFilter hpFilter = new HighpassFilter(sampleRate);
		Derivative derivative = new Derivative(sampleRate);
		MAverageFilter maFilter = new MAverageFilter(sampleRate);
		
		System.out.println(lpFilter.getLength() + " " + hpFilter.getLength() + " " + derivative.getLength() + " " + maFilter.getLength());
		
		int[] dataProcessed = new int[ecgData.length];
		
		for(int i = 0; i < ecgData.length; i++) {
			dataProcessed[i] = lpFilter.filter(ecgData[i]); 
			dataProcessed[i] = hpFilter.filter(dataProcessed[i]);
			dataProcessed[i] = derivative.filter(dataProcessed[i]);
			dataProcessed[i] = Math.abs(dataProcessed[i]);
			dataProcessed[i] = maFilter.filter(dataProcessed[i]);
		}
		*/
		
		QrsFilter filter = new QrsFilter(sampleRate);
		System.out.println(filter.getLengthInfo());
		//MAverageFilter filter = new MAverageFilter(sampleRate);
		
		int[] dataProcessed = new int[ecgData.length];
		
		for(int i = 0; i < ecgData.length; i++) {
			dataProcessed[i] = filter.filter(ecgData[i]);
		}
		
		BmeFileHead fileHead = BmeFileHeadFactory.createDefault().setDataType(BmeFileDataType.INT32);
		BmeFile processFile = BmeFile.createBmeFile("dataProcessed.bme", fileHead).writeData(dataProcessed);
		
		ecgFile.close();
		processFile.close();
		
		
	}
	
	
/*
		RealSeq seq1 = SeqUtil.createSinSeq(1.0, 0.2*PI, 0, 101);
		RealSeq seq2 = SeqUtil.createSinSeq(1.0, 0.7*PI, 0, 101);
		RealSeq before = (RealSeq) SeqUtil.add(seq1, seq2);
		double[] ws = {0.4*Math.PI};
		double[] wp = {0.5*Math.PI};
		double Rp = 0.3;
		double As = 40;
		AFType afType = AFType.BUTT;
		FilterType fType = FilterType.HIGHPASS;
		IDigitalFilter filter = IIRDesigner.design(wp, ws, Rp, As, afType, fType);
		RealSeq after = filter.filter(before);
		before.saveAsBmeFile("before1.bme");
		after.saveAsBmeFile("after1.bme");
		
		ComplexSeq fft = seq1.fft();
		System.out.println(fft.abs());
		ComplexSeq fft1 = new ComplexSeq(seq1,seq2).fft();
		System.out.println(fft1);
		//BmeFile.createBmeFile("fft.bme").writeData(fft.abs().toArray()).close();
		
		RealSeq sinSeq1 = SeqUtil.createSinSeq(1, 0.2*PI, 0, 500);
		RealSeq sinSeq2 = SeqUtil.createSinSeq(1, 0.3*PI, 0, 500);
		RealSeq sinSeq3 = SeqUtil.createSinSeq(1, 0.4*PI, 0, 500);
		before = (RealSeq) SeqUtil.add(sinSeq1, sinSeq2);
		before = (RealSeq) SeqUtil.add(before, sinSeq3);
		System.out.println(before);
		
		IIRFilter notch = NotchDesigner.design(0.3*PI, 0.01*PI);
		RealSeq after1 = notch.filter(before);
		
		notch.createStructure(StructType.IIR_NOTCH);
		
		after = notch.filter(before);
		System.out.println(after1);
		System.out.println(after);
		
		//before.saveAsBmeFile("before.bme");
		//after.saveAsBmeFile("after.bme");	
 */

}
