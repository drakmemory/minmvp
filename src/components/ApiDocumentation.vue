<template>
  <div class="module-wrap">
    <el-alert title="后端: Swagger/Knife4j 2.9.2 API文档" type="info" :closable="false" show-icon class="tech-note" />

    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="6">
        <el-statistic title="API总数" :value="apis.length" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="GET接口" :value="apis.filter(a=>a.method==='GET').length" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="POST接口" :value="apis.filter(a=>a.method==='POST').length" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="需鉴权" :value="apis.filter(a=>a.auth).length" />
      </el-col>
    </el-row>

    <el-collapse v-model="activeApis">
      <el-collapse-item v-for="api in apis" :key="api.id" :name="api.id">
        <template #title>
          <div class="api-title">
            <el-tag :type="methodColor(api.method)" size="small" effect="dark" class="method-tag">
              {{ api.method }}
            </el-tag>
            <code class="api-path">{{ api.path }}</code>
            <span class="api-desc">{{ api.description }}</span>
            <el-tag v-if="api.auth" type="warning" size="small" effect="plain">🔒 需认证</el-tag>
          </div>
        </template>
        <div class="api-detail">
          <el-descriptions :column="2" border size="small" style="margin-bottom:12px">
            <el-descriptions-item label="请求方法">
              <el-tag :type="methodColor(api.method)" size="small" effect="dark">{{ api.method }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="接口路径"><code>{{ api.path }}</code></el-descriptions-item>
            <el-descriptions-item label="接口描述">{{ api.description }}</el-descriptions-item>
            <el-descriptions-item label="响应格式">JSON</el-descriptions-item>
          </el-descriptions>

          <div v-if="api.params && api.params.length">
            <strong style="font-size:14px">请求参数</strong>
            <el-table :data="api.params" border size="small" style="margin-top:8px">
              <el-table-column prop="name" label="参数名" width="120" />
              <el-table-column prop="type" label="类型" width="90" />
              <el-table-column label="必填" width="70">
                <template #default="{ row }">
                  <el-tag :type="row.required ? 'danger' : 'info'" size="small">{{ row.required ? '是' : '否' }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="desc" label="说明" />
            </el-table>
          </div>

          <div style="margin-top:12px">
            <strong style="font-size:14px">响应示例</strong>
            <pre class="response-code">{{ api.response }}</pre>
          </div>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const activeApis = ref(['1'])

const apis = ref([
  {
    id: '1', method: 'GET', path: '/api/users', description: '获取用户列表', auth: true,
    params: [
      { name: 'page', type: 'Integer', required: false, desc: '页码，默认1' },
      { name: 'size', type: 'Integer', required: false, desc: '每页条数，默认10' },
      { name: 'keyword', type: 'String', required: false, desc: '搜索关键词' },
    ],
    response: `{\n  "code": 200,\n  "msg": "success",\n  "data": {\n    "total": 100,\n    "list": [{ "id": 1, "name": "张三" }]\n  }\n}`,
  },
  {
    id: '2', method: 'POST', path: '/api/users', description: '创建用户', auth: true,
    params: [
      { name: 'name', type: 'String', required: true, desc: '用户姓名' },
      { name: 'phone', type: 'String', required: true, desc: '手机号' },
      { name: 'email', type: 'String', required: false, desc: '邮箱地址' },
      { name: 'role', type: 'String', required: false, desc: '用户角色，默认 user' },
    ],
    response: `{\n  "code": 200,\n  "msg": "创建成功",\n  "data": { "id": 101, "name": "新用户" }\n}`,
  },
  {
    id: '3', method: 'PUT', path: '/api/users/{id}', description: '更新用户信息', auth: true,
    params: [
      { name: 'id', type: 'Long', required: true, desc: '用户ID (路径参数)' },
      { name: 'name', type: 'String', required: false, desc: '用户姓名' },
      { name: 'email', type: 'String', required: false, desc: '邮箱地址' },
    ],
    response: `{\n  "code": 200,\n  "msg": "更新成功",\n  "data": null\n}`,
  },
  {
    id: '4', method: 'DELETE', path: '/api/users/{id}', description: '删除用户', auth: true,
    params: [
      { name: 'id', type: 'Long', required: true, desc: '用户ID (路径参数)' },
    ],
    response: `{\n  "code": 200,\n  "msg": "删除成功",\n  "data": null\n}`,
  },
  {
    id: '5', method: 'GET', path: '/api/search', description: '全文搜索接口', auth: false,
    params: [
      { name: 'q', type: 'String', required: true, desc: '搜索关键词' },
      { name: 'from', type: 'Integer', required: false, desc: '分页起始位置' },
      { name: 'size', type: 'Integer', required: false, desc: '返回条数，默认10' },
    ],
    response: `{\n  "code": 200,\n  "hits": 25,\n  "data": [{ "id": 1, "name": "张三", "score": 0.95 }]\n}`,
  },
])

function methodColor(method) {
  return { GET: 'success', POST: 'primary', PUT: 'warning', DELETE: 'danger' }[method] || 'info'
}
</script>

<style scoped>
.module-wrap { padding: 8px 0; }
.tech-note { margin-bottom: 16px; }
.api-title { display: flex; align-items: center; gap: 10px; flex-wrap: wrap; }
.method-tag { font-weight: bold; min-width: 64px; text-align: center; }
.api-path { font-size: 14px; color: #333; background: #f5f5f5; padding: 2px 6px; border-radius: 3px; }
.api-desc { color: #666; font-size: 14px; }
.api-detail { padding: 12px 0; }
.response-code {
  background: #282c34;
  color: #abb2bf;
  padding: 12px;
  border-radius: 6px;
  font-size: 13px;
  overflow-x: auto;
  margin-top: 8px;
}
</style>
