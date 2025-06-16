package com.zhong.concurrent;

import com.zhong.t_item;
import com.zhong.x_item;

import java.util.ArrayList;

/**
 * 我们将Task作为生产和消费的单位：
 *
 * @author 张中俊
 **/
public class Task {
    /**
     * 关键词映射的结果
     */
    private String stag;
    /**
     * 密文   +  t-set索引
     */
    private ArrayList<t_item> t;
    /**
     * 该关键词对应的所有的XSet
     */
    private ArrayList<String> XSets;

    private x_item x;


    public Task(String stag, ArrayList<t_item> t, ArrayList<String> XSets) {
        this.stag = stag;
        this.t = t;
        this.XSets = XSets;
    }

    public Task(String stag, ArrayList<t_item> t, ArrayList<String> XSets, x_item x) {
        this.stag = stag;
        this.t = t;
        this.XSets = XSets;
        this.x = x;
    }

    public String getStag() {
        return stag;
    }

    public ArrayList<t_item> getT() {
        return t;
    }

    public ArrayList<String> getXSets() {
        return XSets;
    }

    public x_item getX() {
        return x;
    }

    @Override
    public String toString() {
        return "";
    }
}
