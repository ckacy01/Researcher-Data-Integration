package org.technoready.researcherdataintegration.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * This class stores the articles attributes for sprint 3.
 * DATE: 06 - October - 2025
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "authors")
    private String authors;

    @Column (name = "publication_date")
    private Date publication_date;

    @Column (name = "abstract")
    private String _abstract;

    @Column (name = "link")
    private String link;

    @Column (name = "keywords")
    private String keywords;

    @Column (name = "cited_by")
    private long cited_by;

}
