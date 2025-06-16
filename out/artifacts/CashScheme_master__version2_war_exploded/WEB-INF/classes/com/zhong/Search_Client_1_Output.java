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
     * 搜索的令牌
     */
    Element[][] xtoken;

    public Search_Client_1_Output(byte[] stag, Element[][] xtoken) {
        this.stag = stag;
        this.xtoken = xtoken;
    }

    public byte[] getStag() {
        return stag;
    }

    public Element[][] getXtoken() {
        return xtoken;
    }
}
