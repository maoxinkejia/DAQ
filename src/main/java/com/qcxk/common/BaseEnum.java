package com.qcxk.common;

public interface BaseEnum<E extends Enum<?>, T> {
    T getValue();
}
