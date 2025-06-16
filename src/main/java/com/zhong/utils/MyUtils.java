package com.zhong.utils;

import com.google.common.hash.HashFunction;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import org.checkerframework.checker.units.qual.C;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.util.Arrays;

public class MyUtils {

    // 获得resources文件夹下的文件
    public static File getFile(String dirname, String fileName) {
        String path = System.getProperty("user.dir");
        path = path + File.separator + dirname;
        File file = new File(path, fileName);
        return file;
    }

    /**
     * PRF  {0,1}^lambda X {0,1}^lambda -> {0,1}^lambda
     * 生成ke密钥
     * @param Ks 密钥
     * @param w 要加密的字符串
     * @return 由密钥和字符串决定的
     * @throws NoSuchAlgorithmException 初始化MD5算法是出错
     * @throws UnsupportedEncodingException 字符串转byte时出错
     */
    public static byte[] F(byte[] Ks, String w) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bytes= CryptoPrimitives.generateCmac(Ks,w);
        return bytes;
    }


    static byte L=-97;
    public static byte[] HashFunctionL(byte[] F,int length){
        for(int i = 0; i < F.length; i++) {
            F[i] ^= L;
        }
        return Arrays.copyOf(F, length);
    }

    /**
     *
     */
    static Byte K=46;
    public static byte[] HashFunctionK(byte[] F,int length){
        for(int i = 0; i < F.length; i++) {
            F[i] ^= K;
        }
        return Arrays.copyOf(F, length);
    }




    /**
     * 一个随机函数 {0,1}^lambda X {0,1}^lambda -> Zp*
     *
     * @param pairing
     * @param a1
     * @param a2
     * @return
     * @throws Exception
     */
    public static Element Fp(final Pairing pairing, final byte[] a1, final byte[] a2) throws Exception {
        byte[] res = F(a1, new String(a2, "utf-8"));
        return pairing.getZr().newElementFromHash(res, 0, res.length);
    }

    /**
     * 加密关键字生成stag
     * @param kt
     * 加密密钥
     * @param w
     * 加密关键字
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] F_(byte[] kt,String w) throws UnsupportedEncodingException {
        byte[] bytes=CryptoPrimitives.generateHmac(kt,w);
        return bytes;
    }

    /**
     * 获得用来索引关键词的key K_T
     *
     * @return 用来索引关键词的K_T
     */
    public static byte[] generateK_T() {
        // Ks是随机函数F的参数
        final byte[] K_T = CryptoPrimitives.randomBytes(128 / 8);
        return K_T;
    }
    /**
     * AES加密时候使用的初始向量
     */
    private static final byte[] ivBytes = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * 使用CBC模式下的AES加密算法加密
     *
     * @param Ke 密钥 16 byte
     * @param mingWen 要加密的明文
     * @return 加密之后得到的密文
     * @throws IOException 异常
     * @throws InvalidAlgorithmParameterException 异常
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchPaddingException 异常
     * @throws NoSuchProviderException 异常
     * @throws InvalidKeyException 异常
     */
    public static byte[] encrypt_AES_CBC(byte[] Ke, String mingWen) throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException {
        final byte[] miWen = CryptoPrimitives.encryptAES_CBC(Ke, ivBytes, mingWen.getBytes("utf-8"));
        return miWen;
    }

    /**
     * 使用CBC模式下的AES加密算法解密
     * @param Ke 解密密钥
     * @param miWen 需要解密的密文
     * @return 解密之后得到的明文
     * @throws InvalidAlgorithmParameterException 异常
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchPaddingException 异常
     * @throws NoSuchProviderException 异常
     * @throws InvalidKeyException 异常
     * @throws IOException 异常
     */
    public static String decrypt_AES_CBC(byte[] Ke,byte[] miWen) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException, IOException {
        byte[] s = CryptoPrimitives.decryptAES_CBC(miWen, Ke);
        return new String(s,"utf-8");
    }

    /**
     * 将对象转化为数组
     *
     * @param o 待转化的对象
     * @return byte[]数组
     */
    public static  byte[] msg2Byte(Object o){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    /**
     * 将byte[]数组转化为对象
     *
     * @param bytes 待转化的数组
     * @return 转化为的对象
     */
    public static Object byte2Msg(byte[] bytes){
        ByteArrayInputStream bais;
        ObjectInputStream in = null;
        try{
            bais = new ByteArrayInputStream(bytes);
            in = new ObjectInputStream(bais);

            return in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static byte[] xor(byte[] a,byte[] b){
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i]= (byte) (a[i]^b[i]);
        }
        return result;
    }

    public static byte[] xor(String a,byte[] b){
        byte[] result = new byte[a.length()];
        for (int i = 0; i < a.length(); i++) {
            result[i]=0;
        }
        return result;
    }

    /**
     * Translates byte array to a HEX String.
     *
     * @param buf of byte
     * @return a HEX Integer String
     */
    public static String parseByte2BinStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toBinaryString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * Translates a HexStr to a byte array.
     *
     * @param hexStr of a HEX Integer
     * @return a byte array represents the String
     */
    public static byte[] parseBinStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i < hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 2);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 2);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    //s为要转换的string,length为要转换成的byte数组的长度
    public static byte[] getBytes(String s, int length) {
        int fixLength = length - s.getBytes().length;
        if (s.getBytes().length < length) {
            byte[] S_bytes = new byte[length];
            System.arraycopy(s.getBytes(), 0, S_bytes, 0, s.getBytes().length);
            for (int x = length-fixLength; x < length; x++) {
                S_bytes[x] = 0x00;
            }
            return S_bytes;
        }
        return s.getBytes();
    }



}
