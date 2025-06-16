package com.zhong;

import java.io.Serializable;

public class record implements Serializable {
    byte[] Label;
    byte[] value;

    public record(byte[] label, byte[] value) {
        Label = label;
        this.value = value;
    }

    public byte[] getLabel() {
        return Label;
    }

    public void setLabel(byte[] label) {
        Label = label;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

}
