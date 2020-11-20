<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select
        v-model="listQuery.roleId"
        placeholder="角色"
        style="width: 140px"
        class="filter-item"
      >
        <el-option
          v-for="item in roleOptions"
          :key="item.roleId"
          :label="item.roleDescription"
          :value="item.roleId"
        />
      </el-select>

      <el-select
        v-model="listQuery.companyId"
        placeholder="公司名"
        style="width: 140px"
        class="filter-item"
        filterable
      >
        <el-option
          v-for="item in companyOptions"
          :key="item.companyId"
          :label="item.companyName"
          :value="item.companyId"
        />
      </el-select>

      <el-input
        v-model="listQuery.name"
        placeholder="用户名"
        style="width: 200px;margin-left: 5px"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />

      <el-button
        v-waves
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >搜索</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >新增</el-button>
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        style="margin-left: 10px"
        @click="goCompany"
      >跳转</el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;margin-top:3px"
      @sort-change="sortChange"
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

      <el-table-column label="用户名" align="center" width="200">
        <template slot-scope="{row}">
          <span>{{ row.userName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="公司名" align="center">
        <template slot-scope="{row}">
          <span>{{ row.companyId | companyFilter(that.companyOptionsMaps) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="部门" align="center" width="90">
        <template slot-scope="{row}">
          <span>{{ row.departmentId | departmentFilter(that.departmentOptionsMaps) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="工号" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.jobNumber }}</span>
        </template>
      </el-table-column>

      <el-table-column label="用户状态" align="center" width="100">
        <template slot-scope="{row}">
          <span class="link-type">{{ row.isDeleted | statusFilter() }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="success" size="mini" @click="handleDistributeRole(row.userId)">分配角色</el-button>
          <el-button
            v-if="row.isDeleted !== 2"
            size="mini"
            type="warning"
            @click="updateUserStatus(row,$index,2)"
          >冻结</el-button>
          <el-button
            v-if="row.isDeleted !== 0"
            size="mini"
            type="success"
            @click="updateUserStatus(row,$index,0)"
          >解冻</el-button>
          <el-button
            v-if="row.isDeleted !== 1"
            size="mini"
            type="danger"
            @click="handleDelete(row,$index)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 底部分页组件 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getUserList"
    />

    <!-- 弹窗编辑 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="sysUser"
        label-position="left"
        label-width="70px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="所属公司" prop="hardware_name" label-width="100px">
          <el-select v-model="sysUser.companyId" filterable clearable placeholder="请选择">
            <el-option
              v-for="item in companyOptions.slice(1, that.companyOptions.length)"
              :key="item.companyId"
              :label="item.companyName"
              :value="item.companyId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="用户名" prop="userName" label-width="100px">
          <el-input v-model="sysUser.userName" />
        </el-form-item>

        <el-form-item label="密码" prop="password" label-width="100px" type="password">
          <el-input v-model="sysUser.password" />
        </el-form-item>

        <el-form-item label="部门" label-width="100px">
          <el-select
            v-model="sysUser.departmentId"
            placeholder="请选择部门"
            style="width: 140px"
            class="filter-item"
            filterable
          >
            <el-option
              v-for="item in departmentOptions.slice(1, departmentOptions.length)"
              :key="item.departmentId"
              :label="item.departmentName"
              :value="item.departmentId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="工号" label-width="100px">
          <el-input v-model="sysUser.jobNumber" />
        </el-form-item>

        <el-form-item label="用户描述" label-width="100px">
          <el-input
            v-model="sysUser.userDescription"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="请输入对该用户的描述"
          />
        </el-form-item>

        <el-form-item label="分配角色" label-width="100px">
          <el-checkbox-group v-model="sysUser.userRemark">
            <el-col v-for="(item,index) in roleOptions.slice(1, roleOptions.length)" :key="index">
              <el-checkbox :label="item.roleId+''" border size="small">{{ item.roleName }}</el-checkbox>
            </el-col>
          </el-checkbox-group>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">提交</el-button>
      </div>
    </el-dialog>

    <!-- 弹窗分配角色 -->
    <el-dialog title="分配角色" :visible.sync="dialogFormVisible2">
      <el-form
        ref="distributeRole"
        label-position="left"
        label-width="70px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="分配角色" label-width="100px">
          <el-checkbox-group v-model="checkedRoles">
            <el-col v-for="(item,index) in roleOptions.slice(1, roleOptions.length)" :key="index">
              <el-checkbox
                :key="item.roleId"
                :label="item.roleId"
                :value="item.roleId"
                border
                size="small"
              >{{ item.roleDescription }}</el-checkbox>
            </el-col>
          </el-checkbox-group>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible2 = false">取消</el-button>
        <el-button type="primary" @click="distributeRole()">提交</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { selectByPage, updateUserStatus, createUser, updateUser, deleteUser } from '@/api/auth/sysuser'
import { selectAllRole, getRoleIdByUserId, distributeRole } from '@/api/auth/sysrole'
import { selectNotDeletedCompany } from '@/api/auth/company'
import { selectNotDeletedDepartment } from '@/api/auth/department'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import store from '@/store'

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    // 用户状态过滤器
    statusFilter(status) {
      const statusMap = {
        0: '正常使用',
        1: '已删除',
        2: '已冻结'
      }
      return statusMap[status]
    },
    // 公司名过滤器
    companyFilter(companyId, companyOptionsMaps) {
      if (companyId > 0) {
        return companyOptionsMaps.get(companyId)
      } else {
        return '无'
      }
    },
    // 用户部门过滤器
    departmentFilter(departmentId, departmentOptionsMaps) {
      if (departmentId > 0) {
        return departmentOptionsMaps.get(departmentId)
      } else {
        return '无'
      }
    }
  },
  data() {
    return {
      that: this,
      tableKey: 0,
      // 根据用户名得到的公司信息
      userCompanyId: null,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        name: '',
        roleId: undefined,
        companyId: undefined
      },
      // 全部角色信息
      roleOptions: [],
      // 全部公司信息
      companyOptions: [],
      // 公司列表转为Map
      companyOptionsMaps: new Map(),
      // 全部部门信息
      departmentOptions: [],
      // 部门列表转为Map供Filter使用
      departmentOptionsMaps: new Map(),
      sysUser: {
        userName: '',
        password: '',
        userDescription: '',
        userRemark: [],
        companyId: undefined,
        departmentId: undefined,
        jobNumber: undefined,
        is_deleted: 0,
        create_time: undefined,
        create_person: undefined,
        modified_time: undefined,
        modified_person: undefined,
        deleted_time: undefined,
        deleted_person: undefined
      },
      dialogFormVisible: false,
      dialogFormVisible2: false,
      distributeUserId: 0,
      checkedRoles: [],
      dialogStatus: '',
      textMap: {
        update: '编辑用户',
        create: '新增用户'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        userName: [{ required: true, message: '用户名必填字段', trigger: 'blur' }],
        password: [{ required: true, message: '密码为必填字段', trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getCompanyId()
    this.getCompanyList()
    this.getDepartmentList()
    this.getRoleList()
    this.getUserList()
  },
  methods: {
    // 从store中获取当前登录用户的公司ID
    getCompanyId() {
      const userCompany = store.getters.company
      if (userCompany !== null && userCompany !== undefined) {
        const companyId = userCompany.companyId
        if (companyId !== null) {
          this.userCompanyId = companyId
        } else {
          this.userCompanyId = null
        }
      } else {
        this.userCompanyId = null
      }
      console.log('this.userCompanyId === ' + this.userCompanyId)
    },
    /**
     * 角色列表初始化
     */
    getRoleList() {
      selectAllRole().then(response => {
        this.roleOptions = response.data
        this.roleOptions.unshift({ roleId: 0, roleDescription: '全部' })
      })
    },
    /**
     * 用户列表初始化
     */
    getUserList() {
      this.listLoading = true
      this.listQuery.companyId = this.userCompanyId
      selectByPage(this.listQuery).then(response => {
        this.list = response.data
        this.total = response.total
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 0.3 * 1000)
      })
    },
    // 获取公司列表
    getCompanyList() {
      selectNotDeletedCompany(this.userCompanyId).then(response => {
        this.companyOptions = response.data
        this.companyOptions.unshift({ companyId: null, companyName: '全部' })
        this.companyOptions.slice(1, this.companyOptions.length).forEach(company => {
          this.companyOptionsMaps.set(company.companyId, company.companyName)
        })
      })
    },
    // 获取部门列表
    getDepartmentList() {
      selectNotDeletedDepartment().then(response => {
        this.departmentOptions = response.data
        this.departmentOptions.unshift({ companyId: undefined, companyName: '全部' })
        this.departmentOptions.slice(1, this.departmentOptions.length).forEach(department => {
          this.departmentOptionsMaps.set(department.departmentId, department.departmentName)
        })
      })
    },
    // 分页累加序号
    table_index(index) {
      return (this.listQuery.page - 1) * this.listQuery.limit + index + 1
    },
    // 列表刷新重置
    handleFilter() {
      this.listQuery.page = 1
      this.getRoleList()
      this.getUserList()
    },
    // 状态修改弹出Tip,暂时没用
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作Success',
        type: 'success'
      })
      row.status = status
    },
    // 列表排序
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    // 顺序/倒序
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    // 初始化弹出表单的数据
    resetSysUser() {
      this.sysUser = {
        userName: '',
        password: '',
        userDescription: '',
        userRemark: [],
        companyId: undefined,
        departmentId: undefined,
        jobNumber: undefined,
        is_deleted: 0,
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
      this.resetSysUser()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      console.log(this.roleOptions.slice(1, this.roleOptions.length))
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 执行新增
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 把chackbox的数组转为String
          if (this.sysUser.userRemark.length > 0) {
            this.sysUser.userRemark = this.sysUser.userRemark.join(',')
          }
          // 新增之前补充一部分信息
          this.sysUser.createPerson = store.getters.userObj.userId
          createUser(this.sysUser).then((response) => {
            if (response.status === 1) {
              this.total++
              this.list.unshift(response.data)
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '添加成功！',
                type: 'success',
                duration: 2000
              })
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
      this.sysUser = Object.assign({}, row) // copy obj
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
          const tempData = Object.assign({}, this.sysUser)
          tempData.modifiedPerson = store.getters.userObj.userId
          updateUser(tempData).then((response) => {
            const index = this.list.findIndex(v => v.userId === this.sysUser.userId)
            this.list.splice(index, 1, this.sysUser)
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
    // 删除用户
    handleDelete(row, index) {
      this.sysUser = Object.assign({}, row)
      deleteUser(this.sysUser.userId).then((response) => {
        const index = this.list.findIndex(v => v.userId === this.sysUser.userId)
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
    // 为用户分配角色
    handleDistributeRole(userId) {
      this.distributeUserId = userId
      this.checkedRoles = []
      // 通过ID查询roleList实现已有角色回显
      getRoleIdByUserId({ userId: userId }).then(response => {
        if (response.status === 1) {
          const hadRoleIds = response.data
          for (let i = 0; i < hadRoleIds.length; i++) {
            const hadRoleId = hadRoleIds[i].roleId
            const index = this.roleOptions.findIndex(v => v.roleId === hadRoleId)
            if (index > -1) {
              this.checkedRoles.unshift(hadRoleId)
            }
          }
        }
      })
      console.log(this.checkedRoles)
      this.dialogFormVisible2 = true
    },
    // 执行分配
    distributeRole() {
      distributeRole({ userId: this.distributeUserId, roleIds: this.checkedRoles.join(',') })
        .then(response => {
          this.dialogFormVisible2 = false
          if (response.status === 1) {
            this.$notify({
              title: '成功',
              message: '分配成功！',
              type: 'success',
              duration: 2000
            })
          } else {
            this.$notify({
              title: '失败',
              message: '分配失败！',
              type: 'fail',
              duration: 2000
            })
          }
        })
    },
    // 修改用户账号状态
    updateUserStatus(row, index, status) {
      this.sysUser = Object.assign({}, row)
      this.sysUser.isDeleted = status
      this.sysUser.modifiedPerson = store.getters.userObj.userId
      updateUserStatus(this.sysUser).then((response) => {
        const index = this.list.findIndex(v => v.userId === this.sysUser.userId)
        this.list.splice(index, 1, this.sysUser)
        this.dialogFormVisible = false
        if (response.status === 1) {
          this.$notify({
            title: '成功',
            message: status === 2 ? '冻结成功！' : '解冻成功',
            type: 'success',
            duration: 2000
          })
        } else {
          this.$notify({
            title: '失败',
            message: '修改失败！',
            type: 'fail',
            duration: 2000
          })
        }
      })
    },
    // Json数据格式化
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    // 点击ID拦执行列表排序
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    },
    goCompany() {
      this.$router.push({ name: 'company', params: {}})
    }
  }
}
</script>
