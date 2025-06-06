import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Board from '../views/board/Board.vue';
import PostDetail from '../views/board/PostDetail.vue';
import PostForm from '../views/board/PostForm.vue';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/board',
    name: 'Board',
    component: Board
  },
  {
    path: '/post/:id',
    name: 'PostDetail',
    component: PostDetail,
    props: route => ({ id: Number(route.params.id) })
  },
  {
    path: '/post/write',
    name: 'WritePost',
    component: PostForm
  },
  {
    path: '/post/edit/:id',
    name: 'EditPost',
    component: PostForm,
    props: route => ({ postId: Number(route.params.id) })
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
