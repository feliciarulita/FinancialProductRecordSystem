const API_BASE = import.meta.env.VITE_API_BASE || '';

async function request(path, options = {}) {
  const res = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(`${res.status} ${res.statusText}: ${text}`);
  }
  if (res.status === 204) return null;
  return res.json();
}

export function getLikesByUser(userId) {
  return request(`/api/likes/user/${encodeURIComponent(userId)}`);
}

export function getLike(id) {
  return request(`/api/likes/${encodeURIComponent(id)}`);
}

export function createLike(payload) {
  return request('/api/likes', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export function updateLike(id, payload) {
  return request(`/api/likes/${encodeURIComponent(id)}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  });
}

export function deleteLike(id) {
  return request(`/api/likes/${encodeURIComponent(id)}`, { method: 'DELETE' });
}

export function getAllProducts() {
  return request('/api/products');
}

export function getAllUsers() {
  return request('/api/users');
}
