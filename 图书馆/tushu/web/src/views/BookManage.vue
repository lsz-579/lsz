<template>
  <div>
    <!-- 搜索区域 -->
    <el-card style="margin-bottom: 20px">
      <el-form :inline="true" :model="query" size="small">
        <el-form-item label="书名">
          <el-input v-model="query.title" placeholder="请输入书名" clearable />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="query.author" placeholder="请输入作者" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="query.category" placeholder="请选择分类" clearable>
            <el-option label="文学" value="文学" />
            <el-option label="科幻" value="科幻" />
            <el-option label="技术" value="技术" />
            <el-option label="历史" value="历史" />
            <el-option label="哲学" value="哲学" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card>
      <div style="margin-bottom: 15px">
        <el-button type="primary" size="small" @click="openDialog()">新增图书</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="书名" min-width="150" />
        <el-table-column prop="author" label="作者" width="120" />
        <el-table-column prop="isbn" label="ISBN" width="140" />
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column prop="publisher" label="出版社" min-width="150" />
        <el-table-column prop="publishDate" label="出版日期" width="110" />
        <el-table-column label="操作" width="140" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="openDialog(scope.row)">编辑</el-button>
            <el-button type="text" size="small" style="color: #F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 15px; text-align: right">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :total="total"
          :page-size="pageSize"
          :current-page.sync="currentPage"
          @current-change="fetchData"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @closed="resetForm">
      <el-form :model="form" :rules="rules" ref="bookForm" label-width="80px" size="small">
        <el-form-item label="书名" prop="title">
          <el-input v-model="form.title" placeholder="请输入书名" />
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="ISBN">
          <el-input v-model="form.isbn" placeholder="请输入ISBN" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="文学" value="文学" />
            <el-option label="科幻" value="科幻" />
            <el-option label="技术" value="技术" />
            <el-option label="历史" value="历史" />
            <el-option label="哲学" value="哲学" />
          </el-select>
        </el-form-item>
        <el-form-item label="出版社">
          <el-input v-model="form.publisher" placeholder="请输入出版社" />
        </el-form-item>
        <el-form-item label="出版日期">
          <el-date-picker v-model="form.publishDate" type="date" placeholder="选择日期" style="width: 100%" value-format="yyyy-MM-dd" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入简介" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="dialogVisible = false">取消</el-button>
        <el-button size="small" type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import bookApi from '../api/book';

export default {
  name: 'BookManage',
  data() {
    return {
      query: { title: '', author: '', category: '' },
      tableData: [],
      loading: false,
      total: 0,
      pageSize: 10,
      currentPage: 1,
      dialogVisible: false,
      dialogTitle: '新增图书',
      editingId: null,
      form: {
        title: '', author: '', isbn: '', category: '',
        publisher: '', publishDate: null, description: ''
      },
      rules: {
        title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
        author: [{ required: true, message: '请输入作者', trigger: 'blur' }]
      }
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.loading = true;
      const { title, author, category } = this.query;
      const request = (title || author || category)
        ? bookApi.search(this.query, this.currentPage - 1, this.pageSize)
        : bookApi.list(this.currentPage - 1, this.pageSize);

      request.then(res => {
        this.tableData = res.data.content;
        this.total = res.data.totalElements;
      }).finally(() => {
        this.loading = false;
      });
    },
    handleSearch() {
      this.currentPage = 1;
      this.fetchData();
    },
    handleReset() {
      this.query = { title: '', author: '', category: '' };
      this.currentPage = 1;
      this.fetchData();
    },
    openDialog(row) {
      if (row) {
        this.dialogTitle = '编辑图书';
        this.editingId = row.id;
        this.form = { ...row };
      } else {
        this.dialogTitle = '新增图书';
        this.editingId = null;
        this.resetForm();
      }
      this.dialogVisible = true;
    },
    resetForm() {
      this.form = {
        title: '', author: '', isbn: '', category: '',
        publisher: '', publishDate: null, description: ''
      };
      if (this.$refs.bookForm) {
        this.$refs.bookForm.clearValidate();
      }
    },
    handleSubmit() {
      this.$refs.bookForm.validate(valid => {
        if (!valid) return;

        const request = this.editingId
          ? bookApi.update(this.editingId, this.form)
          : bookApi.create(this.form);

        request.then(() => {
          this.$message.success(this.editingId ? '修改成功' : '新增成功');
          this.dialogVisible = false;
          this.fetchData();
        }).catch(err => {
          this.$message.error(err.response?.data?.message || '操作失败');
        });
      });
    },
    handleDelete(row) {
      this.$confirm(`确定要删除《${row.title}》吗？`, '提示', {
        type: 'warning'
      }).then(() => {
        bookApi.delete(row.id).then(() => {
          this.$message.success('删除成功');
          if (this.tableData.length === 1 && this.currentPage > 1) {
            this.currentPage--;
          }
          this.fetchData();
        });
      }).catch(() => {});
    }
  }
};
</script>
