package com.dino.plExplorer.entity;

import com.dino.plExplorer.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "articles")
public class Article extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String headline;

    @Column(nullable = false, columnDefinition = "text")
    private String body;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "espn_id")
    private Long espnId;

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false)
    private LocalDateTime dateCreated;
}

