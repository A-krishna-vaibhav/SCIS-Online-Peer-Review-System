package storage;

import java.util.List;
import java.util.Optional;

/**
 * Interface for data storage operations.
 * Demonstrates abstraction by defining a contract without implementation details.
 */
public interface DataStorage<T> {

    /**
     * Save an object to storage
     * @param object The object to save
     * @return true if saved successfully
     */
    boolean save(T object);

    /**
     * Find an object by its ID
     * @param id The ID to search for
     * @return An Optional containing the object if found
     */
    Optional<T> findById(String id);

    /**
     * Get all objects from storage
     * @return List of all objects
     */
    List<T> findAll();

    /**
     * Update an existing object
     * @param object The object with updated values
     * @return true if updated successfully
     */
    boolean update(T object);

    /**
     * Delete an object by its ID
     * @param id The ID of the object to delete
     * @return true if deleted successfully
     */
    boolean deleteById(String id);

    /**
     * Save all objects in memory to persistent storage
     * @return true if saved successfully
     */
    boolean saveAll();

    /**
     * Load all objects from persistent storage
     */
    void loadAll();
}