<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useAppStore } from '../../store/index.js'

const store = useAppStore()
const autoRefresh = ref(true)
let timer = null

const typeMap = {
  login: { label: '登录', type: 'success', icon: 'User' },
  create: { label: '新增', type: 'primary', icon: 'Plus' },
  update: { label: '修改', type: 'warning', icon: 'Edit' },
  delete: { label: '删除', type: 'danger', icon: 'Delete' },
  export: { label: '导出', type: 'info', icon: 'Download' },
  import: { label: '导入', type: 'info', icon: 'Upload' },
}

const mockUsers = ['张三', '李四', '王五', '赵六', '管理员']
const mockTypes = ['login', 'create', 'update', 'export']
const mockContents = {
  login: ['用户登录系统', '用户登出系统'],
  create: ['新增用户: test_user', '创建角色: viewer'],
  update: ['修改个人信息', '更新用户权限'],
  export: ['Excel导出用户数据', '导出统计报表'],
}

function simulateKafkaMessage() {
  const type = mockTypes[Math.floor(Math.random() * mockTypes.length)]
  const contents = mockContents[type]
  store.addMessage({
    type,
    user: mockUsers[Math.floor(Math.random() * mockUsers.length)],
    content: contents[Math.floor(Math.random() * contents.length)],
    status: Math.random() > 0.2 ? 'success' : 'warning'
  })
}

onMounted(() => {
  if (autoRefresh.value) {
    timer = setInterval(simulateKafkaMessage, 4000)
  }
})

onUnmounted(() => {
  clearInterval(timer)
})

function toggleRefresh() {
  if (autoRefresh.value) {
    timer = setInterval(simulateKafkaMessage, 4000)
  } else {
    clearInterval(timer)
  }
}
</script>

<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span><el-icon><Bell /></el-icon> 消息通知 / 操作日志</span>
        <div class="header-right">
          <el-tag size="small" type="danger">Kafka 消息队列</el-tag>
          <el-tag size="small" type="primary">Pinia 状态管理</el-tag>
          <el-switch
            v-model="autoRefresh"
            active-text="实时推送"
            inactive-text="暂停"
            @change="toggleRefresh"
            style="margin-left: 12px"
          />
        </div>
      </div>
    </template>

    <el-alert
      title="Kafka 生产者在用户操作时发送日志消息，消费者实时接收并存储至 MySQL，通过 Pinia 管理前端消息列表。"
      type="info"
      :closable="false"
      show-icon
      style="margin-bottom: 16px"
    />

    <div class="kafka-info">
      <el-descriptions :column="3" border size="small" style="margin-bottom: 16px">
        <el-descriptions-item label="Topic">user-operation-log</el-descriptions-item>
        <el-descriptions-item label="消息总数">{{ store.messages.length }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-badge is-dot :type="autoRefresh ? 'success' : 'info'" />
          {{ autoRefresh ? '实时消费中' : '已暂停' }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <el-timeline>
      <el-timeline-item
        v-for="msg in store.messages"
        :key="msg.id"
        :type="typeMap[msg.type]?.type || 'info'"
        :timestamp="msg.time"
        placement="top"
      >
        <el-card class="message-card" shadow="never">
          <div class="message-content">
            <el-tag
              :type="typeMap[msg.type]?.type || 'info'"
              size="small"
              class="msg-type-tag"
            >{{ typeMap[msg.type]?.label || msg.type }}</el-tag>
            <span class="msg-user"><el-icon><User /></el-icon> {{ msg.user }}</span>
            <span class="msg-text">{{ msg.content }}</span>
            <el-tag
              :type="msg.status === 'success' ? 'success' : 'warning'"
              size="small"
            >{{ msg.status }}</el-tag>
          </div>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </el-card>
</template>

<style scoped>
.card-header { display: flex; align-items: center; justify-content: space-between; }
.header-right { display: flex; align-items: center; gap: 8px; }
.message-card { border: none; background: #f9f9f9; }
.message-content { display: flex; align-items: center; gap: 10px; }
.msg-type-tag { flex-shrink: 0; }
.msg-user { color: #409EFF; font-size: 13px; display: flex; align-items: center; gap: 4px; }
.msg-text { flex: 1; font-size: 14px; }
</style>
