package org.ek.portfoliobackend.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateImageUrlRequest {

    @NotBlank
    private String url;


    public UpdateImageUrlRequest() {
    }

    public UpdateImageUrlRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
}

    public void setUrl(String url) {
        this.url = url;
    }
}
