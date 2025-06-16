package com.zhong.concurrent;
/**
 * 消费者的接口
 *
 */
public interface Consumer {
    void consume() throws InterruptedException;
}
