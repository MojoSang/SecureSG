package com.zhong;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class T_Sets_split_memoryTest {

    @Test
    public void savetset() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        long t1=System.currentTimeMillis();
        T_Sets_split_memory.TSetSetup();
        long t2=System.currentTimeMillis();
        System.out.println("用时:"+(t2-t1)+"ms");
    }

    @Test
    public void savetsetBS() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        long t1=System.currentTimeMillis();
        T_Sets_split_memory.TSetSetupBS();
        long t2=System.currentTimeMillis();
        System.out.println("用时:"+(t2-t1)+"ms");
    }
}
