package com.example.project01mvvm.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("published_at")
    @Expose
    private String publishedAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("starts_at")
    @Expose
    private String startsAt;
    @SerializedName("ends_at")
    @Expose
    private Object endsAt;
    @SerializedName("only_submissions_after")
    @Expose
    private Object onlySubmissionsAfter;
    @SerializedName("visibility")
    @Expose
    private String visibility;
    @SerializedName("featured")
    @Expose
    private Boolean featured;
    @SerializedName("total_photos")
    @Expose
    private Integer totalPhotos;
    @SerializedName("current_user_contributions")
    @Expose
    private List<Object> currentUserContributions;
    @SerializedName("total_current_user_submissions")
    @Expose
    private Object totalCurrentUserSubmissions;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("owners")
    @Expose
    private List<Owner> owners;
    @SerializedName("cover_photo")
    @Expose
    private CoverPhoto coverPhoto;
    @SerializedName("preview_photos")
    @Expose
    private List<PreviewPhoto> previewPhotos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public Object getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Object endsAt) {
        this.endsAt = endsAt;
    }

    public Object getOnlySubmissionsAfter() {
        return onlySubmissionsAfter;
    }

    public void setOnlySubmissionsAfter(Object onlySubmissionsAfter) {
        this.onlySubmissionsAfter = onlySubmissionsAfter;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Integer getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(Integer totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public List<Object> getCurrentUserContributions() {
        return currentUserContributions;
    }

    public void setCurrentUserContributions(List<Object> currentUserContributions) {
        this.currentUserContributions = currentUserContributions;
    }

    public Object getTotalCurrentUserSubmissions() {
        return totalCurrentUserSubmissions;
    }

    public void setTotalCurrentUserSubmissions(Object totalCurrentUserSubmissions) {
        this.totalCurrentUserSubmissions = totalCurrentUserSubmissions;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    public void setOwners(List<Owner> owners) {
        this.owners = owners;
    }

    public CoverPhoto getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(CoverPhoto coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public List<PreviewPhoto> getPreviewPhotos() {
        return previewPhotos;
    }

    public void setPreviewPhotos(List<PreviewPhoto> previewPhotos) {
        this.previewPhotos = previewPhotos;
    }

}





