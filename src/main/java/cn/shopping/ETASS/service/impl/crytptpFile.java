package cn.shopping.ETASS.service.impl;


import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.security.SecureRandom;

public class crytptpFile {

    public crytptpFile() {
    }

    public static final String postfix = ".crypt";



        public static byte[] encrypt(File file,  byte[] key) throws Exception {
            try {
                //ZipUtils.compress();
                SecureRandom random = new SecureRandom();
                DESKeySpec desKey = new DESKeySpec(key);
                //创建一个密匙工厂，然后用它把DESKeySpec转换成
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey securekey = keyFactory.generateSecret(desKey);
                Cipher cipher = Cipher.getInstance("DES");
                cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
                InputStream is = new FileInputStream(file);
                CipherInputStream cis = new CipherInputStream(is, cipher);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
//                OutputStream out = new FileOutputStream("C:\\Users\\shopping\\Documents\\test\\2.txt");
                byte[] buffer = new byte[1024];
                int r;
                while ((r = cis.read(buffer)) > 0) {
                    bos.write(buffer, 0, r);
//                    out.write(buffer, 0, r);
                }
                cis.close();
                is.close();
                bos.close();
                byte[] data = bos.toByteArray();
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void decrypt(byte[] data, String dest,byte[] key) throws Exception {
            try {
                SecureRandom random = new SecureRandom();
                DESKeySpec desKey = new DESKeySpec(key);
                //创建一个密匙工厂，然后用它把DESKeySpec转换成
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey securekey = keyFactory.generateSecret(desKey);
                Cipher cipher = Cipher.getInstance("DES");
                cipher.init(Cipher.DECRYPT_MODE, securekey, random);
                byte[] bytes = cipher.doFinal(data);
                OutputStream out = new FileOutputStream(dest);
                InputStream is = new ByteArrayInputStream(bytes);
                byte[] buff = new byte[1024];
                int len = 0;
                while((len=is.read(buff))!=-1){
                    out.write(buff, 0, len);
                }

                is.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



}
