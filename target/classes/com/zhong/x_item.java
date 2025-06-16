package com.zhong;

import java.io.Serializable;

/**
 * @author 赵子豪
 * @create 2023-3-26 14:38
 **/
public class x_item implements Serializable {
    /**
     * 对应的主文件
     */
    String w;
    /**
     * 序列值，与t_item匹配
     */
    SerializableElement y;
    /**
     * 对应的权值
     */
    int weig;


    public x_item(String w,SerializableElement y, int weig) {
        this.w = w;
        this.y=y;
        this.weig = weig;
    }

    @Override
    public String toString() {
        return "x_item{" +
                "w='" + w + '\'' +
                ", weig=" + weig +
                '}';
    }

    public String getW() {
        return w;
    }

    public int getWeig() {
        return weig;
    }

}
