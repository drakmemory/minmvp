<script setup>
import { ref } from 'vue'
import { useAppStore } from '../../store/index.js'

const store = useAppStore()
const keyword = ref('')

async function handleSearch() {
  await store.searchUsers(keyword.value)
}

function onKeydown(e) {
  if (e.key === 'Enter') handleSearch()
}
</script>

<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span><el-icon><Search /></el-icon> 全文搜索</span>
        <div class="tech-badges">
          <el-tag size="small" type="danger">Elasticsearch 7.3.2</el-tag>
          <el-tag size="small">Spring Data ES</el-tag>
        </div>
      </div>
    </template>

    <div class="search-desc">
      <el-alert
        title="后端通过 Elasticsearch 索引用户数据（与 MySQL 数据同步），支持按姓名/手机号模糊搜索。"
        type="info"
        :closable="false"
        show-icon
        style="margin-bottom: 16px"
      />
    </div>

    <div class="search-box">
      <el-input
        v-model="keyword"
        placeholder="输入用户姓名或手机号进行全文搜索..."
        size="large"
        clearable
        :prefix-icon="'Search'"
        style="max-width: 500px"
        @keydown="onKeydown"
        @clear="store.searchResults = []"
      />
      <el-button
        type="primary"
        size="large"
        :loading="store.searchLoading"
        :icon="'Search'"
        @click="handleSearch"
      >搜索</el-button>
    </div>

    <div v-if="store.searchLoading" class="search-status">
      <el-icon class="is-loading"><Loading /></el-icon>
      正在查询 Elasticsearch 索引...
    </div>

    <div v-else-if="store.searchResults.length > 0" class="search-results">
      <div class="result-header">
        <el-tag type="success">找到 {{ store.searchResults.length }} 条结果</el-tag>
        <span class="es-info">· 来源: Elasticsearch 用户索引 (user_index)</span>
      </div>
      <el-table :data="store.searchResults" border stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="name" label="姓名">
          <template #default="{ row }">
            <span v-html="row.name.replace(new RegExp(keyword, 'g'), `<mark>${keyword}</mark>`)" />
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="role" label="角色">
          <template #default="{ row }">
            <el-tag
              :type="row.role === 'admin' ? 'danger' : 'info'"
              size="small"
            >{{ row.role }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-empty
      v-else-if="keyword && !store.searchLoading"
      description="未找到匹配的用户"
    />

    <!-- ES Index Info -->
    <el-divider content-position="left">Elasticsearch 索引信息</el-divider>
    <el-descriptions :column="2" border size="small">
      <el-descriptions-item label="索引名称">user_index</el-descriptions-item>
      <el-descriptions-item label="ES版本">7.3.2</el-descriptions-item>
      <el-descriptions-item label="文档数量">{{ store.users.length }}</el-descriptions-item>
      <el-descriptions-item label="分片数">5</el-descriptions-item>
      <el-descriptions-item label="同步方式">定时任务 + 数据变更触发</el-descriptions-item>
      <el-descriptions-item label="分析器">ik_max_word (中文分词)</el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<style scoped>
.card-header { display: flex; align-items: center; justify-content: space-between; }
.tech-badges { display: flex; gap: 6px; }
.search-box { display: flex; gap: 12px; align-items: center; margin-bottom: 20px; }
.search-status { display: flex; align-items: center; gap: 8px; color: #909399; margin-bottom: 16px; }
.search-results { margin-top: 16px; }
.result-header { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }
.es-info { color: #909399; font-size: 13px; }
mark { background: #ffefc0; padding: 0 2px; border-radius: 2px; }
</style>
