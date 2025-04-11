package service;

import model.*;
import storage.DataStorage;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class to manage Review-related operations.
 * Demonstrates separation of concerns by isolating review management logic.
 */
public class ReviewService {
    private final DataStorage<Review> reviewStorage;
    private final UserService userService;
    private Admin Admin;

    public ReviewService(DataStorage<Review> reviewStorage,
                         PaperService paperService,
                         UserService userService) {
        this.reviewStorage = reviewStorage;
        this.userService = userService;
    }

    /**
     * Submit a new review
     */
    public boolean submitReview(String paperId, String reviewerId,
                                int rating, String comments) {
        Optional<Paper> paperOpt = PaperService.findPaperById(paperId);
        Optional<User> reviewerOpt = userService.findUserById(reviewerId);

        if (paperOpt.isPresent() && reviewerOpt.isPresent()) {
            Paper paper = paperOpt.get();
            User reviewer = reviewerOpt.get();

            // Check if the reviewer is assigned to this paper
            if (!paper.getReviewerIds().contains(reviewerId)) {
                return false;
            }

            // Check if the reviewer has already submitted a review for this paper
            if (getReviewByPaperAndReviewer(paperId, reviewerId).isPresent()) {
                return false;
            }

            Review review = new Review(paperId, reviewerId, reviewer.getName(), rating, comments);
            return reviewStorage.save(review);
        }

        return false;
    }

    /**
     * Find a review by its ID
     */
    public Optional<Review> findReviewById(String reviewId) {
        return reviewStorage.findById(reviewId);
    }

    /**
     * Get all reviews in the system
     */
    public List<Review> getAllReviews() {
        return reviewStorage.findAll();
    }

    /**
     * Get reviews for a specific paper
     * For authors, returns blinded reviews
     * For admins, returns full reviews
     */

    public List<String> getReviewersForPaper(String paperId) {
        Optional<Paper> paperOpt = PaperService.findPaperById(paperId);
        return paperOpt.map(Paper::getReviewerIds).orElseGet(List::of);
    }

    public List<Review> getReviewsForPaper(String paperId) {
        List<Review> reviews = reviewStorage.findAll().stream()
                .filter(review -> review.getPaperId().equals(paperId))
                .collect(Collectors.toList());

        // If not admin, return blinded copies

        if (!Admin.isAdmin()) {
            return reviews.stream()
                    .map(Review::getBlindedCopy)
                    .collect(Collectors.toList());
        }

        return reviews;
    }

    /**
     * Get reviews submitted by a specific reviewer
     */
    public List<Review> getReviewsByReviewer(String reviewerId) {
        return reviewStorage.findAll().stream()
                .filter(review -> review.getReviewerId().equals(reviewerId))
                .collect(Collectors.toList());
    }

    /**
     * Find a review by paper ID and reviewer ID
     */
    public Optional<Review> getReviewByPaperAndReviewer(String paperId, String reviewerId) {
        return reviewStorage.findAll().stream()
                .filter(review -> review.getPaperId().equals(paperId) &&
                        review.getReviewerId().equals(reviewerId))
                .findFirst();
    }

    /**
     * Update review information
     */
    public boolean updateReview(Review review) {
        return reviewStorage.update(review);
    }

    /**
     * Delete a review by its ID
     */
    public boolean deleteReview(String reviewId) {
        return reviewStorage.deleteById(reviewId);
    }

    /**
     * Calculate the average rating for a paper
     */
    public double getAveragePaperRating(String paperId) {
        List<Review> reviews = reviewStorage.findAll().stream()
                .filter(review -> review.getPaperId().equals(paperId))
                .toList();

        if (reviews.isEmpty()) {
            return 0.0;
        }

        int sum = reviews.stream()
                .mapToInt(Review::getRating)
                .sum();

        return (double) sum / reviews.size();
    }
}