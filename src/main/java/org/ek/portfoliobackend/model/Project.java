package org.ek.portfoliobackend.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter


@Entity
public class Project {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ServiceCategory serviceCategory;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    private LocalDate executionDate;

    private LocalDate creationDate;

    @JsonManagedReference // Stopper recursion
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();



    public void addImage(Image image) {
        images.add(image);
        image.setProject(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setProject(null);
    }


}
