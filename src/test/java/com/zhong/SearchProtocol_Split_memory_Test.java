package com.zhong;

import com.zhong.utils.MyUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;


public class SearchProtocol_Split_memory_Test {
    @Test
    //单一关键字查询
    public void searchProtocolTest() throws Exception {
        ArrayList<String> kws = new ArrayList<>();
        kws.add("RFIs");
        Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1(kws);
        ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, "tsets");
        ArrayList<String> ress = SearchProtocol_Split_memory.search_client_2(kws.get(0), res);
        for (String filename : ress) {
            System.out.println(filename);
        }
    }

    @Test
    public void SingleSearchTest() throws Exception {

        ArrayList<String> kws = new ArrayList<>();
        kws.add("1028465");

        Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1(kws);
        long t1 = System.currentTimeMillis();
        ArrayList<t_item> res = SearchProtocol_Split_memory.search_server_Single(sc1o, "tset");
        long t2 = System.currentTimeMillis();
        ArrayList<t_item> ress = SearchProtocol_Split_memory.search_client_2_v2(kws.get(0), res);

        System.out.println("key:"+kws.get(0));
        System.out.println("result print:");
//        File fi=new File("D:\\WorkSpace\\CashScheme-master -version2\\output_keys\\output.txt");
//        FileOutputStream fio=new FileOutputStream(fi,true);
        System.out.println(t2-t1+"ms");
        for (t_item tt : ress) {
            System.out.println("ind："+tt.getInd()+" weig："+tt.getWeig());
//            fio.write((tt.getWeig()+"\n").getBytes());
//            fio.write((tt.getInd()+"\n").getBytes());
        }


    }


    @Test
    public void SingleSearchTestBS() throws Exception {
        ArrayList<String> kws = new ArrayList<>();
        kws.add("108");
        Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1BS(kws);

        ArrayList<t_item> res = SearchProtocol_Split_memory.search_server_SingleBS(sc1o, "tsetbs");

//        ArrayList<t_item> ress = SearchProtocol_Split_memory.search_client_2_v2(kws.get(0), res);
        System.out.println("key:"+kws.get(0));

        System.out.println("result print:");
//        File fi=new File("D:\\WorkSpace\\CashScheme-master -version2\\output_keys\\output.txt");
//        FileOutputStream fio=new FileOutputStream(fi,true);
        for (t_item tt : res) {
            System.out.println("ind："+tt.getInd()+" weig："+tt.getWeig());
//            fio.write((tt.getWeig()+"\n").getBytes());
//            fio.write((tt.getInd()+"\n").getBytes());
        }


    }

    public static ArrayList<t_item> SingleSearchTest_get(String w) throws Exception {
        ArrayList<String> kws = new ArrayList<>();
        kws.add(w);
        Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1(kws);
        ArrayList<t_item> res = SearchProtocol_Split_memory.search_server_Single(sc1o, "tset");
        ArrayList<t_item> ress = SearchProtocol_Split_memory.search_client_2_v2(kws.get(0), res);
        return  ress;
    }
    public static ArrayList<t_item> SingleSearchTest_getBS(String w) throws Exception {
        ArrayList<String> kws = new ArrayList<>();
        kws.add(w);
        Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1BS(kws);
        ArrayList<t_item> res = SearchProtocol_Split_memory.search_server_SingleBS(sc1o, "tsetbs");
        return  res;
    }

    public static HashMap<String,ArrayList<x_item>> MultiSearch_get(ArrayList<String> kws) throws Exception {
        MK mk = EDBSetup_keyUtils.getKey();
        Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1(kws);
        System.out.println("查询第二阶段 开始");
        long t1 = System.currentTimeMillis();
        HashMap<byte[],ArrayList<byte[]>> res = SearchProtocol_Split_memory.search_server_Multi(sc1o, "TSets");
        long t2 = System.currentTimeMillis();
        System.out.println("查询所用时间 " + (t2 - t1) + " ms");

        System.out.println("查询第三阶段 开始");
        HashMap<String,ArrayList<x_item>> ress = SearchProtocol_Split_memory.search_client_2_Multi(kws.get(0), res);
        return ress;
    }

    @Test
    //多关键字查询
    public void MultiSearchTest() throws Exception {
        MK mk = EDBSetup_keyUtils.getKey();
        ArrayList<String> kws = new ArrayList<>();
        kws.add("1028465");
        kws.add("686496");


        System.out.println("查询第一阶段 开始");
        //第一阶段把关键字列表输入
        Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1(kws);
        System.out.println("查询第二阶段 开始");
        long t1 = System.currentTimeMillis();
        HashMap<byte[],ArrayList<byte[]>> res = SearchProtocol_Split_memory.search_server_Multi(sc1o, "tset");
        long t2 = System.currentTimeMillis();
        System.out.println("查询所用时间 " + (t2 - t1) + " ms");

        System.out.println("查询第三阶段 开始");
        HashMap<String,ArrayList<x_item>> ress = SearchProtocol_Split_memory.search_client_2_Multi(kws.get(0), res);
        System.out.println("result print:");
        for (Map.Entry<String, ArrayList<x_item>> entry : ress.entrySet()) {
            System.out.println("ind:"+entry.getKey());
            ArrayList<t_item> re_sing=SingleSearchTest_get(kws.get(0));
            for (t_item t_item : re_sing) {
                if (t_item.getInd().equals(entry.getKey())){
                    System.out.print("w:"+kws.get(0));
                    System.out.print("  weig:"+t_item.getWeig());
                }
            }
            System.out.println();
            for (x_item x : entry.getValue()) {
                if (x==null) System.out.println("x null");
                else {
                    System.out.print("w:" + x.getW());
                    System.out.print("  weig:" + x.getWeig());
                }
                System.out.println();
            }
        }
    }

    @Test
    //多关键字查询
    public void MultiSearchTestBS() throws Exception {
        MK mk = EDBSetup_keyUtils.getKey();
        ArrayList<String> kws = new ArrayList<>();

        kws.add("774739");
//        kws.add("1134856");
//        kws.add("1081019");
        kws.add("1127117");
//        kws.add("1082385");
//        kws.add("713542");
//        kws.add("1054128");
//        kws.add("1066307");
//        kws.add("995046");
//        kws.add("1069276");
//        kws.add("1061625");


        System.out.println("查询第一阶段 开始");
        //第一阶段把关键字列表输入
        Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1BS(kws);
        System.out.println("查询第二阶段 开始");
        long t1 = System.currentTimeMillis();
        HashMap<String,ArrayList<byte[]>> res = SearchProtocol_Split_memory.search_server_MultiBS(sc1o, "tsetbs");
        long t2 = System.currentTimeMillis();
        System.out.println("查询所用时间 " + (t2 - t1) + " ms");

        System.out.println("查询第三阶段 开始");
        HashMap<String,ArrayList<x_item>> ress = SearchProtocol_Split_memory.search_client_2_MultiBS(kws.get(0), res);
        System.out.println("result print:");
        for (Map.Entry<String, ArrayList<x_item>> entry : ress.entrySet()) {
            System.out.println("ind:"+entry.getKey());
            ArrayList<t_item> re_sing=SingleSearchTest_getBS(kws.get(0));
            for (t_item t_item : re_sing) {
                if (t_item.getInd().equals(entry.getKey())){
                    System.out.print("w:"+kws.get(0));
                    System.out.print("  weig:"+t_item.getWeig());
                }
            }
            System.out.println();
            for (x_item x : entry.getValue()) {
                if (x==null) System.out.println("x null");
                else {
                    System.out.print("w:" + x.getW());
                    System.out.print("  weig:" + x.getWeig());
                }
                System.out.println();
            }
        }
    }

    @Test
    //从测试集中随机获取三个关键字查询
    public void searchProtocolTest3() throws Exception {

        //读取测试集
        File testSetFile = MyUtils.getFile("testset", "testSet.txt");
        List<String> lines = IOUtils.readLines(new FileInputStream(testSetFile));

        Map<String, Integer> kws_count = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            Random random = new Random();
            int index = random.nextInt(lines.size());
            String t = lines.get(index);
            kws_count.put(t.split(" ")[0], Integer.parseInt(t.split(" ")[1]));
        }
        //这里将map.entrySet()转换成list
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(kws_count.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            //升序排序
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

        });

        ArrayList<String> kws = new ArrayList<>();
        for (String s : kws_count.keySet()) {
            kws.add(s);
            System.out.println(s);
        }
        int count = kws_count.get(kws.get(0));

        MK mk = EDBSetup_keyUtils.getKey();
        System.out.println("查询第一阶段 开始");
        Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, count);

        System.out.println("查询第二阶段 开始");
        long t1 = System.currentTimeMillis();
        ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, "TSets");
        System.out.println("resize:"+res.size());
        long t2 = System.currentTimeMillis();
        System.out.println("查询所用时间 " + (t2 - t1) + " ms");

        System.out.println("查询第三阶段 开始");
        ArrayList<String> ress = SearchProtocol_Split_memory.search_client_2(kws.get(0), res);
        for (String filename : ress) {
            System.out.println("filename:"+filename);
        }
    }



    @Test
    public void searchProtocolTest4() throws Exception {
        //读取测试集
        File testSetFile = MyUtils.getFile("testset", "testSet.txt");
        List<String> lines = IOUtils.readLines(new FileInputStream(testSetFile));

        long sum_time = 0;
        for (int i = 0; i < 100; i++) {
            Map<String, Integer> kws_count = new HashMap<>();
            for (int j = 0; j < 3; j++) {
                Random random = new Random();
                int index = random.nextInt(lines.size());
                String t = lines.get(index);
                kws_count.put(t.split(" ")[0], Integer.parseInt(t.split(" ")[1]));
            }
            //这里将map.entrySet()转换成list
            ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(kws_count.entrySet());
            //然后通过比较器来实现排序
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                //升序排序
                public int compare(Map.Entry<String, Integer> o1,
                                   Map.Entry<String, Integer> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }

            });

            ArrayList<String> kws = new ArrayList<>();
            for (String s : kws_count.keySet()) {
                kws.add(s);
            }
            int count = kws_count.get(kws.get(0));

            //这里是查询部分
            SearchProtocol_Split_memory ssm = new SearchProtocol_Split_memory();
            MK mk = EDBSetup_keyUtils.getKey();
            System.out.println("查询第一阶段 开始");
            Search_Client_1_Output sc1o = ssm.search_client_1_version_2(kws, count);

            System.out.println("查询第二阶段 开始");
            long t1 = System.currentTimeMillis();
            ArrayList<byte[]> res = ssm.search_server(sc1o, "TSets");
            long t2 = System.currentTimeMillis();
            System.out.println("查询所用时间 " + (t2 - t1) + " ms");
            sum_time = sum_time + (t2 - t1);
            if (res == null) {
                continue;
            }

            System.out.println("查询第三阶段 开始");
            ArrayList<String> ress = ssm.search_client_2(kws.get(0), res);
            for (String filename : ress) {
                System.out.println(filename);
            }
        }
        System.out.println("100次查询需要 " + sum_time + " ms");
    }




    @Test
    public void searchProtocolTest9() throws Exception {

        ArrayList<String> table_names = new ArrayList<>();
        table_names.add("TSets_32768_rand");
        table_names.add("TSets_65536_rand");
        table_names.add("TSets_131072_rand");
        table_names.add("TSets_262144_rand");
        table_names.add("TSets_524288_rand");
        table_names.add("TSets_1048576_rand");
        for (String tableName : table_names) {
            long sum_t = 0;
            //进行100次查询
            for (int i = 0; i < 100; i++) {
                //要查询的关键字
                List<String> kws = new ArrayList<>();
                kws.add("key3");
                kws.add("key1");
                kws.add("key2");

                int numLeastKeyword = 10;

                //System.out.println("查询第一阶段 开始");
                Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, numLeastKeyword);
                //System.out.println("查询第二阶段 开始");
                long t1 = System.currentTimeMillis();
                ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, tableName);
                long t2 = System.currentTimeMillis();
                //System.out.println("查询所用时间 " + (t2 - t1) + " ms");
                sum_t = sum_t + (t2 - t1);

                //下面是解密的部分
                if (res == null) {
                    continue;
                }else {
                    //List<String> inds = SearchProtocol_Split_memory.search_client_2(kws.get(0),res);
                    //System.out.println("搜索结果"+res.size());
                    //System.out.println("搜索的关键词："+kws.toString());
                    //System.out.println("搜索结果："+inds.toString());
                }
            }
            System.out.println("表 " + tableName + " 花费时间：" + (sum_t / 100) + " ms");
        }
    }
    @Test
    public void searchProtocolTest10() throws Exception {

        ArrayList<String> table_names = new ArrayList<>();
//        table_names.add("TSets_32768_rand");
//        table_names.add("TSets_65536_rand");
//        table_names.add("TSets_131072_rand");
//        table_names.add("TSets_262144_rand");
//        table_names.add("TSets_524288_rand");
//        table_names.add("TSets_1048576_rand");
        table_names.add("TSets");
        for (String tableName : table_names) {
            long sum_t = 0;
            //进行100次查询
            for (int i = 0; i < 100; i++) {
                //要查询的关键字
                List<String> kws = new ArrayList<>();
                kws.add("clubnumber10");
                kws.add("clubnumber11");
                kws.add("clubnumber12");

                int numLeastKeyword = 9;

                //System.out.println("查询第一阶段 开始");
                Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, numLeastKeyword);
                //System.out.println("查询第二阶段 开始");
                long t1 = System.currentTimeMillis();
                ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, tableName);
                long t2 = System.currentTimeMillis();
                //System.out.println("查询所用时间 " + (t2 - t1) + " ms");
                sum_t = sum_t + (t2 - t1);

                //下面是解密的部分
                if (res == null) {
                    continue;
                }else {
                    //List<String> inds = SearchProtocol_Split_memory.search_client_2(kws.get(0),res);
                    //System.out.println("搜索结果"+res.size());
                    //System.out.println("搜索的关键词："+kws.toString());
                    //System.out.println("搜索结果："+inds.toString());
                }
            }
            System.out.println("表 " + tableName + " 花费时间：" + (sum_t / 100) + " ms");
        }
    }


    @Test
    public void searchProtocolTest11() throws Exception {

        ArrayList<String> table_names = new ArrayList<>();
        table_names.add("TSets_32768_rand");
        table_names.add("TSets_65536_rand");
        table_names.add("TSets_131072_rand");
        table_names.add("TSets_262144_rand");
        table_names.add("TSets_524288_rand");
        table_names.add("TSets_1048576_rand");
        for (String tableName : table_names) {
            long sum_t = 0;
            //进行100次查询
            for (int i = 0; i < 2; i++) {
                //要查询的关键字
                List<String> kws = new ArrayList<>();
                kws.add("oP");
                kws.add("nk");

                int numLeastKeyword = 4;

                //System.out.println("查询第一阶段 开始");
                Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, numLeastKeyword);
                //System.out.println("查询第二阶段 开始");
                long t1 = System.currentTimeMillis();
                ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, tableName);
                long t2 = System.currentTimeMillis();
                //System.out.println("查询所用时间 " + (t2 - t1) + " ms");
                sum_t = sum_t + (t2 - t1);

                //下面是解密的部分
                if (res == null) {
                    continue;
                }else {
                    List<String> inds = SearchProtocol_Split_memory.search_client_2(kws.get(0),res);
                    System.out.println("搜索结果"+res.size());
                    System.out.println("搜索的关键词："+kws.toString());
                    System.out.println("搜索结果："+inds.toString());
                }
            }
            System.out.println("表 " + tableName + " 花费时间：" + (sum_t / 100) + " ms");
        }
    }



    @Test
    public void search_serverTest12() throws Exception {
        ArrayList<String> table_names = new ArrayList<>();
//        table_names.add("TSets_32768_rand");
//        table_names.add("TSets_65536_rand_v2");
//        table_names.add("TSets_131072_rand_v2");
//        table_names.add("TSets_262144_rand_v2");
//        table_names.add("TSets_524288_rand_v2");
//        table_names.add("TSets_1048576_rand_v2");
        table_names.add("TSets");
        for (String tableName : table_names) {
            System.out.println("====正在搜索："+tableName+"======");
            long sum_t = 0;

            //要查询的关键字
            List<String> kws = new ArrayList<>();
            kws.add("clubnumber10");
            kws.add("clubnumber11");
            kws.add("clubnumber12");
            int numLeastKeyword = 9;

            //System.out.println("查询第一阶段 开始");
            Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, numLeastKeyword);
            //System.out.println("查询第二阶段 开始");
            long t1 = System.currentTimeMillis();
            ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, tableName);
            long t2 = System.currentTimeMillis();
            //System.out.println("查询所用时间 " + (t2 - t1) + " ms");
            sum_t = sum_t + (t2 - t1);

            //下面是解密的部分
            if (res == null) {
                continue;
            } else {
                List<String> inds = SearchProtocol_Split_memory.search_client_2(kws.get(0),res);
                System.out.println("搜索结果"+res.size());
                System.out.println("搜索的关键词："+kws.toString());
                System.out.println("搜索结果："+inds.toString());
            }
            System.out.println("===============");
        }
    }




    @Test
    public void searchProtocolTest13() throws Exception {

        ArrayList<String> table_names = new ArrayList<>();
        table_names.add("TSets_32768_rand_v2");
        table_names.add("TSets_65536_rand_v2");
        table_names.add("TSets_131072_rand_v2");
        table_names.add("TSets_262144_rand_v2");
        table_names.add("TSets_524288_rand_v2");
        table_names.add("TSets_1048576_rand_v2");
        for (String tableName : table_names) {
            long sum_t = 0;
            //进行100次查询
            for (int i = 0; i < 100; i++) {
                //要查询的关键字
                List<String> kws = new ArrayList<>();
                kws.add("clubnumber10");
                kws.add("clubnumber11");
                kws.add("clubnumber12");
                int numLeastKeyword = 9;

                //System.out.println("查询第一阶段 开始");
                Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, numLeastKeyword);
                //System.out.println("查询第二阶段 开始");
                long t1 = System.currentTimeMillis();
                ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, tableName);
                long t2 = System.currentTimeMillis();
                //System.out.println("查询所用时间 " + (t2 - t1) + " ms");
                sum_t = sum_t + (t2 - t1);

                //下面是解密的部分
                if (res == null) {
                    continue;
                }else {
                   // List<String> inds = SearchProtocol_Split_memory.search_client_2(kws.get(0),res);
                    //System.out.println("搜索结果"+res.size());
                   // System.out.println("搜索的关键词："+kws.toString());
                   // System.out.println("搜索结果："+inds.toString());
                }
            }
            System.out.println("表 " + tableName + " 花费时间：" + (sum_t / 100) + " ms");
        }
    }




}
