package model;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Abstract base class representing a Person.
 * 
 * Demonstrates Key OOP Concepts:
 * 1. Abstraction: Defines the abstract template and common characteristics of a person,
 *    with abstract methods that subclasses must implement.
 * 2. Encapsulation: All properties are private and accessed/mutated via validated getters and setters.
 */
public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    // Encapsulated private attributes
    private String fullName;
    private String email;
    private String mobileNumber;
    private String gender;

    // Regular expression patterns for input validation
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = 
            Pattern.compile("^[0-9]{10}$");
    private static final Pattern NAME_PATTERN = 
            Pattern.compile("^[a-zA-Z\\s.'-]{2,60}$");

    /**
     * Default constructor (demonstrating constructor overloading).
     */
    public Person() {
        this.fullName = "";
        this.email = "";
        this.mobileNumber = "";
        this.gender = "Not Specified";
    }

    /**
     * Parameterized constructor.
     *
     * @param fullName     The full name of the person
     * @param email        The email address
     * @param mobileNumber The 10-digit mobile number
     * @param gender       The gender (Male, Female, Other)
     * @throws ValidationException if any input violates validation constraints
     */
    public Person(String fullName, String email, String mobileNumber, String gender) throws ValidationException {
        setFullName(fullName);
        setEmail(email);
        setMobileNumber(mobileNumber);
        setGender(gender);
    }

    /**
     * Copy constructor (demonstrating constructor overloading).
     *
     * @param other The Person object to copy from
     */
    public Person(Person other) {
        if (other != null) {
            this.fullName = other.fullName;
            this.email = other.email;
            this.mobileNumber = other.mobileNumber;
            this.gender = other.gender;
        }
    }

    // ==========================================
    // Abstract Methods (OOP: Abstraction)
    // ==========================================

    /**
     * Returns the human-readable role of this person (e.g., "Student", "Faculty").
     * Must be implemented by concrete subclasses.
     */
    public abstract String getDisplayRole();

    /**
     * Returns a concise summary description of this person.
     * Must be implemented by concrete subclasses.
     */
    public abstract String getSummary();

    // ==========================================
    // Getters and Setters with Encapsulation & Validation
    // ==========================================

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) throws ValidationException {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new ValidationException("Full Name cannot be empty.");
        }
        String trimmed = fullName.trim();
        if (!NAME_PATTERN.matcher(trimmed).matches()) {
            throw new ValidationException("Full Name must contain only alphabetic characters, spaces, or dots (2-60 characters).");
        }
        this.fullName = trimmed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email Address cannot be empty.");
        }
        String trimmed = email.trim();
        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw new ValidationException("Please enter a valid email address (e.g., student@college.edu).");
        }
        this.email = trimmed;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) throws ValidationException {
        if (mobileNumber == null || mobileNumber.trim().isEmpty()) {
            throw new ValidationException("Mobile Number cannot be empty.");
        }
        String cleanNumber = mobileNumber.trim().replaceAll("[^0-9]", "");
        if (!PHONE_PATTERN.matcher(cleanNumber).matches()) {
            throw new ValidationException("Mobile Number must be exactly 10 numeric digits.");
        }
        this.mobileNumber = cleanNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) throws ValidationException {
        if (gender == null || gender.trim().isEmpty()) {
            throw new ValidationException("Gender selection cannot be empty.");
        }
        String trimmed = gender.trim();
        if (!trimmed.equalsIgnoreCase("Male") && 
            !trimmed.equalsIgnoreCase("Female") && 
            !trimmed.equalsIgnoreCase("Other")) {
            throw new ValidationException("Gender must be Male, Female, or Other.");
        }
        this.gender = trimmed;
    }

    @Override
    public String toString() {
        return "Name: " + fullName + " | Email: " + email + " | Phone: " + mobileNumber + " | Gender: " + gender;
    }
}

