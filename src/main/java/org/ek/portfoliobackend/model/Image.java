package org.ek.portfoliobackend.model;


import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter


@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    private boolean isFeatured;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;



}
