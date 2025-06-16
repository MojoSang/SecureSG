package com.zhong;

import com.zhong.concurrent.Task;
import com.zhong.rm.MysqlUtils;
import com.zhong.utils.MyUtils;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.*;

/**
 * TSet = tuple-set<br>
 * tuple是所有的 关键词 索引的结果（注：这里的索引指的是使用PRF）<br>
 * set 是该关键词对应的文件名的密文
 *
 **/
public class T_Sets_split_memory implements Serializable{
    public static MK mk = null;
    static {
        mk = EDBSetup_keyUtils.getKey();
    }
    /**
     * key是关键词通过PRF映射之后的结果<br>
     * value是该关键词生成的TSet
     */
    private HashMap<byte[],ArrayList<t_item>> T;

    public HashMap<byte[], ArrayList<t_item>> getT() {
        return T;
    }


    static int B=600000;
    static int S=1000;


    public static void TSetSetup() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        int[][] Free=new int[B][S];
        Random rm=new Random();
        //修改数据库结构，原TSet表中stag字段存放明文的关键字w，拿取明文关键字w在此函数中生成新的stag
        String w;
        byte[] stag;
        //b:1-B
        int b=0;
        ArrayList<t_item> t;
        byte[] s;
        byte[] L;
        byte[] K;
        byte beta;
        int j;
        int p;
        int m=0;
        ArrayList<Task> tasks = MysqlUtils.getAllTask();
        System.out.println("已获取数据");
        HashMap<byte[],ArrayList<record>> TSets;
        ArrayList<record> TSet;
        MK mk;
        restart:
        while(true){
            p=1;
            m=0;
            //重新执行时TSets需清空
            TSets=new HashMap<>();
            mk=EDBSetup_keyUtils.getFreshKey();
            System.out.println("刷新 "+p+" 次");
            p++;
            //Free初始化
            for (int i1 = 0; i1 < Free.length; i1++) {
                for (int i2 = 0; i2 < Free[i1].length; i2++) {
                    Free[i1][i2]=i2+1;
                }
            }
            for (Task task : tasks) {
                m++;//计数器
                //每个task对应一个TSet
                TSet=new ArrayList<>();
                w=task.getStag();
                stag=MyUtils.F_(mk.getKT(),w);
                t=task.getT();
                for (int i = 1; i <= t.size(); i++) {
                    s=MyUtils.msg2Byte(t.get(i-1));//si代表t中第i个
                    byte[] l1=MyUtils.F(stag, String.valueOf(i));//lamda位长度
                    L=MyUtils.HashFunctionL(l1,l1.length);
                    K=MyUtils.HashFunctionK(l1,s.length+1);
                    //选取随机数1-B
                    j=rm.nextInt(S);
                    while (j>S){
                        System.out.println("refresh S");
                        j=rm.nextInt(S);
                    }
                    //如果已选择则重启函数
                    if(Free[b][j]==0){
                        continue restart;
                    }
                    //将选择过的数置零标记
                    Free[b][j]=0;
                    if(i<t.size()) beta=1;
                    else beta=0;
                    //连接beta
                    byte[] si=new byte[s.length+1];
                    si[0]=beta;
                    for (int i1 = 0; i1 < s.length; i1++) {
                        si[i1+1]=s[i1];
                    }
                    TSet.add(new record(L,MyUtils.xor(si,K)));

                }
                b++;
                //一个Task执行完毕后,暂存，最后一齐存储
                TSets.put(stag,TSet);
                if(m%10000==0)
                    System.out.println("已处理"+m+"条数据");
            }
            System.out.println("---------启动存储----------");
            //所有task执行完毕后，启动存储
            MysqlUtils.saveTSet(TSets);
            break;
        }

    }


    /**
     * 基线系统的二层加密
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static void TSetSetupBS() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        int[][] Free=new int[B][S];
        Random rm=new Random();
        //修改数据库结构，原TSet表中stag字段存放明文的关键字w，拿取明文关键字w在此函数中生成新的stag
        String w;
        //b:1-B
        int b=0;
        ArrayList<t_item> t;
        byte[] s;
        byte[] L;
        byte[] K;
        byte beta;
        int j;
        int m;
        ArrayList<Task> tasks = MysqlUtils.getAllTaskBS();
        System.out.println("已获取数据");
        HashMap<String,ArrayList<record>> TSets;
        ArrayList<record> TSet;
        restart:
        while(true){
            m=0;
            //重新执行时TSets需清空
            TSets=new HashMap<>();
            //Free初始化
            for (int i1 = 0; i1 < Free.length; i1++) {
                for (int i2 = 0; i2 < Free[i1].length; i2++) {
                    Free[i1][i2]=i2+1;
                }
            }
            for (Task task : tasks) {
                m++;
                //每个task对应一个TSet
                TSet=new ArrayList<>();
                w=task.getStag();
                t=task.getT();
                for (int i = 1; i <= t.size(); i++) {
                    s=MyUtils.msg2Byte(t.get(i-1));//si代表t中第i个

                    byte[] l1=MyUtils.F_(MyUtils.getBytes(w,32), String.valueOf(i));//lamda位长度
                    L=MyUtils.HashFunctionL(l1,l1.length);
                    K=MyUtils.HashFunctionK(l1,s.length+1);
                    //选取随机数1-B
                    j=rm.nextInt(S);
                    while (j>S){
                        j=rm.nextInt(S);
                    }
                    //如果已选择则重启函数
                    if(Free[b][j]==0){
                        continue restart;
                    }
                    //将选择过的数置零标记
                    Free[b][j]=0;
                    if(i<t.size()) beta=1;
                    else beta=0;
                    //连接beta
                    byte[] si=new byte[s.length+1];
                    si[0]=beta;
                    for (int i1 = 0; i1 < s.length; i1++) {
                        si[i1+1]=s[i1];
                    }
                    TSet.add(new record(L,MyUtils.xor(si,K)));

                }
                b++;
                if(m%10000==0)
                    System.out.println("已处理"+m+"条记录");
                //一个Task执行完毕后,暂存，最后一齐存储
                TSets.put(w,TSet);
            }
            System.out.println("--------启动存储--------");
            //所有task执行完毕后，启动存储
            MysqlUtils.saveTSetBS(TSets);
            break;
        }

    }

    /**
     * 客户端<br>
     * 算法2：生成搜索所需的stag
     * @param w  要搜索的关键词
     * @return 搜索的stag，是搜索的凭证
     * @throws UnsupportedEncodingException 异常
     * @throws NoSuchAlgorithmException 异常
     */
    public static  byte[] TSetGetTag(String w) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] stag = MyUtils.F_(mk.getKT(),w);
        return stag;
    }

    /**
     * 服务器端：<br>
     * 算法3：搜索
     * @param stag 要查询的关键字的stag
     * @return 查询到的结果
     */
    public static  ArrayList<t_item> TSetRetrieve(byte[] stag, String table_name) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MysqlUtils mu = new MysqlUtils();
        ArrayList<t_item> t=new ArrayList<>();
        ArrayList<record> records;
        byte beta=1;
        byte[] L;
        byte[] K;
        byte[] v;
        byte[] s;
        int i=1;
        records=mu.getTSet(Arrays.toString(stag),table_name);
        while(beta==1){
            byte[] l1=MyUtils.F(stag,String.valueOf(i));
            L=MyUtils.HashFunctionL(l1,l1.length);
            for (record record : records) {
                if(Arrays.toString(record.getLabel()).equals(Arrays.toString(L))){
                    K=MyUtils.HashFunctionK(l1,record.getValue().length);
                    v=MyUtils.xor(record.getValue(),K);
                    beta=v[0];
                    s=new byte[v.length-1];
                    //提取s
                    for (int i1 = 0; i1 < s.length; i1++) {
                        s[i1]=v[i1+1];
                    }
                    t.add((t_item) MyUtils.byte2Msg(s));
                    i++;
                }
            }
        }

//        //获取w1的task
//        Task task = mu.getTask(Arrays.toString(stag),table_name);
//        if(task==null){
//            return null;
//        }else{
//            return task.getT();
//        }
        return t;
    }



    public static  ArrayList<t_item> TSetRetrieveBS(String w, String table_name) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        MysqlUtils mu = new MysqlUtils();
        ArrayList<t_item> t=new ArrayList<>();
        ArrayList<record> records;
        byte beta=1;
        byte[] L;
        byte[] K;
        byte[] v;
        byte[] s;
        int i=1;
        records=mu.getTSetBS(w,table_name);
        while(beta==1){
            byte[] l1=MyUtils.F_(MyUtils.getBytes(w,32),String.valueOf(i));
            L=MyUtils.HashFunctionL(l1,l1.length);
            for (record record : records) {
                if(Arrays.toString(record.getLabel()).equals(Arrays.toString(L))){
                    K=MyUtils.HashFunctionK(l1,record.getValue().length);
                    v=MyUtils.xor(record.getValue(),K);
                    beta=v[0];
                    s=new byte[v.length-1];
                    //提取s
                    for (int i1 = 0; i1 < s.length; i1++) {
                        s[i1]=v[i1+1];
                    }
                    t.add((t_item) MyUtils.byte2Msg(s));
                    i++;
                }
            }
        }

//        //获取w1的task
//        Task task = mu.getTask(Arrays.toString(stag),table_name);
//        if(task==null){
//            return null;
//        }else{
//            return task.getT();
//        }
        return t;
    }

    /**
     * 客户端：<br>
     * 算法3：对搜索结果进行解密
     * @param w 查询的关键词
     * @param miwens 服务器返回的数据
     * @return 解密之后的结果
     * @throws IOException 异常
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchProviderException 异常
     * @throws InvalidKeyException 异常
     * @throws InvalidAlgorithmParameterException 异常
     * @throws NoSuchPaddingException 异常
     */
    public  static ArrayList<String> TSetDecode(String w,ArrayList<byte[]> miwens) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        ArrayList<String> res = new ArrayList<String>();
        // 加密文件名所需的密钥
        byte[] K_e = MyUtils.F(mk.getKs(),w);
        for(byte[] miwen : miwens){
            res.add(MyUtils.decrypt_AES_CBC(K_e,miwen));
        }
        return res;
    }

    public T_Sets_split_memory(HashMap<byte[], ArrayList<t_item>> t) {
        T = t;
    }
}
