<template>
  <div class="box">
    <div class="mb-3">
      <label class="form-label">작성자</label>
      <shareBoardMember :nickname="myNickname" :profilePic="myProfileImage" />
    </div>

    <div class="mb-3">
      <input
        v-model="title"
        id="title"
        type="text"
        placeholder="제목을 입력해주세요."
        class="form-control"
      />
    </div>

    <div>
      <h5>공유하고 싶은 기록을 선택해주세요.</h5>
    </div>
    <div class="record-list-container">
      <div
        v-for="record in records"
        :key="record.recordSeq"
        :class="{ 'record-list': true, selected: selectedRecord === record.recordSeq }"
        @click="selectRecord(record.recordSeq)"
      >
        <shareBoardRecord
          :duration="record.duration"
          :distance="record.distance"
          :points="record.points"
          :title="record.title"
          :seq="record.recordSeq"
          :movable="false"
        />
      </div>
    </div>

    <div class="mb-3 write-content-container">
      <textarea
        id="content"
        v-model="content"
        class="form-control"
        rows="10"
        placeholder="내용을 입력해주세요."
      ></textarea>
    </div>

    <button @click="write">공유하기</button>
    <button @click="moveList">목록으로</button>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useShareBoardStore } from '@/stores/shareBoard'
import { useCounterStore } from '@/stores/counter'
import shareBoardRecord from '@/components/shareBoard/shareBoardRecord.vue'
import shareBoardMember from '@/components/shareBoard/shareBoardMember.vue'

const router = useRouter()
const shareBoardStore = useShareBoardStore()
const counterStore = useCounterStore()

const records = ref([])
const myNickname = ref('')
const myProfileImage = ref('')
onMounted(async () => {
  await shareBoardStore.getMyRecord()
  records.value = shareBoardStore.myRecords

  myNickname.value = counterStore.getCookie('nickname')
  myProfileImage.value = counterStore.getCookie('profileImage')
})

const selectedRecord = ref(null)

const selectRecord = (seq) => {
  if (selectedRecord.value === seq) {
    selectedRecord.value = null
  } else {
    selectedRecord.value = seq
  }
}

const title = ref('')
const content = ref('')

const write = async () => {
  if (title.value == '' || content.value == '') {
    alert('제목과 내용을 입력해주세요.')
  } else if (selectedRecord.value == null) {
    alert('공유 할 기록을 선택해주세요.')
  } else {
    await shareBoardStore.write(selectedRecord.value, title.value, content.value)
    router.push({ name: 'share-board' })
    selectedRecord.value = null
    title.value = ''
    content.value = ''
  }
}

const moveList = () => {
  router.push({ name: 'share-board' })
}
</script>

<style scoped>
.record-list-container {
  display: flex;
  overflow-x: auto;
  white-space: nowrap; /* 줄 바꿈 방지 */
}

.write-content-container {
  margin-top: 5%;
}

.record-list {
  box-sizing: border-box;
  /* margin: 3%; */
  padding: 3%;
}

.selected {
  background-color: lightgray;
}
</style>
