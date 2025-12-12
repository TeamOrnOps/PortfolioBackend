package org.ek.portfoliobackend.controller;

import jakarta.validation.Valid;
import org.ek.portfoliobackend.dto.request.*;
import org.ek.portfoliobackend.dto.response.ImageResponse;
import org.ek.portfoliobackend.dto.response.ProjectResponse;
import org.ek.portfoliobackend.exception.custom.ResourceNotFoundException;
import org.ek.portfoliobackend.model.CustomerType;
import org.ek.portfoliobackend.model.WorkType;
import org.ek.portfoliobackend.service.ImageStorageService;
import org.ek.portfoliobackend.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling project-related HTTP requests.
 * Provides endpoints for creating, retrieving, updating, and deleting projects.
 *
 * CORS is configured globally in CorsConfig.
 */
@Slf4j
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ImageStorageService imageStorageService;

    public ProjectController(ProjectService projectService, ImageStorageService imageStorageService) {
        this.projectService = projectService;
        this.imageStorageService = imageStorageService;
    }


    // Retrieves a project by its ID (images included)
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        ProjectResponse response = projectService.getProjectById(id);
        log.info("Fetched project with ID: {}", id);
        return ResponseEntity.ok(response);
    }

    // Retrieves all projects with optional filtering and sorting, by workType and customerType.
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects(
            @RequestParam(required = false) WorkType workType,
            @RequestParam(required = false)CustomerType customerType,
            @RequestParam(name = "sort", required = false)
            String sortDirection) {

        log.info("Received request to fetch projects - workType: {}, customerType: {}, sort {}",
                workType, customerType, sortDirection);

        List<ProjectResponse> projects = projectService.getProjectsByFilters(workType, customerType, sortDirection);

        log.info("Successfully retrieved {} projects with applied filters and sorting", projects.size());
        return ResponseEntity.ok(projects);
    }

    /**
     * Creates a new project with multiple images and metadata.
     *
     * Validates that:
     * - All required fields are provided
     * - At least one BEFORE and one AFTER image are included
     * - Image files are valid
     *
     * @param request Project data (title, description, serviceCategory, customerType, executionDate)
     * @param images List of image files to upload
     * @param imageMetadata Metadata for each image (imageType, isFeatured)
     * @return ResponseEntity with the created project and HTTP 201 status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProjectResponse> createProject(
            @RequestPart("data") CreateProjectRequest request,
            @RequestPart("images") List<MultipartFile> images,
            @RequestPart("imageMetadata") List<ImageUploadRequest> imageMetadata) {

        log.info("Received request to create project: {}", request.getTitle());
        log.debug("Request details - Images: {}, Metadata entries: {}",
                images.size(), imageMetadata.size());

        //validate image and metadate list sizes match
        if (images.size() != imageMetadata.size()) {
            String errorMsg = String.format(
                    "Mismatch between images (%d) and metadata (%d) count",
                    images.size(), imageMetadata.size()
            );
            log.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        ProjectResponse createdProject = projectService.createProject(request, images, imageMetadata);

        log.info("Successfully created project with ID: {}", createdProject.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    /**
     * Uploads new images to existing project.
     * new images are added to project's existing images.
     * validates that project maintains at least one BEFORE and one AFTER image.
     *
     * @param id Project ID
     * @param images List of image files to upload
     * @param imageMetadata Metadata for each image (imageType, isFeatured)
     * @return ResponseEntity with the updated project and HTTP 200 status
     * * @throws ResourceNotFoundException if project with given ID does not exist
     * @throws IllegalArgumentException with BAD_REQUEST if validation fails
     * */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}/images", consumes = "multipart/form-data")
    public ResponseEntity<ProjectResponse> uploadProjectImages(
            @PathVariable Long id,
            @RequestPart("images") List<MultipartFile> images,
            @RequestPart("imageMetadata") List<ImageUploadRequest> imageMetadata) {
        log.info("Received request to upload images for project ID: {}", id);

        // validate image and metadate list sizes match
        if (images.size() != imageMetadata.size()) {
            String errorMsg = String.format(
                    "Mismatch between images (%d) and metadata (%d) count",
                    images.size(), imageMetadata.size()
            );
            log.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        ProjectResponse updatedProject = projectService.addImagesToProject(id, images, imageMetadata);

        log.info("Successfully uploaded {} images to project ID: {}", images.size(), id);
        return ResponseEntity.ok(updatedProject);

    }

    /**
     * Updates metadata of an existing image within a project.
     * only updates the fields provided in the request (imageType, isFeatured).
     *
     * @param projectId Project ID
     * @param imageId Image ID of image to update
     * @param request Metadata fields to update (imageType, isFeatured)
     * @return ResponseEntity with the updated project and HTTP 200 status
     * @throws ResourceNotFoundException if project or image not found
     * */

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{projectId}/images/{imageId}")
    public ResponseEntity<ProjectResponse> updateImageMetadata(@PathVariable Long projectId,
                                                       @PathVariable Long imageId,
                                                       @Valid @RequestBody UpdateImageRequest request) {
        log.info("Received request to update image metadata for image ID: {} in project ID: {}", imageId, projectId);

        ProjectResponse updatedProject = projectService.updateImageMetadata(projectId, imageId, request);

        log.info("Successfully updated image metadata");
        return ResponseEntity.ok(updatedProject);
    }

    @PutMapping("/{projectId}/images/{imageId}/url")
    public ImageResponse updateImageUrl(
            @PathVariable Long projectId,
            @PathVariable Long imageId,
            @RequestBody @Valid UpdateImageUrlRequest request) {

        return projectService.updateImageUrl(projectId, imageId, request);
    }

    @PostMapping("/upload")
    public Map<String, String> uploadImage(
            @RequestParam("file") MultipartFile file) {

        String url = imageStorageService.store(file);
        return Map.of("url", url);
    }


    // Updates an existing project's details
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id,
                                                        @Valid @RequestBody UpdateProjectRequest request) {
        log.info("Received request to update project with ID: {}", id);
        log.debug("Update details - Title: {}, Description: {}", request.getTitle(), request.getDescription());

        ProjectResponse updatedProject = projectService.updateProject(id, request);

        log.info("Successfully updated project with ID: {}", id);
        return ResponseEntity.ok(updatedProject);
    }

    // Deletes an image from a project by image ID
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{projectId}/images/{imageId}")
    public ResponseEntity<ProjectResponse> deleteImage(@PathVariable Long projectId,
                                                   @PathVariable Long imageId) {
        log.info("Received request to delete image ID: {} from project ID: {}", imageId, projectId);

        ProjectResponse updatedProject = projectService.deleteImageFromProject(projectId, imageId);

        log.info("Successfully deleted image ID: {} from project ID: {}", imageId, projectId);
        return ResponseEntity.ok(updatedProject);
    }

    // Deletes project and all associated images
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.info("Received request to delete project with ID: {}", id);

        projectService.deleteProject(id);

        log.info("Successfully deleted project with ID: {}", id);
        return ResponseEntity.noContent().build();
    }


}