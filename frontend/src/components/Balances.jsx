import { formatMoney } from '../utils/money'
import Avatar from './Avatar'

function Balances({ balances }) {
  if (balances.length === 0) {
    return <p className="hint">Add people first</p>
  }

  // biggest number decides how long the bars are
  const max = Math.max(...balances.map((b) => Math.abs(b.balanceCents)), 1)

  return (
    <ul className="card balances">
      {balances.map((b) => {
        let status = 'settled up'
        if (b.balanceCents > 0) status = 'gets back'
        if (b.balanceCents < 0) status = 'owes'

        return (
          <li key={b.memberId}>
            <Avatar number={b.avatar} name={b.name} />
            <div className="balance-main">
              <div className="balance-top">
                <strong>{b.name}</strong>
                <span className={b.balanceCents >= 0 ? 'plus' : 'minus'}>
                  {status} {b.balanceCents !== 0 && formatMoney(b.balanceCents)}
                </span>
              </div>
              <div className="bar">
                <div
                  className={b.balanceCents >= 0 ? 'bar-fill plus-bg' : 'bar-fill minus-bg'}
                  style={{ width: `${(Math.abs(b.balanceCents) / max) * 100}%` }}
                />
              </div>
            </div>
          </li>
        )
      })}
    </ul>
  )
}

export default Balances
