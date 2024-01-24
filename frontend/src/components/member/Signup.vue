<template>
  <div>
    <h1>회원가입</h1>
    <fieldset>
      <form>
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label for="inputPassword6" class="col-form-label">아이디</label>
          </div>
          <div class="col-auto">
            <input type="text" class="form-control" aria-describedby="passwordHelpInline" />
          </div>
          <div class="col-auto">
            <button type="submit" class="btn btn-primary mb-3">중복확인</button>
          </div>
        </div>

        <!-- <div class="mb-3">
          <label for="exampleInputPassword1" class="form-label">비밀번호</label>
          <input type="password" class="form-control" id="exampleInputPassword1" />
        </div> -->

        <div class="mb-3 row">
          <label for="inputPassword" class="col-sm-2 col-form-label">비밀번호</label>
          <div class="col-sm-10">
            <input type="password" class="form-control" id="inputPassword" />
          </div>
        </div>

        <!-- <div class="mb-3">
          <label for="exampleInputPassword1" class="form-label">비밀번호 확인</label>
          <input type="password" class="form-control" id="exampleInputPassword2" />
        </div> -->

        <div class="mb-3 row">
          <label for="inputPassword" class="col-sm-2 col-form-label">비밀번호 확인</label>
          <div class="col-sm-10">
            <input type="password" class="form-control" id="inputPassword" />
          </div>
        </div>

        <!-- <div class="mb-3">
          <label for="exampleInputPassword1" class="form-label">생년월일</label>
          <input type="text" class="form-control" />
        </div> -->

        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label class="col-form-label">생년월일</label>
          </div>
          <div class="col-auto">
            <input type="text" class="form-control" aria-describedby="passwordHelpInline" />
          </div>
          <div class="col-auto">
            <span class="form-text"> ex: 19910101 </span>
          </div>
        </div>

        <!-- <div class="mb-3">
          <label for="exampleInputPassword1" class="form-label">성별</label>
          <input type="text" class="form-control" />
        </div> -->

        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label class="col-form-label">성별</label>
          </div>
          <div class="col-auto">
            <input type="text" class="form-control" aria-describedby="passwordHelpInline" />
          </div>
          <div class="col-auto">
            <span class="form-text"> ex: 남 </span>
          </div>
        </div>

        <!-- <div class="mb-3">
          <label for="exampleInputPassword1" class="form-label">닉네임</label>
          <input type="text" class="form-control" />
        </div> -->

        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label for="inputPassword6" class="col-form-label">닉네임</label>
          </div>
          <div class="col-auto">
            <input type="text" class="form-control" aria-describedby="passwordHelpInline" />
          </div>
          <div class="col-auto">
            <button type="submit" class="btn btn-primary mb-3">중복확인</button>
          </div>
        </div>

        <!-- <div class="mb-3">
          <label for="exampleInputPassword1" class="form-label">주소</label>
          <input type="text" class="form-control" />
        </div> -->

        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label for="inputPassword6" class="col-form-label">주소</label>
          </div>
          <div class="col-auto">
            <input type="text" class="form-control" aria-describedby="passwordHelpInline" />
          </div>
          <div class="col-auto">
            <button type="submit" class="btn btn-primary mb-3">동네검색</button>
          </div>
        </div>

        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label for="exampleFormControlTextarea1" class="form-label">자기소개</label>
          </div>
          <div class="col-auto">
            <textarea
              class="form-control"
              id="exampleFormControlTextarea1"
              value="안녕하세요. "
              rows="3"
            ></textarea>
          </div>
        </div>
      </form>
    </fieldset>
  </div>
</template>

<script setup></script>

<style scoped></style>
