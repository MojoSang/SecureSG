package com.zhong;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 包括一个密文 和 一个元素
 */
public class t_item implements Serializable {
    /**
     * 密文
     */
    byte [] e;
    /**
     * 明文
     */
    String ind;
    /**
     * 一个元素，相当于是一个索引
     */
    SerializableElement y;


    int weig;

    public t_item(byte[] e, SerializableElement y) {
        this.e = e;
        this.y = y;
    }

    //重载新构造方法
    public t_item(byte[] e,SerializableElement y,int weig){
        this.e=e;
        this.y=y;
        this.weig=weig;
    }

    public t_item(String ind,SerializableElement y,int weig){
        this.ind=ind;
        this.y=y;
        this.weig=weig;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("e: " + Arrays.toString(e));
        sb.append("y: " + y.getElement().toString());
        return sb.toString();
    }

    public String String() {
        StringBuilder sb = new StringBuilder();
        sb.append("e: " + Arrays.toString(e));
        sb.append("y: " + y.getElement().toString());
        return sb.toString();
    }
    public byte[] getE() {
        return e;
    }

    public SerializableElement getY() {
        return y;
    }

    public int getWeig(){
        return weig;
    }

    public String getInd(){
        return ind;
    }
}