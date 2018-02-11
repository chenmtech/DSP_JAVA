/**
 * Project Name:DSP_JAVA
 * File Name:BmeFile.java
 * Package Name:com.cmtech.dsp.bmefile
 * Date:2018年2月11日上午6:23:50
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.cmtech.dsp.exception.DspException;
import com.cmtech.dsp.exception.DspExceptionCode;
import static com.cmtech.dsp.exception.DspExceptionCode.*;

/**
 * ClassName: BmeFile
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月11日 上午6:23:50 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class BmeFile {
	private static Path dir;
	
	private static final byte[] bme = {'B', 'M', 'E'};
	private byte[] ver = new byte[2];
	private String fileName;
	
	public BmeFile(String fileName) {
		
	}
	
	public static DspException configFileDirectory(String pathName) {
		if(pathName == null || "".equals(pathName)) 
			return new DspException(BMEFILE_OPERATION_ERR, "路径为空");
		
		Path p = Paths.get(pathName);
		if(p.getRoot() == null) 
			return new DspException(BMEFILE_OPERATION_ERR, "路径必须包含根目录");
		
		if(!Files.exists(p)) 
			return new DspException(BMEFILE_OPERATION_ERR, "路径不存在");
		
		if(!Files.isDirectory(p))
			return new DspException(BMEFILE_OPERATION_ERR, "不是路径名");
		
		dir = p;
		
		return null;
	}

}
