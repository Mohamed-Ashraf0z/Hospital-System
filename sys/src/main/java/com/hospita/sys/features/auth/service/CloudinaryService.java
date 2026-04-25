package com.hospita.sys.features.auth.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hospita.sys.features.auth.entity.Doctor;
import com.hospita.sys.features.auth.repo.DoctorRepository;

import io.jsonwebtoken.io.IOException;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private DoctorRepository doctorRepository;

    public String uploadFile(MultipartFile file) throws java.io.IOException {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.emptyMap()
            );
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload failed");
        }
    }


public void uploadCertificates(
        Long id,
        List<MultipartFile> files) {

    Doctor doctor = new Doctor();
    doctor.setId(id);

    List<String> urls = files.stream()
        .map(new Function<MultipartFile, String>() {
        @Override
        public String apply(MultipartFile file) {
            try {
                return uploadFile(file);
            } catch (java.io.IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    })
        .toList();

    doctor.setCertificates(urls);
    doctorRepository.save(doctor);

    // return ResponseEntity.ok(
    //     new ApiResponse(
    //         true,
    //         "Certificates uploaded successfully",
    //         null,
    //         null
    //     )
    // );
}

public Doctor getDoctor(Long id) {
    return doctorRepository.findById(id).orElseThrow();
}


}