package com.axin.communication.domain;

/**
 * 辅助映射数据结构
 * code:index
 *
 * @author Axin
 * @date 18-10-19
 */
public class CodeMap {
    private int code;
    private int index;

    public CodeMap(int code, int index) {
        this.code = code;
        this.index = index;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
