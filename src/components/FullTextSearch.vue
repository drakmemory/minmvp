<template>
  <div class="module-wrap">
    <el-alert title="后端: Elasticsearch 7.3.2 全文索引" type="info" :closable="false" show-icon class="tech-note" />
    <div class="search-bar">
      <el-input v-model="keyword" placeholder="输入关键词进行全文检索..." style="width:400px" clearable @keyup.enter="doSearch">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
      <el-button type="primary" @click="doSearch" :loading="searching">
        <el-icon><Search /></el-icon> 全文检索
      </el-button>
      <el-badge v-if="results.length" :value="results.length" type="primary">
        <el-tag>检索结果</el-tag>
      </el-badge>
    </div>

    <el-table v-if="results.length" :data="results" stripe border style="width:100%;margin-top:16px">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="姓名" width="100">
        <template #default="{ row }">
          <span v-html="highlight(row.name)"></span>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column label="邮箱">
        <template #default="{ row }">
          <span v-html="highlight(row.email)"></span>
        </template>
      </el-table-column>
      <el-table-column label="匹配内容">
        <template #default="{ row }">
          <span v-html="highlight(row.highlight)"></span>
        </template>
      </el-table-column>
      <el-table-column prop="score" label="相关度" width="80">
        <template #default="{ row }">
          <el-progress :percentage="row.score" :stroke-width="6" />
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-else-if="searched" description="未找到相关结果" />

    <div v-if="!searched" class="search-hint">
      <el-text type="info">试试搜索：张、admin、example.com</el-text>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const keyword = ref('')
const searching = ref(false)
const searched = ref(false)
const results = ref([])

const mockData = [
  { id: 1, name: '张三', phone: '13800001111', email: 'zhangsan@example.com', highlight: '管理员用户，拥有全部权限', score: 95 },
  { id: 2, name: '李四', phone: '13800002222', email: 'lisi@example.com', highlight: '普通用户，常用功能访问', score: 82 },
  { id: 3, name: '王五', phone: '13800003333', email: 'wangwu@example.com', highlight: '已禁用账号，无法登录系统', score: 78 },
  { id: 4, name: '赵六', phone: '13800004444', email: 'zhaoliu@example.com', highlight: '访客权限，只读操作', score: 65 },
  { id: 5, name: '张伟', phone: '13900001111', email: 'zhangwei@company.cn', highlight: '技术部门员工，张姓', score: 90 },
]

function doSearch() {
  if (!keyword.value.trim()) return
  searching.value = true
  setTimeout(() => {
    const q = keyword.value.toLowerCase()
    results.value = mockData.filter(d =>
      d.name.includes(q) || d.email.includes(q) || d.phone.includes(q) || d.highlight.includes(q)
    )
    searched.value = true
    searching.value = false
  }, 600)
}

function highlight(text) {
  if (!keyword.value || !text) return text
  const escaped = keyword.value.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
  return text.replace(new RegExp(escaped, 'gi'), m => `<mark style="background:#fff566;padding:0 2px">${m}</mark>`)
}
</script>

<style scoped>
.module-wrap { padding: 8px 0; }
.tech-note { margin-bottom: 16px; }
.search-bar { display: flex; gap: 10px; align-items: center; flex-wrap: wrap; }
.search-hint { margin-top: 40px; text-align: center; }
</style>
