package hexlet.code.controller.impl;

import hexlet.code.controller.UserController;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

import static hexlet.code.controller.impl.UserControllerImpl.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserControllerImpl implements UserController {

    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String USER_ID = "/{id}";
    private static final String ACCOUNT_OWNER = """
                @userRepository.findById(#id).get().getEmail() == authentication.getName()
            """;

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping(USER_ID)
    public User getUser(@PathVariable final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public User createUser(@RequestBody @Valid final UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PutMapping(USER_ID)
    @PreAuthorize(ACCOUNT_OWNER)
    public User updateUser(@PathVariable final Long id,
                           @RequestBody @Valid final UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping(USER_ID)
    @PreAuthorize(ACCOUNT_OWNER)
    public void deleteUser(@PathVariable final Long id) {
        userService.deleteUser(id);
    }
}
