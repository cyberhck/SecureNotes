package com.fossdevs.securenotes;

public class Crypt {
	private String key;
	private String text;
	public Crypt() {
	}
	public String encrypt(String text,String key){
		this.text=text;
		this.key=key;
		this.setPadding();
		char t[]=this.text.toCharArray();
		char k[]=this.key.toCharArray();
		String cipher=text;
		char c[]=cipher.toCharArray();
		for(int i=0;i<text.length();i++){
			int sum=(int)t[i]+(int)k[i];
			c[i]=(char)sum;
		}
		String cip=new String(c);
		cipher=cip;
		return cipher;
	}
	public void setPadding(){
		int t=text.length();
		int k=key.length();
		if(t<k){
			//length of text is less than length of key
			this.key=key.substring(0, t);
		}else if(t==k){
			//length of text is equal to length of key
			return;
		}else{
			int leng=t-k;
			int times=(leng%k)+1;
			for(int i=0;i<=times;i++){
				this.key=this.key.concat(this.key);
			}
			String padding=key.substring(0, leng);
			this.key=this.key.concat(padding);
			this.key=this.key.substring(0, t);
		}
	}
	public String decrypt(String cypher,String key){
		String text=cypher;
		this.text=text;
		this.key=key;
		this.setPadding();
		char c[]=cypher.toCharArray();
		char k[]=this.key.toCharArray();
		char t[]=text.toCharArray();
		for(int i=0;i<this.key.length();++i){
			int charVal=(int)c[i];
			int keyVal=(int)k[i];
			int textVal=charVal-keyVal;
			t[i]=(char)textVal;
		}
		text=new String (t);
		return text;
	}
}
