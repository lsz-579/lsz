<template>
  <div>
    <!-- 统计卡片 -->
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="8">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 14px; color: #909399">藏书总数</div>
            <div style="font-size: 36px; color: #409EFF; font-weight: bold; margin: 10px 0">
              {{ stats.totalBooks }}
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 14px; color: #909399">借阅总次数</div>
            <div style="font-size: 36px; color: #67C23A; font-weight: bold; margin: 10px 0">
              {{ stats.totalBorrows }}
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 14px; color: #909399">当前在借</div>
            <div style="font-size: 36px; color: #E6A23C; font-weight: bold; margin: 10px 0">
              {{ stats.activeBorrows }}
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <div slot="header" style="font-weight: bold">图书分类分布</div>
          <div ref="categoryChart" style="height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" style="font-weight: bold">热门借阅排行</div>
          <div ref="topChart" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import statsApi from '../api/statistics';

export default {
  name: 'Statistics',
  data() {
    return {
      stats: {
        totalBooks: 0,
        totalBorrows: 0,
        activeBorrows: 0,
        categoryDistribution: [],
        topBorrowedBooks: []
      },
      categoryChart: null,
      topChart: null
    };
  },
  mounted() {
    this.fetchStats();
  },
  beforeDestroy() {
    this.categoryChart?.dispose();
    this.topChart?.dispose();
  },
  methods: {
    fetchStats() {
      statsApi.getStats().then(res => {
        this.stats = res.data;
        this.$nextTick(() => {
          this.renderCategoryChart();
          this.renderTopChart();
        });
      });
    },
    renderCategoryChart() {
      if (this.categoryChart) this.categoryChart.dispose();
      this.categoryChart = echarts.init(this.$refs.categoryChart);

      const data = this.stats.categoryDistribution || [];
      this.categoryChart.setOption({
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie',
          radius: ['45%', '70%'],
          data: data.map(item => ({
            name: item.name,
            value: item.value
          })),
          label: { show: true, formatter: '{b}: {c}' }
        }],
        color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
      });
    },
    renderTopChart() {
      if (this.topChart) this.topChart.dispose();
      this.topChart = echarts.init(this.$refs.topChart);

      const data = this.stats.topBorrowedBooks || [];
      this.topChart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '10%', bottom: '3%', containLabel: true },
        xAxis: { type: 'value', name: '借阅次数' },
        yAxis: {
          type: 'category',
          data: data.map(item => item.title).reverse(),
          axisLabel: {
            width: 80,
            overflow: 'truncate'
          }
        },
        series: [{
          type: 'bar',
          data: data.map(item => item.borrowCount).reverse(),
          itemStyle: { color: '#409EFF' }
        }]
      });
    }
  }
};
</script>
