<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 10px">
      <el-breadcrumb-item :to="{ path: '/back' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>{{$store.state.breadcrumb.fName}}</el-breadcrumb-item>
      <el-breadcrumb-item>{{$store.state.breadcrumb.cName}}</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图 -->
    <el-card>
      <div slot="header" class="clearfix">
        <el-button size="mini" type="primary" @click="dialogAddRoleVisible = true">添加角色</el-button>
      </div>

      <!-- 角色列表区域 -->
      <el-table :data="rolelist" border stripe>
        <!-- 展开列 -->
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-row :class="['bdbottom', i1 === 0 ? 'bdtop' : '', 'vcenter']" v-for="(item1, i1) in scope.row.children"
                    :key="item1.id">
              <!-- 渲染一级权限 -->
              <el-col :span="8">
                <el-tag closable @close="removeRightById(scope.row, item1.id)">{{item1.authName}}</el-tag>
                <i class="el-icon-caret-right"></i>
              </el-col>
              <!-- 渲染二级和三级权限 -->
              <el-col :span="16">
                <!-- 通过 for 循环 嵌套渲染二级权限 -->
                <el-row :class="[i2 === 0 ? '' : 'bdtop', 'vcenter']" v-for="(item2, i2) in item1.children"
                        :key="item2.id">
                  <el-col :span="6">
                    <el-tag type="success" closable @close="removeRightById(scope.row, item2.id)">{{item2.authName}}
                    </el-tag>
                    <i class="el-icon-caret-right"></i>
                  </el-col>
                  <!--<el-col :span="18">
                    <el-tag type="warning" v-for="(item3, i3) in item2.children" :key="item3.id" closable
                            @close="removeRightById(scope.row, item3.id)">{{item3.authName}}
                    </el-tag>
                  </el-col>-->
                </el-row>
              </el-col>
            </el-row>

            <!-- <pre>
              {{scope.row}}
            </pre> -->
          </template>
        </el-table-column>
        <!-- 索引列 -->
        <el-table-column type="index"></el-table-column>
        <el-table-column label="角色名称" prop="roleName"></el-table-column>
        <el-table-column label="角色描述" prop="roleDesc"></el-table-column>
        <el-table-column label="是否可用">
          <template slot-scope="scope">
            <el-switch
              @change="updateRole(scope.row)"
              v-model="scope.row.available"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300px">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="editRole(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteRole(scope.row.id)">删除</el-button>
            <el-button size="mini" type="warning" icon="el-icon-setting" @click="showSetRightDialog(scope.row)">分配权限
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分配权限的对话框 -->
    <el-dialog title="分配权限" :visible.sync="setRightDialogVisible" width="50%" @close="setRightDialogClosed">
      <!-- 树形控件 -->
      <el-tree :data="rightslist" :props="treeProps" show-checkbox node-key="id" default-expand-all
               :default-checked-keys="defKeys" ref="treeRef"></el-tree>
      <span slot="footer" class="dialog-footer">
        <el-button @click="setRightDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="allotRights">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="添加角色" :visible.sync="dialogAddRoleVisible" @close="$refs.roleFrm.resetFields()">
      <el-form ref="roleFrm" :model="roleFrm">
        <el-form-item label="角色名称" label-width="80px">
          <el-input v-model="roleFrm.roleName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色描述" label-width="80px">
          <el-input type="textarea" v-model="roleFrm.roleDesc"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogAddRoleVisible = false">取 消</el-button>
        <el-button type="primary" @click="addRole">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  export default {
    name: 'roleManager',
    data() {
      return {
        // 所有角色列表数据
        rolelist: [],
        // 控制分配权限对话框的显示与隐藏
        setRightDialogVisible: false,
        dialogAddRoleVisible: false,
        roleFrm: {
          roleName: '',
          roleDesc: ''
        },
        // 所有权限的数据
        rightslist: [],
        // 树形控件的属性绑定对象
        treeProps: {
          label: 'authName',
          children: 'children'
        },
        // 默认选中的节点Id值数组
        defKeys: [],
        // 当前即将分配权限的角色id
        roleId: ''
      }
    },
    created() {
      this.getRolesList()
    },
    methods: {
      async addRole() {
        if (this.roleFrm.id) {
          //更新
          return this.updateRole(this.roleFrm)
        }
        const res = await this.$http.post('role', this.roleFrm)
        if (res.code !== 200) {
          return this.$message.error('角色添加失败')
        }
        this.$message.success("角色添加成功")
        this.getRolesList()
        this.dialogAddRoleVisible = false
      },
      editRole(row) {
        this.dialogAddRoleVisible = true
        this.roleFrm = row
      },
      async deleteRole(id) {
        this.$confirm('此操作将永久删除该分类, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const res = await this.$http.delete('role/' + id)
          if (res.code !== 200) {
            return this.$message.error("删除失败")
          }
          this.$message.success("删除成功")
          this.getRolesList()
        })
      },
      async updateRole(row) {
        const res = await this.$http.put('role', row)
        this.roleFrm.id = null
        if (res.code !== 200) {
          return this.$message.error('角色更新失败')
        }
        this.$message.success("角色更新成功")
        this.getRolesList()
        this.dialogAddRoleVisible = false
      },
      // 获取所有角色的列表
      async getRolesList() {
        const res = await this.$http.get('role')

        if (res.code !== 200) {
          return this.$message.error('获取角色列表失败！')
        }

        this.rolelist = res.data

        console.log(this.rolelist)
      },
      // 根据Id删除对应的权限
      async removeRightById(role, rightId) {
        // 弹框提示用户是否要删除
        const confirmResult = await this.$confirm(
          '此操作将永久删除该文件, 是否继续?',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        ).catch(err => err)

        if (confirmResult !== 'confirm') {
          return this.$message.info('取消了删除！')
        }

        const res = await this.$http.delete(
          `role/${role.id}/rights/${rightId}`
        )

        if (res.code !== 200) {
          return this.$message.error('删除权限失败！')
        }

        // this.getRolesList()
        role.children = res.data
      },
      // 展示分配权限的对话框
      async showSetRightDialog(role) {
        this.roleId = role.id
        // 获取所有权限的数据
        const res = await this.$http.get('rights/tree')

        if (res.code !== 200) {
          return this.$message.error('获取权限数据失败！')
        }

        // 把获取到的权限数据保存到 data 中
        this.rightslist = res.data
        console.log(this.rightslist)

        // 递归获取三级节点的Id
        this.getLeafKeys(role, this.defKeys)

        this.setRightDialogVisible = true
      },
      // 通过递归的形式，获取角色下所有三级权限的id，并保存到 defKeys 数组中
      getLeafKeys(node, arr) {
        console.log(node)
        if (node.roleName!=null && node.children==null) return
        // 如果当前 node 节点不包含 children 属性，则是三级节点
        if (!node.children) {
          return arr.push(node.id)
        }

        node.children.forEach(item => this.getLeafKeys(item, arr))
      },
      // 监听分配权限对话框的关闭事件
      setRightDialogClosed() {
        this.defKeys = []
      },
      // 点击为角色分配权限
      async allotRights() {
        const keys = [
          ...this.$refs.treeRef.getCheckedKeys(),
          ...this.$refs.treeRef.getHalfCheckedKeys()
        ]

        const rids = keys.join(',')

        const res = await this.$http.post(
          `role/${this.roleId}/rights`,
          {rids: rids}
        )

        if (res.code !== 200) {
          return this.$message.error('分配权限失败！')
        }

        this.$message.success('分配权限成功！')
        this.getRolesList()
        this.setRightDialogVisible = false
      }
    }
  }
</script>

<style lang="less" scoped>
  .el-tag {
    margin: 7px;
  }

  .bdtop {
    border-top: 1px solid #eee;
  }

  .bdbottom {
    border-bottom: 1px solid #eee;
  }

  .vcenter {
    display: flex;
    align-items: center;
  }
</style>
