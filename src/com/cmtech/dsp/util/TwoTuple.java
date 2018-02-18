/**
 * Project Name:DSP_JAVA
 * File Name:TwoTuple.java
 * Package Name:com.cmtech.dsp.util
 * Date:2018年2月18日上午7:26:40
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.util;

/**
 * ClassName: TwoTuple
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月18日 上午7:26:40 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class TwoTuple<A,B> {
    /* 定义成final 是为了保证其安全性，不能进行修改 
     * public final 比private+getter/setter更符合当前的需求 
     */  
    public final A first;   
    public final B second;  
      
    public TwoTuple(A a,B b)  
    {  
        first=a;  
        second=b;  
    }
}
