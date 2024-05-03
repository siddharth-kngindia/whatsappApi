package com.api.whatsapp.service;

import com.api.whatsapp.model.Profile;
import com.api.whatsapp.model.User;
import com.api.whatsapp.repository.ProfileRepository;
import com.api.whatsapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    public User updateProfile(User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setProfilePicture(user.getProfilePicture());
        existingUser.setAbout(user.getAbout());
        return userRepository.save(existingUser);
    }

    public Profile getProfileById(Long id) {
        Optional<Profile> profileOptional = profileRepository.findById(id);
        return profileOptional.orElse(null);
    }
    private String updateUserCoverImagePath(String username, String filePath) {

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found!");
        }
        User user = userOptional.get();

        CoverImage coverImage = user.getCoverImage();
        if (coverImage == null) {
            coverImage = new CoverImage();
            user.setCoverImage(coverImage);
            coverImage.setUser(user);
        }
        coverImage.setImageUrl(filePath);
        userRepository.save(user); // Save the user with the new cover image path
        return  filePath;

    }

    public String getPresignedUrlForProfilePicture(String username, String objectKey) {

        try (S3Presigner presigner = S3Presigner.create()) {
            String fileName = bucketDirectory+updateUserCoverImagePath(username, objectKey);
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10)) // URL expires in 10 minutes
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            String presignedUrl = presignedRequest.url().toExternalForm();
            logger.info("Presigned URL: {}", presignedUrl);
            logger.info("HTTP method: {}", presignedRequest.httpRequest().method());

            return presignedUrl;
        } catch (S3Exception e) {
            logger.error("Failed to generate presigned URL", e);
            throw new RuntimeException("Failed to generate presigned URL", e);
        }

    }

    public byte[] getProfileImageBytes(String username,String objectKey) {

        String fileName = bucketDirectory+updateUserCoverImagePath(username, objectKey);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        ResponseBytes<GetObjectResponse> objectBytes = this.s3Client.getObjectAsBytes(getObjectRequest);
        return objectBytes.asByteArray();

    }

    public boolean isValidFile(MultipartFile file) {

        List<String> allowedContentTypes = List.of("image/jpeg", "image/png", "image/gif", "image/webp");
        final long maxSizeInBytes = 2*5 * 1024 * 1024; // 5 MB
        String contentType = file.getContentType();
        long size = file.getSize();
        return allowedContentTypes.contains(contentType) && size <= maxSizeInBytes;

    }

    public byte[] compressImage(MultipartFile file) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(1024, 768) // New size
                .outputFormat("JPEG") // Change as needed
                .outputQuality(0.75) // Adjust the quality
                .toOutputStream(outputStream);
        return outputStream.toByteArray();

    }


}
