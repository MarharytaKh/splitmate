import { useState } from 'react'
import { addMember } from '../api'

const AVATARS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

function AddMemberForm({ groupId, onAdded }) {
  const [open, setOpen] = useState(false)
  const [name, setName] = useState('')
  const [avatar, setAvatar] = useState(1)
  const [error, setError] = useState('')

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    try {
      await addMember(groupId, name, avatar)
      setName('')
      setOpen(false)
      onAdded()
    } catch (err) {
      setError(err.message)
    }
  }

  if (!open) {
    return (
      <button className="btn btn-outline" onClick={() => setOpen(true)}>+ Add person</button>
    )
  }

  return (
    <form className="add-member" onSubmit={handleSubmit}>
      <div className="row">
        <input
          autoFocus
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Name"
        />
        <button className="btn" type="submit">Add</button>
        <button className="btn btn-ghost" type="button" onClick={() => setOpen(false)}>
          Cancel
        </button>
      </div>

      <p className="label">Pick a face</p>
      <div className="avatar-picker">
        {AVATARS.map((a) => (
          <button
            type="button"
            key={a}
            className={a === avatar ? 'pick selected' : 'pick'}
            onClick={() => setAvatar(a)}
          >
            <img src={`/avatars/${a}.png`} alt={`avatar ${a}`} />
          </button>
        ))}
      </div>
      {error && <p className="error">{error}</p>}
    </form>
  )
}

export default AddMemberForm
