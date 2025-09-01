package com.studytrack.studytrackbackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String nickname;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    @Column(nullable = false)
    private String provider;
    
    @Column(nullable = false)
    private String providerId;
    
    // 기본 생성자
    protected User() {}
    
    // 생성자
    public User(String email, String nickname, Role role, String provider, String providerId) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
    
    // Getter 메서드들
    public Long getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public Role getRole() {
        return role;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public String getProviderId() {
        return providerId;
    }
    
    // Setter 메서드들 (nickname은 변경 가능)
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
