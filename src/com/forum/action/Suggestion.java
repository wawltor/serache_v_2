package com.forum.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import net.sf.json.JSONArray;
import org.apache.struts2.ServletActionContext;
import redis.clients.jedis.Jedis;
import com.opensymphony.xwork2.ActionSupport;
public class Suggestion extends ActionSupport{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String query;
	private Set<String> result;
	private String contentType = "text/html;charset=utf-8"; 
	

	public void  getSuggestion() throws IOException{
		ServletActionContext.getResponse().setContentType(contentType);  
		PrintWriter out = ServletActionContext.getResponse().getWriter();
		query=new String(query.getBytes("iso8859-1"),"UTF-8");  
		Jedis jedis = new Jedis("10.105.245.143",6379);
		result = jedis.zrevrange(query, 0, 5);
		String json = JSONArray.fromObject(result).toString();
		out.print(json);	 
	 }
     
	
	public String getQuery() {
		
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
