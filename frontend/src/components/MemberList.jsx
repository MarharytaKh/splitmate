import Avatar from './Avatar'

function MemberList({ members }) {
  if (members.length === 0) {
    return <p className="hint">Nobody here yet, add your friends 👇</p>
  }

  return (
    <ul className="members">
      {members.map((m) => (
        <li key={m.id}>
          <Avatar number={m.avatar} name={m.name} size={56} />
          <span>{m.name}</span>
        </li>
      ))}
    </ul>
  )
}

export default MemberList
