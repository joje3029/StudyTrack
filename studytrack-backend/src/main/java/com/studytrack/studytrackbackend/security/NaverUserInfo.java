package com.studytrack.studytrackbackend.security;

import java.util.Map;

@SuppressWarnings("unchecked")
public class NaverUserInfo extends OAuth2UserInfo {

    private final Map<String, Object> response;

    public NaverUserInfo(Map<String, Object> attributes) {
        super(attributes);
        this.response = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getId() {
        return (String) response.get("id");
    }

    @Override
    public String getNickname() {
        return (String) response.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) response.get("email");
    }
}
