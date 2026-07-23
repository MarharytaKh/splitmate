import { useState } from 'react'
import Header from './components/Header'
import GroupList from './components/GroupList'
import GroupPage from './components/GroupPage'

function App() {
  const [groupId, setGroupId] = useState(null)

  return (
    <div className="app">
      <Header onLogoClick={() => setGroupId(null)} />

      <main>
        {groupId ? (
          <GroupPage groupId={groupId} onBack={() => setGroupId(null)} />
        ) : (
          <GroupList onOpen={setGroupId} />
        )}
      </main>

      <footer className="footer">splitmate · a small student project</footer>
    </div>
  )
}

export default App
