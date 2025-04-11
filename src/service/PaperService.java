package service;

import model.*;
import storage.DataStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class to manage Paper-related operations.
 * Demonstrates separation of concerns by isolating paper management logic.
 */
public class PaperService {
    private static DataStorage<Paper> paperStorage = null;
    private final UserService userService;

    public PaperService(DataStorage<Paper> paperStorage, UserService userService) {
        PaperService.paperStorage = paperStorage;
        this.userService = userService;
    }

    /**
     * Submit a new paper
     */
    public boolean submitPaper(String title, String abstractText, String content,
                               String authorId, List<String> keywords) {
        Optional<User> author = userService.findUserById(authorId);

        if (author.isPresent()) {
            Paper paper = new Paper(title, abstractText, content, authorId,
                    author.get().getName(), keywords);
            return paperStorage.save(paper);
        }

        return false;
    }

    /**
     * Find a paper by its ID
     */
    public static Optional<Paper> findPaperById(String paperId) {
        return paperStorage.findById(paperId);
    }

    /**
     * Get all papers in the system
     */
    public List<Paper> getAllPapers() {
        return paperStorage.findAll();
    }

    /**
     * Get papers submitted by a specific author
     */
    public List<Paper> getPapersByAuthor(String authorId) {
        return paperStorage.findAll().stream()
                .filter(paper -> paper.getAuthorId().equals(authorId))
                .collect(Collectors.toList());
    }

    /**
     * Get papers assigned to a specific reviewer
     */
    public List<Paper> getPapersForReviewer(String reviewerId) {
        return paperStorage.findAll().stream()
                .filter(paper -> paper.getReviewerIds().contains(reviewerId))
                .map(Paper::getBlindedCopy)  // Return blinded copies for review
                .collect(Collectors.toList());
    }

    /**
     * Assign a reviewer to a paper
     */
    public boolean assignReviewer(String paperId, String reviewerId) {
        Optional<Paper> paperOpt = paperStorage.findById(paperId);
        Optional<User> reviewerOpt = userService.findUserById(reviewerId);

        if (paperOpt.isPresent() && reviewerOpt.isPresent()) {
            Paper paper = paperOpt.get();

            // Don't allow authors to review their own papers
            if (paper.getAuthorId().equals(reviewerId)) {
                return false;
            }

            paper.assignReviewer(reviewerId);
            paper.setStatus(ReviewStatus.IN_PROGRESS);
            return paperStorage.update(paper);
        }

        return false;
    }

    /**
     * Remove a reviewer from a paper
     */
    public boolean removeReviewer(String paperId, String reviewerId) {
        Optional<Paper> paperOpt = paperStorage.findById(paperId);

        if (paperOpt.isPresent()) {
            Paper paper = paperOpt.get();
            paper.removeReviewer(reviewerId);

            // If no reviewers left, set status back to PENDING
            if (paper.getReviewerIds().isEmpty()) {
                paper.setStatus(ReviewStatus.PENDING);
            }

            return paperStorage.update(paper);
        }

        return false;
    }

    /**
     * Update a paper's status
     */
    public boolean updatePaperStatus(String paperId, ReviewStatus status) {
        Optional<Paper> paperOpt = paperStorage.findById(paperId);

        if (paperOpt.isPresent()) {
            Paper paper = paperOpt.get();
            paper.setStatus(status);
            return paperStorage.update(paper);
        }

        return false;
    }

    /**
     * Update paper information
     */
    public void updatePaper(Paper paper) {
        paperStorage.update(paper);
    }

    /**
     * Delete a paper by its ID
     */
    public boolean deletePaper(String paperId) {
        return paperStorage.deleteById(paperId);
    }

    /**
     * Get papers by status
     */
    public List<Paper> getPapersByStatus(ReviewStatus status) {
        return paperStorage.findAll().stream()
                .filter(paper -> paper.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Get papers containing a keyword
     */
    public List<Paper> searchPapersByKeyword(String keyword) {
        return paperStorage.findAll().stream()
                .filter(paper -> paper.getKeywords().stream()
                        .anyMatch(k -> k.toLowerCase().contains(keyword.toLowerCase())))
                .collect(Collectors.toList());
    }
}