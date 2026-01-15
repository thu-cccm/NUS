<script setup>

import TransparentBlogCard from "@/examples/cards/blogCards/TransparentBlogCard.vue";
import BackgroundBlogCard from "@/examples/cards/blogCards/BackgroundBlogCard.vue";

import post4 from "@/assets/img/examples/blog2.jpg";
</script>
<template>
  <section>
    <div class="container">
      <div class="row">
        <div class="col-lg-6">
          <h3 class="mb-5">我的开源项目</h3>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-3 col-sm-6">
          <TransparentBlogCard
            image="https:
            :action="{label:'点击前往', color: 'success', route: 'https:
            action.route=""
            title="Java-Notes"
            description="基于Java语言，实现常见的数据结构，算法，常用的开发技术，框架技术文档，文档会持续更新，是笑小枫的实时笔记。"
          />
        </div>
        <div class="col-lg-3 col-sm-6">
          <TransparentBlogCard
            image="https:
            :action="{label:'点击前往', color: 'success', route: 'https:
            title="SpringBoot框架从零搭建"
            description="本系列主要讲解了JAVA后端开发中常用的操作，以初创一个SpringBoot项目开始，以实例的形式讲解了一个单项目框架的诞生。"
          />
        </div>
        <div class="col-lg-3 col-sm-6">
          <TransparentBlogCard
            image="https:
            :action="{label:'点击前往', color: 'success', route: 'https:
            title="SpringBoot集成中间件合集"
            description="本项目主要讲解使用SpringBoot集成各种中间件，以及SpringBoot常遇到的定制任务，以实例的形式，让你更快的集成你需要的中间件。"
          />
        </div>
        <div class="col-lg-3 col-sm-6">
          <TransparentBlogCard
            :image="post4"
            :action="{label:'点击前往', color: 'success', route: 'https:
            title="更多项目"
            description="想要查看笑小枫更多的开源项目，可以前往笑小枫的GitHub进行查看。热爱开源，努力为开源添砖加瓦，欢迎加入笑小枫。"
          />
        </div>
      </div>
    </div>
  </section>
</template>
