package org.smk.application;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MCrypt {

    private static MCrypt instance;
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;
    private String iv = "ERCFECX6L3YOBQXU";
    private String key = "dYzxXzu8MYyWyhEDawCumvhegFQquPwj";
    
    public static MCrypt getInstance() {
    	if(instance == null){
    		instance = new MCrypt();
		}
		return instance;
	}
        
    public MCrypt()
    {
        try {
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                
                ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));

                keyspec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
                
        } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    public String encrypt(String text){
		String encrypted = null;
		try {
			encrypted = bytesToHex( encryption(text) );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encrypted;
    }
    
    public String decrypt(String code){
    	String decrypted = null;
		try {
			decrypted = new String( decryption( code ) );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decrypted;
    }
    
    private byte[] encryption(String text) throws Exception
    {
        if(text == null || text.length() == 0)
                throw new Exception("Empty string");
        
        byte[] encrypted = null;

        try {
                cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

                encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        } catch (Exception e)
        {                       
                throw new Exception("[encrypt] " + e.getMessage());
        }
        
        return encrypted;
    }
    
    private byte[] decryption(String code) throws Exception
    {
        if(code == null || code.length() == 0)
                throw new Exception("Empty string");
        
        byte[] decrypted = null;

        try {
                cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
                
                decrypted = cipher.doFinal(hexToBytes(code));
        } catch (Exception e)
        {
                throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }
    

    
    private static String bytesToHex(byte[] data)
    {
        if (data==null)
        {
                return null;
        }
        
        int len = data.length;
        String str = "";
        for (int i=0; i<len; i++) {
                if ((data[i]&0xFF)<16)
                        str = str + "0" + Integer.toHexString(data[i]&0xFF);
                else
                        str = str + Integer.toHexString(data[i]&0xFF);
        }
        return str;
    }
    
            
    private static byte[] hexToBytes(String str) {
        if (str==null) {
                return null;
        } else if (str.length() < 2) {
                return null;
        } else {
                int len = str.length() / 2;
                byte[] buffer = new byte[len];
                for (int i=0; i<len; i++) {
                        buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
                }
                return buffer;
        }
    }
    
    

    private static String padString(String source)
    {
      char paddingChar = ' ';
      int size = 16;
      int x = source.length() % size;
      int padLength = size - x;

      for (int i = 0; i < padLength; i++)
      {
          source += paddingChar;
      }

      return source;
    }
}
