<script setup>
import { ref } from 'vue'
import { useAppStore } from '../../store/index.js'
import { ElMessage } from 'element-plus'

const store = useAppStore()
const saving = ref(false)
const localContent = ref(store.richTextContent)

// Simulated CKEditor toolbar items
const toolbar = [
  { icon: 'bold', label: 'B', title: '加粗' },
  { icon: 'italic', label: 'I', title: '斜体' },
  { icon: 'underline', label: 'U', title: '下划线' },
  { icon: 'heading', label: 'H1', title: '标题' },
  { icon: 'link', label: '🔗', title: '插入链接' },
  { icon: 'image', label: '🖼', title: '插入图片' },
  { icon: 'list', label: '≡', title: '列表' },
  { icon: 'table', label: '⊞', title: '插入表格' },
]

async function handleSave() {
  saving.value = true
  await new Promise(r => setTimeout(r, 1000))
  store.setRichTextContent(localContent.value)
  saving.value = false
  ElMessage.success('富文本内容已保存至 MySQL')
}

function applyFormat(format) {
  const el = document.getElementById('richtext-area')
  if (!el) return
  const start = el.selectionStart
  const end = el.selectionEnd
  const selected = localContent.value.substring(start, end)

  const formatMap = {
    bold: [`<strong>`, `</strong>`],
    italic: [`<em>`, `</em>`],
    underline: [`<u>`, `</u>`],
    heading: [`<h2>`, `</h2>`],
    link: [`<a href="#">`, `</a>`],
    image: [`<img src="`, `" />`],
    list: [`<ul><li>`, `</li></ul>`],
    table: [`<table><tr><td>`, `</td></tr></table>`],
  }

  const [open, close] = formatMap[format] || ['', '']
  localContent.value =
    localContent.value.substring(0, start) +
    open + (selected || '内容') + close +
    localContent.value.substring(end)
}
</script>

<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span><el-icon><Edit /></el-icon> 富文本编辑器</span>
        <div>
          <el-tag size="small" type="success">CKEditor 22.0.0</el-tag>
          <el-tag size="small" type="info" style="margin-left: 6px">MySQL 存储</el-tag>
        </div>
      </div>
    </template>

    <el-alert
      title="集成 CKEditor 富文本编辑器，支持图文混排，内容存储至 MySQL text 字段，通过 Hutool 进行 HTML 安全处理。"
      type="info"
      :closable="false"
      show-icon
      style="margin-bottom: 16px"
    />

    <el-row :gutter="16">
      <!-- Editor -->
      <el-col :span="12">
        <div class="editor-wrapper">
          <div class="editor-header">
            <span class="editor-title">编辑区 (CKEditor)</span>
            <el-tag size="small" type="warning">HTML 模式</el-tag>
          </div>

          <!-- Simulated CKEditor Toolbar -->
          <div class="ckeditor-toolbar">
            <el-tooltip
              v-for="btn in toolbar"
              :key="btn.icon"
              :content="btn.title"
              placement="top"
            >
              <el-button
                size="small"
                class="toolbar-btn"
                @click="applyFormat(btn.icon)"
              >{{ btn.label }}</el-button>
            </el-tooltip>
          </div>

          <!-- Editor Content Area -->
          <textarea
            id="richtext-area"
            v-model="localContent"
            class="editor-textarea"
            placeholder="在此输入富文本内容（HTML格式）..."
            rows="12"
          />

          <div class="editor-actions">
            <el-button
              type="primary"
              :loading="saving"
              @click="handleSave"
            >{{ saving ? '保存中...' : '保存至 MySQL' }}</el-button>
            <el-button @click="localContent = store.richTextContent">重置</el-button>
          </div>
        </div>
      </el-col>

      <!-- Preview -->
      <el-col :span="12">
        <div class="preview-wrapper">
          <div class="editor-header">
            <span class="editor-title">预览区</span>
            <el-tag size="small" type="success">HTML 渲染</el-tag>
          </div>
          <div class="preview-content" v-html="localContent" />
        </div>
      </el-col>
    </el-row>

    <!-- Tech Info -->
    <el-divider />
    <el-descriptions :column="3" border size="small">
      <el-descriptions-item label="CKEditor版本">22.0.0 (Vue 3 集成)</el-descriptions-item>
      <el-descriptions-item label="存储字段">MySQL TEXT 类型</el-descriptions-item>
      <el-descriptions-item label="安全处理">Hutool HtmlUtil 过滤 XSS</el-descriptions-item>
      <el-descriptions-item label="图片上传">集成 FastDFS/OSS</el-descriptions-item>
      <el-descriptions-item label="格式支持">H1-H6、加粗、斜体、列表、表格</el-descriptions-item>
      <el-descriptions-item label="字符限制">最大 65535 字符</el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<style scoped>
.card-header { display: flex; align-items: center; justify-content: space-between; }
.editor-wrapper, .preview-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  overflow: hidden;
}
.editor-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  background: #f5f7fa;
  border-bottom: 1px solid #dcdfe6;
}
.editor-title { font-weight: 600; font-size: 14px; }
.ckeditor-toolbar {
  display: flex;
  flex-wrap: wrap;
  gap: 2px;
  padding: 6px;
  background: #fafafa;
  border-bottom: 1px solid #eee;
}
.toolbar-btn {
  padding: 4px 8px;
  font-weight: bold;
}
.editor-textarea {
  width: 100%;
  border: none;
  padding: 12px;
  font-family: monospace;
  font-size: 13px;
  resize: vertical;
  outline: none;
  box-sizing: border-box;
  background: #fff;
}
.editor-actions {
  padding: 8px 12px;
  border-top: 1px solid #eee;
  display: flex;
  gap: 8px;
}
.preview-content {
  padding: 16px;
  min-height: 260px;
  font-size: 14px;
  line-height: 1.8;
}
.preview-content :deep(h2) { font-size: 18px; margin-bottom: 12px; }
.preview-content :deep(strong) { font-weight: bold; }
.preview-content :deep(em) { font-style: italic; }
.preview-content :deep(u) { text-decoration: underline; }
.preview-content :deep(ul) { padding-left: 20px; margin: 8px 0; }
.preview-content :deep(table) { border-collapse: collapse; width: 100%; }
.preview-content :deep(td) { border: 1px solid #ddd; padding: 4px 8px; }
</style>
