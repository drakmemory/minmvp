import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    userToken: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IueuoeeQhuWPsyIsInJvbGUiOiJhZG1pbiIsImlhdCI6MTUxNjIzOTAyMn0.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c',
    currentUser: { name: '管理员', role: 'admin', avatar: '' },
    messages: [
      { id: 1, user: '张三', action: '登录系统', timestamp: '2024-01-15 09:00:01', ip: '192.168.1.1' },
      { id: 2, user: '李四', action: '注册账号', timestamp: '2024-01-15 09:15:32', ip: '192.168.1.2' },
      { id: 3, user: '王五', action: '更新用户信息', timestamp: '2024-01-15 09:30:45', ip: '192.168.1.3' },
      { id: 4, user: '赵六', action: '导出Excel报表', timestamp: '2024-01-15 10:00:10', ip: '192.168.1.4' },
      { id: 5, user: '管理员', action: '删除用户', timestamp: '2024-01-15 10:20:55', ip: '192.168.1.100' },
    ],
    taskStatus: [
      { id: 1, name: '每日用户统计', lastRun: '2024-01-15 00:00:05', nextRun: '2024-01-16 00:00:00', status: 'success' },
      { id: 2, name: '数据备份', lastRun: '2024-01-15 02:00:12', nextRun: '2024-01-16 02:00:00', status: 'success' },
      { id: 3, name: '邮件发送', lastRun: '2024-01-15 08:00:03', nextRun: '2024-01-15 12:00:00', status: 'running' },
      { id: 4, name: '缓存清理', lastRun: '2024-01-14 23:00:08', nextRun: '2024-01-15 23:00:00', status: 'fail' },
    ],
  }),
  actions: {
    hasPermission(role, permission) {
      const permMap = {
        admin: ['read', 'write', 'delete', 'export', 'import', 'manage'],
        user: ['read', 'write', 'export'],
        guest: ['read'],
      }
      return (permMap[role] || []).includes(permission)
    },
    setToken(token) {
      this.userToken = token
    },
  },
import dayjs from 'dayjs'

export const useAppStore = defineStore('app', {
  state: () => ({
    // JWT auth
    token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.demo',
    currentUser: {
      id: 1,
      username: 'admin',
      name: '管理员',
      role: 'admin',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
    },
    // User management (simulated MySQL + Redis cache)
    users: [
      { id: 1, username: 'admin', name: '张三', phone: '13800138001', email: 'admin@example.com', role: 'admin', status: 1, createTime: '2024-01-01 10:00:00' },
      { id: 2, username: 'user1', name: '李四', phone: '13800138002', email: 'user1@example.com', role: 'user', status: 1, createTime: '2024-01-05 09:30:00' },
      { id: 3, username: 'user2', name: '王五', phone: '13800138003', email: 'user2@example.com', role: 'user', status: 1, createTime: '2024-01-10 14:20:00' },
      { id: 4, username: 'user3', name: '赵六', phone: '13800138004', email: 'user3@example.com', role: 'user', status: 0, createTime: '2024-01-15 16:00:00' },
      { id: 5, username: 'user4', name: '钱七', phone: '13800138005', email: 'user4@example.com', role: 'user', status: 1, createTime: '2024-01-20 11:00:00' },
      { id: 6, username: 'manager', name: '孙八', phone: '13800138006', email: 'manager@example.com', role: 'manager', status: 1, createTime: '2024-01-25 08:00:00' },
    ],
    // Statistics data (simulated XXL-Job daily task results)
    statisticsData: {
      daily: [
        { date: '01-20', count: 12 }, { date: '01-21', count: 18 }, { date: '01-22', count: 15 },
        { date: '01-23', count: 22 }, { date: '01-24', count: 28 }, { date: '01-25', count: 20 },
        { date: '01-26', count: 35 }, { date: '01-27', count: 30 }, { date: '01-28', count: 42 },
        { date: '01-29', count: 38 }, { date: '01-30', count: 55 }, { date: '01-31', count: 48 }
      ],
      roleDistribution: [
        { name: '管理员', value: 1 },
        { name: '普通用户', value: 4 },
        { name: '管理人员', value: 1 }
      ]
    },
    // Kafka operation logs (simulated messages)
    messages: [
      { id: 1, type: 'login', user: '张三', content: '用户登录系统', time: dayjs().subtract(2, 'minute').format('HH:mm:ss'), status: 'success' },
      { id: 2, type: 'create', user: '管理员', content: '新增用户: 钱七', time: dayjs().subtract(5, 'minute').format('HH:mm:ss'), status: 'success' },
      { id: 3, type: 'update', user: '李四', content: '修改个人信息', time: dayjs().subtract(10, 'minute').format('HH:mm:ss'), status: 'success' },
      { id: 4, type: 'delete', user: '管理员', content: '删除用户: test001', time: dayjs().subtract(15, 'minute').format('HH:mm:ss'), status: 'warning' },
      { id: 5, type: 'export', user: '孙八', content: 'Excel导出用户数据', time: dayjs().subtract(20, 'minute').format('HH:mm:ss'), status: 'success' },
    ],
    // XXL-Job task status
    tasks: [
      { id: 1, name: '每日用户统计任务', cron: '0 0 1 * * ?', lastRun: dayjs().subtract(1, 'day').format('YYYY-MM-DD 01:00:00'), status: 'success', duration: '1.2s' },
      { id: 2, name: 'Elasticsearch数据同步', cron: '0 */30 * * * ?', lastRun: dayjs().subtract(30, 'minute').format('YYYY-MM-DD HH:mm:ss'), status: 'success', duration: '3.5s' },
      { id: 3, name: 'Redis缓存刷新任务', cron: '0 0 */2 * * ?', lastRun: dayjs().subtract(2, 'hour').format('YYYY-MM-DD HH:mm:ss'), status: 'failed', duration: '0.8s' },
      { id: 4, name: '文件清理定时任务', cron: '0 0 3 * * ?', lastRun: dayjs().subtract(1, 'day').format('YYYY-MM-DD 03:00:00'), status: 'success', duration: '5.1s' },
    ],
    // File list (simulated FastDFS/OSS)
    files: [
      { id: 1, name: '产品说明书.pdf', size: '2.4 MB', type: 'pdf', url: '#', uploadTime: '2024-01-28 10:00:00', uploader: '管理员' },
      { id: 2, name: '用户数据报表.xlsx', size: '156 KB', type: 'excel', url: '#', uploadTime: '2024-01-29 14:30:00', uploader: '孙八' },
      { id: 3, name: '系统架构图.png', size: '890 KB', type: 'image', url: 'https://via.placeholder.com/800x600', uploadTime: '2024-01-30 09:15:00', uploader: '张三' },
      { id: 4, name: '操作手册.docx', size: '1.1 MB', type: 'word', url: '#', uploadTime: '2024-01-31 16:00:00', uploader: '管理员' },
    ],
    // Search results (simulated ES results)
    searchResults: [],
    searchLoading: false,
    // Rich text content
    richTextContent: '<h2>公告标题</h2><p>这是一段富文本内容，支持 <strong>加粗</strong>、<em>斜体</em>、<u>下划线</u> 等格式。</p><ul><li>列表项一</li><li>列表项二</li></ul>',
  }),

  getters: {
    isAdmin: (state) => state.currentUser.role === 'admin',
    totalUsers: (state) => state.users.length,
    activeUsers: (state) => state.users.filter(u => u.status === 1).length,
  },

  actions: {
    // Simulate Kafka message push
    addMessage(msg) {
      this.messages.unshift({
        id: Date.now(),
        ...msg,
        time: dayjs().format('HH:mm:ss')
      })
      if (this.messages.length > 20) this.messages.pop()
    },

    // Simulate user CRUD (would call backend API in production)
    addUser(user) {
      const newUser = { ...user, id: Date.now(), createTime: dayjs().format('YYYY-MM-DD HH:mm:ss') }
      this.users.push(newUser)
      this.addMessage({ type: 'create', user: this.currentUser.name, content: `新增用户: ${user.name}`, status: 'success' })
    },
    updateUser(updated) {
      const idx = this.users.findIndex(u => u.id === updated.id)
      if (idx !== -1) this.users[idx] = { ...this.users[idx], ...updated }
      this.addMessage({ type: 'update', user: this.currentUser.name, content: `修改用户: ${updated.name}`, status: 'success' })
    },
    deleteUser(id) {
      const user = this.users.find(u => u.id === id)
      this.users = this.users.filter(u => u.id !== id)
      this.addMessage({ type: 'delete', user: this.currentUser.name, content: `删除用户: ${user?.name}`, status: 'warning' })
    },

    // Simulate ES full-text search
    async searchUsers(keyword) {
      this.searchLoading = true
      await new Promise(r => setTimeout(r, 600))
      if (!keyword.trim()) {
        this.searchResults = []
      } else {
        this.searchResults = this.users.filter(u =>
          u.name.includes(keyword) || u.phone.includes(keyword) || u.username.includes(keyword)
        )
      }
      this.searchLoading = false
    },

    setRichTextContent(content) {
      this.richTextContent = content
    }
  }
})
