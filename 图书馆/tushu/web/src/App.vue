<template>
  <div id="app">
    <el-container style="height: 100vh">
      <el-header style="background: #409EFF; color: white; display: flex; align-items: center; padding: 0 20px">
        <h2 style="margin: 0">图书管理系统</h2>
        <el-menu
          :default-active="activeMenu"
          mode="horizontal"
          router
          style="margin-left: 40px; background: transparent; border-bottom: none"
          text-color="#fff"
          active-text-color="#ffd04b"
        >
          <el-menu-item index="/books">图书管理</el-menu-item>
          <el-menu-item index="/statistics">查询统计</el-menu-item>
        </el-menu>
      </el-header>
      <el-main style="background: #f5f7fa; padding: 20px">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    activeMenu() {
      return this.$route.path;
    }
  }
};
</script>
