function Header({ onLogoClick }) {
  return (
    <header className="header">
      <button className="logo" onClick={onLogoClick}>
        Split<span>Mate</span>
      </button>
      <svg className="squiggle" viewBox="0 0 160 14" aria-hidden="true">
        <path d="M2 9 C 20 1, 30 13, 48 6 S 80 2, 96 8 S 130 13, 158 4" />
      </svg>
      <p className="tagline">who owes who, without the drama</p>
      <span className="sticker">beta!</span>
    </header>
  )
}

export default Header
