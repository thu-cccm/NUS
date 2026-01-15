<script setup>

import { RouterView } from "vue-router";
</script>

<template>
  <el-backtop :right="100" :bottom="100" >顶</el-backtop>
  <metainfo></metainfo>
  <router-view />
</template>
