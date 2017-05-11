package com.webapi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class HttpCommon {
	private String urlStr;
	private int connTimeout = 3000;
	private int readTimeout = 6000;
	private URL url;
	private URLConnection urlConn;
	
	public HttpCommon(){
	}
	private int connect(){
		int ret = -1;
		try {
			this.url = new URL(this.urlStr);
			this.urlConn = this.url.openConnection();
			this.urlConn.setConnectTimeout(connTimeout);
			this.urlConn.setReadTimeout(readTimeout);
			ret = 1;
		} catch (Exception e) {
			e.printStackTrace();
			ret = -1;
		}
		return ret;
	}
	
	private InputStream in = null;
	private ByteArrayOutputStream out = null;
	public byte[] request(String urlStr, int[] size){
		byte[] ret = null;
		this.urlStr = urlStr;
		if(connect()==1){
				
			this.out = new ByteArrayOutputStream();
			try {
				this.in = new BufferedInputStream(this.urlConn.getInputStream());
				//BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
				byte[] buf = new byte[1024];
				int n = 0;
				//while(-1!=(n=in.read(buf))){
				while(-1!=(n=in.read(buf))){
					out.write(buf,0,n);
					size[0]+=n;
				}
				ret = out.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
				ret = null;
			} finally{
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}
	
	public Document parseXML(String urlStr){
		this.urlStr = urlStr;
		DocumentBuilderFactory objDocumentBuilderFactory = null;
		DocumentBuilder objDocumentBuilder = null;
		Document doc = null;

		try {
			if(connect()==1){
				objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
				objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();

				doc = objDocumentBuilder.parse(this.urlConn.getInputStream());	
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}
}
