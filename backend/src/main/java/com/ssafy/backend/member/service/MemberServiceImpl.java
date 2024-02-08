package com.ssafy.backend.member.service;

import com.ssafy.backend.common.service.S3UploadService;
import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.global.util.JwtProvider;
import com.ssafy.backend.global.util.RedisDao;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.mapping.NicknameUrlMapping;
import com.ssafy.backend.member.dto.mapping.RegionCdMapping;
import com.ssafy.backend.member.dto.mapping.StreakMapping;
import com.ssafy.backend.member.dto.request.*;
import com.ssafy.backend.member.dto.response.*;
import com.ssafy.backend.member.repository.MemberRepository;
import com.ssafy.backend.member.repository.StreakRepository;
import com.ssafy.backend.region.repository.RegionRepository;
import com.ssafy.backend.region.service.RegionService;
import com.ssafy.backend.shareBoard.dto.response.ResponseMemberDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    private final RegionRepository regionRepository;
    private final RegionService regionService;
    private final StreakRepository streakRepository;


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
    public ResponseEntity<ResponseCheckIdDto> memberCheckId(RequestCheckIdDto requestCheckIdDto) {

        ResponseCheckIdDto responseCheckIdDto;

        if (memberRepository.existsByMemberId(requestCheckIdDto.getId())) {
            responseCheckIdDto = ResponseCheckIdDto.builder()
                    .message("중복된 아이디입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCheckIdDto);
        } else {
            responseCheckIdDto = ResponseCheckIdDto.builder()
                    .message("OK")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(responseCheckIdDto);
        }
    }

    @Override
    public ResponseEntity<ResponseCheckNicknameDto> memberCheckNickname(RequestCheckNicknameDto requestCheckNicknameDto) {

        ResponseCheckNicknameDto responseCheckNicknameDto;

        if (memberRepository.existsByNickname(requestCheckNicknameDto.getNickname())) {
            responseCheckNicknameDto = ResponseCheckNicknameDto.builder()
                    .message("중복된 닉네임입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCheckNicknameDto);
        } else {
            responseCheckNicknameDto = ResponseCheckNicknameDto.builder()
                    .message("OK")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(responseCheckNicknameDto);
        }
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public ResponseEntity<ResponseLocalSignupDto> memberLocalSignup(RequestLocalSignupDto requestLocalSignupDto) throws IOException, NoSuchAlgorithmException {

        ResponseLocalSignupDto responseLocalSignupDto;

        String loginType = requestLocalSignupDto.getLoginType();
        Member member = requestLocalSignupDto.toEntity();
        if (loginType != null) {
            member.setLoginType(loginType);
        }

        // 중복된 아이디입니다.
        if (memberRepository.existsByMemberId(member.getMemberId())) {
            responseLocalSignupDto = ResponseLocalSignupDto.builder()
                    .message("중복된 아이디입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseLocalSignupDto);
        }

        // 비밀번호 패턴 매칭 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용하세요.
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(member.getPassword());

        if ("local".equals(member.getLoginType()) && !matcher.matches()) {
            responseLocalSignupDto = ResponseLocalSignupDto.builder()
                    .message("비밀번호 패턴 매칭 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용하세요.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseLocalSignupDto);
        }

        // 올바르지 않은 생일입니다.
        regex = "\\d{8}";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(member.getBirth());

        if (!matcher.matches()) {
            responseLocalSignupDto = ResponseLocalSignupDto.builder()
                    .message("올바르지 않은 생일입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseLocalSignupDto);
        }

        // 올바르지 않은 성별입니다.
        if (!(member.getGender().equals("M") || member.getGender().equals("F"))) {
            responseLocalSignupDto = ResponseLocalSignupDto.builder()
                    .message("올바르지 않은 성별입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseLocalSignupDto);
        }
        // 중복된 닉네임입니다.
        if (memberRepository.existsByNickname(member.getNickname())) {
            responseLocalSignupDto = ResponseLocalSignupDto.builder()
                    .message("중복된 닉네임입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseLocalSignupDto);
        }
        // 올바르지 않은 지역입니다.
        if (!regionRepository.existsByRegionCd(member.getRegionCd())) {
            responseLocalSignupDto = ResponseLocalSignupDto.builder()
                    .message("올바르지 않은 지역입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseLocalSignupDto);
        }

        // correct member

        if ("local".equals(member.getLoginType())) {
            // 비밀번호 암호화
            String hashedPassword = hashPassword(member.getPassword(), salt.getBytes());
            member.setPassword(hashedPassword);
        }

        // save
        Member savedMember = memberRepository.save(member);

        // TEST CODE
        if (requestLocalSignupDto.getMultipartFile() == null) {
            System.out.println("requestLocalSignupDto.getMultipartFile() == null");
        } else if (requestLocalSignupDto.getMultipartFile().isEmpty()) {
            System.out.println("requestLocalSignupDto.getMultipartFile().isEmpty()");
        }

        if (requestLocalSignupDto.getMultipartFile() == null || requestLocalSignupDto.getMultipartFile().isEmpty()) {
            // 로컬 회원가입 사진 등록 안 함
            if(member.getUrl() == null || member.getUrl().isEmpty()){
                savedMember.setUrl("https://walkytalky.s3.ap-northeast-2.amazonaws.com/member/38/profile/6305875e-9599-427a-acf5-19a231bce852.png");
            }
        }
        else{
            String tmpUrl = s3UploadService.uploadMemberProfileImg(requestLocalSignupDto.getMultipartFile(), savedMember.getSeq());
            savedMember.setUrl(tmpUrl);
        }

        // reSave
        memberRepository.save(savedMember);

        responseLocalSignupDto = ResponseLocalSignupDto.builder()
                .message("OK")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseLocalSignupDto);

    }

    @Override
    public Map<String, Object> localLogin(RequestLocalLoginDto loginDto) throws WTException {
        Map<String, Object> data = new HashMap<>();

        if (loginDto.getMemberId() == null || loginDto.getPassword() == null) {
            throw new WTException("아이디 혹은 비밀번호를 확인해주세요.");
        }

        Member member = memberRepository.findByMemberId(loginDto.getMemberId());

        try {
            if (member == null) { // 아이디에 해당하는 회원 없을때
                throw new WTException("아이디 혹은 비밀번호를 확인해주세요.");
            } else {
                if (member.getIsDeleted()) { // 탈퇴한 회원일때
                    throw new WTException("아이디 혹은 비밀번호를 확인해주세요.");
                }
                if (!hashPassword(loginDto.getPassword(), salt.getBytes()).equals(member.getPassword())) { // 비번 틀렸을때
                    throw new WTException("아이디 혹은 비밀번호를 확인해주세요.");
                }
            }
        } catch (Exception e) {
            throw new WTException("아이디 혹은 비밀번호를 확인해주세요.");
        }

        try {
            // jwt
            Long seq = member.getSeq();
            String atk = jwtProvider.createAccessToken(seq, atkExp);
            String rtk = jwtProvider.createRefreshToken(seq, rtkExp);

            // redis에 jwt저장
            redisDao.saveToRedis("atk:" + seq, atk, Duration.ofMillis(atkExp));
            redisDao.saveToRedis("rtk:" + seq, rtk, Duration.ofMillis(rtkExp));

            data.put("atk", atk);
            data.put("rtk", rtk);

            data.put("nickname", member.getNickname());
            data.put("profileImage", member.getUrl());

            return data;
        } catch (Exception e) {
            throw new WTException("로그인에 실패하였습니다.");
        }
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

    @Override
    public String getRegionCd(Long memberSeq) throws WTException {
        RegionCdMapping regionCdMapping = memberRepository.findRegionCdBySeq(memberSeq);

        if (regionCdMapping == null) {
            throw new WTException("사용자의 동네를 확인해주세요.");
        }

        return regionCdMapping.getRegionCd();
    }

    @Override
    public List<Long> getSimilarMemberList(Long memberSeq) throws WTException {
        Optional<Member> memberOptional = memberRepository.findById(memberSeq);
        if (memberOptional.isEmpty()) {
            throw new WTException("사용자 정보가 없습니다.");
        }

        Member member = memberOptional.get();
        try {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int birthYear = Integer.parseInt(member.getBirth().substring(0, 4));
            int age = currentYear - birthYear + 1;

            return memberRepository.findSeqInAgeAndGender(memberSeq, (age / 10) * 10, (age / 10) * 10 + 9, member.getGender());
        } catch (Exception e) {
            throw new WTException("사용자 불러오기에 실패했습니다.");
        }
    }

    @Override
    public ResponseMypageDto mypage(Long memberSeq) throws WTException {
        Optional<Member> memberOptional = memberRepository.findById(memberSeq);

        if (memberOptional.isEmpty()) {
            throw new WTException("마이페이지 불러오기에 실패하였습니다.");
        }

        Member member = memberOptional.get();

        ResponseMypageDto responseMypageDto = new ResponseMypageDto();

        try {
            responseMypageDto.setNickname(member.getNickname());
            responseMypageDto.setMemberId(member.getMemberId());
            responseMypageDto.setAddress(regionService.findAddress(member.getRegionCd()));

            responseMypageDto.setRegionCd(member.getRegionCd());

            responseMypageDto.setBirth(member.getBirth());
            responseMypageDto.setGender(member.getGender());
            responseMypageDto.setProfileImage(member.getUrl());
            responseMypageDto.setIntroduce(member.getIntroduce());
        } catch (Exception e) {
            throw new WTException("마이페이지 불러오기에 실패하였습니다.");
        }
        return responseMypageDto;
    }

    @Override
    public List<StreakMapping> myStreak(Long memberSeq) throws WTException {
        try {
            return streakRepository.findAllByMemberSeq(memberSeq);
        } catch (Exception e) {
            throw new WTException("산책 스트릭 불러오기에 실패하였습니다.");
        }
    }

    @Override
    public void modifyInfo(Long memberSeq, RequestModifyInfoDto requestModifyInfoDto) throws WTException {
        Optional<Member> memberOptional = memberRepository.findById(memberSeq);

        if (memberOptional.isEmpty()) {
            throw new WTException("회원 정보 수정에 실패하였습니다.");
        }

        Member member = memberOptional.get();

        if (requestModifyInfoDto.getProfileImage() != null) {
            s3UploadService.deleteImg(member.getUrl());

            String url;
            try {
                url = s3UploadService.uploadMemberProfileImg(requestModifyInfoDto.getProfileImage(), memberSeq);
            } catch (Exception e) {
                throw new WTException("사진 업로드에 실패하였습니다.");
            }
            member.update(requestModifyInfoDto.getRegionCd(), requestModifyInfoDto.getIntroduce(), requestModifyInfoDto.getNickname(), url);
        } else {
            member.update(requestModifyInfoDto.getRegionCd(), requestModifyInfoDto.getIntroduce(), requestModifyInfoDto.getNickname());
        }

        memberRepository.save(member);
    }

    @Override
    public void modifyPassword(Long memberSeq, RequestModifyPasswordDto requestModifyPasswordDto) throws WTException {
        Optional<Member> memberOptional = memberRepository.findById(memberSeq);

        if (memberOptional.isEmpty()) {
            throw new WTException("비밀번호 변경에 실패하였습니다.");
        }

        Member member = memberOptional.get();

        try {
            if (!hashPassword(requestModifyPasswordDto.getPassword(), salt.getBytes()).equals(member.getPassword())) { // 현재 비번 틀렸을때
                throw new WTException("현재 비밀번호를 확인해주세요.");
            }
        } catch (Exception e) {
            throw new WTException(e.getMessage());
        }

        // 비밀번호 패턴 매칭 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용하세요.
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(requestModifyPasswordDto.getNewPassword());

        if (!matcher.matches()) {
            throw new WTException("비밀번호 패턴 매칭 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용하세요.");
        }

        if (Objects.equals(requestModifyPasswordDto.getNewPassword(), requestModifyPasswordDto.getPassword())) {
            throw new WTException("이전과 동일한 비밀번호로 변경할 수 없습니다.");
        }

        if (!Objects.equals(requestModifyPasswordDto.getNewPassword(), requestModifyPasswordDto.getCheckNewPassword())) {
            throw new WTException("새로운 비밀번호를 다시 확인해주세요.");
        }

        try {
            member.update(hashPassword(requestModifyPasswordDto.getNewPassword(), salt.getBytes()));
        } catch (Exception e) {
            throw new WTException("비밀번호 변경에 실패하였습니다.");
        }

        memberRepository.save(member);
    }

    @Override
    public void delete(Long memberSeq, RequestDeleteDto requestDeleteDto) throws WTException {
        Optional<Member> memberOptional = memberRepository.findById(memberSeq);

        if (memberOptional.isEmpty()) {
            throw new WTException("회원 탈퇴에 실패하였습니다.");
        }

        Member member = memberOptional.get();

        try {
            if (!hashPassword(requestDeleteDto.getPassword(), salt.getBytes()).equals(member.getPassword())) { // 비번 틀렸을때
                throw new WTException("비밀번호를 확인해주세요.");
            }
        } catch (Exception e) {
            throw new WTException(e.getMessage());
        }

        member.delete();
        memberRepository.save(member);
    }

    public ResponseMemberDto getMemberNicknameUrl(Long memberSeq) {
        ResponseMemberDto responseMemberDto = new ResponseMemberDto();

        NicknameUrlMapping m = memberRepository.findNickNameAndUrlBySeq(memberSeq);
        responseMemberDto.setNickname(m.getNickname());
        responseMemberDto.setProfilePic(m.getUrl());

        return responseMemberDto;
    }

}
