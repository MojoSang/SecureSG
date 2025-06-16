package com.zhong;

import org.junit.Test;


public class EDBSetup_keyUtilsTest {
    @Test
    public void getKeyTest() {
        MK mk = EDBSetup_keyUtils.getKey();
        System.out.println(mk);
    }

    @Test
    public void getFreshKeyTest(){
        MK mk=EDBSetup_keyUtils.getFreshKey();
        System.out.println(mk);
    }
}
