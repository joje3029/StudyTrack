package com.studytrack.studytrackbackend.service;

import com.studytrack.studytrackbackend.domain.User;
import com.studytrack.studytrackbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * 이메일로 사용자 조회
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findActiveUserByEmail(email);
    }
    
    /**
     * 소셜 로그인 사용자 조회
     */
    public Optional<User> findBySocialLogin(String provider, String socialId) {
        return userRepository.findActiveUserByProviderAndSocialId(provider, socialId);
    }
    
    /**
     * 이메일 중복 확인
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * 닉네임 중복 확인
     */
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
    
    /**
     * 자체 회원가입
     */
    @Transactional
    public User registerUser(String email, String password, String nickname) {
        // 중복 확인
        if (existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);
        
        // 사용자 생성 및 저장
        User user = new User(email, encodedPassword, nickname);
        return userRepository.save(user);
    }
    
    /**
     * 소셜 회원가입/로그인
     */
    @Transactional
    public User registerOrLoginSocialUser(String email, String nickname, String provider, String socialId) {
        // 기존 소셜 사용자 확인
        Optional<User> existingUser = findBySocialLogin(provider, socialId);
        if (existingUser.isPresent()) {
            // 로그인 시간 업데이트
            User user = existingUser.get();
            user.setLastLoginAt(LocalDateTime.now());
            return userRepository.save(user);
        }
        
        // 신규 소셜 사용자 생성
        User newUser = new User(email, nickname, provider, socialId);
        newUser.setLastLoginAt(LocalDateTime.now());
        return userRepository.save(newUser);
    }
    
    /**
     * 비밀번호 검증
     */
    public boolean validatePassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
    
    /**
     * 로그인 시간 업데이트
     */
    @Transactional
    public void updateLastLoginTime(UUID userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }
    
    /**
     * 사용자 정보 수정
     */
    @Transactional
    public User updateUser(UUID userId, String nickname, String profileImageUrl) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        if (nickname != null && !nickname.equals(user.getNickname())) {
            if (existsByNickname(nickname)) {
                throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
            }
            user.setNickname(nickname);
        }
        
        if (profileImageUrl != null) {
            user.setProfileImageUrl(profileImageUrl);
        }
        
        return userRepository.save(user);
    }
    
    /**
     * 사용자 비활성화 (소프트 삭제)
     */
    @Transactional
    public void deactivateUser(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        user.setStatus("inactive");
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
