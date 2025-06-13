<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" placeholder="请输入车牌号查询" style="width: 240px"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="name" label="车牌号"></el-table-column>
        <el-table-column prop="userName" label="所属用户"></el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>

    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="车辆信息" v-model="data.formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :rules="data.rules" :model="data.form" label-width="80px"  style="padding: 20px 30px" ref="formRef">
        <el-form-item label="车牌号" prop="name">
          <el-input v-model="data.form.name" placeholder="车牌号"></el-input>
        </el-form-item>
        <el-form-item label="所属用户" prop="userId" v-if="data.user.role === 'ADMIN'">
          <el-select style="width: 100%" v-model="data.form.userId">
            <el-option v-for="item in data.userList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { reactive, ref } from "vue"
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";
const baseUrl = import.meta.env.VITE_BASE_URL
const data = reactive({
  user: JSON.parse(localStorage.getItem('loginUser') || '{}'),
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {},
  ids: [],
  name: null,
  userList: [],
  rules: {
    name: [
      { required: true, message: '请输入车牌号', trigger: 'blur' },
    ],
    userId: [
      { required: true, message: '请选择用户', trigger: 'blur' },
    ]
  }
})
const formRef = ref()
const loadUser = () => {
  request.get('/user/selectAll').then(res => {
    if (res.code === 200) {
      data.userList = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadUser()
// 加载表格数据
const load = () => {
  request.get('/vehicle/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total
  })
}

// 打开新增弹窗
const handleAdd = () => {
  data.form = {}
  if (data.user.role === 'USER') {
    data.form.userId = data.user.id
  }
  data.formVisible = true
}

// 打开编辑弹窗
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// 新增
const add = () => {
  request.post('/vehicle/add', data.form).then(res => {
    if (res.code === 200) {
      data.formVisible = false
      ElMessage.success('操作成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 更新
const update = () => {
  request.put('/vehicle/update', data.form).then(res => {
    if (res.code === 200) {
      data.formVisible = false
      ElMessage.success('操作成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 删除
const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/vehicle/delete/' + id).then(res => {
      if (res.code === 200) {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

// 批量删除
const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/vehicle/delete/batch', {data: data.ids}).then(res => {
      if (res.code === 200) {
        ElMessage.success('操作成功')
        load()  // 刷新表格数据
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => console.log(err))
}

const save = () => {
  formRef.value.validate(valid => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

const reset = () => {
  data.name = null
  load()
}

load()

</script>