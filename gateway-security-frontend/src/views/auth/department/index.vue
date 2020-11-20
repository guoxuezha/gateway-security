<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button
        v-waves
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-refresh"
        @click="handleFilter"
      >刷新</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >新增</el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;margin-top:3px"
    >
      <el-table-column
        label="序号"
        type="index"
        :index="table_index"
        prop="id"
        align="center"
        width="80"
        :class-name="getSortClass('id')"
      />

      <el-table-column label="单位名" align="center">
        <template slot-scope="{row}">
          <span>{{ row.departmentName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 底部分页组件 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <!-- 弹窗编辑 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="department"
        label-position="left"
        label-width="70px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="单位名" prop="departmentName" label-width="100px">
          <el-input v-model="department.departmentName" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { selectByPage, createDepartment, updateDepartment, deleteDepartment } from '@/api/auth/department'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import store from '@/store'

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {},
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        name: ''
      },
      department: {
        departmentName: '',
        create_time: undefined,
        create_person: undefined,
        modified_time: undefined,
        modified_person: undefined,
        deleted_time: undefined,
        deleted_person: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑单位信息',
        create: '新增单位信息'
      },
      rules: {
        departmentName: [{ required: true, message: '单位名为必填字段', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /**
     * 列表初始化
     */
    getList() {
      this.listLoading = true
      selectByPage(this.listQuery).then(response => {
        this.list = response.data
        this.total = response.total
        setTimeout(() => {
          this.listLoading = false
        }, 0.3 * 1000)
      })
    },
    // 分页累加序号
    table_index(index) {
      return (this.listQuery.page - 1) * this.listQuery.limit + index + 1
    },
    // 列表刷新重置
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    // 初始化弹出表单的数据
    resetDepartment() {
      this.department = {
        departmentName: '',
        create_time: undefined,
        create_person: undefined,
        modified_time: undefined,
        modified_person: undefined,
        deleted_time: undefined,
        deleted_person: undefined
      }
    },
    // 弹出新增窗口时设置参数
    handleCreate() {
      this.resetDepartment()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 执行新增
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.department.createPerson = store.getters.userObj.userId
          createDepartment(this.department).then((response) => {
            if (response.status === 1) {
              this.total++
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '添加成功！',
                type: 'success',
                duration: 2000
              })
              this.list.unshift(response.data)
            } else {
              this.$notify({
                title: '失败',
                message: '添加失败！',
                type: 'fail',
                duration: 2000
              })
            }
          })
        }
      })
    },
    // 弹出修改窗口时设置参数
    handleUpdate(row) {
      this.department = Object.assign({}, row) // copy obj
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 执行修改
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.department)
          tempData.modifiedPerson = store.getters.userObj.userId
          console.log(tempData)
          updateDepartment(tempData).then((response) => {
            const index = this.list.findIndex(v => v.departmentId === this.department.departmentId)
            this.list.splice(index, 1, response.data)
            this.dialogFormVisible = false
            if (response.status === 1) {
              this.$notify({
                title: '成功',
                message: '编辑成功！',
                type: 'success',
                duration: 2000
              })
            } else {
              this.$notify({
                title: '失败',
                message: '编辑失败！',
                type: 'fail',
                duration: 2000
              })
            }
          })
        }
      })
    },
    // 执行删除
    handleDelete(row, index) {
      this.department = Object.assign({}, row)
      this.department.isDeleted = 1
      this.department.deletedPerson = store.getters.userObj.userId
      deleteDepartment(this.department).then((response) => {
        const index = this.list.findIndex(v => v.departmentId === this.department.departmentId)
        if (response.status === 1) {
          this.total--
          this.list.splice(index, 1)
          this.dialogFormVisible = false
          this.$notify({
            title: '成功',
            message: '删除成功！',
            type: 'success',
            duration: 2000
          })
        } else {
          this.$notify({
            title: '失败',
            message: '删除失败！',
            type: 'fail',
            duration: 2000
          })
        }
      })
    },
    // 点击ID拦执行列表排序
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    },
    // 手机号格式校验
    checkPhone: function(rule, value, callback) {
      const phoneReg = /^1[3|4|5|7|8][0-9]{9}$/
      if (!value) {
        return callback(new Error('电话号码不能为空'))
      }
      setTimeout(() => {
        // Number.isInteger是es6验证数字是否为整数的方法,但是我实际用的时候输入的数字总是识别成字符串
        // 所以我就在前面加了一个+实现隐式转换

        if (!Number.isInteger(+value)) {
          callback(new Error('请输入数字值'))
        } else {
          if (phoneReg.test(value)) {
            callback()
          } else {
            callback(new Error('电话号码格式不正确'))
          }
        }
      }, 100)
    },
    // 邮箱格式校验
    checkEmail: function(rule, value, callback) {
      const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/
      if (!value) {
        return callback(new Error('邮箱不能为空'))
      }
      setTimeout(() => {
        if (mailReg.test(value)) {
          callback()
        } else {
          callback(new Error('请输入正确的邮箱格式'))
        }
      }, 100)
    }
  }
}
</script>
