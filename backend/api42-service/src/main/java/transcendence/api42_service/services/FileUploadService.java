package transcendence.api42_service.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import transcendence.api42_service.exception.DeleteDefaultException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.UUID;

@Getter
@Service
public class FileUploadService {

	@Value("${api42.upload-location}")
	private String uploadDir;

	@Value("${api42.default-banner}")
	private String defaultBanner;

	public String uploadImage(String userId, MultipartFile image, String fileExtension) throws IOException {
		if (uploadDir.isEmpty())
			throw new RuntimeException("No upload location or domain specified");
		Path uploadPath = Paths.get(uploadDir);
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		String fileName = userId + UUID.randomUUID() + fileExtension;
		Files.copy(image.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
		return fileName;
	}

	public void deleteImage(String imageName) throws IOException {
		if (imageName == null || imageName.isEmpty() || imageName.equals(defaultBanner))
			throw new DeleteDefaultException();
		Path imagePath = Paths.get(uploadDir).resolve(imageName).normalize();
		if (!imagePath.startsWith(Paths.get(uploadDir).toAbsolutePath())) {
			throw new SecurityException();
		}
		Files.deleteIfExists(imagePath);
	}

	public boolean isImage(MultipartFile file) {
		String[] allowedType = {"image/gif", "image/jpeg", "image/png", "image/webp"};
		String contentType = file.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) {
			return false;
		}
		if (Arrays.stream(allowedType).noneMatch(s -> s.equals(contentType))) {
			return false;
		}
		String fileName = file.getOriginalFilename();
		if (fileName == null) {
			return false;
		}
		try {
			BufferedImage image = ImageIO.read(file.getInputStream());
			if (image == null)
				return false;
		} catch(IOException e) {
			return false;
		}
		return true;
	}

	public String getFileExtension(MultipartFile file) {
		String[] allowedExtension = {".gif", ".jpeg", ".jpg", ".png", ".webp"};
		String fileName = file.getOriginalFilename();
		if (fileName == null || !fileName.contains(".")) {
			return null;
		}
		String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		if (Arrays.stream(allowedExtension).noneMatch(s -> s.equals(extension))) {
			return null;
		}
		return extension;
	}
}