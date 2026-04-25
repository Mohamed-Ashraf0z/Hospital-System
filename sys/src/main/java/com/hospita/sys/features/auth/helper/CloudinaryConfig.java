package com.hospita.sys.features.auth.helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

    @Autowired
    private CloudinaryProperties props;


@Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", props.getCloud_name(),
            "api_key", props.getApi_key(),
            "api_secret", props.getApi_secret()
        ));
    }
}