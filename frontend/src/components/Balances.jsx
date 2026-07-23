import { formatMoney } from '../utils/money'
import Avatar from './Avatar'

function Balances({ balances }) {
  return (
    <ul className="card balances">
      {balances.map((b) => (
        <li key={b.memberId}>
          <Avatar number={b.avatar} name={b.name} />
          <div className="balance-main">
            <div className="balance-top">
              <strong>{b.name}</strong>
              <span className={b.balanceCents >= 0 ? 'plus' : 'minus'}>
                {formatMoney(b.balanceCents)}
              </span>
            </div>
          </div>
        </li>
      ))}
    </ul>
  )
}

export default Balances
