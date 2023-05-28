package com.global.language.web_content_translate.model.param;

import java.util.StringJoiner;

public class DeleteParam {
    Integer Id;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DeleteParam.class.getSimpleName() + "[", "]")
                .add("Id=" + Id)
                .toString();
    }
}
