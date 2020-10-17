/**  

* <p>Title: IDataService.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年5月13日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.service.dataservice;

import java.util.List;

import cn.edu.hbut.hfcrt.service.message.Datasource;
import cn.edu.hbut.hfcrt.service.message.IMessage;

/**
*/

/**
 * @author sha
 *
 */
public interface IDataService {
	
	public Datasource getDatasource();
	
	public List<IMessage> get();
	
	public IMessage get(String id);
	
	public boolean set(IMessage message);

}
