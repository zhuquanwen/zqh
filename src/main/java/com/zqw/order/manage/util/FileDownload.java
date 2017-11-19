package com.zqw.order.manage.util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
*@author:quanwen
*@date:2016年9月26日
*@description:
*/
public class FileDownload {
	public static byte[] getBytesFromFile(File file) throws IOException {

	    InputStream is = new FileInputStream(file);
	    // 获取文件大小
	    long length = file.length();
	    if (length > Integer.MAX_VALUE) {
	    // 文件太大，无法读取
	    throw new IOException("File is to large "+file.getName());

	    }
	    // 创建一个数据来保存文件数据
	    byte[] bytes = new byte[(int)length];
	    // 读取数据到byte数组中
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }
	    // 确保所有数据均被读取
	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }
	    // Close the input stream and return bytes
	    is.close();
	    return bytes;
	}
	
	@SuppressWarnings("serial")
	public static void download(HttpServletResponse response,File file) throws Exception {
		boolean flag=true;
		OutputStream outputStream=null;
		try{
			if(!file.exists()){
				flag=false;
				throw new Exception("您所下载的文件已经不存在!") {};
				
			}else{
				byte[] data = getBytesFromFile(file);  
				String fileName = URLEncoder.encode(file.getName(), "UTF-8"); 
				
				response.reset();  
				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
				response.addHeader("Content-Length", "" + data.length);  
				response.setContentType("application/x-msdownload;charset=UTF-8");  
				outputStream = new BufferedOutputStream(response.getOutputStream());  
				outputStream.write(data);  
				outputStream.flush();  
				outputStream.close();  
			}
			
		}catch(IOException e){
			e.printStackTrace();
			flag=false;
		}finally{
			if(outputStream!=null){
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(!flag){
			throw new Exception("文件传输出错!") {};
			 /*HttpServletResponse response=Struts2Utils.getResponse();
		    response.setCharacterEncoding("UTF-8");  
		    response.setContentType("application/json; charset=utf-8");  
		    PrintWriter out = null;  
		   try {  
		        out = response.getWriter();  
		        out.print("文件下载出错!");  
		      
		    } catch (IOException e) {  
		        e.printStackTrace();  
		    } finally {  
		        if (out != null) {  
		            out.close();  
		        }  
		    }  */
		
		}
	}
}
