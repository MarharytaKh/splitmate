import { formatMoney } from '../utils/money'
import Avatar from './Avatar'

function SettleUp({ transfers, members }) {
  if (transfers.length === 0) {
    return (
      <div className="card all-good">
        <div className="all-good-faces">
          {members.slice(0, 6).map((m) => (
            <Avatar key={m.id} number={m.avatar} name={m.name} size={40} />
          ))}
        </div>
        <h3>Everyone is even 🎉</h3>
        <p className="muted">Nothing to pay back right now.</p>
      </div>
    )
  }

  return (
    <div>
      <p className="muted">
        {transfers.length} {transfers.length === 1 ? 'transfer' : 'transfers'} and everyone is even:
      </p>
      <ul className="transfers">
        {transfers.map((t, i) => (
          <li key={`${t.from.id}-${t.to.id}-${i}`} className="card transfer">
            <Avatar number={t.from.avatar} name={t.from.name} />
            <div className="transfer-text">
              <span>
                <strong>{t.from.name}</strong> pays <strong>{t.to.name}</strong>
              </span>
              <span className="transfer-amount">{formatMoney(t.amountCents)}</span>
            </div>
            <span className="transfer-arrow">→</span>
            <Avatar number={t.to.avatar} name={t.to.name} />
          </li>
        ))}
      </ul>
    </div>
  )
}

export default SettleUp
