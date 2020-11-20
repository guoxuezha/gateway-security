<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.name"
        placeholder="菜单名"
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

      <el-table-column label="菜单名" align="center">
        <template slot-scope="{row}">
          <span>{{ row.menuName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="父节点" align="center">
        <template slot-scope="{row}">
          <span>{{ row.parentName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="菜单Code" align="center">
        <template slot-scope="{row}">
          <span>{{ row.menuCodeName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="路径" align="center">
        <template slot-scope="{row}">
          <span>{{ row.menuUrl }}</span>
        </template>
      </el-table-column>

      <el-table-column label="菜单描述" align="center">
        <template slot-scope="{row}">
          <span class="link-type">{{ row.menuDescription }}</span>
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
        :model="sysMenu"
        label-position="left"
        label-width="70px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="菜单名" prop="menuName" label-width="100px">
          <el-input v-model="sysMenu.menuName" />
        </el-form-item>

        <el-form-item label="菜单Code" prop="menuCodeName" label-width="100px">
          <el-input v-model="sysMenu.menuCodeName" />
        </el-form-item>

        <el-form-item label="访问路径" prop="menuUrl" label-width="100px">
          <el-input v-model="sysMenu.menuUrl" />
        </el-form-item>

        <el-form-item label="父节点" prop="menuPid" label-width="100px">
          <el-select v-model="sysMenu.menuPid" filterable placeholder="请选择">
            <el-option
              v-for="item in parentMenuOptions"
              :key="item.menuId"
              :label="item.menuName"
              :value="item.menuId"
              filterable
            />
          </el-select>
        </el-form-item>

        <el-form-item label="菜单描述" label-width="100px">
          <el-input
            v-model="sysMenu.menuDescription"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="请输入对该菜单项的描述"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">提交</el-button>
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
import { selectAllMenu, selectByPage, createMenu, updateMenu, deleteMenu } from '@/api/auth/frontendmenu'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { parseTime } from '@/utils'
import store from '@/store'

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    // 将table中的pid转为pname
    trunParentMenuIdToName(pid) {
      for (let i = 0; i < this.list.length; i++) {
        const menuId = this.list[i].menuId
        if (pid === menuId) {
          return this.list[i].menuName
        }
      }
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      parentMenuOptions: [],
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        name: ''
      },
      sysMenu: {
        manuName: '',
        menuUrl: '',
        menuPid: 0,
        menuCodeName: '',
        menuSort: 0,
        menuDescription: '',
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
        update: '编辑菜单',
        create: '新增菜单'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        manuName: [{ required: true, message: '菜单名为必填字段', trigger: 'blur' }],
        manuCodeName: [{ required: true, message: '菜单代号为必填字段', trigger: 'blur' }],
        menuUrl: [{ required: true, message: '菜单路径为必填字段', trigger: 'blur' }],
        menuPid: [{ required: true, message: '父节点为必填字段', trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
    this.getParentMenus()
  },
  methods: {
    /**
     * 菜单全查,为了添加父节点
     */
    getParentMenus() {
      this.listLoading = true
      selectAllMenu(this.listQuery).then(response => {
        this.parentMenuOptions = response.data
        this.parentMenuOptions.unshift({ menuId: 0, menuName: '无' })
      })
    },
    /**
     * 用户列表初始化
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
      this.getParentMenus()
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
    // 初始化弹出表单的数据
    resetSysMenu() {
      this.sysMenu = {
        manuName: '',
        menuUrl: '',
        menuPid: 0,
        menuSort: 0,
        menuDescription: '',
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
      this.resetSysMenu()
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
          this.sysMenu.createPerson = store.getters.userObj.userId
          createMenu(this.sysMenu).then((response) => {
            this.list.unshift(response.data)
            if (response.status === 1) {
              this.total++
              this.parentMenuOptions.push({ menuId: response.data.menuId, menuName: response.data.menuName })
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
      this.sysMenu = Object.assign({}, row) // copy obj
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
          const tempData = Object.assign({}, this.sysMenu)
          tempData.modifiedPerson = store.getters.userObj.userId
          updateMenu(tempData).then((response) => {
            const index = this.list.findIndex(v => v.menuId === this.sysMenu.menuId)
            console.log(response.data)
            this.list.splice(index, 1, response.data)
            // parentMenuOptions也需要跟着修改
            const parentIndex = this.list.findIndex(v => v.menuId === this.parentMenuOptions.menuId)
            this.parentMenuOptions.splice(parentIndex, 1, { menuId: response.data.menuId, menuName: response.data.menuName })
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
    handleDelete(row, index) {
      this.sysMenu = Object.assign({}, row)
      this.sysMenu.isDeleted = 1
      this.sysMenu.deletePreson = store.getters.userObj.userId
      deleteMenu(this.sysMenu).then((response) => {
        const index = this.list.findIndex(v => v.menuId === this.sysMenu.menuId)
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
