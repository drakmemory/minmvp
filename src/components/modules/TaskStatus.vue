<script setup>
import { ref } from 'vue'
import { useAppStore } from '../../store/index.js'
import { ElMessage } from 'element-plus'

const store = useAppStore()
const runningTaskId = ref(null)

async function triggerTask(task) {
  runningTaskId.value = task.id
  await new Promise(r => setTimeout(r, 2000))
  const taskRef = store.tasks.find(t => t.id === task.id)
  if (taskRef) {
    taskRef.status = 'success'
    taskRef.lastRun = new Date().toLocaleString('zh-CN', { hour12: false })
  }
  runningTaskId.value = null
  ElMessage.success(`任务 "${task.name}" 执行成功`)
}
</script>

<template>
  <div>
    <el-card style="margin-bottom: 16px">
      <template #header>
        <div class="card-header">
          <span><el-icon><Timer /></el-icon> 定时任务管理</span>
          <div>
            <el-tag size="small" type="warning">XXL-Job 2.0.1</el-tag>
            <el-tag size="small" type="info" style="margin-left: 6px">Cron 表达式</el-tag>
          </div>
        </div>
      </template>

      <el-alert
        title="通过 XXL-Job 调度平台管理定时任务，支持 Cron 表达式配置、任务监控和手动触发。"
        type="info"
        :closable="false"
        show-icon
        style="margin-bottom: 16px"
      />

      <el-row :gutter="16">
        <el-col
          v-for="task in store.tasks"
          :key="task.id"
          :span="12"
          style="margin-bottom: 16px"
        >
          <el-card
            :class="['task-card', task.status === 'failed' ? 'task-failed' : '']"
            shadow="hover"
          >
            <div class="task-header">
              <span class="task-name">
                <el-icon><Timer /></el-icon>
                {{ task.name }}
              </span>
              <el-tag
                :type="task.status === 'success' ? 'success' : 'danger'"
                size="small"
              >
                {{ task.status === 'success' ? '✓ 成功' : '✗ 失败' }}
              </el-tag>
            </div>

            <el-descriptions :column="1" size="small" class="task-desc">
              <el-descriptions-item label="Cron">
                <el-tag size="small" type="info">{{ task.cron }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="上次执行">{{ task.lastRun }}</el-descriptions-item>
              <el-descriptions-item label="耗时">{{ task.duration }}</el-descriptions-item>
            </el-descriptions>

            <div class="task-actions">
              <el-button
                size="small"
                type="primary"
                :loading="runningTaskId === task.id"
                @click="triggerTask(task)"
              >
                {{ runningTaskId === task.id ? '执行中...' : '手动触发' }}
              </el-button>
              <el-button size="small" type="warning">
                {{ task.status === 'success' ? '暂停' : '启用' }}
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- XXL-Job Admin Console Link -->
    <el-card>
      <template #header>
        <span>XXL-Job 调度中心</span>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="版本">2.0.1</el-descriptions-item>
        <el-descriptions-item label="调度策略">轮询</el-descriptions-item>
        <el-descriptions-item label="执行器">minmvp-executor</el-descriptions-item>
        <el-descriptions-item label="失败重试">3次</el-descriptions-item>
        <el-descriptions-item label="告警邮件">admin@example.com</el-descriptions-item>
        <el-descriptions-item label="日志保留">30天</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<style scoped>
.card-header { display: flex; align-items: center; justify-content: space-between; }
.task-card { border-left: 4px solid #67C23A; }
.task-failed { border-left-color: #F56C6C !important; }
.task-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 12px; }
.task-name { display: flex; align-items: center; gap: 6px; font-weight: 600; font-size: 14px; }
.task-desc { margin-bottom: 12px; }
.task-actions { display: flex; gap: 8px; }
</style>
