<template>
  <div class="module-wrap">
    <el-alert title="后端: XXL-Job定时任务每日统计 | ECharts图表展示" type="info" :closable="false" show-icon class="tech-note" />
    <el-row :gutter="16">
      <el-col :span="14">
        <el-card shadow="hover">
          <template #header><span>用户增长趋势（过去7天）</span></template>
          <v-chart :option="barOption" style="height:300px" autoresize />
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="hover">
          <template #header><span>用户活跃占比</span></template>
          <v-chart :option="pieOption" style="height:300px" autoresize />
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="6" v-for="stat in stats" :key="stat.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value" :style="{ color: stat.color }">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, PieChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, TitleComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, BarChart, PieChart, GridComponent, TooltipComponent, LegendComponent, TitleComponent])

const days = ['1月9日', '1月10日', '1月11日', '1月12日', '1月13日', '1月14日', '1月15日']
const barOption = {
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: days },
  yAxis: { type: 'value' },
  series: [{
    name: '新增用户',
    type: 'bar',
    data: [42, 68, 55, 91, 73, 88, 104],
    itemStyle: { color: '#1890ff' },
  }],
}

const pieOption = {
  tooltip: { trigger: 'item' },
  legend: { bottom: 0 },
  series: [{
    type: 'pie',
    radius: ['40%', '70%'],
    data: [
      { value: 521, name: '活跃用户', itemStyle: { color: '#52c41a' } },
      { value: 143, name: '非活跃用户', itemStyle: { color: '#faad14' } },
      { value: 98, name: '新注册用户', itemStyle: { color: '#1890ff' } },
    ],
  }],
}

const stats = [
  { label: '总用户数', value: '1,284', color: '#1890ff' },
  { label: '今日新增', value: '104', color: '#52c41a' },
  { label: '活跃用户', value: '521', color: '#faad14' },
  { label: '禁用用户', value: '37', color: '#ff4d4f' },
]
</script>

<style scoped>
.module-wrap { padding: 8px 0; }
.tech-note { margin-bottom: 16px; }
.stat-card { text-align: center; padding: 8px 0; }
.stat-value { font-size: 32px; font-weight: bold; }
.stat-label { color: #999; font-size: 14px; margin-top: 4px; }
</style>
