package com.api.whatsapp.controller;

import com.api.whatsapp.model.Profile;
import com.api.whatsapp.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;


    @PutMapping("/update")
    public ResponseEntity<User> updateProfile(@RequestBody User user) {
        User updatedUser = profileService.updateProfile(user);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<Profile> getMyProfile() {
        Long userId = 1L; // Replace this with the actual user ID from the authentication process
        Profile profile = profileService.getProfileById(userId);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());

        Authentication authenticated = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        String token = tokenProvider.createToken(authenticated);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/profile-picture-fetch")
    public void getProfilePicture(@RequestParam String username, HttpServletResponse response) {

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            CoverImage coverImage = coverImageRepository.findByUser(user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CoverImage not found for username: " + username));

            String objectKey = coverImage.getImageUrl();
            String presignedUrl = userProfileService.getPresignedUrlForProfilePicture(user.getUsername(), objectKey);
            log.info("Presigned URL: {}", presignedUrl);
            log.info("Object key: {}", objectKey);
            byte[] imageBytes = userProfileService.getProfileImageBytes(user.getUsername(),objectKey);
            response.setContentType("image/jpeg");
            response.getOutputStream().write(imageBytes);
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error writing image to response", e);
        }

    }

}