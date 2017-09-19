package com.jiudianlianxian.socket01;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @Title: KeepAlive
 * @Description: ά�����ӵ���Ϣ���� 
 * @Company: �����ŵ�������Ϣ�������޹�˾
 * @ProjectName: socket
 * @author fupengpeng
 * @date 2017��9��18�� ����2:31:10
 */
public class KeepAlive implements Serializable{  
   
    private static final long serialVersionUID = -2813120366138988480L;  
   
    /* ���Ǹ÷����������ڲ���ʹ�á� 
     * @see java.lang.Object#toString() 
     */ 
    @Override 
    public String toString() {  
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\tά�����Ӱ�";  
    }  
   
} 
