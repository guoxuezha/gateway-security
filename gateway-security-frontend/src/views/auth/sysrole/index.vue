<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.name"
        placeholder="角色名"
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

      <el-table-column label="角色名" align="center">
        <template slot-scope="{row}">
          <span>{{ row.roleName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="角色描述" align="center">
        <template slot-scope="{row}">
          <span class="link-type">{{ row.roleDescription }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="success" size="mini" @click="handleDistributeMenu(row)">分配菜单</el-button>
          <el-button type="primary" size="mini" @click="handleUpdate(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 底部分页组件 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getRoleList"
    />

    <!-- 弹窗编辑 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="sysRole"
        label-position="left"
        label-width="70px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="角色名" prop="roleName" label-width="100px">
          <el-input v-model="sysRole.roleName" />
        </el-form-item>

        <el-form-item label="角色描述" label-width="100px">
          <el-input
            v-model="sysRole.roleDescription"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="请输入对该角色的描述"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">提交</el-button>
      </div>
    </el-dialog>

    <!-- 弹窗分配菜单树 -->
    <el-dialog title="分配菜单" :visible.sync="dialogFormVisible2">
      <el-form
        ref="distributeMenu"
        label-position="left"
        label-width="70px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="分配菜单项" label-width="100px">
          <el-tree
            ref="tree"
            :data="menuTree"
            show-checkbox
            node-key="menuId"
            :default-expanded-keys="defaultExpandedKeys"
            :default-checked-keys="[]"
            :props="defaultProps"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible2 = false">取消</el-button>
        <el-button type="primary" @click="distributeMenu()">提交</el-button>
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
import { selectByPage, createRole, updateRole, deleteRole } from '@/api/auth/sysrole'
import { selectAllMenu, getMenuTreeJsonData, selectMenuByRoleId, distributeMenu } from '@/api/auth/frontendmenu'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import store from '@/store'

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
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
      roleOptions: [],
      statusOptions: ['发布', '下架', '删除'],
      sysRole: {
        roleName: '',
        roleDescription: '',
        create_time: undefined,
        create_person: undefined,
        modified_time: undefined,
        modified_person: undefined,
        deleted_time: undefined,
        deleted_person: undefined
      },
      dialogFormVisible: false,
      // 分配菜单项需要的几个变量:
      // 1.menu树形弹窗是否可见
      dialogFormVisible2: false,
      // 2.当前要分配菜单的角色
      distributeRoleId: 0,
      // 3.全部菜单结构
      menuTree: [],
      // 4.已被选中的菜单项,可用于实现回显
      checkedKeys: [],
      // 5.指定tree中属性与实体类中字段的匹配规则
      defaultProps: {
        children: 'children',
        label: 'menuName'
      },
      // 6.默认展开的节点
      defaultExpandedKeys: [0],
      dialogStatus: '',
      textMap: {
        update: '编辑角色',
        create: '新增角色'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        roleName: [{ required: true, message: '角色名为必填字段', trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getRoleList()
    this.getMenuTree()
  },
  methods: {
    /**
     * 角色列表初始化
     */
    getRoleList() {
      this.listLoading = true
      selectByPage(this.listQuery).then(response => {
        this.list = response.data
        this.total = response.total
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 0.3 * 1000)
      })
    },
    // 初始化菜单树
    getMenuTree() {
      selectAllMenu().then(response => {
        if (response.status === 1) {
          // 处理默认展开所有根节点
          response.data.forEach(menu => {
            if (menu.menuSort === 1) {
              this.defaultExpandedKeys.push(menu.menuId)
            }
          })
          // 将列表结构构建为树形结构
          this.menuTree.unshift(getMenuTreeJsonData(response.data))
        }
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
    resetSysRole() {
      this.sysRole = {
        roleName: '',
        roleDescription: '',
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
      this.resetSysRole()
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
          // 新增之前补充一部分信息
          this.sysRole.createPerson = store.getters.userObj.userId
          createRole(this.sysRole).then((response) => {
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
      this.sysRole = Object.assign({}, row) // copy obj
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
          const tempData = Object.assign({}, this.sysRole)
          tempData.modifiedPerson = store.getters.userObj.userId
          updateRole(tempData).then((response) => {
            const index = this.list.findIndex(v => v.roleId === this.sysRole.roleId)
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
    // 回显该角色拥有的菜单树
    handleDistributeMenu(row) {
      this.distributeRoleId = row.roleId
      this.checkedKeys = []
      selectMenuByRoleId({ roleId: this.distributeRoleId }).then(response => {
        if (response.status === 1) {
          const hadMenuTree = response.data
          hadMenuTree.forEach(menu => {
            this.checkedKeys.push(menu.menuId)
          })
        }
        console.log(this.checkedKeys)
        // 先清空树中已被选中的项
        this.$refs.tree.setCheckedKeys([])
        // 通过对Key的操作实现回显
        this.checkedKeys.forEach((i, n) => {
          const node = this.$refs.tree.getNode(i)
          if (node.isLeaf) {
            this.$refs.tree.setChecked(node, true)
          }
        })
      })
      this.dialogFormVisible2 = true
    },
    // 给角色分配菜单项
    distributeMenu() {
      // 处理选中的菜单项
      // 通过对Key获取被选中的项
      const menuIds = this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys())
      const menuIdsStr = menuIds.join(',')
      distributeMenu({ roleId: this.distributeRoleId, menuIds: menuIdsStr }).then(response => {
        if (response.status === 1) {
          this.dialogFormVisible2 = false
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
    handleDelete(row, index) {
      this.sysRole = Object.assign({}, row)
      this.sysRole.isDeleted = 1
      this.sysRole.deletedPerson = store.getters.userObj.userId
      deleteRole(this.sysRole).then((response) => {
        const index = this.list.findIndex(v => v.roleId === this.sysRole.roleId)
        if (response.status === 1) {
          this.total--
          this.list.splice(index, 1, this.sysRole)
          this.dialogFormVisible = false
          this.$notify({
            title: '成功',
            message: '删除成功！',
            type: 'success',
            duration: 2000
          })
          this.list.splice(index, 1)
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
    }
  }
}
</script>
