function Avatar({ number, name, size = 44 }) {
  return (
    <img
      className="avatar"
      src={`/avatars/${number || 1}.png`}
      alt={name || 'avatar'}
      title={name}
      width={size}
      height={size}
    />
  )
}

export function AvatarStack({ members, max = 5 }) {
  const shown = members.slice(0, max)
  const rest = members.length - shown.length

  return (
    <div className="avatar-stack">
      {shown.map((m) => (
        <Avatar key={m.id} number={m.avatar} name={m.name} size={34} />
      ))}
      {rest > 0 && <span className="more">+{rest}</span>}
    </div>
  )
}

export default Avatar
