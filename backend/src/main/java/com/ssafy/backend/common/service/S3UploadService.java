package com.ssafy.backend.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ssafy.backend.global.error.WTException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadMemberProfileImg(MultipartFile multipartFile, Long memberSeq) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("member/").append(memberSeq).append("/").append("profile/").append(UUID.randomUUID());
        return upload(multipartFile, sb.toString());
    }

    public String uploadRecordImg(MultipartFile multipartFile, Long memberSeq, Long recordSeq) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("member/").append(memberSeq).append("/").append("record/").append(recordSeq).append("/").append(UUID.randomUUID());
        return upload(multipartFile, sb.toString());
    }

    public String uploadClubBoardImg(MultipartFile multipartFile, Long clubSeq, Long boardSeq) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("club/").append(clubSeq).append("/").append(boardSeq).append("/").append(UUID.randomUUID());
        return upload(multipartFile, sb.toString());
    }

    public String uploadClubProfileImg(MultipartFile multipartFile, Long clubSeq) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("club/").append(clubSeq).append("/").append("profile/").append(UUID.randomUUID());
        return upload(multipartFile, sb.toString());
    }


    public String upload(MultipartFile multipartFile, String filePath) throws IOException {
        String contentType = multipartFile.getContentType();

        verifyExtension(multipartFile);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        filePath = filePath + "." + contentType.substring(contentType.lastIndexOf('/') + 1);
        amazonS3.putObject(bucket, filePath, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, filePath).toString();
    }

    public void deleteImg(String url) {
        String filePath = url.substring(51);
        amazonS3.deleteObject(bucket, filePath);
    }

    public void verifyExtension(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();

        if (ObjectUtils.isEmpty(contentType) ||
                (!contentType.contains("image/jpeg")
                        && !contentType.contains("image/png")
                        && !contentType.contains("image/jpg"))) {
            throw new WTException("verifyException");
        }
    }
}
