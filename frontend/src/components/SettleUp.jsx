import { formatMoney } from '../utils/money'
import Avatar from './Avatar'

function SettleUp({ transfers }) {
  if (transfers.length === 0) {
    return <p className="hint">Nothing to settle</p>
  }

  return (
    <ul className="transfers">
      {transfers.map((t, i) => (
        <li key={i} className="card transfer">
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
  )
}

export default SettleUp
