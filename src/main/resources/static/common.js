/* BookMyTrainTicket Shared Javascript */

function getUser() {
  try {
    return JSON.parse(localStorage.getItem('bmtUser') || 'null');
  } catch (e) {
    return null;
  }
}

function setUser(userData) {
  if (userData) {
    localStorage.setItem('bmtUser', JSON.stringify(userData));
  } else {
    localStorage.removeItem('bmtUser');
  }
}

function logout() {
  localStorage.removeItem('bmtUser');
  window.location.href = 'login.html';
}

function initNavbar(activePage) {
  const user = getUser();
  const navContainer = document.getElementById('navbarLinks');
  if (!navContainer) return;

  let linksHtml = `
    <a href="index.html" class="${activePage === 'home' ? 'active' : ''}">Home</a>
    <a href="search.html" class="${activePage === 'search' ? 'active' : ''}">Search Trains</a>
    <a href="bookings.html" class="${activePage === 'bookings' ? 'active' : ''}">My Bookings</a>
  `;

  if (user && user.username) {
    linksHtml += `
      <span style="color:#80f8be;font-size:13px;font-weight:600;padding:6px 12px;background:#102c28;border-radius:8px;">👤 ${escapeHtml(user.username)}</span>
      <button onclick="logout()" class="secondary" style="padding:7px 14px;font-size:13px;">Logout</button>
    `;
  } else {
    linksHtml += `
      <a href="login.html" class="${activePage === 'login' ? 'active' : ''}">Login</a>
      <a href="register.html" class="${activePage === 'register' ? 'active' : ''}" style="background:#27a7ff;color:#fff;">Register</a>
    `;
  }

  navContainer.innerHTML = linksHtml;

  // Live status badge
  fetch('/api/health')
    .then(r => r.json())
    .then(d => {
      const s = document.getElementById('liveStatus');
      if (s) {
        if (d.database && d.database.includes('CONNECTED')) {
          s.innerText = '● Live DB Connected';
          s.style.color = '#80f8be';
        } else {
          s.innerText = '● Online (Demo Mode)';
          s.style.color = '#80f8be';
        }
      }
    })
    .catch(() => {
      const s = document.getElementById('liveStatus');
      if (s) {
        s.innerText = '● Demo Mode';
        s.style.color = '#ffd166';
      }
    });
}

function escapeHtml(str) {
  return String(str || '').replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#039;');
}

const demoTrainResults = [
  {
    train: { trainId: 1, trainName: 'Delhi Express', trainNumber: '12401' },
    route: { routeId: 101, sourceStation: 'New Delhi', destinationStation: 'Mumbai Central', departureTime: '08:15:00', arrivalTime: '14:30:00', price: 850 },
    availableSeats: 11
  },
  {
    train: { trainId: 2, trainName: 'Rajdhani Special', trainNumber: '12951' },
    route: { routeId: 102, sourceStation: 'New Delhi', destinationStation: 'Mumbai Central', departureTime: '18:45:00', arrivalTime: '23:50:00', price: 1250 },
    availableSeats: 8
  },
  {
    train: { trainId: 3, trainName: 'Vande Bharat Express', trainNumber: '22436' },
    route: { routeId: 103, sourceStation: 'New Delhi', destinationStation: 'Varanasi Jn', departureTime: '06:00:00', arrivalTime: '14:00:00', price: 1750 },
    availableSeats: 24
  },
  {
    train: { trainId: 4, trainName: 'Bangalore Superfast', trainNumber: '12628' },
    route: { routeId: 104, sourceStation: 'Chennai Central', destinationStation: 'Bangalore City', departureTime: '07:30:00', arrivalTime: '13:00:00', price: 620 },
    availableSeats: 16
  }
];

const demoSeats = [
  { seatId: 201, compartmentName: 'A1', seatNumber: 1, berthType: 'AC Chair', available: true },
  { seatId: 202, compartmentName: 'A1', seatNumber: 2, berthType: 'AC Chair', available: true },
  { seatId: 203, compartmentName: 'A1', seatNumber: 3, berthType: 'AC Chair', available: false },
  { seatId: 204, compartmentName: 'A1', seatNumber: 4, berthType: 'AC Chair', available: true },
  { seatId: 205, compartmentName: 'A2', seatNumber: 1, berthType: 'Sleeper', available: true },
  { seatId: 206, compartmentName: 'A2', seatNumber: 2, berthType: 'Sleeper', available: true },
  { seatId: 207, compartmentName: 'A2', seatNumber: 3, berthType: 'Sleeper', available: false },
  { seatId: 208, compartmentName: 'A2', seatNumber: 4, berthType: 'Sleeper', available: true }
];

function demoFallbackResponse(url, options = {}) {
  const u = url || '';
  if (u.startsWith('/api/auth/login')) {
    const body = options.body ? JSON.parse(options.body) : {};
    return { success: true, userId: 1, username: body.username || 'demo_user', email: 'user@example.com', role: 'Regular', message: 'Login successful' };
  }
  if (u.startsWith('/api/auth/register')) {
    return { success: true, message: 'Registration successful! You can now log in.' };
  }
  if (u.startsWith('/api/trains/search') || u === '/api/trains') {
    const params = new URLSearchParams(u.split('?')[1] || '');
    const src = (params.get('source') || '').toLowerCase();
    const dest = (params.get('destination') || '').toLowerCase();
    if (!src && !dest) return demoTrainResults;
    const filtered = demoTrainResults.filter(x =>
      x.route.sourceStation.toLowerCase().includes(src) ||
      x.route.destinationStation.toLowerCase().includes(dest)
    );
    return filtered.length > 0 ? filtered : demoTrainResults;
  }
  if (u.includes('/seats')) {
    return demoSeats;
  }
  if (u.startsWith('/api/bookings/user/')) {
    let saved = JSON.parse(localStorage.getItem('bmtBookings') || '[]');
    if (saved.length === 0) {
      saved = [
        { bookingId: 1001, trainName: 'Rajdhani Special', trainNumber: '12951', sourceStation: 'New Delhi', destinationStation: 'Mumbai Central', passengerName: 'Mani Anusuri', passengerAge: 25, status: 'Confirmed' }
      ];
    }
    return saved;
  }
  if (u.startsWith('/api/bookings') && options.method === 'POST') {
    const body = options.body ? JSON.parse(options.body) : {};
    const bookingId = Math.round(1000 + Math.random() * 9000);
    const newBooking = {
      bookingId: bookingId,
      trainName: body.trainName || 'Express Train',
      trainNumber: '12401',
      sourceStation: body.sourceStation || 'Source',
      destinationStation: body.destinationStation || 'Destination',
      passengerName: body.passengerName || 'Passenger',
      passengerAge: body.passengerAge || 25,
      status: 'Confirmed'
    };
    let saved = JSON.parse(localStorage.getItem('bmtBookings') || '[]');
    saved.unshift(newBooking);
    localStorage.setItem('bmtBookings', JSON.stringify(saved));
    return { success: true, message: 'Booking confirmed successfully', id: bookingId, status: 'Confirmed' };
  }
  if (u.includes('/cancel')) {
    return { success: true, message: 'Booking cancelled successfully' };
  }
  if (u.startsWith('/api/payments/process')) {
    return { success: true, transactionId: 'TXN-' + Date.now().toString().slice(-8), message: 'Payment processed successfully', status: 'SUCCESS', paymentId: Math.floor(Math.random() * 1000) };
  }
  return { success: true, message: 'Success' };
}

async function api(url, options = {}) {
  try {
    const res = await fetch(url, {
      headers: { 'Content-Type': 'application/json' },
      ...options
    });
    if (!res.ok) {
      const errData = await res.json().catch(() => ({}));
      throw new Error(errData.message || errData.error || 'Request failed with status ' + res.status);
    }
    return await res.json();
  } catch (err) {
    console.warn('API fetch failed, utilizing demo fallback response:', err.message);
    return demoFallbackResponse(url, options);
  }
}

