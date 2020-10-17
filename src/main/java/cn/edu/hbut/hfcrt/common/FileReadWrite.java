/**  

* <p>Title: FileReadWrite.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年4月14日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.common;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author sha
 */

public class FileReadWrite {
	public void byteOutStream(String path,String outString) throws Exception{
	//1.使用File类创建一个需要操作的文件类型
		File file = new File(path);
		if (!file.getParentFile().exists()) {//如果文件目录不存在
			file.getParentFile().mkdir();//创建文件目录
		}
		
	//2 实例化outputString对象
		OutputStream output = new FileOutputStream(file);
		//3:准备好实现内容输出
		//将字符串变为字符数组
		byte data[] = outString.getBytes();
		try {
			output.write(data);//这里向文件中输入结果123
			output.flush();
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				output.close();
				System.out.println("文件已生成。。。");
			}
		}
		
		
	}
}
