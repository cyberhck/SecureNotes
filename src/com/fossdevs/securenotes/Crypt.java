package com.fossdevs.securenotes;

public class Crypt {
	private String key;
	//The key for encrypting and decrypting
	private String text;
	public Crypt() {
	//empty constructor, no need of implementation
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
			//add respective positioned characters and store.
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
			while(key.length()<text.length()){
	                key=key+key;
	        }
	        this.key=this.key.substring(0, text.length());
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
