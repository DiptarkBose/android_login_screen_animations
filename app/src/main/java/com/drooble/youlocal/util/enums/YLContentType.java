package com.drooble.youlocal.util.enums;

/**
 * Created by Vanya Mihova on 1/27/2016.
 */
public enum YLContentType {
    URL_ENCODED("application/x-www-form-urlencoded; charset=UTF-8"),
    JSON("application/json");

    private String type;

    YLContentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
