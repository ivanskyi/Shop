package com.ivanskyi.controller;

import com.ivanskyi.dto.UserDataDTO;
import com.ivanskyi.dto.UserResponseDTO;
import com.ivanskyi.model.User;
import com.ivanskyi.service.impl.UserSecurityServiceImpl;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
@RequiredArgsConstructor
public class UserController {

  private static final int HTTP_CODE_400 = 400;
  private static final int HTTP_CODE_422 = 422;
  private static final int HTTP_CODE_403 = 403;
  private static final int HTTP_CODE_500 = 500;
  private static final int HTTP_CODE_404 = 404;
  private final UserSecurityServiceImpl userSecurityServiceImpl;
  private final ModelMapper modelMapper;

  @PostMapping("/signin")
  @ApiOperation(value = "Sign in a user")
  @ApiResponses(value = {
          @ApiResponse(code = HTTP_CODE_400, message = "Something went wrong"),
          @ApiResponse(code = HTTP_CODE_422, message = "Invalid username/password supplied")
  })
  public String login(@RequestParam final String username, @RequestParam final String password) {
    return userSecurityServiceImpl.signin(username, password);
  }

  @PostMapping("/signup")
  @ApiOperation(value = "Sign up a user")
  @ApiResponses(value = {
          @ApiResponse(code = UserController.HTTP_CODE_400, message = "Something went wrong"),
          @ApiResponse(code = HTTP_CODE_403, message = "Access denied"),
          @ApiResponse(code = UserController.HTTP_CODE_422, message = "Username is already in use")
  })
  public String signup(@RequestBody final UserDataDTO user) {
    return userSecurityServiceImpl.signup(modelMapper.map(user, User.class));
  }

  @DeleteMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_MANAGER')")
  @ApiOperation(value = "Delete a user", authorizations = {@Authorization(value = "apiKey")})
  @ApiResponses(value = {
          @ApiResponse(code = HTTP_CODE_400, message = "Something went wrong"),
          @ApiResponse(code = HTTP_CODE_403, message = "Access denied"),
          @ApiResponse(code = HTTP_CODE_404, message = "The user doesn't exist"),
          @ApiResponse(code = HTTP_CODE_500, message = "Expired or invalid JWT token")
  })
  public String delete(@PathVariable final String username) {
    userSecurityServiceImpl.delete(username);
    return username;
  }

  @GetMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_MANAGER')")
  @ApiOperation(value = "Search for a user", response = UserResponseDTO.class, authorizations = {@Authorization(value = "apiKey")})
  @ApiResponses(value = {
          @ApiResponse(code = HTTP_CODE_400, message = "Something went wrong"),
          @ApiResponse(code = HTTP_CODE_403, message = "Access denied"),
          @ApiResponse(code = HTTP_CODE_404, message = "The user doesn't exist"),
          @ApiResponse(code = HTTP_CODE_500, message = "Expired or invalid JWT token")
  })
  public UserResponseDTO search(@PathVariable final String username) {
    return modelMapper.map(userSecurityServiceImpl.search(username), UserResponseDTO.class);
  }

  @GetMapping("/me")
  @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_CLIENT')")
  @ApiOperation(value = "Get current user", response = UserResponseDTO.class, authorizations = {@Authorization(value = "apiKey")})
  @ApiResponses(value = {
          @ApiResponse(code = HTTP_CODE_400, message = "Something went wrong"),
          @ApiResponse(code = HTTP_CODE_403, message = "Access denied"),
          @ApiResponse(code = HTTP_CODE_500, message = "Expired or invalid JWT token")
  })
  public UserResponseDTO getInformationAboutCurrentUser(final HttpServletRequest request) {
    return modelMapper.map(userSecurityServiceImpl.getInformationAboutCurrentUser(request), UserResponseDTO.class);
  }

  @GetMapping("/refresh")
  @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_CLIENT')")
  public String refresh(final HttpServletRequest request) {
    return userSecurityServiceImpl.refresh(request.getRemoteUser());
  }
}
