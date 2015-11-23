package com.movie.util;


/**
 * 星座
 * @author wenyt
 *
 */
public enum Horoscope {
	Capricorn0((byte)0,(byte)1,(byte)1,(byte)1,(byte)19,"魔羯座","Capricorn"),
	Aquarius((byte)1,(byte)1,(byte)20,(byte)2,(byte)18,"水瓶座","Aquarius"),
	Pisces((byte)2,(byte)2,(byte)19,(byte)3,(byte)20,"双鱼座","Pisces"),
	Aries((byte)3,(byte)3,(byte)21,(byte)4,(byte)20,"牡羊座 ","Aries"),
	Taurus((byte)4,(byte)4,(byte)21,(byte)5,(byte)20,"金牛座","Taurus"),
	Gemini((byte)5,(byte)5,(byte)21,(byte)6,(byte)21,"双子座","Gemini"),
	Cancer((byte)6,(byte)6,(byte)22,(byte)7,(byte)22,"巨蟹座","Cancer"),
	Leo((byte)7,(byte)7,(byte)23,(byte)8,(byte)22,"狮子座","Leo"),
	Virgo((byte)8,(byte)8,(byte)23,(byte)9,(byte)22,"处女座","Virgo"),
	Libra((byte)9,(byte)9,(byte)23,(byte)10,(byte)22,"天秤座","Libra"),
	Scorpio((byte)10,(byte)10,(byte)23,(byte)11,(byte)21,"天蝎座","Scorpio"),
	Sagittarius((byte)11,(byte)11,(byte)22,(byte)12,(byte)21,"射手座","Sagittarius"),
	Capricorn1((byte)12,(byte)12,(byte)22,(byte)12,(byte)31,"魔羯座","Capricorn");
	
/*
	魔羯座 (12/22 - 1/19) Capricorn 
	水瓶座 (1/20 - 2/18) Aquarius 
	双鱼座 (2/19 - 3/20) Pisces 
	牡羊座 (3/21 - 4/20) Aries 
	金牛座 (4/21 - 5/20) Taurus 
	双子座 (5/21 - 6/21) Gemini 
	巨蟹座 (6/22 - 7/22) Cancer 
	狮子座 (7/23 - 8/22) Leo 
	处女座 (8/23 - 9/22) Virgo 
	天秤座 (9/23 - 10/22) Libra 
	天蝎座 (10/23 - 11/21) Scorpio 
	射手座 (11/22 - 12/21) Sagittarius
*/
	private byte id;
	private byte startMonth;
	private byte startDate;
	private byte endMonth;
	private byte endDate;
	private String cnName;
	private String enName;
	private Horoscope(byte id, byte startMonth,byte startDate, byte endMonth,byte endDate, String cnName,
			String enName) {
		this.id = id;
		this.startMonth = startMonth;
		this.startDate = startDate;
		this.endMonth = endMonth;
		this.endDate = endDate;
		this.cnName = cnName;
		this.enName = enName;
	}
	public byte getId() {
		return id;
	}
	
	public String getCnName() {
		return cnName;
	}
	public String getEnName() {
		return enName;
	}
	
	
	public static Horoscope getHoroscope(byte month,byte date){
		
		Horoscope horoscope[]=Horoscope.values();
		int start=0,end=horoscope.length-1;
		
		while(start<=end){
			int midd=((start+end)>>1);
			Horoscope m=horoscope[midd];
		
			if(month>m.endMonth){
				start=midd+1;
			}
			else if(month<m.startMonth){
				end=midd-1;
			}
			else{
				if(month==m.startMonth){
					if(date>=m.startDate){
						return m;
					}
					else{
						return horoscope[midd-1];
					}
				}
				else{
					if(date<=m.endDate){
						return m;
					}
					else{
						return horoscope[midd+1];
					}
				}
			}
		}
		throw new RuntimeException("cannot found Horoscope by birthday: "+ month+ "-"+date);
	}
	
	public static Horoscope getHoroscope(byte id){
		try{
			return Horoscope.values()[id];
		}catch(Throwable e){
			throw new RuntimeException("not exist Horoscope: "+id);
		}
	}
	
	public static void main(String args[])throws Exception{
		Horoscope h=getHoroscope((byte)9,(byte)28);
		System.out.println(h);
	}
}
