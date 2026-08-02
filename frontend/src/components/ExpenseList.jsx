import { deleteExpense } from '../api'
import { formatMoney } from '../utils/money'
import Avatar from './Avatar'

function ExpenseList({ groupId, expenses, members, onDeleted }) {
  if (expenses.length === 0) {
    return <p className="hint">No expenses yet</p>
  }

  function avatarOf(memberId) {
    const m = members.find((x) => x.id === memberId)
    return m ? m.avatar : 1
  }

  async function handleDelete(expense) {
    if (!window.confirm(`Delete "${expense.description}"?`)) return
    try {
      await deleteExpense(groupId, expense.id)
      onDeleted()
    } catch (err) {
      alert(err.message)
    }
  }

  return (
    <ul className="expense-list">
      {expenses.map((e) => (
        <li key={e.id} className="card expense">
          <Avatar number={avatarOf(e.paidById)} name={e.paidByName} />
          <div className="expense-text">
            <strong>{e.description}</strong>
            <span className="muted">
              {e.paidByName} paid · split {e.participantIds.length} ways
            </span>
          </div>
          <span className="expense-amount">{formatMoney(e.amountCents)}</span>
          <button className="delete" onClick={() => handleDelete(e)} title="Delete">×</button>
        </li>
      ))}
    </ul>
  )
}

export default ExpenseList
