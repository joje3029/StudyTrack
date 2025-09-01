package com.studytrack.studytrackbackend.security;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected final Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();
    public abstract String getNickname();
    public abstract String getEmail();
}
