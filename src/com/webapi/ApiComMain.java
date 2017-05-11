package com.webapi;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.webapi.temperature.TempKor;
import com.webapi.temperature.TempKor.TempKorCity;
import com.webapi.temperature.TempKor.TempKorGu;

public class ApiComMain {

	public static void main(String[] args) {
		
//		HttpCommon temp = new HttpCommon();
//		int[] size = new int[1];
//		byte[] res = temp.request("http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt", size);
//		String response = "";
//		try {
//			response = new String(res, "UTF-8");
//			
//			res = temp.request("http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl."+"11"+".json.txt", size);
//
//			response = new String(res, "UTF-8");
//			
//
//			res = temp.request("http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone="+"11110", size);
//
//			response = new String(res, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		
		List<TempKor> tempList = new ArrayList<TempKor>();
		TempKor.RetrieveTemperature(tempList);
		for(int i = 0; i < tempList.size(); i++){
			String tempDat = tempList.get(i).getTempDat();
			String tempTim = tempList.get(i).getTempTim();
			String guNam = tempList.get(i).getGuNam();
			String temp = tempList.get(i).getTemp();
			System.out.println(tempDat+" "+tempTim+" "+guNam+" "+temp);
		}
		System.out.println(tempList.size());
//		List<TempKorCity> cityList = new ArrayList<TempKorCity>();
//		TempKor.RetrieveCities(cityList);
//		for(int i = 0; i < cityList.size(); i++){
//			List<TempKorGu> guList = new ArrayList<TempKorGu>();
//			TempKor.RetrieveGu(cityList.get(i).code, guList);
//			for(int j = 0; j < guList.size(); j++){
//				List<TempKor> tempList = new ArrayList<TempKor>();
//				TempKor.RetrieveTemperature(cityList.get(i), guList.get(j), tempList);
//				for(int k = 0; k < tempList.size(); k++){
//					System.out.println(tempList.get(k).getGuNam()+" "+tempList.get(k).getTempDat()+tempList.get(k).getTempTim()+" "+tempList.get(k).getTemp());	
//				}
//			}
//		}
		//System.out.println(String.valueOf(1));
	}

}
