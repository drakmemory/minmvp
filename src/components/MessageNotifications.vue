<template>
  <div class="module-wrap">
    <el-alert title="后端: Kafka消息队列生产者/消费者 | Pinia状态管理" type="info" :closable="false" show-icon class="tech-note" />
    <el-row :gutter="16">
      <el-col :span="14">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>操作日志时间线</span>
              <el-badge :value="store.messages.length" type="primary" />
            </div>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="msg in store.messages"
              :key="msg.id"
              :timestamp="msg.timestamp"
              placement="top"
              :type="timelineType(msg.action)"
            >
              <el-card shadow="never" class="log-card">
                <div class="log-content">
                  <el-tag size="small" class="user-tag">{{ msg.user }}</el-tag>
                  <span class="action-text">{{ msg.action }}</span>
                  <el-text type="info" size="small">IP: {{ msg.ip }}</el-text>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="hover" style="margin-bottom:16px">
          <template #header><span>Kafka 主题状态</span></template>
          <div v-for="topic in kafkaTopics" :key="topic.name" class="kafka-row">
            <span class="topic-name">{{ topic.name }}</span>
            <div>
              <el-tag :type="topic.status === '正常' ? 'success' : 'warning'" size="small">{{ topic.status }}</el-tag>
              <el-text type="info" size="small" style="margin-left:8px">{{ topic.count }} 条</el-text>
            </div>
          </div>
        </el-card>
        <el-card shadow="hover">
          <template #header><span>模拟发送消息</span></template>
          <el-form @submit.prevent>
            <el-form-item label="主题">
              <el-select v-model="newMsg.topic" style="width:100%">
                <el-option v-for="t in kafkaTopics" :key="t.name" :label="t.name" :value="t.name" />
              </el-select>
            </el-form-item>
            <el-form-item label="内容">
              <el-input v-model="newMsg.content" type="textarea" :rows="2" />
            </el-form-item>
            <el-button type="primary" @click="sendMessage" style="width:100%">发送到Kafka</el-button>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAppStore } from '../store/index.js'
import { ElMessage } from 'element-plus'

const store = useAppStore()

const kafkaTopics = ref([
  { name: 'user-operation-log', status: '正常', count: 1024 },
  { name: 'system-notification', status: '正常', count: 356 },
  { name: 'email-queue', status: '积压', count: 89 },
])

const newMsg = ref({ topic: 'user-operation-log', content: '' })

function timelineType(action) {
  if (action.includes('登录')) return 'success'
  if (action.includes('删除')) return 'danger'
  if (action.includes('注册')) return 'primary'
  return 'warning'
}

function sendMessage() {
  if (!newMsg.value.content) return
  store.messages.unshift({
    id: Date.now(),
    user: '管理员',
    action: newMsg.value.content,
    timestamp: new Date().toLocaleString('zh-CN'),
    ip: '127.0.0.1',
  })
  ElMessage.success(`消息已发送到 ${newMsg.value.topic}`)
  newMsg.value.content = ''
}
</script>

<style scoped>
.module-wrap { padding: 8px 0; }
.tech-note { margin-bottom: 16px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.log-card { background: #fafafa; }
.log-content { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }
.user-tag { flex-shrink: 0; }
.action-text { flex: 1; font-size: 14px; }
.kafka-row { display: flex; justify-content: space-between; align-items: center; padding: 8px 0; border-bottom: 1px solid #f0f0f0; }
.topic-name { font-size: 13px; font-weight: 500; }
</style>
