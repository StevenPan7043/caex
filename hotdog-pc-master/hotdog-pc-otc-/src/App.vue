<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { logoutApi } from '@/api/GetCode';

export default {
  beforeDestroy() {
    logoutApi();
  },
};
</script>
