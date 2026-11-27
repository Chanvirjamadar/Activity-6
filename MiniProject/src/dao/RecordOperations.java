package dao;

import java.util.List;
import model.ValidationException;

/**
 * Generic interface for repository operations.
 * 
 * Demonstrates Key OOP Concepts:
 * 1. Abstraction: Defines the public contract without exposing the underlying storage mechanism.
 * 2. Polymorphism: Allows any entity manager (File, Database, In-Memory) to be swapped interchangeably.
 *
 * @param <T> The entity type handled by the repository
 */
public interface RecordOperations<T> {

    /**
     * Adds a new record to the repository.
     *
     * @param record The entity to be added
     * @return true if added successfully
     * @throws ValidationException if validation rules are violated
     */
    boolean add(T record) throws ValidationException;

    /**
     * Updates an existing record in the repository.
     *
     * @param record The entity with updated details
     * @return true if updated successfully
     * @throws ValidationException if validation rules or constraints are violated
     */
    boolean update(T record) throws ValidationException;

    /**
     * Deletes a record by its unique identifier.
     *
     * @param id The unique identifier
     * @return true if deleted successfully, false if record not found
     * @throws ValidationException if deletion cannot be performed
     */
    boolean delete(String id) throws ValidationException;

    /**
     * Finds and returns a record by its unique identifier.
     *
     * @param id The unique identifier
     * @return The entity if found, or null otherwise
     */
    T findById(String id);

    /**
     * Returns an unmodifiable or new copy of all records.
     *
     * @return List of all stored entities
     */
    List<T> getAll();

    /**
     * Returns the total count of records.
     *
     * @return count of records
     */
    int count();
}

