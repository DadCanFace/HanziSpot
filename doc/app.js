
const search = document.querySelector('#search');
const rows = [...document.querySelectorAll('#pair-table tbody tr')];
const normalize = value => value.normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase();
search.addEventListener('input', () => {
  const query = normalize(search.value.trim());
  let count = 0;
  rows.forEach(row => { row.hidden = !normalize(row.textContent).includes(query); if (!row.hidden) count++; });
  document.querySelector('#result-count').textContent = `${count} ${count === 1 ? 'pair' : 'pairs'}`;
  document.querySelector('#no-results').hidden = count !== 0;
});
document.querySelectorAll('.copy').forEach(button => button.addEventListener('click', async () => {
  try {
    if (!navigator.clipboard) throw new Error('Clipboard unavailable');
    await navigator.clipboard.writeText(button.dataset.copy);
    button.textContent = 'Copied';
    document.querySelector('#copy-status').textContent = 'Build command copied.';
    setTimeout(() => button.textContent = 'Copy', 1800);
  } catch {
    const selection = window.getSelection();
    const range = document.createRange();
    range.selectNodeContents(button.parentElement.nextElementSibling);
    selection.removeAllRanges(); selection.addRange(range);
    document.querySelector('#copy-status').textContent = 'Command selected. Press Ctrl+C or Command+C to copy.';
    button.textContent = 'Selected';
  }
}));
const grid = document.querySelector('#demo-grid');
const status = document.querySelector('#demo-status');
function resetDemo() {
  grid.replaceChildren();
  status.textContent = 'Look closely at the strokes.';
  const target = Math.floor(Math.random() * 9);
  for (let i = 0; i < 9; i++) {
    const button = document.createElement('button');
    button.textContent = i === target ? '學' : '学';
    button.lang = i === target ? 'zh-Hant' : 'zh-Hans';
    button.setAttribute('aria-label', `Row ${Math.floor(i / 3) + 1}, column ${i % 3 + 1}: ${button.textContent}`);
    button.addEventListener('click', () => {
      if (i === target) {
        button.classList.add('correct');
        status.textContent = 'You found it! 學 → 学 · xué · to learn.';
        grid.querySelectorAll('button').forEach(tile => tile.disabled = true);
      } else {
        button.classList.add('wrong'); button.disabled = true;
        status.textContent = 'That is Simplified. Look for the Traditional character.';
      }
    });
    grid.append(button);
  }
}
resetDemo();
document.querySelector('#demo-reset').addEventListener('click', resetDemo);
if ('IntersectionObserver' in window) {
  const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => { if (entry.isIntersecting) {
      document.querySelectorAll('nav a').forEach(a => {
        const active = a.getAttribute('href') === `#${entry.target.id}`;
        a.classList.toggle('active', active);
        if (active) a.setAttribute('aria-current', 'location'); else a.removeAttribute('aria-current');
      });
    }});
  }, {rootMargin: '-15% 0px -65% 0px'});
  document.querySelectorAll('main section').forEach(section => observer.observe(section));
}
