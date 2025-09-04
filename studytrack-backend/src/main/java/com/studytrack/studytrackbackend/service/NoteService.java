package com.studytrack.studytrackbackend.service;

import com.studytrack.studytrackbackend.domain.Note;
import com.studytrack.studytrackbackend.domain.Subject;
import com.studytrack.studytrackbackend.domain.User;
import com.studytrack.studytrackbackend.repository.NoteRepository;
import com.studytrack.studytrackbackend.repository.SubjectRepository;
import com.studytrack.studytrackbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class NoteService {
    
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    
    @Autowired
    public NoteService(NoteRepository noteRepository, UserRepository userRepository, SubjectRepository subjectRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }
    
    /**
     * 사용자별 노트 목록 조회
     */
    public List<Note> findByUserId(UUID userId) {
        return noteRepository.findByUserIdOrderByUpdatedAtDesc(userId);
    }
    
    /**
     * 사용자별 노트 목록 조회 (페이징)
     */
    public Page<Note> findByUserId(UUID userId, Pageable pageable) {
        return noteRepository.findByUserIdOrderByUpdatedAtDesc(userId, pageable);
    }
    
    /**
     * 학습 분야별 노트 목록 조회
     */
    public List<Note> findBySubjectId(UUID subjectId) {
        return noteRepository.findBySubjectIdOrderByUpdatedAtDesc(subjectId);
    }
    
    /**
     * 노트 조회 (본인 소유만)
     */
    public Optional<Note> findByIdAndUserId(UUID noteId, UUID userId) {
        Optional<Note> note = noteRepository.findByIdAndUserId(noteId, userId);
        
        // 조회수 증가
        if (note.isPresent()) {
            incrementViewCount(noteId, userId);
        }
        
        return note;
    }
    
    /**
     * 노트 생성
     */
    @Transactional
    public Note createNote(UUID userId, UUID subjectId, String title, String content) {
        // 사용자 확인
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        // 학습 분야 확인
        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new IllegalArgumentException("학습 분야를 찾을 수 없습니다."));
        
        // 권한 확인 (본인 소유 분야인지)
        if (!subject.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 학습 분야에 대한 권한이 없습니다.");
        }
        
        // 노트 생성
        Note note = new Note(user, subject, title, content);
        return noteRepository.save(note);
    }
    
    /**
     * 노트 수정
     */
    @Transactional
    public Note updateNote(UUID noteId, UUID userId, String title, String content, String contentType) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
            .orElseThrow(() -> new IllegalArgumentException("노트를 찾을 수 없거나 권한이 없습니다."));
        
        if (title != null) {
            note.setTitle(title);
        }
        
        if (content != null) {
            note.setContent(content);
        }
        
        if (contentType != null) {
            note.setContentType(contentType);
        }
        
        return noteRepository.save(note);
    }
    
    /**
     * 노트 삭제
     */
    @Transactional
    public void deleteNote(UUID noteId, UUID userId) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
            .orElseThrow(() -> new IllegalArgumentException("노트를 찾을 수 없거나 권한이 없습니다."));
        
        noteRepository.delete(note);
    }
    
    /**
     * 즐겨찾기 토글
     */
    @Transactional
    public Note toggleFavorite(UUID noteId, UUID userId) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
            .orElseThrow(() -> new IllegalArgumentException("노트를 찾을 수 없거나 권한이 없습니다."));
        
        note.setIsFavorite(!note.getIsFavorite());
        return noteRepository.save(note);
    }
    
    /**
     * 즐겨찾기 노트 목록 조회
     */
    public List<Note> findFavoriteNotes(UUID userId) {
        return noteRepository.findByUserIdAndIsFavoriteTrue(userId);
    }
    
    /**
     * 노트 검색
     */
    public List<Note> searchNotes(UUID userId, String keyword) {
        return noteRepository.searchByUserIdAndKeyword(userId, keyword);
    }
    
    /**
     * 조회수 증가
     */
    @Transactional
    public void incrementViewCount(UUID noteId, UUID userId) {
        Optional<Note> noteOpt = noteRepository.findByIdAndUserId(noteId, userId);
        if (noteOpt.isPresent()) {
            Note note = noteOpt.get();
            note.incrementViewCount();
            noteRepository.save(note);
        }
    }
    
    /**
     * 사용자별 노트 개수 조회
     */
    public long countByUserId(UUID userId) {
        return noteRepository.countByUserId(userId);
    }
    
    /**
     * 학습 분야별 노트 개수 조회
     */
    public long countBySubjectId(UUID subjectId) {
        return noteRepository.countBySubjectId(subjectId);
    }
}
