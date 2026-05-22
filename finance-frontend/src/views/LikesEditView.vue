<template>
  <main>
    <h2>Edit Preference</h2>

    <form @submit.prevent="submit" v-if="like">
      <div class="form-group display-value">
        <label>User:</label>
        <p>{{ like.user.userName }} ({{ like.user.userId }}) - {{ like.user.account }}</p>
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

      <div class="form-group display-value">
        <label>Total Amount:</label>
        <p>${{ like.totalAmount ? like.totalAmount.toFixed(2) : '0.00' }}</p>
      </div>

      <div class="form-group display-value">
        <label>Total Fee:</label>
        <p>${{ like.totalFee ? like.totalFee.toFixed(2) : '0.00' }}</p>
      </div>

      <button type="submit">Update</button>
      <button type="button" @click="cancel" class="cancel-btn">Cancel</button>
    </form>

    <p v-if="message" :class="messageType">{{ message }}</p>
  </main>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getLike, updateLike, getAllProducts } from '../api/likeService'

const router = useRouter()
const route = useRoute()
const message = ref('')
const messageType = ref('')
const accountType = ref('default')
const like = ref(null)
const products = ref([])

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
})

async function loadLike() {
  try {
    const sn = route.params.id
    like.value = await getLike(sn)
    form.value.productId = like.value.product.id
    form.value.purchaseQuantity = like.value.purchaseQuantity
    
    if (like.value.account === like.value.user.account) {
      accountType.value = 'default'
      form.value.account = like.value.user.account
    } else {
      accountType.value = 'custom'
      form.value.account = like.value.account
    }
  } catch (err) {
    console.error(err)
    message.value = 'Failed to load like: ' + sanitizeError(err.message)
    messageType.value = 'error'
  }
}

async function loadProducts() {
  try {
    products.value = await getAllProducts()
  } catch (err) {
    console.error(err)
    message.value = 'Failed to load products: ' + sanitizeError(err.message)
    messageType.value = 'error'
  }
}

async function submit() {
  try {
    message.value = ''
    const sn = route.params.id
    
    const payload = {
      productId: form.value.productId,
      purchaseQuantity: form.value.purchaseQuantity,
    }
    
    if (accountType.value === 'default') {
      payload.account = like.value.user.account
    } else {
      payload.account = form.value.account
    }

    await updateLike(sn, payload)
    message.value = 'Successfully updated like'
    messageType.value = 'success'
    
    setTimeout(() => {
      router.push('/likes')
    }, 1500)
  } catch (err) {
    console.error(err)
    message.value = 'Update failed: ' + sanitizeError(err.message)
    messageType.value = 'error'
  }
}

function cancel() {
  router.push('/likes')
}

onMounted(() => {
  loadLike()
  loadProducts()
})
</script>

<style scoped>
main {
  padding: 1rem;
  max-width: 600px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.25rem;
  font-weight: 500;
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

button.cancel-btn {
  background-color: #ccc;
  color: #333;
}

button.cancel-btn:hover {
  background-color: #aaa;
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
