package de.unibayreuth.se.taskboard.api.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserDto {

    @Nullable
    private UUID id;

    @Nullable
    private LocalDateTime createdAt;

    @NotNull
    @NotBlank
    @Size(max = 255, message = "Name must not exceed 255 characters.")
    private String name;

    // Default constructor for serialization/deserialization frameworks
    public UserDto() {
    }

    // Constructor for initializing all fields
    public UserDto(@Nullable UUID id, @Nullable LocalDateTime createdAt, @NotNull String name) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
    }

    // Getter and Setter for ID
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    // Getter and Setter for createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}