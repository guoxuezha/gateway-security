<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select
        v-model="listQuery.type"
        style="width: 140px"
        class="filter-item"
      >
        <el-option
          v-for="item in bigTypeOptions"
          :key="item.key"
          :label="item.label"
          :value="item.key"
        />
      </el-select>

      <el-input
        v-model="listQuery.name"
        placeholder="类型名称"
        style="width: 200px; margin-left: 5px"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />

      <el-select
        v-model="listQuery.sort"
        style="width: 140px; margin-left: 5px"
        class="filter-item"
        @change="handleFilter"
      >
        <el-option
          v-for="item in sortOptions"
          :key="item.key"
          :label="item.label"
          :value="item.key"
        />
      </el-select>

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

      <el-table-column label="软件/硬件" align="center" width="95">
        <template slot-scope="{ row }">
          <span v-if="row.big_type == 0" class="link-type">软件</span>
          <span v-else>硬件</span>
        </template>
      </el-table-column>

      <el-table-column label="类型名称" width="140px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.assets_type_name }}</span>
        </template>
      </el-table-column>

      <el-table-column label="类型描述" min-width="150px">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.assets_type_description }}</span>
        </template>
      </el-table-column>

      <el-table-column label="报警阈值">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.warning_threshold }}</span>
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
            v-if="row.is_deleted == 0"
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
        :model="assetsType"
        label-position="left"
        label-width="70px"
        style="width: 400px; margin-left: 50px"
      >
        <!-- <el-input v-model="assetsType.assets_type_id" type="hidden" /> -->

        <el-form-item label="软件/硬件" prop="big_type" label-width="100px">
          <el-select
            v-model="assetsType.big_type"
            class="filter-item"
            placeholder="请选择"
          >
            <el-option
              v-for="item in bigTypeOptions"
              :key="item.key"
              :label="item.label"
              :value="item.key"
            />
          </el-select>
        </el-form-item>

        <el-form-item
          label="类型名称"
          prop="assets_type_name"
          label-width="100px"
        >
          <el-input v-model="assetsType.assets_type_name" />
        </el-form-item>

        <el-form-item label="类型描述" label-width="100px">
          <el-input
            v-model="assetsType.assets_type_description"
            :autosize="{ minRows: 2, maxRows: 4 }"
            type="textarea"
            placeholder="请输入对该类型的描述"
          />
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

    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table
        :data="pvData"
        border
        fit
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          @click="dialogPvVisible = false"
        >Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
// import { fetchList, fetchPv, createArticle, updateArticle } from '@/api/article'
import { selectByPage, createAssetsType, updateAssetsType, deleteAssetsType } from '@/api/assets/type'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

const calendarTypeOptions = [
  { key: 'CN', display_name: 'China' },
  { key: 'US', display_name: 'USA' },
  { key: 'JP', display_name: 'Japan' },
  { key: 'EU', display_name: 'Eurozone' }
]

// arr to obj, such as { CN : "China", US : "USA" }
const calendarTypeKeyValue = calendarTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

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
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        name: '',
        type: 1,
        isDeleted: 0,
        sort: '+id'
      },
      bigTypeOptions: [{ label: '软件', key: 0 }, { label: '硬件', key: 1 }],
      calendarTypeOptions,
      sortOptions: [{ label: '序号升序', key: '+id' }, { label: '序号降序', key: '-id' }],
      statusOptions: ['发布', '下架', '删除'],
      showReviewer: false,
      assetsType: {
        assets_type_id: 0,
        big_type: 1,
        assets_type_name: '',
        assets_type_description: '',
        is_deleted: 0
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑资产类型',
        create: '新建资产类型'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        big_type: [{ required: true, message: '分类类别为必填字段', trigger: 'change' }],
        // timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        assets_type_name: [{ required: true, message: '分类名称为必填字段', trigger: 'blur' }]
      },
      downloadLoading: false
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
        // Just to simulate the time of the request
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
    // 初始化弹出表单的数据,暂时没用
    resetAssetsType() {
      this.assetsType = {
        assets_type_id: 0,
        big_type: 1,
        assets_type_name: '',
        assets_type_description: '',
        is_deleted: 0
      }
    },
    // 弹出新增窗口时设置参数
    handleCreate() {
      this.resetAssetsType()
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
          const tempData = Object.assign({}, this.assetsType)

          createAssetsType(tempData).then((response) => {
            this.assetsType.assets_type_id = response.data
            // console.log(this.assetsType)
            this.list.unshift(this.assetsType)
            this.dialogFormVisible = false
            if (response.status === 1) {
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
      this.assetsType = Object.assign({}, row) // copy obj
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
          const tempData = Object.assign({}, this.assetsType)
          updateAssetsType(tempData).then((response) => {
            const index = this.list.findIndex(v => v.assets_type_id === this.assetsType.assets_type_id)
            this.list.splice(index, 1, this.assetsType)
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
      this.assetsType = Object.assign({}, row)
      deleteAssetsType(this.assetsType.assets_type_id).then((response) => {
        const index = this.list.findIndex(v => v.assets_type_id === this.assetsType.assets_type_id)
        this.list.splice(index, 1, this.assetsType)
        this.dialogFormVisible = false
        if (response.status === 1) {
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
    handleFetchPv(pv) {
      /* fetchPv(pv).then(response => {
        this.pvData = response.data.pvData
        this.dialogPvVisible = true
      }) */
    },
    // 下载
    /* handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
        const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
        const data = this.formatJson(filterVal)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'table-list'
        })
        this.downloadLoading = false
      })
    }, */
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
