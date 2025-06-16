package com.zhong;

import com.zhong.rm.MysqlUtils;
import com.zhong.params.curves.utils.MyUtils;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索的类
 *
 * @author 张中俊
 **/
public class SearchProtocol_Split_memory {
    private static MK mk;
    private static Pairing pairing;
    static {
        mk = EDBSetup_keyUtils.getKey();
        pairing = PairingFactory.getPairing("com/zhong/params/curves/a.properties");
    }

    /**
     * 在搜索中客户端需要做的事情 1
     *
     * @param kws 要搜索的关键词
     * @return 返回两部分内容，第一部分是 w1 的 stag，第二部分是搜索的token
     * @throws Exception 异常
     */
    public static Search_Client_1_Output search_client_1(ArrayList<String> kws) throws Exception {
        mk = EDBSetup_keyUtils.getKey();
        pairing = PairingFactory.getPairing("com/zhong/params/curves/a.properties");
        //群的生成元
        Element g = mk.getG();
        //w1的映射结果
        byte[] stag = T_Sets_split_memory.TSetGetTag(kws.get(0));
        // 计算token
        Element xtoken[][] = new Element[100][kws.size()];
        //预测：最多有c个文件包含关键词w1
        // until the server stops
        for (int c = 0; c < 100; c++) {
            for (int i = 1; i < kws.size(); i++) {
                Element left =MyUtils.Fp(pairing,mk.getKz(),(kws.get(0)+c+"").getBytes("utf-8"));
                Element right = MyUtils.Fp(pairing,mk.getKx(),kws.get(i).getBytes("utf-8"));
                Element res = g.duplicate().powZn(left.duplicate().mul(right));
                xtoken[c][i] = res;
            }
        }
        return new Search_Client_1_Output(stag,xtoken);
    }

    /**
     * 在搜索中客户端需要做的事情 1
     *
     * @param kws 要搜索的关键词，要求是结果排序的
     * @return 返回两部分内容，第一部分是 w1 的 stag，第二部分是搜索的token
     * @throws Exception 异常
     */
    public static Search_Client_1_Output search_client_1_version_2(List<String> kws, int count) throws Exception {
        //群的生成元
        Element g = mk.getG();

        //w1的映射结果
        byte[] stag = T_Sets_split_memory.TSetGetTag(kws.get(0));
        // 计算token
        Element xtoken[][] = new Element[count][kws.size()];
        //预测：最多有c个文件包含关键词w1
        // until the server stops
        for (int c = 0; c < count; c++) {
            for (int i = 1; i < kws.size(); i++) {
                Element left =MyUtils.Fp(pairing,mk.getKz(),(kws.get(0)+c+"").getBytes("utf-8"));
                Element right = MyUtils.Fp(pairing,mk.getKx(),kws.get(i).getBytes("utf-8"));

                Element res = g.duplicate().powZn(left.duplicate().mul(right));
                xtoken[c][i] = res;
            }
        }
        return new Search_Client_1_Output(stag,xtoken);
    }

    /**
     * 搜索中服务器需要做的事情
     * @param search_client_1_output 客户端提交的东西
     * @return 搜索的结果，是密文形式的
     */
    public static ArrayList<byte[]> search_server(Search_Client_1_Output search_client_1_output, String table_name) throws UnsupportedEncodingException {
        //搜索的结果
        ArrayList<byte[]> es = new ArrayList<>();


        //密文下w和ind
        ArrayList<t_item> t = T_Sets_split_memory.TSetRetrieve(search_client_1_output.getStag(),table_name);
        if (t != null) {
            System.out.println("tsize:"+t.size());
        }
        else
            System.out.println("t null");
        //mysql的utils类
        MysqlUtils mu = new MysqlUtils();
        if(t==null){
            System.out.println("w1不包含在TSets中");
            return null;
        }
        //遍历t集合
        for(int c=0;c<t.size();c++){
            //要求全部是真
            boolean flag2 = true;
            t_item tt = t.get(c);//获取单个对象
            System.out.println(tt.toString());
            Element y = tt.getY().getElement();//获取序列化元素--索引
            for(int i=1;i<search_client_1_output.getXtoken()[c].length;i++){
                Element e = search_client_1_output.getXtoken()[c][i].duplicate();
                Element ee = e.duplicate().powZn(y);

                //System.out.println(ee.toString());
                flag2 = flag2 && MysqlUtils.isInXSets_v2(ee.toString());
                if(!flag2){
                    break;
                }
            }
            if (flag2) {
                System.out.println("add");
                es.add(tt.getE());
            }
            //把e存储
            //es.add(tt.getE());
        }
        return es;
    }
    public static ArrayList<t_item> search_server_Single(Search_Client_1_Output search_client_1_output, String table_name) throws UnsupportedEncodingException {

        //搜索的结果
        ArrayList<t_item> t = T_Sets_split_memory.TSetRetrieve(search_client_1_output.getStag(),table_name);

        //经过匹配筛选后的结果
        ArrayList<t_item> es=new ArrayList<t_item>();
//        if (t != null) {
//            System.out.println("tsize:"+t.size());
//        }
//        else
//            System.out.println("t null");
        //mysql的utils类
        MysqlUtils mu = new MysqlUtils();
        if(t==null){
            System.out.println("w1不包含在TSets中");
            return null;
        }
        //遍历t集合
        for(int c=0;c<t.size();c++){
            //要求全部是真
            boolean flag2 = true;
            t_item tt = t.get(c);//获取单个对象
            Element y = tt.getY().getElement();//获取序列化元素--索引
            for(int i=1;i<search_client_1_output.getXtoken()[c].length;i++){
                Element e = search_client_1_output.getXtoken()[c][i].duplicate();
                //ee表示w和ind有无关联信息
                Element ee = e.duplicate().powZn(y);

                //System.out.println(ee.toString());
                flag2 = flag2 && MysqlUtils.isInXSets_v2(ee.toString());
                if(!flag2){
                    break;
                }
            }
            if (flag2) {
                es.add(tt);
            }
        }

        return es;
    }

    public static HashMap<byte[],ArrayList<byte[]>> search_server_Multi(Search_Client_1_Output search_client_1_output, String table_name) throws UnsupportedEncodingException {

        //搜索的结果
        //存放w1的t_item[[e,y],[e,y]]
        ArrayList<t_item> t = T_Sets_split_memory.TSetRetrieve(search_client_1_output.getStag(),table_name);

        //返回结果存放
        //ind <w1,weig1>,<w2,weig>
        HashMap<byte[],ArrayList<byte[]>> res=new HashMap<>();

        if (t != null) {
            System.out.println("tsize:"+t.size());
        }
        else
            System.out.println("t null");
        //mysql的utils类
        MysqlUtils mu = new MysqlUtils();
        if(t==null){
            System.out.println("w1不包含在TSets中");
            return null;
        }
        //遍历t集合
        //遍历所有的w和ind关联
        for(int c=0;c<t.size();c++){
            //要求全部是真
            boolean flag2 = true;
            ArrayList<byte[]> xx=new ArrayList<>();
            t_item tt = t.get(c);//获取单个对象
            Element y = tt.getY().getElement();//获取序列化元素--索引
            for(int i=1;i<search_client_1_output.getXtoken()[c].length;i++){
                Element e = search_client_1_output.getXtoken()[c][i].duplicate();
                //ee表示w和ind有无关联信息
                Element ee = e.duplicate().powZn(y);

                flag2 = flag2 && MysqlUtils.isInXSets_v2(ee.toString());
                //无关则退出
                if(!flag2){
                    break;
                }
                //获取xset中存储的相关<w,weig>
                byte[] bx=MysqlUtils.getXsetsWW(ee.toString());
                xx.add(bx);
                res.put(tt.getE(),xx);
            }
//            if (flag2) {
//                res.put(tt.getE(),xx);
//            }

        }
        return res;
    }


    //返回整个t队列，用于拿取权值和文件名
    public static ArrayList<t_item> search_server_v2(Search_Client_1_Output search_client_1_output, String table_name) throws UnsupportedEncodingException {

        //搜索的结果
        ArrayList<t_item> t = T_Sets_split_memory.TSetRetrieve(search_client_1_output.getStag(),table_name);

        ArrayList<byte[]> res=new ArrayList<>();
        MyUtils mt=new MyUtils();
        //返回值
        //ind,<w,weig>
        HashMap<String,x_item> x=new HashMap<>();
        //经过匹配筛选后的结果
        ArrayList<t_item> es=new ArrayList<t_item>();
        if (t != null) {
            System.out.println("tsize:"+t.size());

        }
        else
            System.out.println("t null");
        //mysql的utils类
        MysqlUtils mu = new MysqlUtils();
        if(t==null){
            System.out.println("w1不包含在TSets中");
            return null;
        }
        //遍历t集合
        for(int c=0;c<t.size();c++){
            //要求全部是真
            boolean flag2 = true;
            t_item tt = t.get(c);//获取单个对象
            System.out.println("ind:"+tt.getE());
            System.out.println("weig"+tt.getWeig());
            Element y = tt.getY().getElement();//获取序列化元素--索引
            for(int i=1;i<search_client_1_output.getXtoken()[c].length;i++){
                Element e = search_client_1_output.getXtoken()[c][i].duplicate();
                //ee表示w和ind有无关联信息
                Element ee = e.duplicate().powZn(y);

                //System.out.println(ee.toString());
                flag2 = flag2 && MysqlUtils.isInXSets(ee.toString());
                if(!flag2){
                    break;
                }
                System.out.println("flag:"+flag2);
                //获取相关<w,weig>
                byte[] xx=MysqlUtils.getXsetsWW(ee.toString());
//                x_item xt=(x_item) mt.byte2Msg(xx);
                System.out.println("xx:"+xx);
                //从xset中获取对应的w和weig并存储
                //最后存储开始的w和weig
                //如果flag为true则加入
            }
            if (flag2) {
                es.add(tt);
            }
        }

        return es;
    }

    /**
     * 在搜索中客户端需要做的事情 2
     *
     * @param w1 搜索的关键字的出现次数最少的
     * @param es 搜索得到的文件名的密文
     * @return 搜索得到的文件名的明文
     * @throws IOException 异常
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchProviderException 异常
     * @throws InvalidKeyException 异常
     * @throws InvalidAlgorithmParameterException 异常
     * @throws NoSuchPaddingException 异常
     */
    public static ArrayList<String> search_client_2(String w1,ArrayList<byte[]> es) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        ArrayList<String> inds = new ArrayList<>();
        // ke 是用来加密文件名的Identifier 的key
        byte[] Ke = MyUtils.F(mk.getKs(), w1);
        System.out.println("essize:"+es.size());
        for(byte[] e : es){
            String ind = MyUtils.decrypt_AES_CBC(Ke,e);
            inds.add(ind);
        }
        return inds;
    }

    public static ArrayList<t_item> search_client_2_v2(String w1,ArrayList<t_item> es) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException {

        // ke 是用来加密文件名的Identifier 的key
        byte[] Ke = MyUtils.F(mk.getKs(), w1);

        ArrayList<t_item> ress=new ArrayList<t_item>();
        for(t_item t:es){
            String ind=MyUtils.decrypt_AES_CBC(Ke,t.getE());
            ress.add(new t_item(ind,t.getY(),t.getWeig()));
        }
        return ress;
    }

    //es: ind [w,weig] [w,weig]
    public static HashMap<String,ArrayList<x_item>> search_client_2_Multi(String w1,HashMap<byte[],ArrayList<byte[]>> es) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException {

        // ke 是用来加密文件名的Identifier 的key
        byte[] Ke = MyUtils.F(mk.getKs(), w1);
        //返回结果
        HashMap<String,ArrayList<x_item>> res=new HashMap<>();

        for (Map.Entry<byte[], ArrayList<byte[]>> arrayListEntry : es.entrySet()) {
            //解密ind
            String ind=MyUtils.decrypt_AES_CBC(Ke,arrayListEntry.getKey());
            ArrayList<x_item> xs=new ArrayList<>();
            for (byte[] bytes : arrayListEntry.getValue()) {
                x_item x=(x_item) MyUtils.byte2Msg(bytes);
                xs.add(x);
            }

            res.put(ind,xs);
        }
        return res;
    }

    public static ArrayList<t_item> SingleSearchTest_get(String w) throws Exception {
        ArrayList<String> kws = new ArrayList<>();
        kws.add(w);
        Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1(kws);
        ArrayList<t_item> res = SearchProtocol_Split_memory.search_server_Single(sc1o, "tsets");
        ArrayList<t_item> ress = SearchProtocol_Split_memory.search_client_2_v2(kws.get(0), res);
        return  ress;
    }
}

