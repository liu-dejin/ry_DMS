<template>
  <div class="personal-introduction">
    <div class="personal-info">
      <h1>{{ nickname }}</h1>
      <h2>{{ profession }}</h2>
      <h3>技术栈：</h3>
      <ul>
        <li v-for="tech in techStack" :key="tech">{{ tech }}</li>
      </ul>
      <div>
        <h3>个人简介：</h3>
        <p>{{ introduction }}</p>
      </div>
    </div>
    <div class="project-introduction">
      <div class="project">
        <h3>项目介绍：</h3>
        <p>
          外卖管理系统是一个使用Vue2和SpringBoot搭建的项目，具有分类管理、菜单管理、用户管理等功能。系统界面友好，操作简便。
        </p>
      </div>
      <div class="tech-stack">
        <h3>技术选型：</h3>
        <div>
          <h4>后端技术：</h4>
          <ul>
            <li v-for="backendTech in backendTechStack" :key="backendTech">
              {{ backendTech }}
            </li>
          </ul>
        </div>
        <div>
          <h4>前端技术：</h4>
          <ul>
            <li v-for="frontendTech in frontendTechStack" :key="frontendTech">
              {{ frontendTech }}
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      nickname: "没睡醒",
      profession: "Java工程师",
      techStack: ["Java", "Spring Boot", "MyBatis", "Vue.js"],
      introduction:
        "作为一名热爱编程的Java工程师，我擅长运用Spring Boot等先进的技术栈进行高效的后端开发，善于设计优雅的架构和实现复杂的业务逻辑。同时，我对前端开发技术也有着深入的了解与实践经验，能够利用Vue.js等现代框架构建交互流畅、功能丰富的用户界面。我追求技术的卓越与创新，不断探索最佳实践与解决方案，期待与您分享经验、交流思想，共同进步，共同成长！",
      backendTechStack: [
        "Spring Boot",
        "Spring Security",
        "JWT",
        "MyBatis",
        "Druid",
        "Fastjson",
      ],
      frontendTechStack: [
        "Vue",
        "Vuex",
        "Element-ui",
        "Axios",
        "Sass",
        "Quill",
      ],
    };
  },
};
</script>

<style>
.personal-introduction {
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 20px;
}

.personal-info {
  flex: 1;
}

.project-introduction {
  flex: 1;
}

h1 {
  font-size: 24px;
  color: #333;
}

h2 {
  font-size: 20px;
  color: #666;
}

h3 {
  font-size: 18px;
  color: #999;
  margin-top: 10px;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 5px;
}

p {
  font-size: 16px;
  color: #555;
}

.project {
  margin-bottom: 20px;
}

.tech-stack {
  display: flex;
  justify-content: space-between;
}

.tech-stack h4 {
  font-size: 16px;
  color: #333;
}

.tech-stack ul {
  list-style-type: none;
  padding: 0;
}

.tech-stack li {
  margin-bottom: 5px;
}
</style>
