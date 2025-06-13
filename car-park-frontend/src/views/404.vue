<template>
  <div>
    <div style="height: 100vh; overflow: hidden; display: flex; justify-content: center; align-items: center;">
      <div style="width: 30%">
        <img style="width: 100%" src="@/assets/images/404.jpg" alt="">
        <div style="font-size: 30px; text-align: center">找不到页面啦！ <router-link to="/">请返回到登录页</router-link></div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "404",
  data() {
    return {}
  },
  created() {

  },
  methods: {}
}
</script>

<style scoped>

</style>