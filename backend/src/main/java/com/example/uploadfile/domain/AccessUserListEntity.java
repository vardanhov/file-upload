package com.example.uploadfile.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_user_list")
@Data
@NoArgsConstructor
public class AccessUserListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Basic
    @Column(name = "date_from")
    private LocalDateTime dateFrom;
    @Basic
    @Column(name = "date_to")
    private LocalDateTime dateTo;
    @Basic
    @Column(name = "is_native")
    private Boolean isNative;

    @Column(name = "role")
    private String role;

    @Basic
    @Column(name = "added_by")
    private String addedBy;

    @Basic
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ParticipantEntity participantEntity;
}
