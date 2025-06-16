package com.zhong.rm;

import bloomfilter.mutable.BloomFilter;
import com.sun.javaws.IconUtil;
import com.zhong.concurrent.Task;
import com.zhong.record;
import com.zhong.t_item;
import com.zhong.utils.MyUtils;
import com.zhong.utils.SerializationDemonstrator;
import com.zhong.x_item;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.*;


public class MysqlUtils {
    private static Connection conn;                                      //连接
    private static PreparedStatement pres;                              //PreparedStatement对象

    private static ArrayList<BloomFilter<byte[]>> bflist; //bloomfilter组对象

   private static BloomFilter<byte[]> bf;
    static {
        File file = MyUtils.getFile("tsets_bloom_filter","xset_bf_vsse.bf");
        bf = SerializationDemonstrator.deserialize(file.getAbsolutePath());

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");              //加载驱动
            System.out.println("数据库加载成功!!!");
            String url = "jdbc:mysql://localhost:3306/vsse";
            //String url = "jdbc:mysql://127.0.0.1:3306/vsse?serverTimezone=UTC";
            String user = "root";
            String password = "942694426";

            conn = DriverManager.getConnection(url, user, password); //建立连接
            System.out.println("数据库连接成功!!!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 二次加密存储
     * @param TSets
     */
    public static void saveTSet(HashMap TSets){
        String sql_TSet="insert into tset(stag,record) values(?,?)";
        int m=0;
        Set<Map.Entry<byte[], ArrayList<record>>> entries = TSets.entrySet();
        try {
            pres=conn.prepareStatement(sql_TSet);
            for (Map.Entry<byte[], ArrayList<record>> entry : entries) {
                m++;
                pres.setString(1,Arrays.toString(entry.getKey()));
                byte[] temp=MyUtils.msg2Byte(entry.getValue());
                if(temp.length>16*1024*1024){
                    if (pres != null)
                        pres.close();
                    return ;
                }
                pres.setBytes(2,temp);
                pres.execute();
                if(m%10000==0)
                    System.out.println("已存储"+m+"条记录");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void saveTSetBS(HashMap TSets){
        String sql_TSet="insert into tsetbs(w,record) values(?,?)";
        Set<Map.Entry<String, ArrayList<record>>> entries = TSets.entrySet();
        try {
            pres=conn.prepareStatement(sql_TSet);
            for (Map.Entry<String, ArrayList<record>> entry : entries) {
                pres.setString(1,entry.getKey());
                byte[] temp=MyUtils.msg2Byte(entry.getValue());
                if(temp.length>16*1024*1024){
                    if (pres != null)
                        pres.close();
                    return ;
                }
                pres.setBytes(2,temp);
                pres.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }




    /**
     * 向数据库中的表TSets中插入数据
     *
     * @param task
     *         要插入的数据
     */
    public static void saveTask(Task task) {
        String sql_TSets = "insert into tsets(stag,t) values(?,?)";
        String sql_XSets = "insert into xsets(xSet,x) values(?,?)";
        try {
            //开始保存XSet
            pres = conn.prepareStatement(sql_XSets);
            for (int i = 0; i < task.getXSets().size(); i++) {
                pres.setString(1, task.getXSets().get(i));
                //对应一个xsets的一个x
                x_item x=task.getX().get(i);
                byte[] xmsg=MyUtils.msg2Byte(x);
                pres.setBytes(2,xmsg);
                pres.addBatch(); //实现批量插入
            }
            pres.executeBatch();//批量插入到数据库中

            //开始保存TSets
            pres = conn.prepareStatement(sql_TSets);
            pres.setString(1, task.getStag());
            //t对象转换成字节数组
            byte[] temp = MyUtils.msg2Byte(task.getT());
            if(temp.length>16*1024*1024){
                if (pres != null)
                    pres.close();
                System.out.println("t太大了,t里面有 "+task.getT().size()+" 个t_item");
                return ;
            }
            pres.setBytes(2, temp);
            try {
                pres.execute();
            }catch(Exception e){
                if (pres != null)
                    pres.close();
                System.out.println("插入表TSets时发生异常 " + e);
                return;
            }
            if (pres != null)
                pres.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 基线系统向数据库中的表TSets中插入数据
     *
     * @param task
     *         要插入的数据
     */
    public static void saveTaskBS(Task task) {
        String sql_TSets = "insert into tsetsbs(w,t) values(?,?)";
        String sql_XSets = "insert into xsetbs(xSet,x) values(?,?)";
        try {
            //开始保存XSet
            pres = conn.prepareStatement(sql_XSets);
            for (int i = 0; i < task.getXSets().size(); i++) {
                pres.setString(1, task.getXSets().get(i));
                //对应一个xsets的一个x
                x_item x=task.getX().get(i);
                byte[] xmsg=MyUtils.msg2Byte(x);
                pres.setBytes(2,xmsg);
                pres.addBatch(); //实现批量插入
            }
            pres.executeBatch();//批量插入到数据库中

            //开始保存TSets
            pres = conn.prepareStatement(sql_TSets);
            pres.setString(1, task.getStag());
            //t对象转换成字节数组
            byte[] temp = MyUtils.msg2Byte(task.getT());
            if(temp.length>16*1024*1024){
                if (pres != null)
                    pres.close();
                System.out.println("t太大了,t里面有 "+task.getT().size()+" 个t_item");
                return ;
            }
            pres.setBytes(2, temp);
            try {
                pres.execute();
            }catch(Exception e){
                if (pres != null)
                    pres.close();
                System.out.println(task.getStag()+"插入表TSets时发生异常 " + e);
                return;
            }
            if (pres != null)
                pres.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向指定的表中插入Task
     * @param task 要插入的数据
     * @param tableName 要插入的表
     * 备份表的插入数据
     */
    public static void saveTask(Task task, String tableName) {
        String sql_TSets = "insert into "+tableName+"(stag,t) values(?,?)";
        String sql_XSets = "insert into XSets(xSet) values(?)";
        try {
            //开始保存XSet
            pres = conn.prepareStatement(sql_XSets);
            for (int i = 0; i < task.getXSets().size(); i++) {
                pres.setString(1, task.getXSets().get(i));
                pres.addBatch(); //实现批量插入
            }
            pres.executeBatch();//批量插入到数据库中

            //开始保存TSets
            pres = conn.prepareStatement(sql_TSets);
            pres.setString(1, task.getStag());
            byte[] temp = MyUtils.msg2Byte(task.getT());
            if(temp.length>65*1024){
                if (pres != null)
                    pres.close();
                System.out.println("t太大了,t里面有 "+task.getT().size()+" 个t_item");
                return ;
            }
            pres.setBytes(2, temp);
            try {
                pres.execute();
            }catch(Exception e){
                if (pres != null)
                    pres.close();
                System.out.println("插入表TSets时发生异常 " + e);
                return;
            }
            if (pres != null)
                pres.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库中读出存入的对象
     * @return 得到所有的Task
     */
    public static ArrayList<Task> getAllTask() {
        ArrayList<Task> tasks = new ArrayList<>();
        int cnt=0;
        String sql_TSets = "select * from tsets";
        try {
            pres = conn.prepareStatement(sql_TSets);

            ResultSet res = pres.executeQuery();
            while (res.next()) {
                cnt++;
                String stag = res.getString(1);
                byte[] b2 = res.getBytes(2);
                ArrayList<t_item> t = (ArrayList<t_item>) MyUtils.byte2Msg(b2);
                Task task = new Task(stag, t, null);
                tasks.add(task);
                if(cnt%10000==0) System.out.println("已处理"+cnt+"条数据");
            }

            if (pres != null)
                pres.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }


    public static ArrayList<Task> getAllTaskBS() {
        ArrayList<Task> tasks = new ArrayList<>();

        int m=0;
        String sql_TSets = "select * from tsetsbs";
        try {
            pres = conn.prepareStatement(sql_TSets);

            ResultSet res = pres.executeQuery();
            while (res.next()) {
                m++;
                String stag = res.getString(1);
                byte[] b2 = res.getBytes(2);
                ArrayList<t_item> t = (ArrayList<t_item>) MyUtils.byte2Msg(b2);
                Task task = new Task(stag, t, null);
                tasks.add(task);
                if(m%10000==0)
                    System.out.println("已获取"+m+"条记录");
            }

            if (pres != null)
                pres.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * 通过指定的stag查询
     *
     * @param key 指定的stag
     * @return 该stag对应的task
     */
    public static Task getTask(String key){
        Task task = null;
        String sql_TSets = "select * from TSets WHERE stag=(?)";
        try {
            pres = conn.prepareStatement(sql_TSets);
            pres.setString(1,key);
            ResultSet res = pres.executeQuery();

            while (res.next()) {
                String stag = res.getString(1);
                byte[] b2 = res.getBytes(2);
                ArrayList<t_item> t = (ArrayList<t_item>) MyUtils.byte2Msg(b2);
                task = new Task(stag, t, null);
            }

            if (pres != null)
                pres.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    /**
     * 通过指定的stag 在特定的表中查询
     *
     * @param key 指定的stag
     * @param table_name 指定的表名
     * @return 该stag对应的task
     */
    public static Task getTask(String key, String table_name){
        Task task = null;
        String sql_TSets = "select * from "+table_name+" WHERE stag=? limit 1";
        try {
            pres = conn.prepareStatement(sql_TSets);
            pres.setString(1,key);
            ResultSet res = pres.executeQuery();
            while (res.next()) {
                String stag = res.getString(1);
                byte[] b2 = res.getBytes(2);
                ArrayList<t_item> t = (ArrayList<t_item>) MyUtils.byte2Msg(b2);
                task = new Task(stag, t, null);
            }

            if (pres != null)
                pres.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    /**
     * 获取record二次加密内容
     * @param key
     * @param table_name
     * @return
     */
    public static ArrayList<record> getTSet(String key, String table_name){
        ArrayList<record> records=new ArrayList<>();
        String sql_TSets = "select record from "+table_name+" WHERE stag=? limit 1";
        try {
            pres=conn.prepareStatement(sql_TSets);
            pres.setString(1,key);
            ResultSet rs=pres.executeQuery();
            while (rs.next()){
                records= (ArrayList<record>) MyUtils.byte2Msg(rs.getBytes(1));
            }
            if(pres!=null)
                pres.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return records;
    }


    public static ArrayList<record> getTSetBS(String key, String table_name){
        ArrayList<record> records=new ArrayList<>();
        String sql_TSets = "select record from "+table_name+" WHERE w=? limit 1";
        try {
            pres=conn.prepareStatement(sql_TSets);
            pres.setString(1,key);
            ResultSet rs=pres.executeQuery();
            while (rs.next()){
                records= (ArrayList<record>) MyUtils.byte2Msg(rs.getBytes(1));
            }
            if(pres!=null)
                pres.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return records;
    }



    /**
     * 使用bloom filter测试xset是否在表XSet中
     *
     * @param key
     *         要测试的数据库
     *
     * @return key是否包含在bloom filter中
     *
     * @throws UnsupportedEncodingException
     *         异常
     */
    public static boolean isInXSets_v2(String key) throws UnsupportedEncodingException {
        return bf.mightContain(key.getBytes("utf-8"));
    }


    public static boolean isInXSets_v3(String key) throws UnsupportedEncodingException {
        for (BloomFilter<byte[]> bf : bflist) {
            if(bf.mightContain(key.getBytes("utf-8")))
                return true;
        }
        return false;
    }

    /**
     * 通过查询数据表判断key是否在XSets中
     *
     * @param key
     *         要查询的关键词
     *
     * @return 是否在表Xsets中
     */
    public static boolean isInXSets(String key){
        Task task = null;
        String sql_XSets = "select count(*) from xsets WHERE xSet=(?) limit 1 ";
        try {
            pres = conn.prepareStatement(sql_XSets);
            pres.setString(1,key);

            ResultSet res = pres.executeQuery();
            res.next();
            int count = res.getInt(1);

            if (pres != null)
                pres.close();
            System.out.println("count:"+count);
            if(count==0){
                return false;
            }else{
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获得表Xsets中的内容
     *
     * @param offset
     *         偏移量
     * @param row_count
     *         要获得的行数
     *
     * @return Xsets中的内容
     */
    public ArrayList<String> getXSets(int offset,int row_count){
        ArrayList<String> xSets = new ArrayList<>();
        Task task = null;
        String sql_XSets = "select * from xsetbs limit "+offset+","+row_count;
        try {
            pres = conn.prepareStatement(sql_XSets);

            ResultSet res = pres.executeQuery();
            while (res.next()) {
                String xSet = res.getString(1);
                xSets.add(xSet);
            }
            if (pres != null)
                pres.close();
            return xSets;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得表Xsets中的内容
     *
     * @param offset
     *         偏移量
     * @param row_count
     *         要获得的行数
     *
     * @return Xsets中的内容
     */
    public HashMap<String, x_item> getXSets_v2(int offset,int row_count){
        HashMap<String, x_item> xSets=new HashMap<>();
        String sql_XSets = "select * from XSets limit "+offset+","+row_count;
        MyUtils mu=new MyUtils();
        try {
            pres = conn.prepareStatement(sql_XSets);

            ResultSet res = pres.executeQuery();
            while (res.next()) {
                String xSet = res.getString(1);
                byte[] xx=res.getBytes(2);
                x_item x=(x_item)mu.byte2Msg(xx);
                xSets.put(xSet,x);
            }
            if (pres != null)
                pres.close();
            return xSets;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取<w,weig>
    public static byte[] getXsetsWW(String key) {
        byte[] x=null;
        String sql_xsets = "select x from xsets where xsets.xset=(?) limit 1";
        try {
            pres=conn.prepareStatement(sql_xsets);
            pres.setString(1,key);
            ResultSet res=pres.executeQuery();
            res.next();
            x=res.getBytes(1);

            if (pres!=null)
                pres.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return x;
    }


}

