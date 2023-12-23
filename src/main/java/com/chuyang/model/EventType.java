package com.chuyang.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChuYang
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum EventType {

    add("add", "增量"),
    finish("finish", "结束"),
    error("error", "错误"),
    interrupted("interrupted", "中断");
    private final String type;
    private final String info;
}
