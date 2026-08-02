import { useEffect, useState } from 'react'
import { getBalances, getExpenses, getGroup, getSettleUp } from '../api'
import { formatMoney } from '../utils/money'
import MemberList from './MemberList'
import AddMemberForm from './AddMemberForm'
import ExpenseForm from './ExpenseForm'
import ExpenseList from './ExpenseList'
import Balances from './Balances'
import SettleUp from './SettleUp'

const TABS = [
  { id: 'expenses', label: 'Expenses' },
  { id: 'balances', label: 'Balances' },
  { id: 'settle', label: 'Settle up' },
]

function GroupPage({ groupId, onBack }) {
  const [group, setGroup] = useState(null)
  const [expenses, setExpenses] = useState([])
  const [balances, setBalances] = useState([])
  const [transfers, setTransfers] = useState([])
  const [tab, setTab] = useState('expenses')
  const [error, setError] = useState('')

  useEffect(() => {
    loadAll()
  }, [groupId])

  async function loadAll() {
    try {
      const [g, e, b, t] = await Promise.all([
        getGroup(groupId),
        getExpenses(groupId),
        getBalances(groupId),
        getSettleUp(groupId),
      ])
      setGroup(g)
      setExpenses(e)
      setBalances(b)
      setTransfers(t)
      setError('')
    } catch (err) {
      setError(err.message)
    }
  }

  if (!group) {
    return <p className="muted center">{error || 'loading...'}</p>
  }

  const total = expenses.reduce((sum, e) => sum + e.amountCents, 0)

  return (
    <section>
      <button className="link-btn" onClick={onBack}>← all groups</button>

      <div className="card group-header">
        <h2>{group.name}</h2>
        <div className="stats">
          <div>
            <span className="stat-num">{formatMoney(total)}</span>
            <span className="muted">spent together</span>
          </div>
          <div>
            <span className="stat-num">{expenses.length}</span>
            <span className="muted">expenses</span>
          </div>
        </div>

        <MemberList members={group.members} />
        <AddMemberForm groupId={groupId} members={group.members} onAdded={loadAll} />
      </div>

      {error && <p className="error">{error}</p>}

      <div className="tabs">
        {TABS.map((t) => (
          <button
            key={t.id}
            className={tab === t.id ? 'tab active' : 'tab'}
            onClick={() => setTab(t.id)}
          >
            {t.label}
          </button>
        ))}
      </div>

      {tab === 'expenses' && (
        <>
          {group.members.length < 2 ? (
            <p className="hint">Add at least 2 people to start adding expenses</p>
          ) : (
            <ExpenseForm groupId={groupId} members={group.members} onAdded={loadAll} />
          )}
          <ExpenseList groupId={groupId} expenses={expenses} members={group.members} onDeleted={loadAll} />
        </>
      )}

      {tab === 'balances' && <Balances balances={balances} />}

      {tab === 'settle' && <SettleUp transfers={transfers} members={group.members} />}
    </section>
  )
}

export default GroupPage
