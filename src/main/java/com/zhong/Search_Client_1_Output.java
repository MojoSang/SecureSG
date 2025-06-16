package com.zhong;

import it.unisa.dia.gas.jpbc.Element;

/**
 * 客户端在第1阶段的输出
 */
public class Search_Client_1_Output {
    /**
     * w1 的stag
     */
    byte[] stag;

    /**
     * 基线系统
     */
    String w;
    /**
     * 搜索的令牌
     */
    Element[][] xtoken;

    public Search_Client_1_Output(byte[] stag, Element[][] xtoken) {
        this.stag = stag;
        this.xtoken = xtoken;
    }

    public Search_Client_1_Output(String w, Element[][] xtoken) {
        this.w = w;
        this.xtoken = xtoken;
    }

    public String getW() {
        return w;
    }

    public byte[] getStag() {
        return stag;
    }

    public Element[][] getXtoken() {
        return xtoken;
    }
}
