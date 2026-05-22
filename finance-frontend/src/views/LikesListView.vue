<template>
  <main>
    <h2>Preferred Financial Product List</h2>

    <section>
      <div class="user-selector-group">
        <label>Select User:</label>
        <select v-model="selectedUserId" @change="onUserChange" required>
          <option value="" disabled>-- Select a user --</option>
          <option v-for="user in users" :key="user.userId" :value="user.userId">
            {{ user.userName }} ({{ user.userId }}) - {{ user.account }}
          </option>
        </select>
      </div>

      <button @click="goToCreate" class="create-btn">Add Preference</button>
    </section>

    <section>
      <h3>Likes ({{ likes.length }})</h3>
      <table>
        <thead>
          <tr>
            <th>Product Name</th>
            <th>Account</th>
            <th>Total amount</th>
            <th>Total fee</th>
            <th>Email</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody v-if="likes.length">
          <tr v-for="like in likes" :key="like.sn">
            <td>{{ like.product.productName }}</td>
            <td>{{ like.account }}</td>
            <td>{{ like.totalAmount.toFixed(2) }}</td>
            <td>{{ like.totalFee.toFixed(2) }}</td>
            <td>{{ like.user.email }}</td>
            <td>
              <div class="flex gap-2">
                <button class="update-btn" @click="goToEdit(like.sn)" title="Edit">
                  <IconUpdate />
                </button>
                <button class="delete-btn" @click="deleteLikeItem(like.sn)" title="Delete">
                  <IconTrash />
                </button>
              </div>
            </td>
          </tr>
        </tbody>
        <p v-else>No likes found.</p>
      </table>
    </section>
  </main>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getLikesByUser, deleteLike, getAllUsers } from '../api/likeService'
import IconTrash from '../components/icons/IconTrash.vue'
import IconUpdate from '../components/icons/IconUpdate.vue'

const router = useRouter()
const likes = ref([])
const users = ref([])
const selectedUserId = ref('')

const STORAGE_KEY = 'selectedUserId'

function sanitizeError(error) {
  if (!error) return ''
  const str = String(error)
  return str
    .substring(0, 200)
    .replace(/<[^>]*>/g, '')
    .replace(/[<>\"']/g, '')
}

const selectedUserName = computed(() => {
  const user = users.value.find(u => u.userId === selectedUserId.value)
  return user ? `${user.userName} (${user.userId})` : 'No user selected'
})

async function loadUsers() {
  try {
    users.value = await getAllUsers()
    const stored = localStorage.getItem(STORAGE_KEY)
    if (stored && users.value.find(u => u.userId === stored)) {
      selectedUserId.value = stored
      await loadUserLikes(stored)
    }
  } catch (err) {
    console.error(err)
    alert('Failed to load users: ' + sanitizeError(err.message))
  }
}

async function onUserChange() {
  localStorage.setItem(STORAGE_KEY, selectedUserId.value)
  await loadUserLikes(selectedUserId.value)
}

async function loadUserLikes(userId) {
  try {
    if (!userId) {
      likes.value = []
      return
    }
    likes.value = await getLikesByUser(userId)
  } catch (err) {
    console.error(err)
    alert('Failed to load likes: ' + sanitizeError(err.message))
    likes.value = []
  }
}

async function deleteLikeItem(sn) {
  if (confirm('Are you sure you want to delete this like?')) {
    try {
      await deleteLike(sn)
      if (selectedUserId.value) {
        await loadUserLikes(selectedUserId.value)
      }
    } catch (err) {
      console.error(err)
      alert('Failed to delete like: ' + sanitizeError(err.message))
    }
  }
}

function goToCreate() {
  router.push('/likes/new')
}

function goToEdit(sn) {
  router.push(`/likes/${sn}/edit`)
}

onMounted(() => loadUsers())
</script>

<style scoped>
main {
  padding: 1rem;
  width: 100%;
  max-width: 1000px;
  margin: 0 auto;
  box-sizing: border-box;
}

section {
  margin: 1rem 0;
  width: 740px;
}

.user-selector-group {
  margin-bottom: 1.5rem;
  padding: 1rem;
  background-color: #f9f9f9;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
}

.user-selector-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #333;
}

.user-selector-group select {
  width: 100%;
  padding: 0.6rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: white;
  cursor: pointer;
}

.user-selector-group select:focus {
  outline: none;
  border-color: #0066cc;
  box-shadow: 0 0 0 2px rgba(0, 102, 204, 0.1);
}

.filter-group {
  margin-bottom: 1rem;
  padding: 1rem;
  background-color: #e8f4f8;
  border-left: 4px solid #0066cc;
  border-radius: 4px;
}

.user-info {
  margin: 0 0 1rem 0;
  font-size: 0.95rem;
  color: #333;
}

label {
  display: block;
  margin-bottom: 0.25rem;
}

input {
  padding: 0.4rem;
  margin-right: 0.5rem;
}

button {
  padding: 0.5rem 0.8rem;
  margin-right: 0.5rem;
  background-color: #0066cc;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.2s;
}

button:hover {
  background-color: #ebeced;
}

button.create-btn {
  background-color: #28a745;
}

button.create-btn:hover {
  background-color: #218838;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

thead {
  background-color: #f5f5f5;
}

th, td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  font-weight: bold;
}

a {
  color: #0066cc;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

.delete-btn {
  background: none;
  border: none;
  padding: 0.25rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #dc3545;
  transition: color 0.2s;
}

.delete-btn:hover {
  color: #c82333;
}

.update-btn {
  background: none;
  border: none;
  padding: 0.25rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #0066cc;
  transition: color 0.2s;
}

.update-btn:hover {
  color: #0052a3;
}

.flex {
  display: flex;
}

.gap-2 {
  gap: 0.5rem;
}
</style>
