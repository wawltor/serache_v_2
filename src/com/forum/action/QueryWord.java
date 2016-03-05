package com.forum.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.sf.json.JSONArray;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.struts2.ServletActionContext;
import org.wltea.analyzer.lucene.IKAnalyzer;


import com.opensymphony.xwork2.ActionSupport;

public class QueryWord extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String contentType = "text/html;charset=utf-8"; 
	private String line;
	String[] words = new String[22];
	public  void queryResult() throws IOException{
		line=new String(line.getBytes("iso8859-1"),"UTF-8");
		System.out.println("inaction"+line);
		ServletActionContext.getResponse().setContentType(contentType);  
		PrintWriter out = ServletActionContext.getResponse().getWriter();
      
		 Analyzer anal=new IKAnalyzer(true);       
        StringReader reader=new StringReader(line);  
       
        TokenStream ts= anal.tokenStream("", reader);  
        CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);  
      
        int sum = 0;
        while(ts.incrementToken()){  
        	  if(sum<20){
        		  words[sum] = term.toString();
        	  }
        	  sum++;
        	  
        } 
        reader.close();  
         
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "10.105.245.144,10.105.245.147");
     	HTable table = new HTable(conf,"sort".getBytes());
 		// TODO Auto-generated method stub
 	    Map<String,Float> map = new TreeMap<String,Float>();
 	     for(int i =0 ;i<sum ; i++){
 	  
 	    	String key = words[i];
 	    	Get get = new Get(Bytes.toBytes(key));
 			get.addFamily(Bytes.toBytes("title"));
 			Result rst = table.get(get);
 			List<KeyValue> kvs = rst.list();
 			for(KeyValue kv : kvs){
 				String k = Bytes.toString(kv.getQualifier());
 				if(map.get(k)==null){
 				  //int v = Float(Bytes.toString(kv.getValue()));
 				  Float v= Float.parseFloat(Bytes.toString(kv.getValue()));
 				  map.put(k, v); 
 				  
 			    }
 				else{
 					Float v = map.get(k);
 					Float tmp = Float.parseFloat(Bytes.toString(kv.getValue()));
 					v = v + tmp;
 					map.put(k,v);
 							
 				}
 				
 			}
 			
 	    	 
 	     }
 	     List<Map.Entry<String,Float>> list = new ArrayList<Map.Entry<String,Float>>(map.entrySet());
 	     Collections.sort(list,new Comparator<Map.Entry<String,Float>>() {

			@Override
			public int compare(Entry<String, Float> o1, Entry<String, Float> o2) {
				// TODO Auto-generated method stub
			 	return o2.getValue().compareTo(o1.getValue());

			}
 	    	});
 	       HTable table1 = new HTable(conf,"record".getBytes());
 			List<SerachResult> sr = new ArrayList<SerachResult>();
 	       for(Map.Entry<String,Float> entry:list){
 	    	

 	    	 //System.out.println(mapping.getKey()+":"+mapping.getValue());
 	    	   SerachResult s = new SerachResult(); 
 			   Object key1 = entry.getKey();
 			   Object value1 = entry.getValue();
 			
 			   Get get = new Get(Bytes.toBytes((String)key1));
 			   get.addColumn(Bytes.toBytes("message"), Bytes.toBytes("con")); // ��ȡָ������������η��Ӧ����
 		       Result result = table1.get(get);
 			   byte [] buf = result.getValue(Bytes.toBytes("message"),Bytes.toBytes("con"));
 			   String content = Bytes.toString(buf);
 			   Get get1 = new Get(Bytes.toBytes((String)key1));
 			   get1.addColumn(Bytes.toBytes("message"), Bytes.toBytes("url")); // ��ȡָ������������η��Ӧ����
 		       Result result1 = table1.get(get1);
 			   byte [] buf1 = result1.getValue(Bytes.toBytes("message"),Bytes.toBytes("url"));
 			   String url = Bytes.toString(buf1);
               s.setRank((Float)value1);
               System.out.println((Float)value1);
 			   s.setContent(content);
 			   s.setTitle((String)key1);
 			   
 			   s.setUrl(url);
 			   sr.add(s); 

 	    	 }
 		   System.out.println(JSONArray.fromObject(sr).toString());
 		   out.print(JSONArray.fromObject(sr).toString());
        
        
    }
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}  
		
		
		
		
	

}
