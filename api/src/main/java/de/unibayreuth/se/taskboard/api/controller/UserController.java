package de.unibayreuth.se.taskboard.api.controller;

import de.unibayreuth.se.taskboard.api.dtos.TaskDto;
import de.unibayreuth.se.taskboard.api.dtos.UserDto;
import de.unibayreuth.se.taskboard.api.mapper.UserDtoMapper;
import de.unibayreuth.se.taskboard.business.domain.Task;
import de.unibayreuth.se.taskboard.business.ports.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@OpenAPIDefinition(
        info = @Info(
                title = "TaskBoard",
                version = "0.0.1"
        )
)
@Tag(name = "Users")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    @Operation(
        summary = "Retrieve a list of all users.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "array", implementation = UserDto.class)
                ),
                description = "List of users retrieved successfully."
            )
        }
    )
    @GetMapping
    public List<UserDto> fetchAllUsers() {
        return userService.getAll().stream()
            .map(userDtoMapper::fromBusiness)
            .collect(Collectors.toList());
    }

    @Operation(
        summary = "Retrieve a user by their unique identifier.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class)
                ),
                description = "User retrieved successfully."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User with the given ID not found."
            )
        }
    )
    @GetMapping("/{id}")
    public UserDto fetchUserById(@PathVariable UUID id) {
        return userService.getById(id)
            .map(userDtoMapper::fromBusiness)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found with ID: " + id));
    }

    @Operation(
        summary = "Create a new user.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class)
                ),
                description = "New user created successfully."
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request data provided."
            )
        }
    )
    @PostMapping
    public ResponseEntity<UserDto> addNewUser(@RequestBody @Valid UserDto userDto) {
        UserDto createdUser = userDtoMapper.fromBusiness(
            userService.create(userDtoMapper.toBusiness(userDto))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}