<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.name"
        placeholder="公司名"
        style="width: 200px; margin-left: 5px"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />

      <el-button
        v-waves
        class="filter-item"
        style="margin-left: 10px"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >搜索</el-button>

      <el-button
        class="filter-item"
        style="margin-left: 10px"
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
      style="width: 100%; margin-top: 3px"
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

      <el-table-column label="公司名" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.companyName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="联系人" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.contacts }}</span>
        </template>
      </el-table-column>

      <el-table-column label="电话" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.tel }}</span>
        </template>
      </el-table-column>

      <el-table-column label="邮箱" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.email }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{ row, $index }">
          <el-button
            type="primary"
            size="mini"
            @click="handleUpdate(row)"
          >编辑</el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(row, $index)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 底部分页组件 -->
    <pagination
      v-show="total > 0"
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
        :model="company"
        label-position="left"
        label-width="70px"
        style="width: 400px; margin-left: 50px"
      >
        <el-form-item label="公司名" prop="companyName" label-width="100px">
          <el-input v-model="company.companyName" />
        </el-form-item>

        <el-form-item label="联系人" prop="contacts" label-width="100px">
          <el-input v-model="company.contacts" />
        </el-form-item>

        <el-form-item label="联系方式" prop="tel" label-width="100px">
          <el-input v-model="company.tel" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email" label-width="100px">
          <el-input v-model="company.email" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="dialogStatus === 'create' ? createData() : updateData()"
        >提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { selectByPage, createCompany, updateCompany, deleteCompany } from '@/api/auth/company'
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
      company: {
        companyName: '',
        contacts: '',
        tel: '',
        email: '',
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
        update: '编辑公司信息',
        create: '新增公司信息'
      },
      rules: {
        companyName: [{ required: true, message: '公司名为必填字段', trigger: 'blur' }],
        contacts: [{ required: true, message: '联系人为必填字段', trigger: 'blur' }],
        tel: [{ required: true, validator: this.checkPhone, trigger: 'blur' }],
        email: [{ required: true, validator: this.checkEmail, trigger: 'blur' }]
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
    resetCompany() {
      this.company = {
        companyName: '',
        contacts: '',
        tel: '',
        email: '',
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
      this.resetCompany()
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
          const tempData = Object.assign({}, this.company)
          tempData.createPerson = store.getters.userObj.userId
          createCompany(tempData).then((response) => {
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
      this.company = Object.assign({}, row) // copy obj
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
          const tempData = Object.assign({}, this.company)
          tempData.modifiedPerson = store.getters.userObj.userId
          updateCompany(tempData).then((response) => {
            const index = this.list.findIndex(v => v.companyId === this.company.companyId)
            this.list.splice(index, 1, tempData)
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
      const tempData = Object.assign({}, row)
      tempData.isDeleted = 1
      tempData.deletedPerson = store.getters.userObj.userId
      deleteCompany(tempData).then((response) => {
        const index = this.list.findIndex(v => v.companyId === tempData.companyId)
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
