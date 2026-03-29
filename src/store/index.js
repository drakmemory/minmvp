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
})
