/**  

* <p>Title: Utility.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年4月14日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.common;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Utility {
	
	public static String readTxtFile(String filePath) {
        File txtFile = new File(filePath);
        FileInputStream is = null;
        StringBuilder stringBuilder = null;
        try {
            if (txtFile.length() != 0) {
                is = new FileInputStream(txtFile);
                InputStreamReader streamReader = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(streamReader);
                String line;
                stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
                is.close();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(stringBuilder);

    }
	
	/**
	 * decode [ String1, String2,...] to List<String>
	 * @param filePath
	 * @return strList
	 */
	public static List<String> decodeTxtToArray(String filePath){
		return new ArrayList<String>();
	}
	
	public static void byteOutStream(String path ,String outString) throws Exception {
      	//1:使用File类创建一个要操作的文件路径
        File file = new File(path);
        if (!file.getParentFile().exists()) { //如果文件的目录不存在
            file.getParentFile().mkdirs(); //创建目录
        }
        //2: 实例化OutputString 对象
        OutputStream output = new FileOutputStream(file);
        //3: 准备好实现内容的输出
        //将字符串变为字节数组
        byte data[] = outString.getBytes();
        try {
            output.write(data);//这里向文件中输入结果123
            output.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //4: 资源操作的最后必须关闭
                output.close();
                System.out.println("文件已生成。");
            }
        }
    }
}

