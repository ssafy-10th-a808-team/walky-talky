package com.ssafy.backend.member.service;

import com.ssafy.backend.common.service.S3UploadService;
import com.ssafy.backend.global.util.JwtProvider;
import com.ssafy.backend.global.util.RedisDao;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.request.RequestCheckIdDto;
import com.ssafy.backend.member.dto.request.RequestCheckNicknameDto;
import com.ssafy.backend.member.dto.request.RequestLocalLoginDto;
import com.ssafy.backend.member.dto.request.RequestLocalSignupDto;
import com.ssafy.backend.member.repository.MemberRepository;
import com.ssafy.backend.region.service.RegionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    //    private static final long atkExp = 900000L; // 15분
    private static final long atkExp = 604800000L; // 일주일
    private static final long rtkExp = 604800000L; // 일주일
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RedisDao redisDao;
    private final S3UploadService s3UploadService;
    private final RegionService regionService;
    @Value("${security.salt}")
    private String salt;

    private static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        String hashedPassword = null;

        // Salt와 비밀번호를 합침
        byte[] combined = concatenateByteArrays(password.getBytes(), salt);

        // SHA-256 해시 알고리즘 사용
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(combined);

        // 해시된 값을 Base64로 인코딩
        hashedPassword = Base64.getEncoder().encodeToString(md.digest());

        return hashedPassword;
    }

    private static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    @Override
    public boolean checkId(RequestCheckIdDto requestCheckIdDto) {
        return memberRepository.existsByMemberId(requestCheckIdDto.getId());
    }

    @Override
    public boolean checkNickname(RequestCheckNicknameDto requestCheckNicknameDto) {
        return memberRepository.existsByNickname(requestCheckNicknameDto.getNickname());
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public boolean localSignup(RequestLocalSignupDto requestLocalSignupDto) throws IOException, NoSuchAlgorithmException {

        Member member = requestLocalSignupDto.toEntity();


        // TODO : 비밀번호 패턴 매칭
        // 최소 10자 이상, 대문자, 소문자, 특수문자를 각각 1개 이상 포함하는 정규표현식
//        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{10,}$";
//
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(member.getPassword());
//
//        if (!matcher.matches()) {
//            return false;
//        }

        // TODO: 비밀번호 암호화
        String hashedPassword = hashPassword(member.getPassword(), salt.getBytes());

        member.setPassword(hashedPassword);

        Member savedMember = memberRepository.save(member);

        // if file data exist
        if (requestLocalSignupDto.getMultipartFile() != null && !requestLocalSignupDto.getMultipartFile().isEmpty()) {
            String tmpUrl = s3UploadService.uploadMemberProfileImg(requestLocalSignupDto.getMultipartFile(), savedMember.getSeq());
            savedMember.setUrl(tmpUrl);
        }

        memberRepository.save(savedMember);

        return true;

    }

    @Override
    public Map<String, Object> localLogin(RequestLocalLoginDto loginDto) throws NoSuchAlgorithmException {
        Member member = memberRepository.findByMemberId(loginDto.getMemberId());
        Map<String, Object> returnMap = new HashMap<>();

        if (member == null) { // 아이디에 해당하는 회원 없을때
            returnMap.put("message", "아이디 혹은 비밀번호를 확인해주세요.");
            return returnMap;
        } else {
            if (member.isDeleted()) { // 탈퇴한 회원일때
                returnMap.put("message", "아이디 혹은 비밀번호를 확인해주세요.");
                return returnMap;
            }
            if (!hashPassword(loginDto.getPassword(), salt.getBytes()).equals(member.getPassword())) { // 비번 틀렸을때
                returnMap.put("message", "아이디 혹은 비밀번호를 확인해주세요.");
                return returnMap;
            }
        }
        // jwt
        Long seq = member.getSeq();
        String atk = jwtProvider.createAccessToken(seq, atkExp);
        String rtk = jwtProvider.createRefreshToken(seq, rtkExp);

        // redis에 jwt저장
        redisDao.saveToRedis("atk:" + seq, atk, Duration.ofMillis(atkExp));
        redisDao.saveToRedis("rtk:" + seq, rtk, Duration.ofMillis(rtkExp));

        returnMap.put("atk", atk);
        returnMap.put("rtk", rtk);

        Map<String, String> data = new HashMap<>();
        data.put("nickname", member.getNickname());
        data.put("profileImage", member.getUrl());

        returnMap.put("data", data);

        return returnMap;
    }

    @Override
    public void logout(Long seq) {
        redisDao.deleteFromRedis("atk:" + seq);
        redisDao.deleteFromRedis("rtk:" + seq);
    }

    @Override
    public Map<String, String> reissue(Long seq) {
        Map<String, String> returnMap = new HashMap<>();

        String atk = jwtProvider.createAccessToken(seq, atkExp);
        String rtk = jwtProvider.createRefreshToken(seq, rtkExp);

        // redis에 jwt저장
        redisDao.saveToRedis("atk:" + seq, atk, Duration.ofMillis(atkExp));
        redisDao.saveToRedis("rtk:" + seq, rtk, Duration.ofMillis(rtkExp));

        returnMap.put("atk", atk);
        returnMap.put("rtk", rtk);

        return returnMap;
    }


}
