package com.chuyang.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChuYang
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public enum Role {

    user("user"),

    assistant("assistant");
    private final String role;
}


