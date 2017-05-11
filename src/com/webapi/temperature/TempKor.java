package com.webapi.temperature;

import java.util.ArrayList;
import java.util.List;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.webapi.HttpCommon;
import com.webapi.TimeLibApiCom;

public class TempKor {
	private String tempDat;
	private String tempTim;
	private String temp;
	private String doNam;
	private String doCod;
	private String guNam;
	private String guCod;
	public String getTempDat() {
		return tempDat;
	}
	public void setTempDat(String tempDat) {
		this.tempDat = tempDat;
	}
	public String getTempTim() {
		return tempTim;
	}
	public void setTempTim(String tempTim) {
		this.tempTim = tempTim;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getDoNam() {
		return doNam;
	}
	public void setDoNam(String doNam) {
		this.doNam = doNam;
	}
	public String getDoCod() {
		return doCod;
	}
	public void setDoCod(String doCod) {
		this.doCod = doCod;
	}
	public String getGuNam() {
		return guNam;
	}
	public void setGuNam(String guNam) {
		this.guNam = guNam;
	}
	public String getGuCod() {
		return guCod;
	}
	public void setGuCod(String guCod) {
		this.guCod = guCod;
	}
	
	public static class TempKorCity{
		public String name = "";
		public String code = "";
	}
	public static class TempKorGu{
		public String name = "";
		public String code = "";
	}
	public static void RetrieveCities(List<TempKorCity> cityList){
		HttpCommon http = new HttpCommon();
		int[] size = new int[1];
		size[0] = 0;
		String res = "";
		try {
			res = new String(http.request("http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt", size), "UTF-8");

			JSONArray jsCityArr = (JSONArray)JSONValue.parseWithException(res);
			JSONObject jsCity = null;
			TempKorCity city = null;
			for(int i = 0; i < jsCityArr.size(); i++){
				jsCity = (JSONObject)jsCityArr.get(i);
				city = new TempKorCity();
				city.name = (String)jsCity.get("value");
				city.code = (String)jsCity.get("code");
				cityList.add(city);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void RetrieveGu(String cityCode, List<TempKorGu> guList){
		HttpCommon http = new HttpCommon();
		int[] size = new int[1];
		size[0] = 0;
		String res = "";
		try {
			res = new String(http.request("http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl."+cityCode+".json.txt", size), "UTF-8");

			JSONArray jsArr = (JSONArray)JSONValue.parseWithException(res);
			JSONObject jsObj = null;
			TempKorGu gu = null;
			for(int i = 0; i < jsArr.size(); i++){
				jsObj = (JSONObject)jsArr.get(i);
				gu = new TempKorGu();
				gu.name = (String)jsObj.get("value");
				gu.code = (String)jsObj.get("code");
				guList.add(gu);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void RetrieveTemperature(TempKorCity city, TempKorGu gu, List<TempKor> tempList){
		HttpCommon http = new HttpCommon();
		int[] size = new int[1];
		size[0] = 0;
		String res = "";
		TimeLibApiCom tl = new TimeLibApiCom();
		try {
			
			Document doc = http.parseXML("http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone="+gu.code);
			NodeList header = doc.getElementsByTagName("header");
			String tempDat = null;
			String tempTim = null;
			String tempDay = null;
			
			for (Node node = header.item(0).getFirstChild(); node != null; node = node.getNextSibling()) {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (node.getNodeName().equals("tm")) {
						tempDat = node.getTextContent().substring(0,8);
					}
				}
			}
			NodeList data = doc.getElementsByTagName("data");
			TempKor tempKor = null;
			for(int k=0; k<data.getLength(); k++){
				for (Node node = data.item(k).getFirstChild(); node != null; node = node.getNextSibling()) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						
						if (node.getNodeName().equals("hour")) {
							tempTim = node.getTextContent()+"0000";
							int len = tempTim.length();
							for(int i = 0; i < 6-len; i++){
								tempTim = "0"+tempTim;
							}
						}
						else if(node.getNodeName().equals("day")){
							tempDay = node.getTextContent();
						}
						else if (node.getNodeName().equals("temp")) {
							tempKor = new TempKor();
							String tDat = tl.getDateFromSpecifiedDate(tempDat, Integer.parseInt(tempDay));
							tempKor.setTempDat(tDat);
							if(tempTim.equals("240000")){
								tempKor.setTempDat(tl.getDateFromSpecifiedDate(tDat, 1));
								tempTim = "000000";
							}
							tempKor.setTempTim(tempTim);
							tempKor.setTemp(node.getTextContent());
							tempKor.setDoCod(city.code);
							tempKor.setDoNam(city.name);
							tempKor.setGuCod(gu.code);
							tempKor.setGuNam(gu.name);
							tempList.add(tempKor);
						}
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void RetrieveTemperature(List<TempKor> tempList){

		List<TempKorCity> cityList = new ArrayList<TempKorCity>();
		TempKor.RetrieveCities(cityList);
		for(int i = 0; i < cityList.size(); i++){
			List<TempKorGu> guList = new ArrayList<TempKorGu>();
			TempKor.RetrieveGu(cityList.get(i).code, guList);
			for(int j = 0; j < guList.size(); j++){
				
				TempKor.RetrieveTemperature(cityList.get(i), guList.get(j), tempList);
//				for(int k = 0; k < tempList.size(); k++){
//					System.out.println(tempList.get(k).getGuNam()+" "+tempList.get(k).getTempDat()+tempList.get(k).getTempTim()+" "+tempList.get(k).getTemp());	
//				}
			}
		}
	}
}
