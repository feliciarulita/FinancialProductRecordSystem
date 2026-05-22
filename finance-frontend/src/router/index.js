import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/likes',
    },
    {
      path: '/likes',
      name: 'likes',
      component: () => import('../views/LikesListView.vue'),
    },
    {
      path: '/likes/new',
      name: 'likes-create',
      component: () => import('../views/LikesCreateView.vue'),
    },
    {
      path: '/likes/:id/edit',
      name: 'likes-edit',
      component: () => import('../views/LikesEditView.vue'),
    },
  ],
})

export default router
