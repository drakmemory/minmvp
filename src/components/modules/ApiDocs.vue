<script setup>
const apiGroups = [
  {
    name: '用户管理 API',
    tag: 'user-controller',
    endpoints: [
      { method: 'GET', path: '/api/users', summary: '获取用户列表（分页）', params: 'page, size, keyword', auth: true },
      { method: 'POST', path: '/api/users', summary: '新增用户', params: 'UserDTO (body)', auth: true },
      { method: 'PUT', path: '/api/users/{id}', summary: '更新用户信息', params: 'id, UserDTO', auth: true },
      { method: 'DELETE', path: '/api/users/{id}', summary: '删除用户', params: 'id', auth: true },
      { method: 'GET', path: '/api/users/{id}', summary: '获取用户详情', params: 'id', auth: true },
    ]
  },
  {
    name: '全文搜索 API',
    tag: 'search-controller',
    endpoints: [
      { method: 'GET', path: '/api/search/users', summary: 'ES全文搜索用户', params: 'keyword, page, size', auth: true },
      { method: 'POST', path: '/api/search/sync', summary: '触发ES数据同步', params: '-', auth: true },
    ]
  },
  {
    name: '文件管理 API',
    tag: 'file-controller',
    endpoints: [
      { method: 'POST', path: '/api/files/upload', summary: '上传文件（FastDFS/OSS）', params: 'MultipartFile', auth: true },
      { method: 'GET', path: '/api/files/{fileId}', summary: '下载文件', params: 'fileId', auth: true },
      { method: 'DELETE', path: '/api/files/{fileId}', summary: '删除文件', params: 'fileId', auth: true },
    ]
  },
  {
    name: 'Excel API',
    tag: 'excel-controller',
    endpoints: [
      { method: 'GET', path: '/api/excel/export/users', summary: '导出用户Excel', params: '-', auth: true },
      { method: 'POST', path: '/api/excel/import/users', summary: '导入用户Excel', params: 'MultipartFile', auth: true },
    ]
  },
  {
    name: '定时任务 API',
    tag: 'task-controller',
    endpoints: [
      { method: 'GET', path: '/api/tasks', summary: '获取任务列表', params: '-', auth: true },
      { method: 'POST', path: '/api/tasks/{taskId}/trigger', summary: '手动触发任务', params: 'taskId', auth: true },
    ]
  },
  {
    name: '消息通知 API',
    tag: 'message-controller',
    endpoints: [
      { method: 'GET', path: '/api/messages', summary: '获取操作日志', params: 'page, size', auth: true },
    ]
  },
]

const methodColors = {
  GET: '#61affe',
  POST: '#49cc90',
  PUT: '#fca130',
  DELETE: '#f93e3e',
  PATCH: '#50e3c2',
}
</script>

<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span><el-icon><Connection /></el-icon> API 文档</span>
        <div>
          <el-tag size="small" type="success">Swagger 2.9.2</el-tag>
          <el-tag size="small" type="primary" style="margin-left: 6px">Knife4j 增强</el-tag>
        </div>
      </div>
    </template>

    <el-alert
      title="后端集成 Swagger 2.9.2 + Knife4j 生成交互式 API 文档，支持在线调试。下方为 API 文档预览。"
      type="info"
      :closable="false"
      show-icon
      style="margin-bottom: 16px"
    />

    <!-- Swagger Info Header -->
    <div class="swagger-header">
      <div class="swagger-title">MinMVP API 文档 v1.0.0</div>
      <div class="swagger-meta">
        <el-tag size="small">OAS 2.0</el-tag>
        <span>Schemes: HTTP, HTTPS</span>
        <span>Base URL: /api</span>
        <el-tag size="small" type="warning">JWT Bearer Auth</el-tag>
      </div>
    </div>

    <!-- API Groups -->
    <el-collapse accordion>
      <el-collapse-item
        v-for="group in apiGroups"
        :key="group.tag"
        :name="group.tag"
      >
        <template #title>
          <span class="group-title">
            <el-tag size="small" type="info">{{ group.endpoints.length }}</el-tag>
            {{ group.name }}
            <span class="group-tag">{{ group.tag }}</span>
          </span>
        </template>

        <div
          v-for="ep in group.endpoints"
          :key="ep.path + ep.method"
          class="endpoint-row"
        >
          <div class="endpoint-method" :style="{ background: methodColors[ep.method] }">
            {{ ep.method }}
          </div>
          <div class="endpoint-path">{{ ep.path }}</div>
          <div class="endpoint-summary">{{ ep.summary }}</div>
          <div class="endpoint-params">
            <span class="param-label">参数:</span> {{ ep.params }}
          </div>
          <el-tag v-if="ep.auth" size="small" type="warning">🔒 JWT</el-tag>
        </div>
      </el-collapse-item>
    </el-collapse>

    <el-divider />

    <!-- Global Models -->
    <div class="models-section">
      <div class="models-title">数据模型 (Schemas)</div>
      <el-row :gutter="12">
        <el-col :span="8" v-for="model in ['UserDTO', 'UserVO', 'PageResult', 'ApiResult', 'FileDTO', 'MessageDTO']" :key="model">
          <el-tag type="info" style="margin-bottom: 8px; width: 100%; justify-content: center">
            {{ model }}
          </el-tag>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>

<style scoped>
.card-header { display: flex; align-items: center; justify-content: space-between; }
.swagger-header {
  background: #1b1b1b;
  color: #fff;
  padding: 16px 20px;
  border-radius: 6px;
  margin-bottom: 16px;
}
.swagger-title { font-size: 20px; font-weight: bold; margin-bottom: 8px; }
.swagger-meta { display: flex; align-items: center; gap: 12px; font-size: 13px; color: #ccc; }
.group-title { display: flex; align-items: center; gap: 8px; font-weight: 500; }
.group-tag { color: #909399; font-size: 12px; margin-left: 8px; }
.endpoint-row {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 12px;
  background: #f9f9f9;
  border-radius: 4px;
  margin-bottom: 6px;
  border: 1px solid #e9ecef;
}
.endpoint-method {
  color: #fff;
  padding: 2px 8px;
  border-radius: 4px;
  font-weight: bold;
  font-size: 12px;
  min-width: 60px;
  text-align: center;
}
.endpoint-path { font-family: monospace; font-size: 13px; font-weight: 600; min-width: 200px; }
.endpoint-summary { flex: 1; color: #606266; font-size: 13px; }
.endpoint-params { font-size: 12px; color: #909399; }
.param-label { font-weight: 600; color: #606266; }
.models-section { margin-top: 8px; }
.models-title { font-weight: 600; margin-bottom: 12px; color: #303133; }
</style>
