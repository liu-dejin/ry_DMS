<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="菜品名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入菜品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="菜品分类" prop="categoryId">
        <!--        <el-input
          v-model="queryParams.categoryId"
          placeholder="请输入菜品分类"
          clearable
          @keyup.enter.native="handleQuery"
        />-->
        <el-select
          v-model="queryParams.categoryId"
          placeholder="请选择菜品分类"
          clearable
        >
          <el-option
            v-for="item in categoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
          clearable
        >
          <el-option
            v-for="dict in dict.type.dish_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['sky:dish:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['sky:dish:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['sky:dish:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['sky:dish:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="dishList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!--      <el-table-column label="主键" align="center" prop="id" />-->
      <el-table-column label="菜品名称" align="center" prop="name" />
      <el-table-column label="菜品分类" align="center" prop="categoryName" />
      <el-table-column label="菜品价格" align="center" prop="price" />
      <el-table-column label="图片" align="center" prop="image" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.image" :width="50" :height="50" />
        </template>
      </el-table-column>
      <el-table-column label="描述信息" align="center" prop="description" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.dish_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sky:dish:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sky:dish:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改菜品管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="菜品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入菜品名称" />
        </el-form-item>
        <el-form-item label="菜品分类" prop="categoryId">
          <!--          <el-input v-model="form.categoryId" placeholder="请输入菜品分类" />-->
          <el-select v-model="form.categoryId" placeholder="请选择菜品分类">
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="菜品价格" prop="price">
          <el-input v-model="form.price" placeholder="请输入菜品价格" />
        </el-form-item>
        <el-form-item label="图片" prop="image">
          <image-upload v-model="form.image" />
        </el-form-item>
        <el-form-item label="描述信息" prop="description">
          <el-input v-model="form.description" placeholder="请输入描述信息" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option
              v-for="dict in dict.type.dish_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-divider content-position="center">菜品口味关系信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAddDishFlavor"
              >添加</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              @click="handleDeleteDishFlavor"
              >删除</el-button
            >
          </el-col>
        </el-row>
        <el-table
          :data="dishFlavorList"
          :row-class-name="rowDishFlavorIndex"
          @selection-change="handleDishFlavorSelectionChange"
          ref="dishFlavor"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column
            label="序号"
            align="center"
            prop="index"
            width="50"
          />
          <el-table-column label="口味名称" prop="name" width="150">
            <template slot-scope="scope">
              <!--              <el-input v-model="scope.row.name" placeholder="请输入口味名称" />-->
              <el-select v-model="scope.row.name" placeholder="请选择口味名称">
                <el-option
                  v-for="item in dishFlavorsData"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name"
                >
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="口味数据list" prop="value" width="150">
            <template slot-scope="scope">
              <!--              <el-input v-model="scope.row.value" placeholder="请输入口味数据list" />-->
              <el-select
                v-model="scope.row.value"
                placeholder="请选择口味数据list"
              >
                <el-option
                  v-for="item in dishFlavorsData"
                  :key="item.value"
                  :label="item.value"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listDish,
  getDish,
  delDish,
  addDish,
  updateDish,
} from "@/api/sky/dish";
import {
  listCategory,
  getCategory,
  delCategory,
  addCategory,
  updateCategory,
} from "@/api/sky/category";

export default {
  name: "Dish",
  dicts: ["dish_status"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedDishFlavor: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 菜品管理表格数据
      dishList: [],
      // 菜品口味关系表格数据
      dishFlavorList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        categoryId: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "菜品名称不能为空", trigger: "blur" },
        ],
        categoryId: [
          { required: true, message: "菜品分类不能为空", trigger: "blur" },
        ],
        price: [
          { required: true, message: "菜品价格不能为空", trigger: "blur" },
        ],
        image: [{ required: true, message: "图片不能为空", trigger: "blur" }],
        description: [
          { required: true, message: "描述信息不能为空", trigger: "blur" },
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" },
        ],
      },
      // 分类管理表格数据
      categoryList: [],
      // 口味列表
      dishFlavorsData: [
        { name: "甜味", value: "['无糖', '少糖', '半糖', '多糖', '全糖']" },
        { name: "温度", value: "['热饮', '常温', '去冰', '少冰', '多冰']" },
        { name: "忌口", value: "['不要葱', '不要蒜', '不要香菜', '不要辣']" },
        { name: "辣度", value: "['不辣', '微辣', '中辣', '重辣']" },
      ],
    };
  },
  created() {
    this.getList();
    this.getCategoryList();
  },
  methods: {
    /** 查询菜品管理列表 */
    getList() {
      this.loading = true;
      listDish(this.queryParams).then((response) => {
        this.dishList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询分类管理列表 */
    getCategoryList() {
      this.loading = true;
      listCategory({
        pageNum: 1,
        pageSize: 100,
        type: 1,
      }).then((response) => {
        this.categoryList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        categoryId: null,
        price: null,
        image: null,
        description: null,
        status: null,
        createTime: null,
        updateTime: null,
        createUser: null,
        updateUser: null,
      };
      this.dishFlavorList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加菜品管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getDish(id).then((response) => {
        this.form = response.data;
        this.dishFlavorList = response.data.dishFlavorList;
        this.open = true;
        this.title = "修改菜品管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.form.dishFlavorList = this.dishFlavorList;
          if (this.form.id != null) {
            updateDish(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDish(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除菜品管理编号为"' + ids + '"的数据项？')
        .then(function () {
          return delDish(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 菜品口味关系序号 */
    rowDishFlavorIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 菜品口味关系添加按钮操作 */
    handleAddDishFlavor() {
      let obj = {};
      obj.name = "";
      obj.value = "";
      this.dishFlavorList.push(obj);
    },
    /** 菜品口味关系删除按钮操作 */
    handleDeleteDishFlavor() {
      if (this.checkedDishFlavor.length == 0) {
        this.$modal.msgError("请先选择要删除的菜品口味关系数据");
      } else {
        const dishFlavorList = this.dishFlavorList;
        const checkedDishFlavor = this.checkedDishFlavor;
        this.dishFlavorList = dishFlavorList.filter(function (item) {
          return checkedDishFlavor.indexOf(item.index) == -1;
        });
      }
    },
    /** 复选框选中数据 */
    handleDishFlavorSelectionChange(selection) {
      this.checkedDishFlavor = selection.map((item) => item.index);
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "sky/dish/export",
        {
          ...this.queryParams,
        },
        `dish_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
