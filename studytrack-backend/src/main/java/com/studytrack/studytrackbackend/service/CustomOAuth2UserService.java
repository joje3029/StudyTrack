package com.studytrack.studytrackbackend.service;

import com.studytrack.studytrackbackend.domain.User;
import com.studytrack.studytrackbackend.repository.UserRepository;
import com.studytrack.studytrackbackend.security.GoogleUserInfo;
import com.studytrack.studytrackbackend.security.KakaoUserInfo;
import com.studytrack.studytrackbackend.security.NaverUserInfo;
import com.studytrack.studytrackbackend.security.OAuth2UserInfo;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2UserInfo userInfo = getOAuth2UserInfo(registrationId, attributes);

        Optional<User> userOptional = userRepository.findByProviderAndSocialId(registrationId, userInfo.getId());
        User user;

        if (userOptional.isEmpty()) {
            user = createUser(userInfo, registrationId);
        } else {
            user = userOptional.get();
            updateUser(user, userInfo);
        }

        return new DefaultOAuth2User(
                Collections.emptyList(), // roles
                attributes,
                userRequest.getClientRegistration().getProviderDetails()
                        .getUserInfoEndpoint().getUserNameAttributeName()
        );
    }

    private OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleUserInfo(attributes);
            case "naver" -> new NaverUserInfo(attributes);
            case "kakao" -> new KakaoUserInfo(attributes);
            default -> throw new IllegalArgumentException("Unknown registrationId: " + registrationId);
        };
    }

    private User createUser(OAuth2UserInfo userInfo, String registrationId) {
        User user = new User(
                userInfo.getEmail(),
                userInfo.getNickname(),
                registrationId,
                userInfo.getId()
        );
        return userRepository.save(user);
    }

    private void updateUser(User user, OAuth2UserInfo userInfo) {
        user.setNickname(userInfo.getNickname());
        // 필요하다면 다른 정보도 업데이트
    }
}
