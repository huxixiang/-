package com.dcits.restapiapp.scheduler.interfaceprocess.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonPrase implements Serializable {

	private static final long serialVersionUID = 6091136074523681790L;
	
	private static final Logger logger = LoggerFactory.getLogger(JsonPrase.class);

	
	public static void main(String[] args) throws Exception{

		String ss = "[{\"s_name\":\"huxixiang\",\"s_sex\":\"man\",\"s_age\":29,\"s_time\":1588067557959}]";
		System.out.println(praseJsonStr(ss));

	}


	/**
	 * @desc 解析json字符串
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String,Object>> praseJsonStr(String jsonStr)throws Exception{
		List<Map<String,Object>> paraseResult = new ArrayList<>();
		Object jsonObj = JSON.parse(jsonStr);
		if(jsonObj instanceof Map){
			praseMap((Map<?, ?>) jsonObj, null, paraseResult);
		}else if(jsonObj instanceof List){
			praseList((List<?>)jsonObj, null, paraseResult);
		}else{
			throw new Exception("【无法解析此json串："+jsonStr+"】");
		}
		return paraseResult;
	}
	
	/**
	 * 行增加、列不增加
	 */
	public static void praseList(List<?> source, String parentName, List<Map<String, Object>> limitHandleRows){
		List<Map<String, Object>> template = depCopy(limitHandleRows);
		boolean firstFlag = true;
		for(Object obj : source){
			List<Map<String, Object>>  handleRows = null;
			if(firstFlag){
				handleRows = limitHandleRows;
			}else{//增加新行
				handleRows = depCopy(template);
				//limitHandleRows.addAll(handleRows);
			}
			if(obj instanceof Map){
				praseMap((Map<?, ?>)obj, parentName==null?"":parentName.trim(), handleRows);
			}else if(obj instanceof List){
				praseList((List<?>)obj, parentName==null?"":parentName.trim(), handleRows);
			}else{
				if(handleRows.size() == 0){
					Map<String,Object> map = new HashMap<>();
//					map.put(parentName.toUpperCase().trim(),obj.toString().trim());
					map.put(parentName.trim(),obj.toString().trim());
					handleRows.add(map);
				}else{
					for(Map<String, Object> tempRow: handleRows){
//						tempRow.put(parentName.toUpperCase().trim(), obj.toString().trim());
						tempRow.put(parentName.trim(), obj.toString().trim());
					}
				}
			}
			if(!firstFlag){
				limitHandleRows.addAll(handleRows);
			}
			if(firstFlag){
				firstFlag = false;
			}
		}
		
	}
	
	/**
	 * 列增加、行不增加
	 */
	public static void praseMap(Map<?, ?> source, String parentName, List<Map<String, Object>> limitHandleRows){
		for(Map.Entry<?, ?> entry : source.entrySet()){
			String colName = null;
			Object colValue=entry.getValue();
			if (null == parentName || "".equals(parentName.trim())) {
				colName = entry.getKey().toString().trim();
			} else {
				colName = parentName.trim() + "." + entry.getKey().toString().trim();
			}
			if(entry.getValue() instanceof Map){
				praseMap((Map<?, ?>)colValue, colName, limitHandleRows);
			}else if(entry.getValue() instanceof List){
				praseList((List<?>) colValue, colName, limitHandleRows);
			}else{
				String value = entry.getValue() == null ? "" : entry.getValue().toString().trim();
				if(limitHandleRows.size() == 0){
					Map<String, Object> tmpRow = new HashMap<String, Object>();
					limitHandleRows.add(tmpRow);
//					tmpRow.put(colName.toUpperCase().trim(), value);
					tmpRow.put(colName.trim(), value);
				}else{
					for(Map<String, Object> tempRow : limitHandleRows){
//						tempRow.put(colName.toUpperCase().trim(), value);
						tempRow.put(colName.trim(), value);
					}
				}
			}
		}
	}
	
	/***
	 * 对集合进行深拷贝 注意需要对泛型类进行序列化(实现Serializable)
	 * 
	 * @param srcList
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> depCopy(List<T> srcList) {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(srcList);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream inStream = new ObjectInputStream(byteIn);
			@SuppressWarnings("unchecked")
			List<T> destList = (List<T>) inStream.readObject();
			return destList;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从控制台获取字符串
	 * @return
	 */
	public static String getInputString() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
