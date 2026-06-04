import Vue from 'vue';
import Router from 'vue-router';
import BookManage from '../views/BookManage.vue';
import Statistics from '../views/Statistics.vue';

Vue.use(Router);

export default new Router({
  routes: [
    { path: '/', redirect: '/books' },
    { path: '/books', name: 'BookManage', component: BookManage },
    { path: '/statistics', name: 'Statistics', component: Statistics }
  ]
});
