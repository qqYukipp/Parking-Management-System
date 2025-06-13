<template>
  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入昵称查询"></el-input>
      <el-input v-model="data.username" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入用户名查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
<!-- 弃用新增     <el-button type="primary" plain @click="handleAdd">新增</el-button>-->
      <el-button type="danger" plain @click="downToUser">取消管理员权限</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickName" label="昵称"/>
        <el-table-column prop="avatar" label="头像">
          <template v-slot="scope">
            <el-image style="width: 40px; height: 40px; border-radius: 50%; display: block" v-if="scope.row.avatar"
                      :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="email" label="邮箱" />
        <!--        <el-table-column prop="role" label="角色" />-->
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card" v-if="data.total">
      <el-pagination
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          background
          layout="total, sizes, prev, pager, next, jumper"
          v-model:page-size="data.pageSize"
          v-model:current-page="data.pageNum"
          :page-sizes="data.pageSizes"
          :total="data.total"/>
    </div>

    <el-dialog v-model="data.formVisible" title="管理员信息" width="40%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item prop="username" label="用户名" >
          <el-input v-model="data.form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <!--        需要密码-->            <!--废弃-->
        <!--        <el-form-item prop="password" label="密码">
                  <el-input
                      v-model="data.form.password"
                      type="password"
                      placeholder="请输入密码"
                      show-password
                  ></el-input>
                </el-form-item>-->

        <el-form-item prop="name" label="昵称">
          <el-input v-model="data.form.nickName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item prop="phone" label="电话">
          <el-input v-model="data.form.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="data.form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item prop="avatar" label="头像">
          <el-upload
              class="avatar-uploader"
              :show-file-list="false"
              :http-request="uploadWithRequest"
              :on-success="handleFileUpload"
              :before-upload="beforeAvatarUpload"
          >
            <img v-if="data.form.avatar" alt="" :src="data.form.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            <el-button type="primary">修改头像</el-button>
          </el-upload>

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
import {reactive} from "vue"
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit, Plus} from "@element-plus/icons-vue";
import {useUserStore} from "@/stores/user.js";

const data = reactive({
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数，默认改为10
  pageSizes: [5, 10, 15, 20, 50],  // 可选的每页记录数选项
  formVisible: false,
  form: {},
  ids: [],
  name: null,
  username: null,
})

const userStore = useUserStore()

// 加载表格数据
const load = () => {
  request.get('/admin/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      nickName: data.name,
      username: data.username
    }
  }).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total
  })
}

// 处理页码变化
const handleCurrentChange = (page) => {
  data.pageNum = page
  load()
}

// 处理每页记录数变化
const handleSizeChange = (size) => {
  data.pageSize = size
  data.pageNum = 1  // 重置到第一页
  load()
}

// 打开新增弹窗
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

// 打开编辑弹窗
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// 新增
const add = () => {
  request.post('/admin/add', data.form).then(res => {
    if (res.code === 200) {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 更新
const update = () => {
  request.put('/admin/update', data.form).then(res => {
    if (res.code === 200) {
      ElMessage.success('操作成功')
      userStore.setUserInfo(data.form)
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 删除
const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/admin/delete/' + id).then(res => {
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

// 批量选择id
const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const downToUser = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择用户")
    return
  }
  ElMessageBox.confirm('您确定取消该用户的管理员身份吗?', '取消管理员权限', { type: 'warning' }).then(res => {
    request.put('/admin/downToUser',  data.ids).then(res => {
      if (res.code === 200) {
        ElMessage.success('操作成功')
        load()  // 刷新表格数据
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => console.log(err))
}

// 批量删除
const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/admin/delete/batch', {data: data.ids}).then(res => {
      if (res.code === 200) {
        ElMessage.success('操作成功')
        load()  // 刷新表格数据
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => console.log(err))
}

// 保存
const save = () => {
  data.form.id ? update() : ElMessage.warning("请选择数据")
}

// 头像上传
const handleFileUpload = (res) => {
  data.form.avatar = res.data
}

// 上传头像成功处理
/*const handleFileUpload = (res) => {
  //userStore.userInfo.avatar = res.data
  userData.value.avatar = res.data   // ← 同步更新到本地表单数据
}*/

const uploadWithRequest = async (uploadOption) => {
  // uploadOption.file 是当前要上传的 File 对象
  const form = new FormData()
  form.append('file', uploadOption.file)

  try {
    const res = await request.post('/upload', form, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    // 成功后手动调用 el-upload 的 onSuccess
    uploadOption.onSuccess(res, uploadOption.file)
    // 更新 avatar
    //userStore.userInfo.avatar = res.data
  } catch (err) {
    uploadOption.onError(err)
    ElMessage.error('上传失败')
  }
}

// 文件上传之前触发
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('只支持上传图片')
    return false
  } else if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error('只能上传10M以内图片')
    return false
  }
  return true
}

// 搜索重置
const reset = () => {
  data.name = ''
  data.username = ''
  load()
}

// 调用查询方法
load()

</script>


<style scoped>
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>
