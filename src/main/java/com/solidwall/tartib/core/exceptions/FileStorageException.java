package com.solidwall.tartib.core.exceptions;

/**
 * Custom exception for handling file storage related errors.
 * This exception is thrown when operations related to file storage
 * (upload, delete, read) fail.
 */
public class FileStorageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new FileStorageException with the specified message.
     *
     * @param message the detail message
     */
    public FileStorageException(String message) {
        super(message);
    }

    /**
     * Constructs a new FileStorageException with the specified message and cause.
     * This constructor is particularly useful when you want to preserve the stack trace
     * of the original exception while throwing a more specific exception.
     *
     * @param message the detail message
     * @param cause the cause (which is saved for later retrieval by the getCause() method)
     */
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a FileStorageException for missing files.
     *
     * @param filename the name of the file that was not found
     * @return a new FileStorageException with an appropriate message
     */
    public static FileStorageException fileNotFound(String filename) {
        return new FileStorageException(String.format("File '%s' could not be found", filename));
    }

    /**
     * Creates a FileStorageException for file upload failures.
     *
     * @param filename the name of the file that failed to upload
     * @param cause the underlying cause of the failure
     * @return a new FileStorageException with an appropriate message
     */
    public static FileStorageException uploadFailed(String filename, Throwable cause) {
        return new FileStorageException(
            String.format("Failed to store file '%s'. Please try again", filename),
            cause
        );
    }

    /**
     * Creates a FileStorageException for file deletion failures.
     *
     * @param filename the name of the file that failed to be deleted
     * @return a new FileStorageException with an appropriate message
     */
    public static FileStorageException deleteFailed(String filename) {
        return new FileStorageException(
            String.format("Failed to delete file '%s'", filename)
        );
    }

    /**
     * Creates a FileStorageException for invalid file types.
     *
     * @param filename the name of the invalid file
     * @param allowedTypes a description of the allowed file types
     * @return a new FileStorageException with an appropriate message
     */
    public static FileStorageException invalidFileType(String filename, String allowedTypes) {
        return new FileStorageException(
            String.format("File '%s' is not of allowed type. Allowed types are: %s", filename, allowedTypes)
        );
    }
}