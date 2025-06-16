package com.zhong.rm;

import com.zhong.*;
import com.zhong.utils.CryptoPrimitives;
import com.zhong.utils.MyUtils;
import javafx.geometry.HPos;
import org.bouncycastle.jcajce.provider.symmetric.Serpent;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zhong.SearchProtocol_Split_memory_Test.MultiSearch_get;
import static com.zhong.SearchProtocol_Split_memory_Test.SingleSearchTest_get;

/**
 * @author 赵子豪
 * @create 2023-03-22 20:32:25
 **/

public class test {

    Jedis jedis = null;

    @Test
    public void test() throws IOException {

        //获取Jedis对象
        jedis = RedisWrapper.getJedisObject();
        String filename = "D:\\WorkSpace\\workstation\\CashScheme-master -version2\\testset\\testSet.txt";
        Stream<String> lines = Files.lines(Paths.get(filename));
        List<String> collect = lines.collect(Collectors.toList());
        //System.out.println(collect.size());
        int count = 0;
        for (String re : collect) {

            // String re="Solution 260 [23951451, 23955310, 23990261, 23991671, 23999120, 24011222, 24018043, 24022395, 24031712, 24056083, 24059773, 24072257, 24073122, 24077235, 24082675, 24082995, 24084383, 24095945, 24120049, 24171902, 24177366, 24184605, 24190824, 24199753, 24200851, 24206741, 24210225, 24211957, 24221417, 24223730, 24225498, 24226489, 24236289, 24262954, 24270179, 24271063, 24274407, 24274909, 24276627, 24296157, 24296470, 24306483, 24309196, 24319615, 24339481, 24349608, 24364893, 24367827, 24370027, 24378167, 24383272, 24389962, 24390080, 24434254, 24445207, 24445574, 24449505, 24453125, 24454707, 24459344, 24494126, 24499475, 24502345, 24521849, 24529532, 24551710, 24558566, 24588337, 24595882, 24598717, 24603522, 24603848, 24611098, 24647378, 24651369, 24668736, 24680447, 24700905, 24713397, 24720184, 24740881, 24771325, 24773133, 24785528, 24806050, 24809681, 24839129, 24844384, 24886034, 24887696, 24891716, 24911325, 24912840, 24916356, 24929621, 24942299, 24952147, 24956001, 24960516, 24971404, 24982863, 25016044, 25038674, 25046467, 25059807, 25060001, 25064763, 25071025, 25083300, 25103538, 25105194, 25123709, 25127804, 25132261, 25137172, 25142230, 25145227, 25179139, 25181800, 25183569, 25214358, 25217328, 25260147, 25264491, 25270627, 25272288, 25273140, 25275992, 25284771, 25287880, 25289224, 25340332, 25355766, 25382326, 25386480, 25405869, 25417627, 25428952, 25429843, 25445328, 25460115, 25463466, 25500851, 25513731, 25519718, 25520630, 25536548, 25559243, 25572706, 25594932, 25598358, 25600054, 25604272, 25617470, 25618738, 25624923, 25644277, 25654635, 25665011, 25665521, 25688568, 25689054, 25712445, 25738564, 25745688, 25752364, 25752965, 25809550, 25810352, 25813550, 25819731, 25821227, 25829084, 25841464, 25862404, 25871258, 25876567, 25881060, 25901280, 25902134, 25904973, 25917466, 25923933, 25955086, 25960200, 25960962, 25961743, 25969531, 25972775, 25985370, 26009965, 26015916, 26030322, 26032744, 26049323, 26058414, 26083594, 26103261, 26112287, 26117608, 26119615, 26130113, 26141640, 26152632, 26161356, 26174604, 26191655, 26194827, 26195885, 26196553, 26200683, 26218414, 26242406, 26245455, 26254203, 26254563, 26269351, 26278388, 26311678, 26311772, 26312461, 26313673, 26342406, 26348936, 26395841, 26396440, 26399753, 26403099, 26407069, 26409173, 26417846, 26422303, 26429118, 26459478, 26474139, 26480448, 26487785, 26487878, 26522536, 26529489, 26541003, 26568341, 26573560, 26576270, 26583800, 26584782, 26592735, 26602526, 26610833, 26615974, 26625876, 26645762, 26652212, 26665533, 26673815, 26674927, 26689653, 26707415, 26749102, 26756149]";
            //中间数值显示后面文件的个数
            //String re="Fangoria 35 [23946657, 23971386, 24061146, 24090737, 24102727, 24131841, 24146034, 24186504, 24305984, 24346839, 24451216, 24494077, 24563042, 24753402, 24892602, 24918110, 24970206, 24978412, 25028785, 25065994, 25097042, 25378836, 25393420, 25531604, 25569336, 25606631, 25714458, 25848385, 25933488, 25934563, 26007240, 26190611, 26242774, 26478464, 26483578]";
            List<String> st = Arrays.asList(re.split(" "));
            //System.out.println(st);
            String key = st.get(0);//获取key值
            System.out.println("key:" + key);
            jedis.lpush("allkeys", key);
            //get（2-size()-1）获取filename
            //循环导入
            if (st.size() > 3) {
                //System.out.println("filename1:" + st.get(2).substring(2,6));
                jedis.sadd(key, st.get(2).substring(1, 9));
                for (int i = 3; i < st.size() - 1; i++) {
                    //System.out.println("filename" + (i - 1) + ":" + st.get(i).substring(1, 5));
                    jedis.sadd(key, st.get(i).substring(0, 8));
                }
                //System.out.println(st.get(2).substring(1,9));
                //System.out.println("filename" + (st.size() - 1) + ":" + st.get(st.size() - 1).substring(1, 5));
                jedis.sadd(key, st.get(st.size() - 1).substring(0, 8));
            } else {
                //System.out.println("filename1:" + st.get(2).substring(2, 6));
                jedis.sadd(key, st.get(2).substring(1, 9));
            }
            count++;
        }
        System.out.println("插入结束,共插入" + count + "条记录");
        RedisPool.returnResource(jedis);
    }

    //测试redis连接
    @Test
    public void getAndReleaseResourceTest() {
        jedis = RedisWrapper.getJedisObject();
        System.out.println(jedis);
        RedisPool.returnResource(jedis);
    }

    @Test
    public void randomtest() {
        Random rm = new Random();
        for (int i = 0; i < 100; i++) {
            int k = rm.nextInt(100) + 1;
            System.out.print(k + "\t");
            if (i % 10 == 0)
                System.out.println();
        }
    }

    //获取XSets
    @Test
    public void getXSets() {
        MysqlUtils mq = new MysqlUtils();
        HashMap<String, x_item> res = mq.getXSets_v2(10, 20);
        Collection<x_item> xx = res.values();
        for (x_item x_item : xx) {
            System.out.println(x_item.getW() + " " + x_item.getWeig());
        }
    }

    @Test
    public void getfile() throws IOException {
        long t1 = System.currentTimeMillis();
        jedis = RedisWrapper.getJedisObject();
        String filename = "/home/qdu_workstation/workspace/CashScheme-master -version2/release-youtube-links.txt";
        Stream<String> lines = Files.lines(Paths.get(filename));
        List<String> collect = lines.collect(Collectors.toList());
        int dealdate=0;
        String pre=collect.get(0);
        jedis.lpush("allkeys",pre.split("\t")[0]);
        jedis.sadd(pre.split("\t")[0],pre.split("\t")[1]);
        //将首项移除
        collect.remove(0);
        for (String s : collect) {
            String[] map = s.split("\t");
            String w = map[0];
            String ind = map[1];
            //pre存储前一个关键字信息
            //如果当前关键字等于前一关键字
            if(w.equals(pre)){
                jedis.sadd(w, ind);
            }
            //如果不等于
            else {
                jedis.lpush("allkeys", w);
                jedis.sadd(w, ind);
                pre=w;
            }
            dealdate++;
            if(dealdate%1000==0)
                System.out.println("正在处理"+dealdate+"~"+(dealdate+1000)+"条记录");
        }
        long t2 = System.currentTimeMillis();
        System.out.println("总计用时：" + (t2 - t1) + " ms");

    }

    @Test
    public void frametest(){
        JFrame jf=new JFrame("Search");
        jf.setSize(200,1000);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        JPanel jP=new JPanel();
        JButton jb_sing=new JButton("SingleSearch");//1154986
        JButton jb_multi=new JButton("MultiSerch");
        jP.add(jb_sing);
        jP.add(jb_multi);
        //单关键字查询按钮事件
        jb_sing.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel jp1=new JPanel();
                JLabel jlw=new JLabel("w:");
                JTextField jtw=new JTextField(20);
                jp1.add(jlw);jp1.add(jtw);
                JButton jb1=new JButton("查询");
                jb1.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String w =jtw.getText();
                        try {
                            ArrayList<t_item> res= SingleSearchTest_get(w);
                            System.out.println(res.size());
                            JFrame jf_sing_res=new JFrame("SingleSearchResult");
                            jf_sing_res.setLayout(new GridLayout(res.size()+1,1));
                            jf_sing_res.setSize(400,600);
                            jf_sing_res.setVisible(true);
                            JPanel jpl=new JPanel();
                            JLabel jl=new JLabel(w);
                            jpl.add(jl);
                            jf_sing_res.add(jpl);

                            for(t_item tt:res){
                                JLabel jind=new JLabel(tt.getInd());
                                JLabel jweig=new JLabel(String.valueOf(tt.getWeig()));
                                JPanel jp=new JPanel();
                                jp.add(jind);
                                jp.add(jweig);
                                jf_sing_res.add(jp);
                            }

                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                JButton jb2=new JButton("重置");
                jb2.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jtw.setText("");
                    }
                });
                JPanel jp2=new JPanel();
                jp2.add(jb1);
                jp2.add(jb2);

                JFrame jf_sing=new JFrame("SingleSearch");
                jf_sing.setLayout(new GridLayout(2,1));
                jf_sing.setSize(500,200);
                jf_sing.setVisible(true);
                jf_sing.add(jp1);
                jf_sing.add(jp2);
            }
        });


        //多关键字查询结果
        jb_multi.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jf_multi=new JFrame("MultiSearch");//1101995 1067613 1061625
                jf_multi.setLayout(new GridLayout(4,2));
                jf_multi.setSize(500,200);
                jf_multi.setVisible(true);

                JPanel jp1=new JPanel();
                JPanel jp2=new JPanel();
                JPanel jp3=new JPanel();

                JLabel jl1=new JLabel("w1:");
                JLabel jl2=new JLabel("w2:");
                JLabel jl3=new JLabel("w3:");

                JTextField jt1=new JTextField(20);
                JTextField jt2=new JTextField(20);
                JTextField jt3=new JTextField(20);

                JPanel jpb=new JPanel();
                JButton jb1=new JButton("查询");
                jb1.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<String> kws=new ArrayList<>();
                        kws.add(jt1.getText());
                        kws.add(jt2.getText());
                        kws.add(jt3.getText());
                        try {
                            JFrame jf_multi_res=new JFrame("MultiSearchResult");
                            jf_multi_res.setLayout(new GridLayout(10,1));
                            jf_multi_res.setSize(300,400);

                            HashMap<String,ArrayList<x_item>> ress=MultiSearch_get(kws);
                            System.out.println("size:"+ress.size());
                            System.out.println("result print");
                            JPanel jpl=new JPanel();
                            jf_multi_res.add(jpl);
                            for (Map.Entry<String, ArrayList<x_item>> entry : ress.entrySet()) {
                                JPanel jpind=new JPanel();
                                JLabel jind=new JLabel(entry.getKey());
                                jpind.add(new JLabel("ind:"));
                                jpind.add(jind);
                                jf_multi_res.add(jpind);
                                ArrayList<t_item> re_sing=SingleSearchTest_get(kws.get(0));
                                for (t_item t_item : re_sing) {
                                    if (t_item.getInd().equals(entry.getKey())){
                                        JPanel jpk=new JPanel();
                                        JLabel jlw=new JLabel(kws.get(0));
                                        JLabel jlweig=new JLabel(String.valueOf(t_item.getWeig()));
                                        jpk.add(jlw);jpk.add(jlweig);
                                        jf_multi_res.add(jpk);
                                    }
                                }
                                for (x_item x : entry.getValue()) {
                                    if (x==null) System.out.println("x null");
                                    else {
                                        JPanel jpk=new JPanel();
                                        JLabel jlw=new JLabel(x.getW());
                                        JLabel jlweig=new JLabel(String.valueOf(x.getWeig()));
                                        jpk.add(jlw);jpk.add(jlweig);
                                        jf_multi_res.add(jpk);
                                    }
                                }

                            }
                            jf_multi_res.setVisible(true);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }


                    }
                });

                JButton jb2=new JButton("重置");
                jb2.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jt1.setText("");
                        jt2.setText("");
                        jt3.setText("");
                    }
                });
                jpb.add(jb1);
                jpb.add(jb2);

                jp1.add(jl1);jp1.add(jt1);
                jp2.add(jl2);jp2.add(jt2);
                jp3.add(jl3);jp3.add(jt3);


                jf_multi.add(jp1);
                jf_multi.add(jp2);
                jf_multi.add(jp3);
                jf_multi.add(jpb);


            }
        });


        JFrame jf=new JFrame("Main");
        jf.setLayout(new GridLayout(2,1));
        jf.setSize(500,200);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.add(jP);
    }


    @Test
    public void filetest () throws IOException {
        String filename = "D:\\WorkSpace\\CashScheme-master -version2\\output_keys\\outputkey.txt";
        File fi=new File(filename);
        String str="\n"+"heklo";
        FileOutputStream fio=new FileOutputStream(fi,true);
        fio.write(str.getBytes());
        fio.close();
    }

    @Test
    public void tt() throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        byte[] a={45,25,65,32,56,98,48,78,56,52};
//        byte[] b={12,52,32,54,89,45,26,45,23,69};
//        record r=new record(a,b);
//        byte[] msg=MyUtils.msg2Byte(r);
//        byte[] msg1=new byte[msg.length+1];
//        msg1[0]=1;
//        for (int i = 0; i < msg.length; i++) {
//            msg1[i+1]=msg[i];
//        }
//        byte[] msg2=new byte[msg.length];
//        for (int i = 0; i < msg2.length; i++) {
//            msg2[i]=msg1[i+1];
//        }
//        record r1= (record) MyUtils.byte2Msg(msg2);
//        System.out.println(Arrays.toString(r1.getLabel()));
//        record re=new record(a,b);
//        byte[] be=MyUtils.msg2Byte(re);
//        String st1=MyUtils.parseByte2BinStr(be);
//        System.out.println(st1.length());
//        /////
//        byte[] eb=MyUtils.parseBinStr2Byte(st1);
//        record er= (record) MyUtils.byte2Msg(be);
//        System.out.println(Arrays.toString(re.getLabel()));
//        System.out.println(Arrays.toString(re.getValue()));

    String w="FileBRA_vs_USA_womens_basketball_Rio_2007jpg";
    byte[] w1=MyUtils.getBytes(w,32);
        System.out.println(w.getBytes().length);
    byte[] l=MyUtils.F_(w1,String.valueOf(1));







    }
}
