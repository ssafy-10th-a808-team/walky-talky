# WalkyTalky

<div align=center>
<img src="images/MainPage.png" height=600>
</div>

## :book: 목차
- [프로젝트 소개](#walking-프로젝트-소개)
- [팀원 구성](#construction_worker-팀원-구성)
- [개발 기간](#calendar-개발-기간)
- [기술 스택](#hammer_and_wrench-기술-스택)
- [프로젝트 구조](#package-프로젝트-구조)
- [아키텍처](#building_construction-아키텍처)
- [문서](#memo-문서)
- [역할 분담](#pushpin-역할-분담)
- [기능 시연](#movie_camera-기능-시연)
- [구동 방법](#computer-구동-방법)

## :walking: 프로젝트 소개

- WalkyTalky는 산책을 좋아하는 사람들이 본 서비스를 이용하여 산책을 한 뒤 자신의 산책 기록을 공유하고 같이 산책 할 사람들을 구하여 산책을 할 수 있는 서비스입니다.
- 사용자가 산책을 시작하면 실시간으로 지도에 산책 경로가 표시되며 산책을 마친 후에는 자신의 산책의 제목, 한줄평, 별점을 기록하고 확인 할 수 있습니다.
- 다른 사용자와 산책을 하고 싶은 사용자는 소모임을 모집하여 산책 일정을 관리하고 이야기를 나눌 수 있습니다.
- 자신의 산책 기록을 다른 사용자와 공유하고 싶은 사용자는 산책 기록 공유 게시판을 통해 다른 사용자들과 자신의 기록을 공유할 수 있습니다.
- 지도를 통해 원하는 사용자의 산책 기록을 확인하며 산책 할 수 있습니다.

## :construction_worker: 팀원 구성

|                                   김진용                                   |                                   강민정                                   |                                   김연화                                    |                                   신우섭                                   |                                   임덕기                                    |
| :------------------------------------------------------------------------: | :------------------------------------------------------------------------: | :-------------------------------------------------------------------------: | :------------------------------------------------------------------------: | :-------------------------------------------------------------------------: |
| <img src="https://avatars.githubusercontent.com/u/88269663?v=4" width=150> | <img src="https://avatars.githubusercontent.com/u/57582487?v=4" width=150> | <img src="https://avatars.githubusercontent.com/u/124342362?v=4" width=150> | <img src="https://avatars.githubusercontent.com/u/87111673?v=4" width=150> | <img src="https://avatars.githubusercontent.com/u/130431922?v=4" width=150> |
|               [@jinyong3512](https://github.com/jinyong3512)               |                    [@min922](https://github.com/min922)                    |                 [@myeon0109](https://github.com/myeon0109)                  |                 [@Wooseobee](https://github.com/Wooseobee)                 |                    [@DKIMDK](https://github.com/DKIMDK)                     |

## :calendar: 개발 기간

2024.01.08 - 2024.02.16 (6주)

## :hammer_and_wrench: 기술 스택

#### Front-end

<img alt="JavaScript" src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white">
<img alt="vue.js" src="https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white">
<img alt="bootstrap" src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">

#### Back-end

<img alt="Java" src ="https://img.shields.io/badge/Java-007396.svg?&style=for-the-badge&logo=Java&logoColor=white"/>
<img alt="Spring Boot" src ="https://img.shields.io/badge/Spring Boot-6DB33F.svg?&style=for-the-badge&logo=Spring Boot&logoColor=white"/>
<img alt="Redis" src ="https://img.shields.io/badge/Redis-DC382D.svg?&style=for-the-badge&logo=Redis&logoColor=white"/>

#### Database

<img alt="mariadb" src ="https://img.shields.io/badge/mariadb-003545.svg?&style=for-the-badge&logo=mariadb&logoColor=white"/>

#### 버전 및 이슈관리

<img alt="gitlab" src ="https://img.shields.io/badge/gitlab-FC6D26.svg?&style=for-the-badge&logo=gitlab&logoColor=white"/>

#### 협업 툴

<img alt="discord" src ="https://img.shields.io/badge/discord-5865F2.svg?&style=for-the-badge&logo=discord&logoColor=white"/>
<img alt="jira" src ="https://img.shields.io/badge/jira-0052CC.svg?&style=for-the-badge&logo=jira&logoColor=white"/>
<img alt="notion" src ="https://img.shields.io/badge/notion-000000.svg?&style=for-the-badge&logo=notion&logoColor=white"/>
<img alt="mattermost" src ="https://img.shields.io/badge/mattermost-0058CC.svg?&style=for-the-badge&logo=mattermost&logoColor=white"/>

#### 서비스 배포 환경

<img alt="AWS Lightsail" src ="https://img.shields.io/badge/AWS Lightsail-FF9900.svg?&style=for-the-badge&logo=amazonaws&logoColor=white"/>
<img alt="amazons3" src ="https://img.shields.io/badge/amazons3-569A31.svg?&style=for-the-badge&logo=amazons3&logoColor=white"/>
<img alt="docker" src ="https://img.shields.io/badge/docker-2496ED.svg?&style=for-the-badge&logo=docker&logoColor=white"/>
<img alt="nginx" src ="https://img.shields.io/badge/nginx-009639.svg?&style=for-the-badge&logo=nginx&logoColor=white"/>

#### CI/CD

<img alt="jenkins" src ="https://img.shields.io/badge/jenkins-D24939.svg?&style=for-the-badge&logo=jenkins&logoColor=white"/>

## :package: 프로젝트 구조

<details>
<summary>Frontend</summary>

```
└──src
    ├──assets
    │   └──img
    │
    ├──components
    │   ├──common
    │   │   ├──ButtonWithIcon
    │   │   ├──ClubDetailHeaderNav
    │   │   ├──TheFooter
    │   │   ├──TheHeaderNav
    │   │   ├──CourseFeed
    │   │   └──WalkHeaderNav
    │   ├──chat
    │   │   ├──Message
    │   │   └──MessageList
    │   ├──club
    │   │   └──ClubList
    │   ├──member
    │   │   └──MemberListView
    │   ├──walk
    │   │   ├──StarRating
    │   │   ├──StopWatch
    │   │   └──RecordModifyModal
    │   └──shareboard
    │       ├──shareBoardComment
    │       ├──shareBoardCommentForm
    │       ├──shareBoardLike
    │       ├──shareBoardListItem
    │       ├──shareBoardListUpper
    │       ├──shareBoardMember
    │       ├──shareBoardRecord
    │       ├──shareBoardScrap
    │       └──shareBoardTitle
    │
    ├──router
    │   └──index
    │
    ├──stores
    │   ├──chat
    │   ├──jwtFilter
    │   ├──counter
    │   ├──club
    │   ├──member
    │   ├──shareboard
    │   └──walk
    │
    ├──views
    │   ├──club
    │   │   ├──ClubView
    │   │   ├──ClubDetailView
    │   │   ├──ClubCreateView
    │   │   ├──ClubPlanDetail
    │   │   ├──ClubPlanOverwrite
    │   │   ├──ClubPlanRegist
    │   │   ├──ClubPlanView
    │   │   ├──ClubSettingApplicantView
    │   │   ├──ClubSettingClubView
    │   │   ├──ClubSettingHeaderNav
    │   │   ├──ClubSettingMemberView
    │   │   └──ClubChatView
    │   ├──member
    │   │   ├──Login
    │   │   ├──Logout
    │   │   ├──ModifyInfo
    │   │   ├──MyLocationView
    │   │   ├──Mypage
    │   │   ├──RecordScrapList
    │   │   └──Signup
    │   ├──shareboard
    │   │   ├──ShareBoardView
    │   │   ├──ShareBoardDetailView
    │   │   ├──ShareBoardModifyView
    │   │   └──ShareBoardWriteView
    │   ├──walk
    │   │   ├──DoWalk
    │   │   ├──RecommendView
    │   │   ├──ScrapDetaillView
    │   │   ├──ScrapListView
    │   │   ├──WalkDetaillView
    │   │   └──WalkList
    │   └──Homewview

```

</details>
<details>
<summary>Backend</summary>

```
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── ssafy
    │   │           └── backend
    │   │               ├── BackendApplication.java
    │   │               ├── member
    │   │               │   ├── controller
    │   │               │   ├── domain
    │   │               │   ├── dto
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── group
    │   │               │   ├── controller
    │   │               │   ├── domain
    │   │               │   ├── dto
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── record
    │   │               │   ├── controller
    │   │               │   ├── domain
    │   │               │   ├── dto
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── shareBoard
    │   │               │   ├── controller
    │   │               │   ├── domain
    │   │               │   ├── dto
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── shareBoardComment
    │   │               │   ├── controller
    │   │               │   ├── domain
    │   │               │   ├── dto
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── global
    │   │               │   ├── config
    │   │               │   │   ├── SwaggerConfig.java
    │   │               │   │   ├── properties
    │   │               │   │   ├── resttemplate
    │   │               │   │   └── security
    │   │               │   ├── error
    │   │               │   │   ├── ErrorResponse.java
    │   │               │   │   ├── GlobalExceptionHandler.java
    │   │               │   │   └── exception
    │   │               │   └── util
    │   └── resources
    │       └── application.yml

```

</details>

## :building_construction: 아키텍처

<img src="images/architecture.png">

## :memo: 문서

- [ERD](https://www.erdcloud.com/d/EQGT5edvcxBBzAcPR)
- [API 명세서](https://www.notion.so/ee47fd310c7b40d4b581b4f313514baa?pvs=21)
- [Figma](https://www.figma.com/file/QcdcQyWpdbUQMWfj0cwQ5A/WalkyTalky?type=design&node-id=0-1&mode=design&t=4vfgV5hQb4Y1JbKe-0)
- [코드 및 커밋 컨벤션](https://www.notion.so/Git-bfca2331473a433a8ff5c9eeb0d76bfb?pvs=21)

## :pushpin: 역할 분담

- 신우섭
  - Infra
    - 서버 구축
    - Jenkins CI/CD 구축
  - UI
    - 카카오 로그인
    - 소모임 채팅
  - 기능
    - Oauth - 카카오 로그인 구현
    - Chatting - 소모임 가입자들간의 채팅 구현
    - S3 Image Upload 구현
- 임덕기
  - UI
    - 홈
    - 로그인, 로그아웃
    - 마이페이지 조회 및 수정
    - 내 위치 주소 불러오기
    - 네비게이션 바
  - 기능
    - KaKaoMap API (Geolocation)
    - Router guard
    - 로그인, 로그아웃, 회원 탈퇴
- 강민정
  - UI
    - 산책 기록 공유 게시판 게시글 작성, 수정, 상세 조회, 목록 조회, 삭제
    - 내 기록 목록 조회, 상세 조회
    - 스크랩 한 기록 목록 조회, 상세 조회
    - 추천 기록 목록 조회
    - 기록 따라 뛰기
  - 기능
    - 로컬 로그인 및 로그아웃 API
    - jwt 사용자 인증 로직 구현 - Frontend, Backend
    - 회원 정보 조회 API
    - 비밀번호 수정 API
    - 회원 정보 수정 API
    - 회원 탈퇴 API
    - 산책 기록 관련 API
    - 산책 기록 추천 관련 API
    - 산책 기록 스크랩 관련 API
    - 산책 기록 공유 게시판, 댓글, 좋아요 관련 API
- 김연화
  - UI
    - 산책기록 페이지
    - 회원가입(로컬)
  - 기능
    - 산책 시작하기
    - 산책 일시정지
    - 산책 중단
    - 산책 기록하기
    - 로컬 회원가입
    - 회원가입 정보(아이디, 닉네임, 비밀번호) 유효성 검사
- 김진용
  - UI
    - 소모임 페이지
  - 기능
    - 회원가입 API
      - SHA-256 비밀번호 암호화, 비밀번호 패턴 매칭, 멤버 정보 유효성 검사
    - 소모임 API
    - 소모임 멤버 API
    - 소모임 일정 API

## :movie_camera: 기능 시연

<details>
<summary>카카오 로그인</summary>
  <img src="images/kakaoLogin.gif">
</details>

<details>
<summary>채팅</summary>

- 소모임 가입, 탈퇴시 채팅방 알림 구현
- 소모임원들만 채팅방에 접근할 수 있게 제한
- 가입 이전의 내용은 확인 불가

  <img src="images/chatting.gif">
  </details>

<details>
<summary>로컬 회원가입 및 로그인</summary>

- 카카오 맵 api의 geolocation을 활용, gps상의 좌표로 주소를 가져와 내 동네 저장
- 닉네임, 아이디 중복 등 유효성검사
- 로그인 시 네비게이션바에 프로필 사진과 닉네임, 마이페이지 및 로그아웃 항목 렌더링

  <img src="images/member/local_signup.gif" height=600>
</details>

<details>
<summary>라우터가드</summary>

- 비 로그인 상태일 때 클럽, 산책 등 주소로 접근 방지
- 로그인 상태일 때 로그인, 회원가입 주소로 접근 방지

  <img src="images/member/login_logout.gif" height=600>
</details>

<details>
<summary>마이페이지</summary>

- 내 정보 렌더링
- 프로필 사진, 닉네임, 소개, 내 동네, 비밀번호 수정 가능
- 유효성검사 시행
- 회원탈퇴 기능

  <img src="images/member/mypage.gif/" height=600>
</details>

<details>
<summary>산책하기</summary>

- 사용자의 산책을 GPS와 KakaoMap을 이용해 실시간으로 추적하여 거리, 시간을 측정하고, 경로를 지도에 그린다.
- 산책 종료 시 기록을 DB에 저장한다

<img src="images/산책/산책하기.gif" height=600><img src="images/산책/산책기록입력.gif" height=600>

</details>

<details>
<summary>따라 산책하기</summary>

- 따라 산책
- 따라 걸을 라인(빨간색)이 뜨고, 내가 걷는 라인(노란색)도 역시 보여짐
- 따라 산책후 기록 입력

<img src="images/산책/따라걷기.gif" height=600> &nbsp; <img src="images/산책/따라걷고산책기록입력.gif" height=600>

</details>

<details>
<summary>산책 기록 공유 게시판</summary>

- 산책 기록 공유 게시글 목록 조회

  <img src="images/산책공유게시글/산책기록공유게시판목록조회.gif" height=600>

- 산책 기록 공유 게시글 작성

  <img src="images/산책공유게시글/산책공유게시글작성.gif" height=600>

  - 제목이나 내용을 입력하지 않거나 공유 할 기록을 선택하지 않으면 글을 작성하지 못함

    <img src="images/산책공유게시글/산책공유게시글작성오류.gif" height=600>

</details>

<details>
<summary>산책 기록 공유 게시글 상세 조회</summary>
  <img src="images/산책공유게시글/산책공유게시글상세조회.gif" height=600>

- 산책 기록 공유 게시글 수정

  <img src="images/산책공유게시글/산책공유게시글수정.gif" height=600>

- 산책 기록 공유 게시글 삭제

  <img src="images/산책공유게시글/산책공유게시글삭제.gif" height=600>

- 산책 기록 공유 게시글 댓글 작성

  <img src="images/산책공유게시글댓글/산책공유게시글댓글작성.gif" height=600>

- 산책 기록 공유 게시글 댓글 수정

  <img src="images/산책공유게시글댓글/댓글수정.gif" height=600>

- 산책 기록 공유 게시글 댓글 삭제

  <img src="images/산책공유게시글댓글/댓글삭제.gif" height=600>

- 산책 기록 공유 게시글 좋아요

  <img src="images/좋아요/좋아요.gif" height=600>

  - 자신의 글은 좋아요를 누를 수 없음

  <img src="images/좋아요/좋아요오류.gif" height=600>

- 산책 기록 공유 게시글 좋아요 취소

  <img src="images/좋아요/좋아요취소.gif" height=600>

</details>

<details>
<summary>산책 기록 스크랩</summary>

- 산책 기록 공유 게시글 스크랩 및 확인

  <img src="images/스크랩/스크랩후확인.gif" height=600>

  - 자신의 글은 스크랩 할 수 없음

    <img src="images/스크랩/스크랩오류.gif" height=600>

- 산책 기록 공유 게시글 스크랩 취소

  <img src="images/스크랩/스크랩취소.gif" height=600>

- 스크랩 한 코스 상세 보기

  <img src="images/스크랩/스크랩한코스상세보기.gif" height=600>

</details>

<details>
<summary>나의 산책 기록</summary>

- 내 기록 목록 조회

  <img src="images/내기록/내기록목록조회.gif" height=600>

- 내 기록 상세 조회

  <img src="images/내기록/내기록상세.gif" height=600>

- 내 기록 수정

  <img src="images/내기록/내기록수정.gif" height=600>

- 내 기록 삭제

  <img src="images/내기록/내기록삭제.gif" height=600>

</details>

<details>
<summary>산책 기록 추천</summary>

- 사용자의 동네에 기반하여 해당 동네의 산책 기록을 필터링 하여 보여줌

  <img src="images/산책추천/동네기반추천.gif" height=600>

- 사용자의 정보에 기반하여 유사한 사용자의 산책 기록을 필터링 하여 보여줌

  <img src="images/산책추천/정보기반추천.gif" height=600>

- 추천 받은 기록이 기호에 부합하지 않으면 해당 기록을 더 이상 추천 받지 않을 수 있음

  <img src="images/산책추천/동네기반추천싫어요.gif" height=600> &nbsp; <img src="images/산책추천/정보기반추천싫어요.gif" height=600>

</details>

<details>
<summary> 소모임 </summary>

- 소모임 리스트

  - 사용자가 가입한 소모임 목록들을 보여줌
  - 사용자의 정보에 기반하여 소모임 추천 목록들을 보여줌
    - 나이, 성별, 최대인원, 모집여부, 같은 법정동
  - 그 이외의 소모임 목록들을 보여줌

  <img src="images/소모임/소모임리스트.gif" height=600>

- 소모임 생성

  - 위치, 사진, 나이, 성별, 인원, 가입 설정을 받아 소모임을 생성 함

  <img src="images/소모임/소모임생성.gif" height=600>

</details>

<details>
<summary> 소모임 멤버 </summary>

- 소모임 가입

  <img src="images/소모임/소모임가입.gif" height=600>

- 소모임 설정

  - 소모임 멤버를 추방
  - 소모임 신청 멤버를 수락 거절
  - 소모임 삭제

  <img src="images/소모임/소모임설정.gif" height=600>

</details>

<details>
<summary> 소모임 일정 </summary>

- 소모임 일정 생성

  - 제목, 날짜, 시간, 장소, 인원, 내용을 입력받아 일정을 생성함

  <img src="images/소모임/소모임일정생성.gif" height=600>

- 소모임 기록 업데이트

  - 구성원이 산책한 기록을 소모임 일정에 등록할 수 있음

  <img src="images/소모임/소모임기록업데이트.gif" height=600>

- 소모임 일정 기록 복사

  - 일정에 참여한 소모임원은 자신의 산책 기록으로 복사 할 수 있음

  <img src="images/소모임/소모임일정복사.gif" height=600>

</details>

## :computer: 구동 방법

1. Clone Project

```bash
git clone https://lab.ssafy.com/s10-webmobile2-sub2/S10P12A808.git
```

2. KAKAO API issuance

```Plain Text
1. 카카오 개발자사이트 (https://developers.kakao.com) 접속
2. 개발자 등록 및 앱 생성
3. 웹 플랫폼 추가: 앱 선택 – [플랫폼] – [Web 플랫폼 등록] – 사이트 도메인 등록
4. 사이트 도메인 등록: [웹] 플랫폼을 선택하고, [사이트 도메인] 을 등록. (예: http://localhost:5173)
5. 페이지 상단의 [JavaScript 키]를 지도 API의 appkey로, [REST API 키] 카카오 로그인 API의 appkey로 사용.
```

3. change path to /frontend & npm install

```bash
npm i
```

4. create .env file

```bash
VITE_KAKAO_API_KEY = {kakao JavaScript API key}
VITE_KAKAO_CLIENT_Id = {kakao REST API key}
VITE_KAKAO_REDIRECT_URI = {service url}/member/local-signup
VITE_MARKER_IMAGE_ROUTE = https://walkytalky.s3.ap-northeast-2.amazonaws.com/marker/
```

5. frontend start

```bash
npm run dev
```

6. change path to /backend/src/main

```bash
mkdir resources
```

7. change path to /backend/src/main/resources

```bash
cd resources
```

8. create application.yml

```bash
server:
  port: {spring boot application port number}

spring:
  datasource:
    driver-class-name: {database class name}
    url: {MariaDB connect url}
    username: {MariaDB username}
    password: {MariaDB password}

  data:
    redis:
      host: {service url}
      port: {redis port number}
      password: {redis password}

  servlet:
    multipart:
      max-file-size: {max-file-size}
      max-request-size: {max-request-size}

management:
  endpoints:
    jmx:
      exposure:
        include: "health"

security:
  salt: {JWT secret value}

cloud:
  aws:
    credentials:
      access-key: {S3 access-key}
      secret-key: {S3 secret-key}
    region:
      static: {S3 region}
      auto: false
    stack:
      auto: false
    s3:
      bucket: {S3 Bucket name}

oauth:
  kakao:
    client-id: {kakao REST API key}
    client-secret: {kakao login Client Secret}
    redirect-uri: {service url}/member/local-signup
    request-token-uri: https://kauth.kakao.com/oauth/token
    request-user-info-uri: https://kapi.kakao.com/v2/user/me
```
