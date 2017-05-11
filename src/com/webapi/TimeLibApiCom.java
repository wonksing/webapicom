package com.webapi;


import java.util.*;
import java.text.*;

public class TimeLibApiCom {
	SimpleDateFormat smDateTime;
	SimpleDateFormat smDate;
	SimpleDateFormat smTime;
	SimpleDateFormat smDateTimeMill;

	// 20120817 wonk
	SimpleDateFormat wkDate;
	SimpleDateFormat smsDate;
	SimpleDateFormat nextDate;
	
	public TimeLibApiCom() {
		smDateTime	= new SimpleDateFormat( "yyyyMMddHHmmss" );
		smDate		= new SimpleDateFormat( "yyyyMMdd" );
		smTime		= new SimpleDateFormat( "HHmmss" ); 
		smDateTimeMill	= new SimpleDateFormat( "yyyyMMddHHmmssSSS" );
		
		// 20120817 wonk
		wkDate = new SimpleDateFormat("yyyy-MM-dd");
		
		// 20120824 wonk
		smsDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		nextDate		= new SimpleDateFormat( "yyyyMMdd" );
	}

	public String getDateTime() {
		return smDateTime.format( new Date() );
	}

	public String getDate() {
		return smDate.format( new Date() );
	}

	public String getTime() {
		return smTime.format( new Date() );
	}

	public String getMill() {
		return smDateTimeMill.format( new Date() );
	}
	
	public synchronized String getMillSync() {
		return smDateTimeMill.format( new Date() );
	}
	
	// 20120817 wonk
	public String getPastDate(long _days){
		long now = System.currentTimeMillis();
		long past = (long)_days*24*60*60*1000;
		return wkDate.format(new Date(now-past));
	}
	
	public String getSMSDateTime(){
		return smsDate.format(new Date());
	}
	
	public String getNextDate(String curDate){
		Calendar cal = Calendar.getInstance();
		int year = Integer.parseInt(curDate.substring(0,4));
		int month = Integer.parseInt(curDate.substring(4,6));
		month-=1;
		int date = Integer.parseInt(curDate.substring(6,8));
		
		cal.set(year, month, date);
		//Date d2 = cal.getTime();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		
		Date d = cal.getTime();
		return nextDate.format(d);
	}
	
	public String getNextMonth(String curDate){
		Calendar cal = Calendar.getInstance();
		int year = Integer.parseInt(curDate.substring(0,4));
		int month = Integer.parseInt(curDate.substring(4,6));
		month-=1;
		int date = Integer.parseInt(curDate.substring(6,8));
		date = 1;
		cal.set(year, month, date);
		//Date d2 = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		
		Date d = cal.getTime();
		return nextDate.format(d);
	}
	public String getPrevMonth(String curDate){
		Calendar cal = Calendar.getInstance();
		int year = Integer.parseInt(curDate.substring(0,4));
		int month = Integer.parseInt(curDate.substring(4,6));
		month-=1;
		int date = Integer.parseInt(curDate.substring(6,8));
		date = 1;
		cal.set(year, month, date);
		//Date d2 = cal.getTime();
		cal.add(Calendar.MONTH, -1);
		
		Date d = cal.getTime();
		return nextDate.format(d);
	}
	
	public String incSecond(String _date, String _time, int _sec){
		int year = Integer.parseInt(_date.substring(0,4));
		int month = Integer.parseInt(_date.substring(4,6));
		//month-=1;
		int date = Integer.parseInt(_date.substring(6,8));
		
		int hour = Integer.parseInt(_time.substring(0,2));
		int min = Integer.parseInt(_time.substring(2,4));
		int sec = Integer.parseInt(_time.substring(4,6));
		
		sec+=_sec;
		
		Calendar cal = Calendar.getInstance();
		cal.set(year,month,date,hour,min,sec);
		Date d = cal.getTime();
		return smTime.format(d);
	}
	public String incMilliSecond(String _date, String _time, int _msec){
		//Date dd = smDateTimeMill.parse(_date+_time);
		
		int year = Integer.parseInt(_date.substring(0,4));
		int month = Integer.parseInt(_date.substring(4,6));
		//month-=1;
		int date = Integer.parseInt(_date.substring(6,8));
		
		int hour = Integer.parseInt(_time.substring(0,2));
		int min = Integer.parseInt(_time.substring(2,4));
		int sec = Integer.parseInt(_time.substring(4,6));
		int milli = Integer.parseInt(_time.substring(6,9));
		milli+=_msec;
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, sec);
		cal.set(Calendar.MILLISECOND, milli);
		//cal.set(year,month,date,hour,min,sec);
		Date d = cal.getTime();
		SimpleDateFormat smTimeMill		= new SimpleDateFormat( "HHmmssSSS" ); 
		return smTimeMill.format(d);
	}
	
	public String getOpenDate(String baseTime){
		if(getTime().compareTo(baseTime)>=0){
			return smDate.format( new Date() );
		}else{
			long now = System.currentTimeMillis();
			long past = (long)1*24*60*60*1000;
			return smDate.format(new Date(now-past));
		}
		
	}
	
	public long getTimeDiff(String dateTime){
		long ret = 0;
		try{
			if (dateTime==null){
				ret = -1;
			}else{
				DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				//String t1 = tl.getDateTime();
				Date old = null;
				try {
					old = format.parse(dateTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String tmp = getDateTime();
				Date now = null;
				try {
					now = format.parse(tmp);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long diff = now.getTime()-old.getTime();
				ret = diff;
			}
		}catch(Exception e){
			
		}
		return ret;
	}
	public long getTimeDiff(String start, String end){
		long ret = 0;
		try{
			if (start==null || end==null){
				ret = -1;
			}else{
				DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				//String t1 = tl.getDateTime();
				Date old = null;
				try {
					old = format.parse(start);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//String tmp = getDateTime();
				Date now = null;
				try {
					now = format.parse(end);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long diff = now.getTime()-old.getTime();
				ret = diff;
			}
		}catch(Exception e){
			
		}
		return ret;
	}
	
	public String getDateFromSpecifiedDate(String curDate, int days){
		
		Calendar cal = Calendar.getInstance();
		try{
			int year = Integer.parseInt(curDate.substring(0,4));
			int month = Integer.parseInt(curDate.substring(4,6));
			month-=1;
			int date = Integer.parseInt(curDate.substring(6,8));
			
			cal.set(year, month, date);
			//Date d2 = cal.getTime();
			cal.add(Calendar.DAY_OF_YEAR, days);
			
			Date d = cal.getTime();
			return nextDate.format(d);
		}catch(Exception e){
			return null;
		}
	}
	public String getMonthFromSpecifiedMonth(String curDate, int months){
		
		Calendar cal = Calendar.getInstance();
		try{
			int year = Integer.parseInt(curDate.substring(0,4));
			int month = Integer.parseInt(curDate.substring(4,6));
			month-=1;
			int date = Integer.parseInt(curDate.substring(6,8));
			
			cal.set(year, month, date);
			//Date d2 = cal.getTime();
			cal.add(Calendar.MONTH, months);
			
			Date d = cal.getTime();
			return nextDate.format(d);
		}catch(Exception e){
			return null;
		}
	}
	public String getYearFromSpecifiedYear(String curDate, int years){
		
		Calendar cal = Calendar.getInstance();
		try{
			int year = Integer.parseInt(curDate.substring(0,4));
			int month = Integer.parseInt(curDate.substring(4,6));
			month-=1;
			int date = Integer.parseInt(curDate.substring(6,8));
			
			cal.set(year, month, date);
			//Date d2 = cal.getTime();
			cal.add(Calendar.YEAR, years);
			
			Date d = cal.getTime();
			return nextDate.format(d);
		}catch(Exception e){
			return null;
		}
	}
	
	public String getDateTime(String _date, String _time, int _sec){
		int year = Integer.parseInt(_date.substring(0,4));
		int month = Integer.parseInt(_date.substring(4,6));
		month-=1;
		int date = Integer.parseInt(_date.substring(6,8));
		
		int hour = Integer.parseInt(_time.substring(0,2));
		int min = Integer.parseInt(_time.substring(2,4));
		int sec = Integer.parseInt(_time.substring(4,6));
		
		sec+=_sec;
		
		Calendar cal = Calendar.getInstance();
		cal.set(year,month,date,hour,min,sec);
		//cal.add(Calendar.SECOND, _sec);
		Date d = cal.getTime();
		return smDateTime.format(d);
	}

	public String formatDate(String date, String div){
		String formatted = date;
		if(formatted!=null){
			int index = 0;
			String tmp = "";
			for(int i = 0; i < formatted.length(); i++){
				if(formatted.charAt(i)>='0' && formatted.charAt(i)<='9'){
					tmp += formatted.charAt(i);
				}
			}
			formatted = tmp;
			if(formatted.length()==8){
				formatted = formatted.substring(0,4)+div+formatted.substring(4,6)+div+formatted.substring(6,8);
			}else if(date.length()==6){
				formatted = formatted.substring(0,2)+div+formatted.substring(2,4)+div+formatted.substring(4,6);
			}
		}
		return formatted;
	}
    public String formatDate(String newFormat, String date, String oldFormat){
        String ret = date;
        try {
            if (ret != null && ret.length() > 0) {
                DateFormat d = new SimpleDateFormat(oldFormat);
                Date dat = d.parse(date);
                d = new SimpleDateFormat(newFormat);
                ret = d.format(dat);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ret;
    }
	public static String[] DAY_OF_WEEK = {"일", "월", "화", "수", "목", "금", "토"};
	public int getDayOfWk(String datein){
		int dayOfWk = -1;
		try{
			int year = Integer.parseInt(datein.substring(0,4));
			int month = Integer.parseInt(datein.substring(4,6));
			month-=1;
			int date = Integer.parseInt(datein.substring(6,8));

			Calendar cal = Calendar.getInstance();
			cal.set(year, month, date);
			dayOfWk = cal.get(Calendar.DAY_OF_WEEK);
		}catch(Exception e){
			
		}

		return dayOfWk;
	}
}
	
