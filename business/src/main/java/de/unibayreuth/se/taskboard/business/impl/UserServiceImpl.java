package de.unibayreuth.se.taskboard.business.impl;

import de.unibayreuth.se.taskboard.business.domain.User;
import de.unibayreuth.se.taskboard.business.exceptions.DuplicateNameException;
import de.unibayreuth.se.taskboard.business.exceptions.MalformedRequestException;
import de.unibayreuth.se.taskboard.business.exceptions.TaskNotFoundException;
import de.unibayreuth.se.taskboard.business.exceptions.UserNotFoundException;
import de.unibayreuth.se.taskboard.business.ports.UserPersistenceService;
import de.unibayreuth.se.taskboard.business.ports.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserPersistenceService persistenceService;

    @Override
    public void clear() {
        persistenceService.clear();
    }

    @Override
    @NonNull
    public User create(@NonNull User user) throws MalformedRequestException {
        if (user.getId() != null) {
            throw new MalformedRequestException("User ID must be null when creating a new user.");
        }
        return saveOrUpdate(user);
    }

    @Override
    @NonNull
    public List<User> getAll() {
        return persistenceService.getAll();
    }

    @Override
    public Optional<User> getById(@NonNull UUID id) throws TaskNotFoundException {
        return persistenceService.getById(id)
                .or(() -> {
                    throw new TaskNotFoundException("No user found with ID: " + id);
                });
    }

    @Override
    @NonNull
    public User saveOrUpdate(@NonNull User user) throws UserNotFoundException, DuplicateNameException {
        if (user.getId() != null) {
            ensureUserExists(user.getId());
        }
        return persistenceService.upsert(user);
    }

    private void ensureUserExists(@NonNull UUID userId) throws UserNotFoundException {
        if (persistenceService.getById(userId).isEmpty()) {
            throw new UserNotFoundException("User with ID " + userId + " was not found.");
        }
    }
}
