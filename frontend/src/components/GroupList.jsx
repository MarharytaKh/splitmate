import { useEffect, useState } from 'react'
import { createGroup, getGroups } from '../api'
import { AvatarStack } from './Avatar'

function GroupList({ onOpen }) {
  const [groups, setGroups] = useState([])
  const [name, setName] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    loadGroups()
  }, [])

  async function loadGroups() {
    try {
      const data = await getGroups()
      setGroups(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleCreate(e) {
    e.preventDefault()
    setError('')
    try {
      const group = await createGroup(name)
      setName('')
      onOpen(group.id)
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <section>
      <div className="card hero">
        <h2>Your groups</h2>
        <p className="muted">Trips, flats, dinners... create a group and start splitting.</p>
        <form className="row" onSubmit={handleCreate}>
          <input
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="e.g. Trip to Zakopane"
          />
          <button className="btn" type="submit">+ Create</button>
        </form>
        {error && <p className="error">{error}</p>}
      </div>

      {loading && <p className="muted center">loading...</p>}

      {!loading && groups.length === 0 && (
        <p className="hint">No groups yet. Make the first one above ☝️</p>
      )}

      <ul className="group-list">
        {groups.map((g) => (
          <li key={g.id}>
            <button className="card group-card" onClick={() => onOpen(g.id)}>
              <div className="group-card-text">
                <h3>{g.name}</h3>
                <span className="muted">
                  {g.members.length} {g.members.length === 1 ? 'person' : 'people'}
                </span>
              </div>
              <AvatarStack members={g.members} />
              <span className="arrow">→</span>
            </button>
          </li>
        ))}
      </ul>
    </section>
  )
}

export default GroupList
