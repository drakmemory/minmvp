<script setup>
import { ref, onMounted, computed } from 'vue'
import { useAppStore } from '../../store/index.js'
import * as echarts from 'echarts'

const store = useAppStore()
const barChartRef = ref(null)
const pieChartRef = ref(null)
let barChart = null
let pieChart = null

const stats = computed(() => store.statisticsData)

onMounted(() => {
  initBarChart()
  initPieChart()
})

function initBarChart() {
  barChart = echarts.init(barChartRef.value)
  barChart.setOption({
    title: { text: '近12日新增用户趋势', left: 'center' },
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: stats.value.daily.map(d => d.date)
    },
    yAxis: { type: 'value', name: '用户数' },
    series: [{
      name: '新增用户',
      type: 'bar',
      data: stats.value.daily.map(d => d.count),
      itemStyle: { color: '#409EFF' },
      label: { show: true, position: 'top' }
    }]
  })
}

function initPieChart() {
  pieChart = echarts.init(pieChartRef.value)
  pieChart.setOption({
    title: { text: '用户角色分布', left: 'center' },
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { bottom: 10 },
    series: [{
      name: '角色分布',
      type: 'pie',
      radius: ['40%', '70%'],
      data: stats.value.roleDistribution,
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }]
  })
}
</script>

<template>
  <div>
    <!-- Stats Summary Cards -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <el-icon size="40" color="#409EFF"><UserFilled /></el-icon>
            <div>
              <div class="stat-value">{{ store.totalUsers }}</div>
              <div class="stat-label">总用户数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <el-icon size="40" color="#67C23A"><Check /></el-icon>
            <div>
              <div class="stat-value">{{ store.activeUsers }}</div>
              <div class="stat-label">活跃用户</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <el-icon size="40" color="#E6A23C"><TrendCharts /></el-icon>
            <div>
              <div class="stat-value">{{ stats.daily[stats.daily.length - 1]?.count }}</div>
              <div class="stat-label">昨日新增</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <el-icon size="40" color="#F56C6C"><DataAnalysis /></el-icon>
            <div>
              <div class="stat-value">{{ stats.daily.reduce((s, d) => s + d.count, 0) }}</div>
              <div class="stat-label">本月累计</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Charts -->
    <el-row :gutter="16">
      <el-col :span="14">
        <el-card>
          <template #header>
            <div class="card-header">
              <span><el-icon><TrendCharts /></el-icon> 用户增长趋势（柱状图）</span>
              <div>
                <el-tag size="small" type="warning">XXL-Job 2.0.1 每日统计</el-tag>
                <el-tag size="small" type="success">ECharts 5.x</el-tag>
              </div>
            </div>
          </template>
          <div ref="barChartRef" style="height: 300px" />
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card>
          <template #header>
            <div class="card-header">
              <span><el-icon><PieChart /></el-icon> 用户角色分布（饼图）</span>
              <el-tag size="small" type="success">ECharts 5.x</el-tag>
            </div>
          </template>
          <div ref="pieChartRef" style="height: 300px" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.stats-row { margin-bottom: 16px; }
.stat-card { text-align: center; }
.stat-content { display: flex; align-items: center; gap: 16px; justify-content: center; padding: 8px 0; }
.stat-value { font-size: 28px; font-weight: bold; color: #303133; }
.stat-label { color: #909399; font-size: 13px; }
.card-header { display: flex; align-items: center; justify-content: space-between; }
.card-header span { display: flex; align-items: center; gap: 6px; }
</style>
