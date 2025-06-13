<template>
  <div>
    <div class="card" id="line" style="height: 350px; border: 1px solid #ddd; padding: 10px; margin-bottom: 10px;"></div>
    <div style="margin-top: 10px; display: flex; gap: 10px">
      <div class="card" id="bar" style="height: 350px; flex: 1; border: 1px solid #ddd; padding: 10px;"></div>
      <div class="card" id="pie" style="height: 350px; flex: 1; border: 1px solid #ddd; padding: 10px;"></div>
    </div>
  </div>
</template>

<script setup>
import {reactive, onMounted, nextTick} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import * as echarts from "echarts";

const data = reactive({
  user: JSON.parse(localStorage.getItem('loginUser') || '{}'),
})

const loadLine = async () => {
  try {
    const res = await request.get('/dashboard/line');
    if (res.code === 200) {
      await nextTick(); // 确保DOM已渲染
      let chartDom = document.getElementById('line');
      if (chartDom) {
        let myChart = echarts.init(chartDom);
        lineOptions.xAxis.data = res.data.xList;
        lineOptions.series[0].data = res.data.yList;
        myChart.setOption(lineOptions);
      }
    } else {
      ElMessage.error(res.msg);
    }
  } catch (error) {
    console.error('加载折线图数据失败:', error);
    ElMessage.error('加载折线图数据失败');
  }
}

const loadPie = async () => {
  try {
    const res = await request.get('/dashboard/pie');
    if (res.code === 200) {
      await nextTick();
      let chartDom = document.getElementById('pie');
      if (chartDom) {
        let myChart = echarts.init(chartDom);
        // 根据实际数据结构调整
        if (Array.isArray(res.data)) {
          pieOptions.series[0].data = res.data;
        } else if (res.data.xList && res.data.yList) {
          // 如果返回的是xList和yList格式，转换为饼图需要的格式
          pieOptions.series[0].data = res.data.xList.map((name, index) => ({
            name: name,
            value: res.data.yList[index]
          }));
        }
        myChart.setOption(pieOptions);
      }
    } else {
      ElMessage.error(res.msg);
    }
  } catch (error) {
    console.error('加载饼图数据失败:', error);
    ElMessage.error('加载饼图数据失败');
  }
}

const loadBar = async () => {
  try {
    const res = await request.get('/dashboard/bar');
    if (res.code === 200) {
      await nextTick();
      let chartDom = document.getElementById('bar');
      if (chartDom) {
        let myChart = echarts.init(chartDom);
        barOptions.xAxis.data = res.data.xList;
        barOptions.series[0].data = res.data.yList;
        myChart.setOption(barOptions);
      }
    } else {
      ElMessage.error(res.msg);
    }
  } catch (error) {
    console.error('加载柱状图数据失败:', error);
    ElMessage.error('加载柱状图数据失败');
  }
}

// 在组件挂载后加载数据
onMounted(async () => {
  await loadLine();
  await loadPie();
  await loadBar();
});

// 平滑折线图配置
let lineOptions = {
  title: {
    text: '近一周停车场车辆入场数量趋势图',
    subtext: '统计维度：最近一周',
    left: 'center'
  },
  legend: {
    data: ['停车数量']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  tooltip: {
    trigger: 'item'
  },
  xAxis: {
    name: '日期',
    type: 'category',
    data: [] // 将从API获取
  },
  yAxis: {
    name: '停车入场数量',
    type: 'value'
  },
  series: [
    {
      name: '停车数量',
      data: [], // 将从API获取
      type: 'line',
      smooth: true,
      markLine: {
        data: [{ type: 'average', name: '最近一周停车数量平均值' }]
      },
      markPoint: {
        data: [
          { type: 'max', name: '最大值' },
          { type: 'min', name: '最小值' }
        ]
      },
    },
  ]
};

// 饼图配置
let pieOptions = {
  title: {
    text: '停车场车位状态统计饼状图',
    subtext: '统计维度：车位状态',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '数量占比',
      type: 'pie',
      radius: '50%',
      center: ['50%', '60%'],
      data: [] // 将从API获取
    }
  ]
}

// 柱状图配置
let barOptions = {
  title: {
    text: '停车场不同区域位置停车位数量统计柱状图',
    subtext: '统计维度：停车区域',
    left: 'center'
  },
  grid: {
    bottom: '10%'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: [], // 将从API获取
    name: '停车区域',
    axisLabel: {
      show: true,
      interval: 0,
      rotate: -45, // 调整旋转角度
      inside: false,
      margin: 6,
    },
  },
  yAxis: {
    type: 'value',
    name: '车位数量',
  },
  tooltip: {
    trigger: 'item',
  },
  series: [
    {
      data: [], // 将从API获取
      type: 'bar',
      itemStyle: {
        normal: {
          color: function () {
            return "#" + Math.floor(Math.random() * (256 * 256 * 256 - 1)).toString(16);
          }
        },
      },
    }
  ]
};
</script>

<style scoped>
.card {
  background: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  border-radius: 4px;
}
</style>