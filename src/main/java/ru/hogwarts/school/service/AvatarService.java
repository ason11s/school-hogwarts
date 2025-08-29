package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Transactional
public class AvatarService {

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentRepository.findById(studentId).orElseThrow();

        Path filePath = Path.of(avatarsDir, student.getId() + "." + getExtension(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (var inputStream = avatarFile.getInputStream();
             var outputStream = Files.newOutputStream(filePath)) {
            inputStream.transferTo(outputStream);
        }

        Avatar avatar = findByStudentId(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }

    public Avatar findByStudentId(Long studentId) {
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    public Avatar findById(Long id) {
        return avatarRepository.findById(id).orElseThrow();
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
