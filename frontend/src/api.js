const BASE_URL = '/api'

async function request(path, options = {}) {
  const res = await fetch(BASE_URL + path, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  })

  if (!res.ok) {
    let message = 'Something went wrong :('
    try {
      const data = await res.json()
      if (data.message) message = data.message
    } catch (e) {
      // body was not json, keep default message
    }
    throw new Error(message)
  }

  if (res.status === 204) return null
  return res.json()
}

export function getGroups() {
  return request('/groups')
}

export function getGroup(id) {
  return request(`/groups/${id}`)
}

export function createGroup(name) {
  return request('/groups', { method: 'POST', body: JSON.stringify({ name }) })
}

export function addMember(groupId, name, avatar) {
  return request(`/groups/${groupId}/members`, {
    method: 'POST',
    body: JSON.stringify({ name, avatar }),
  })
}

export function getExpenses(groupId) {
  return request(`/groups/${groupId}/expenses`)
}

export function addExpense(groupId, expense) {
  return request(`/groups/${groupId}/expenses`, {
    method: 'POST',
    body: JSON.stringify(expense),
  })
}

export function deleteExpense(groupId, expenseId) {
  return request(`/groups/${groupId}/expenses/${expenseId}`, { method: 'DELETE' })
}

export function getBalances(groupId) {
  return request(`/groups/${groupId}/balances`)
}

export function getSettleUp(groupId) {
  return request(`/groups/${groupId}/settle-up`)
}
