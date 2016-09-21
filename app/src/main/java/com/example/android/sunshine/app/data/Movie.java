package com.example.android.sunshine.app.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Serializable {
    private String id ;
    private String originalTitle ;
    private String title ;
    private String posterPath ;
    private String backdropPath ;
    private String voteAve ;
    private Boolean adult ;
    private String overview ;
    private String releaseDate ;
    private String trailersJsonData ;
    private String reviwesJsonData ;
    private List <String> trailerLinks = new ArrayList<String>();
    private List <String> trailerName =new ArrayList <String>();
    private List <String> review ;

    public Movie() {
    }

    public Movie(String id, String originalTitle,
                 String title, String backdropPath, String posterPath,
                 Double voteAve, Boolean adult, String overview, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.backdropPath = "http://image.tmdb.org/t/p/w185"+backdropPath;
        this.posterPath = "http://image.tmdb.org/t/p/w342"+posterPath;
        this.voteAve = String.valueOf(voteAve);
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setVoteAve(String voteAve) {this.voteAve = voteAve;}

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getVoteAve() {
        return voteAve;
    }

    public Boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<String> getTrailerLinks() {
        return trailerLinks;
    }

    public void setTrailerLinks(List<String> trailerLinks) {this.trailerLinks = trailerLinks;}

    public List<String> getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(List<String> trailerName) {
        this.trailerName = trailerName;
    }

    public List <String> getReview() {
        return review;
    }

    public void setReview(List <String> review) {
        this.review = review;
    }

    public String getTrailersJsonData() {
        return trailersJsonData;
    }

    public void setTrailersJsonData(String trailersJsonData) {
        this.trailersJsonData = trailersJsonData;
    }

    public String getReviwesJsonData() {
        return reviwesJsonData;
    }

    public void setReviwesJsonData(String reviwesJsonData) {
        this.reviwesJsonData = reviwesJsonData;
    }
}
