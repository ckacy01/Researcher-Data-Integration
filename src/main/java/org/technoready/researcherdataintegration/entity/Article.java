package org.technoready.researcherdataintegration.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * This class stores the articles attributes for sprint 3.
 * DATE: 07 - October - 2025
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 2.0
 */

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id
    @Column(name = "id", length = 255)
    private String id;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "authors", columnDefinition = "TEXT")
    private String authors;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "abstract", columnDefinition = "TEXT")
    private String abstractText;

    @Column(name = "link", columnDefinition = "TEXT")
    private String link;

    @Column(name = "keywords", columnDefinition = "TEXT")
    private String keywords;

    @Column(name = "cited_by")
    private Integer citedBy;

    @Column(name = "researcher_name", length = 255)
    private String researcherName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
