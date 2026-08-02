export function formatMoney(cents) {
  return (Math.abs(cents) / 100).toFixed(2) + ' zł'
}

// "12,5" -> 1250, "abc" -> NaN
export function parseMoney(text) {
  const value = parseFloat(text.replace(',', '.'))
  return Math.round(value * 100)
}
