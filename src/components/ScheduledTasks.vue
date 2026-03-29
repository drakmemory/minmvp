<template>
  <div class="module-wrap">
    <el-alert title="后端: XXL-Job 2.0.1 定时任务" type="info" :closable="false" show-icon class="tech-note" />
    <el-row :gutter="16">
      <el-col :span="12" v-for="task in tasks" :key="task.id" style="margin-bottom:16px">
        <el-card shadow="hover" :class="['task-card', `task-${task.status}`]">
          <div class="task-header">
            <span class="task-name">{{ task.name }}</span>
            <el-tag :type="statusType(task.status)" size="small">
              <el-icon v-if="task.status === 'running'" class="spinning"><Loading /></el-icon>
              {{ statusLabel(task.status) }}
            </el-tag>
          </div>
          <div class="task-info">
            <div class="info-row">
              <el-text type="info" size="small">上次执行:</el-text>
              <span class="info-val">{{ task.lastRun }}</span>
            </div>
            <div class="info-row">
              <el-text type="info" size="small">下次执行:</el-text>
              <span class="info-val">{{ task.nextRun }}</span>
            </div>
            <div class="info-row">
              <el-text type="info" size="small">Cron表达式:</el-text>
              <el-tag type="info" size="small" effect="plain">{{ task.cron }}</el-tag>
            </div>
          </div>
          <div class="task-actions">
            <el-button size="small" type="primary" @click="runNow(task)">立即执行</el-button>
            <el-button size="small" :type="task.enabled ? 'warning' : 'success'" @click="toggleTask(task)">
              {{ task.enabled ? '暂停' : '启用' }}
            </el-button>
            <el-button size="small" type="info" @click="viewLog(task)">执行日志</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const tasks = ref([
  { id: 1, name: '每日用户统计', lastRun: '2024-01-15 00:00:05', nextRun: '2024-01-16 00:00:00', status: 'success', cron: '0 0 0 * * ?', enabled: true },
  { id: 2, name: '数据备份', lastRun: '2024-01-15 02:00:12', nextRun: '2024-01-16 02:00:00', status: 'success', cron: '0 0 2 * * ?', enabled: true },
  { id: 3, name: '邮件发送', lastRun: '2024-01-15 08:00:03', nextRun: '2024-01-15 12:00:00', status: 'running', cron: '0 0 */4 * * ?', enabled: true },
  { id: 4, name: '缓存清理', lastRun: '2024-01-14 23:00:08', nextRun: '2024-01-15 23:00:00', status: 'fail', cron: '0 0 23 * * ?', enabled: false },
])

function statusType(s) {
  return { success: 'success', fail: 'danger', running: 'warning' }[s] || 'info'
}

function statusLabel(s) {
  return { success: '成功', fail: '失败', running: '执行中' }[s] || s
}

function runNow(task) {
  task.status = 'running'
  ElMessage.info(`任务 "${task.name}" 已触发执行`)
  setTimeout(() => {
    task.status = 'success'
    task.lastRun = new Date().toLocaleString('zh-CN')
    ElMessage.success(`任务 "${task.name}" 执行完成`)
  }, 2000)
}

function toggleTask(task) {
  task.enabled = !task.enabled
  ElMessage.success(`任务 "${task.name}" 已${task.enabled ? '启用' : '暂停'}`)
}

function viewLog(task) {
  ElMessage.info(`查看 "${task.name}" 的执行日志`)
}
</script>

<style scoped>
.module-wrap { padding: 8px 0; }
.tech-note { margin-bottom: 16px; }
.task-card { transition: all 0.3s; }
.task-success { border-left: 4px solid #52c41a; }
.task-fail { border-left: 4px solid #ff4d4f; }
.task-running { border-left: 4px solid #faad14; }
.task-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.task-name { font-size: 16px; font-weight: 600; }
.task-info { margin-bottom: 12px; }
.info-row { display: flex; align-items: center; gap: 8px; margin-bottom: 6px; }
.info-val { font-size: 13px; }
.task-actions { display: flex; gap: 8px; }
.spinning { animation: spin 1s linear infinite; }
@keyframes spin { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
</style>
