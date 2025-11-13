package com.hng.notification_dispatcher.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class UserData {
    @NotBlank
    private String name;

    @NotBlank
    private String link;

    private Map<String, Object> meta;


    public UserData() {}

    public UserData(String name, String link, Map<String, Object> meta) {
        this.name = name;
        this.link = link;
        this.meta = meta;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }
}
