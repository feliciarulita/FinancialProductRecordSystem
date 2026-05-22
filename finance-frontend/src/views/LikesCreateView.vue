<template>
  <main>
    <h2>Add Preference</h2>

    <form @submit.prevent="submit" v-if="selectedUser">
      <div class="form-group display-value">
        <label>User:</label>
        <p>{{ selectedUser.userName }} ({{ selectedUser.userId }}) - {{ selectedUser.account }}</p>
      </div>

      <div class="form-group">
        <label>Product:</label>
        <select v-model.number="form.productId" required>
          <option value="" disabled>-- Select a product --</option>
          <option v-for="product in products" :key="product.id" :value="product.id">
            {{ product.productName }} (ID: {{ product.id }}) - ${{ product.price }}
          </option>
        </select>
      </div>
      <div class="form-group">
        <label>Purchase Quantity:</label>
        <input v-model.number="form.purchaseQuantity" type="number" required />
      </div>

      <div class="form-group">
        <label>Account:</label>
        <div class="radio-group">
          <div>
            <input 
              type="radio" 
              id="default-account" 
              v-model="accountType" 
              value="default"
            />
            <label for="default-account">Use Default (User's Account)</label>
          </div>
          <div>
            <input 
              type="radio" 
              id="custom-account" 
              v-model="accountType" 
              value="custom"
            />
            <label for="custom-account">Custom Account</label>
          </div>
        </div>
        <input 
          v-if="accountType === 'custom'" 
          v-model="form.account" 
          required 
          placeholder="e.g., A100, B200"
          pattern="^[a-zA-Z0-9_\-]{1,50}$"
          title="Account: alphanumeric, hyphen, underscore only (max 50 chars)"
          class="custom-input"
        />
      </div>
      <button type="submit">Create</button>
      <router-link to="/likes" class="cancel-link">Cancel</router-link>
    </form>

    <p v-else class="error">Please select a user from the list view first.</p>

    <p v-if="message" :class="messageType">{{ message }}</p>
  </main>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { createLike, getAllProducts, getAllUsers } from '../api/likeService'

const router = useRouter()
const message = ref('')
const messageType = ref('')
const products = ref([])
const allUsers = ref([])
const accountType = ref('default')

const STORAGE_KEY = 'selectedUserId'

const selectedUser = computed(() => {
  const stored = localStorage.getItem(STORAGE_KEY)
  return allUsers.value.find(u => u.userId === stored) || null
})

function sanitizeError(error) {
  if (!error) return ''
  const str = String(error)
  return str
    .substring(0, 200)
    .replace(/<[^>]*>/g, '')
    .replace(/[<>\"']/g, '')
}

const form = ref({
  productId: null,
  purchaseQuantity: 1,
  account: '',
  totalFee: 0.0,
  totalAmount: 0.0,
})

async function loadProducts() {
  try {
    products.value = await getAllProducts()
  } catch (err) {
    console.error(err)
    message.value = 'Failed to load products: ' + sanitizeError(err.message)
    messageType.value = 'error'
  }
}

async function loadUsers() {
  try {
    allUsers.value = await getAllUsers()
    if (!selectedUser.value) {
      message.value = 'No user selected. Please go back to the list view and select a user.'
      messageType.value = 'error'
    }
  } catch (err) {
    console.error(err)
    message.value = 'Failed to load users: ' + sanitizeError(err.message)
    messageType.value = 'error'
  }
}

async function submit() {
  try {
    message.value = ''
    
    if (!selectedUser.value) {
      message.value = 'No user selected. Please go back to the list view and select a user.'
      messageType.value = 'error'
      return
    }
    
    const payload = {
      userId: selectedUser.value.userId,
      productId: form.value.productId,
      purchaseQuantity: form.value.purchaseQuantity,
      totalFee: form.value.totalFee,
      totalAmount: form.value.totalAmount,
    }
    
    if (accountType.value === 'custom') {
      payload.account = form.value.account
    }

    const saved = await createLike(payload)
    message.value = 'Successfully created like #' + (saved && saved.sn ? saved.sn : 'N/A')
    messageType.value = 'success'
    
    setTimeout(() => {
      router.push('/likes')
    }, 1500)
  } catch (err) {
    console.error(err)
    message.value = 'Create failed: ' + sanitizeError(err.message)
    messageType.value = 'error'
  }
}

onMounted(() => {
  loadProducts()
  loadUsers()
})
</script>

<style scoped>
main {
  padding: 1rem;
  max-width: 600px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.25rem;
  font-weight: 500;
}

.display-value {
  background-color: #f9f9f9;
  padding: 0.75rem;
  border-radius: 4px;
  border: 1px solid #eee;
}

.display-value p {
  margin: 0;
  color: #333;
}

.hint {
  display: block;
  margin-top: 0.5rem;
  font-size: 0.85rem;
  color: #666;
  background-color: #f5f5f5;
  padding: 0.5rem;
  border-left: 3px solid #0066cc;
}

input {
  padding: 0.5rem;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 4px;
}

select {
  padding: 0.5rem;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
  background-color: white;
}

button {
  padding: 0.7rem 1.5rem;
  background-color: #0066cc;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  margin-right: 1rem;
  transition: background-color 0.2s;
}

button:hover {
  background-color: #0052a3;
}

.cancel-link {
  display: inline-block;
  padding: 0.7rem 1.5rem;
  background-color: #ccc;
  color: #333;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.cancel-link:hover {
  background-color: #aaa;
}

.radio-group {
  margin-bottom: 0.5rem;
}

.radio-group div {
  margin-bottom: 0.5rem;
}

.radio-group input[type="radio"] {
  width: auto;
  margin-right: 0.5rem;
  cursor: pointer;
}

.radio-group label {
  display: inline;
  margin-bottom: 0;
  font-weight: normal;
  cursor: pointer;
}

.custom-input {
  margin-top: 0.5rem;
}

.success {
  color: #28a745;
  padding: 1rem;
  background-color: #d4edda;
  border: 1px solid #c3e6cb;
  border-radius: 4px;
  margin-top: 1rem;
}

.error {
  color: #dc3545;
  padding: 1rem;
  background-color: #f8d7da;
  border: 1px solid #f5c6cb;
  border-radius: 4px;
  margin-top: 1rem;
}
</style>
