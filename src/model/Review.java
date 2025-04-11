package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Class representing a review for a paper.
 * Demonstrates encapsulation and information hiding.
 */
public class Review implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reviewId;
    private String paperId;
    private String reviewerId;
    private String reviewerName;  // Stored as "ANONYMOUS" for blind reviews
    private int rating;  // Rating on a scale (e.g., 1-5)
    private String comments;
    private LocalDateTime submissionDate;
    private ReviewStatus status;

    /**
     * Constructor for creating a new review
     */
    public Review(String paperId, String reviewerId, String reviewerName,
                  int rating, String comments) {
        this.reviewId = UUID.randomUUID().toString();
        this.paperId = paperId;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.rating = validateRating(rating);
        this.comments = comments;
        this.submissionDate = LocalDateTime.now();
        this.status = ReviewStatus.COMPLETED;
    }

    /**
     * Constructor for loading a review from storage
     */
    public Review(String reviewId, String paperId, String reviewerId, String reviewerName,
                  int rating, String comments, LocalDateTime submissionDate, ReviewStatus status) {
        this.reviewId = reviewId;
        this.paperId = paperId;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.rating = validateRating(rating);
        this.comments = comments;
        this.submissionDate = submissionDate;
        this.status = status;
    }

    // Validate that rating is within a valid range (1-5)
    private int validateRating(int rating) {
        if (rating < 1) return 1;
        if (rating > 5) return 5;
        return rating;
    }

    // Getters and setters
    public String getReviewId() {
        return reviewId;
    }

    public String getPaperId() {
        return paperId;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = validateRating(rating);
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    /**
     * Get a blinded version of the review (hides reviewer information)
     */
    public Review getBlindedCopy() {
        Review blindedReview = new Review(
                reviewId, paperId, "ANONYMOUS", "ANONYMOUS",
                rating, comments, submissionDate, status
        );
        return blindedReview;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId='" + reviewId + '\'' +
                ", paperId='" + paperId + '\'' +
                ", reviewer='" + reviewerName + '\'' +
                ", rating=" + rating +
                ", submission date=" + submissionDate +
                ", status=" + status +
                '}';
    }
}