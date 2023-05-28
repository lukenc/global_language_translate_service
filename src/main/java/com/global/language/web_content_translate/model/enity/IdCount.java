package com.global.language.web_content_translate.model.enity;

import java.util.StringJoiner;

public class IdCount {
    private Integer id;
    private Integer count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IdCount.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("count=" + count)
                .toString();
    }
}
