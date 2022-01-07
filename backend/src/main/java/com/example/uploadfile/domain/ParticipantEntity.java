package com.example.uploadfile.domain;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "participant")
@NoArgsConstructor
@RequiredArgsConstructor
public class ParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "full_name")
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private UserGroups userGroups;

    public Long getId() {
        return this.id;
    }

    public @NonNull String getUsername() {
        return this.username;
    }

    public String getFullName() {
        return this.fullName;
    }

    public UserGroups getUserGroups() {
        return this.userGroups;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserGroups(UserGroups userGroups) {
        this.userGroups = userGroups;
    }

    public String toString() {
        return "ParticipantEntity(id=" + this.getId() + ", username=" + this.getUsername() + ", fullName=" + this.getFullName() + ")";
    }
}
