import { useEffect, useState } from 'react'
import { addExpense } from '../api'
import { formatMoney, parseMoney } from '../utils/money'

function ExpenseForm({ groupId, members, onAdded }) {
  const [description, setDescription] = useState('')
  const [amount, setAmount] = useState('')
  const [paidById, setPaidById] = useState(members[0].id)
  const [participantIds, setParticipantIds] = useState(members.map((m) => m.id))
  const [error, setError] = useState('')

  // new person joined -> include them by default
  useEffect(() => {
    setParticipantIds(members.map((m) => m.id))
  }, [members.length])

  function toggleParticipant(id) {
    if (participantIds.includes(id)) {
      setParticipantIds(participantIds.filter((p) => p !== id))
    } else {
      setParticipantIds([...participantIds, id])
    }
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')

    const amountCents = parseMoney(amount)
    if (!amountCents || amountCents <= 0) {
      setError('Enter a valid amount')
      return
    }

    try {
      await addExpense(groupId, {
        description,
        amountCents,
        paidById: Number(paidById),
        participantIds,
      })
      setDescription('')
      setAmount('')
      onAdded()
    } catch (err) {
      setError(err.message)
    }
  }

  const preview = parseMoney(amount)
  const perPerson = participantIds.length > 0 && preview > 0 ? preview / participantIds.length : 0

  return (
    <form className="card expense-form" onSubmit={handleSubmit}>
      <h3>New expense</h3>

      <div className="row">
        <input
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="What was it? (pizza, taxi...)"
        />
        <input
          className="amount-input"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          placeholder="0.00"
          inputMode="decimal"
        />
      </div>

      <label className="label">
        Paid by
        <select value={paidById} onChange={(e) => setPaidById(e.target.value)}>
          {members.map((m) => (
            <option key={m.id} value={m.id}>{m.name}</option>
          ))}
        </select>
      </label>

      <p className="label">Split between</p>
      <div className="chips">
        {members.map((m) => (
          <button
            type="button"
            key={m.id}
            className={participantIds.includes(m.id) ? 'chip on' : 'chip'}
            onClick={() => toggleParticipant(m.id)}
          >
            <img src={`/avatars/${m.avatar}.png`} alt="" />
            {m.name}
          </button>
        ))}
      </div>

      <div className="form-bottom">
        <span className="muted">
          {perPerson > 0 ? `≈ ${formatMoney(Math.round(perPerson))} each` : ''}
        </span>
        <button className="btn" type="submit">Add expense</button>
      </div>
      {error && <p className="error">{error}</p>}
    </form>
  )
}

export default ExpenseForm
