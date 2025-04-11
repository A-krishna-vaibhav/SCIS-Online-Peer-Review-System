package model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class representing a research paper in the system.
 * Demonstrates encapsulation by protecting its internal state.
 */
public class Paper implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String paperId;
    private String title;
    private String abstractText;
    private String content;
    private final String authorId;  // ID of the user who submitted the paper
    private final String authorName;  // Name of the author (for display purposes)
    private final LocalDateTime submissionDate;
    private List<String> keywords;
    private final List<String> reviewerIds;  // IDs of users assigned to review this paper
    private ReviewStatus status;

    /**
     * Constructor for creating a new paper
     */
    public Paper(String title, String abstractText, String content,
                 String authorId, String authorName, List<String> keywords) {
        this.paperId = UUID.randomUUID().toString();
        this.title = title;
        this.abstractText = abstractText;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.submissionDate = LocalDateTime.now();
        this.keywords = keywords != null ? keywords : new ArrayList<>();
        this.reviewerIds = new ArrayList<>();
        this.status = ReviewStatus.PENDING;
    }

    /**
     * Constructor for loading a paper from storage
     */
    public Paper(String paperId, String title, String abstractText, String content,
                 String authorId, String authorName, LocalDateTime submissionDate,
                 List<String> keywords, List<String> reviewerIds, ReviewStatus status) {
        this.paperId = paperId;
        this.title = title;
        this.abstractText = abstractText;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.submissionDate = submissionDate;
        this.keywords = keywords != null ? keywords : new ArrayList<>();
        this.reviewerIds = reviewerIds != null ? reviewerIds : new ArrayList<>();
        this.status = status;
    }

    // Getters and setters
    public String getPaperId() {
        return paperId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public List<String> getKeywords() {
        return new ArrayList<>(keywords);  // Return a copy to prevent external modification
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = new ArrayList<>(keywords);
    }

    public List<String> getReviewerIds() {
        return new ArrayList<>(reviewerIds);  // Return a copy to prevent external modification
    }

    public void assignReviewer(String reviewerId) {
        if (!reviewerIds.contains(reviewerId) && !reviewerId.equals(authorId)) {
            reviewerIds.add(reviewerId);
        }
    }

    public void removeReviewer(String reviewerId) {
        reviewerIds.remove(reviewerId);
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    /**
     * Get a blinded version of the paper for review purposes (hides author information)
     */
    public Paper getBlindedCopy() {
        return new Paper(
                paperId, title, abstractText, content,
                "ANONYMOUS", "ANONYMOUS", submissionDate,
                keywords, reviewerIds, status
        );
    }

    @Override
    public String toString() {
        return STR."Paper{paperId='\{paperId}', title='\{title}', author='\{authorName}', submission date='\{submissionDate}', status='\{status}', reviewers=\{reviewerIds.size()}}";
    }
}