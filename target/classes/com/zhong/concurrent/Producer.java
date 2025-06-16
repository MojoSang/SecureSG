package com.zhong.concurrent;
/**
 * 生产者的接口
 *
 */
public interface Producer {
    void produce() throws InterruptedException;
}
