package com.studytrack.studytrackbackend.service;

import com.studytrack.studytrackbackend.domain.Subject;
import com.studytrack.studytrackbackend.domain.User;
import com.studytrack.studytrackbackend.repository.SubjectRepository;
import com.studytrack.studytrackbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SubjectService {
    
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public SubjectService(SubjectRepository subjectRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * 사용자별 학습 분야 목록 조회
     */
    public List<Subject> findByUserId(UUID userId) {
        return subjectRepository.findByUserIdOrderBySortOrderAsc(userId);
    }
    
    /**
     * 학습 분야 조회 (본인 소유만)
     */
    public Optional<Subject> findByIdAndUserId(UUID subjectId, UUID userId) {
        Optional<Subject> subject = subjectRepository.findById(subjectId);
        if (subject.isPresent() && subject.get().getUser().getId().equals(userId)) {
            return subject;
        }
        return Optional.empty();
    }
    
    /**
     * 학습 분야 생성
     */
    @Transactional
    public Subject createSubject(UUID userId, String name, String description, String color) {
        // 사용자 확인
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        // 중복 확인
        if (subjectRepository.existsByUserIdAndName(userId, name)) {
            throw new IllegalArgumentException("이미 존재하는 분야명입니다.");
        }
        
        // 학습 분야 생성
        Subject subject = new Subject(user, name, description, color);
        return subjectRepository.save(subject);
    }
    
    /**
     * 학습 분야 수정
     */
    @Transactional
    public Subject updateSubject(UUID subjectId, UUID userId, String name, String description, String color) {
        // 권한 확인
        Subject subject = findByIdAndUserId(subjectId, userId)
            .orElseThrow(() -> new IllegalArgumentException("학습 분야를 찾을 수 없거나 권한이 없습니다."));
        
        // 이름 중복 확인 (기존 이름과 다를 때만)
        if (name != null && !name.equals(subject.getName())) {
            if (subjectRepository.existsByUserIdAndName(userId, name)) {
                throw new IllegalArgumentException("이미 존재하는 분야명입니다.");
            }
            subject.setName(name);
        }
        
        if (description != null) {
            subject.setDescription(description);
        }
        
        if (color != null) {
            subject.setColor(color);
        }
        
        return subjectRepository.save(subject);
    }
    
    /**
     * 학습 분야 삭제
     */
    @Transactional
    public void deleteSubject(UUID subjectId, UUID userId) {
        Subject subject = findByIdAndUserId(subjectId, userId)
            .orElseThrow(() -> new IllegalArgumentException("학습 분야를 찾을 수 없거나 권한이 없습니다."));
        
        subjectRepository.delete(subject);
    }
    
    /**
     * 학습 분야 검색
     */
    public List<Subject> searchSubjects(UUID userId, String keyword) {
        return subjectRepository.findByUserIdAndNameContaining(userId, keyword);
    }
    
    /**
     * 사용자별 학습 분야 개수 조회
     */
    public long countByUserId(UUID userId) {
        return subjectRepository.countByUserId(userId);
    }
    
    /**
     * 정렬 순서 업데이트
     */
    @Transactional
    public Subject updateSortOrder(UUID subjectId, UUID userId, Integer sortOrder) {
        Subject subject = findByIdAndUserId(subjectId, userId)
            .orElseThrow(() -> new IllegalArgumentException("학습 분야를 찾을 수 없거나 권한이 없습니다."));
        
        subject.setSortOrder(sortOrder);
        return subjectRepository.save(subject);
    }
}
