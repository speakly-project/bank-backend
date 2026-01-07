package com.speakly.bank_backend.persistence.dao.impl.entity;

import es.speakly.store_backend.domain.model.UserRole;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserJpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private String encryptedPassword;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @ManyToMany
    @JoinTable(
        name = "user_courses",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<CourseJpaEntity> coursesTaken = new ArrayList<>();

    public UserJpaEntity() {}

    public UserJpaEntity(Long id, String username, String email, String profilePictureUrl, String encryptedPassword, LocalDateTime createdAt, List<CourseJpaEntity> coursesTaken, UserRole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.encryptedPassword = encryptedPassword;
        this.createdAt = createdAt;
        if (coursesTaken != null) {
            this.coursesTaken.addAll(coursesTaken);
        }
        this.role = role;
    }

    public List<CourseJpaEntity> getCoursesTaken() {
        return coursesTaken;
    }

    public void setCourses(List<CourseJpaEntity> coursesTaken) {
        this.coursesTaken.clear();
        if (coursesTaken != null) {
            this.coursesTaken.addAll(coursesTaken);
        }
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
    public String getEncryptedPassword() {
        return encryptedPassword;
    }
    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}