package by.teachmeskills.shopwebservice.controllers;

import by.teachmeskills.shopwebservice.dto.AuthResponse;
import by.teachmeskills.shopwebservice.dto.RefreshJwtTokenDto;
import by.teachmeskills.shopwebservice.dto.UserCredentialsRequest;
import by.teachmeskills.shopwebservice.dto.UserDto;
import by.teachmeskills.shopwebservice.exceptions.LoginException;
import by.teachmeskills.shopwebservice.services.AuthService;
import by.teachmeskills.shopwebservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Tag(name = "user", description = "User Endpoint")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @Operation(
            summary = "Find all users",
            description = "Find all existed users in Shop",
            tags = {"user"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All users were found",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Users not found - server error"
                    )
            }
    )
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Find certain user",
            description = "Find certain existed user in Shop by his id",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User was found by his id",
                    content = @Content(schema = @Schema(contentSchema = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User not fount - forbidden operation"
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<UserDto> getUserById(@Parameter(required = true, description = "User ID") @PathVariable int id) {
        return Optional.ofNullable(userService.getUser(id)).map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Login user",
            description = "Login existed user in Shop by his email and password",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The user is logged in with their email address and password",
                    content = @Content(schema = @Schema(contentSchema = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User isn't logged in - forbidden operation"
            )
    })
    @GetMapping("/login/{email}/{password}")
    public ResponseEntity<UserDto> login(@Parameter(required = true, description = "Email") @PathVariable String email,
                                         @Parameter(required = true, description = "Password") @PathVariable String password) throws LoginException {
        return Optional.ofNullable(userService.getUserByEmailAndPassword(email, password)).map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @Operation(
            summary = "Authenticate user",
            description = "Login user and get access token",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token is returned, user logged in"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User not logged in",
                    content = @Content
            )
    })
    @PostMapping("/login")
    public AuthResponse auth(@RequestBody @Valid UserCredentialsRequest request) throws AuthException {
        return authService.login(request);
    }

    @Operation(
            summary = "Access token",
            description = "Get new access token by refresh token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token is returned, user logged in"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User not logged in",
                    content = @Content
            )
    })
    @PostMapping("/token")
    public AuthResponse getNewAccessToken(@RequestBody @Valid RefreshJwtTokenDto refreshTokenRequest) {
        return authService.getAccessToken(refreshTokenRequest);
    }

    @Operation(
            summary = "Refresh security token",
            description = "Get new access token",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token is returned, access token updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Token isn't returned.",
                    content = @Content
            )
    })
    @PostMapping("/refreshToken")
    public AuthResponse refreshToken(@RequestBody @Valid RefreshJwtTokenDto refreshTokenRequest) throws AuthException {
        return authService.getRefreshToken(refreshTokenRequest);
    }

    @Operation(
            summary = "Create user",
            description = "Create new user",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User was created",
                    content = @Content(schema = @Schema(contentSchema = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User not created - server error"
            )
    })
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update user",
            description = "Update existed user",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User was updated",
                    content = @Content(schema = @Schema(contentSchema = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User not updated - server error"
            )
    })
    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.updateUser(userDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete user",
            description = "Delete existed user",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User was deleted"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User not deleted - server error"
            )
    })
    @DeleteMapping("/{id}")
    public void deleteUser(@Parameter(required = true, description = "User ID") @PathVariable int id) {
        userService.deleteUser(id);
    }

    @Operation(
            summary = "Set or update user role",
            description = "Set or update user role",
            tags = {"role"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Role updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Role not updated - server error",
                    content = @Content
            )
    })
    @PostMapping("/updateRole")
    public ResponseEntity<UserDto> updateUserRole(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.updateUserRole(userDto), HttpStatus.OK);
    }
}
