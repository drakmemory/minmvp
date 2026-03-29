<template>
  <div class="module-wrap">
    <el-alert title="后端: MySQL存储富文本 | CKEditor 22.0.0" type="info" :closable="false" show-icon class="tech-note" />

    <el-row :gutter="16">
      <el-col :span="14">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>富文本编辑器</span>
              <div>
                <el-button type="primary" size="small" @click="saveContent">
                  <el-icon><Check /></el-icon> 保存
                </el-button>
                <el-button type="success" size="small" @click="showPreview = !showPreview">
                  <el-icon><View /></el-icon> {{ showPreview ? '隐藏' : '预览' }}
                </el-button>
                <el-button size="small" @click="clearContent">
                  <el-icon><Delete /></el-icon> 清空
                </el-button>
              </div>
            </div>
          </template>
          <div class="editor-wrapper">
            <ckeditor :editor="editor" v-model="editorData" :config="editorConfig" />
          </div>
          <div class="word-count">
            <el-text type="info" size="small">字符数: {{ plainTextLength }}</el-text>
          </div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="hover" v-if="showPreview">
          <template #header><span>预览效果</span></template>
          <div class="preview-content" v-html="editorData"></div>
        </el-card>
        <el-card shadow="hover" style="margin-top:16px">
          <template #header><span>已保存文档</span></template>
          <div v-for="doc in savedDocs" :key="doc.id" class="doc-item" @click="loadDoc(doc)">
            <el-icon><Document /></el-icon>
            <span class="doc-title">{{ doc.title }}</span>
            <el-text type="info" size="small">{{ doc.time }}</el-text>
          </div>
          <el-empty v-if="!savedDocs.length" description="暂无保存的文档" :image-size="60" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Ckeditor as CKEditor } from '@ckeditor/ckeditor5-vue'
import ClassicEditor from '@ckeditor/ckeditor5-build-classic'
import { ElMessage } from 'element-plus'

const ckeditor = CKEditor

const editor = ClassicEditor
const showPreview = ref(true)

const editorData = ref(`<h2>欢迎使用富文本编辑器</h2>
<p>这是一个基于 <strong>CKEditor 5</strong> 的富文本编辑示例。</p>
<ul>
  <li>支持<strong>加粗</strong>、<em>斜体</em>、<u>下划线</u></li>
  <li>支持有序/无序列表</li>
  <li>支持表格插入</li>
  <li>支持图片上传</li>
</ul>
<blockquote>
  <p>数据存储于 MySQL，通过 MyBatis-Plus 进行增删改查操作。</p>
</blockquote>`)

const editorConfig = {
  toolbar: ['heading', '|', 'bold', 'italic', 'underline', '|', 'bulletedList', 'numberedList', '|', 'blockQuote', 'insertTable', '|', 'undo', 'redo'],
}

// Use DOMParser to safely extract plain text for character counting
// instead of a regex-based tag stripping approach.
const plainTextLength = computed(() => {
  try {
    return new DOMParser().parseFromString(editorData.value, 'text/html').body.textContent.length
  } catch {
    return 0
  }
})

const savedDocs = ref([
  { id: 1, title: '系统公告 2024-01-15', time: '10:30', content: '<h2>系统维护通知</h2><p>系统将于今晚22:00-24:00进行维护。</p>' },
  { id: 2, title: '用户手册 v1.0', time: '09:15', content: '<h2>用户操作手册</h2><p>本手册介绍系统的主要功能...</p>' },
])

function saveContent() {
  const title = `文档 ${new Date().toLocaleString('zh-CN').slice(0, 10)}`
  savedDocs.value.unshift({
    id: Date.now(),
    title,
    time: new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }),
    content: editorData.value,
  })
  ElMessage.success('文档已保存')
}

function clearContent() {
  editorData.value = '<p></p>'
}

function loadDoc(doc) {
  editorData.value = doc.content
  ElMessage.success(`已加载文档: ${doc.title}`)
}
</script>

<style scoped>
.module-wrap { padding: 8px 0; }
.tech-note { margin-bottom: 16px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.editor-wrapper { min-height: 300px; }
.word-count { margin-top: 8px; text-align: right; }
.preview-content { min-height: 100px; line-height: 1.8; }
.preview-content :deep(h1), .preview-content :deep(h2), .preview-content :deep(h3) { margin: 12px 0 8px; }
.preview-content :deep(ul), .preview-content :deep(ol) { padding-left: 20px; }
.preview-content :deep(blockquote) { border-left: 4px solid #1890ff; padding-left: 12px; color: #666; }
.doc-item { display: flex; align-items: center; gap: 8px; padding: 8px 0; border-bottom: 1px solid #f0f0f0; cursor: pointer; }
.doc-item:hover { background: #f5f5f5; }
.doc-title { flex: 1; font-size: 14px; }
</style>
