package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import java.util.List;

public interface RatingService {
    void addRating(Rating rating);
    List<Rating> getRatings(String game);
}